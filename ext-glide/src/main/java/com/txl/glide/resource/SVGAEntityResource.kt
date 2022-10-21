package com.txl.glide.resource

import com.bumptech.glide.load.engine.Resource
import com.opensource.svgaplayer.SVGAVideoEntity

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc:
 */
class SVGAEntityResource(private val entity: SVGAVideoEntity, private val size: Int) :
    Resource<SVGAVideoEntity> {
    override fun getResourceClass() = SVGAVideoEntity::class.java

    override fun get(): SVGAVideoEntity = entity

    override fun getSize() = size

    override fun recycle() {
        entity.clear()
    }
}