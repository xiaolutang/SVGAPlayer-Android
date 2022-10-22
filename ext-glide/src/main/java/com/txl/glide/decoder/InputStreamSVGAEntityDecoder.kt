package com.txl.glide.decoder

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.proto.MovieEntity
import com.txl.glide.helper.reflect.SVGAImageHeaderHelper
import com.txl.glide.helper.reflect.SVGAVideoEntityReflectHelper
import com.txl.glide.resource.SVGAEntityResource
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.util.zip.InflaterInputStream

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc:
 */
class InputStreamSVGAEntityDecoder(
    private val cachePath: String,
    private val arrayPool: ArrayPool
) : ResourceDecoder<InputStream, SVGAVideoEntity> {
    override fun handles(source: InputStream, options: Options): Boolean {
//        val result = SVGAImageHeaderHelper.isZLibFormatStream(inputStream = source)
        return SVGAImageHeaderHelper.isZLibFormatStream(inputStream = source)
    }

    override fun decode(
        source: InputStream,
        width: Int,
        height: Int,
        options: Options
    ): SVGAEntityResource? {
        inflate(source)?.let { bytesOrigin ->
            //fixme 暂时不指定宽高 缩放有问题
            val entity = SVGAVideoEntity(MovieEntity.ADAPTER.decode(bytesOrigin), File(cachePath))
            SVGAVideoEntityReflectHelper.setupAudios(entity)
            return SVGAEntityResource(entity, bytesOrigin.size)
        }
        return null
    }

    private fun inflate(source: InputStream): ByteArray? {
        val buffer = arrayPool.get(ArrayPool.STANDARD_BUFFER_SIZE_BYTES, ByteArray::class.java)
        var byteArray:ByteArray? = null
        try {
            InflaterInputStream(source).let { input ->
                ByteArrayOutputStream().let { output ->
                    while (true) {
                        val cnt = input.read(buffer)
                        if (cnt <= 0) break
                        output.write(buffer, 0, cnt)
                    }
                    byteArray = output.toByteArray()
                }
            }
        } finally {
            arrayPool.put(buffer)
        }
        return byteArray
    }

}