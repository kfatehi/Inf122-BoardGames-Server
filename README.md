# Inf122-BoardGames
Informatics 122 Final Group Project for Board Games

## External Dependencies

* [javax.websocket-api-1.1.jar](http://central.maven.org/maven2/javax/websocket/javax.websocket-api/1.1/javax.websocket-api-1.1.jar)

You have two options for installing the dependencies. You can either use maven or manually download.

### Manual Download

Download each jar listed above, and place them in the `lib/` folder of the project (you may need to create this folder).

### Automatic Download

You can run `mvn dependency:copy-dependencies -DoutputDirectory=lib` or `make deps`

## Install
This should be quite easy. I made this project with IntelliJ but it should work fine with Eclipse for you. Tell me if it doesn't.

### IntelliJ
To install from IntelliJ, clone the repo, start IntelliJ, and you should choose the "Open Folder" option. Choose the root folder of the project. This should open the project with all configuration settings ready.

If for some reason you don't have a build/run config, follow these instructions. Near the upper right choose the dropdown and selected `Edit Configurations...`, press the `+` button, choose `Application`, change the name from `Unnamed` to `Main` and in the Main class field add `com.jlinnell.Main`.

This is optional if you're not using Maven and downloading the jars manually as listed above. Next right click on the jar files in IntelliJ's project explorer and near the bottom select "Add as Library". You should be all good now.

### Eclipse
File -> "Open Projects from Filesystem...", choose the root folder for the git repo and that should open the project just fine. Open the Main.java and click on the green run button and it _should_ run. If not, click the black arrow next to that button and choose "Run Configurations..." and make a new config much like above in IntelliJ.

Eclipse seems to find and include jar files in the `lib` folder automatically.

## Running
Right now, run as is. The default port is 8080. The console will give you a link.

The only endpoint implemented so far, for testing purposes, is `/echo` which reports your HTTP request back to you.
