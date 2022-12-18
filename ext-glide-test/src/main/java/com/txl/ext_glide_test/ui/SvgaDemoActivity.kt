package com.txl.ext_glide_test.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.txl.ext_glide_test.R


class SvgaDemoActivity : AppCompatActivity() {
    private val  tag = SvgaDemoActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_svga_demo)
        initView()
    }

    private fun initView() {
        setClickListener()
    }

    private fun setClickListener() {
        findViewById<View>(R.id.tvAssetDemo).setOnClickListener {
            startActivity(Intent(this,LoadAssetSvgaActivity::class.java))
        }
        findViewById<View>(R.id.tvNetDemo).setOnClickListener {
            startActivity(Intent(this,LoadNetSvgaActivity::class.java))
        }
        findViewById<View>(R.id.tvChangeScaleTypeDemo).setOnClickListener {
            startActivity(Intent(this,ChangeScaleTypeActivity::class.java))
        }
        findViewById<View>(R.id.tvReplaceResourceDemo).setOnClickListener {
            startActivity(Intent(this,ReplaceResourceActivity::class.java))
        }
        findViewById<View>(R.id.tvSetRepeatCountDemo).setOnClickListener {
            startActivity(Intent(this,ChangeRepeatCountActivity::class.java))
        }
        findViewById<View>(R.id.tvSetRepeatModeDemo).setOnClickListener {
            startActivity(Intent(this,ChangeRepeatModeActivity::class.java))
        }
        findViewById<View>(R.id.tvAddListenerDemo).setOnClickListener {
            startActivity(Intent(this,AddListenerActivity::class.java))
        }
        findViewById<View>(R.id.tvNormalGlide).setOnClickListener {
            startActivity(Intent(this,NormalGlideLoadActivity::class.java))
        }
        findViewById<View>(R.id.tvPlayAudio).setOnClickListener {
            startActivity(Intent(this,LoadAudioSvgaActivity::class.java))
        }
        findViewById<View>(R.id.tvCompareMemory).setOnClickListener {
            startActivity(Intent(this,LoadSvgaCompareMemoryActivity::class.java))
        }
        findViewById<View>(R.id.tvTestAllSvgaFile).setOnClickListener {
            startActivity(Intent(this,TestAllSVGAFileActivity::class.java))
        }
        findViewById<View>(R.id.tvCustomPlayAudio).setOnClickListener {
            startActivity(Intent(this,CustomPlayAudioSvgaActivity::class.java))
        }
    }
}