	var x=document.getElementById("output2");
    var pg=document.getElementById("outputpg");

  var lat;
  var lon;
  var t;
  var watchID;
  var geoLoc;
  var iterations = 0;
  
  // display flags
  var flicker = new Array();
  var lastValue = new Array();
  // Timer variables
  var timers = new Array();
  var timerStateArray = new Array();
  var ajax = new Array();
  // initialize all arrays
  var i = 0;
  //for (i=0;i<<2;i=i+1) {
      timerStateArray[i] = 0;
      ajax[i] = new Ajax();
      ajax[i].complete = AjaxUpdateEvent;
      flicker[i] = 0;
      
  function errorHandler(err) {
	  if(err.code == 1) {
	    alert("Error: Access is denied!");
	  }else if( err.code == 2) {
	    alert("Error: Position is unavailable!");
	  }
	}
  
  
  // LOOP
  function showLocation(position) {
	  var latitude = position.coords.latitude;
	  var longitude = position.coords.longitude;
	  //alert("Latitude : " + latitude + " Longitude: " + longitude);
	  document.getElementById("status").innerHTML = "iter: " + iterations;
	  	document.getElementById("output2").innerHTML= position.coords.latitude + ": " + position.coords.longitude;  	
	  	document.getElementById("1").innerHTML= position.coords.latitude + ": " + position.coords.longitude;
	  	lat = position.coords.latitude;
	  	lon = position.coords.longitude;
		iterations = iterations + 1;
		// PUSH to the server
		//alert("test")
		//ajax[0].call('/gps/FrontController?action=setGps&u=1&lt=45&lg=75.4&ac=1.1',0);
		ajax[0].call('/gps/FrontController?action=setGps&u=3&lt=' + lat + '&lg=' + lon + '&ac=1.1',0);
	}
  
  
  function getLocation()  {
	  iterations = iterations + 1;
	  document.getElementById("status").innerHTML = "iter: " + iterations;
	  
	  
  	if (navigator.geolocation)    {
    	navigator.geolocation.getCurrentPosition(showPosition);
    	alert(latitude + ":" + longitude);
    } else{
    	x.innerHTML="Geolocation is not supported by this browser.";
    }
  }

