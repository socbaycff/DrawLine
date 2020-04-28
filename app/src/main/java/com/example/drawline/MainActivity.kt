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
//        val lineView = findViewById<LineView>(R.id.lineView)
//        button.setOnClickListener {
//            lineView.reset()
//        }
//        button2.setOnClickListener {
//            lineView.count = 1
//        }


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




}
