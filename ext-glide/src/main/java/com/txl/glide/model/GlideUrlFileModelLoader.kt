package com.txl.glide.model

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.DataRewinder
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.RuntimeException
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicBoolean
import java.util.zip.ZipInputStream

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc:
 */
class GlideUrlFileModelLoader(
    private val modelLoader: ModelLoader<GlideUrl, InputStream>,
    private val cachePath: String,
    private val obtainRewind: (InputStream) -> DataRewinder<InputStream>
) : ModelLoader<GlideUrl, File> {
    override fun buildLoadData(
        model: GlideUrl,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<File>? {
        val fetcher = modelLoader.buildLoadData(model, width, height, options)?.fetcher
        if (fetcher != null) {
            return ModelLoader.LoadData(model, FileFetcher(model, fetcher))
        }
        return null
    }

    override fun handles(model: GlideUrl): Boolean {
        return modelLoader.handles(model)
    }


    class GlideUrlFileModelLoaderFactory(
        private val cachePath: String,
        private val obtainRewind: (InputStream) -> DataRewinder<InputStream>
    ) :
        ModelLoaderFactory<GlideUrl, File> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<GlideUrl, File> {
            return GlideUrlFileModelLoader(
                multiFactory.build(GlideUrl::class.java, InputStream::class.java),
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
        override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in File>) {
            inputStreamFetcher.loadData(priority, object : DataFetcher.DataCallback<InputStream> {
                override fun onDataReady(data: InputStream?) {
                    if(data == null){
                        callback.onDataReady(null)
                    }else{
                        decode(data,callback)
                    }


                }

                override fun onLoadFailed(e: Exception) {
                    callback.onLoadFailed(e)
                }
            })
        }

        private fun decode(data: InputStream,callback: DataFetcher.DataCallback<in File>) {
            if (isCanceled.get()){
                callback.onDataReady(null)
                return
            }
            val isZip = isZipFile(data.readBytes())
            if(!isZip){
                callback.onLoadFailed(RuntimeException("inputStream not zip glideUrl is : $glideUrl"))
                return
            }
            //文件存在
            if (cacheDir.isDirectory && !cacheDir.list().isNullOrEmpty()){
                callback.onDataReady(cacheDir)
                return
            }
            //数据回退
            val rewind = obtainRewind(data)
            try {
                // FIXME: 这一块文件缓存 考虑接入 Glide lru 文件缓存
                cacheDir.makeSureExist()
                unzip(rewind.rewindAndGet(), cacheDir)
                if(isCanceled.get()){
                    callback.onDataReady(null)
                }else{
                    callback.onDataReady(cacheDir)
                }
            } catch (e:Exception){
                callback.onLoadFailed(e)
            }finally {
                rewind.cleanup()
            }
            return
        }

        override fun cleanup() {
            inputStreamFetcher.cleanup()
        }

        override fun cancel() {
            inputStreamFetcher.cancel()
            isCanceled.set(true)
        }

        override fun getDataClass(): Class<File> {
            return File::class.java
        }

        override fun getDataSource(): DataSource {
            return DataSource.REMOTE
        }

        private fun readAsBytes(inputStream: InputStream): ByteArray? {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                val byteArray = ByteArray(2048)
                while (true) {
                    val count = inputStream.read(byteArray, 0, 2048)
                    if (count <= 0) {
                        break
                    } else {
                        byteArrayOutputStream.write(byteArray, 0, count)
                    }
                }
                return byteArrayOutputStream.toByteArray()
            }
        }

        private fun unzip(inputStream: InputStream, dir: File) {
            ZipInputStream(inputStream).use { zipInputStream ->
                while (true) {
                    val zipItem = zipInputStream.nextEntry ?: break
                    if (zipItem.name.contains("../")) {
                        // 解压路径防止穿透
                        continue
                    }
                    if (zipItem.name.contains("/")) {
                        continue
                    }
                    val file = File(dir, zipItem.name)
                    FileOutputStream(file).use { fileOutputStream ->
                        val buff = ByteArray(2048)
                        while (true) {
                            val readBytes = zipInputStream.read(buff)
                            if (readBytes <= 0) {
                                break
                            }
                            fileOutputStream.write(buff, 0, readBytes)
                        }
                    }
                    zipInputStream.closeEntry()
                }
            }
        }

        // 是否是 zip 文件
        private fun isZipFile(bytes: ByteArray): Boolean {
            return bytes.size > 4 && bytes[0].toInt() == 80 && bytes[1].toInt() == 75 && bytes[2].toInt() == 3 && bytes[3].toInt() == 4
        }

        private fun cacheKey(glideUrl: GlideUrl): String {
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.update(glideUrl.toStringUrl().toByteArray(charset("UTF-8")))
            val digest = messageDigest.digest()
            var sb = ""
            for (b in digest) {
                sb += String.format("%02x", b)
            }
            return sb
        }

        private val cacheDir: File by lazy(LazyThreadSafetyMode.NONE) {
            File(cachePath, cacheKey(glideUrl))
        }

        private fun File.makeSureExist() {
            val dir = this
            if (dir.exists()) {
                if (!dir.isDirectory) {
                    dir.deleteRecursively()
                    dir.mkdirs()
                }
            } else {
                dir.mkdirs()
            }
        }
    }
}