OBS Scene Que

_Basic application to easily control a scene sequence for OBS_

_By Samuel-Anton Jansen_

![Screenshot 1](img/screenshot1.png)

_Screenshot which shows scene "Another screen" as the current active que item. On the right, six Quick Access Buttons are shown._

#### Requirements

- Installation of OBS
- Installation of OBS Websocket (at least v4.0.0)
- Installation of Java (at least Java 1.8 / Java 8) on the computer where OBS Scene Que will be running

If OBS is running on another computer, make sure the computer where OBS Scene Que is running on can reach the other computer. Use the Websocket Scanner built in OBS Scene Que to quickly identify the other computer where OBS is running.

## Build

You can download all versions from [GitHub](https://github.com/sampie777/obs-scene-que/), or build this application yourself using Maven. [Launch4j](http://launch4j.sourceforge.net/) is used to generate .exe files from the compiled JARs (this is also integrated in the Maven package step).

## OBS Scene Que application setup

1. Download and **install [obs-websocket](https://github.com/Palakis/obs-websocket)** (>= v4.0.0) for your OBS application.
1. Make sure you have at least Java 8 installed.
1. Make sure your OBS websocket is discoverable by the computer you will run this application on. If it's the same computer, no worries. 
1. **Launch this application** (by running the executable JAR file with Java or running the EXE file) and enjoy.
1. **Edit the settings** if needed. This can be done using the menu Application menu -> Settings or editing the _obs-scene-que.properties_ file. This file will be created after first launch of the application. Don't run the application when editing this file. Application restart will be needed after changing the application settings.

### Features

### Que List

#### Creation

Create a que by drag-and-dropping scenes or other que items from the Sources panel (left panel) to the que list (middle panel).

#### Control

Control the que by using the control buttons "Previous" and "Next". These buttons will show which que item will be previous or next. Using the separate control window, the current que item will also be displayed for clarity. 

When a que item becomes active (thus the new current que index), it will be executed. This execution is different for each que item type. 

#### Execute After Previous
Mark a que item to be executed after the previous que item is executed. In this way, when a que item becomes active, all the directly following que items which are marked as Execute After Previous, will also be activated and executed in order.
 
Marking a que item as Execte After Previous: 
- `SHIFT + RIGHT ARROW` will mark a que item to be executed after the execution of the previous que item.
- `SHIFT + LEFT ARROW` will unmark a que item to be executed after the execution of the previous que item.


#### Quick Access Buttons

Drag and drop any que item from the Source or Que list onto a Quick Access Button to assign this item to this button. Clicking the Quick Access Button will execute the que item. Note that this will not interfere with the que at all.
Use `CTRL + MOUSE CLICK` on a Quick Access Button to clear this button's state. 


#### Network Scanner

When using OBS Scene Que on another computer as OBS projector itself, it can be quite a struggle to figure out the correct IP address of the OBS projector computer. Head to Application menu -> Network Scanner to let OBS Scene Que scan for any possible OBS projector hosts on your local network. This scanning is done by querying the default OBS websocket port 4444 on all possible IP address available on your local network(s). 

The default timeout used for the scanning progress is 200 ms. This value can be increased in the Network Scanner window if no OBS websockets are detected. Users of slow local networks may need do increase this value. 

#### Auto reconnect

When connection to OBS is lost, OBS Scene Que will continue keeping track of time, no matter what. Even after connection with OBS is re-established, the time for the same scene is still correct. Of course, the application cannot detect any scene changes while disconnected with OBS. 

OBS Scene Que will not attempt to reconnect immediately, but during a continuous interval of 3 seconds (as specified in the properties).

#### Themes
A application theme can be set in Application menu -> Settings. Currently, only Light theme is available and Dark theme is experimental. You can develop your own theme by extending the BaseTheme class and creating a pull request to the `develop` branch. 

#### Notifications

When an error or something else interesting occurs, a notification of this event will be available. Click on the "Notifications" button to show all these notifications. The user will be alerted for new notifications by the number of new notifications being displayed on this button. 

### Plugins

Plugins can be added to enable the use of different que item types which can perform different actions on execution/activation. The following plugins are internally installed:

- `OBSPlugin`: lets you activate OBS scenes
- `TextPlugin`: doesn't really do anything. It's just there to create some clarity and order in your que

To install new plugins, just place their .jar file in the plugin directory (see settings for where this directory is).

### Settings

Most settings can be changed using the GUI. All settings are visible (and configurable) in the settings file: _obs-scene-que.properties_. Some of these settings will be explained below. 

Don't run the application while editing the _obs-scene-que.properties_ file (and before saving your changes), as your changes won't be loaded until the next launch of the application. Also, the application will overwrite your changes if it is still running.

By deleting this file, all properties are reset to default. 

**Note:** Application restart is required to load any changes to the settings.

_Connection settings_
 
* `obsAddress` (string) (default: `ws://localhost:4444`): holds the full address of the OBS websocket server. This server can be on any computer in the same network of even over internet, as long as it can be reached by the obs-scene-timer application.
* `obsPassword` (string) (default: `<empty>`): the password needed to connect to the OBS websocket. Please note that this **password is stored as plain text** in the properties file and therefore is readable by anyone with access to this file.
* `obsReconnectionTimeout` (milliseconds) (default: `3000`): if connection with OBS failed or is lost, OBS Scene Timer will try to reconnect to OBS after this time in milliseconds.

_Other settings_

- `controlWindowLocation`: Pixel coordinates on where the control window will be located. To set the default location for the Control Window to the center of the main window, manually set the property `controlWindowLocation` to `-1,-1`. Other values will be interpreted to absolute coordinates relative to the left upper screen corner. 
- `pluginDirectory`: Directory location of the plugins to be enabled. Default is a directory named 'plugins' in the current directory.
- `queFile`: Location of the file where the current que will be loaded from and saved to.
- `quickAccessButtonCount`: How many Quick Access Buttons dou you want? Temporarily decreasing this number won't lose the configuration of the Quick Access Buttons that won't be visible anymore.

---

## Contribution

Feel free to contribute by opening a pull request to `develop` branch or leaving a comment somewhere.

Please try to add/edit tests for the code you've worked on. Also build the application with `mvn clean install` and run the compiled jar with `java -jar obs-scene-que-X.X.X-SNAPSHOT.jar`.