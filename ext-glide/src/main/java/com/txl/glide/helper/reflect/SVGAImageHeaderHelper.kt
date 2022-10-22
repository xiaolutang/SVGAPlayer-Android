package com.txl.glide.helper.reflect

import android.util.Log
import java.io.InputStream

/**
 * @author 唐小陆
 * Created on 2022/10/19
 * Desc: 判断资源类型
 */
object SVGAImageHeaderHelper {
    private val ByteArray.isZipFormat
        get() = this.size >= 4 &&
                this[0].toInt() == 80 &&
                this[1].toInt() == 75 &&
                this[2].toInt() == 3 &&
                this[3].toInt() == 4

    private val ByteArray.isZLibFormat
        get() = this.size >= 2 && //78 9C
                this[0].toInt() == 120 &&
                this[1].toInt() == -100


    /**
     * Note: don't close the inputStream!
     */
    private fun readHeadAsBytes(inputStream: InputStream): ByteArray? = attempt {
        val byteArray = ByteArray(4)
        val count = inputStream.read(byteArray, 0, 4)
        if (count <= 0) {
            null
        } else {
            byteArray
        }
    }

    private inline fun <T : Any> attempt(action: () -> T?): T? {
        return try {
            action()
        } catch (e: Throwable) {
            handleError(e)
            null
        }
    }

    private fun handleError(e: Throwable) = Log.e("SVGAPlayer", e.message, e)


    fun isZLibFormatStream(inputStream: InputStream?): Boolean {
        inputStream?.let {
            return readHeadAsBytes(it)?.isZLibFormat == true
        }
        return false
    }

    fun isZipFormatStream(inputStream: InputStream?): Boolean {
        inputStream?.let {
            return readHeadAsBytes(it)?.isZipFormat == true
        }
        return false
    }



}