package ru.holly.script

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.ClipData
import android.content.ClipboardManager
import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.Point
import android.os.Handler
import android.os.Looper
import android.view.Display
import android.view.accessibility.AccessibilityEvent
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class ClickerService : AccessibilityService() {
    companion object {
        var isRunning = false
        var running = false
        var selectedScript = 0
        lateinit var instance: ClickerService
    }

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val main = Handler(Looper.getMainLooper())

    override fun onServiceConnected() {
        isRunning = true
        instance = this
    }

    override fun onAccessibilityEvent(e: AccessibilityEvent?) {}
    override fun onInterrupt() {}

    fun click(p: Point) {
        val path = Path().apply { moveTo(p.x.toFloat(), p.y.toFloat()) }
        dispatchGesture(GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, 60)).build(), null, null)
    }

    fun swipe(from: Point, to: Point, dur: Long = 300) {
        val path = Path().apply { moveTo(from.x.toFloat(), from.y.toFloat()); lineTo(to.x.toFloat(), to.y.toFloat()) }
        dispatchGesture(GestureDescription.Builder().addStroke(GestureDescription.StrokeDescription(path, 0, dur)).build(), null, null)
    }

    fun typeText(s: String) {
        getSystemService(ClipboardManager::class.java).setPrimaryClip(ClipData.newPlainText("t", s))
    }

    fun ocrRegion(a: Point, b: Point, cb: (String) -> Unit) {
        takeScreenshot(Display.DEFAULT_DISPLAY, mainExecutor, object : TakeScreenshotCallback {
            override fun onSuccess(s: ScreenshotResult) {
                val hw = Bitmap.wrapHardwareBuffer(s.hardwareBuffer, s.colorSpace)
                val bmp = hw.copy(Bitmap.Config.ARGB_8888, false); hw.recycle()
                val crop = Bitmap.createBitmap(bmp, a.x, a.y, b.x - a.x, b.y - a.y)
                recognizer.process(InputImage.fromBitmap(crop, 0)).addOnSuccessListener { cb(it.text) }.addOnFailureListener { cb("") }
            }
            override fun onFailure(code: Int) { cb("") }
        }, main)
    }

    fun capture(cb: (Bitmap?) -> Unit) {
        takeScreenshot(Display.DEFAULT_DISPLAY, mainExecutor, object : TakeScreenshotCallback {
            override fun onSuccess(s: ScreenshotResult) {
                val hw = Bitmap.wrapHardwareBuffer(s.hardwareBuffer, s.colorSpace)
                val bmp = hw.copy(Bitmap.Config.ARGB_8888, false); hw.recycle()
                main.post { cb(bmp) }
            }
            override fun onFailure(code: Int) { main.post { cb(null) } }
        }, main)
    }
}
