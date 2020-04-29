package com.example.drawline.ui.bai1

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drawline.R
import kotlinx.android.synthetic.main.fragment_bai1.*
import kotlinx.android.synthetic.main.fragment_bai1.view.*
import pl.droidsonroids.gif.GifImageView


class Bai1Frag : Fragment() {
    lateinit var player: MediaPlayer
    lateinit var gifview: GifImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bai1, container, false)
        player = MediaPlayer.create(context,R.raw.file1)
        gifview = root.gif
        player.setOnCompletionListener {
            gifview.visibility = View.INVISIBLE
        }

        root.resetBtn.setOnClickListener {
            bai1View.reset()
            root.xEditText.text.clear()
            root.yEditText.text.clear()
        }
        root.addBtn.setOnClickListener {
            val x = root.xEditText.text.toString()
            val y = root.yEditText.text.toString()
            bai1View.addPoint(x.toInt(),y.toInt())
        }
        root.addBtn.setOnLongClickListener {
            if (root.xEditText.text.toString() == 1999.toString())  {
                player.start()
                gifview.visibility = View.VISIBLE

            }
            true
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        gifview.visibility = View.INVISIBLE
        player.stop()
    }
}
