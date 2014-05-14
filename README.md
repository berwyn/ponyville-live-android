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

Then you can use the provided gradle wrapper to compile and install the application on your device

```bash
$ ./gradlew :app:installDebug
```

or for Windows users,

```batch
> gradlew.bat :app:installDebug
```

### License

Unless otherwise noted, all source code files and layout assets are to be considered licensed under
the Apache v2 license. Branding, marketing assets, the name "Ponyville Live!" and other related media
are the property of Ponyville Live! and may be used under the terms of their branding agreement. This
application and compiled binaries derived from this unmodified source code are copyrighted material
© 2014 berwyn. Permission is hereby granted to utilise for personal use any binaries built from this
source code, however no rights are granted to redistribute generated binaries without written consent.