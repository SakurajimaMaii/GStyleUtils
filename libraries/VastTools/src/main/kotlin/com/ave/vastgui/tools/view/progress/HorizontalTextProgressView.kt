/*
 * Copyright 2022 VastGui guihy2019@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ave.vastgui.tools.view.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import java.text.DecimalFormat

// Author: Vast Gui
// Email: guihy2019@gmail.com
// Date: 2023/4/4
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/progress/horizontal-progress-view/

/**
 * HorizontalTextProgressView
 *
 * @property mProgressHeight The height of the progress. The height should
 *     not be less than 40% of the maximum height.The maximum height is the
 *     maximum height of the widget minus twice the mTextBoxStrokeWidth.
 * @property mTextMargin The stroke width of the text.
 * @since 0.2.0
 */
class HorizontalTextProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.Default_HorizontalTextProgressView_Style,
    defStyleRes: Int = R.style.BaseHorizontalTextProgressView
) : ProgressView(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultProgressHeight
        get() = resources.getDimension(R.dimen.default_horizontal_text_progress_height)
    private val mDefaultTextMargin
        get() = resources.getDimension(R.dimen.default_horizontal_text_progress_text_margin)

    private var mWidth = 0
    private var mHeight = 0

    private var mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private var mBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private var mBoxPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val mBoxRectF = RectF()

    var mProgressHeight = mDefaultProgressHeight
        private set
    var mTextMargin = mDefaultTextMargin
        private set

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            mWidth = MeasureSpec.getSize(widthMeasureSpec)
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            mHeight = MeasureSpec.getSize(heightMeasureSpec)
        }
        // Ensure the display of the progress bar.
        val maxHeight = mHeight - 2 * mTextMargin
        if (mProgressHeight >= maxHeight || mProgressHeight <= 0.4 * maxHeight) {
            mProgressHeight = (maxHeight * 0.4).toFloat()
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val progressWidth = (mCurrentProgress / mMaximumProgress * mWidth)

        val progressTop = ((mHeight - mProgressHeight) / 2).toInt()
        val backgroundTop = ((mHeight - mProgressHeight) / 2).toInt()
        drawProgress(
            canvas,
            backgroundTop,
            mWidth,
            mHeight - backgroundTop,
            mBackgroundPaint
        )
        drawProgress(
            canvas,
            progressTop,
            progressWidth.toInt(),
            mHeight - progressTop,
            mProgressPaint
        )

        drawBox(canvas)
    }

    /**
     * Set [mCurrentProgress] and [mText].
     *
     * @since 0.2.0
     */
    override fun setCurrentProgress(currentProgress: Float) {
        super.setCurrentProgress(currentProgress)
        mText = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)
    }

    /**
     * You should call [setCurrentProgress] in order to set [mText].
     *
     * @since 0.2.0
     */
    override fun setText(text: String) {
        nothing_to_do()
    }

    /**
     * Set text size for [mTextSize] and [mTextPaint].
     *
     * @since 0.5.3
     */
    override fun setTextSize(@FloatRange(from = 0.0) size: Float) {
        super.setTextSize(size)
        mTextPaint.textSize = size
    }

    /**
     * Set color-int for [mTextColor] and [mTextPaint].
     *
     * @since 0.5.3
     */
    override fun setTextColor(@ColorInt color: Int) {
        super.setTextColor(color)
        mTextPaint.color = color
    }

    /**
     * Set color-int for [mProgressColor] and [mProgressPaint].
     *
     * @since 0.5.3
     */
    override fun setProgressColor(color: Int) {
        super.setProgressColor(color)
        mBoxPaint.color = mProgressColor
        mProgressPaint.color = mProgressColor
    }

    /**
     * Set color-int for [mProgressBackgroundColor] and [mBackgroundPaint].
     *
     * @since 0.5.3
     */
    override fun setProgressBackgroundColor(color: Int) {
        super.setProgressBackgroundColor(color)
        mBackgroundPaint.color = mProgressBackgroundColor
    }

    /**
     * Set progress height.
     *
     * @since 0.5.3
     */
    fun setProgressHeight(@FloatRange(from = 0.0) height: Float) {
        mProgressHeight = height
    }

    /**
     * Set text box stroke width.
     *
     * @since 0.5.3
     */
    fun setTextMargin(@FloatRange(from = 0.0) margin: Float) {
        mTextMargin = margin
    }

    /**
     * Draw text box.
     *
     * @since 0.5.3
     */
    private fun drawBox(canvas: Canvas) {
        val progressWidth = (mCurrentProgress / mMaximumProgress * mWidth)
        // The width of text box.
        val boxWidth = (mTextMargin * 2 + mTextPaint.measureText("100.00%")).toInt()
        // The maximum value in order to ensure that the right side of
        // the TextBox will not cross the border.
        // The minimum value in order to ensure that the left side of
        // the TextBox will not cross the border.
        val boxStart = progressWidth.coerceIn(boxWidth / 2f, mWidth - boxWidth / 2f).toInt()
        val boxHeight = (mProgressHeight + 2 * mTextMargin).toInt()
        mBoxRectF.set(
            (boxStart - boxWidth / 2).toFloat(),
            ((mHeight - boxHeight) / 2).toFloat(),
            (boxStart + boxWidth / 2).toFloat(),
            ((mHeight + boxHeight) / 2).toFloat()
        )
        canvas.drawRoundRect(
            mBoxRectF,
            (boxHeight / 2).toFloat(),
            (boxHeight / 2).toFloat(),
            mBoxPaint
        )
        // Draw text of box.
        canvas.drawText(
            mText,
            (boxStart - mTextPaint.measureText(mText) / 2),
            mHeight / 2f + getTextBaseline(),
            mTextPaint
        )
    }

    /**
     * Draw progress.
     *
     * @param top The top position of the progress.
     * @param right The right position of the progress.
     * @param bottom The left position of the progress.
     * @since 0.2.0
     */
    private fun drawProgress(
        canvas: Canvas,
        top: Int,
        right: Int,
        bottom: Int,
        paint: Paint
    ) {
        // Calculate circle.
        val circleRadius = (bottom - top) / 2f
        val circleLeftX = (bottom - top) / 2f
        val circleRightX = mWidth - circleRadius
        val circleY = top + circleRadius

        canvas.drawCircle(circleLeftX, circleY, circleRadius, paint)

        if (right >= circleLeftX) {
            canvas.save()
            var currentRight = right
            if (right > circleRightX) {
                currentRight = circleRightX.toInt()
            }
            canvas.drawRect(
                Rect(circleLeftX.toInt(), top, currentRight, bottom),
                paint
            )
            canvas.restore()
        }

        if (right >= circleRightX) {
            canvas.save()
            canvas.clipRect(
                Rect(circleRightX.toInt(), top, right, bottom)
            )
            canvas.drawCircle(circleRightX, circleY, circleRadius, paint)
            canvas.restore()
        }
    }

    /**
     * Get text baseline.
     *
     * @since 0.5.3
     */
    private fun getTextBaseline(): Float {
        val fontMetrics = mTextPaint.fontMetrics
        val textHeight = fontMetrics.bottom - fontMetrics.top
        return textHeight / 2 - fontMetrics.bottom
    }

    init {
        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.HorizontalTextProgressView,
            defStyleAttr,
            defStyleRes
        )
        mMaximumProgress =
            typedArray.getFloat(
                R.styleable.HorizontalTextProgressView_progress_maximum_value,
                mDefaultMaximumProgress
            )
        mCurrentProgress =
            typedArray.getFloat(
                R.styleable.HorizontalTextProgressView_progress_current_value,
                mDefaultCurrentProgress
            )
        mText = DecimalFormat("0.00%").format(mCurrentProgress / mMaximumProgress)
        val textColor = typedArray.getColor(
            R.styleable.HorizontalTextProgressView_progress_text_color,
            context.getColor(R.color.md_theme_onPrimary)
        )
        setTextColor(textColor)
        val textSize = typedArray.getDimension(
            R.styleable.HorizontalTextProgressView_progress_text_size,
            mDefaultTexSize
        )
        setTextSize(textSize)
        val progressColor = typedArray.getColor(
            R.styleable.HorizontalTextProgressView_progress_color,
            context.getColor(R.color.md_theme_primary)
        )
        setProgressColor(progressColor)
        val progressBackgroundColor = typedArray.getColor(
            R.styleable.HorizontalTextProgressView_progress_background_color,
            context.getColor(R.color.md_theme_primaryContainer)
        )
        setProgressBackgroundColor(progressBackgroundColor)
        mProgressHeight = typedArray.getDimension(
            R.styleable.HorizontalTextProgressView_horizontal_text_progress_height,
            mDefaultProgressHeight
        )
        mTextMargin = typedArray.getDimension(
            R.styleable.HorizontalTextProgressView_horizontal_text_progress_text_margin,
            mDefaultTextMargin
        )
        typedArray.recycle()
    }

}