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
package org.eclipse.e4.opensocial.container.internal.browserHandlers.pubsub;

import org.eclipse.e4.ui.web.BrowserRPCHandler;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;

public abstract class AbstractPubSubHandler implements BrowserRPCHandler {
	public final static String MODULEID_EVENT_PROPERTY = "org.eclipse.e4.opensocial.moduleId";

	protected EventAdmin getEventAdmin() {
		ServiceReference sr = getBundleContext().getServiceReference(
				EventAdmin.class.getName());
		return (EventAdmin) getBundleContext().getService(sr);
	}

	protected BundleContext getBundleContext() {
		return FrameworkUtil.getBundle(this.getClass()).getBundleContext();
	}
}
