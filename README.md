Ponyville Live for Android
==========================

### Building

First, you'll need both JDK7 and JDK8 installed. Ponyville Live for Android utilises Retrolambda to backport
Java 8 features to the Dalvik and ART JVMs. After having both set up, create `app/local.gradle`, it should
look something like this:

```groovy
retrolambda {
    jdk 'path/to/jdk8'
    oldJdk 'path/to/jdk7'
    javaVersion JavaVersion.VERSION_1_7
}
```

Next, obtain the google-services.json files you need from another project member, and place them in
`app/src/main` and `app/src/debug`. These are needed to compile GMS into the app.

Then you can use the provided gradle wrapper to compile and install the application on your device

```bash
$ ./gradlew :app:installDebug
```

or for Windows users,

```batch
> gradlew.bat :app:installDebug
```

### License

Copyright 2014, 2015, 2016 berwyn

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
