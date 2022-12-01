package com.txl.glide

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.LibraryGlideModule
import com.opensource.svgaplayer.SVGACache
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.decoder.InputStreamSVGAEntityDecoder
import com.txl.glide.decoder.ZipInputStreamSVGAEntityDecoder
import com.txl.glide.drawable.SVGAAnimationDrawable
import com.txl.glide.encode.SVGAVideoEntityEncoder
import com.txl.glide.resource.transcode.SVGAAnimationDrawableTranscoder
import com.txl.glide.target.SVGAImageViewTargetFactory
import java.io.File
import java.io.InputStream

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc:
 */
@GlideModule
class SVGAModelInit : LibraryGlideModule() {
    private val tag = SVGAModelInit::class.java.simpleName
    private var isInit = false

    // FIXME: 还需要验证网络传输过程中zip 格式的svga 能否正常工作  (有问题)
    //  编写svga 文件缓存encoder
    //  完善demo
    //  完善文档
    //  发布
    //  接入项目
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        if(isInit){
            return
        }
        isInit = true
        SVGACache.onCreate(context)
        //cachePath is equals to SVGACache.cacheDir
        val cachePath = context.cacheDir.absolutePath + File.separatorChar + "glide-ext-svga"

        //处理InputStream 流转换为SVGAEntity
        registry
            //能够加载出网络和本地非zip 格式的svga
            .register(
                SVGAVideoEntity::class.java,
                SVGAAnimationDrawable::class.java,
                SVGAAnimationDrawableTranscoder()
            )
            .append(//InputStream ——> SVGAVideoEntity  Glide 默认的modelLoader 能够加载出InputStream
                Registry.BUCKET_BITMAP, InputStream::class.java, SVGAVideoEntity::class.java,
                InputStreamSVGAEntityDecoder(cachePath, glide.arrayPool)
            )
            .append(//InputStream ——> SVGAVideoEntity  zip 格式的数据处理
                Registry.BUCKET_BITMAP, InputStream::class.java, SVGAVideoEntity::class.java,
                ZipInputStreamSVGAEntityDecoder(cachePath, glide.arrayPool,registry::getRewinder)
            )
            .append(SVGAVideoEntity::class.java, SVGAVideoEntityEncoder(glide.arrayPool))

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