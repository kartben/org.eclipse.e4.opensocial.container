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

import org.eclipse.e4.opensocial.container.internal.util.BrowserUtils;
import org.eclipse.e4.opensocial.container.resolver.ModuleResolver.UnresolvedException;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.e4.ui.web.BrowserRPC;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * An SWT {@link Browser} configured to automatically display a given OpenSocial
 * {@link Module}
 */
public class OpenSocialBrowser extends Composite {

	private Module _module;
	private BrowserRPC _browserRPC;
	private Browser _browser;

	/**
	 * @param module
	 *            the OpenSocial {@link Module} to display in this browser
	 * 
	 * @see Browser#Browser(Composite, int)
	 */
	public OpenSocialBrowser(Composite parent, int style, Module module) {
		super(parent, style);
		this.setLayout(new FillLayout());
		_browser = new Browser(this, SWT.NONE);
		_browserRPC = new BrowserRPC(_browser);

		_module = module;
		_module.getModulePrefs().eAdapters().add(new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				System.out.println("preference changed => refresh gadget");
				refreshBrowserContent();
			}
		});

		refreshBrowserContent();

	}

	private void refreshBrowserContent() {
		try {
			String html = BrowserUtils.generateHtml(_module);
			System.out.println(html);
			BrowserUtils.registerHandlers(_module, _browserRPC);
			_browser.setText(html);
		} catch (UnresolvedException e) {
			// TODO log?
			_browser.setText(e.getMessage());
		}
	}

	public Browser getBrowser() {
		return _browser;
	}

	public Module getModule() {
		return _module;
	}
}
