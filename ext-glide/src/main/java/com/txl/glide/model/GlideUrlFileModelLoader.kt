package com.txl.glide.model

import android.net.Uri
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.DataRewinder
import com.bumptech.glide.load.data.HttpUrlFetcher
import com.bumptech.glide.load.model.*
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader
import com.txl.glide.helper.InputStreamEncodeFile
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc:
 */
class GlideUrlFileModelLoader(
    private val cachePath: String,
    private val obtainRewind: (InputStream) -> DataRewinder<InputStream>
) : ModelLoader<Uri, File> {
    private val SCHEMES = Collections.unmodifiableSet(HashSet(Arrays.asList("http", "https")))

    override fun buildLoadData(
        model: Uri,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<File>? {
        val glideUrl = GlideUrl(model.toString())
        val fetcher = HttpUrlFetcher(glideUrl, options.get(HttpGlideUrlLoader.TIMEOUT)!!)
        return ModelLoader.LoadData(glideUrl, FileFetcher(glideUrl, fetcher))
    }

    override fun handles(model: Uri): Boolean {
        return SCHEMES.contains(model.scheme)
    }


    class GlideUrlFileModelLoaderFactory(
        private val cachePath: String,
        private val obtainRewind: (InputStream) -> DataRewinder<InputStream>
    ) :
        ModelLoaderFactory<Uri, File> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<Uri, File> {
            return GlideUrlFileModelLoader(
                cachePath,
                obtainRewind
            )
        }

        override fun teardown() {

        }
    }

    inner class FileFetcher(
        private val glideUrl: GlideUrl,
        private val inputStreamFetcher: DataFetcher<InputStream>
    ) :
        DataFetcher<File> {
        private val isCanceled = AtomicBoolean(false)
        private var file:File? = null
        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in File>) {
            inputStreamFetcher.loadData(priority, object : DataFetcher.DataCallback<InputStream> {
                override fun onDataReady(data: InputStream?) {
                    if(data == null){
                        callback.onDataReady(null)
                    }else{
                        file = InputStreamEncodeFile(glideUrl,cachePath,obtainRewind).decodeZipFile(data)
                        callback.onDataReady(file)
                    }
                }

                override fun onLoadFailed(e: Exception) {
                    callback.onLoadFailed(e)
                }
            })
        }



        override fun cleanup() {
            inputStreamFetcher.cleanup()
//            file?.delete()
        }

        override fun cancel() {
            inputStreamFetcher.cancel()
            isCanceled.set(true)
        }

        override fun getDataClass(): Class<File> {
            return File::class.java
        }

        override fun getDataSource(): DataSource {
            return DataSource.LOCAL
        }
    }
}