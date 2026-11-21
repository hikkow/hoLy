package ru.holly.script

import android.content.Context
import android.graphics.Point
import org.json.JSONObject

object PointsStore {
    private const val PREF = "points"
    val NAMES = listOf(
        "NINJUROK6", "NINJUROK7", "NINJUROK10", "NINJUROK11", "NINJUROK12", "NINJUROK13",
        "NINJUROKFREE", "NINJUROKOK", "NINJUROKBALANCE1", "NINJUROKBALANCE2",
        "NINJUROKSELL", "NINJUROKSELLBACK",
        "DARKLINE2", "DARKLINETOP", "DARKLINETOP1", "DARKLINE3", "DARKLINEBUY",
        "DARKLINESTICKERTOP", "DARKLINESTICKERBOTTOM", "DARKLINESTICKERSECONDTOP",
        "DARKLINEBALANCE1", "DARKLINEBALANCE2"
    )
    
    fun get(ctx: Context, name: String): Point {
        val j = JSONObject(ctx.getSharedPreferences(PREF, 0).getString(name, "{}")!!)
        return Point(j.optInt("x", 500), j.optInt("y", 500))
    }
    
    fun set(ctx: Context, name: String, p: Point) {
        ctx.getSharedPreferences(PREF, 0).edit().putString(name,
            JSONObject().put("x", p.x).put("y", p.y).toString()).apply()
    }
    
    const val DARKLINE1 = 137
}
