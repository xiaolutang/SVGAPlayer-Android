package com.txl.glide

import android.content.Context
import com.bumptech.glide.Glide

/**
 * @author 唐小陆
 * Created on 2022/11/3
 * Desc: 灰度管理扩展库
 */
object GrayManager {
    @Volatile
    private var isGray = false

    fun isGray(): Boolean {
        return isGray
    }

    @Synchronized
    fun onGrayChange(context: Context, gray: Boolean) {
        isGray = gray
        if (isGray) {
            val glide = Glide.get(context)
            SVGAModelInit().registerComponents(context, glide, glide.registry)
        }
    }

}