package com.txl.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.Registry
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.LibraryGlideModule
import com.opensource.svgaplayer.SVGACache
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.decoder.FileSVGAEntityDecoder
import com.txl.glide.decoder.InputStreamSVGAEntityDecoder
import com.txl.glide.drawable.SVGAAnimationDrawable
import com.txl.glide.model.GlideUrlFileModelLoader
import com.txl.glide.resource.transcode.SVGAAnimationDrawableTranscoder
import com.txl.glide.target.SVGAImageViewTargetFactory
import java.io.File
import java.io.InputStream

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc:
 */
class SVGAModelInit : LibraryGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        SVGACache.onCreate(context)
        //cachePath is equals to SVGACache.cacheDir
        val cachePath = context.cacheDir.absolutePath + File.separatorChar + "svga"

        //处理InputStream 流转换为SVGAEntity
        registry
            .register(
                SVGAVideoEntity::class.java,
                SVGAAnimationDrawable::class.java,
                SVGAAnimationDrawableTranscoder()
            )
            .append(//InputStream ——> SVGAVideoEntity  Glide 默认的modelLoader 能够加载出InputStream
                Registry.BUCKET_ANIMATION, InputStream::class.java, SVGAVideoEntity::class.java,
                InputStreamSVGAEntityDecoder(cachePath, glide.arrayPool)
            )
            .append(//InputStream -> File 注册ModelLoader
                GlideUrl::class.java,
                File::class.java,
                GlideUrlFileModelLoader.GlideUrlFileModelLoaderFactory(cachePath,registry::getRewinder)
            )
            .append(//File -> SVGAVideoEntity
                Registry.BUCKET_ANIMATION, File::class.java, SVGAVideoEntity::class.java,
                FileSVGAEntityDecoder(glide.arrayPool)
            )

        hookTheImageViewFactory(glide)
    }

    private fun hookTheImageViewFactory(glide: Glide) {
        try {
            val imageFactory = GlideContext::class.java.getDeclaredField("imageViewTargetFactory")
                ?: return
            val glideContext = Glide::class.java.getDeclaredField("glideContext")
                ?: return
            glideContext.isAccessible = true
            imageFactory.isAccessible = true
            imageFactory.set(glideContext.get(glide), SVGAImageViewTargetFactory())
        } catch (e: Exception) {
            Log.e("SVGAPlayer", e.message, e)
        }
    }
}