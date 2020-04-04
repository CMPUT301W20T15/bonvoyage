# BonVoyage
![](https://github.com/CMPUT301W20T15/bonvoyage/blob/master/doc/UI_Mockups_Images/header4.png)

Bon Voyage is developed as a mobile application that allows drivers to search for open and recent ride requests, and for riders to post requests for rides. Riders will describe the start and end of their ride and their suggested payment. Drivers will browse for nearby requests and accept ride requests that they are willing to fulfil.

On ride completion, drivers and riders use QR generated codes to complete payments.



## Getting Started

Please download the apk file <a href="">here</a> to run this on your android device.

The entire project can be downloaded from the master branch for running, developing and testing purposes. 

### Prerequisites

Android device requirements:

```
API: 16 or higher
```

Android Studio requirements:

```
Java SDK compatibility: 1.8
```

## Running the tests

If project is run on Android Studio, previously defined tests are provided in the src java folder

UI Testing with Robotium:

```
bonvoyage\app\src\androidTest\java\com\example\bonvoyage\
```

Unit Testing with JUnit
```
bonvoyage\app\src\test\java\com\example\bonvoyage\
```


## Built With

* [GoogleMaps API](https://developers.google.com/maps/documentation) - The maps api used
* [Robotium](https://github.com/RobotiumTech/robotium) - Intent Testing
* [FireBase](https://firebase.google.com/) - Used to generate/retrieve database to store user information
* [QRGenerator](https://github.com/androidmads/QRGenerator) - Used to generate QR Codes
* [MaterialDrawer](https://github.com/mikepenz/MaterialDrawer/tree/v6.1.2) - Create the navigation drawer 

## Contributing

```
All collaborators on this project have carefully discussed and divided areas by interest. For more information, please check the wikipage for this project.
```

## Authors
* **Nasiv Adhikari**  - [anasiv](https://github.com/anasiv)
* **Joe Ha**           - [jha8](https://github.com/jha8)
* **Debangana Ghosh**  - [orackle](https://github.com/orackle)
* **Nick Anderson**    - [nick98anderson](https://github.com/nick98anderson)
* **Nicole Hagerman**  - [nicolelisa](https://github.com/nicolelisa)
* **Nomar Chavez**     - [nmrchvz](https://github.com/nmrchvz)

See also the [wiki](https://github.com/CMPUT301W20T15/bonvoyage/wiki) for full contribution in this project

## License
```Copyright 2018 

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
## Acknowledgments

* Inspiration for splash screen and general theme from creator [Anggy Risky](https://www.youtube.com/channel/UCG1aEPR4NO2Sd_mmJFimfQQ) on youtube
* Animation inspiration from aforementioned creator
* All the precious stackoverflow help required to implement the QR generator and Google Maps API into our project


