package com.txl.ext_glide_test.ui

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.ext_glide_test.R
import com.txl.ext_glide_test.source.ISource
import com.txl.ext_glide_test.source.SVGASourceAsset
import com.txl.ext_glide_test.source.SourceAsset
import com.txl.ext_glide_test.source.SourceNet
import kotlinx.android.synthetic.main.activity_load_asset_svga.*
import kotlinx.android.synthetic.main.activity_test_all_svgafile.*
import kotlinx.android.synthetic.main.activity_test_all_svgafile.SVGAImageView

/**
 * 测试所有asset目录下的svga 文件是否能够正常显示
 * */
class TestAllSVGAFileActivity : AppCompatActivity() {

    private val tag = TestAllSVGAFileActivity::class.java.simpleName

    private val resourceNameList = ArrayList<String>()
    private val resourceErrorNameList = ArrayList<String>()
    private var glideSource: ISource = SourceAsset()
    private var svgaSource:ISource = SVGASourceAsset()
    private var source:ISource = glideSource
    private var index: Int = -1

    //本地 异常文件：
    //  750x80.svga
    //  alarm.svga
    //  EmptyState.svga
    //  Goddess.svga


    private var loadByGlide = true
    init {
        resourceNameList.add("750x80")
        resourceNameList.add("alarm")
        resourceNameList.add("angel")
        resourceNameList.add("Castle")
        resourceNameList.add("EmptyState")
        resourceNameList.add("Goddess")
        resourceNameList.add("gradientBorder")
        resourceNameList.add("heartbeat")
        resourceNameList.add("jojo_audio")
        resourceNameList.add("matteBitmap")
        resourceNameList.add("matteBitmap_1.x")
        resourceNameList.add("matteRect")
        resourceNameList.add("MerryChristmas")
        resourceNameList.add("mp3_to_long")
        resourceNameList.add("posche")
        resourceNameList.add("Rocket")
        resourceNameList.add("rose")
        resourceNameList.add("rose_2.0.0")
        resourceNameList.add("theme_award_beans")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_all_svgafile)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            glideSource = if (checkedId == R.id.buttonLocal) {
                SourceAsset()
            } else {
                SourceNet()
            }
            index = 0
            source = glideSource
        }
        radioGroupRender.setOnCheckedChangeListener { group, checkedId ->
            loadByGlide = checkedId == R.id.buttonImageView
            if(loadByGlide){
                imageView.visibility = View.VISIBLE
                SVGAImageView.visibility = View.GONE
                svgaSource = SVGASourceAsset()
            }else{
                imageView.visibility = View.GONE
                SVGAImageView.visibility = View.VISIBLE
            }
            source = svgaSource

        }
        tvNext.setOnClickListener {
            loadImage(true)
        }
        tvPre.setOnClickListener {
            loadImage(false)
        }

    }

    private fun loadImage(next: Boolean) {
        if(loadByGlide){
            Glide.with(this).load(getSourcePath(next)).addListener(object : RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    resourceErrorNameList.add(model.toString())
                    Log.e(tag,"load failed ::: $model")
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).into(imageView)
        }else{
            val path = getSourcePath(next)
            SVGAParser(this).decodeFromAssets(path,object :SVGAParser.ParseCompletion{
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    val drawable = SVGADrawable(videoItem)
                    SVGAImageView.setImageDrawable(drawable)
                    SVGAImageView.startAnimation()
                }

                override fun onError() {
                    Log.e(tag,"load failed svga lib::: $path")
                }
            })
        }

    }


    private fun getSourcePath(add:Boolean):String{
        if(add){
            index++
            if(index == resourceNameList.size){
                Toast.makeText(this,"循环到最后一个了",Toast.LENGTH_SHORT).show()
                index = 0
            }
        }else{
            index--
            if(index < 0){
                Toast.makeText(this,"循环到第一个了",Toast.LENGTH_SHORT).show()
                index = resourceNameList.size -1
            }
        }
        return source.getPath(resourceNameList[index])
    }


}