package com.txl.glide.model.factory

import android.content.res.AssetManager
import android.net.Uri
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.data.DataRewinder
import com.bumptech.glide.load.model.AssetUriLoader
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.txl.glide.load.data.FileAssetPathFetcher
import java.io.File
import java.io.InputStream

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class AssetUriFileLoaderFactory(
    private val assetManager: AssetManager,
    private val cachePath: String,
    private val obtainRewind: (InputStream) -> DataRewinder<InputStream>
) :
    ModelLoaderFactory<Uri, File>, AssetUriLoader.AssetFetcherFactory<File> {
    override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<Uri, File> {
        return AssetUriLoader(assetManager, this)
    }

    override fun teardown() {

    }

    override fun buildFetcher(assetManager: AssetManager, assetPath: String): DataFetcher<File> {
        return FileAssetPathFetcher(assetManager,assetPath,cachePath,obtainRewind)
    }


}