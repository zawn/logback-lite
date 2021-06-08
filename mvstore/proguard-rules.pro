

#-keepparameternames
-keeppackagenames

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,MethodParameters

-keepnames class * {*;}
#-keepnames class *
-keep class org.h2.mvstore.** {*;}
#-keep class org.h2.mvstore.MVStore$Builder {*;}
#-keep class org.h2.mvstore.MVStore { *; }
#-keep class org.h2.mvstore.OffHeapStore { *; }
#-keep class org.h2.mvstore.rtree.MVRTreeMap { *; }
#-keep class org.h2.mvstore.rtree.SpatialKey { *; }
#-keep class org.h2.mvstore.tx.TransactionStore { *; }
#-keep class org.h2.store.fs.FileChannelInputStream { *; }
#-keep class ** {*;}