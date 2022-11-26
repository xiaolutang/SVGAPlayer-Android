package com.txl.glide

import android.content.Context
import android.net.Uri
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideContext
import com.bumptech.glide.Registry
import com.bumptech.glide.module.LibraryGlideModule
import com.opensource.svgaplayer.SVGACache
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.decoder.FileSVGAEntityDecoder
import com.txl.glide.decoder.InputStreamSVGAEntityDecoder
import com.txl.glide.drawable.SVGAAnimationDrawable
import com.txl.glide.encode.FileEncoder
import com.txl.glide.encode.SVGAVideoEntityEncoder
import com.txl.glide.model.GlideUrlFileModelLoader
import com.txl.glide.model.factory.AssetUriFileLoaderFactory
import com.txl.glide.model.factory.FileFileModelLoaderFactory
import com.txl.glide.model.factory.StringFileModelLoaderFactory
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
    private val tag = SVGAModelInit::class.java.simpleName

    // FIXME: 还需要验证网络传输过程中zip 格式的svga 能否正常工作  (有问题)
    //  编写svga 文件缓存encoder
    //  完善demo
    //  完善文档
    //  发布
    //  接入项目
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        SVGACache.onCreate(context)
        //cachePath is equals to SVGACache.cacheDir
        val cachePath = context.cacheDir.absolutePath + File.separatorChar + "svga"

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

            //处理本地asset目录中的zip 格式svga
            .append(//InputStream -> File 注册ModelLoader
                String::class.java,
                File::class.java,
                StringFileModelLoaderFactory()
            )
            .append(
                Uri::class.java,
                File::class.java,
                AssetUriFileLoaderFactory(context.assets,cachePath,registry::getRewinder)
            )

                //处理网络中的zip 格式svga  这个方式缓存不对
            .append(
                Uri::class.java,
                File::class.java,
                GlideUrlFileModelLoader.GlideUrlFileModelLoaderFactory(
                    cachePath,
                    registry::getRewinder
                )
            )

            .append(File::class.java, FileEncoder(glide.arrayPool))
            .append(SVGAVideoEntity::class.java, SVGAVideoEntityEncoder(glide.arrayPool))
//
                //修复SVGA文件缓存 在从缓存中加载 因为 原始加载路径 存在ModelLoader -> LoadPath
            //  DataCacheGenerator 的加载链路只会执行一个的问题  通过prepend 让自己的ModelLoader 先于Glide
            //  框架层被找到  从而优先尝试自己的 加载路劲
            .prepend(File::class.java,File::class.java,
                FileFileModelLoaderFactory()
            )
            .append(//File -> SVGAVideoEntity
                Registry.BUCKET_BITMAP, File::class.java, SVGAVideoEntity::class.java,
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