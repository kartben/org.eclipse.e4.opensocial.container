var gadgets = gadgets || {};

gadgets.util = function() {
	return {
		registerOnLoadHandler : function(callback) {
			callback() ;
			//window.onload = callback;
		},
	
    	makeEnum : function (values) {
			var obj = {};
		    for (var i = 0, v; v = values[i]; ++i) {
		    	obj[v] = v;
		    }
		    return obj;
		}
	}
}();
