var gadgets = gadgets || {};

/**
 * Log an informational message
 */
gadgets.log = function(message) {
	if(gadgets.log.currentLevel <= gadgets.log.INFO) {
		e4RPC('e4.opensocial.core.log.Log', 'info', message) ;
	}
};

 
/**
 * Log a warning
 */
gadgets.warn = function(message) {
	if(gadgets.log.currentLevel <= gadgets.log.WARNING) {
		e4RPC('e4.opensocial.core.log.Log', 'warning', message) ;
	}
};

/**
 * Log an error
 */
gadgets.error = function(message) {
	if(gadgets.log.currentLevel <= gadgets.log.ERROR) {
		e4RPC('e4.opensocial.core.log.Log', 'error', message) ;
	}
};

/**
 * Sets the log level threshold.
 * @param {Number} logLevel - New log level threshold.
 * @static
 */
gadgets.setLogLevel = function(logLevel) {
  gadgets.log.logLevelThreshold_ = logLevel;
};

gadgets.log.INFO = 1;
gadgets.log.WARNING = 2;
gadgets.log.ERROR = 3;
gadgets.log.NONE = 4;

gadgets.log.currentLevel = gadgets.log.INFO;
