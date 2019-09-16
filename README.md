# logback-lite
Logback Lite for Android，based on the original logback, using proguard optimization

 [ ![Download](https://api.bintray.com/packages/zhangzhenli/maven/logback-lite/images/download.svg) ](https://bintray.com/zhangzhenli/maven/logback-lite/_latestVersion)


Download
--------

```groovy
repositories {
  maven { url 'https://dl.bintray.com/zhangzhenli/maven' }
}

dependencies {
  compile 'com.appunity:logback-lite:1.0.0'
}
```

Build
-----
Use these commands to create the AAR:

    git clone https://github.com/zawn/logback-lite.git
    cd logback-lite
    gradle :logger:clean :logger:proguard :logger:publish

The file is output to: `./logger/build/repos/com/appunity/logback-lite/1.0.0/logback-lite-1.0.0.jar

 [1]: http://logback.qos.ch
 [2]: https://github.com/zawn/logback-lite.git
 [3]: http://developer.android.com/sdk/index.html
