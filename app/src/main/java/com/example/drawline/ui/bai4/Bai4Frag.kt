package com.example.drawline.ui.bai4

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.drawline.R
import kotlinx.android.synthetic.main.fragment_bai4.view.*


class Bai4Frag : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bai4, container, false)
        val radiusTv = root.ellipseRadTV
        val modeTv = root.modeTextView
        modeTv.text = "Đang điều chỉnh trục ngang(X)"
        val bai4view = root.bai4View
        root.guideb4.text = "Thao tác zoom 2 ngón để thay đổi kích thước bán kính \n Kéo thả để di chuyển tâm"
        bai4view.setRadiusChangeListener { center, radiusX, radiusY ->
            radiusTv.text = "Tam ellipse: (${center.x} , ${center.y}) \n Ban Kinh X: $radiusX \n" + " Ban Kinh Y: $radiusY"
        }

        root.changeRadScaleBtn.setOnClickListener {
            val changeMode = bai4view.changeMode()
            val text = if (!changeMode) " trục dọc(Y)" else "trục ngang(X)"
            Toast.makeText(context," Đổi trục điều chỉnh thành $text ",Toast.LENGTH_SHORT).show()
            modeTv.text = "Đang điều chỉnh $text"
        }
        return root
    }

}
