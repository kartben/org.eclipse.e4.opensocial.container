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

import java.util.Arrays;
import java.util.List;

import org.eclipse.e4.opensocial.container.internal.features.Feature;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.osgi.service.resolver.ResolverError;

public interface ModuleResolver {
	public class UnresolvedException extends Exception {
		private static final long serialVersionUID = -5975029222979497392L;
		public ResolverError[] _resolverErrors;

		public UnresolvedException(Module m, ResolverError[] resolverErrors) {
			super("Unsatisfied constraints met while resolving module " + m
					+ ": " + Arrays.toString(resolverErrors));
			_resolverErrors = resolverErrors;
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
