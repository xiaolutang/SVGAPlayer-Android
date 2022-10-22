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
import java.net.URL

/**
 * 测试所有asset目录下的svga 文件是否能够正常显示
 * */
class TestAllSVGAFileActivity : AppCompatActivity() {

    private val tag = TestAllSVGAFileActivity::class.java.simpleName

    private val resourceNameList = ArrayList<String>()
    private val resourceErrorNameList = ArrayList<String>()
    private var source:ISource = SourceAsset()
    private var index: Int = -1


    //本地 异常文件：
    //  750x80.svga
    //  alarm.svga
    //  EmptyState.svga
    //  Goddess.svga


    //通过Glide 加载数据
    private var loadByGlide = true
    //本地数据
    private var localSource = true
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

    private fun changeSource() {
        source = if(loadByGlide){
            if(localSource){
                SourceAsset()
            }else{
                SourceNet()
            }
        }else{
            if(localSource){
                SVGASourceAsset()
            }else{
                SourceNet()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_all_svgafile)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            localSource = checkedId == R.id.buttonLocal
            index = 0
            changeSource()
        }
        radioGroupRender.setOnCheckedChangeListener { group, checkedId ->
            loadByGlide = checkedId == R.id.buttonImageView
            index = 0
            changeSource()
            if(loadByGlide){
                imageView.visibility = View.VISIBLE
                SVGAImageView.visibility = View.GONE
            }else{
                imageView.visibility = View.GONE
                SVGAImageView.visibility = View.VISIBLE
            }
        }
        tvNext.setOnClickListener {
            loadImage(true)
        }
        tvPre.setOnClickListener {
            loadImage(false)
        }
        Glide.with(this).load("https://gitee.com/comfromit/com.xiaolu.designmodel/blob/master/icon_ggg.png").into(imageView)

    }

    private fun loadImage(next: Boolean) {
        val path = getSourcePath(next)
        tvLoadPath.text = path
        if(loadByGlide){
            Glide.with(this).load(path).addListener(object : RequestListener<Drawable>{
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

            val listener = object :SVGAParser.ParseCompletion{
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    val drawable = SVGADrawable(videoItem)
                    SVGAImageView.setImageDrawable(drawable)
                    SVGAImageView.startAnimation()
                }

                override fun onError() {
                    Log.e(tag,"load failed svga lib::: $path")
                }
            }
            if(localSource){
                SVGAParser(this).decodeFromAssets(path,listener)
            }else{
                SVGAParser(this).decodeFromURL(URL(path),listener)
            }

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