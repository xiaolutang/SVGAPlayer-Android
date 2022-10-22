package com.txl.glide.encode

import android.util.Log
import com.bumptech.glide.load.Encoder
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.txl.glide.helper.isSVGAUnZipFile
import java.io.*
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

/**
 * @author 唐小陆
 * Created on 2022/10/22
 * Desc:
 */
class FileEncoder(private val arrayPool: ArrayPool) : Encoder<File> {
    override fun encode(data: File, file: File, options: Options): Boolean {
        if(!data.isSVGAUnZipFile()){
            return false
        }
        copyFile(data,file)
        //拷贝到diskLruCache后 将缓存目录下的文件直接删除
        data.delete()
        Log.e("FileEncoder ","file is zip ${file.isSVGAUnZipFile()}")
        return true
    }

    @Throws(IOException::class)
    fun copyFile(zipFile: File?, newFile: File?) {
        val zipSrc = ZipFile(zipFile)
        val zos = ZipOutputStream(FileOutputStream(newFile))
        val srcEntries: Enumeration<*> = zipSrc.entries()
        while (srcEntries.hasMoreElements()) {
            val entry: ZipEntry = srcEntries.nextElement() as ZipEntry
            val newEntry = ZipEntry(entry.getName())
            zos.putNextEntry(newEntry)
            val bis = BufferedInputStream(
                zipSrc
                    .getInputStream(entry)
            )
            while (bis.available() > 0) {
                zos.write(bis.read())
            }
            zos.closeEntry()
            bis.close()
        }
        zos.finish()
        zos.close()
        zipSrc.close()
    }
}