# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
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

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

  -dontwarn com.baidu.**
  -keep class com.baidu.**{*; }

  -dontwarn com.amap.api.maps2d.overlay.**
  -dontwarn com.amap.api.mapcore2d.**
  -keep class com.amap.api.**{*;}
  -keep class com.autonavi.aps.amapapi.model.**{*;}
  -keep class com.loc.**{*;}
  -keep class com.amap.api.location.**{*;}

  -keep class com.amap.api.fence.**{*;}

  -keep class com.autonavi.aps.amapapi.model.**{*;}

  -keep class com.amap.api.maps2d.**{*;}

      -keep class com.amap.api.mapcore2d.**{*;}

      -dontwarn com.google.android.maps.**
      -dontwarn android.webkit.WebView
      -dontwarn com.umeng.**
      -dontwarn com.tencent.weibo.sdk.**
      -dontwarn com.facebook.**


      -keep enum com.facebook.**
      -keepattributes Exceptions,InnerClasses,Signature
      -keepattributes *Annotation*
      -keepattributes SourceFile,LineNumberTable

      -keep public interface com.facebook.**
      -keep public interface com.tencent.**
      -keep public interface com.umeng.socialize.**
      -keep public interface com.umeng.socialize.sensor.**
      -keep public interface com.umeng.scrshot.**

      -keep public class com.umeng.socialize.* {*;}
      -keep public class javax.**
      -keep public class android.webkit.**

      -dontwarn org.simalliance.openmobileapi.**
      -keep class com.unionpay.** { *; }
      -keep class com.UCMobile.PayPlugin.** { *; }
      -keep class org.simalliance.openmobileapi.** { *; }


      -keep class com.facebook.**
      -keep class com.umeng.scrshot.**
      -keep public class com.tencent.** {*;}
      -keep class com.umeng.socialize.sensor.**

      -keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}

      -keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}

      -keep class im.yixin.sdk.api.YXMessage {*;}
      -keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}

      -keep public class [com.administrator.elwj].R$*{
          public static final int *;
      }

-dontwarn android.support.v4.**     #缺省proguard 会检查每一个引用是否正确，但是第三方库里面往往有些不会用到的类，没有正确引用。如果不配置的话，系统就会报错。
-dontwarn android.os.**
-keep class android.support.v4.** { *; }        # 保持哪些类不被混淆
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
-keep class android.os.**{*;}
-keep class com.google.gson.**{*;}
-keep class com.android.volley.**{*;}
-keep class com.android.volley.**
-keep class eu.the4thfloor.volley.**{*;}
-keep class org.xutils.**{*;}
-keep class com.administrator.bean.**{*;}
-keep class com.library.listview.**{*;}
-keep class com.githang.viewpagerindicator.**{*;}

-keep interface android.support.v4.app.** { *; }
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.widget
-keep public class * extends com.sqlcrypt.database
-keep public class * extends com.sqlcrypt.database.sqlite
-keep public class * extends de.greenrobot.dao.**


-keepclasseswithmembernames class * {       # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {            # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {            # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity { #保持类成员
   public void *(android.view.View);
}

-keepclassmembers enum * {                  # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {    # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}