package com.txl.glide.helper.reflect

import com.opensource.svgaplayer.SVGADynamicEntity

/**
 * @author 唐小陆
 * Created on 2022/11/26
 * Desc:
 */
object SVGADynamicEntityReflectHelper {
    private val mClickMapFiled = SVGADynamicEntity::class.java.getDeclaredField("mClickMap")

    fun getClickMap(entity:SVGADynamicEntity):HashMap<String, IntArray>{
        var clackMap : HashMap<String, IntArray> = hashMapOf()
        try {
            clackMap =  mClickMapFiled.get(entity) as HashMap<String, IntArray>
        }catch (e:Exception){
            e.printStackTrace()
        }
        return clackMap
    }
}