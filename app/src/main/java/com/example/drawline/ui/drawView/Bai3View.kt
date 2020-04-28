package com.example.drawline.ui.drawView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

class Bai3View(context: Context, attributeSet: AttributeSet): View(context,attributeSet) {

    private  var centerPoint = PointF()
    private  var radius: Int = 100

    private val paint =
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 20f
            strokeCap = Paint.Cap.ROUND
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
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(radius,centerPoint.x.toInt(),centerPoint.y.toInt(),canvas)
        drawListener(centerPoint,radius)
    }


    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val factor = detector.scaleFactor
            val multi = if (factor > 1.0) 10 else -10
            radius = radius+ multi
            postInvalidate()
            return true
        }
    }

    private val mScaleDetector = ScaleGestureDetector(context, scaleListener)
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action

        mScaleDetector.onTouchEvent(event)
        if (!mScaleDetector.isInProgress()) {
            when (action) {
                MotionEvent.ACTION_MOVE -> {

                    centerPoint.x = event.x
                    centerPoint.y = event.y
                    postInvalidate()

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


        // When radius is zero only a single point will be printed
        if (r > 0) {
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
}