package com.txl.ext_glide_test.source

/**
 * @author 唐小陆
 * Created on 2022/10/21
 * Desc:
 */
class SourceNet() : ISource {
    override fun getPath(name: String): String {
//        return "https://github.com/xiaolutang/SVGAPlayer-Android/blob/feature/txl/ext_glide_2/ext-glide-test/src/main/assets/${name}.svga?raw=true"
        return "https://github.com/xiaolutang/SVGAPlayer-Android/blob/feature/txl/ext_glide_2/ext-glide-test/src/main/assets/${name}.svga?raw=true"
//        return "https://github.com/YvesCheung/SVGAGlidePlugin/blob/master/" +
//                "app/src/main/assets/${name}.svga?raw=true"
    }
}