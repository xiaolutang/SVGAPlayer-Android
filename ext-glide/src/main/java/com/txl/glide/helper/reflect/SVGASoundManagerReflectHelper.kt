package com.txl.glide.helper.reflect

import com.opensource.svgaplayer.SVGASoundManager

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc:
 */
object SVGASoundManagerReflectHelper {
    private val isInitMethod = SVGASoundManager::class.members.find { it.name == "isInit" }
    private val stopMethod = SVGASoundManager::class.members.find { it.name == "stop" }
    private val pauseMethod = SVGASoundManager::class.members.find { it.name == "pause" }

    init {
//        isInitMethod.isAccessible = true
//        stopMethod.isAccessible = true
//        pauseMethod.isAccessible = true
    }

    fun isInit():Boolean{
        return isInitMethod?.call(SVGASoundManager) == true
    }

    fun stop(soundId: Int){
        stopMethod?.call(SVGASoundManager,soundId)
    }

    fun pause(soundId: Int){
        pauseMethod?.call(SVGASoundManager,soundId)
    }
}