var gadgets = gadgets || {};

gadgets.util = gadgets.util || {};

/**
 * Registers an onload handler; a function that's executed when the gadget
 * loads. Multiple handlers can be registered, and all will be invoked in the
 * same order that they were registered.
 */
gadgets.util.registerOnLoadHandler = function(callback) {
	if (window.addEventListener) {
		window.addEventListener('onload', callback, false);
	} else if (window.attachEvent) {
		window.attachEvent('onload', callback);
	}
};

gadgets.util.makeEnum = function(values) {
	var obj = {};
	for ( var i = 0, v; v = values[i]; ++i) {
		obj[v] = v;
	}
	return obj;
};

/**
 * Escapes the input using HTML entities to make it safer. The following
 * characters are affected:
 */
gadgets.util.escapeString = function(str) {

};

/**
 * Reverses escapeString
 */
gadgets.util.unescapeString = function(str) {

};

/**
 * Returns the value of parameters for this feature. A gadget specifies
 * parameters using the <Param> subelement of the <Requires> or <Optional>
 * element.
 */
gadgets.util.getFeatureParameters = function(feature) {

};

/**
 * Returns whether the specified feature is supported.
 */
gadgets.util.hasFeature = function(feature) {

};

/**
 * Sanitizes a text string. The returned value is safe to assign to innerHTML.
 * The returned value may include HTML tags. If plain text is desired, use
 * gadgets.util.escapeString instead.
 */
gadgets.util.sanitizeHtml = function(text) {

};