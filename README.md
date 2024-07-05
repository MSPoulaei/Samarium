# Overview
Samarium is a network side application developed for android devices, enabling users to measure quality of services (QOS) in cellular networks while commuting. Basically, it tracks the user's live location using `Google Play Services Location` APIs while extracting cells' information using `android telephony` APIs. Furthermore, the application functions properly in unreachable locations such as tunnels, using `Euclidean` method. Notably, here are some network metrics that Smarium is able to extract:
- Signal quality indicator parameters in 4G and 3G  such as `RSRQ`,`EC/N0`
- Signal quantity indicator parameters in 4G and 3G such as `RSRP`,`RSCP`
- Cell-specific information such as `PLMN`,`LAC`,`TAC`
The retrieved information are displayed on the map in the end. The following table illustrates how information are displayed on the map:

| color                                                         | range                 | signal interpretation |
| ------------------------------------------------------------- | --------------------- | --------------------- |
| <span style="color: #00703c; font-size: 20px;">&#9679;</span> | $$ -80 < x < \infty$$ | Excellent             |
| <span style="color: #00a032; font-size: 20px;">&#9679;</span> | $$ -85 < x < 80$$     | Very Good             |
| <span style="color: #00d228; font-size: 20px;">&#9679;</span> | $$ -90 < x < 85$$     | Good                  |
| <span style="color: #ffff00; font-size: 20px;">&#9679;</span> | $$ -95 < x < 90$$     | Fair                  |
| <span style="color: #ffaa00; font-size: 20px;">&#9679;</span> | $$ -100 < x < 95$$    | Poor                  |
| <span style="color: #fa6400; font-size: 20px;">&#9679;</span> | $$ -105 < x < 100$$   | Very Poor             |
| <span style="color: #ff0000; font-size: 20px;">&#9679;</span> | $$ -110 < x < 105$$   | Bad                   |
| <span style="color: #dc133c; font-size: 20px;">&#9679;</span> | $$ -115 < x < 110$$   | Very Bad              |
| <span style="color: #820000; font-size: 20px;">&#9679;</span> | $$ -120 < x < 115$$   | Awful                 |
| <span style="color: #aaaaaa; font-size: 20px;">&#9679;</span> | No coverage           | Undefined             |
# Proposed Features
## Permission validator
The program needs some permissions such as **location access** to work flawlessly. Consequently, it asks for required permissions (if not granted) on reboot. 
- location access permission
<br/>
![location access image](/images/loc-perm.jpg)
- ask to activate GPS 
<br/>
![gps enable image](/images/gps-enable.jpg)

***Although the location access permission is required, the GPS service must be activated as well so the program can work well.***



## Customize configuration
- Generally, the program updates information every 10 seconds. However, this configuration can be modified using setting panel. 
- The internal database, that contains application information, can also be cleaned up for speed up purposes.
![setting image](/images/setting.jpg)
## Location estimation
There are situations where the GPS provides no services. As a result, the location access service through GPS might be unavailable and, the user's current location has to be estimated in some ways. Samarium uses `Euclidean` method to estimate the exact location of the user. Basically, it uses the last known location before losing GPS connection and the first available location after reconnection of GPS to approximate the longitude and latitude of unreachable locations. Finally, it divides the unknown distance into equal sections to assign estimated longitude and latitude to them.
The core functionality of this method is implemented in `/app/src/main/java/ir/tehranshomal/samarium/Services/InformationFetchService.kt` of application tree. 
