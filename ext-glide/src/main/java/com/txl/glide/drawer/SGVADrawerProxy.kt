package com.txl.glide.drawer

import android.graphics.Canvas
import android.widget.ImageView
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.helper.reflect.SVGACanvasDrawerReflectHelper

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc:
 */
class SGVADrawerProxy private constructor(private val mSVGADrawer:Any?):ISGVADrawer {

    override fun drawFrame(canvas: Canvas, frameIndex: Int, scaleType: ImageView.ScaleType) {
        mSVGADrawer?.let {
            SVGACanvasDrawerReflectHelper.invokeDrawFrame(canvas,frameIndex,scaleType,it)
        }
    }

    companion object{
        fun createSGVADrawer(videoItem: SVGAVideoEntity, dynamicItem: SVGADynamicEntity):ISGVADrawer{
            return SGVADrawerProxy(SVGACanvasDrawerReflectHelper.createSVGACanvasDrawer(videoItem, dynamicItem))
        }
    }
}