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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.eclipse.e4.ui.web.BrowserRPCHandler;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;

public class MakeXmlHttpRequestHandler implements BrowserRPCHandler {
	public static final String NO_HEADER_VALUE = "undefined";

	public static final String HEADER_NAME_VALUE_SEPARATOR = "#";

	public static final String HEADERS_SEPARATOR = "\n";

	public static final String GET_METHOD = "GET";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.e4.ui.web.BrowserRPCHandler#handle(org.eclipse.swt.browser
	 * .Browser, java.lang.Object[])
	 */
	@Override
	public Object handle(final Browser browser, final Object[] arguments) {
		/*
		 * function('makeXmlHttpRequest', url, callback, method, contentType,
		 * headers, postData)
		 */
		try {
			// Retrieve arguments
			String url = (String) arguments[1];
			String callback = (String) arguments[2];
			String method = (String) arguments[3];
			String contentType = (String) arguments[4];
			// headers format :
			// headerName#headerValue\nheader2Name#header2Value
			String headers = (String) arguments[5];
			String postData = (String) arguments[6];

			HttpClient httpClient = new HttpClient();
			// Create GET or POST http method
			HttpMethod httpMethod = createMethod(url, method);

			// Add needed headers
			if (!"".equals(headers))
				addHeaders(httpMethod, headers);

			// Handle PostData if needed
			if (postData != null) {
				((PostMethod) httpMethod)
						.setRequestEntity(new StringRequestEntity(postData,
								"text/xml", "utf-8"));
			}

			int status = httpClient.executeMethod(httpMethod);

			String responseBodyAsString = retrieveResponseBody(httpMethod);

			// FIXME: at the moment, remove single quotes causing js
			// problems.
			responseBodyAsString = responseBodyAsString.replaceAll("'", " ");

			String responseScript = "\n";
			// Create and fill response object
			responseScript += "var response = new Object();\n";
			responseScript += "response.rc=" + status + ";\n";
			responseScript += "response.text='" + responseBodyAsString + "';\n";

			// Create the response's data property
			if ("DOM".equals(contentType)) {
				responseScript = createDOMResponseData(responseScript);
			} else if ("JSON".equals(contentType)) {
				responseScript = createJSONResponseData(responseBodyAsString,
						responseScript);
			} else if ("TEXT".equals(contentType)) {
				responseScript = createTEXTReponseData(responseScript);
			} else if ("FEED".equals(contentType)) {
				// FIXME : Not implemented for the moment.
				responseScript = createFEEDResponseData(responseScript);
			}

			// Instanciate and call callback
			responseScript += "var callback = " + callback + ";\n"
					+ "callback(response);\n";

			final String script = responseScript;
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					browser.execute(script);
				}
			});
			return status;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return HttpStatus.SC_INTERNAL_SERVER_ERROR;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.e4.ui.web.BrowserRPCHandler#dispose()
	 */
	public void dispose() {
		// Nothing
	}

	/**
	 * TODO : Create a JSON representation of the RSS XML data
	 * 
	 * @param responseScript
	 *            String
	 */
	protected String createFEEDResponseData(String responseScript) {
		responseScript += "response.data='Not implemented';";
		return responseScript;
	}

	/**
	 * TEXT data corresponds to the raw response text
	 * 
	 * @param responseScript
	 *            String
	 */
	protected String createTEXTReponseData(String responseScript) {
		responseScript += "response.data=response.text;";
		return responseScript;
	}

	/**
	 * Create the JSON Object
	 * 
	 * @param responseBodyAsString
	 *            String, raw response body
	 * @param responseScript
	 *            String
	 */
	protected String createJSONResponseData(String responseBodyAsString,
			String responseScript) {
		// Parse response body to create the corresponding JSON
		// object
		responseScript += "response.data=eval('(" + responseBodyAsString
				+ ")')\n";
		return responseScript;
	}

	/**
	 * Parse the XML response body to create the corresponding DOM.
	 * 
	 * @param responseScript
	 *            String
	 */
	protected String createDOMResponseData(String responseScript) {
		// Parse response body to create the corresponding DOM
		// object
		responseScript += "var xmlDoc;\n";
		responseScript += "if (window.DOMParser){\n"
				+ "parser=new DOMParser();\n"
				+ "xmlDoc=parser.parseFromString(response.text,'text/xml');\n"
				+ "} else {\n// Internet Explorer\n"
				+ "xmlDoc=new ActiveXObject('Microsoft.XMLDOM');\n"
				+ "xmlDoc.async='false';\n"
				+ "xmlDoc.loadXML(response.text);\n" + "}\n";
		responseScript += "response.data=xmlDoc;\n";
		return responseScript;
	}

	protected String retrieveResponseBody(HttpMethod httpMethod)
			throws IOException, UnsupportedEncodingException {

		String responseBodyAsString = "";
		InputStream bodyAsStream = httpMethod.getResponseBodyAsStream();

		StringBuilder sb = new StringBuilder();
		String line;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					bodyAsStream, "UTF-8"));
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} finally {
			bodyAsStream.close();
		}

		responseBodyAsString = sb.toString();
		return responseBodyAsString;
	}

	/**
	 * Parse the String corresponding the headers and add them to the HttpMethod
	 * 
	 * @param httpMethod
	 *            HttpMethod which will be filled with the given headers
	 * @param headers
	 *            String representation of the headers to add. They respect the
	 *            following format :
	 *            headerName#headerValue>header2Name#header2Value
	 * 
	 */
	private void addHeaders(HttpMethod httpMethod, String headers) {
		// Split to retrieve all headers
		String[] splittedHeader = headers.split(HEADERS_SEPARATOR);

		// Loop through the headers
		for (String header : splittedHeader) {
			// Split to retrieve the header name & value
			String[] headerNameAndValue = header
					.split(HEADER_NAME_VALUE_SEPARATOR);
			String name = headerNameAndValue[0];
			String value = headerNameAndValue[1];
			// If the value exists add it to the
			// HttpMethod
			if (!NO_HEADER_VALUE.equals(value))
				httpMethod.addRequestHeader(name, value);
		}
	}

	/**
	 * 
	 * @param url
	 * @param method
	 * @return
	 */
	protected HttpMethod createMethod(String url, String method) {
		if (GET_METHOD.equals(method))
			return new GetMethod(url);

		return new PostMethod(url);
	}
}