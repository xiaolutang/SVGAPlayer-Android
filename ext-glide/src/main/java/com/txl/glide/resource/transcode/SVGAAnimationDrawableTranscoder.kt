package com.txl.glide.resource.transcode

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.drawable.SVGAAnimationDrawable
import com.txl.glide.resource.SVGAAnimationDrawableResource

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc:
 */
class SVGAAnimationDrawableTranscoder : ResourceTranscoder<SVGAVideoEntity, SVGAAnimationDrawable> {
    override fun transcode(
        toTranscode: Resource<SVGAVideoEntity>,
        options: Options
    ): Resource<SVGAAnimationDrawable>? {
        val svgaAnimationDrawable = SVGAAnimationDrawable(
            toTranscode.get(),
            SVGADynamicEntity()
        )
        svgaAnimationDrawable.size = toTranscode.size
        return SVGAAnimationDrawableResource(svgaAnimationDrawable)
    }
}