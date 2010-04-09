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

import java.util.Arrays;

import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

/**
 * The {@link BrowserFunction} used as a common dispatcher between a gadget and
 * its e4 container
 */
public class RPCBrowserFunction extends BrowserFunction {
	/**
	 * The Module definition of the gadget displayed in the Browser associated
	 * to this BrowserFunction
	 */
	private Module _module;

	/**
	 * @param browser
	 * @param name
	 */
	public RPCBrowserFunction(Browser browser, Module module) {
		super(browser, "__e4_RPC");
		_module = module;
	}

	/**
	 * Convention to call __e4_RPC from Javascript is:<br>
	 * <ul>
	 * <li>1st arg: the function name</li>
	 * <li>2nd arg: array of arguments</li>
	 * <li>3rd arg (optional): callback, if any</li>
	 * </ul>
	 * 
	 * Example: <br>
	 * <code>__e4_RPC('greeting', ['hello'], onResult); </code>
	 * 
	 * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
	 */
	@Override
	public Object function(Object[] arguments) {
		System.out.println("called __e4_RPC with args: "
				+ Arrays.toString(arguments));
		return super.function(arguments);
	}

}
