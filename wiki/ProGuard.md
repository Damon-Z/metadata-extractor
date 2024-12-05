[ProGuard](http://developer.android.com/tools/help/proguard.html) is a tool that shrinks, optimizes and obfuscates code, and is commonly used in **Android** development to reduce the size of `.apk`.

Thanks to @black-snow for providing these settings:

```text
-keep class com.drew.** {*;}
-keep interface com.drew.** {*;}
-keep enum com.drew.** {*;}
```

Please be aware that the above rules may not be ideal but they should hold your release apk together. Feel free to contribute an optimized ruleset.