# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\android-sdk\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# ApacheHttp
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-dontwarn android.webkit.*

-dontwarn com.google.android.gms.**
-keep class com.google.android.gms.**

# Support library
-keep public class * extends android.support.v7.app.Fragment
-keep class android.support.v7.app.** { *; }
-keep interface android.support.v7.app.** { *; }
-dontwarn android.support.v7.**

# Keep the pojos used by GSON or Jackson
-keep class com.futurice.project.models.pojo.** { *; }

# Keep GSON stuff
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

# Keep these for GSON, Jackson and Crashlytics
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes SourceFile,LineNumberTable

# Keep these for Custom Exceptions
-keep public class * extends java.lang.Exception

# Application classes that will be serialized/deserialized over Gson
-keep class com.dhb.** { *; }
-keep class com.android.internal.telephony.** { *; }
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-dontwarn okio.**

-keep class javax.security.** { *; }
-keep class java.beans.** { *; }
-keep class java.awt.** { *; }

# Library RoundCornerProgressBar
-keep class com.akexorcist.** { *; }
-dontwarn com.akexorcist.**

# Library Ramotion Folding Cell
-keep class com.ramotion.** { *; }
-dontwarn com.ramotion.**

# Library Bumptech Glide
-keep class com.github.** { *; }
-dontwarn com.github.**

# Library Zxing and Gson
-keep class com.google.** { *; }
-dontwarn com.google.**

# Library Journey Apps Barcode Scanner
-keep class com.journeyapps.** { *; }
-dontwarn com.journeyapps.**

# Android Support Libraries
-keep class com.android.** { *; }
-dontwarn com.android.**

#Crashlytics library
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**