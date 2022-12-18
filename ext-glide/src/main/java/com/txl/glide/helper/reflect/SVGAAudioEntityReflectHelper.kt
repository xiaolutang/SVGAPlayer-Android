package com.txl.glide.helper.reflect

import kotlin.reflect.KMutableProperty

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc:反射 SVGAAudioEntity
 */
object SVGAAudioEntityReflectHelper {
    private var playIDFiled: KMutableProperty<*>? = null
    private var soundIDFiled: KMutableProperty<*>? = null

    init {
        val sVGAAudioEntity =
            Class.forName("com.opensource.svgaplayer.entities.SVGAAudioEntity").kotlin
        sVGAAudioEntity.members.find { it.name == "playID" }?.let {
            if(it is KMutableProperty<*>){
                playIDFiled = it
            }
        }
        sVGAAudioEntity.members.find { it.name == "soundID" }?.let {
            if(it is KMutableProperty<*>){
                soundIDFiled = it
            }
        }
    }

    fun getPlayID(any: Any): Int? {
        return playIDFiled?.getter?.call(any) as? Int?
    }

    fun setPlayId(any: Any, playId: Int?) {
        playIDFiled?.setter?.call(any,playId)
    }

    fun getSoundID(any: Any): Int? {
        return soundIDFiled?.getter?.call(any) as? Int?
    }

    fun setSoundID(any: Any, playId: Int?) {
        soundIDFiled?.setter?.call(any,playId)
    }
}