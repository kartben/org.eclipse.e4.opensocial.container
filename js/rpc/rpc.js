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
    	targetId = targetId || '..';

        if (targetId === '..') {
        	// call to the e4 container
        	e4RPC(serviceName, var_args, (callback ? callback.toString() : null));
        }  
        else {
        	// targetId is the module ID of the gadget we want to send a remote call to
        	e4RPC('osGadgetToGadget', new Array(serviceName).concat(var_args), (callback ? callback.toString() : null));
        }
    }
  };
}();
 