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

import org.eclipse.e4.opensocial.model.GadgetFeature;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.StateObjectFactory;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.osgi.framework.Version;

public class FeatureUtils {

	public static BundleDescription moduleToBundleDescription(Module module,
			StateObjectFactory factory) {
		List<BundleSpecification> bundleDependencies = new ArrayList<BundleSpecification>();
		for (GadgetFeature required : module.getModulePrefs().getRequire()) {
			bundleDependencies.add(factory.createBundleSpecification(required
					.getFeature(), new VersionRange(required.getVersion()),
					false, false));
		}
		for (GadgetFeature optional : module.getModulePrefs().getOptional()) {
			bundleDependencies.add(factory.createBundleSpecification(optional
					.getFeature(), new VersionRange(optional.getVersion()),
					false, true));
		}

		BundleDescription bundleDescription = factory.createBundleDescription(
				module.hashCode(), module.getModulePrefs().getTitle(),
				new Version("1.0.0"), null, bundleDependencies
						.toArray(new BundleSpecification[0]), null, null, null,
				false, false, false, null, null, null, null, null);
		bundleDescription.setUserObject(module);
		return bundleDescription;
	}

	public static BundleDescription featureExtensionToBundleDescription(
			Feature feature, StateObjectFactory factory) {
		List<String> dependencies = feature.getDependencies();
		List<BundleSpecification> bundleDependencies = new ArrayList<BundleSpecification>();
		for (String dependency : dependencies) {
			bundleDependencies.add(factory.createBundleSpecification(
					dependency, new VersionRange(null), false, false));
		}

		BundleDescription bundleDescription = factory.createBundleDescription(
				feature.hashCode(), feature.getName(), new Version(feature
						.getVersion()), null, bundleDependencies
						.toArray(new BundleSpecification[0]), null, null, null,
				false, false, false, null, null, null, null, null);
		bundleDescription.setUserObject(feature);
		return bundleDescription;
	}
}
