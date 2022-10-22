package com.txl.glide.helper.reflect

import android.graphics.Canvas
import android.widget.ImageView
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAVideoEntity
import java.lang.reflect.Constructor
import java.lang.reflect.Method

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc: 反射 SVGACanvasDrawer
 */
object SVGACanvasDrawerReflectHelper {
    private var constructor: Constructor<*>? = null
    private var drawFrameMethod: Method? = null

    init {
        try {
            val sVGACanvasDrawerClass =
                Class.forName("com.opensource.svgaplayer.drawer.SVGACanvasDrawer")
            constructor = sVGACanvasDrawerClass.getConstructor(
                SVGAVideoEntity::class.java,
                SVGADynamicEntity::class.java
            )
            constructor?.isAccessible = true

            drawFrameMethod = sVGACanvasDrawerClass.getDeclaredMethod(
                "drawFrame",
                Canvas::class.java,
                Int::class.java,
                ImageView.ScaleType::class.java
            )
            drawFrameMethod?.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun createSVGACanvasDrawer(videoItem: SVGAVideoEntity, dynamicItem: SVGADynamicEntity): Any? {
        try {
            return constructor?.newInstance(videoItem, dynamicItem)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun invokeDrawFrame(
        canvas: Canvas,
        frameIndex: Int,
        scaleType: ImageView.ScaleType,
        sVGACanvasDrawer: Any
    ) {
        try {
            drawFrameMethod?.invoke(sVGACanvasDrawer,canvas,frameIndex,scaleType)
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
}