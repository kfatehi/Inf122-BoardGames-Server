# Inf122-BoardGames
Informatics 122 Final Group Project for Board Games

## Install
This should be quite easy. I made this project with IntelliJ but it should work fine with Eclipse for you. Tell me if it doesn't.

To install from IntelliJ, clone the repo, start IntelliJ, and you should choose the "Open Folder" option. There might be a similar option in Eclipse. If you are committing with Eclipse add a .gitignore entry for any Eclipse files, I already did so with `.idea/*`

## Dependencies
The only major dependency so far is `com.sun.net.httpserver.*` which _should_ be on everyone's install.

## Running
Right now, run as is. The default port is 8080. The console will give you a link.

The only endpoint implemented so far, for testing purposes, is `/echo` which reports your HTTP request back to you.
