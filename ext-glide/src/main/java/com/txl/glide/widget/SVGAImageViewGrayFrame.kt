package com.txl.glide.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.BlendMode
import android.graphics.PorterDuff
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.DrawableUtils
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGAParser
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.GrayManager
import com.txl.glide.R

/**
 * @author 唐小陆
 * Created on 2022/11/2
 * Desc:通过灰度的方式实现动态切换使用SVGAImageView 还是使用ImageView来进行加载
 * 实现ImageView AppCompatImage所有的属性，实现指定子View宽高的属性
 * 支持从XML布局中加载子元素。注意id要和默认布局相同
 * fixme 这个类不能放在仓库里面 放在接入模块
 */
class SVGAImageViewGrayFrame : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        val typeArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SVGAImageViewGrayFrame, defStyleAttr, defStyleRes)
        addViewBySelf = typeArray.getBoolean(R.styleable.SVGAImageViewGrayFrame_addViewBySelf, true)
        if (addViewBySelf) {
            val view =
                if (isGray()) {
                    SVGAImageView(context)
                } else {
                    ImageView(context)
                }
            parseImageView(typeArray, view)
            if (view is SVGAImageView) {
                parseSVGAImageView(typeArray, view)
            }
        }
        typeArray.recycle()
    }


    private fun parseImageView(typeArray: TypedArray, imageView: ImageView) {
        this.imageView = imageView
        val width = typeArray.getInt(R.styleable.SVGAImageViewGrayFrame_image_layout_width, LayoutParams.WRAP_CONTENT)
        val height = typeArray.getInt(R.styleable.SVGAImageViewGrayFrame_image_layout_height, LayoutParams.WRAP_CONTENT)
        val layoutParams = LayoutParams(width, height)
        addView(imageView, layoutParams)
        val count = typeArray.indexCount
        for (index in 0 until count) {
            when (val attr = typeArray.getIndex(index)) {
                R.styleable.SVGAImageViewGrayFrame_image_src -> {
                    imageView.setImageDrawable(typeArray.getDrawable(attr))
                }
                R.styleable.SVGAImageViewGrayFrame_image_scaleType -> {
                    val scale: Int = typeArray.getInt(attr, -1)
                    if (scale >= 0) {
                        imageView.scaleType = sScaleTypeArray[scale]
                    }
                }
                R.styleable.SVGAImageViewGrayFrame_image_adjustViewBounds -> {
                    imageView.adjustViewBounds = typeArray.getBoolean(attr, false)
                }
                R.styleable.SVGAImageViewGrayFrame_image_maxWidth -> {
                    imageView.maxWidth = typeArray.getDimensionPixelSize(attr, Int.MAX_VALUE)
                }
                R.styleable.SVGAImageViewGrayFrame_image_maxHeight -> {
                    imageView.maxHeight = typeArray.getDimensionPixelSize(attr, Int.MAX_VALUE)
                }
                R.styleable.SVGAImageViewGrayFrame_image_tint -> {
                    imageView.imageTintList = typeArray.getColorStateList(attr)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        imageView.imageTintBlendMode = BlendMode.SRC_ATOP
                    }
                }
                R.styleable.SVGAImageViewGrayFrame_image_baselineAlignBottom -> {
                    imageView.baselineAlignBottom = typeArray.getBoolean(attr, false)
                }
                R.styleable.SVGAImageViewGrayFrame_image_cropToPadding -> {
                    imageView.cropToPadding = typeArray.getBoolean(attr, false)
                }
                R.styleable.SVGAImageViewGrayFrame_image_baseline -> {
                    imageView.baseline = typeArray.getDimensionPixelSize(attr, -1)
                }
                R.styleable.SVGAImageViewGrayFrame_image_drawableAlpha -> {
                    imageView.imageAlpha = typeArray.getInt(attr, 255)
                }
                R.styleable.SVGAImageViewGrayFrame_image_tintMode -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        imageView.imageTintBlendMode = parseBlendMode(typeArray.getInt(attr, -1), imageView.imageTintBlendMode)
                    }
                }
                R.styleable.SVGAImageViewGrayFrame_frame_image_background -> {
                    imageView.background = typeArray.getDrawable(attr)
                }
                R.styleable.SVGAImageViewGrayFrame_image_backgroundTint -> {
                    imageView.backgroundTintList = typeArray.getColorStateList(attr)
                }
                R.styleable.SVGAImageViewGrayFrame_image_backgroundTintMode -> {
                    imageView.backgroundTintMode = parseTintMode(typeArray.getInt(attr, -1), null)
                }
            }
        }
    }

    private fun parseSVGAImageView(typeArray: TypedArray, sVGAImageView: SVGAImageView) {
        this.sVGAImageView = sVGAImageView
    }


    private var imageLayout: Int = R.layout.default_svga_change_image_layout
    private var addViewBySelf = true
    var autoPlay = true
    var dynamicItem: SVGADynamicEntity = SVGADynamicEntity()
    private var imageView: ImageView? = null
    private var sVGAImageView: SVGAImageView? = null


    fun loadLayoutRes(@LayoutRes imageLayout: Int) {
        this.imageLayout = imageLayout
        removeAllViews()
        LayoutInflater.from(context).inflate(imageLayout, this, true)
    }

    fun loadAssetSVGA(assetName:String) {
        if(isGray()){
            tag = assetName
            SVGAParser(context).decodeFromAssets("$assetName.svga",object : SVGAParser.ParseCompletion{
                override fun onComplete(videoItem: SVGAVideoEntity) {
                    if(tag == assetName){
                        sVGAImageView?.setVideoItem(videoItem,dynamicItem)
                        sVGAImageView?.stepToFrame(0,autoPlay)
                    }

                }

                override fun onError() {

                }
            })
        }else{

        }
    }


    fun loadNetSVGA(url:String) {

    }

    /**
     * 是否被灰度
     * */
    fun isGray(): Boolean {
        return GrayManager.isGray()
    }


    private val sScaleTypeArray = arrayOf(
        ImageView.ScaleType.MATRIX,
        ImageView.ScaleType.FIT_XY,
        ImageView.ScaleType.FIT_START,
        ImageView.ScaleType.FIT_CENTER,
        ImageView.ScaleType.FIT_END,
        ImageView.ScaleType.CENTER,
        ImageView.ScaleType.CENTER_CROP,
        ImageView.ScaleType.CENTER_INSIDE
    )

    @RequiresApi(Build.VERSION_CODES.Q)
    fun parseBlendMode(value: Int, defaultMode: BlendMode?): BlendMode? {
        return when (value) {
            3 -> BlendMode.SRC_OVER
            5 -> BlendMode.SRC_IN
            9 -> BlendMode.SRC_ATOP
            14 -> BlendMode.MODULATE
            15 -> BlendMode.SCREEN
            16 -> BlendMode.PLUS
            else -> defaultMode
        }
    }


    /**
     * Parses tint mode.
     */
    private fun parseTintMode(value: Int, defaultMode: PorterDuff.Mode?): PorterDuff.Mode? {
        return when (value) {
            3 -> PorterDuff.Mode.SRC_OVER
            5 -> PorterDuff.Mode.SRC_IN
            9 -> PorterDuff.Mode.SRC_ATOP
            14 -> PorterDuff.Mode.MULTIPLY
            15 -> PorterDuff.Mode.SCREEN
            16 -> PorterDuff.Mode.ADD
            else -> defaultMode
        }
    }
}