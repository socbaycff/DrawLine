package com.example.drawline

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.abs


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val lineView = findViewById<LineView>(R.id.lineView)
        button.setOnClickListener {
            lineView.reset()
        }
        button2.setOnClickListener {
            lineView.count = 1
        }


    }
}


class LineView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    var xStart: Float? = null
        set(value) {
            field = value
            xTemp = value?.toInt() ?: 0
            Log.i("-----------", value.toString())
        }

    var yStart: Float? = null
        set(value) {
            field = value
            yTemp = value?.toInt() ?: 0
            Log.i("-----------", value.toString())
        }

    var xEnd: Float? = null
        set(value) {
            field = value
            Log.i("-----------", value.toString())
        }

    var yEnd: Float? = null
        set(value) {
            field = value
            Log.i("-----------", value.toString())
        }

    private val paint =
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 20f
            strokeCap = Paint.Cap.ROUND
        }


    fun reset() {
        count = 0
        xEnd = null
        xStart = null
        yStart = null
        yEnd = null
        postInvalidate()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // ve duong
        //   drawLineMidPoint(0, 0, 800, 1500, canvas)
//        if (xStart != null && yStart != null && xEnd != null && yEnd != null) {
//            val x1 = xStart!!.toInt()
//            val y1 = yStart!!.toInt()
//            val x2 = xEnd!!.toInt()
//            val y2 = yEnd!!.toInt()
//            if (yEnd!! > yStart!!) {
//                drawLineMidPointSolid(x1, y1, x2, y2, canvas)
//            } else {
//                drawLineMidPointSolid(x2, y2, x1, y1, canvas)
//            }
//
//        }

        // ve hinh cn
        if (xStart != null && yStart != null && xEnd != null && yEnd != null) {
            var firstX = 0
            var firstY = 0
            var secondX = 0
            var secondY = 0
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

            drawRect(firstX,firstY,secondX,secondY,canvas)
        }


        // ve duong tron
         // drawCircle(400, 600, 1000, canvas)

        // ve duong ellipse
         //   drawEllipse(400f,500f,700f,700f,canvas)
    }
    var count = 0
    var putLength = 100
    var unputLength = 50
    val pattern = ArrayList<Boolean>(putLength + unputLength).apply {
        var i: Int = 0
        for (index in 0..putLength) {
            add(true)
        }
        for (index in 0..unputLength) {
            add(false)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (count == 0) {
                    xStart = event.x
                    yStart = event.y

                }

                if (count == 1) {
                    xEnd = event.x
                    yEnd = event.y
                  //  animateView()
                    postInvalidate()
                }
            }

        }
        return true
    }

    var xTemp = 0
    var yTemp = 0
    fun animateView() {
        val animatorSet = AnimatorSet()
        animatorSet.duration = 250
        animatorSet.interpolator =
            DecelerateInterpolator() // quy dinh toc do cua dong chay value animator

        val xAnimator = ValueAnimator.ofFloat(xStart!!, xEnd!!)
        xAnimator.addUpdateListener {
            xTemp = (it.getAnimatedValue() as Float).toInt()
            invalidate() // keu ve lai
        }

        val yAnimator = ValueAnimator.ofFloat(yStart!!, yEnd!!)
        yAnimator.addUpdateListener {
            yTemp = (it.getAnimatedValue() as Float).toInt()
            invalidate()// keu ve lai
        }

        animatorSet.play(xAnimator).with(yAnimator)
        animatorSet.start()
    }

    fun changeAxis(x: Int, y: Int) {
        val originX = width/2
        val originY = height/2

        // toa do x thi cong them , toa do y thi tru
    }

    fun drawRect(firstX: Int, firstY: Int, secondX: Int, secondY: Int,canvas: Canvas?) {

        drawLineMidPointSolid(firstX,firstY,secondX,firstY,canvas)
        drawLineMidPointSolid(secondX,firstY,secondX,secondY,canvas)
        drawLineMidPointSolid(secondX,secondY,firstX,secondY,canvas)
        drawLineMidPointSolid(firstX,firstY,firstX,secondY,canvas)
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

    fun drawCircle(r: Int, x_centre: Int, y_centre: Int, canvas: Canvas?) {
        var counter = 0

        var x: Int = r
        var y = 0

        // Printing the initial point on the axes after translation
        System.out.print(
            "(" + (x + x_centre)
                .toString() + ", " + (y + y_centre).toString() + ")"
        )

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


    fun drawLineMidPointDash(startX: Int, startY: Int, endX: Int, endY: Int, canvas: Canvas?) {

        val dx = endX - startX
        val dy = endY - startY

        var d = 0
        var x: Float = startX.toFloat()
        var y: Float = startY.toFloat()
        var counter = 0
        canvas?.drawPoint(x, y, paint)
        if (endX < startX) {
            if (abs(dx) > abs(dy)) {
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
            if (abs(dx) > abs(dy)) {
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


    fun drawLineMidPointSolid(startX: Int, startY: Int, endX: Int, endY: Int, canvas: Canvas?) {

        val dx = endX - startX
        val dy = endY - startY

        var d: Int
        var x: Float = startX.toFloat()
        var y: Float = startY.toFloat()

        canvas?.drawPoint(x, y, paint)
        if (endX < startX) {
            if (abs(dx) > abs(dy)) {
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
            if (abs(dx) > abs(dy)) {
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
