package com.txl.glide.model.factory

import android.net.Uri
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.load.model.StringLoader
import java.io.File

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class StringFileModelLoaderFactory:ModelLoaderFactory<String,File> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, File> {
        return StringLoader( multiFactory.build(Uri::class.java,File::class.java))
    }

    override fun teardown() {

    }
}