var gadgets = gadgets || {};

gadgets.Prefs.prototype.set = function(key, value) {
	this[key] = value;
};
gadgets.Prefs.prototype.getString = function(key) {
	return this[key] || "";
};
gadgets.Prefs.prototype.getBool = function(key) {
	return this[key] || true;
};
gadgets.Prefs.prototype.getInt = function(key) {
	return 0 + this[key] || 0;
};
gadgets.Prefs.prototype.getMsg = function(key) {
	return key;
};
gadgets.Prefs.prototype.getArray = function(key) {
	return [];
};

//Alias for legacy code
var _IG_Prefs = gadgets.Prefs;
