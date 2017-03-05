# Inf122-BoardGames
Informatics 122 Final Group Project for Board Games

## Install
This should be quite easy. I made this project with IntelliJ but it should work fine with Eclipse for you. Tell me if it doesn't.

### IntelliJ
To install from IntelliJ, clone the repo, start IntelliJ, and you should choose the "Open Folder" option. Choose the root folder of the project. Near the upper right choose the dropdown and selected `Edit Configurations...`, press the `+` button, choose `Application`, change the name from `Unnamed` to `Main` and in the Main class field add `com.jlinnell.Main`.

Next download the websocket jar file from here http://central.maven.org/maven2/javax/websocket/javax.websocket-api/1.1/javax.websocket-api-1.1.jar and put it in `/path/to/projectfolder/lib/`. Once that's done, right-click on that jar file in IntelliJ's project explorer and near the bottom select "Add as Library". You should be all good now.

### Eclipse
File -> "Open Projects from Filesystem...", choose the root folder for the git repo and that should open the project just fine. Open the Main.java and click on the green run button and it _should_ run. If not, click the black arrow next to that button and choose "Run Configurations..." and make a new config much like above in IntelliJ. Eclipse seems to find and include the websocket jar file automatically.

## Dependencies
The only major dependency so far is `com.sun.net.httpserver.*` which _should_ be on everyone's install.

Added also is Websockets. It can be added via Maven/Gradle but I don't know how to make use those (especially if it's between two IDEs) so right now we're just manually including the jar file for websockets. Explained above in Installation.

## Running
Right now, run as is. The default port is 8080. The console will give you a link.

The only endpoint implemented so far, for testing purposes, is `/echo` which reports your HTTP request back to you.
