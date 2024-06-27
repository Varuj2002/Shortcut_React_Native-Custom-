package com.shortcut;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

public class ShortcutUtilModule(reactContext: ReactApplicationContext): ReactContextBaseJavaModule(reactContext) {


    private var shortcutUtil: ShortcutUtil? = ShortcutUtil(reactContext)


    @ReactMethod
    public fun setShortcutItems(items: ReadableArray) {
        shortcutUtil?.setShortcutItems(items);
    }

    override fun getName(): String {
        return "ShortcutUtil";
    }
}
