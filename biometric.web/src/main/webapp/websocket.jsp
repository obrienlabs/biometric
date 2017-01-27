<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" version="2.0">
	<jsp:directive.page contentType="text/html; charset=UTF-8" 
		pageEncoding="UTF-8" session="false"/>
	<jsp:output doctype-root-element="html"
		doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
		omit-xml-declaration="true" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>WebSocket</title>
<script language="javascript" type="text/javascript">
  var echo_websocket;
  function init() {
	  output = document.getElementById("output");
  }
  function send_echo() {
	  var wsUrl = "ws://localhost:8080/biometric/echo";
	  writeToScreen("Connecting " + wsUri);
	  echo_websocket = new WebSocket(wsUri);
	  echo_websocket.onopen = function (evt) {
		  writeToScreen("connected ");
		  doSend(textID.value);
	  };
	  echo_websocket.onmessage = function (evt) {
		  writeToScreen("Received " + evt.data);
		  echo_websocket.close();
	  };
	  echo_websocket.onerror = function (evt) {
		  writeToScreen('span style="color: red;">ERROR:</span> ' + evt.data);
		  echo_websocket.close();
	  };
  }
  function doSent(message) {
	  echo_websocket.send(message);
	  writeToScreen("Sent: " + message);
	  
  }
  function writeToScreen(message) {
	  var pre = document.createElement("p");
	  pre.style.wordWrap = "break-word";
	  pre.innerHTML = message;
	  output.appendChild(pre);
  }
  window.addEventListener("load", init, false);
</script>
</head>
<body>
    <h2>Echo Server</h2>
    <div style="text-align: left;">
        <form action="">
            <input onclick="send_echo()" value="Press to send" type="button"/>
            <input id="textID" name="message" value="Hello WS" type="text"/>
            <br/>
        </form>
    </div>
    <div id="output"></div>
</body>
</html>
</jsp:root>