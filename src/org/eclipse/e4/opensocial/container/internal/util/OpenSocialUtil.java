/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Boris Bokowski, IBM Corporation - initial API and implementation
 *     Benjamin Cabe, Sierra Wireless - ongoing enhancements
 *******************************************************************************/
package org.eclipse.e4.opensocial.container.internal.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.e4.opensocial.model.DocumentRoot;
import org.eclipse.e4.opensocial.model.LanguageDirection;
import org.eclipse.e4.opensocial.model.Locale;
import org.eclipse.e4.opensocial.model.Module;
import org.eclipse.e4.opensocial.model.Msg;
import org.eclipse.e4.opensocial.model.OpenSocialPackage;
import org.eclipse.e4.opensocial.model.UserPref;
import org.eclipse.e4.opensocial.model.util.OpenSocialResourceFactoryImpl;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class OpenSocialUtil {

	private static int moduleId = 0;

	public static void main(String[] args) {
		// System.out
		// .println(loadModule("http://m2mdemo.eanyware-tech.com/resources/gadget/Temperature.xml"));

	}

	public static Module loadModule(String uriString) {
		java.net.URI uri;
		Module module;
		try {
			uri = new java.net.URI(uriString);
			Resource resource = new OpenSocialResourceFactoryImpl()
					.createResource(URI.createURI(uriString));
			resource.load(Collections.EMPTY_MAP);
			module = ((DocumentRoot) resource.getContents().get(0)).getModule();
		} catch (Exception e) {
			throw new RuntimeException("Could not load module", e);
		}
		Locale locale = determineLocale(module);

		Map<String, String> hangmanMap = new HashMap<String, String>();
		hangmanMap.put("__MODULE_ID__", String.valueOf(moduleId++));
		hangmanMap.put("__ENV_google_apps_auth_path__", "render#");

		populateHangmanMapFromLocale(hangmanMap, locale, uri);
		populateHangmanMapFromUserPrefs(hangmanMap, module.getUserPref());

		// perform hangman substitution on every attribute
		// from Gadget Specification 1.0: Containers MUST perform substitution
		// on all elements and attributes defined in Elements and Attributes
		// (Section 4), with the exceptions of /ModulePrefs/Locale (and
		// children), or any element with an explicit enumeration.

		// Current limitation: element substitution is not possible (unless the
		// elements are somewhere inside the textual Content of the module
		TreeIterator<EObject> it = module.eAllContents();
		while (it.hasNext()) {
			EObject o = it.next();
			for (EAttribute att : o.eClass().getEAllAttributes()) {
				if (OpenSocialPackage.Literals.LOCALE == att.eContainer())
					continue;
				if (FeatureMapUtil.isFeatureMap(att))
					continue;
				if (o.eGet(att) != null
						&& att.getEType().getInstanceClassName()
								.equals("java.lang.String")) {
					// System.out.println(hangmanExpand(o.eGet(att).toString(),
					// hangmanMap));
					o.eSet(att,
							hangmanExpand(o.eGet(att).toString(), hangmanMap));
				}
			}
		}

		return module;
	}

	private static void populateHangmanMapFromUserPrefs(
			Map<String, String> hangmanMap, List<UserPref> userPrefs) {
		for (UserPref pref : userPrefs) {
			String value = (pref.getValue() != null) ? pref.getValue() : pref
					.getDefaultValue();
			hangmanMap.put("__UP_" + pref.getName() + "__", value);
		}
	}

	private static void populateHangmanMapFromLocale(
			Map<String, String> hangmanMap, Locale locale, java.net.URI uri) {
		hangmanMap.put("__BIDI_START_EDGE__", "left");
		hangmanMap.put("__BIDI_END_EDGE__", "right");
		hangmanMap.put("__BIDI_DIR__", "ltr");
		hangmanMap.put("__BIDI_REVERSE_DIR__", "rtl");
		if (locale != null) {
			if (locale.getLanguageDirection() == LanguageDirection.RTL) {
				hangmanMap.put("__BIDI_START_EDGE__", "right");
				hangmanMap.put("__BIDI_END_EDGE__", "left");
				hangmanMap.put("__BIDI_DIR__", "rtl");
				hangmanMap.put("__BIDI_REVERSE_DIR__", "ltr");
			}
			String messagesURIString = locale.getMessages();
			if (messagesURIString != null) {
				if (!messagesURIString.startsWith("http")) {
					if (!messagesURIString.startsWith("/")) {
						String uriString = uri.toString();
						messagesURIString = uriString.substring(0,
								uriString.lastIndexOf('/'))
								+ "/" + messagesURIString;
					} else {
						messagesURIString = "http://" + uri.getHost()
								+ messagesURIString;
					}
				}
				try {
					addMessagesToMap(hangmanMap, messagesURIString);
				} catch (Exception e) {
					throw new RuntimeException("Could not parse messages", e);
				}
			} else {
				addMessagesToMap(hangmanMap, locale.getMsg());
			}
		}
	}

	private static Locale determineLocale(Module module) {
		Map<LocaleKey, Locale> moduleLocales = new HashMap<LocaleKey, Locale>();
		for (Locale l : module.getModulePrefs().getLocale()) {
			moduleLocales.put(new LocaleKey(l.getLang(), l.getCountry()), l);
		}

		Locale locale = moduleLocales.get(new LocaleKey(System
				.getProperty("osgi.nl"), System.getProperty("user.country")));
		if (locale == null) {
			locale = moduleLocales.get(new LocaleKey(System
					.getProperty("user.language"), System
					.getProperty("user.country")));
		}
		if (locale == null) {
			locale = moduleLocales.get(new LocaleKey(System
					.getProperty("osgi.nl"), null));
		}
		if (locale == null) {
			locale = moduleLocales.get(new LocaleKey(System
					.getProperty("user.language"), null));
		}
		if (locale == null) {
			locale = moduleLocales.get(new LocaleKey(null, System
					.getProperty("user.country")));
		}
		if (locale == null) {
			locale = moduleLocales.get(new LocaleKey(null, null));
		}
		return locale;
	}

	private static void addMessagesToMap(final Map<String, String> hangmanMap,
			String messagesURIString) throws Exception {
		// System.out.println("Loading message bundle from: " +
		// messagesURIString);
		DefaultHandler handler = new DefaultHandler() {
			String name;
			String value;

			public void startElement(String uri, String localName,
					String qName, Attributes attributes) throws SAXException {
				name = attributes.getValue("name");
			}

			public void characters(char[] ch, int start, int length)
					throws SAXException {
				value = new String(ch, start, length);
			}

			public void endElement(String uri, String localName, String qName)
					throws SAXException {
				if ("msg".equalsIgnoreCase(qName) && name != null
						&& value != null) {
					hangmanMap.put("__MSG_" + name + "__", value);
				}
			}
		};
		parseXML(new java.net.URI(messagesURIString), handler);
	}

	private static void addMessagesToMap(Map<String, String> hangmanMap,
			List<Msg> messages) {
		for (Msg msg : messages) {
			hangmanMap.put("__MSG_" + msg.getName() + "__", msg.getValue());
		}
	}

	private static void parseXML(java.net.URI uri, DefaultHandler handler)
			throws MalformedURLException, IOException,
			ParserConfigurationException, SAXException {
		URL messagesURL = uri.toURL();
		URLConnection connection = messagesURL.openConnection();
		String encoding = connection.getContentEncoding();
		if (encoding == null) {
			String contentType = connection.getHeaderField("Content-Type");
			int charsetIndex;
			if (contentType != null
					&& (charsetIndex = contentType.indexOf("charset=")) != -1) {
				int charsetBegin = charsetIndex + "charset=".length();
				int charsetEnd = charsetBegin;
				while (contentType.length() > charsetEnd
						&& " ,;".indexOf(contentType.charAt(charsetEnd)) == -1) {
					charsetEnd++;
				}
				if (charsetEnd > charsetBegin) {
					encoding = contentType.substring(charsetBegin, charsetEnd);
				}
			}
		}
		InputStream is = connection.getInputStream();
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser parser = spf.newSAXParser();
			InputSource source = new InputSource(is);
			if (encoding != null) {
				source.setEncoding(encoding);
			}
			parser.parse(source, handler);
		} finally {
			is.close();
		}
	}

	private static String hangmanExpand(String s, Map<String, String> hangmanMap) {
		if (s == null) {
			return null;
		}
		StringBuffer result = new StringBuffer(s.length());
		int currentIndex = 0;
		int indexOfUnderscores;
		// TODO use Regexp instead?
		while ((indexOfUnderscores = s.indexOf("__", currentIndex)) != -1) {
			result.append(s.substring(currentIndex, indexOfUnderscores));
			int indexOfNextUnderscores = s
					.indexOf("__", indexOfUnderscores + 2);
			if (indexOfNextUnderscores != -1) {
				String key = s.substring(indexOfUnderscores,
						indexOfNextUnderscores + 2);
				String value = hangmanMap.get(key);
				if (value != null) {
					result.append(value);
				} else {
					result.append(key);
				}
				currentIndex = indexOfNextUnderscores + 2;
			} else {
				result.append("__");
				currentIndex = indexOfUnderscores + 2;
			}
		}

		result.append(s.substring(currentIndex));
		return result.toString();
	}

}