// todo: get rid of this dup
    function getLocationpg()  {
  	if (navigator.geolocation)    {
    	navigator.geolocation.getCurrentPosition(showPosition3);
    } else{
    	pg.innerHTML="Geolocation is not supported by this browser.";
    }
  }
 
    function getLocationUpdate(){
    	//alert("test");
    	   if(navigator.geolocation){
    	      // timeout at 60000 milliseconds (60 seconds)
    	      var options = {timeout:10000};
    	      geoLoc = navigator.geolocation;
    	      watchID = geoLoc.watchPosition(showLocation, 
    	                                     errorHandler,
    	                                     options);
    	   }else{
    	      alert("Sorry, browser does not support geolocation!");
    	   }
    	}    
    
  function showPosition(position)  {
	//alert(position.coords.latitude + ":" + position.coords.longitude);
  	document.getElementById("output2").innerHTML= position.coords.latitude + ": " + position.coords.longitude;  	
  	document.getElementById("1").innerHTML= position.coords.latitude + ": " + position.coords.longitude;
  	lat = position.coords.latitude;
  	lon = position.coords.longitude;
  	//alert(latitude + "," + longitude);
  }
  
  
    
  function showPosition3(position)  {
	//alert(position.coords.latitude + ":" + position.coords.longitude);
  	document.getElementById("outputpg").innerHTML= position.coords.latitude + ": " + position.coords.longitude;  	
  	document.getElementById("3").innerHTML= position.coords.latitude + ": " + position.coords.longitude;
  	lat = position.coords.latitude;
  	lon = position.coords.longitude;
  }
    
 function FactoryXMLHttpRequest() {
	   if(window.XMLHttpRequest) {
		   // Mozilla
		   return new XMLHttpRequest();
	   } else if(window.ActiveXObject) {
		   try {
		    // Internet Explorer only
            return new ActiveXObject("Microsoft.XMLHTTP");
		   } catch (e) {
		   }
       }
       // Verify Chrome, Firefox, Opera and Apple
       throw new Error("Could not get an AJAX XMLHttpRequest Object");
 }

 function Ajax() {
	 this._xmlHttp = new FactoryXMLHttpRequest();
 }

 // This code is loosely based on 
 // p.22 of Ajax Patterns and Best Practices by Christian Gross (2006)
 // http://books.google.com/books?id=qNzBbUWhGM0C&printsec=frontcover&dq=ajax+patterns+and&cd=2#v=onepage&q&f=false
 // and http://www.w3schools.com/js/js_timing.asp
 // htmlDOMElementId must be an integer
 function AjaxUpdateEvent(htmlDOMelementId, status, statusText, responseText, responseXML) {
	 // This line updates the DOM <span id="[0-9]+"/>
//	 var classNumber = responseXML.getElementsByTagName("size")[0].firstChild.nodeValue;
//     var classifications = responseXML.getElementsByTagName("class");
	 // past to text area
//	 document.getElementById('classes').innerHTML = classNumber;
	 // print number
/*	 document.getElementById(htmlDOMelementId).innerHTML = classNumber;
    	 var select = document.getElementsByTagName('select')[0];
    	 select.options.length = 0; // clear out existing items
         // get canvas
         var example = document.getElementById('left');
         var context = example.getContext('2d');
         context.fillStyle = "rgb(0,0,0)";
         context.fillRect(0, 0, width * pixelSize, height * pixelSize);

    	 for(var i=0; i < classNumber; i++) {
    	     var option = classifications[i].firstChild.nodeValue;
    	     classes[i] = option;
    	     select.options.add(new Option(option, i));
    	     if(option.substring(0,1) == "1") {
    	    	   context.fillStyle = "rgb(245,210,32)";
    	     } else {
                 context.fillStyle = "rgb(96,64,255)";
             }
             context.fillRect(10, i, option.length, 1);
    	 }
	*/ 
 }

 // This code is loosely based on 
 // p.22 of Ajax Patterns and Best Practices by Christian Gross (2006)
 // and http://www.w3schools.com/js/js_timing.asp
 // htmlDOMElementId must be an integer
 function AjaxUpdateEvent_old(htmlDOMelementId, status, statusText, responseText, responseXML) {
	 // This line updates the DOM <span id="[0-9]+"/>
	 document.getElementById(htmlDOMelementId).innerHTML = responseText;
	 // flash data cell only if data has changed
	 var color = "orange";
	 if(flicker[htmlDOMelementId] < 1) {
		 flicker[htmlDOMelementId] = 1;
		 color = "white";
	 } else {
		 flicker[htmlDOMelementId] = 0;
	 }
     if(responseText != lastValue[htmlDOMelementId]) {
         document.getElementById(htmlDOMelementId).style.color = color;
         lastValue[htmlDOMelementId] = responseText;
     }
	 
 }


 //}
 
 function Ajax_call(url, htmlDOMelementId) {
   	 //getLocation();
	 ////alert(lat);
 
 	 var instance = this;
	 this._xmlHttp.open('GET', url, true);
	 // inner anonymous function
	 this._xmlHttp.onreadystatechange = function() {
		 switch(instance._xmlHttp.readyState) {
		 case 1:
			 instance.loading();
			 break;
		 case 2:
			 instance.loaded();
			 break;
		 case 3:
			 instance.interactive();
			 break;
		 case 4:
			 // pass parameters
			 instance.complete(
					 htmlDOMelementId,
					 instance._xmlHttp.status,
					 instance._xmlHttp.statusText,
					 instance._xmlHttp.responseText,
					 instance._xmlHttp.responseXML);
			 break;
		 }
	 };
	 this._xmlHttp.send(null);
 }

 function Ajax_loading(){ }
 function Ajax_loaded(){ }
 function Ajax_interactive(){ }
 function Ajax_complete(htmlDOMelementId, status, statusText, responseText, responseHTML){ }

 // create static class functions
 Ajax.prototype.loading = Ajax_loading;
 Ajax.prototype.loaded = Ajax_loaded;
 Ajax.prototype.interactive = Ajax_interactive;
 Ajax.prototype.complete = Ajax_complete;
 Ajax.prototype.call = Ajax_call;

 // Base case: Switch the timer flag and call the main loop
 function doTimer(url,cell,speed,htmlDOMelementId) {
//     if(!timerStateArray[cell]) {
//    	  timerStateArray[cell] = 1;
          timedCall(url,cell,speed,htmlDOMelementId);
//     }
 }
 // Main timing loop calls itself
 function timedCall(url,cell,speed,htmlDOMelementId) {
 
     ajax[cell].call(url,htmlDOMelementId);
//     if(timerStateArray[cell]) {
           timers[cell] = setTimeout(function() { timedCall(url,cell,speed,htmlDOMelementId); }, speed); // no speed = max speed
//     }
 }
 
 function doPing(url) {
 }	
 
 function init() {
	 
 }
	function iterate() {
		t=setTimeout("iterate()",10);
	}
	function start() {
		//init();
		//geolocation();
		//load();
		//iterate();
	}
	
    