package com.example.drawline.ui.drawView

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator

class Bai2View(context:Context, attributeSet: AttributeSet) : View(context,attributeSet) {

    private val paint =
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 20f
            strokeCap = Paint.Cap.ROUND
        }

    private val axisPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 5f
    }

    private var xTemp = 0
    private var yTemp = 0
    private var isStart = false
    private var isFirstPoint = true

    private val putLength = 100
    private val unputLength = 50

    private val dashPattern = ArrayList<Boolean>(putLength + unputLength).apply {
        for (index in 1..putLength) {
            add(true)
        }
        for (index in 1..unputLength) {
            add(false)
        }
    }

    val secondUnputLength = 30
    val secondPutLength = 15
    private val onePointPattern = ArrayList<Boolean>(putLength + 2*secondUnputLength + secondPutLength).apply {

        for (index in 1..putLength) {
            add(true)
        }
        for (index in 1..secondUnputLength) {
            add(false)
        }

        for (index in 1..secondPutLength) {
            add(true)
        }

        for (index in 1..secondUnputLength) {
            add(false)
        }
    }


    private val twoPointPattern = ArrayList<Boolean>(putLength + secondPutLength*2 + secondUnputLength*3).apply {

        for (index in 1..putLength) {
            add(true)
        }
        for (index in 1..secondUnputLength) {
            add(false)
        }

        for (index in 1..secondPutLength) {
            add(true)
        }

        for (index in 1..secondUnputLength) {
            add(false)
        }

        for (index in 1..secondPutLength) {
            add(true)
        }

        for (index in 1..secondUnputLength) {
            add(false)
        }


    }

    private var xStart: Float? = null
        set(value) {
            field = value
            xTemp = value?.toInt() ?: 0
            Log.i("-----------", value.toString())
        }

    private var yStart: Float? = null
        set(value) {
            field = value
            yTemp = value?.toInt() ?: 0
            Log.i("-----------", value.toString())
        }

    private var xEnd: Float? = null
    private var yEnd: Float? = null



    fun reset() {
      //  isStart = false
        xEnd = null
        xStart = null
        yStart = null
        yEnd = null
        startDraw()
        postInvalidate()
    }

    var drawMode = true // true line, false rectangle
    enum class LineMode { DASH, ONE_POINT, TWO_POINT }
     var lineMode = LineMode.DASH

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // ve truc x, y
        canvas?.drawLine(0f,height/2f,width.toFloat(),height/2f,axisPaint)
        canvas?.drawLine(width/2f,0f,width/2f,height.toFloat(),axisPaint)
        if (xStart != null && yStart != null && xEnd != null && yEnd != null) {

            if (drawMode) { // neu mode ve duong thang
                val x1 = xStart!!.toInt()
                val y1 = yStart!!.toInt()

                if (yEnd!! > yStart!!) {
                    drawLineMidPointDash(x1, y1, xTemp, yTemp, canvas)
                } else {
                    drawLineMidPointDash(xTemp, yTemp, x1, y1, canvas)
                }


            } else { // mode ve hcn

                var firstX = 0
                var firstY = 0
                var secondX = 0
                var secondY = 0

                // dieu chinh vi tri cua diem dau tien, cuoi cung thanh vi tri co the ve duoc
                if (xStart!! > xEnd!!) {
                    firstX = xEnd!!.toInt()
                    secondX = xStart!!.toInt()
                } else {
                    firstX = xStart!!.toInt()
                    secondX = xEnd!!.toInt()
                }

                if (yStart!! > yEnd!!) {
                    firstY = yEnd!!.toInt()
                    secondY = yStart!!.toInt()
                } else {
                    secondY = yEnd!!.toInt()
                    firstY = yStart!!.toInt()
                }
                //ve hcn voi toa do da dieu chinh
                drawRect(firstX,firstY,secondX,secondY,canvas)
            }

        }

    }
    fun startDraw() {
        isStart = true
        onPickListenter("Chạm để chọn điểm bắt đầu!!")
    }

    private lateinit var onPickListenter: (message: String)-> Unit
    fun setOnPickListener( listener: (message: String)-> Unit ) {
        onPickListenter = listener
    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        when (action) {
            MotionEvent.ACTION_UP -> {
                if (isStart) {
                    if (isFirstPoint)  {
                        xStart = event.x
                        yStart = event.y
                        isFirstPoint = false
                        onPickListenter("Chạm để chọn điểm kết thúc!!")
                    } else {
                        xEnd = event.x
                        yEnd = event.y
                          animateView()
                       // postInvalidate()
                        isFirstPoint = true
                        isStart = false
                        onPickListenter("Đã vẽ xong, bấm vẽ để vẽ lại")

                    }

                }

            }


        }
        return true
    }

    private  fun drawRect(firstX: Int, firstY: Int, secondX: Int, secondY: Int,canvas: Canvas?) {

        drawLineMidPointDash(firstX,firstY,secondX,firstY,canvas)
        drawLineMidPointDash(secondX,firstY,secondX,secondY,canvas)
        drawLineMidPointDash(secondX,secondY,firstX,secondY,canvas)
        drawLineMidPointDash(firstX,firstY,firstX,secondY,canvas)
    }



    private fun drawLineMidPointDash(startX: Int, startY: Int, endX: Int, endY: Int, canvas: Canvas?) {

        val dx = endX - startX
        val dy = endY - startY

        var d = 0
        var x: Float = startX.toFloat()
        var y: Float = startY.toFloat()
        var counter = 0

        // choose pattern
        val pattern: ArrayList<Boolean>
        when (lineMode) {
            LineMode.DASH -> pattern = dashPattern
            LineMode.ONE_POINT -> pattern = onePointPattern
            LineMode.TWO_POINT -> pattern = twoPointPattern
        }


        canvas?.drawPoint(x, y, paint)
        if (endX < startX) {
            if (Math.abs(dx) > Math.abs(dy)) {
                d = -dy - dx / 2
                while (x > endX) {
                    x--
                    if (d > 0) d = d - dy else {
                        d += -dy - dx
                        y++
                    }

                    if (pattern[counter++ % (pattern.size)]) {
                        canvas?.drawPoint(x, y, paint)
                    }

                }
            } else {
                d = -dx - dy / 2
                while (y < endY) {
                    y++
                    if (d < 0) d = d - dx else {
                        d += -dx - dy
                        x--
                    }
                    if (pattern[counter++ % (pattern.size)]) {
                        canvas?.drawPoint(x, y, paint)
                    }
                }
            }
        } else {
            if (Math.abs(dx) > Math.abs(dy)) {
                d = dy - dx / 2
                while (x < endX) {
                    x++
                    if (d < 0) d = d + dy else {
                        d += dy - dx
                        y++
                    }
                    if (pattern[counter++ % (pattern.size)]) {
                        canvas?.drawPoint(x, y, paint)
                    }
                }
            } else {
                d = dx - dy / 2
                while (y < endY) {
                    y++
                    if (d < 0) d = d + dx else {
                        d += dx - dy
                        x++
                    }
                    if (pattern[counter++ % (pattern.size)]) {
                        canvas?.drawPoint(x, y, paint)
                    }

                }
            }
        }


    }

    fun animateView() {
        val animatorSet = AnimatorSet()
        animatorSet.duration = 250
        animatorSet.interpolator =
            DecelerateInterpolator() // quy dinh toc do cua dong chay value animator

        val xAnimator = ValueAnimator.ofFloat(xStart!!, xEnd!!)
        xAnimator.addUpdateListener {
            xTemp = (it.getAnimatedValue() as Float).toInt()
            postInvalidate() // keu ve lai
        }

        val yAnimator = ValueAnimator.ofFloat(yStart!!, yEnd!!)
        yAnimator.addUpdateListener {
            yTemp = (it.getAnimatedValue() as Float).toInt()
            postInvalidate()// keu ve lai
        }

        animatorSet.play(xAnimator).with(yAnimator)
        animatorSet.start()
    }
    private fun drawLineMidPointSolid(startX: Int, startY: Int, endX: Int, endY: Int, canvas: Canvas?) {

        val dx = endX - startX
        val dy = endY - startY

        var d: Int
        var x: Float = startX.toFloat()
        var y: Float = startY.toFloat()

        canvas?.drawPoint(x, y, paint)
        if (endX < startX) {
            if (Math.abs(dx) > Math.abs(dy)) {
                d = -dy - dx / 2
                while (x > endX) {
                    x--

                    if (d > 0) d = d - dy else {
                        d += -dy - dx
                        y++
                    }
                    canvas?.drawPoint(x, y, paint)

                }
            } else {
                d = -dx - dy / 2
                while (y < endY) {
                    y++

                    if (d < 0) d = d - dx else {
                        d += -dx - dy
                        x--
                    }
                    canvas?.drawPoint(x, y, paint)

                }
            }
        } else {
            if (Math.abs(dx) > Math.abs(dy)) {
                d = dy - dx / 2
                while (x < endX) {
                    x++

                    if (d < 0) d = d + dy else {
                        d += dy - dx
                        y++
                    }
                    canvas?.drawPoint(x, y, paint)

                }
            } else {
                d = dx - dy / 2
                while (y < endY) {
                    y++

                    if (d < 0) d = d + dx else {
                        d += dx - dy
                        x++
                    }
                    canvas?.drawPoint(x, y, paint)

                }
            }
        }



    }
}