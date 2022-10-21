package com.txl.glide.resource

import com.bumptech.glide.load.resource.drawable.DrawableResource
import com.txl.glide.drawable.SVGAAnimationDrawable

class SVGAAnimationDrawableResource(private val drawable: SVGAAnimationDrawable?) :
    DrawableResource<SVGAAnimationDrawable>(drawable) {
    override fun getResourceClass(): Class<SVGAAnimationDrawable> {
        return SVGAAnimationDrawable::class.java
    }

    override fun getSize(): Int {
        return drawable.size()
    }

    override fun recycle() {
        drawable.recycle()
    }
}