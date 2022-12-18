package com.txl.glide.helper

import com.opensource.svgaplayer.proto.AudioEntity
import java.io.File

/**
 * @author 唐小陆
 * Created on 2022/12/16
 * Desc:
 */
class AudioPlayerEntity(audioItem: AudioEntity) {
    val audioKey: String? = audioItem.audioKey
    val startFrame: Int = audioItem.startFrame ?: 0
    val endFrame: Int = audioItem.endFrame ?: 0
    val startTime: Int = audioItem.startTime ?: 0
    val totalTime: Int = audioItem.totalTime ?: 0

    var file:File? = null
}