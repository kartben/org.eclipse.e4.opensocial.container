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
package org.eclipse.e4.opensocial.container.internal.features;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;

public class Feature {

	private String _name;
	private String _version;
	private IConfigurationElement _ice;

	/**
	 * @param version
	 * @param name
	 * @param ice
	 */
	public Feature(String name, String version, IConfigurationElement ice) {
		_name = name;
		_version = version;
		_ice = ice;
	}

	public String getName() {
		return _name;
	}

	public String getVersion() {
		return _version;
	}

	public List<String> getDependencies() {
		List<String> deps = new ArrayList<String>();
		for (IConfigurationElement ice : _ice.getChildren("dependsOn")) {
			deps.add(ice.getAttribute("feature"));
		}
		return deps;
	}
}
