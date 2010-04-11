/**
 * Copyright (c) 2010 Sierra Wireless Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *   Contributors:
 *      Benjamin Cabe, Sierra Wireless - initial API and implementation
 */
package org.eclipse.e4.opensocial.container.modules;

import org.eclipse.e4.opensocial.container.internal.ui.OpenSocialBrowser;
import org.eclipse.e4.opensocial.model.Module;

public interface ModuleManager {
	public class ModuleNotFoundException extends Exception {
		private static final long serialVersionUID = 5067845322975035064L;
	}

	void addDeployedModule(Module module, int moduleId,
			OpenSocialBrowser browser);

	void removeDeployedModule(int moduleId);

	OpenSocialBrowser getOpenSocialBrowser(int moduleId)
			throws ModuleNotFoundException;
}
