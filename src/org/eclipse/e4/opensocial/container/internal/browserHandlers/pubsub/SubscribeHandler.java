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

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.e4.ui.web.BrowserRPCHandler;
import org.eclipse.swt.browser.Browser;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class SubscribeHandler extends AbstractPubSubHandler implements
		BrowserRPCHandler {

	private List<ServiceRegistration> registeredHandlers = new ArrayList<ServiceRegistration>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.web.BrowserRPCHandler#handle(org.eclipse.swt.browser
	 * .Browser, java.lang.Object[])
	 */
	@Override
	public Object handle(final Browser browser, final Object[] arguments) {
		final String channel = (String) arguments[1];
		final String callbackName = (String) arguments[2];

		// publish an EventHandler
		EventHandler eventHandler = new EventHandler() {
			@Override
			public void handleEvent(Event event) {
				final Integer source = (Integer) event
						.getProperty(MODULEID_EVENT_PROPERTY);
				// 'message' field is the JSON representation of the event
				// properties map
				final JSONObject message = new JSONObject();
				try {
					for (String key : event.getPropertyNames()) {
						Object value = event.getProperty(key);
						if (value instanceof String)
							message.put(key, (String) value);
						else if (value instanceof Integer)
							message.put(key, (Integer) value);
						else if (value instanceof Long)
							message.put(key, (Long) value);
						else if (value instanceof Double)
							message.put(key, (Double) value);
						else
							message.put(key, value.toString()
									+ "                                    ");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				browser.getDisplay().syncExec(new Runnable() {
					public void run() {
						if (!browser.isDisposed())
							browser.evaluate("var f = " + callbackName
									+ "; f('" + source + "', '"
									+ message.toString() + "');");
					}
				});
			}
		};

		Dictionary<Object, Object> properties = new Hashtable<Object, Object>();
		properties.put(EventConstants.EVENT_TOPIC, channel);
		// TODO put moduleId
		ServiceRegistration sr = getBundleContext().registerService(
				EventHandler.class.getName(), eventHandler, properties);
		registeredHandlers.add(sr);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.web.BrowserRPCHandler#dispose()
	 */
	@Override
	public void dispose() {
		for (ServiceRegistration sr : registeredHandlers) {
			sr.unregister();
		}
	}

}
