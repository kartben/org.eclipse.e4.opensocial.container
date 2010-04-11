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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.ui.web.BrowserRPCHandler;

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

	public Map<String, String> getJavascriptSources() {
		Map<String, String> deps = new HashMap<String, String>();
		for (IConfigurationElement ice : _ice.getChildren("script")) {
			StringBuffer sb = new StringBuffer();
			BufferedReader scriptReader = null;
			try {
				String sourceLocation = ice.getAttribute("source");
				InputStream jsSourceStream = Platform.getBundle(
						ice.getNamespaceIdentifier()).getEntry(sourceLocation)
						.openStream();
				scriptReader = new BufferedReader(new InputStreamReader(
						jsSourceStream));
				String line = null;
				while ((line = scriptReader.readLine()) != null) {
					sb.append(line + "\r\n");
				}
				deps.put(sourceLocation, sb.toString());
			} catch (InvalidRegistryObjectException e) {
				// TODO log correctly
			} catch (IOException e) {
				// TODO log correctly
			} finally {
				if (scriptReader != null)
					try {
						scriptReader.close();
					} catch (IOException e) {
					}
			}
		}
		return deps;
	}

	public List<String> getDependencies() {
		List<String> deps = new ArrayList<String>();
		for (IConfigurationElement ice : _ice.getChildren("dependsOn")) {
			deps.add(ice.getAttribute("feature"));
		}
		return deps;
	}

	public Map<String, BrowserRPCHandler> getBrowserRPCHandlers() {
		Map<String, BrowserRPCHandler> handlers = new HashMap<String, BrowserRPCHandler>();
		for (IConfigurationElement ice : _ice.getChildren("browserRPCHandler")) {
			try {
				String name = ice.getAttribute("name");
				BrowserRPCHandler handler;
				handler = (BrowserRPCHandler) ice
						.createExecutableExtension("class");
				handlers.put(name, handler);
			} catch (CoreException e) {
				// TODO log RPC handler instantiation error
			}
		}
		return handlers;
	}
}
