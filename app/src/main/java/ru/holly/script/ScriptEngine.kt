package ru.holly.script

import android.content.Context
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import ru.holly.script.ClickerService.Companion.instance as svc

class ScriptEngine(private val ctx: Context) {
    private val h = Handler(Looper.getMainLooper())
    private val P = { n: String -> PointsStore.get(ctx, n) }

    fun start(script: Int) {
        SettingsStore.init(ctx)
        if (script == 0) script003() else scriptNakl()
    }

    private fun delay(ms: Long, action: () -> Unit) {
        if (SettingsStore.speedMs == 0) h.post(action) else h.postDelayed(action, ms)
    }

    private fun script003() {
        if (!ClickerService.running) return
        svc.click(P("NINJUROK13")); delay(50) {
            svc.typeText("0.03"); svc.click(P("NINJUROKOK")); delay(50) {
                svc.click(P("NINJUROK10")); delay(50) {
                    svc.ocrRegion(P("NINJUROKBALANCE1"), P("NINJUROKBALANCE2")) { bal ->
                        if (bal.contains("0.03")) { svc.click(P("NINJUROK12")); delay(50) { svc.click(P("NINJUROKSELL")) } }
                        else { svc.click(P("NINJUROK11")) }
                        delay(50) { svc.click(P("NINJUROKFREE")); delay(50) { svc.click(P("NINJUROKSELLBACK")); loop { script003() } } }
                    }
                }
            }
        }
    }

    private fun scriptNakl() {
        if (!ClickerService.running) return
        svc.click(P("DARKLINE2")); delay(50) {
            svc.click(P("DARKLINEBUY")); delay(50) {
                svc.ocrRegion(P("DARKLINESTICKERTOP"), P("DARKLINESTICKERBOTTOM")) { st1 ->
                    svc.ocrRegion(P("DARKLINESTICKERSECONDTOP"), P("DARKLINESTICKERBOTTOM")) { st2 ->
                        val count = (st1.split("\n").filter { it.isNotBlank() }.size + st2.split("\n").filter { it.isNotBlank() }.size).coerceIn(1, 4)
                        val mask = SettingsStore.stickerCounts
                        val isOk = (count == 2 && (mask and 1) != 0) || (count == 3 && (mask and 2) != 0) || (count == 4 && (mask and 4) != 0)
                        if (isOk) { svc.click(P("DARKLINE3")); delay(50) { svc.click(P("DARKLINETOP")) } }
                        else { svc.click(P("DARKLINETOP1")); val top = P("DARKLINEBUY"); svc.swipe(top, Point(top.x, top.y - PointsStore.DARKLINE1)) }
                        loop { scriptNakl() }
                    }
                }
            }
        }
    }

    private fun loop(after: () -> Unit) { delay(100, after) }
}
