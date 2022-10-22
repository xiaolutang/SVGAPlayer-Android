package com.txl.glide.model

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.signature.ObjectKey
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.helper.isSVGAUnZipFile
import java.io.File

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class SVGAFileModelLoader:ModelLoader<File,File> {
    override fun buildLoadData(
        model: File,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<File> {
        return ModelLoader.LoadData(ObjectKey(model),SVGAFileDataFetcher(model))
    }

    override fun handles(model: File): Boolean {
        return model.isSVGAUnZipFile()
    }

    class SVGAFileDataFetcher(private val model: File):DataFetcher<File>{
        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in File>) {
            callback.onDataReady(model)
        }

        override fun cleanup() {

        }

        override fun cancel() {

        }

        override fun getDataClass(): Class<File> {
           return File::class.java
        }

        override fun getDataSource(): DataSource {
            // FIXME: Remote 缓存问题
            return DataSource.REMOTE
        }
    }
}