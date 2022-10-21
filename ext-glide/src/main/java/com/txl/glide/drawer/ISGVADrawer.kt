package com.txl.glide.drawer

import android.graphics.Canvas
import android.widget.ImageView

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc:
 */
interface ISGVADrawer {
    fun drawFrame(canvas : Canvas, frameIndex: Int, scaleType: ImageView.ScaleType)
}