package com.txl.ext_glide_test.ui

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.ext_glide_test.R
import com.txl.glide.target.SVGAImageViewTarget
import kotlinx.android.synthetic.main.activity_load_asset_svga.*


/**
 * 加载Asset目录下的资源
 * */
class AddListenerActivity : AppCompatActivity() {

    private val imageString = "file:///android_asset/theme_award_beans.svga"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_asset_svga)
        val svgaImageViewTarget = SVGAImageViewTarget.Builder(glideSVGAImg)
            .setAnimationUpdateListener { Log.d("AddListenerActivity","onAnimationUpdate") }
            .setAnimationListener(object :Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) {
                    Log.d("AddListenerActivity","onAnimationStart")
                }

                override fun onAnimationEnd(animation: Animator?) {
                    Log.d("AddListenerActivity","onAnimationEnd")
                }

                override fun onAnimationCancel(animation: Animator?) {
                    Log.d("AddListenerActivity","onAnimationCancel")
                }

                override fun onAnimationRepeat(animation: Animator?) {
                    Log.d("AddListenerActivity","onAnimationRepeat")
                }

            })
            .build()
        Glide.with(this).load(imageString).into(svgaImageViewTarget)
        loadBySvgaLib()
    }

    /**
     * SVGAImageView 只支持在xml中提前设置 图片路径 如果在代码中需要自自己创建解析对象
     * */
    private fun loadBySvgaLib() {
        val parse = SVGAParser(this)
        parse.decodeFromAssets("theme_award_beans.svga", object : SVGAParser.ParseCompletion {
            override fun onComplete(videoItem: SVGAVideoEntity) {
                val drawable = SVGADrawable(videoItem)
                SVGAImageView.setImageDrawable(drawable)
                SVGAImageView.startAnimation()
            }

            override fun onError() {

            }
        })
    }
}