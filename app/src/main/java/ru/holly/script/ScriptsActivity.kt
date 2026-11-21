package ru.holly.script

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ScriptsActivity : AppCompatActivity() {
    private val SCRIPTS = listOf("0.03 | HoLy", "HoLy | Nakl")
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_scripts)
        val container = findViewById<LinearLayout>(R.id.scriptsList)
        SCRIPTS.forEachIndexed { i, name ->
            val b = Button(this).apply {
                text = "${i + 1}.  $name"; textAllCaps = false; setTextColor(getColor(R.color.text))
                setBackgroundResource(R.drawable.card_blue); setPadding(32, 32, 32, 32)
                setOnClickListener {
                    if (!ClickerService.isRunning) { startActivity(Intent("android.settings.ACCESSIBILITY_SETTINGS")); return@setOnClickListener }
                    ClickerService.selectedScript = i; ClickerService.running = true
                    startService(Intent(this, OverlayService::class.java)); finish()
                }
            }
            container.addView(b)
        }
    }
}
