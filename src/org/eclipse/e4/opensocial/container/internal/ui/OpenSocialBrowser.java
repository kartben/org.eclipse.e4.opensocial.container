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
package org.eclipse.e4.opensocial.container.internal.ui;

import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.e4.ui.web.BrowserRPC;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;

/**
 * An SWT {@link Browser} configured to automatically display a given OpenSocial
 * {@link Module}
 */
public class OpenSocialBrowser extends Browser {

	private Module _module;
	private BrowserRPC _browserRPC;

	/**
	 * @param module
	 *            the OpenSocial {@link Module} to display in this browser
	 * 
	 * @see Browser#Browser(Composite, int)
	 */
	public OpenSocialBrowser(Composite parent, int style, Module module) {
		super(parent, style);
		_module = module;

		_browserRPC = new BrowserRPC(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
	}

}
