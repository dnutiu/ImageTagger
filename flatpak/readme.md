# Flatpak

This directory contains the flatpak build files.

If you are on Linux and would like to install this application as a Flatpak image then
build the application with gradle:

```
# in parrent folder:
./gradlew jlinkZip
```

And execute:

```shell
cd flatpak
./debug.sh
```
