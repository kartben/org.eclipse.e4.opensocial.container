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
package org.eclipse.e4.opensocial.container.internal.features;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.StateObjectFactory;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.osgi.framework.Version;

public class FeatureUtils {
	public static BundleDescription featureToBundleDescription(Feature f,
			StateObjectFactory factory) {
		List<String> dependencies = f.getDependencies();
		List<BundleSpecification> bundleDependencies = new ArrayList<BundleSpecification>();
		for (String dependency : dependencies) {
			bundleDependencies.add(factory.createBundleSpecification(
					dependency, new VersionRange(null), true, false));
		}

		return factory.createBundleDescription(f.hashCode(), f.getName(),
				new Version(f.getVersion()), null, bundleDependencies
						.toArray(new BundleSpecification[0]), null, null, null,
				false, false, false, null, null, null, null, null);
	}
}
