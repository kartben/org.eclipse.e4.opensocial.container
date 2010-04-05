var gadgets = gadgets || {};

gadgets.rpc = function() {
  return {
    register: function(serviceName, handler) {
	  // TODO
    },

    unregister: function(serviceName) {
    	// TODO
    },

    registerDefault: function(handler) {
    	// TODO
    },

    unregisterDefault: function() {
      // TODO
    },

    call: function(targetId, serviceName, callback, var_args) {
    	// TODO
    }
  };
}();
 