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
package net.servicefixture.fitnesse;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;

import fitnesse.components.CommandLine;

/**
 * @author Chunyun Zhao
 * @since  Oct 17, 2006
 */
public class TestRunner {
	private static final String SUITE_TYPE = "suite";
	private static final String TEST_TYPE = "test";
	
	private static final String TEST_RESULT_BEGINNING_MARKER = "innerHTML = \"";
	private static final String TEST_RESULT_ENDING_MARKER = "\"";
	
	private String host;
	private String port;
	private String page;
	private String testType = TEST_TYPE;
	private String resultHtmlFile;
	private String baseUrl;
	private String testUrl;
	
	private static boolean verbose;
	
	public static void main(String[] args) throws IOException {
		TestRunner testRunner = new TestRunner();
		testRunner.executeTest(args);
	}
	
	private void executeTest(String[] args) throws IOException {
		parseArgs(args);
		
		baseUrl = "http://" + host + ":" + port + "/";
		testUrl = baseUrl + page + "?" + testType;
		GetMethod get = new GetMethod(testUrl);
		try {
			HttpClient client = new HttpClient();
			int status;
			try {
				verbose("Executing fitnesse test: " + testUrl);
				status = client.executeMethod(get);
			} catch (Exception e) {
				throw new RuntimeException("Unable to execute fitnesse test:" + testUrl, e);
			}
			
			if ( status != -1 ) {
				String response = get.getResponseBodyAsString();
				printTestSummary(response);
				writeToResultHtmlFile(response);
				verbose("Fitnesse test completed successfully.");
			} else {
				throw new RuntimeException("Unable to execute fitnesse test:" + testUrl + ", httpclient status:" + status);
			}
		} finally {
			get.releaseConnection();
		}		
	}
	
	
	private void printTestSummary(String response) {
		if ( verbose ) {
			int fromIndex = response.indexOf(TEST_RESULT_BEGINNING_MARKER) + TEST_RESULT_BEGINNING_MARKER.length();
			if ( fromIndex > 0 ) {
				int endIndex = response.indexOf(TEST_RESULT_ENDING_MARKER, fromIndex);
				String testResults = response.substring(fromIndex, endIndex);
				System.out.println(stripHtmlInfo(testResults));
			}
		}
	}


	private String stripHtmlInfo(String testResults) {
		testResults = StringUtils.replace(testResults, "&nbsp;", " ");
		StringBuilder builder = new StringBuilder();
		boolean skip = false;
		for (int i = 0; i < testResults.length(); i++) {
			char c = testResults.charAt(i);
			switch (c) {
			case '<':
				skip = true;
				break;
			case '>':
				skip = false;
				break;
			default:
				if ( !skip ) {
					builder.append(c);
				}
				break;
			}
		}
		return builder.toString();
	}

	private void verbose(String message) {
		if ( verbose ) {
			System.out.println(message);
		}
	}

	private void writeToResultHtmlFile(String response) throws IOException {
		FileWriter fileWriter = null;
		try {
			verbose("Writing test report to file: " + resultHtmlFile);
			fileWriter = new FileWriter(resultHtmlFile);
			StringBuilder builder = new StringBuilder();
			int index = response.indexOf("<head>");
			if ( index > 0 ) {
				builder.append(response.substring(0, index + 6));
				builder.append("\n\t\t<base href=\"").append(baseUrl).append("\"/>");
				builder.append(response.substring(index + 6));
				fileWriter.write(builder.toString());
			} else {
				fileWriter.write(response);
			}
		} finally {
			if ( fileWriter != null ) {
				fileWriter.close();
			}
		}
	}
	
	private void parseArgs(String[] args) {
        CommandLine commandline = new CommandLine("[-v] [-suite] host port pageName resultHtmlFile");
        if(!commandline.parse(args)) {
            usage();
        }
        host = commandline.getArgument("host");
        port = commandline.getArgument("port");
        page = commandline.getArgument("pageName");
        resultHtmlFile = commandline.getArgument("resultHtmlFile");
        if(commandline.hasOption("suite")) {
        	testType = SUITE_TYPE;
        }
        if(commandline.hasOption("v")) {
        	verbose = true;
        }
	}	
    private void usage()
    {
    	System.out.println("usage: java net.servicefixture.fitnesse.TestRunner -v -suite host port pageName result-html-file");
        System.out.println("\t-v \tverbose: prints test progress to stdout");
        System.out.println("\t-suite \tsuite: executes the test suite. If not specified, executes the test.");
    	System.exit(-1);
    }	
}
