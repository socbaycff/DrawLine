package com.example.drawline.ui.bai3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drawline.R
import kotlinx.android.synthetic.main.fragment_bai3.view.*

class Bai3Frag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_bai3, container, false)
        val tamTv = root.modeTextView
        val banKinhTV = root.ellipseRadTV
        val bai3view = root.bai3View
        root.guideb3.text = "Thao tác zoom 2 ngón để thay đổi kích thước bán kính \n Kéo thả để di chuyển tâm"
        bai3view.setDrawListener { center, radius ->
            tamTv.text = "Toạ độ tâm (${center.x.toInt()}, ${center.y.toInt()})"
            banKinhTV.text = "Độ dài bán kính: $radius"
        }
        return root
    }
}
