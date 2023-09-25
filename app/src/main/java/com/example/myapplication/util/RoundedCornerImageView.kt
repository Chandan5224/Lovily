package com.example.myapplication.util


import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class RoundedCornerImageView : AppCompatImageView {
    private var path: Path? = null
    private var rect: RectF? = null
    private var cornerRadii: FloatArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        path = Path()
        rect = RectF()
    }

    /**
     * Set custom corner radii for all four corners.
     * @param topLeftRadius Top-left corner radius in pixels
     * @param topRightRadius Top-right corner radius in pixels
     * @param bottomRightRadius Bottom-right corner radius in pixels
     * @param bottomLeftRadius Bottom-left corner radius in pixels
     */
    fun setCornerRadii(
        topLeftRadiusX: Float,
        topLeftRadiusY: Float,
        topRightRadiusX: Float,
        topRightRadiusY: Float,
        bottomRightRadiusX: Float,
        bottomRightRadiusY: Float,
        bottomLeftRadiusX: Float,
        bottomLeftRadiusY: Float
    ) {
        cornerRadii = floatArrayOf(
            topLeftRadiusX, topLeftRadiusY,
            topRightRadiusX, topRightRadiusY,
            bottomRightRadiusX, bottomRightRadiusY,
            bottomLeftRadiusX, bottomLeftRadiusY
        )
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        rect!![0f, 0f, width.toFloat()] = height.toFloat()
        path!!.reset()
        path!!.addRoundRect(rect!!, cornerRadii, Path.Direction.CW)
        canvas.clipPath(path!!)
        super.onDraw(canvas)
    }
}
