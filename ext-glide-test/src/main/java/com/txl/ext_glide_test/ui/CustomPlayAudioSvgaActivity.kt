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
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import android.widget.Toast
import com.txl.glide.drawable.SVGAAnimationDrawable
import com.txl.glide.helper.AudioPlayerEntity
import com.txl.glide.target.SVGAImageViewTarget
import java.net.URL

/**
 * 自定义音频播放
 * */
class CustomPlayAudioSvgaActivity : AppCompatActivity() {
    private val imageString = "file:///android_asset/MerryChristmas.svga"
    private val TAG = CustomPlayAudioSvgaActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_audio_svga)
        val svgaImageViewTarget = SVGAImageViewTarget.Builder(glideSVGAImg)
            .setRepeatMode(ValueAnimator.REVERSE)
            .setAnimationRepeatCount(ValueAnimator.INFINITE)
            .setAudioPlayerListener(object :SVGAAnimationDrawable.AudioPlayerListener{
                override fun onAudioFileReady(audioList: ArrayList<AudioPlayerEntity>) {
                    Toast.makeText(this@CustomPlayAudioSvgaActivity,"svga音频文件准备好",Toast.LENGTH_SHORT).show()
                    audioList.forEach {
                        Log.d(TAG, "onAudioFileReady: file ${it.file?.absolutePath}")
                    }
                }

                override fun drawFrame(frame: Int) {

                }

                override fun onDrawableClear() {

                }
            })
            .build()
        Glide.with(this).load(imageString).into(svgaImageViewTarget)
        tvPause.setOnClickListener {
            val drawable = glideSVGAImg.drawable
            if(drawable is SVGAAnimationDrawable){
                drawable.stop()
            }
        }
        tvPause.visibility = View.GONE
        tvResume.setOnClickListener {
            val drawable = glideSVGAImg.drawable
            if(drawable is SVGAAnimationDrawable){
                drawable.start()
            }
        }
        tvResume.visibility = View.GONE
    }

}