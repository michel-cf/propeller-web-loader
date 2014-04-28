/**
Provides a web worker process for openspin.js. 
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

self.addEventListener('message', function(e) {
    var data = e.data;
//    console.log(data.msgType);
    switch (data.msgType) {
        case 'init_openspin':
            consoleID = data.id;
            ModuleConf['arguments'] = data.args;
            log ('w Worker started - consoleID: ' + data.id);
            reportProgress(10);
            try {
                openspin(ModuleConf);
                log ('w OpenSpin run time env. created.');
                reportProgress (20);
                // now we are playing pingpong with the calling process. execution continues with messages 'put_file' followed by a 'call_openspin'. 
                self.postMessage({msgType: 'init_openspin_done', 'id': consoleID, 'spinid': data.spinid});            
            } catch (e) {
                log ('w Exception init_openspin: ' + e );
                log ('w Exception init_openspin Stack: ' + e.stack );
                reportProgress (100);
                self.postMessage({msgType: 'terminating', 'id': consoleID});
                self.close();    // Terminates the worker.
            }
            break;
        case 'put_file':
            log ('w Writing ' + data.name + ' to MemFS.');
            try {
                ModuleConf.FS_createDataFile('', data.name, data.buffer, true, true, true);
            } catch (e) {
                if (e == 'Error: File exists') {
                    log ('w put_file: already there : ' + data.name);
                } else {
                    log ('w Exception put_file: ' + e );
                    log ('w Exception put_file Stack: ' + e.stack );
                }
            }
            break;
        case 'call_openspin':             
            reportProgress (50);
            log ('w Running openspin main');
            try {
                ModuleConf['callMain'](ModuleConf['arguments']);    // Call openspin's main method. 
                log ('w Finished openspin main.');
                reportProgress (80);
                // now we are playing pingpong with the calling process. execution continues with a message 'get_file' followed by a 'terminate'. 
                self.postMessage({msgType: 'call_openspin_done', 'id': consoleID});            
            } catch (e) {
                log ('w Exception call_openspin: ' + e );
                log ('w Exception call_openspin Stack: ' + e.Stack );
                reportProgress (100);
                self.postMessage({msgType: 'terminating', 'id': consoleID});
                self.close();    // Terminates the worker.
            }
            break;
        case 'get_file':
            log ('w Reading ' + data.name + ' from MemFS.');
            var u8b=new Uint8Array();
            try {
                u8b = ModuleConf['FS'].readFile(data.name, { encoding: 'binary' });
            } catch (e) {
                log ('w No such file.');
            }
            reportProgress (90);//get_file_done
            self.postMessage({msgType: 'get_file_done', 'id': consoleID, 'buffer': u8b});     
            break;
        case 'terminate':
            reportProgress (100);
            self.postMessage({msgType: 'terminating', 'id': consoleID});
            self.close();    // Terminates the worker.
            break;
        default:
            self.postMessage({msgType: "unknown", 'id': consoleID});
    };
}, false);


var ModuleConf = {};

ModuleConf['preRun'] = function () {
//    log ('preRun:');
};

ModuleConf['postRun'] = function () {
//    log ('postRun:');
};

ModuleConf['print'] = function print(s) {
    log ('+ ' + s);
};

ModuleConf['printErr'] = function printErr(s) {
    log ('- ' + s);
};
 
//ModuleConf['arguments'] = ['-b', 'test.spin']; 

// Let's not have main execute immeditaly on load.
ModuleConf['noInitialRun'] = true;

// Do not exit runtime 
ModuleConf['noExitRuntime'] = true;

// Pull in the openspin compiler
importScripts('openspin.js');

