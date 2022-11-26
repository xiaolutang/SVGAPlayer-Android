package com.txl.glide.encode

import android.util.Log
import com.bumptech.glide.load.Encoder
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool
import com.txl.glide.helper.isSVGAUnZipFile
import com.txl.glide.helper.makeSureExist
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
        if(file.isDirectory){
            Log.e("FileEncoder ","file is zip ${file.isSVGAUnZipFile()}")
        }
        if(data.isDirectory){
            Log.e("FileEncoder ","file is zip ${file.isSVGAUnZipFile()}")
        }
//        copyFile(data,file)
//        copyFileUsingStream(data,file)
        copyDir(data,file)
        //拷贝到diskLruCache后 将缓存目录下的文件直接删除
        data.delete()
        Log.e("FileEncoder ","file is zip ${file.isSVGAUnZipFile()}")
        return true
    }

    @Throws(IOException::class)
    fun copyFile(zipFile: File?, newFile: File?) {
        newFile?.makeSureExist()
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

    //使用io的方式复制文件，复制后源文件也还在。
    @Throws(IOException::class)
    private fun copyFileUsingStream(source: File, dest: File) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null
        try {
            inputStream = FileInputStream(source)
            outputStream = FileOutputStream(dest)
            val buffer = ByteArray(1024)
            var length: Int
            while (inputStream.read(buffer).also { length = it } > 0) {
                outputStream.write(buffer, 0, length)
            }
        } finally {
            inputStream?.close()
            outputStream?.close()
        }
    }

    /**
     * 复制文件夹的方法
     * @param srcDir：源文件目录
     * @param targetDir：目的文件目录
     */
    private fun copyDir(srcDir:File ,targetDir:File){
        targetDir.makeSureExist()
        val files = srcDir.listFiles(); //获取指定目录下的所有File对象
        files?.forEach { file ->
            if(file.isFile){
                copyFileUsingStream(File(srcDir,file.name),File(targetDir,file.name))
            }else{
                copyDir(File(srcDir,file.name),File(targetDir,file.name))
            }
        }

    }



}