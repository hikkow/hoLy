package ru.holly.script

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class CalibrationActivity : AppCompatActivity() {
    private var idx = 0
    private var freeze: Bitmap? = null
    private var pending: Point? = null
    private lateinit var ivFreeze: ImageView
    private lateinit var ivCross: ImageView
    private lateinit var tvProgress: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvHint: TextView
    private lateinit var tvCoord: TextView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).apply { 
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE 
        }
        setContentView(R.layout.activity_calibration)
        ivFreeze = findViewById(R.id.ivFreeze); ivCross = findViewById(R.id.ivCross)
        tvProgress = findViewById(R.id.tvProgress); tvTitle = findViewById(R.id.tvTitle)
        tvHint = findViewById(R.id.tvHint); tvCoord = findViewById(R.id.tvCoord)

        if (!ClickerService.isRunning) {
            Toast.makeText(this, "Включи HolLy Script в Специальных возможностях", Toast.LENGTH_LONG).show()
            startActivity(android.content.Intent("android.settings.ACCESSIBILITY_SETTINGS")); finish(); return
        }
        findViewById<Button>(R.id.btnRetake).setOnClickListener { retake() }
        findViewById<Button>(R.id.btnBack).setOnClickListener { if (idx > 0) { idx--; render() } }
        findViewById<Button>(R.id.btnSkip).setOnClickListener { advance() }
        findViewById<Button>(R.id.btnOk).setOnClickListener {
            val p = pending
            if (p == null) { Toast.makeText(this, "Сначала тапни пипеткой", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
            PointsStore.set(this, TutorialData.steps[idx].key, p)
            Toast.makeText(this, "✓ ${TutorialData.steps[idx].key} = ${p.x},${p.y}", Toast.LENGTH_SHORT).show()
            advance()
        }
        ivFreeze.setOnTouchListener { _, e ->
            when (e.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> { 
                    ivCross.visibility = View.VISIBLE
                    ivCross.translationX = e.x - ivCross.width / 2f
                    ivCross.translationY = e.y - ivCross.height / 2f 
                }
                MotionEvent.ACTION_UP -> { 
                    ivCross.visibility = View.GONE
                    pending = Point(e.rawX.toInt(), e.rawY.toInt())
                    freeze?.let { ivFreeze.setImageBitmap(drawMark(it, pending!!)) }
                    tvCoord.text = "→ ${pending!!.x} : ${pending!!.y}" 
                }
            }; true
        }
        render(); retake()
    }
    private fun retake() {
        ClickerService.instance.capture { bmp ->
            if (bmp == null) { Toast.makeText(this, "Скриншот недоступен", Toast.LENGTH_SHORT).show(); return@capture }
            freeze = bmp; pending = null; ivFreeze.setImageBitmap(bmp); tvCoord.text = "Тапни нужную точку"
        }
    }
    private fun drawMark(src: Bitmap, p: Point): Bitmap {
        val out = src.copy(Bitmap.Config.ARGB_8888, true); val c = Canvas(out); val pt = Paint(Paint.ANTI_ALIAS_FLAG)
        pt.color = Color.parseColor("#4FC3F7"); pt.style = Paint.Style.STROKE; pt.strokeWidth = 4f
        c.drawCircle(p.x.toFloat(), p.y.toFloat(), 28f, pt); pt.strokeWidth = 2f
        c.drawLine(p.x - 44f, p.y.toFloat(), p.x + 44f, p.y.toFloat(), pt)
        c.drawLine(p.x.toFloat(), p.y - 44f, p.x.toFloat(), p.y + 44f, pt)
        return out
    }
    private fun advance() {
        if (idx < TutorialData.steps.size - 1) { idx++; render(); retake() }
        else { Toast.makeText(this, "Все точки записаны 🥷", Toast.LENGTH_LONG).show(); finish() }
    }
    private fun render() {
        val st = TutorialData.steps[idx]
        tvProgress.text = "ШАГ ${idx + 1}/${TutorialData.steps.size}  •  ${st.key}"
        tvTitle.text = st.title; tvHint.text = st.hint; tvCoord.text = "Тапни нужную точку"
    }
}
