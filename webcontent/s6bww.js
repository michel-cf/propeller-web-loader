/**
Provides a web worker process. 
**/

// ID for console output
var consoleID = 0;
    
/**
 * Send progress update to parent process.
 *
 * @method reportProgress
 * @param  {Number} value Percentage of progress so far.
 */
function reportProgress (value) {
    self.postMessage({msgType: 'progress', 'id': consoleID, 'value': value });
}

/**
 * Send a log message to the parent process.
 *
 * @method log
 * @param  {String} s The log message text.
 */
function log (s) {
    self.postMessage({msgType: 'log', 'id': consoleID, string: s });
}

/** 
* Pause all for some ms
* horrible solution
*/
//function pausecomp(millis)
// {
//  var date = new Date();
//  var curDate = null;
//  do { curDate = new Date(); }
//  while(curDate-date < millis);
//}

function loadS6B(data) {
    reportProgress (0);
	var root = data.root;
	var eepromOffset = data.eepromOffset;
    var size = data.size;
    log ('w root: ' + root);
    log ('w filesize: ' + size);
    log ('w eepromOffset: ' + eepromOffset);
    try {
        log ('w sending 0 bytes - PUT ' + root + 'load/' + eepromOffset + '/' + size);
        var outputBytes = new Array(); 
        var xhr = new XMLHttpRequest();
        xhr.open( 'PUT', root + 'load/' + eepromOffset + '/' + size , false );
        xhr.send(outputBytes.buffer);
        log ('w ' + xhr.statusText);
    
    } catch (e) {
        log ('w Exception load_s6b: ' + e );
        log ('w Exception load_s6b Stack: ' + e.stack );
    } finally {
        reportProgress (100);
    }
}
function sendS6B(data) {
	var root = data.root;
	var delay = data.delay;
	var chunkSize = data.chunkSize;
    var eepromOffset = data.eepromOffset; 
	var buffer = data.buffer;
	var bytes = new Uint8Array(buffer);
	var size = bytes.length;
	var index = 0;
	var progress = 0;
	var progressadd = 100;
	
    log ('w root: ' + root);
    log ('w filesize: ' + size);
    log ('w chunkSize: ' + chunkSize);
    log ('w eepromOffset: ' + eepromOffset);
    log ('w delay: ' + delay);
    if ((size/chunkSize)>0) {
        progressadd = 100 / (size/chunkSize);
    }
    reportProgress (0);
    try {
	    while (size>0) {
	    
//	        pausecomp(delay);   // horrible construction.
	        
    	    if (size<chunkSize) {
    	        chunkSize=size;
    	    } 
            log ('w sending ' + chunkSize + ' bytes from file ' + index + ' to eeprom ' + eepromOffset + ' - PUT ' + root + 'eeprom/' + eepromOffset);
    	    
            var output = new ArrayBuffer(chunkSize);
            var outputBytes = new Uint8Array(output);
            for (var i = 0; i < chunkSize; i++) {
                outputBytes[i] = bytes[index+i];
            }

            var xhr = new XMLHttpRequest();
            xhr.open( 'PUT', root + 'eeprom/' + eepromOffset  , false );
            xhr.send(outputBytes.buffer);
            log ('w ' + xhr.statusText);
            
            eepromOffset += chunkSize;
            index += chunkSize;
            size -= chunkSize;   
            progress += progressadd;
            reportProgress (progress);
	    }
    } catch (e) {
        log ('w Exception send_s6b: ' + e );
        log ('w Exception send_s6b Stack: ' + e.stack );
    } finally {
        reportProgress (100);
    }
}
function terminate() {
    self.postMessage({msgType: 'terminating', 'id': consoleID});
    self.close();    // Terminates the worker.
}
self.addEventListener('message', function(e) {
    var data = e.data;
    consoleID = data.id;
    switch (data.msgType) {
        case 'load_s6b':
            log ('w Load S6B Worker started - consoleID: ' + data.id);
            loadS6B(data);
            terminate();
            break;            
        case 'send_s6b':
            log ('w Send SB6 Worker started - consoleID: ' + data.id);
            sendS6B(data);
            terminate();
            break;
        case 'sendload_s6b':
            log ('w Send and Load S6B Worker started - consoleID: ' + data.id);
            sendS6B(data);
            loadS6B(data);
            terminate();
            break;
        case 'terminate':
            reportProgress (100);
            terminate();
            break;
        default:
            self.postMessage({msgType: "unknown", 'id': consoleID});
    };
}, false);
