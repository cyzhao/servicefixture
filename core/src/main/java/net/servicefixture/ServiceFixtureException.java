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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The generic exception. Will be thrown if an exception is not
 * recoverable.
 * 
 * @author Chunyun Zhao
 * @since Dec 21, 2005
 */
public class ServiceFixtureException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(ServiceFixtureException.class);

    public ServiceFixtureException() {
    }

    public ServiceFixtureException(String message) {
        super(message);
    }

    public ServiceFixtureException(String message, Throwable cause) {
        super(message, cause);
        log.error(message, cause);
    }

    public ServiceFixtureException(Throwable cause) {
        super(cause);
        log.error(null, cause);
    }

    public String getMessage() {
        return getCause() != null ? super.getMessage() + ". Nested exception:"
                + getCause().toString() : super.getMessage();
    }
}
