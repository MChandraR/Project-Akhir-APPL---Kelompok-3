# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep interface com.squareup.okhttp.** { *; }
-keep class okhttp3.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

-keep class com.mcr.e_library.app.MainMenu.** { *; }
-keep class com.mcr.e_library.app.Home.** { *; }
-keep class com.mcr.e_library.app.model.** { *; }
-keep class com.mcr.e_library.app.apiInterface.** { *; }
-keep class com.mcr.e_library.MainMenu.** { *; }
-keep class com.mcr.e_library.ui.fragment.Home.** { *; }
-keep class com.mcr.e_library.model.** { *; }
-keep class com.mcr.e_library.apiInterface.** { *; }

-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keepattributes *Annotation*

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

