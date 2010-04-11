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
package org.eclipse.e4.opensocial.container.internal.browserHandlers.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.ui.web.BrowserRPCHandler;
import org.eclipse.swt.browser.Browser;
import org.eclipse.ui.statushandlers.StatusManager;

/**
 * @author kartben
 * 
 */
public class LogHandler implements BrowserRPCHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.web.BrowserRPCHandler#handle(org.eclipse.swt.browser
	 * .Browser, java.lang.Object[])
	 */
	@Override
	public Object handle(Browser browser, Object[] arguments) {
		int statusSeverity = IStatus.INFO;
		if ("info".equals(arguments[1]))
			statusSeverity = IStatus.INFO;
		else if ("warning".equals(arguments[1]))
			statusSeverity = IStatus.WARNING;
		else if ("error".equals(arguments[1]))
			statusSeverity = IStatus.ERROR;

		if ("info".equals(arguments[1])) {
			StatusManager.getManager().handle(
					new Status(statusSeverity, "opensocial-demo",
							(String) arguments[2]));
		}
		return null;
	}

}
