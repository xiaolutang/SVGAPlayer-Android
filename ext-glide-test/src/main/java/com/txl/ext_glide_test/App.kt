package com.txl.ext_glide_test

import android.app.Application
import com.bumptech.glide.Glide
import com.opensource.svgaplayer.SVGASoundManager
import com.opensource.svgaplayer.utils.log.SVGALogger
import com.txl.glide.SVGAModelInit

class App : Application() {
    override fun onCreate() {
        super.onCreate()
//        val glide = Glide.get(this)
//        SVGAModelInit().registerComponents(this, glide, glide.registry)
        SVGALogger.setLogEnabled(true)
    }
}