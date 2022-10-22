package com.txl.glide.helper.reflect

import android.media.SoundPool
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.proto.MovieEntity
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.concurrent.CountDownLatch

/**
 * @author 唐小陆
 * Created on 2022/10/20
 * Desc: 反射  SVGAVideoEntity
 */
object SVGAVideoEntityReflectHelper {

    private var audioListFiled: Field? = null
    private var soundPoolFiled :Field? = null
    private var setupAudiosMethod : Method? = null

    init {
        try {
            val sVGAVideoEntityClass =
                Class.forName("com.opensource.svgaplayer.SVGAVideoEntity")
            audioListFiled = sVGAVideoEntityClass.getDeclaredField("audioList")
            audioListFiled?.isAccessible = true

            soundPoolFiled = sVGAVideoEntityClass.getDeclaredField("soundPool")
            soundPoolFiled?.isAccessible = true

            setupAudiosMethod = sVGAVideoEntityClass.getDeclaredMethod("setupAudios", MovieEntity::class.java,
                kotlin.jvm.functions.Function0::class.java)
            setupAudiosMethod?.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // FIXME: 这个类型要注意 能不能强制转换
    fun getAudioList(videoItem:SVGAVideoEntity):List<Any>{
        return audioListFiled?.get(videoItem) as List<Any>
    }

    fun getSoundPool(videoItem:SVGAVideoEntity): SoundPool?{
        return soundPoolFiled?.get(videoItem) as? SoundPool?
    }

    fun setupAudios(entity: SVGAVideoEntity) {
        val movie = entity.movieItem
        movie?.let {
            setupAudiosMethod?.let { method->
                val waitSync = CountDownLatch(1)
                val callback: () -> Unit = { //callback in mainThread
                    waitSync.countDown()
                }
                try {
                    method.invoke(entity, movie, callback)
                    waitSync.await() //await in GlideThread
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}