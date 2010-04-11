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
package org.eclipse.e4.opensocial.container.resolver;

import java.util.List;

import org.eclipse.e4.opensocial.container.internal.features.Feature;
import org.eclipse.e4.opensocial.model.Module;

public interface ModuleResolver {
	public class UnresolvedException extends Exception {
		public UnresolvedException(String msg) {
			super(msg);
		}

	}

	/**
	 * Returns the ordered list of {@link Feature}s needed by a given
	 * {@link Module}
	 * 
	 * @param m
	 * @return
	 * @throws UnresolvedException
	 */
	List<Feature> resolve(Module m) throws UnresolvedException;
}
