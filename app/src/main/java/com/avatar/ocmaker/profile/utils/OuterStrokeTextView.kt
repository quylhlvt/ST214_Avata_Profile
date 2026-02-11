package com.avatar.ocmaker.profile.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Join
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.avatar.ocmaker.profile.R

class OuterStrokeTextView : AppCompatTextView {

    private var outerStrokeWidth = 0f
    private var outerStrokeColor: Int = Color.WHITE
    private var outerStrokeJoin: Join = Join.ROUND
    private var strokeMiter = 5f

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs == null) return

        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.OuterStrokeTextView
        )

        try {
            outerStrokeWidth = a.getDimension(
                R.styleable.OuterStrokeTextView_outerStrokeWidth,
                0f
            )

            outerStrokeColor = a.getColor(
                R.styleable.OuterStrokeTextView_outerStrokeColor,
                Color.WHITE
            )

            outerStrokeJoin = when (a.getInt(
                R.styleable.OuterStrokeTextView_outerStrokeJoinStyle, 2)) {
                0 -> Join.MITER
                1 -> Join.BEVEL
                2 -> Join.ROUND
                else -> Join.ROUND
            }
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (outerStrokeWidth > 0f) {
            val textColor = currentTextColor
            val paint = paint

            // Vẽ stroke bên ngoài
            setTextColor(outerStrokeColor)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = outerStrokeWidth * 2f
            paint.strokeJoin = outerStrokeJoin
            paint.strokeMiter = strokeMiter
            paint.isAntiAlias = true

            super.onDraw(canvas)

            // Vẽ text fill bên trong
            setTextColor(textColor)
            paint.style = Paint.Style.FILL

            super.onDraw(canvas)
        } else {
            // Không có stroke, vẽ bình thường
            super.onDraw(canvas)
        }
    }
}