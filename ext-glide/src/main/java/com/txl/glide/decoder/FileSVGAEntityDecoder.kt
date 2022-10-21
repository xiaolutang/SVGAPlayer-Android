package com.txl.glide.decoder

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.bumptech.glide.load.resource.file.FileResource
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.proto.MovieEntity
import com.txl.glide.helper.SVGAImageHeaderHelper
import com.txl.glide.helper.SVGAVideoEntityReflectHelper
import com.txl.glide.resource.SVGAEntityResource
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.zip.InflaterInputStream

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc:
 */

const val movieBinary = "movie.binary"
const val movieSpec = "movie.spec"

class FileSVGAEntityDecoder(
    private val arrayPool: ArrayPool
) : ResourceDecoder<File, SVGAVideoEntity> {
    override fun handles(source: File, options: Options): Boolean {
        return source.isSVGAUnZipFile()
    }

    override fun decode(
        source: File,
        width: Int,
        height: Int,
        options: Options
    ): SVGAEntityResource? {
        decodeUnZipFile(source)
        return decodeUnZipFile(source)
    }

    private fun File.isSVGAUnZipFile(): Boolean {

        fun hasChild(vararg fileNames: String): Boolean {
            if (this.isDirectory) {
                val childFileNames = this.list()?.toSet() ?: emptySet()
                return fileNames.any { childFileNames.contains(it) }
            }
            return false
        }

        return hasChild(movieBinary, movieSpec)
    }

    private fun decodeUnZipFile(source: File): SVGAEntityResource? {
        val binaryFile = File(source, movieBinary)
        val jsonFile = File(source, movieSpec)
        if (binaryFile.isFile) {
            return parseBinaryFile(source, binaryFile)
        } else if (jsonFile.isFile) {
            return parseSpecFile(source, jsonFile)
        }
        return null
    }

    private fun parseBinaryFile(source: File, binaryFile: File): SVGAEntityResource? {
        try {
            FileInputStream(binaryFile).use {
                //fixme 暂时不指定宽高 缩放有问题
                val entity = SVGAVideoEntity(MovieEntity.ADAPTER.decode(it), source)
                SVGAVideoEntityReflectHelper.setupAudios(entity)
                return SVGAEntityResource(entity, source.totalSpace.toInt())
            }
        } catch (e: Exception) {
            binaryFile.delete()
            return null
        }
    }

    private fun parseSpecFile(source: File, jsonFile: File): SVGAEntityResource? {
        val buffer = arrayPool.get(ArrayPool.STANDARD_BUFFER_SIZE_BYTES, ByteArray::class.java)
        try {
            FileInputStream(jsonFile).use { fileInputStream ->
                ByteArrayOutputStream().use { byteArrayOutputStream ->
                    while (true) {
                        val size = fileInputStream.read(buffer)
                        if (size == -1) {
                            break
                        }
                        byteArrayOutputStream.write(buffer, 0, size)
                    }
                    val jsonObj = JSONObject(byteArrayOutputStream.toString())
                    val entity = SVGAVideoEntity(jsonObj, source)
                    return SVGAEntityResource(entity, source.totalSpace.toInt())
                }
            }
        } catch (e: Exception) {
            jsonFile.delete()
            return null
        } finally {
            arrayPool.put(buffer)
        }
    }

}