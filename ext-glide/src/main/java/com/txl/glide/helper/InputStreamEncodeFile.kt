package com.txl.glide.helper


import com.bumptech.glide.load.data.DataRewinder
import com.bumptech.glide.load.model.GlideUrl
import java.io.*
import java.security.MessageDigest
import java.util.zip.ZipInputStream

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class InputStreamEncodeFile(
    private val glideUrl: GlideUrl,
    private val cachePath: String,
    private val obtainRewind: (InputStream) -> DataRewinder<InputStream>
) {
    fun decodeZipFile(data: InputStream): File {
        readAsBytes(data).let { bytes ->
            val isZip = isZipFile(bytes)
            if (!isZip) {
                throw IOException("data not zip ")
            }
            try {
                cacheDir.makeSureExist()
                ByteArrayInputStream(bytes).use {
                    unzip(it, cacheDir)
                }
                return cacheDir
            } catch (e: Exception) {
                throw e
            } finally {

            }
        }
    }



    private fun unzip(inputStream: InputStream, dir: File) {
        ZipInputStream(inputStream).use { zipInputStream ->
            while (true) {
                val zipItem = zipInputStream.nextEntry ?: break
                if (zipItem.name.contains("../")) {
                    // 解压路径防止穿透
                    continue
                }
                if (zipItem.name.contains("/")) {
                    continue
                }
                val file = File(dir, zipItem.name)
                ensureUnzipSafety(file, cacheDir.absolutePath)
                FileOutputStream(file).use { fileOutputStream ->
                    val buff = ByteArray(2048)
                    while (true) {
                        val readBytes = zipInputStream.read(buff)
                        if (readBytes <= 0) {
                            break
                        }
                        fileOutputStream.write(buff, 0, readBytes)
                    }
                }
                zipInputStream.closeEntry()
            }
        }
    }

    // 检查 zip 路径穿透
    private fun ensureUnzipSafety(outputFile: File, dstDirPath: String) {
        val dstDirCanonicalPath = File(dstDirPath).canonicalPath
        val outputFileCanonicalPath = outputFile.canonicalPath
        if (!outputFileCanonicalPath.startsWith(dstDirCanonicalPath)) {
            throw IOException("Found Zip Path Traversal Vulnerability with $dstDirCanonicalPath")
        }
    }



    private fun cacheKey(glideUrl: GlideUrl): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(glideUrl.toStringUrl().toByteArray(charset("UTF-8")))
        val digest = messageDigest.digest()
        var sb = ""
        for (b in digest) {
            sb += String.format("%02x", b)
        }
        return sb
    }

    private val cacheDir: File by lazy(LazyThreadSafetyMode.NONE) {
        File(cachePath, cacheKey(glideUrl))
    }

    private fun File.makeSureExist() {
        val dir = this
        if (dir.exists()) {
            if (!dir.isDirectory) {
                dir.deleteRecursively()
                dir.mkdirs()
            }
        } else {
            dir.mkdirs()
        }
        SVGACacheManager.createNewFile(parentFile,this)
    }
}