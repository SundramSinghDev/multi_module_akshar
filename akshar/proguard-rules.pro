-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose
-dontpreverify
-allowaccessmodification
-mergeinterfacesaggressively
-overloadaggressively
-keepattributes *Annotation*

####################################################################  KEEP ANDROID SUPPORT V7 AND DESIGN
-keepclassmembers class * {
    private <fields>;
}
-keep public class * extends android.app.Activity
-keepclassmembers class * implements android.os.Parcelable {
static ** CREATOR;
}
-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep public class * extends android.support.v4.view.ActionProvider.* {
    public <init>(android.content.Context);
}

-keep class androidx.core.app.CoreComponentFactory.* { <init>(); }
 -dontwarn androidx.core.app.CoreComponentFactory.**

-keep class * extends com.akshar.store.* { *; }
-dontwarn com.akshar.store.**

-keep class androidx.appcompat.widget.* { *; }
-dontwarn androidx.appcompat.widget.**

-keep class com.google.zxing.* { *; }
-dontwarn com.google.zxing.**

-keep class com.journeyapps.* { *; }
-dontwarn com.journeyapps.**

-keep class com.google.code.gson.* { *; }
-dontwarn com.google.code.gson.**


-keep class com.google.android.material.* { *; }
-dontwarn com.google.android.material.**
-keep class com.google.android.gms.* { *; }
-dontwarn com.google.android.gms.**
-keep class androidx.legacy.* { *; }
-dontwarn androidx.legacy.**
-keep class com.astuetz.* { *; }
-dontwarn com.astuetz.**
-keep class com.daimajia.swipelayout.* { *; }
-dontwarn com.daimajia.swipelayout.**
-keep class com.balysv.* { *; }
-dontwarn com.balysv.**
-keep class de.hdodenhof.* { *; }
-dontwarn de.hdodenhof.**
-keep class com.loopj.android.* { *; }
-dontwarn com.loopj.android.**
-keep class com.google.firebase.* { *; }
-dontwarn com.google.firebase.**
-keep class com.github.bumptech.glide.* { *; }
-dontwarn com.github.bumptech.glide.**
-keep class androidx.constraintlayout.* { *; }
-dontwarn androidx.constraintlayout.**
-keep class androidx.multidex.* { *; }
-dontwarn androidx.multidex.**
-keep class androidx.recyclerview.* { *; }
-dontwarn androidx.recyclerview.**

-keep class com.github.chrisbanes.* { *; }
-dontwarn com.github.chrisbanes.**
-keep class om.github.pzienowicz.* { *; }
-dontwarn om.github.pzienowicz.**

-keep class android.view.View.* { *; }
-dontwarn android.view.View.**

-keep class com.tbuonomo.andrui.* { *; }
-dontwarn com.tbuonomo.andrui.**
-keep class com.pierfrancescosoffritti.androidyoutubeplayer.* { *; }
-dontwarn com.pierfrancescosoffritti.androidyoutubeplayer.**

-keep class com.google.android.play.* { *; }
-dontwarn com.google.android.play.**

-keep class org.jsoup.* { *; }
-dontwarn org.jsoup.**

-keep class junit.* { *; }
-dontwarn junit.**


-keep class com.facebook.network.connectionclass.* { *; }
-dontwarn com.facebook.network.connectionclass.**

-keep class com.facebook.android.* { *; }
-dontwarn com.facebook.android.**

-keep class com.razorpay.* {*;}
-dontwarn com.razorpay.*

-keep class androidx.cardview.* {*;}
-dontwarn androidx.cardview.*

-keep class androidx.drawerlayout.* {*;}
-dontwarn androidx.drawerlayout.*

-keep class androidx.lifecycle.* {*;}
-dontwarn androidx.lifecycle.*

-keep class com.google.dagger.* {*;}
-dontwarn com.google.dagger.*

-keep class com.squareup.retrofit2.* {*;}
-dontwarn com.squareup.retrofit2.*
-keep class com.squareup.okhttp3.* {*;}
-dontwarn com.squareup.okhttp3.*

-keep class com.github.ybq.* {*;}
-dontwarn com.github.ybq.*

-keep class com.github.smarteist.* {*;}
-dontwarn com.github.smarteist.*

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepattributes JavascriptInterface
-keepattributes *Annotation*

-optimizations !method/inlining/*

