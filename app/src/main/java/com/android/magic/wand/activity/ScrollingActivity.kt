package com.android.magic.wand.activity

import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.android.magic.wand.Constants.Config
import com.android.magic.wand.R
import com.android.magic.wand.utils.Utils
import com.android.magic.wand.view.BubblePop
import kotlinx.android.synthetic.main.activity_scrolling.*

class ScrollingActivity : AppCompatActivity() {

    private val mHandler = Handler(Looper.getMainLooper())
    private var mBubblePop: BubblePop? = null
    private var mMemText: TextView? = null
    private var mCacheText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)

        mBubblePop = findViewById(R.id.toolbar_image)
        mMemText = findViewById(R.id.clean_mem)
        mCacheText = findViewById(R.id.clean_cache)
        scanClean()
    }

    private fun scanClean(){
        mBubblePop!!.setSpeed(20, false)
        mBubblePop!!.start()
        animateMemoryText(500 * 1024 * 1024f)
        animateCacheText(900 * 1024 * 1024f)
        mHandler.postDelayed({
            mBubblePop!!.stop()
        }, Config.ANIMATION_DURATION + 1000)
    }

    private fun animateMemoryText(usedMemory: Float) {
        if (usedMemory <= 0) {
            mMemText!!.text = ""
            return
        }
        val valueAnimator = ValueAnimator.ofFloat(0f, usedMemory)
        valueAnimator.duration = Config.ANIMATION_DURATION
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()

        valueAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            mMemText!!.text = getString(R.string.clean_to_memory, Utils.humanReadableByteCount(value.toLong(), false))
        }
        valueAnimator.start()
    }

    private fun animateCacheText(usedCache: Float) {
        if (usedCache <= 0) {
            mCacheText!!.text = ""
            return
        }
        val valueAnimator = ValueAnimator.ofFloat(0f, usedCache)
        valueAnimator.duration = Config.ANIMATION_DURATION
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()

        valueAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            mCacheText!!.text = getString(R.string.clean_to_cache, Utils.humanReadableByteCount(value.toLong(), false))
        }
        valueAnimator.start()
    }

    fun onViewClick(view: View?){
        when (view!!.id) {
            R.id.one_key_clear -> {
                try {
                    val intent = Intent(this, CLeanMemActivity::class.java)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
