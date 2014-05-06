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