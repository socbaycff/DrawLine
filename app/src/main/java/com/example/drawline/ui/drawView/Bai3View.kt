package com.example.drawline.ui.drawView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import java.lang.Math.pow

class Bai3View(context: Context, attributeSet: AttributeSet): View(context,attributeSet) {

    private  var centerPoint = PointF()
    private var newCenterPoint = PointF()
    private  var radius: Int = 100

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

    var putLength = 25
    var unputLength = 25
    val pattern = ArrayList<Boolean>(putLength + unputLength).apply {
        var i: Int = 0
        for (index in 0..putLength) {
            add(true)
        }
        for (index in 0..unputLength) {
            add(false)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        centerPoint.x = width/2f
        centerPoint.y = height/2f
        originX = width/2
        originY = height/2
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // ve truc x, y
        canvas?.drawLine(0f,height/2f,width.toFloat(),height/2f,axisPaint)
        canvas?.drawLine(width/2f,0f,width/2f,height.toFloat(),axisPaint)
        drawCircle(radius,centerPoint.x.toInt(),centerPoint.y.toInt(),canvas)
        changeAxis()
        drawListener(newCenterPoint,radius)
    }


    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val factor = detector.scaleFactor
            val multi = if (factor > 1.0) 10 else -10
            if (radius + multi > 0) {
                radius = radius+ multi
            }

            postInvalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action

        val onTouchEvent = mScaleDetector.onTouchEvent(event)
        Log.i("pppppppppp",onTouchEvent.toString())
        if (!mScaleDetector.isInProgress()) {
            when (action) {

                MotionEvent.ACTION_MOVE -> {
                    val x = event.x
                    val y = event.y
                    if (pow(x.toDouble() - centerPoint.x, 2.0) + pow(y.toDouble() - centerPoint.y,2.0) <= pow(radius.toDouble(),2.0)) {
                        centerPoint.x = x
                        centerPoint.y =y
                        postInvalidate()
                    }


                }

            }
        }


        return true
    }

    private lateinit  var drawListener: (PointF, Int) -> Unit
    fun setDrawListener(listener: (center: PointF,radius: Int) -> Unit ) {
        drawListener = listener
    }

    fun drawCircle(r: Int, x_centre: Int, y_centre: Int, canvas: Canvas?) {
        var counter = 0

        var x: Int = r
        var y = 0


        canvas?.drawPoint(x + x_centre.toFloat(), y + y_centre.toFloat(), paint)
        if (radius > 0)
        {
            canvas?.drawPoint(x + x_centre.toFloat(), -y + y_centre.toFloat(), paint)
            canvas?.drawPoint(y + x_centre.toFloat(), x + y_centre.toFloat(), paint)
            canvas?.drawPoint(-y + x_centre.toFloat(), x + y_centre.toFloat(), paint)
        }

        // Initialising the value of P
        var P: Int = 1 - r
        while (x > y) {
            y++
            // Mid-point is inside or on the perimeter
            P = if (P <= 0) P + 2 * y + 1 else {
                x--
                P + 2 * y - 2 * x + 1
            }

            // All the perimeter points have already been printed
            if (x < y) break

            // Printing the generated point and its reflection in the other octants after translation
            if (pattern[counter++ % (pattern.size)]) {
                canvas?.drawPoint(x + x_centre.toFloat(), y + y_centre.toFloat(), paint)
                canvas?.drawPoint(-x + x_centre.toFloat(), y + y_centre.toFloat(), paint)
                canvas?.drawPoint(x + x_centre.toFloat(), -y + y_centre.toFloat(), paint)
                canvas?.drawPoint(-x + x_centre.toFloat(), -y + y_centre.toFloat(), paint)

                // If the generated point is on the line x = y then the perimeter points have already been printed
                if (x != y) {
                    canvas?.drawPoint(y + x_centre.toFloat(), x + y_centre.toFloat(), paint)
                    canvas?.drawPoint(-y + x_centre.toFloat(), x + y_centre.toFloat(), paint)
                    canvas?.drawPoint(y + x_centre.toFloat(), -x + y_centre.toFloat(), paint)
                    canvas?.drawPoint(-y + x_centre.toFloat(), -x + y_centre.toFloat(), paint)
                }
            }

        }
    }

    var originX = 0
    var originY = 0
    fun changeAxis() {
        newCenterPoint.x = (centerPoint.x-width) + originX
        newCenterPoint.y = originY - centerPoint.y
    }
}