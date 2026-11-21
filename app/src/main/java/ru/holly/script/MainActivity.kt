package ru.holly.script

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnScripts).setOnClickListener { startActivity(Intent(this, ScriptsActivity::class.java)) }
        findViewById<Button>(R.id.btnCreator).setOnClickListener { startActivity(Intent(this, CalibrationActivity::class.java)) }
        findViewById<Button>(R.id.btnSettings).setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }
    }
}
