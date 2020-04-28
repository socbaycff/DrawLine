package com.example.drawline.ui.bai1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.drawline.R

import kotlinx.android.synthetic.main.fragment_bai1.*
import kotlinx.android.synthetic.main.fragment_bai1.view.*


class Bai1Frag : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bai1, container, false)

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
        
        return root
    }
}
