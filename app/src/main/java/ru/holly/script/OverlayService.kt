package ru.holly.script

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.TextView

class OverlayService : Service() {
    private lateinit var wm: WindowManager
    private var watermark: TextView? = null
    private var btn: TextView? = null
    private val engine by lazy { ScriptEngine(this) }

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        addWatermark()
        addStartButton()
    }

    private fun addWatermark() {
        val lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.CENTER }
        val alpha = (SettingsStore.watermarkAlpha / 100f * 255).toInt()
        val tv = TextView(this).apply {
            text = getString(R.string.watermark)
            setTextColor(Color.argb(alpha, 255, 255, 255))
            textSize = 18f
        }
        wm.addView(tv, lp)
        watermark = tv
    }

    private fun addStartButton() {
        val lp = WindowManager.LayoutParams(
            140, 140,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.START or Gravity.TOP
            x = 20
            y = 300
        }
        val b = TextView(this).apply {
            text = "▶"
            textSize = 36f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.parseColor("#334FC3F7"))
        }
        b.setOnClickListener {
            if (ClickerService.running) {
                ClickerService.running = false
                b.text = "▶"
            } else {
                ClickerService.running = true
                b.text = "■"
                engine.start(ClickerService.selectedScript)
            }
        }
        b.setOnTouchListener { v, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN -> {
                    lp.x = e.rawX.toInt() - 70
                    lp.y = e.rawY.toInt() - 70
                }
                MotionEvent.ACTION_MOVE -> {
                    wm.updateViewLayout(v, lp)
                }
            }
            false
        }
        wm.addView(b, lp)
        btn = b
    }

    override fun onDestroy() {
        watermark?.let { runCatching { wm.removeView(it) } }
        btn?.let { runCatching { wm.removeView(it) } }
        ClickerService.running = false
        super.onDestroy()
    }

    override fun onBind(i: Intent?): IBinder? = null
}
