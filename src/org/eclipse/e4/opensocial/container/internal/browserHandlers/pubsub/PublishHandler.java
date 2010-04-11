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

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.swt.browser.Browser;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;

public class PublishHandler extends AbstractPubSubHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.web.BrowserRPCHandler#handle(org.eclipse.swt.browser
	 * .Browser, java.lang.Object[])
	 */
	@Override
	public Object handle(Browser browser, Object[] arguments) {
		Dictionary<String, Object> properties = new Hashtable<String, Object>();
		properties.put(EventConstants.MESSAGE, arguments[2]);
		// TODO
		// properties.put(MODULEID_EVENT_PROPERTY, 1234);
		getEventAdmin().postEvent(new Event((String) arguments[1], properties));
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.web.BrowserRPCHandler#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
