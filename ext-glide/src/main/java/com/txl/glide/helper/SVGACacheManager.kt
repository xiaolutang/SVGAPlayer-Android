package com.txl.glide.helper

import java.io.File
import java.io.FileFilter
import java.util.*
import java.util.concurrent.Executors

/**
 * @author 唐小陆
 * Created on 2022/11/28
 * Desc:
 */
object SVGACacheManager : ISVGACache {
    private const val MAX_DIR_SIZE = 3000
    private var first = true

    //时差减小 5分钟
    private val startTime = System.currentTimeMillis() - 5 * 60 * 1000
    private val execute = Executors.newSingleThreadExecutor { r ->
        val result: Thread = Thread(r, "svga-ext-glide-cache-thread")
        result.priority = Thread.MIN_PRIORITY
        result
    }

    private val defaultSVGACache = object : ISVGACache {
        override fun createNewFile(root: File?, newFile: File) {
            if (root == null) {
                throw RuntimeException("cache parent file is null")
            }
            val size = root.list()?.size ?: 0
            if (root.isDirectory && size > MAX_DIR_SIZE || first) {
                execute.submit {
                    var files = root.listFiles(FileFilter { file ->
                        file.lastModified() < startTime
                    })
                    if(first){
                        files?.forEach { file ->
                            //第一次时 删除上次app运行期间创建的与本次SVGACacheManager 初始化时差大于 MAX_DIR_SIZE 的文件
                            if (!newFile.name.equals(file.name)) {
                                file.deleteRecursively()
                            }
                        }
                        first = false
                        return@submit
                    }

                    val secondSize = root.list()?.size ?: 0
                    if (secondSize > MAX_DIR_SIZE) {
                        files = root.listFiles()
                        if (files == null) {
                            return@submit
                        }
                        Arrays.sort(files) { o1, o2 -> (o1.lastModified() - o2.lastModified()).toInt() }
                        val deleteSize = secondSize - MAX_DIR_SIZE + 1
                        for (i in 0 until deleteSize){
                            files[i].deleteRecursively()
                            val file = files[i]
                            if (!newFile.name.equals(file.name)) {
                                file.deleteRecursively()
                            }
                        }
                    }
                }
            }
        }
    }

    var svgaCache: ISVGACache = defaultSVGACache

    override fun createNewFile(root: File?, newFile: File) {
        svgaCache.createNewFile(root, newFile)
    }
}