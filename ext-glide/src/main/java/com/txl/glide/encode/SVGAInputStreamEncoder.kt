package com.txl.glide.encode

import com.bumptech.glide.load.Encoder
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataRewinder
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.bumptech.glide.load.model.StreamEncoder
import com.txl.glide.helper.isSVGAUnZipFile
import com.txl.glide.helper.isZipFile
import com.txl.glide.helper.makeSureExist
import com.txl.glide.helper.readAsBytes
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class SVGAInputStreamEncoder(
    private val arrayPool: ArrayPool,
    private val obtainRewind: (InputStream) -> DataRewinder<InputStream>
) : StreamEncoder(arrayPool) {

    override fun encode(data: InputStream, file: File, options: Options): Boolean {

        readAsBytes(data).let { bytes ->
            val zip = isZipFile(bytes)
            if (!zip) {//
                return super.encode(data, file, options)
            } else {
                file.makeSureExist()
                ByteArrayInputStream(bytes).use {
                    unzip(it, file)
                }
                return true
            }
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
                ensureUnzipSafety(file, dir.absolutePath)
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

    // 检查 zip 路径穿透
    private fun ensureUnzipSafety(outputFile: File, dstDirPath: String) {
        val dstDirCanonicalPath = File(dstDirPath).canonicalPath
        val outputFileCanonicalPath = outputFile.canonicalPath
        if (!outputFileCanonicalPath.startsWith(dstDirCanonicalPath)) {
            throw IOException("Found Zip Path Traversal Vulnerability with $dstDirCanonicalPath")
        }
    }


}