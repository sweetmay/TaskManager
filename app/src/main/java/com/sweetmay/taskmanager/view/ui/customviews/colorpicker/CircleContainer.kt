package com.sweetmay.taskmanager.view.ui.customviews.colorpicker

import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.annotation.Dimension
import androidx.core.view.children
import com.sweetmay.taskmanager.R

class CircleContainer@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr){

    companion object{
        private const val ANIMATION_DURATION = 150L
        private val displayMetrics = Resources.getSystem().displayMetrics
        @Dimension(unit = Dimension.DP) var defCirclesPaddingDp = 8
        private const val HEIGHT = "height"
        private const val SCALE = "scale"
    }

    val isOpen: Boolean
        get() = measuredHeight > 0

    @Dimension(unit = Dimension.PX) var circlesPadding: Float = defCirclesPaddingDp * displayMetrics.density

    private var desiredHeight = 0

    private val animator by lazy {
        ValueAnimator().apply {
            duration = ANIMATION_DURATION
            addUpdateListener(updateListener)
    }
    }

    private val updateListener by lazy {
        ValueAnimator.AnimatorUpdateListener { animator ->
            layoutParams.apply {
                height = animator.getAnimatedValue(HEIGHT) as Int
            }.let {
                layoutParams = it
            }

            val scaleFactor = animator.getAnimatedValue(SCALE) as Float
            for (i in 0 until childCount) {
                getChildAt(i).apply {
                    scaleX = scaleFactor
                    scaleY = scaleFactor
                    alpha = scaleFactor
                }
            }
        }
    }

    init {
        gravity = Gravity.CENTER
        orientation = HORIZONTAL
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleContainer)

        val defCirclePaddingsPx = defCirclesPaddingDp *displayMetrics.density
        circlesPadding = a.getDimension(
            R.styleable.CircleContainer_circlePaddings, defCirclePaddingsPx)

        a.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        layoutParams.apply {
            desiredHeight = height
            height = 0
        }.let {
            layoutParams = it
        }
    }

    fun open() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, desiredHeight),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 1f)
        )
        animator.start()
    }

    fun close() {
        animator.cancel()
        animator.setValues(
            PropertyValuesHolder.ofInt(HEIGHT, measuredHeight, 0),
            PropertyValuesHolder.ofFloat(SCALE, getChildAt(0).scaleX, 0f)
        )
        animator.start()
    }

    fun addCircles(colors: List<Int>, onColorClickListener: OnColorClickListener){
        colors.forEach(){
            addView(ColorCircle(context).apply {
                fillColorRes = it
                tag = it
                setPadding(circlesPadding.toInt(),
                    circlesPadding.toInt(),
                    circlesPadding.toInt(),
                    circlesPadding.toInt()
                )
                strokeColorRes = R.color.violet
                setOnClickListener { onColorClickListener.onClick(fillColorRes)
                changeSelected(fillColorRes)}
            })
        }
    }

    private fun changeSelected(color: Int) {

        children.forEach {
            if(it.tag == color){
                (it as ColorCircle).changeSelect(true)
            }else {
                (it as ColorCircle).changeSelect(false)
            }
        }
    }
}