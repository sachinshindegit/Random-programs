$( document ).ready(function() {
    token = "uTFbNd5TPgIv%2FWuQS7n2985ovL%2BziQ%2FjeVVfkjWfpcVOSk%2BTkecij0%2BBLXkQHC%2FHbRgecztIkesHLsqnxByifySO6SN12QRj57Y3uNJYQ9ZZeM8ekSW6a6za4ZJUIeQve4ZdY%2BHI2suSCOQqhKnIyJk9nSVepDiH8GYHt2WJQf2yMaUDaJuFOYcNTHlRXSNxXtI6cuUv6wkdPEPaIV6HUBy6CoHxyG5eedxHvvl5emn2cKHwkLuk8qynrJxttwi7%2BaP8PDqQHmbNirwW0FjUehlGzIA3VTJzDTc5vMNt2ItEMW3N9ul0UVo2cXof0AX3cl2ExOMdPsgBaEQxufdipXjZ7kZU%2FUesxuf5PJzOEQgYIKj7kqoYHRq3VrEAOicPHSUwCaLdiUX5rjWaBzGHTEAk3NZE2CqZ2UJsKYwswYOJV%2FRoJF%2FVicr7sYVV9IuIYWX7ER7ORGPgoM82UKB4bMRLkYTcxgHG4F%2FCCUVUUHvHQahcmPCxWMFuvQNaleb4BAeLdloziX%2BnsjerqxMh9fyqgXw2R3h1DpNehpvlZkcwRE%2BqLjkIeYnkT7K8sGtPiF7tSdF4GbK%2FBWIH%2FZG3wZXNdtpG84GdQ9ZAWb0wCcwmnWGN9iSlFZsVYcSQaWWkDVU2fnCGLXmHCU%2BQ1HAc2072lNgiBrlPqYh%2FjzecxzTuBG5oUdQ4cMJar9PBUaWqo%2FBASM%2Bbu60K4pSTDkIWDb%2FpB6qZZfeOj9DWTDB2q1npy8ROewUhjk%2BVaSPoUjZIyBobmeuKoSHxvDfO%2FyRkeH%2BEU2lsKusLMyyVr%2Bg6pz%2BgQMkft%2Bd4VUDkMAbz7m4QgRb9cRUBaJ0MnVsopLp8dNF3GHX7eBgV";
    wsURI = "wss://stream.watsonplatform.net/speech-to-text/api/v1/recognize?watson-token=" + token+ "&model=en-US_BroadbandModel";
    websocket = new WebSocket(wsURI); 
    websocket.onopen = function(evt) { onOpen(evt) };
    websocket.onclose = function(evt) { onClose(evt) };
    websocket.onmessage = function(evt) { onMessage(evt) };
    websocket.onerror = function(evt) { onError(evt) };

    

});


function onMessage(evt) {
      console.log('recognition result onMessage: ' + evt.data);
      var parsed = JSON.parse(evt.data);
      var temp = document.getElementById("outputText").value;

      if(parsed.hasOwnProperty("state")){
          if(parsed.state=="listening"){
              listeningFlag = true;
          }
      }
      
      if(parsed.hasOwnProperty("results")){
	      if(parsed.results[0]!=null){
	          if(parsed.results[0].alternatives[0]!=null){
	            if(parsed.results[0].alternatives[0].transcript!=null){
	              temp = temp +" "+parsed.results[0].alternatives[0].transcript;
	              document.getElementById("outputText").value="";
	              document.getElementById("outputText").value = temp;
	            }
	          }
	      }
      }

      
  convertSpeech();
  
}
function onOpen(evt) {
  console.log("Connection Opened");

      var message = {'action': 'start', 'content-type': 'audio/wav;rate=44100'};
    websocket.send(JSON.stringify(message));

}
function onClose(evt) {
    console.log("Closed connection: " + evt.data);
    location.reload();
}

function onError(evt) {
  console.log('Error: ' + evt.data);
}


function convertSpeech(){
    if(myQueue.size() > 0){
      if(listeningFlag){
                websocket.send(myQueue.dequeue());
                websocket.send(JSON.stringify({'action': 'stop'}));
                listeningFlag=false;
        }
    }
}


function clearTextArea(){
	$('#outputText').val("");
}

function submitMyFile() {
	var message = {'action': 'start', 'content-type': 'audio/flac'};
    websocket.send(JSON.stringify(message));
    var x = document.getElementById("myFile").files[0];;
    var xblob = new Blob([x]);
    websocket.send(xblob);
    websocket.send(JSON.stringify({'action':'stop'}));
    //x.disabled = true;
    document.getElementById("submitButton").className = "btn btn-primary disabled";


}

function fileselect(){
	document.getElementById("submitButton").className = "btn btn-primary";
}


// Queue implementation
function Queue() {
    this._oldestIndex = 1;
    this._newestIndex = 1;
    this._storage = {};
}
 
Queue.prototype.size = function() {
    return this._newestIndex - this._oldestIndex;
};
 
Queue.prototype.enqueue = function(data) {
    this._storage[this._newestIndex] = data;
    this._newestIndex++;
};
 
Queue.prototype.dequeue = function() {
    var oldestIndex = this._oldestIndex,
        newestIndex = this._newestIndex,
        deletedData;
 
    if (oldestIndex !== newestIndex) {
        deletedData = this._storage[oldestIndex];
        delete this._storage[oldestIndex];
        this._oldestIndex++;
 
        return deletedData;
    }
};