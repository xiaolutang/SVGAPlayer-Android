package com.txl.glide

import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideType
import com.bumptech.glide.request.RequestOptions
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.drawable.SVGAAnimationDrawable

/**
 * @author 唐小陆
 * Created on 2022/11/30
 * Desc:
 */
@GlideExtension
object SVGATypeExtension {

    val DECODE_TYPE_SVGA = RequestOptions.decodeTypeOf(SVGAVideoEntity::class.java).lock()

    @JvmStatic
    @GlideType(SVGAAnimationDrawable::class)
    fun asSVGA(requestBuilder: RequestBuilder<SVGAAnimationDrawable>): RequestBuilder<SVGAAnimationDrawable> {
        return requestBuilder.apply(DECODE_TYPE_SVGA)
    }
}