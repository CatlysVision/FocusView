package com.example.focusview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import kotlin.math.abs


class FocusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var imageView: ImageView
    private var mViewX = 0f
    private var mViewY = 0f
    private var downX = 0f
    private var downY = 0f

    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    private var offsetX = 0f
    private var offsetY = 0f

    private var radius = 0

    init {
        mPaint.color = Color.parseColor("#ff813c")
        mPaint.strokeWidth = dip2px(context, 3f)
        mPaint.style = Paint.Style.STROKE

        radius = dip2px(context, 60f).toInt()
        imageView = ImageView(context)
        imageView.setImageResource(R.drawable.circle)
        val layoutParams = LayoutParams(radius, radius)
        imageView.layoutParams = layoutParams
        imageView.setOnTouchListener(this)
        addView(imageView)
    }

    fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + 0.5f
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.apply {
            when (this.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = this.rawX
                    downY = this.rawY

                    originalOffsetX = offsetX
                    originalOffsetY = offsetY
                }
                MotionEvent.ACTION_MOVE -> {
                    val moveX = this.rawX - downX
                    val moveY = this.rawY - downY

                    offsetX = originalOffsetX + moveX
                    offsetY = originalOffsetY + moveY

                    layoutImageView()
                }
                MotionEvent.ACTION_UP -> {

                }
            }
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetX = width / 2f
        offsetY = height / 2f
        layoutImageView()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        layoutImageView()
    }

    private fun layoutImageView() {
        mViewX = offsetX - radius / 2
        mViewY = offsetY - radius / 2

        adjustOffset()

        imageView.x = mViewX
        imageView.y = mViewY
    }

    private fun adjustOffset() {
        mViewX = mViewX.coerceAtMost((width - radius).toFloat())
        mViewX = mViewX.coerceAtLeast(0f)
        mViewY = mViewY.coerceAtMost((height - radius).toFloat())
        mViewY = mViewY.coerceAtLeast(0f)
    }

}