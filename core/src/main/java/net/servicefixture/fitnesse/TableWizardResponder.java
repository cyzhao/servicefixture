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

import org.apache.commons.lang.StringUtils;

import fitnesse.components.ClassPathBuilder;
import fitnesse.wiki.WikiPage;

/**
 * The Fitnesse responder that uses ServiceFixture FixtureTemplateCreator to generate
 * test table template for ServiceFixture.
 * <p>
 * To configure this TableWizardResponder in fitnesse, add following line to 
 * plugins.properties:
 * <p>
 * <code>
 * 		Responders=tableWizard:net.servicefixture.fitnesse.TableWizardResponder
 * </code>
 *  
 * @author Chunyun Zhao
 * @since  Mar 20, 2006
 *
 */
public class TableWizardResponder extends fitnesse.responders.editing.TableWizardResponder {
	private static final String DEFAULT_TEMPLATE_COMMAND_PATTERN = "java -D" + FixtureTemplateCreator.TEMPLATE_CREATOR_FLAG + "=yes -cp %p net.servicefixture.fitnesse.FixtureTemplateCreator %m";

	/* 
	 * @see fitnesse.responders.editing.TableWizardResponder#createCommandLine(fitnesse.wiki.WikiPage, java.lang.String)
	 */
	protected String createCommandLine(WikiPage wikipage, String fixtureClass)
			throws Exception {
		String classpath = (new ClassPathBuilder()).getClasspath(wikipage);
		String templateRunner = wikipage.getData().getVariable("TEMPLATE_COMMAND_PATTERN");
		if(templateRunner == null)
			templateRunner = DEFAULT_TEMPLATE_COMMAND_PATTERN;
		String command = StringUtils.replace(templateRunner, "%p", classpath);
		command = StringUtils.replace(command, "%m", fixtureClass);
		return command;
	}

}
