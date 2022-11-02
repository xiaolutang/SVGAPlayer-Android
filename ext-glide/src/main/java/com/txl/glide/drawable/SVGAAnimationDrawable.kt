package com.txl.glide.drawable

import android.animation.Animator
import android.animation.ValueAnimator

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.media.SoundPool
import android.os.Build
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.opensource.svgaplayer.SVGADynamicEntity
import com.opensource.svgaplayer.SVGAVideoEntity
import com.txl.glide.drawer.SGVADrawerProxy
import com.txl.glide.helper.reflect.SVGAAudioEntityReflectHelper
import com.txl.glide.helper.reflect.SVGASoundManagerReflectHelper
import com.txl.glide.helper.reflect.SVGAVideoEntityReflectHelper

/**
 * 当同一个SVGA图片被加载的时候 如果此时svga动画在运行中他们会共享同样的动画效果
 *
 * ***/
class SVGAAnimationDrawable(
    private val videoItem: SVGAVideoEntity,
    private val dynamicItem: SVGADynamicEntity
) : Animatable, Drawable(), ValueAnimator.AnimatorUpdateListener {

    var repeatCount: Int = ValueAnimator.INFINITE
        set(value) {
            resetAnimation()
            field = value
        }
    var repeatMode: Int = ValueAnimator.RESTART
        set(value) {
            resetAnimation()
            field = value
        }

    var hasAudio: Boolean? = null

    var size: Int = 0

    private var visible = true
    private var isStarted = true

    companion object {
        const val TAG = "SVGAAnimationDrawable"
    }

    var animatorListener: Animator.AnimatorListener? = null
        set(value) {
            mAnimator?.removeListener(field)
            field = value
            value?.let {
                mAnimator?.addListener(it)
            }

        }

    var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null

    private var mAnimator: ValueAnimator? = null
    private var currentFrame = 0

    //一共有多少帧
    private var totalFrame = 0

    private var drawer = SGVADrawerProxy.createSGVADrawer(videoItem, dynamicItem)

    private var mSoundPool: SoundPool? = null

    private fun getSoundPool(): SoundPool? {
        if (mSoundPool == null) {
            mSoundPool = SVGAVideoEntityReflectHelper.getSoundPool(videoItem)
        }
        return mSoundPool
    }

    var scaleType = ImageView.ScaleType.MATRIX

    private fun resetAnimation() {
        val isR = mAnimator?.isRunning
        mAnimator?.cancel()
        mAnimator = null
        if (isRunning) {
            start()
        }
    }

    fun resetDynamicEntity(dynamicItem: SVGADynamicEntity) {
        drawer = SGVADrawerProxy.createSGVADrawer(videoItem, dynamicItem)
        resetAnimation()
    }

    override fun getIntrinsicWidth(): Int {
//        return videoItem.videoSize.width.toInt()
        return -1
    }

    override fun getIntrinsicHeight(): Int {
//        return videoItem.videoSize.height.toInt()
        return -1
    }

    override fun start() {
        Log.d(TAG, "start")
        isStarted = true
        if(visible){
            startAnimation()
        }
    }

    private fun startAnimation() {
        if (mAnimator == null || mAnimator?.isRunning == false) {
            val startFrame = 0
            val endFrame = videoItem.frames - 1
            totalFrame = (endFrame - startFrame + 1)
            mAnimator = ValueAnimator.ofInt(startFrame, endFrame)
            mAnimator?.interpolator = LinearInterpolator()
            mAnimator?.duration = (totalFrame * (1000 / videoItem.FPS) / generateScale()).toLong()
            mAnimator?.repeatCount = repeatCount
            mAnimator?.repeatMode = repeatMode
            mAnimator?.addUpdateListener(this)
            animatorListener?.let {
                mAnimator?.addListener(it)
            }

            mAnimator?.start()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mAnimator?.resume()
            } else {
                mAnimator?.start()
            }
        }
    }

    private fun generateScale(): Double {
        var scale = 1.0
        try {
            val animatorClass = Class.forName("android.animation.ValueAnimator") ?: return scale
            val getMethod = animatorClass.getDeclaredMethod("getDurationScale") ?: return scale
            scale = (getMethod.invoke(animatorClass) as Float).toDouble()
            if (scale == 0.0) {
                val setMethod =
                    animatorClass.getDeclaredMethod("setDurationScale", Float::class.java)
                        ?: return scale
                setMethod.isAccessible = true
                setMethod.invoke(animatorClass, 1.0f)
                scale = 1.0
            }
        } catch (ignore: Exception) {
            ignore.printStackTrace()
        }
        return scale
    }


    fun hasAudio(): Boolean {
        if (hasAudio == null) {
            hasAudio = SVGAVideoEntityReflectHelper.getAudioList(videoItem).isNotEmpty()
        }
        return hasAudio == true
    }


    override fun setVisible(visible: Boolean, restart: Boolean): Boolean {
        this.visible = visible
        if (!visible) {
            stopAnimation()
        } else if (isStarted) {
            startAnimation()
        }
        return super.setVisible(visible, restart)
    }

    override fun stop() {
        Log.d(TAG, "stop")
        isStarted = false
        stopAnimation()
    }

    private fun stopAnimation() {
        mAnimator?.cancel()
        mAnimator = null
        SVGAVideoEntityReflectHelper.getAudioList(videoItem).forEach {
            SVGAAudioEntityReflectHelper.getPlayID(it)?.let { playId ->
                if (SVGASoundManagerReflectHelper.isInit()) {
                    SVGASoundManagerReflectHelper.pause(playId)
                } else {
                    getSoundPool()?.pause(playId)
                }
            }
        }
    }

    override fun isRunning(): Boolean {
        return mAnimator?.isRunning == true
    }

    override fun draw(canvas: Canvas) {
        //fixme 暂时不处理缩放问题 后续源码更新
        drawer.drawFrame(canvas, currentFrame, scaleType)
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        currentFrame = animation?.animatedValue as Int
        invalidateSelf()
        animatorUpdateListener?.onAnimationUpdate(animation)
    }

    /**
     * 跳转到指定帧
     * */
    fun stepToFrame(frame: Int, andPlay: Boolean) {
        stopAnimation()
        currentFrame = frame
        invalidateSelf()
        if (andPlay) {
            startAnimation()
            mAnimator?.let {
                it.currentPlayTime = (Math.max(0.0f, Math.min(1.0f, (frame.toFloat() / videoItem.frames.toFloat()))) * it.duration).toLong()
            }
        }
    }

    // FIXME: 完成资源回收
    fun recycle() {
        clear()
        animatorUpdateListener = null
        animatorListener = null
    }

    fun size(): Int {
        return size
    }

    private fun clear() {
        SVGAVideoEntityReflectHelper.getAudioList(videoItem).forEach { audio ->
            SVGAAudioEntityReflectHelper.getPlayID(audio)?.let { playId ->
                if (SVGASoundManagerReflectHelper.isInit()) {
                    SVGASoundManagerReflectHelper.stop(playId)
                } else {
                    getSoundPool()?.stop(playId)
                }
            }
            SVGAAudioEntityReflectHelper.setPlayId(audio, null)
        }
        videoItem.clear()
    }
}