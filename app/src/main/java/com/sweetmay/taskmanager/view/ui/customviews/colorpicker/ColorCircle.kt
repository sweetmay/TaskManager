package com.sweetmay.taskmanager.view.ui.customviews.colorpicker

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Dimension.DP
import androidx.annotation.Dimension.PX
import androidx.core.content.ContextCompat
import com.sweetmay.taskmanager.R

class ColorCircle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    companion object{
        @Dimension(unit = DP) private const val defRadiusDp = 16
        @Dimension(unit = DP) private const val defStrokeWidthDp = 3
    }

    private val displayMetrics = Resources.getSystem().displayMetrics

    private var isSelectedColor = false

    fun changeSelect(boolean: Boolean){
        isSelectedColor = boolean
        invalidate()
    }

    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private var center: Pair<Float, Float> = 0f to 0f

    @Dimension(unit = PX) var radius: Float = defRadiusDp*displayMetrics.density
    @Dimension(unit = PX) var strokeWidth: Float = defStrokeWidthDp*displayMetrics.density

    @ColorRes var fillColorRes: Int = R.color.white
        set(value) {
            field = value
            fillPaint.color = ContextCompat.getColor(context, value)
        }

    @ColorRes var strokeColorRes: Int = R.color.color_text_secondary
        set(value) {
            field = value
            strokePaint.color = ContextCompat.getColor(context, value)
        }

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorCircle)

        val defRadiusPx = defRadiusDp*displayMetrics.density
        radius = a.getDimension(R.styleable.ColorCircle_circleRadius, defRadiusPx)
        fillColorRes = a.getResourceId(
            R.styleable.ColorCircle_fillColor, R.color.white)

        val defStrokeWidthPx = defStrokeWidthDp*displayMetrics.density
        strokeWidth = a.getDimension(
            R.styleable.ColorCircle_strokeWidth, defStrokeWidthPx)

        strokeColorRes = a.getResourceId(
            R.styleable.ColorCircle_strokeColor, R.color.color_text_secondary)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = (radius * 2 + paddingTop + paddingBottom + strokeWidth).toInt()
        val width = (radius * 2 + paddingStart + paddingEnd + strokeWidth).toInt()

        strokePaint.strokeWidth = strokeWidth
        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        center = measuredWidth/2f to measuredHeight/2f
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(isSelectedColor){
            canvas.drawCircle(center.first, center.second, radius, strokePaint)
        }
        canvas.drawCircle(center.first, center.second, radius - strokeWidth/2, fillPaint)
    }
}