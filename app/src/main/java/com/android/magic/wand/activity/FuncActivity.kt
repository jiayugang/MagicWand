package com.android.magic.wand.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.Group
import com.android.magic.wand.Constants.Config
import com.android.magic.wand.R
import com.android.magic.wand.view.BubblePop

class FuncActivity : AppCompatActivity() {
    private val TAG = FuncActivity::class.java.simpleName

    private var mSeekBar: SeekBar? = null
    private var mBubblePop: BubblePop? = null

    var gBubble: Group? = null
    var gCircle:Group? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_func)

        gBubble = findViewById(R.id.bubble_group)
        gCircle = findViewById(R.id.circle_group)

        val intent = intent
        if (intent == null) {
            finish()
        }
        showHide(intent!!.getStringExtra("function"))

        mSeekBar = findViewById(R.id.seekBar)
        mBubblePop = findViewById(R.id.bubble)
        mSeekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                Log.d(TAG, "onProgressChanged:$progress")
                if (mBubblePop != null) {
                    mBubblePop!!.setSpeed(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                Log.d(TAG, "onStartTrackingTouch:" + seekBar.progress)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d(TAG, "onStopTrackingTouch:" + seekBar.progress)
            }
        })
    }

    private fun showHide(function: String) {
        gBubble!!.visibility = View.GONE
        gCircle!!.visibility = View.GONE
        when (function) {
            Config.BUBBLE_POP -> gBubble!!.visibility = View.VISIBLE
            Config.CIRCLE -> gCircle!!.visibility = View.VISIBLE
        }
    }
}
