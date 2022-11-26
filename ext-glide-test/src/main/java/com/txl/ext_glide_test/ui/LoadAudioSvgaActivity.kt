package com.txl.ext_glide_test.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.ext_glide_test.R
import kotlinx.android.synthetic.main.activity_load_asset_svga.*
import kotlinx.android.synthetic.main.activity_load_asset_svga.SVGAImageView
import kotlinx.android.synthetic.main.activity_load_asset_svga.glideSVGAImg
import kotlinx.android.synthetic.main.activity_load_audio_svga.*
import com.txl.glide.drawable.SVGAAnimationDrawable
import java.net.URL

/**
 * 加载网络的SVGA图片
 * */
class LoadAudioSvgaActivity : AppCompatActivity() {
    private val imageString = "https://jojopublicfat.jojoread.com/cc/cc-admin/course/420954735744875520/1657003430002ba7b4355ed6e6948ecedecfc33440e82.svga"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_audio_svga)
//        loadBySvgaLib()
        Glide.with(this).load(imageString).into(glideSVGAImg)
        tvPause.setOnClickListener {
            val drawable = glideSVGAImg.drawable
            if(drawable is SVGAAnimationDrawable){
                drawable.stop()
            }
        }
        tvResume.setOnClickListener {
            val drawable = glideSVGAImg.drawable
            if(drawable is SVGAAnimationDrawable){
                drawable.start()
            }
        }

    }

    /**
     * SVGAImageView 只支持在xml中提前设置 图片路径 如果在代码中需要自自己创建解析对象
     * */
    private fun loadBySvgaLib() {
        val parse = SVGAParser(this)
        parse.decodeFromURL(URL(imageString), object : SVGAParser.ParseCompletion {
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