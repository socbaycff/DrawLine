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
import java.lang.Math.pow

class Bai4View(context: Context, attributeSet: AttributeSet): View(context,attributeSet) {

    private  var centerPoint = PointF()
    private  var radiusX: Int = 100
    private  var radiusy: Int = 70

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
        drawEllipse(radiusX.toFloat(),radiusy.toFloat(),centerPoint.x,centerPoint.y,canvas)
        changeAxis()
        onRadiusChangeListener(newCenterPoint,radiusX,radiusy)
    }

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

    private var scaleMode = true // true: X mode, false Y mode
    fun changeMode(): Boolean {
        scaleMode = !scaleMode
        return scaleMode
    }

    lateinit private var onRadiusChangeListener: (PointF,Int,Int) -> Unit
    fun setRadiusChangeListener( listener:(center: PointF,radiusX: Int,radiusY: Int) -> Unit ) {
        onRadiusChangeListener = listener
    }

    private val scaleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val delta = if (detector.scaleFactor > 1.0) 10 else -10

            if (scaleMode) {
                radiusX += delta
            } else {
                radiusy += delta
            }

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
                    val x = event.x
                    val y = event.y
                    val vt = pow((x-centerPoint.x).toDouble(),2.0)/ pow(radiusX.toDouble(),2.0)
                    val vp = pow((y-centerPoint.y).toDouble(),2.0)/ pow(radiusy.toDouble(),2.0)



                    if (vt + vp <= 1) {

                        centerPoint.x = x
                        centerPoint.y = y
                        postInvalidate()

                    }


                }

            }
        }


        return true
    }

    fun drawEllipse(radius_x: Float, radius_y: Float, center_x: Float, center_y: Float, canvas: Canvas?) {
        var counter = 0
        var d1: Float
        var d2: Float
        var x: Float
        var y: Float
        x = 0f
        y = radius_y

        // Initial decision parameter of region 1
        d1 = radius_y * radius_y - radius_x * radius_x * radius_y + 0.25f * radius_x * radius_x
        var dx = 2 * radius_y * radius_y * x
        var dy = 2 * radius_x * radius_x * y

        // For region 1
        while (dx < dy) { // Print points based on 4-way symmetry
            canvas?.drawPoint(x + center_x, y + center_y, paint)
            canvas?.drawPoint(-x + center_x, y + center_y, paint)

            if (pattern[counter++ %(pattern.size)]) {
                canvas?.drawPoint(x + center_x, -y + center_y, paint)
                canvas?.drawPoint(-x + center_x, -y + center_y, paint)
            }


            // Checking and updating value of decision parameter based on algorithm
            if (d1 < 0) {
                x++
                dx = dx + 2 * radius_y * radius_y
                d1 = d1 + dx + radius_y * radius_y
            } else {
                x++
                y--
                dx = dx + 2 * radius_y * radius_y
                dy = dy - 2 * radius_x * radius_x
                d1 = d1 + dx - dy + radius_y * radius_y
            }
        }

        // Decision parameter of region 2
        d2 = (radius_y * radius_y * ((x + 0.5f) * (x + 0.5f))
                + radius_x * radius_x * ((y - 1) * (y - 1))
                - radius_x * radius_x * radius_y * radius_y)

        // Plotting points of region 2
        while (y >= 0) { // printing points based on 4-way symmetry

            canvas?.drawPoint(x + center_x, y + center_y, paint)
            canvas?.drawPoint(-x + center_x, y + center_y, paint)

            if (pattern[counter++ %(pattern.size)]) {
                canvas?.drawPoint(x + center_x, -y + center_y, paint)
                canvas?.drawPoint(-x + center_x, -y + center_y, paint)
            }

            // Checking and updating parameter value based on algorithm
            if (d2 > 0) {
                y--
                dy = dy - 2 * radius_x * radius_x
                d2 = d2 + radius_x * radius_x - dy
            } else {
                y--
                x++
                dx = dx + 2 * radius_y * radius_y
                dy = dy - 2 * radius_x * radius_x
                d2 = d2 + dx - dy + radius_x * radius_x
            }
        }
    }

    var originX = 0
    var originY = 0
    private var newCenterPoint = PointF()
    fun changeAxis() {
        newCenterPoint.x = (centerPoint.x-width) + originX
        newCenterPoint.y = originY - centerPoint.y
    }
}