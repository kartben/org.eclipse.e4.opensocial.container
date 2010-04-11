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
import org.eclipse.swt.browser.Browser;

public class UnsubscribeHandler implements BrowserRPCHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.web.BrowserRPCHandler#handle(org.eclipse.swt.browser
	 * .Browser, java.lang.Object[])
	 */
	@Override
	public Object handle(Browser browser, Object[] arguments) {
		// try to find the ServiceReference of an EventHandler with the same
		// topic, and the same moduleId
		// TODO
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.web.BrowserRPCHandler#dispose()
	 */
	@Override
	public void dispose() {
		// Nothing
	}

}
