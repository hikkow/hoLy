package ru.holly.script

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_admin)
        val tvKey = findViewById<TextView>(R.id.tvGeneratedKey)
        
        findViewById<Button>(R.id.btnGenerate).setOnClickListener {
            tvKey.text = AccessManager.generateNewKey(this)
            Toast.makeText(this, "Ключ создан и добавлен в базу", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Button>(R.id.btnCopy).setOnClickListener {
            val key = tvKey.text.toString()
            if (key.isNotEmpty() && key != "Здесь появится ключ") {
                (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText("key", key))
                Toast.makeText(this, "Ключ скопирован!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
