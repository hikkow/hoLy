package ru.holly.script

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AccessActivity : AppCompatActivity() {
    private var timer: CountDownTimer? = null
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        AccessManager.init(this)
        setContentView(R.layout.activity_access)
        val tvStatus = findViewById<TextView>(R.id.tvAccessStatus)
        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val btnBuy = findViewById<Button>(R.id.btnBuy)
        val etKey = findViewById<EditText>(R.id.etKey)
        val btnActivate = findViewById<Button>(R.id.btnActivate)
        val btnContinueBeta = findViewById<Button>(R.id.btnContinueBeta)

        if (AccessManager.isActivated(this)) { goToLogin(); return }

        if (AccessManager.isBetaValid(this)) {
            tvStatus.text = "Beta mode active"
            btnContinueBeta.visibility = Button.VISIBLE
            val remaining = AccessManager.getRemainingBetaTime(this)
            timer = object : CountDownTimer(remaining, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val min = millisUntilFinished / 60000
                    val sec = (millisUntilFinished % 60000) / 1000
                    tvTimer.text = "Time left: %02d:%02d".format(min, sec)
                }
                override fun onFinish() {
                    tvTimer.text = "Time expired!"
                    btnContinueBeta.visibility = Button.GONE
                    btnBuy.visibility = Button.VISIBLE; etKey.visibility = EditText.VISIBLE; btnActivate.visibility = Button.VISIBLE
                    tvStatus.text = "Beta test finished. Key required."
                }
            }.start()
            btnContinueBeta.setOnClickListener { goToLogin() }
        } else {
            tvStatus.text = "Beta test finished.\nActivation key required."
            tvTimer.text = "Price: 200 RUB or 250 gold (1 activation forever)"
            btnContinueBeta.visibility = Button.GONE
            btnBuy.setOnClickListener { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/holysct"))) }
            btnActivate.setOnClickListener {
                val key = etKey.text.toString().trim().uppercase()
                if (key.isEmpty()) { Toast.makeText(this, "Enter key", Toast.LENGTH_SHORT).show(); return@setOnClickListener }
                if (AccessManager.activateKey(this, key)) { Toast.makeText(this, "Key activated!", Toast.LENGTH_LONG).show(); goToLogin() }
                else { Toast.makeText(this, "Invalid or used key", Toast.LENGTH_LONG).show() }
            }
        }
    }
    private fun goToLogin() { timer?.cancel(); startActivity(Intent(this, LoginActivity::class.java)); finish() }
    override fun onDestroy() { super.onDestroy(); timer?.cancel() }
}
