

-keepparameternames
-keeppackagenames

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,MethodParameters

-keepnames class * {*;}
#-keepnames class *
-keep class com.appunity.logger.** {*;}
-keep class org.slf4j.LoggerFactory { *; }
-keep class org.slf4j.Logger { *; }
-keep class ch.qos.logback.classic.Logger { public synchronized void addAppender(...); }
-keep class ch.qos.logback.core.rolling.TimeBasedRollingPolicy { *; }
-keep class ch.qos.logback.core.rolling.ContextAwareBase { *; }
#-keep class ** {*;}


-dontwarn javax.servlet.**
-dontwarn javax.naming.**
-dontwarn javax.jms.**
-dontwarn javax.mail.**
-dontwarn javax.management.**
-dontwarn javax.xml.stream.**
-dontwarn org.codehaus.groovy.**
-dontwarn org.codehaus.janino.**
-dontwarn groovy.lang.**

-dontwarn org.slf4j.impl.StaticMDCBinder
-dontwarn org.slf4j.impl.StaticMarkerBinder
-dontwarn org.codehaus.commons.compiler.CompileException


-dontwarn ch.qos.logback.classic.net.**
-dontwarn ch.qos.logback.core.net.ssl.**
-dontwarn ch.qos.logback.classic.jmx.**
-dontwarn ch.qos.logback.classic.gaffer.**
-dontwarn ch.qos.logback.classic.joran.**
-dontwarn ch.qos.logback.core.net.**
-dontwarn ch.qos.logback.classic.BasicConfigurator

-assumenosideeffects class ch.qos.logback.classic.util.ContextInitializer {
    public void autoConfig();
}