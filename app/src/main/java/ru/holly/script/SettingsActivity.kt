package ru.holly.script

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(s: Bundle?) {
        super.onCreate(s)
        setContentView(R.layout.activity_settings)
        SettingsStore.init(this)

        val spSpeed = findViewById<Spinner>(R.id.spSpeed)
        val speeds = listOf("Instant (0 ms)", "Very fast (10 ms)", "Fast (50 ms)", "Normal (100 ms)")
        spSpeed.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, speeds)
        spSpeed.setSelection(when (SettingsStore.speedMs) { 0 -> 0; 10 -> 1; 50 -> 2; else -> 3 })
        spSpeed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                SettingsStore.speedMs = when (pos) { 0 -> 0; 1 -> 10; 2 -> 50; else -> 100 }
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        val sbAlpha = findViewById<SeekBar>(R.id.sbAlpha)
        val tvAlpha = findViewById<TextView>(R.id.tvAlpha)
        sbAlpha.max = 100; sbAlpha.progress = SettingsStore.watermarkAlpha
        tvAlpha.text = "Transparency: ${SettingsStore.watermarkAlpha}%"
        sbAlpha.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, p: Int, f: Boolean) { SettingsStore.watermarkAlpha = p; tvAlpha.text = "Transparency: $p%" }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })

        val cb2 = findViewById<CheckBox>(R.id.cb2); val cb3 = findViewById<CheckBox>(R.id.cb3); val cb4 = findViewById<CheckBox>(R.id.cb4)
        val mask = SettingsStore.stickerCounts
        cb2.isChecked = (mask and 1) != 0; cb3.isChecked = (mask and 2) != 0; cb4.isChecked = (mask and 4) != 0
        val saveMask = {
            var m = 0
            if (cb2.isChecked) m = m or 1; if (cb3.isChecked) m = m or 2; if (cb4.isChecked) m = m or 4
            SettingsStore.stickerCounts = m
        }
        cb2.setOnCheckedChangeListener { _, _ -> saveMask() }; cb3.setOnCheckedChangeListener { _, _ -> saveMask() }; cb4.setOnCheckedChangeListener { _, _ -> saveMask() }

        val btnAdmin = Button(this).apply {
            text = "Admin Panel"; setTextColor(getColor(R.color.text_dim)); setBackgroundResource(R.drawable.card_red)
            setOnClickListener {
                val input = EditText(this@SettingsActivity).apply { hint = "Enter admin password"; inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD }
                AlertDialog.Builder(this@SettingsActivity).setTitle("Admin Access").setView(input)
                    .setPositiveButton("Enter") { _, _ ->
                        if (input.text.toString() == "seredkin2010!") startActivity(Intent(this@SettingsActivity, AdminActivity::class.java))
                        else Toast.makeText(this@SettingsActivity, "Wrong password!", Toast.LENGTH_SHORT).show()
                    }.setNegativeButton("Cancel", null).show()
            }
        }
        (findViewById<LinearLayout>(R.id.rootSettingsLayout)).addView(btnAdmin)
    }
}
