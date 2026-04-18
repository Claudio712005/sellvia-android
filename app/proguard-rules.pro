-keep class **.BuildConfig { *; }

-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-keep class br.com.claus.sellvia.**.data.** { *; }
-keep class br.com.claus.sellvia.**.domain.model.** { *; }

-keep class org.koin.** { *; }
-keepnames class * { @org.koin.core.annotation.* *; }

-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

-keep class androidx.datastore.** { *; }

-keepattributes SourceFile, LineNumberTable
-renamesourcefileattribute SourceFile
