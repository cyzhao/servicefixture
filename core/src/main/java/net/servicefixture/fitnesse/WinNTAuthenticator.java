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

import java.util.Properties;

import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbSession;
import fitnesse.authentication.Authenticator;

/**
 * Authenticates the user to NT domain controller. Use this authenticator
 * to allow NT users to access a secured fitnesse site.
 * <p>
 * To configure this Authenticator in fitnesse, add following lines to 
 * plugins.properties:
 * <p>
 * <code>
 * 	Authenticator=net.servicefixture.fitnesse.WinNTAuthenticator
 *	net.servicefixture.fitnesse.domainController=<Domain Controller IP or Host Name>
 * 	net.servicefixture.fitnesse.domain=<NT Domain Name>
 * </code>
 * 
 * Also add the servicefixture jar and jcifs-1.2.7.jar to the classpath in fitnesse 
 * startup script.
 * 
 * @author Chunyun Zhao
 * @since  Jan 13, 2006
 *
 */
public class WinNTAuthenticator extends Authenticator {
    private static final String DOMAIN_CONTROLLER_PROP = "net.servicefixture.fitnesse.domainController";
    private static final String DOMAIN_PROP = "net.servicefixture.fitnesse.domain";
    private String domainController;
    private String domain;
    
    public WinNTAuthenticator(Properties props) {
        domainController = props.getProperty(DOMAIN_CONTROLLER_PROP);
        domain = props.getProperty(DOMAIN_PROP);
        
        if (domain == null || domainController == null) {
            throw new RuntimeException("You must configure " + DOMAIN_CONTROLLER_PROP + " and " + DOMAIN_PROP);
        }
    }
    
    /* 
     * @see fitnesse.authentication.Authenticator#isAuthenticated(java.lang.String, java.lang.String)
     */
    public boolean isAuthenticated(String user, String password) throws Exception {
        UniAddress dc = UniAddress.getByName(domainController);
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication( domain, user, password );
        try {
            SmbSession.logon( dc, auth );
            return true;
        } catch (SmbException e) {
            return false;
        }
    }
}
