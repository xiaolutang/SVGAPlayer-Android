package com.txl.glide.load.data

import android.content.res.AssetManager
import com.bumptech.glide.load.data.AssetPathFetcher
import com.bumptech.glide.load.data.DataRewinder
import com.bumptech.glide.load.model.GlideUrl
import com.txl.glide.helper.InputStreamEncodeFile
import java.io.File
import java.io.InputStream

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class FileAssetPathFetcher(
    assetManager: AssetManager,
    assetPath: String,
    private val cachePath: String,
    private val obtainRewind: (InputStream) -> DataRewinder<InputStream>,
) : AssetPathFetcher<File>(assetManager, assetPath) {

    override fun getDataClass(): Class<File> {
        return File::class.java
    }

    override fun loadResource(assetManager: AssetManager, path: String): File {
        return InputStreamEncodeFile(GlideUrl(path),cachePath, obtainRewind)
            .decodeZipFile(assetManager.open(path))
    }

    override fun close(data: File?) {
        // FIXME: 暂时不删除
//        data?.delete()
    }
}