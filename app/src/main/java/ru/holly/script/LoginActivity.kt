package ru.holly.script

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private val PASSWORD = "HoLy1337"
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_login)
        val input = findViewById<EditText>(R.id.etPass)
        val btn = findViewById<Button>(R.id.btnEnter)
        val go = {
            if (input.text.toString() == PASSWORD) { startActivity(Intent(this, MainActivity::class.java)); finish() }
            else { Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show() }
        }
        btn.setOnClickListener { go() }
        input.setOnEditorActionListener { _, a, _ -> if (a == EditorInfo.IME_ACTION_DONE) go(); true }
    }
}
