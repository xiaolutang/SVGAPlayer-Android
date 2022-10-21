package com.txl.ext_glide_test.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.txl.ext_glide_test.R
import kotlinx.android.synthetic.main.activity_normal_glide_load.*

class NormalGlideLoadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_glide_load)
        //把正常图片当做svga加载
        val imgPath = "https://img1.baidu.com/it/u=3009731526,373851691&fm=253&fmt=auto&app=138&f=JPEG?w=800&h=500"
//        Glide.with(this).load(SVGAModel(imgPath,repeatMode = ValueAnimator.REVERSE)).into(image)
        Glide.with(this).load(imgPath).into(image)
    }
}