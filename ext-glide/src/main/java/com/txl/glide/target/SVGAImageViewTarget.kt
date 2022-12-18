package com.txl.glide.target

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.ImageView
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.opensource.svgaplayer.SVGAClickAreaListener
import com.opensource.svgaplayer.SVGADynamicEntity
import com.txl.glide.drawable.SVGAAnimationDrawable
import com.txl.glide.helper.reflect.SVGADynamicEntityReflectHelper
import java.lang.ref.WeakReference
import kotlin.math.min

class SVGAImageViewTarget private constructor(imageView: ImageView) : DrawableImageViewTarget(imageView),
    ValueAnimator.AnimatorUpdateListener {

    companion object {
        /**
         * 不设置重复次数  在资源准备好的时候  如果SVGA 有音频 则只重复一次，没有则一直重复下去
         * */
        const val REPEAT_COUNT_UN_SET = -2
    }
    private var repeatCount: Int = REPEAT_COUNT_UN_SET
    private var repeatMode: Int = ValueAnimator.RESTART
    private var animatorListener: Animator.AnimatorListener? = null
    private var animatorUpdateListener: ValueAnimator.AnimatorUpdateListener? = null
    private var dynamicEntity: SVGADynamicEntity = SVGADynamicEntity()
    private var mItemClickAreaListener: SVGAClickAreaListener? = null
    private var toFrame: ToFrame? = null
    private var audioPlayerListener: SVGAAnimationDrawable.AudioPlayerListener? = null

    private val internalAnimatorListener: Animator.AnimatorListener = object :Animator.AnimatorListener{
        override fun onAnimationStart(animation: Animator?) {
            resetScaleType(view.drawable)
            animatorListener?.onAnimationStart(animation)
        }

        override fun onAnimationEnd(animation: Animator?) {
            animatorListener?.onAnimationEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {
            animatorListener?.onAnimationCancel(animation)
        }

        override fun onAnimationRepeat(animation: Animator?) {
            resetScaleType(view.drawable)
            animatorListener?.onAnimationRepeat(animation)
        }
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        if (resource is SVGAAnimationDrawable) {
            resetScaleType(resource)
            resource.animatorListener = WeakReference(internalAnimatorListener)
            resource.animatorUpdateListener = WeakReference(this)
            resource.repeatMode = repeatMode
            if(repeatCount == REPEAT_COUNT_UN_SET){
                if(resource.hasAudio()){
                    resource.repeatCount = 0
                }else{
                    resource.repeatCount = ValueAnimator.INFINITE
                }
            }else{
                resource.repeatCount = repeatCount
            }
            resource.resetDynamicEntity(dynamicEntity)
            //这个需要测试 会不会导致View的点击事件失效
            mItemClickAreaListener?.let {
                view.setOnTouchListener { v, event ->
                    if (event?.action != MotionEvent.ACTION_DOWN) {
                        return@setOnTouchListener false
                    }
                    val clickMap = SVGADynamicEntityReflectHelper.getClickMap(dynamicEntity)
                    for ((key, value) in clickMap) {
                        if (event.x >= value[0] && event.x <= value[2] && event.y >= value[1] && event.y <= value[3]) {
                            mItemClickAreaListener?.let {
                                it.onClick(key)
                                return@setOnTouchListener true
                            }
                        }
                    }
                    return@setOnTouchListener false
                }
            }
            toFrame?.let { myToFrame->
                val frame = min(myToFrame.frame,resource.videoItem.frames-1)
                resource.stepToFrame(frame,myToFrame.play)
            }
            audioPlayerListener?.let { listener->
                resource.audioPlayerListener = WeakReference(listener)
            }
        }
        super.onResourceReady(resource, transition)
    }


    override fun onAnimationUpdate(animation: ValueAnimator?) {
        resetScaleType(view.drawable)
        animatorUpdateListener?.onAnimationUpdate(animation)
    }

    /**
     * 处理播放过程中 切换ImageViewScaleType 失效的问题
     * */
    private fun resetScaleType(drawable: Drawable?) {
        if (drawable is SVGAAnimationDrawable) {
            drawable.scaleType = view.scaleType
        }
    }

    class Builder(private val imageView: ImageView) {

        private val target = SVGAImageViewTarget(imageView)

        fun setAnimationRepeatCount(repeatCount: Int): Builder {
            target.repeatCount = repeatCount
            return this
        }

        fun setRepeatMode(repeatMode: Int): Builder {
            target.repeatMode = repeatMode
            return this
        }

        fun setAnimationListener(animatorListener: Animator.AnimatorListener?): Builder {
            target.animatorListener = animatorListener
            return this
        }

        fun setAnimationUpdateListener(animatorUpdateListener:ValueAnimator.AnimatorUpdateListener):Builder{
            target.animatorUpdateListener = animatorUpdateListener
            return this
        }

        fun setDynamicEntity(dynamicEntity: SVGADynamicEntity):Builder{
            target.dynamicEntity = dynamicEntity
            return this
        }

        fun setItemClickAreaListener(mItemClickAreaListener: SVGAClickAreaListener):Builder{
            target.mItemClickAreaListener = mItemClickAreaListener
            return this
        }

        fun setToFrame(frame: Int,play: Boolean):Builder{
            target.toFrame = ToFrame(frame, play)
            return this
        }

        fun setAudioPlayerListener(listener: SVGAAnimationDrawable.AudioPlayerListener): Builder {
            target.audioPlayerListener = listener
            return this
        }

        fun build():SVGAImageViewTarget{
           return target
        }
    }

    class ToFrame(val frame:Int = 0,val play:Boolean = true)
}