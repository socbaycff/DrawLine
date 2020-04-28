package com.example.drawline.ui.bai2

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.drawline.R
import com.example.drawline.ui.drawView.Bai2View
import kotlinx.android.synthetic.main.fragment_bai2.view.*

class Bai2Frag : Fragment() {

    lateinit var view: Bai2View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_bai2, container, false)
        view = root.bai2View
        val console = root.bai2_console_tv
        root.startDraw.setOnClickListener {
            view.reset()
            view.drawMode = true
        }


        root.rectDraw.setOnClickListener {
            view.reset()
            view.drawMode = false
        }

        view.setOnPickListener {
            console.text = it
        }

        registerForContextMenu(root.textView2)
        return root
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.dash -> view.lineMode = Bai2View.LineMode.DASH
            R.id.onePoint -> view.lineMode = Bai2View.LineMode.ONE_POINT
            R.id.twoPoint -> view.lineMode = Bai2View.LineMode.TWO_POINT
            else -> return super.onContextItemSelected(item)
        }
        return true
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {

        menu.setHeaderTitle("Chọn loại nét vẽ: ");
        activity?.menuInflater?.inflate(R.menu.menu_line_mode,menu)
    }
}
