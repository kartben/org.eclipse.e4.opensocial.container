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
package org.eclipse.e4.opensocial.container.internal.util;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.e4.opensocial.container.internal.features.Feature;
import org.eclipse.e4.opensocial.container.resolver.ModuleResolver;
import org.eclipse.e4.opensocial.container.resolver.ModuleResolver.UnresolvedException;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.e4.opensocial.model.Type;
import org.eclipse.e4.ui.web.BrowserRPC;
import org.eclipse.e4.ui.web.BrowserRPCHandler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

/**
 * @author Benji
 * 
 */
public class BrowserUtils {
	public static String generateHtml(Module module) throws UnresolvedException {
		StringBuilder sb = new StringBuilder();

		sb.append("<html>\r\n");
		sb.append("<head>\r\n</head>\r\n");
		sb.append("<body>\r\n");

		appendContainerScripts(module, sb);

		appendModuleContent(module, sb);

		sb.append("</body>\r\n");
		sb.append("</html>\r\n");

		return sb.toString();
	}

	private static void appendContainerScripts(Module module, StringBuilder sb)
			throws UnresolvedException {
		sb.append("<script>\r\n");

		List<Feature> features = getModuleResolver().resolve(module);

		for (Feature feature : features) {
			sb.append("/********** Scripts for feature: '" + feature.getName()
					+ "' *********/\r\n");
			for (Entry<String, String> script : feature.getJavascriptSources()
					.entrySet()) {
				sb.append("// - " + script.getKey() + "\r\n");
				sb.append(script.getValue() + "\r\n");
			}
		}
		sb.append("</script>\r\n");
	}

	private static void appendModuleContent(Module module, StringBuilder sb) {
		if (module.getContent().get(0).getType() == Type.HTML) {
			sb.append(module.getContent().get(0).getValue());
		}

	}

	public static void registerHandlers(Module module, BrowserRPC _browserRPC)
			throws UnresolvedException {
		List<Feature> features = getModuleResolver().resolve(module);
		for (Feature feature : features) {
			for (Entry<String, BrowserRPCHandler> handlerEntry : feature
					.getBrowserRPCHandlers().entrySet()) {
				_browserRPC.addRPCHandler(handlerEntry.getKey(), handlerEntry
						.getValue());
			}
		}

	}

	private static ModuleResolver getModuleResolver() {
		BundleContext bundleContext = FrameworkUtil.getBundle(
				BrowserUtils.class).getBundleContext();
		ServiceReference sr = bundleContext
				.getServiceReference(ModuleResolver.class.getName());
		ModuleResolver resolver = (ModuleResolver) bundleContext.getService(sr);
		return resolver;
	}

}
