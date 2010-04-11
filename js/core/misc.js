function _IG_LoadLibraryDeferred(url, callback) {
  var scriptElement = document.createElement("script");
  scriptElement.src=url;
  document.getElementsByTagName("head")[0].appendChild(scriptElement);
  setTimeout(function(){eval(callback);}, 1000);
}

function _gel(id) {
  if(document.all) {
    return document.all[id];
  } else {
    return document.getElementById(id);
  }
}
function _IG_AddDOMEventHandler(domObject,event,callback){
  if (domObject.addEventListener){
    domObject.addEventListener(event, callback, true);}
  else{
    domObject.attachEvent('on'+event,callback);
  }
}

function _IG_Analytics() {
}

function _IG_RegisterOnloadHandler(handler) {
	_IG_AddDOMEventHandler(window, "load", function(){handler();});
}

function _IG_RegisterMaximizeHandler(handler) {
}
