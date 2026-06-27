# EasyNiddleNavigation Consumer Rules
# These rules are automatically applied to the app that uses this library.

# 1. Preserve the public API of the library
-keep public class dev.ishant.easyniddle.navigation.** {
    public protected *;
}

# 2. Keep all implementations of SerializableRoute
# Critical for navigation and state restoration in the consumer app.
-keep interface dev.ishant.easyniddle.navigation.SerializableRoute { *; }
-keep class * implements dev.ishant.easyniddle.navigation.SerializableRoute { *; }

# 3. Kotlin Serialization specific rules
# Instead of a broad -keepattributes, we target the serialization logic.
-keepclassmembers class * {
    *** Companion;
    *** $serializer;
}

-keepclassmembers class * {
    @kotlinx.serialization.Serializer *;
}

# 4. Preserve NavKey (base of Navigation 3)
-keep interface androidx.navigation3.runtime.NavKey { *; }
