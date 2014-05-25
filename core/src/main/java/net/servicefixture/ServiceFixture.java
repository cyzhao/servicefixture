/*
 * Copyright 2006 (C) Chunyun Zhao(Chunyun.Zhao@gmail.com).
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *	http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License.
 */
package net.servicefixture;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.servicefixture.context.GlobalContext;
import net.servicefixture.context.TableContext;
import net.servicefixture.parser.RequestParser;
import net.servicefixture.parser.TreeParser;
import net.servicefixture.parser.TreeParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fit.Fixture;
import fit.Parse;

/**
 * The fixture invokes the backend service or pojo with complex domain objects 
 * as request. The backend service could be a web service, stateless session bean,
 * and pojo etc..
 * <p>
 * The complex domain objects are represented in the test table using expression
 * language.
 * 
 * <p>
 * The test table must define following row types and in the right order:
 * <ul>
 * <li>set: prepares the request for the invocation.</li>
 * <li>invoke: invokes te backend service.</li>
 * <li>check: checks or displays the response data.</li>
 * </ul>
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public abstract class ServiceFixture extends Fixture {
    private static Log log = LogFactory.getLog(ServiceFixture.class);
    
    private TreeParser treeParser;

    private ResponseChecker responseChecker;

    /** The test table key used to store the request and response */
    private String testTableKey;

    private String treeParserClassName;

    private boolean stopOnError = true;
    
    private boolean errorOccurred = false;
    /**
     * Stores current row that is under process.
     */
    protected Parse currentRow;
    
    private String[] requestArgumentNames;

    private static final String EXCEPTION_TOKEN = "exception";
    
    /**
     * The hook to do initialization before fixture is executed.
     */
    public void setUp(Parse table) {
    }

    /**
     * The hook to do clean up after fixture is executed.
     */
    public void tearDown() {
    }

    /*
     * @see fit.Fixture#doTable(fit.Parse)
     */
    public final void doTable(Parse table) {
        try {
        	this.setUp(table);
        	super.doTable(table);
        } finally {
        	GlobalContext.clearLocalResponse();
        	this.tearDown();
        }
    }

    public final void doCells(Parse parse) {
    	if ( stopOnError && errorOccurred ) {
    		return;
    	}
        currentRow = parse;
        try {
            Method method;
			try {
				List<String> argList = new ArrayList<String>();
				//Looks for method that takes array of String.
				method = getClass().getMethod(parse.text(),
						new Class[] { String[].class });
				for (Parse cell = parse.more; cell != null; cell = cell.more) {
					String cellData = cell.text();
					argList.add(cellData);
				}
				method.invoke(this, new Object[] { argList
						.toArray(new String[argList.size()]) });
			} catch (NoSuchMethodException e) {
				List<Parse> argList = new ArrayList<Parse>();
				//Now looking for method that takes array of Parse. Usually the
				//method needs to manipulate Parses' content.
				method = getClass().getMethod(parse.text(),
						new Class[] { Parse[].class });
				for (Parse cell = parse.more; cell != null; cell = cell.more) {
					argList.add(cell);
				}
				method.invoke(this, new Object[] { argList
						.toArray(new Parse[argList.size()]) });
			}
        } catch (Exception exception) {
        	errorOccurred = true;
            exception.printStackTrace();
            exception(parse, exception);
        }
    }

    /**
     * Explicitly specify the request data tree parser clas.
     */
    public final void parser(String[] dataCells) {
        if (dataCells.length > 0) {
            treeParserClassName = dataCells[0];
        }
    }

    /**
     * Prepares the data with current cells.
     */
    public final void set(Parse[] dataCells) {
        if (treeParser == null) {
            treeParser = TreeParserFactory
                    .createTreeParser(treeParserClassName);
        }
        treeParser.addDataRow(dataCells);
    }

    /**
     * Sets the test table key.
     */
    public final void key(String[] dataCells) {
        testTableKey = dataCells[0];
    }

    /**
     * Whether or not to stop test execution when an error occurred.
     * If this row doesn't exist, default value of stopOnError is set
     * to true.
     */
    public final void stopOnError(String[] dataCells) {
    	stopOnError = Boolean.valueOf(dataCells[0]).booleanValue();
    }

    /**
     * Invokes the service.
     */
    public final void invoke(String[] dataCells) {
        RequestParser requestParser = new RequestParser(getServiceOperation());

        //If request data tree parser is not available, no set rows have
        //been provided, create the empty tree, which will then produce 
        //the empty request arguments.
        if (treeParser == null) {
            treeParser = TreeParserFactory
            .createTreeParser(treeParserClassName);
        }

        requestParser.parse(treeParser.getTree());
        Object[] arguments = requestParser.getRequestArguments();
        setRequestArgumentNames(requestParser.getRequestArgumentNames());
        
        TableContext context = new TableContext();
        context.setRequest(arguments);
        GlobalContext.storeTableContext(testTableKey, context);
        Object response = null;
        long startTime = System.currentTimeMillis();
        try {
            response = invoke(arguments);
        } catch (Throwable e) {
            handleException(e, dataCells);
            return;
        } finally {
        	long elapseTime = (System.currentTimeMillis() - startTime);
        	context.setElapse(elapseTime);
        	GlobalContext.putVar("elapseTime", elapseTime);
        }
        context.setResponse(response);
        
        if (expectingException(dataCells)) {
            wrong(currentRow.more, "No exception is thrown.");
        } else {
            responseChecker = new ResponseChecker(this);
            responseChecker.setResponse(response);
        }
    }
    
    /**
     * 
     * Handles the exception thrown from service operation invocation.
     *
     * @throws ServiceFixtureException when the exception is not expected.
     */
    private void handleException(Throwable e, String[] dataCells) {
        while ( e instanceof InvocationTargetException ) {
            e = ((InvocationTargetException)e).getTargetException();
        }
        
        e = unWrapException(e);
        
        if (expectingException(dataCells)) {
            responseChecker = new ResponseChecker(this);
            responseChecker.setException(e);
            right(currentRow.more);
            log.error(e);
            return;
        }
        throw new ServiceFixtureException("Unable to invoke the operation.", e);
    }
    
    /**
     * Overwrite this method to unwrap the exception.
     *
     * @param e
     * @return
     */
    protected Throwable unWrapException(Throwable e) {
        return e;
    }

    private boolean expectingException(String[] dataCells) {
        if (dataCells.length > 0) {
           return EXCEPTION_TOKEN.equalsIgnoreCase(dataCells[0]);
        }
        return false;
    }

    /**
     * Verifies the output.
     */
    public final void check(String[] dataCells) {
        if (dataCells.length != 2) {
            throw new ServiceFixtureException(
                    "Expression and expected value must be supplied.");
        }
        if (responseChecker != null) {
            responseChecker.check(dataCells, currentRow.more.more);
        }
    }

    /**
     * Overrides the fit.Fixture#isFriendlyException(Throwable) to not print
     * full stacktrace for ServiceFixtureException in the result table.
     */
    public boolean isFriendlyException(Throwable throwable) {
        return super.isFriendlyException(throwable)
                || (throwable instanceof ServiceFixtureException);
    }

    /**
     * Returns the service operation Method which will be needed to build the
     * request data.
     */
    public abstract Method getServiceOperation();

    /**
     * Will be invoked when the invoke row is encountered in the test table with
     * arguments built from set rows.
     */
    public abstract Object invoke(Object[] args) throws Exception;
    
    //Load the plugins.
    static {
    	PluginManager.loadPlugins();
    }

	public String[] getRequestArgumentNames() {
		return requestArgumentNames;
	}

	public void setRequestArgumentNames(String[] requestArgumentNames) {
		this.requestArgumentNames = requestArgumentNames;
	}
}
