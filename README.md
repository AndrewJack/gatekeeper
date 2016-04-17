# Gatekeeper

Android app to open an intercom system, which is connected to a [Particle](https://www.particle.io/) [Photon](https://docs.particle.io/datasheets/photon-datasheet/).

Created in collaboration with [@sjparkinson](https://github.com/sjparkinson).

## hackster.io

Visit our [hackster.io page](https://www.hackster.io/main-thread-technology/photon-powered-communal-door-b5d1d2) to see how we made it.

[![Photon Gatekeeper](https://hackster.imgix.net/uploads/cover_image/file/117370/IMG_4272.JPG?w=900&h=675&fit=min)](https://www.hackster.io/main-thread-technology/photon-powered-communal-door-b5d1d2)

## How to Use

1. Clone
2. Import with Android Studio
3. [Configure the app](#configuring-the-app)
3. Connect device or emulator
4. Build and run

### Configuring the App

All of the secret values of this app have been extracted into the `.secrets` directory. 
To run the app for with your photon and service you will need to configure the following values.

#### Particle service & Firebase

1. Login or create a [Firebase](https://www.firebase.com) account, then create a new app. 
2. In the directory `.secrets/props` there is a file called `app.properties.example`
3. Copy and rename this file in the same location, to `app.properties`
4. Fill in the config file with your particle and Firebase settings
    
    ```
    # This is an example app.properties file.
    particle_device=device-id
    particle_auth=Authorization: particle-auth
    firebase_endpoint=https://the-door-unlocker.firebaseio.com/
    firebase_secret=custom-auth-token
    ```
5. Add following to the `local.properties` file
    ```
    app.props.file=../.secrets/props/app.properties
    ```

#### Keystore(s)
To create an app that can be installed on a device and recieve GCM messages you'll need to create a keystore.
Learn how to sign the app here https://developer.android.com/tools/publishing/app-signing.html

Similar to the steps above:

1. In the directory `.secrets/props` there is a file called `keystore.properties.example`
2. Copy and rename this file in the same location, to `keystore-debug.properties` & `keystore-release.properties`
3. Fill in the config file with keystore alias and passwords
    
    ```
    # This is an example keystore.properties file.
    store=../.secrets/keys/app-release.keystore
    alias=your_alias
    storePass=your_keystore_password
    pass=your_password
    ```
4. Add following to the `local.properties` file
    ```
    keystore-debug.props.file=../.secrets/props/keystore-debug.properties
    keystore-release.props.file=../.secrets/props/keystore-release.properties
    ```

#### Google Cloud Messaging

I have already registered the current app ids for release and debug so first step is to change the `applicationId` in the `build.gradle` file.
To setup cloud messaging follow the guide in the Google developers documentation.

https://developers.google.com/cloud-messaging/android/start

Once you have a configuration file for release and debug (if you want a different AppId for debug).
Add them to `.secrets/google-services/release/google-services.json` & `.secrets/google-services/debug/google-services.json`

#### Fabric (Optional)

Fabric is a very useful tool for crash reporting, simple analytics, and beta distribution.
to use fabric add a `fabric.properties` file with your apiSecret & apiKey in the `/mobile` directory.

https://docs.fabric.io/android/fabric/integration.html#add-api-key-and-build-secret

## CI
| - | Status |
| ------------- | ------------- |
| master  | [![Circle CI](https://circleci.com/gh/AndrewJack/gatekeeper/tree/master.svg?style=svg)](https://circleci.com/gh/AndrewJack/gatekeeper/tree/master) |

License
-------

    Copyright 2016 Andrew Jack

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
