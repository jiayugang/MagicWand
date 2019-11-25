package com.android.magic.wand.activity

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.magic.wand.R
import com.android.magic.wand.view.RoundCornersTrans
import com.android.magic.wand.view.RoundedCornersTransformation
import com.android.magic.wand.view.RoundedImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.shehuan.niv.NiceImageView

class RoundActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var image1: RoundedImageView
    private lateinit var image2: RoundedImageView
    private lateinit var image3: RoundedImageView
    private lateinit var image4: RoundedImageView
    private lateinit var imageView: NiceImageView
    private lateinit var imageView2: RoundedImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_round)

        image= findViewById(R.id.imageView)
        image1 = findViewById(R.id.imageView1)
        image2 = findViewById(R.id.imageView2)
        image3 = findViewById(R.id.imageView3)
        image4 = findViewById(R.id.imageView4)
        imageView = findViewById(R.id.round)
        imageView2 = findViewById(R.id.round2)

        image1.setImageResource(R.drawable.image)
        image2.setImageResource(R.drawable.image)
        image3.setImageResource(R.drawable.image)
        image4.setImageResource(R.drawable.image)
        imageView.setImageResource(R.drawable.aa)
        imageView2.setImageResource(R.drawable.aa)

        loading(image)
//        loading(image1,20,RoundedCornersTransformation.CornerType.TOP_LEFT)
//        loading(image2,20,RoundedCornersTransformation.CornerType.TOP_RIGHT)
//        loading(image3,20,RoundedCornersTransformation.CornerType.BOTTOM_LEFT)
//        loading(image4,20,RoundedCornersTransformation.CornerType.BOTTOM_RIGHT)
    }

    private fun loading(mImageView:ImageView){
        if (mImageView == null) return
        val roundedCorners = RoundedCorners(20)
        val options = RequestOptions.bitmapTransform(roundedCorners)
        Glide.with(this).load(R.drawable.image).apply(options).into(mImageView)
    }

    private fun loading(mImageView:ImageView, radius:Int , cornerType:RoundedCornersTransformation.CornerType){
        if (mImageView == null) return

        val roundedCorners = RoundedCornersTransformation(this,radius,0,cornerType)
        val options = RequestOptions.bitmapTransform(roundedCorners)
        Glide.with(this).load(R.drawable.image).apply(options).into(mImageView)
    }
}
