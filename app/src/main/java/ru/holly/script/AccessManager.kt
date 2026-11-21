package ru.holly.script

import android.content.Context
import java.util.UUID

object AccessManager {
    private const val PREF = "access_prefs"
    private const val KEY_FIRST_LAUNCH = "first_launch"
    private const val KEY_ACTIVATED = "activated_key"
    private const val KEY_VALID_KEYS = "valid_keys_list"
    private const val BETA_DURATION_MS = 15 * 60 * 1000L

    fun init(ctx: Context) {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        if (!prefs.contains(KEY_FIRST_LAUNCH)) prefs.edit().putLong(KEY_FIRST_LAUNCH, System.currentTimeMillis()).apply()
    }
    fun isBetaValid(ctx: Context): Boolean {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        return (System.currentTimeMillis() - prefs.getLong(KEY_FIRST_LAUNCH, System.currentTimeMillis())) < BETA_DURATION_MS
    }
    fun getRemainingBetaTime(ctx: Context): Long {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val remaining = BETA_DURATION_MS - (System.currentTimeMillis() - prefs.getLong(KEY_FIRST_LAUNCH, System.currentTimeMillis()))
        return if (remaining > 0) remaining else 0
    }
    fun isActivated(ctx: Context): Boolean {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val currentKey = prefs.getString(KEY_ACTIVATED, "") ?: ""
        val validKeys = prefs.getString(KEY_VALID_KEYS, "") ?: ""
        return currentKey.isNotEmpty() && validKeys.contains(currentKey)
    }
    fun activateKey(ctx: Context, key: String): Boolean {
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val validKeys = prefs.getString(KEY_VALID_KEYS, "") ?: ""
        if (validKeys.contains(key)) {
            prefs.edit().putString(KEY_ACTIVATED, key).apply()
            return true
        }
        return false
    }
    fun generateNewKey(ctx: Context): String {
        val newKey = "HOLY-" + UUID.randomUUID().toString().substring(0, 8).uppercase()
        val prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
        val validKeys = prefs.getString(KEY_VALID_KEYS, "") ?: ""
        val updatedKeys = if (validKeys.isEmpty()) newKey else "$validKeys,$newKey"
        prefs.edit().putString(KEY_VALID_KEYS, updatedKeys).apply()
        return newKey
    }
}
