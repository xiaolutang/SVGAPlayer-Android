package com.txl.glide.helper.reflect

import com.opensource.svgaplayer.SVGASoundManager
import java.lang.reflect.Method
import kotlin.reflect.full.companionObjectInstance

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc:
 */
object SVGASoundManagerReflectHelper {
    private val isInitMethod = SVGASoundManager::class.members.find { it.name == "isInit" }
    private val stopMethod = SVGASoundManager::class.members.find { it.name == "stop" }
    private val pauseMethod = SVGASoundManager::class.members.find { it.name == "pause" }
        //兼容项目上的2.5版本
    private var manager:SVGASoundManager? = null
    private var isInitMethod2:Method? = null
    private var stopMethod2:Method? = null
    private var pauseMethod2:Method? = null

    init {
        try {
            val get = SVGASoundManager::class.companionObjectInstance?.javaClass?.getDeclaredMethod("get")
            manager = get?.invoke(SVGASoundManager::class.companionObjectInstance) as SVGASoundManager?
            isInitMethod2 = SVGASoundManager::class.java.getDeclaredMethod("isInit")
            stopMethod2 = SVGASoundManager::class.java.getDeclaredMethod("stop",Int::class.java)
            pauseMethod2 = SVGASoundManager::class.java.getDeclaredMethod("pause",Int::class.java)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    fun isInit():Boolean{
        if(manager != null){
            return isInitMethod2?.invoke(manager) == true
        }
        return isInitMethod?.call(SVGASoundManager) == true
    }

    fun stop(soundId: Int){
        if(manager != null){
            stopMethod2?.invoke(manager,soundId)
            return
        }
        stopMethod?.call(SVGASoundManager,soundId)
    }

    fun pause(soundId: Int){
        if(manager != null){
            pauseMethod2?.invoke(manager,soundId)
            return
        }
        pauseMethod?.call(SVGASoundManager,soundId)
    }
}