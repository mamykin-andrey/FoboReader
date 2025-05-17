#-keepnames
# Hilt
-keeppackagenames
-keepnames @dagger.hilt.android.HiltAndroidApp class *
-keepnames @dagger.hilt.android.internal.managers.FragmentComponentManager$FragmentComponentBuilderEntryPoint class *
-keepnames @dagger.hilt.android.internal.managers.ViewComponentManager$ViewComponentBuilderEntryPoint class *
-keep,allowobfuscation @javax.inject.Named class *
-keep,allowobfuscation @dagger.hilt.* class *
-keep,allowobfuscation @dagger.* class *
-keep,allowobfuscation @com.google.inject.* class *
-keepclasseswithmembers class * {
    @javax.inject.* <fields>;
}
-keepclasseswithmembers class * {
    @dagger.* <fields>;
}
-keepclasseswithmembers class * {
    @javax.inject.* <init>(...);
}
-keepclasseswithmembers class * {
    @dagger.* <init>(...);
}
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponentManager { *; }

# OkHttp
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking class * extends com.google.gson.reflect.TypeToken
-keep,allowobfuscation,allowshrinking interface ru.mamykin.foboreader.read_book.translation.google.GoogleTranslateService

# AppCompat
-keep class androidx.appcompat.** { *; }
-keep interface androidx.appcompat.** { *; }