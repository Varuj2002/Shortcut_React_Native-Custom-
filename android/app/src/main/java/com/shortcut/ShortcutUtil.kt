// ShortcutPackage.kt

package com.shortcut

import android.R.attr.type
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.os.PersistableBundle
import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager


class ShortcutUtil(private val reactContext: ReactApplicationContext) {

    companion object {
        private const val ACTION_SHORTCUT = "shortcut.ACTION_SHORTCUT"
        private const val SHORTCUT_ITEM = "shortcut_item"
    }

    fun setShortcutItems(items: ReadableArray) {
        if (!isShortcutSupported() || items.size() == 0) {
            return
        }

        val currentActivity = reactContext.currentActivity ?: return
        val context: Context = reactContext

        val shortcuts = ArrayList<ShortcutInfo>(items.size())

        for (i in 0 until items.size()) {
            val item = ShortcutItem.fromReadableMap(items.getMap(i))

            val iconResId = context.resources.getIdentifier(item.icon, "drawable", context.packageName)
            val intent = Intent(context, currentActivity.javaClass)
            intent.action = ACTION_SHORTCUT
            intent.putExtra(SHORTCUT_ITEM, item.toPersistableBundle())

            val shortcut = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                ShortcutInfo.Builder(context, "id$i")
                    .setShortLabel(item.title)
                    .setLongLabel(item.title)
                    .setIcon(Icon.createWithResource(context, iconResId))
                    .setIntent(intent)
                    .build()
            } else {
                TODO("VERSION.SDK_INT < N_MR1")
            }

            shortcuts.add(shortcut)
        }

        val shortcutManager = context.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager
        shortcutManager?.setDynamicShortcuts(shortcuts)
    }

    private fun isShortcutSupported(): Boolean {
        val shortcutManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            reactContext.getSystemService(Context.SHORTCUT_SERVICE) as ShortcutManager?
        } else {
            TODO("VERSION.SDK_INT < N_MR1")
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            shortcutManager?.isRequestPinShortcutSupported ?: false
        } else {
            TODO("VERSION.SDK_INT < O")
        }
    }

    private class ShortcutItem(val title: String, val icon: String, val type: String) {

        fun toPersistableBundle(): PersistableBundle? {
            val bundle = PersistableBundle()
            bundle.putString("type", type)
            bundle.putString("title", title)
            bundle.putString("icon", icon)
//            bundle.putPersistableBundle("userInfo", userInfo.toPersistableBundle())
            return bundle
        }

        companion object {
            fun fromReadableMap(map: ReadableMap): ShortcutItem {
                val title = map.getString("title") ?: ""
                val icon = map.getString("icon") ?: ""
                return ShortcutItem(title, icon, "")
            }
        }
    }
}
