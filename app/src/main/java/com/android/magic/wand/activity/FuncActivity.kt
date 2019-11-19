package com.android.magic.wand.activity

import android.animation.ValueAnimator
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.android.magic.wand.Constants.Config
import com.android.magic.wand.R
import com.android.magic.wand.utils.UpdateLog
import com.android.magic.wand.utils.Utils
import com.android.magic.wand.view.BubblePop
import java.lang.Exception

class FuncActivity : AppCompatActivity() {
    private val TAG = FuncActivity::class.java.simpleName

    private var mSeekBar: SeekBar? = null
    private var mBubblePop: BubblePop? = null
    private var mCleanBg: BubblePop? = null
    private var mTextDec: TextView? = null
    private var mMemText: TextView? = null
    private var mCacheText: TextView? = null

    private var gBubble: Group? = null
    private var gCircle: Group? = null
    private var gClean: Group? = null

    private val mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func)

        val intent = intent
        if (intent == null) {
            finish()
        }
        gBubble = findViewById(R.id.bubble_group)
        gCircle = findViewById(R.id.circle_group)
        gClean = findViewById(R.id.clean_group)
        mSeekBar = findViewById(R.id.seekBar)
        mBubblePop = findViewById(R.id.bubble)

        mCleanBg = findViewById(R.id.clean_bg)
        mMemText = findViewById(R.id.clean_dec2)
        mCacheText = findViewById(R.id.clean_dec3)


        showHide(intent!!.getStringExtra("function"))
        UpdateLog.d(TAG, "mSeekBar:$mSeekBar")
        mSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                UpdateLog.d(TAG, "onProgressChanged:$progress")
                if (mBubblePop != null) {
                    mBubblePop!!.setSpeed(progress, false)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                UpdateLog.d(TAG, "onStartTrackingTouch:" + seekBar.progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                UpdateLog.d(TAG, "onStopTrackingTouch:" + seekBar.progress)
            }
        })
    }

    fun onViewClick(view: View?) {
        when (view!!.id) {
            R.id.start -> {
                if (mBubblePop != null) {
                    mBubblePop!!.start()
                }
            }
            R.id.stop -> {
                if (mBubblePop != null) {
                    mBubblePop!!.stop()
                }
            }
            R.id.clean_btn -> {
                try {
                    val intent = Intent(this, CLeanMemActivity::class.java)
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun showHide(function: String) {
        gBubble!!.visibility = View.GONE
        gCircle!!.visibility = View.GONE
        gClean!!.visibility = View.GONE
        when (function) {
            Config.BUBBLE_POP -> gBubble!!.visibility = View.VISIBLE
            Config.CIRCLE -> gCircle!!.visibility = View.VISIBLE
            Config.CLEAN -> {
                gClean!!.visibility = View.VISIBLE
                scanClean()
            }
        }
    }

    private fun scanClean(){
        mCleanBg!!.setSpeed(20, false)
        mCleanBg!!.start()
        animateMemoryText(500 * 1024 * 1024f)
        animateCacheText(900 * 1024 * 1024f)
        mHandler.postDelayed({
            mCleanBg!!.stop()
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
}
