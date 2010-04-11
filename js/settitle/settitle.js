var gadgets = gadgets || {};

gadgets.window = gadgets.window || {};

gadgets.window.setTitle = function(title) {
	window.parent.document.title = title ;
};

// Legacy function
var _IG_SetTitle = gadgets.window.setTitle;
 