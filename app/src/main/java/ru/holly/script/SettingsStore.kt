package ru.holly.script

import android.content.Context

object SettingsStore {
    private const val PREF = "holly_settings"
    private lateinit var prefs: android.content.SharedPreferences
    
    fun init(ctx: Context) { 
        prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE) 
    }
    
    var speedMs: Int 
        get() = prefs.getInt("speed_ms", 0)
        set(value) = prefs.edit().putInt("speed_ms", value).apply()
        
    var watermarkAlpha: Int 
        get() = prefs.getInt("watermark_alpha", 50)
        set(value) = prefs.edit().putInt("watermark_alpha", value).apply()
        
    var stickerCounts: Int 
        get() = prefs.getInt("sticker_counts", 0b111)
        set(value) = prefs.edit().putInt("sticker_counts", value).apply()
}
