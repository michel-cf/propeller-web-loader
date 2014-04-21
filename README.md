Propeller web-loader
=======================

Introduction
-----------------

This project provides others with the ability to compile Parallax Propeller programs and load them into the microcontroller.

* Parallax Propeller http://www.parallax.com/microcontrollers/propeller
* Propeller-load https://sites.google.com/site/propellergcc/documentation/tutorials/load-propgcc-code-images
* Openspin cross platform Spin/PASM Compiler https://code.google.com/p/open-source-spin-compiler/

The latest version can be downloaded from http://owncloud.creatingfuture.eu/public.php?service=files&t=0eae565db031cd3ca3f1163de91c5984
(It needs Java 1.7 to run)

Technical overview
-----------------

BlocklyProp runs in the browser, but is started using a bat file.
This will start a Java application that contains an embedded Jetty server. When the server has started it will open your browser.

This contains a cross platform compiler and way to load the application in the microcontroller.

Currently the bat file only starts the java jar: "java -jar BlocklyProp-1.0-SNAPSHOT.jar", but might be extended with more functionality in a later stage.

$ git clone https://github.com/michel-cf/propeller-web-loader.git

It is created as a maven project wich should make importing and building the project very easy.
As there are a lot of static files I used the assembly plugin to create a zip file with all the files.
(The application with dependencies, the bat file to start it, the compiler and all static files put in the webcontent directory)


Usage
-----------------

1. On Windows run prop-web-loader.bat to start the server (This might give some warning from Java, Windows and/or your firewall.
On Linux run ?? to start the server. (This still needs to be implemented)
2. Your browser should start, if not please open it manualy. The url will be shown in the console opened by the blocklyprop.bat file. It will be something as "Open url: http://localhost:57328")
3. Drag and drop Blocks to create your program. If you are ready to test it click on Compile, Load into Ram or Load into Eeprom as wished.
4. If you wish you can save your project
5. To stop the server, click Ctrl+c in the console and confirm.


Credit
-----------------

To compile your code I use Openspin https://code.google.com/p/open-source-spin-compiler/
And to do the interaction the microcontroller I use Propeller-load from the Propgcc toolchain https://sites.google.com/site/propellergcc/

Thanks to you all.

License
-----------------
Copyright (C) 2014 Michel Lampo michel@creatingfuture.eu

```
 Permission is hereby granted, free of charge, to any person
 obtaining a copy of this software and associated documentation
 files (the "Software"), to deal in the Software without
 restriction, including without limitation the rights to use,
 copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the
 Software is furnished to do so, subject to the following
 conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 OTHER DEALINGS IN THE SOFTWARE.
```
