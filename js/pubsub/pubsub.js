var gadgets = gadgets || {};

gadgets.pubsub = (function() {
  return {
    publish: function(channel, message) {
  	e4RPC('e4.opensocial.pubsub.publish', channel, message);
    },

    subscribe: function(channel, callback) {
    	e4RPC('e4.opensocial.pubsub.subscribe', channel, callback.toString());
    },

    unsubscribe: function(channel) {
    	e4RPC('e4.opensocial.pubsub.unsubscribe', channel);
    }
  };
})();
 