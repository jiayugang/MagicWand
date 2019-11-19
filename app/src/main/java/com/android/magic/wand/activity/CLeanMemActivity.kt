package com.android.magic.wand.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.magic.wand.Constants.Config
import com.android.magic.wand.R
import com.android.magic.wand.model.BackgroundColor
import com.android.magic.wand.model.BubbleColor
import com.android.magic.wand.view.BubblePop
import java.util.*

class CLeanMemActivity : AppCompatActivity() {

    private var mTextMem: TextView? = null
    private var mCleanBg: BubblePop? = null
    private val mHandler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clean_mem)

        mCleanBg = findViewById(R.id.clean_bg)
        mTextMem = findViewById(R.id.number)

        animateProgressText()
    }

    private fun finishAc(){
        finish()
    }

    private fun animateProgressText() {

        mCleanBg!!.setSpeed(30, true)
        mCleanBg!!.setBubbleMaxSize(40)

        var bubble = BubbleColor(ContextCompat.getColor(this, R.color.clean_bubble_medium_top),
            ContextCompat.getColor(this, R.color.clean_bubble_medium_bottom))

        mCleanBg!!.setBubbleColor(bubble)
        val mBackgroundColor = BackgroundColor(ContextCompat.getColor(this, android.R.color.white),
            ContextCompat.getColor(this, R.color.clean_bg_medium_bottom))
        mCleanBg!!.setBackgroundColor(mBackgroundColor)
        mCleanBg!!.start()

        val animator = ValueAnimator.ofInt(99, (Math.random() * 10 + 10).toInt())
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.duration = Config.ANIMATION_DURATION * 4
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            mTextMem!!.text = String.format(Locale.US, "%2d", value)
            when (value) {
                in 70..99 -> {
                    val mBackgroundColor = BackgroundColor(ContextCompat.getColor(this, android.R.color.white),
                        ContextCompat.getColor(this, R.color.clean_bg_high_bottom))
                    mCleanBg!!.setBackgroundColor(mBackgroundColor)

                    var bubble = BubbleColor(ContextCompat.getColor(this, R.color.clean_bubble_high_top),
                        ContextCompat.getColor(this, R.color.clean_bubble_high_bottom))
                    mCleanBg!!.setBubbleColor(bubble)
                }
                in 40..60 -> {
                    val mBackgroundColor = BackgroundColor(ContextCompat.getColor(this, android.R.color.white),
                        ContextCompat.getColor(this, R.color.clean_bg_medium_bottom))
                    mCleanBg!!.setBackgroundColor(mBackgroundColor)

                    var bubble = BubbleColor(ContextCompat.getColor(this, R.color.clean_bubble_medium_top),
                        ContextCompat.getColor(this, R.color.clean_bubble_medium_bottom))
                    mCleanBg!!.setBubbleColor(bubble)
                }
                in 0..40 -> {
                    val mBackgroundColor = BackgroundColor(ContextCompat.getColor(this, android.R.color.white),
                        ContextCompat.getColor(this, R.color.clean_bg_low_bottom))
                    mCleanBg!!.setBackgroundColor(mBackgroundColor)

                    var bubble = BubbleColor(ContextCompat.getColor(this, R.color.clean_bubble_low_top),
                        ContextCompat.getColor(this, R.color.clean_bubble_low_bottom))
                    mCleanBg!!.setBubbleColor(bubble)
                }
            }
            animation
        }
        animator.start()

        mHandler.postDelayed({
            mCleanBg!!.stop()
            finishAc()
        },Config.ANIMATION_DURATION * 4)
    }
}
