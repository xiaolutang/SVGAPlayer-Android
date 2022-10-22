package com.txl.glide.model.factory

import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.txl.glide.model.SVGAFileModelLoader
import java.io.File

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class FileFileModelLoaderFactory:ModelLoaderFactory<File,File> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<File, File> {
        return SVGAFileModelLoader()
    }

    override fun teardown() {

    }
}