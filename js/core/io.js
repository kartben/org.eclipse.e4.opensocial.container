var gadgets = gadgets || {};

gadgets.io = function() {
	return {
		makeRequest : function(url, callback, params) {
			try {
				// Retrieve parameters to forward them to the java part
				var method = params[gadgets.io.RequestParameters.METHOD];
				var contentType = params[gadgets.io.RequestParameters.CONTENT_TYPE];
				
				// All headers will be converted in a single String. The format will be :
				// headerName#headerValue>header2Name#header2Value
				var headers = params[gadgets.io.RequestParameters.HEADERS];
				var headersStr = "" ;
				if (headers != null) {
					headersStr = "Authorization#" + headers["Authorization"];
					headersStr += "\n" + "ContentType#" + headers["ContentType"];
				}

				var postData = params[gadgets.io.RequestParameters.POST_DATA];

				gadgets.rpc.call('', 'makeXmlHttpRequest', callback.toString(), method,
						contentType, headersStr, postData);
			} catch (err) {
				// FIXME: this should not happen, but for some reason
				// e4_makeXmlHttpRequest is not declared at the very beginning
				// of the execution...
			}

		}
	}
}();

gadgets.io.RequestParameters = gadgets.util.makeEnum( [ "METHOD",
		"CONTENT_TYPE", "POST_DATA", "HEADERS", "AUTHORIZATION", "NUM_ENTRIES",
		"GET_SUMMARIES", "REFRESH_INTERVAL", "OAUTH_SERVICE_NAME",
		"OAUTH_USE_TOKEN", "OAUTH_TOKEN_NAME", "OAUTH_REQUEST_TOKEN",
		"OAUTH_REQUEST_TOKEN_SECRET" ]);

gadgets.io.MethodType = gadgets.util.makeEnum( [ "GET", "POST", "PUT",
		"DELETE", "HEAD" ]);

gadgets.io.ContentType = gadgets.util
		.makeEnum( [ "TEXT", "DOM", "JSON", "FEED" ]);

gadgets.io.AuthorizationType = gadgets.util.makeEnum( [ "NONE", "SIGNED",
		"OAUTH" ]);
