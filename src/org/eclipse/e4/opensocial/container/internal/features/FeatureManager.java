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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryEventListener;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.PlatformAdmin;
import org.eclipse.osgi.service.resolver.State;

public class FeatureManager implements IRegistryEventListener {
	private static final String FEATURES_EXT_POINT_ID = "org.eclipse.e4.opensocial.container.features";
	private static final String FEATURE_ELEM = "feature";
	private static final String FEATURE_ATTR_NAME = "name";
	private static final String FEATURE_ATTR_VERSION = "version";
	private PlatformAdmin _platformAdmin;
	private IExtensionRegistry _extensionRegistry;
	private Map<String, Feature> _availableFeatures = new HashMap<String, Feature>();

	private State _featuresState;

	protected void activate() {
	}

	protected void deactivate() {
		_extensionRegistry.removeListener(this);
	}

	protected void setPlatformAdmin(PlatformAdmin platformAdmin) {
		_platformAdmin = platformAdmin;
		_featuresState = _platformAdmin.getFactory().createState(true);
	}

	protected void setExtensionRegistry(IExtensionRegistry extensionRegistry) {
		_extensionRegistry = extensionRegistry;
		_extensionRegistry.addListener(this, FEATURES_EXT_POINT_ID);

		for (IConfigurationElement ice : _extensionRegistry
				.getConfigurationElementsFor(FEATURES_EXT_POINT_ID)) {
			if (FEATURE_ELEM.equals(ice.getName())) {
				addFeature(ice);
			} else {
				// no other top-level elements at the moment
			}
		}
	}

	@Override
	public void added(IExtension[] extensions) {
		for (IExtension ext : extensions) {
			for (IConfigurationElement ice : ext.getConfigurationElements()) {
				if (FEATURE_ELEM.equals(ice.getName())) {
					addFeature(ice);
				}
			}
		}

	}

	private void addFeature(IConfigurationElement ice) {
		String name = ice.getAttribute(FEATURE_ATTR_NAME);
		String version = ice.getAttribute(FEATURE_ATTR_VERSION);
		Feature f = new Feature(name, version, ice);
		_availableFeatures.put(name, f);
		_featuresState.addBundle(FeatureUtils.featureToBundleDescription(f,
				_featuresState.getFactory()));
		_featuresState.resolve();
		BundleDescription[] resolvedBundles = _featuresState
				.getResolvedBundles();
		System.out.println(Arrays.toString(resolvedBundles));
		_featuresState.getStateHelper().sortBundles(resolvedBundles);
		System.out.println(Arrays.toString(resolvedBundles));
	}

	@Override
	public void removed(IExtension[] extensions) {
		for (IExtension ext : extensions) {
			for (IConfigurationElement ice : ext.getConfigurationElements()) {
				if (FEATURE_ELEM.equals(ice.getName())) {
					removeFeature(ice);
				}
			}
		}
	}

	private void removeFeature(IConfigurationElement ice) {
		String name = ice.getAttribute(FEATURE_ATTR_NAME);
		Feature oldFeature = _availableFeatures.remove(name);
		_featuresState.removeBundle(oldFeature.hashCode());
	}

	@Override
	public void added(IExtensionPoint[] extensionPoints) {
	}

	@Override
	public void removed(IExtensionPoint[] extensionPoints) {

	}

}
