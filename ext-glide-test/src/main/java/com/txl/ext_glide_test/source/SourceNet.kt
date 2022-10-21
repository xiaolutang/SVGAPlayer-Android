package com.txl.ext_glide_test.source

/**
 * @author 唐小陆
 * Created on 2022/10/21
 * Desc:
 */
class SourceNet() : ISource {
    override fun getPath(name: String): String {
        return "file:///android_asset/${name}.svga"
    }
}