package com.txl.glide.helper

import java.io.File

/**
 * @author 唐小陆
 * Created on 2022/11/28
 * Desc:
 */
interface ISVGACache {
    /**
     * @param root 新增文件夹的目录
     * @param newFile 新增的文件、文件夹
     * */
    fun createNewFile(root: File?, newFile: File)
}