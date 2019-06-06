#************************* 1、Input/Output Options *************************
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
#************************* 1、Input/Output Options *************************



#************************* 2、Keep Options *************************
#-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# Keep serializable classes and necessary members for serializable classes
# Copied from the ProGuard manual at http://proguard.sourceforge.net.
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.ActivityGroup
-keep public class * extends android.os.Binder
-keep public class * extends android.app.backup.BackupAgentHelper

-keep class * extends android.widget.** { *; }

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keep public class * extends android.view.** {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    void set*(***);
    *** get*();
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

# system
-keep class javax.**
-keep public interface javax.**
-keep class android.net.** { *; }
-keep class android.webkit.** { *; }
-keep class android.support.v4.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class com.buzz.funny.free.R$*{
    public static final int *;
}

#************************* 2、Keep Options *************************



#************************* 3、Shrinking Options *************************
#************************* 3、Shrinking Options *************************



#************************* 4、Optimization Options *************************
-optimizationpasses 5
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!class/unboxing/enum
#************************* 4、Optimization Options *************************



#************************* 5、Obfuscation Options *************************
-dontusemixedcaseclassnames
-keepattributes Exceptions,InnerClasses,SourceFile,LineNumberTable,Signature,*Annotation*
#************************* 5、Obfuscation Options *************************



#************************* 6、Preverification Options *************************
-dontpreverify
#************************* 6、Preverification Options *************************



#************************* 7、General Options *************************
-verbose

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version. We know about them, and they are safe.
-dontwarn android.support.**
-dontwarn android.net.**
-dontwarn android.webkit.**
-dontwarn android.webkit.WebView
-dontwarn javax.**
#************************* 7、General Options *************************

-ignorewarnings

# evernote
-dontwarn com.evernote.android.job.gcm.**
-dontwarn com.evernote.android.job.GcmAvailableHelper
-dontwarn com.evernote.android.job.work.**
-dontwarn com.evernote.android.job.WorkManagerAvailableHelper

-keep public class com.evernote.android.job.v21.PlatformJobService
-keep public class com.evernote.android.job.v14.PlatformAlarmService
-keep public class com.evernote.android.job.v14.PlatformAlarmReceiver
-keep public class com.evernote.android.job.JobBootReceiver
-keep public class com.evernote.android.job.JobRescheduleService
-keep public class com.evernote.android.job.gcm.PlatformGcmService
-keep public class com.evernote.android.job.work.PlatformWorker

# referralsio
-dontwarn io.referrals.lib.**
-keep class io.referrals.lib.** {*;}

# mta
-keep class com.tencent.stat.*{*;}
-keep class com.tencent.mid.*{*;}