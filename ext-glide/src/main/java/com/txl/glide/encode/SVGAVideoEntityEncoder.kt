package com.txl.glide.encode

import com.bumptech.glide.load.EncodeStrategy
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceEncoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.opensource.svgaplayer.SVGAVideoEntity
import java.io.File

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class SVGAVideoEntityEncoder(private val arrayPool: ArrayPool) :
    ResourceEncoder<SVGAVideoEntity> {

    override fun encode(
        data: Resource<SVGAVideoEntity>,
        file: File,
        options: Options
    ): Boolean {

        return false
    }

    override fun getEncodeStrategy(options: Options): EncodeStrategy {
       return EncodeStrategy.SOURCE
    }
}