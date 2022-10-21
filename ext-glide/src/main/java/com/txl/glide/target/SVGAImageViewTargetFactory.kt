package com.txl.glide.target

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.target.ImageViewTargetFactory
import com.bumptech.glide.request.target.ViewTarget
import com.txl.glide.drawable.SVGAAnimationDrawable

/**
 * @author 唐小陆
 * Created on 2022/10/21
 * Desc:
 */
class SVGAImageViewTargetFactory: ImageViewTargetFactory() {
    override fun <Z : Any?> buildTarget(
        view: ImageView,
        clazz: Class<Z>
    ): ViewTarget<ImageView, Z> {
        if(Drawable::class.java.isAssignableFrom(clazz)){
            return SVGAImageViewTarget.Builder(view).build() as (ViewTarget<ImageView, Z>)
        }
        return super.buildTarget(view, clazz)
    }
}