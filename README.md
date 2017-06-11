# DataWedge-WebSockets-Bridge
Interface with Zebra DataWedge using WebSockets 

**Read the blog associated with this application at https://darryncampbellblog.wordpress.com/2017/06/11/datawedge-to-websockets-bridge/**

*This application is provided without guarantee or warranty*
=========================================================

# Architecture
![Alt text](https://raw.githubusercontent.com/darryncampbell/datawedge-websockets-bridge/master/screenshots/architecture.png "Architecture")


# Steps to use the demo:

1. Configure a DataWedge profile as described earlier and ensure the profile is active when you launch the browser
2. Install the bridge application and either launch it or scan a barcode to start the WebSocket server
3. Start a web server and host the demo page
4. Navigate to the demo webpage on the device using Chrome or a similar browser capable of WebSockets
5. Scan a barcode, if everything is set up correctly you should see scans appear on the page.  If you do not see scans, check the DataWedge setup and whether the WebSockets are connected, the status is shown in blue on the page.
6. Disable and re-enable the laser using the buttons on the page, again if everything is set up correctly the buttons should affect the laser.

# Web application
![Alt text](https://raw.githubusercontent.com/darryncampbell/datawedge-websockets-bridge/master/screenshots/chrome.png "Web application")

# DataWedge configuration
![Alt text](https://raw.githubusercontent.com/darryncampbell/datawedge-websockets-bridge/master/screenshots/datawedge_profile_screenshot.png "DataWedge Configuration")

# Bridge application
![Alt text](https://raw.githubusercontent.com/darryncampbell/datawedge-websockets-bridge/master/screenshots/datawedge_websockets_bridge.png "Bridge Application")

# Questions?
* **Read the blog associated with this application at https://darryncampbellblog.wordpress.com/2017/06/11/datawedge-to-websockets-bridge/**
* Leave a comment on that blog or an issue in this repo
