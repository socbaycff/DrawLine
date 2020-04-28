package com.example.drawline.ui.drawView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Bai1View(context: Context,attributeSet: AttributeSet): View(context,attributeSet) {

    val pointArray = ArrayList<Pair<Int,Int>>()
    var originX = 0
    var originY = 0

    var doRongDonVi = 100

    val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
    }

    val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
    }

    val pointPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 40f
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        originX = width/2
        originY = height/2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // ve truc x, y
        canvas?.drawLine(0f,height/2f,width.toFloat(),height/2f,axisPaint)
        canvas?.drawLine(width/2f,0f,width/2f,height.toFloat(),axisPaint)
        pointArray.forEach {
            val newPoint = changeAxis(it.first,it.second)
            canvas?.drawPoint(newPoint.x,newPoint.y,pointPaint) // cham diem toa do
            canvas?.drawText("(${it.first}, ${it.second})",newPoint.x - 40,newPoint.y-40,textPaint) // ve text toa do
        }

    }

    fun unitToPixel(x: Int,y: Int): Pair<Int,Int> { // chuyen doi don vi thanh pixel
        val newX = x * doRongDonVi
        val newY = y * doRongDonVi
        val result = Pair<Int,Int>(newX,newY)
        return result
    }

    fun changeAxis(x: Int, y: Int): PointF{ // chuyen doi toa do may tinh sang toa do cua bai
        val oldPoint = unitToPixel(x, y)
        val oldX = oldPoint.first.toFloat()
        val oldY = oldPoint.second.toFloat()

        // toa do x thi cong them , toa do y thi tru
        val newPoint = PointF( oldX+ originX, originY - oldY)
        return newPoint
    }

    fun reset() {
        pointArray.clear()
        postInvalidate()
    }
    fun addPoint(x: Int, y : Int) {
        pointArray.add(Pair(x,y))
        postInvalidate()
    }


}
