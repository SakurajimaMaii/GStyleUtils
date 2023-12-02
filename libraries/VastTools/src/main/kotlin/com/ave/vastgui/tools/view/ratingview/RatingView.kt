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

package com.ave.vastgui.tools.view.ratingview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.ave.vastgui.core.extension.nothing_to_do
import com.ave.vastgui.tools.R
import com.ave.vastgui.tools.graphics.BmpUtils
import kotlin.math.round

// Author: Vast Gui
// Email: sakurajimamai2020@qq.com
// Date: 2021/7/28
// Documentation: https://ave.entropy2020.cn/documents/VastTools/core-topics/ui/rating/rating-view/

/**
 * RatingView.
 *
 * @property mOriginalSelectedBitmap The original bitmap of the selected star.
 * @property mOriginalUnselectedBitmap The original bitmap of the unselected star.
 * @property mStarSelectedBitmap The bitmap of the selected star with required size.
 * @property mStarUnselectedBitmap The bitmap of the unselected star with required size.
 * @property mStarIntervalWidth Star interval width(in pixels).
 * @property mStarBitmapWidth Star Bitmap width(in pixels).
 * @property mStarBitmapHeight Star Bitmap height(in pixels).
 * @property mStarCountNumber Max number of stars.
 * @property mStarRating The progress of the currently selected stars.
 * @property mStarSolidNumber The number of currently selected stars.
 * @property mStarSelectMethod The star selection method.
 * @property mStarOrientation The star orientation.
 */
class RatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = R.attr.Default_RatingView_Style,
    defStyleRes: Int = R.style.BaseRatingView
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val mDefaultStarIntervalWidth
        get() = context.resources.getDimension(R.dimen.default_star_interval_width)
    private val mDefaultStarBitmapWidth
        get() = context.resources.getDimension(R.dimen.default_star_width)
    private val mDefaultStarBitmapHeight
        get() = context.resources.getDimension(R.dimen.default_star_height)
    private val mDefaultSelectMethod
        get() = context.resources.getInteger(R.integer.default_rating_select_method)
    private val mDefaultOrientation
        get() = context.resources.getInteger(R.integer.default_rating_star_orientation)
    private val mDefaultStarCount
        get() = context.resources.getInteger(R.integer.default_rating_star_count)
    private val mDefaultRating
        get() = context.resources.getInteger(R.integer.default_rating_star_rating).toFloat()

    private val mExtraSrc: Rect = Rect()
    private val mExtraDst: Rect = Rect()
    private val mPaint: Paint = Paint()
    private var mStarSelectedBitmap: Bitmap
    private var mStarUnselectedBitmap: Bitmap
    private var mStarSolidNumber = 0

    var mOriginalSelectedBitmap: Bitmap
        private set

    var mOriginalUnselectedBitmap: Bitmap
        private set

    var mStarIntervalWidth: Float
        private set

    var mStarBitmapWidth: Float
        private set

    var mStarBitmapHeight: Float
        private set

    var mStarCountNumber: Int
        private set

    var mStarRating: Float
        private set

    var mStarSelectMethod: StarSelectMethod
        private set

    var mStarOrientation: StarOrientation
        private set

    override fun onDraw(canvas: Canvas) {
        // Draw selected star.
        var solidStartPoint = paddingStart.toFloat()
        // Unselected Star Bitmap start point.
        var hollowStartPoint: Float
        // Unselected Star number.
        val hollowStarNum = mStarCountNumber - mStarSolidNumber
        when (mStarOrientation) {
            StarOrientation.UNSPECIFIED -> return

            StarOrientation.HORIZONTAL -> {
                for (i in 1..mStarSolidNumber) {
                    canvas.drawBitmap(
                        mStarSelectedBitmap,
                        solidStartPoint,
                        paddingTop.toFloat(),
                        mPaint
                    )
                    solidStartPoint += mStarIntervalWidth + mStarBitmapWidth
                }
                hollowStartPoint = solidStartPoint
                for (j in 1..hollowStarNum) {
                    canvas.drawBitmap(
                        mStarUnselectedBitmap,
                        hollowStartPoint,
                        paddingTop.toFloat(),
                        mPaint
                    )
                    hollowStartPoint += mStarIntervalWidth + mStarBitmapWidth
                }
                canvas.drawBitmap(mStarSelectedBitmap, mExtraSrc, mExtraDst, mPaint)
            }

            StarOrientation.VERTICAL -> {
                for (i in 1..mStarSolidNumber) {
                    canvas.drawBitmap(
                        mStarSelectedBitmap,
                        paddingStart.toFloat(),
                        solidStartPoint,
                        mPaint
                    )
                    solidStartPoint += mStarIntervalWidth + mStarBitmapHeight
                }
                hollowStartPoint = solidStartPoint
                for (j in 1..hollowStarNum) {
                    canvas.drawBitmap(
                        mStarUnselectedBitmap,
                        paddingStart.toFloat(),
                        hollowStartPoint,
                        mPaint
                    )
                    hollowStartPoint += mStarIntervalWidth + mStarBitmapHeight
                }
                canvas.drawBitmap(mStarSelectedBitmap, mExtraSrc, mExtraDst, mPaint)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        when (mStarOrientation) {
            StarOrientation.HORIZONTAL -> {
                val requiredWidth = mStarCountNumber * mStarBitmapWidth +
                        (mStarCountNumber - 1) * mStarIntervalWidth +
                        paddingStart + paddingEnd
                val width = resolveSize(requiredWidth.toInt(), widthMeasureSpec)
                val requiredHeight = mStarBitmapHeight + paddingTop + paddingBottom
                val height = resolveSize(requiredHeight.toInt(), heightMeasureSpec)
                setMeasuredDimension(width, height)
            }

            StarOrientation.VERTICAL -> {
                val requiredWidth = mStarBitmapWidth + paddingStart + paddingEnd
                val width = resolveSize(requiredWidth.toInt(), widthMeasureSpec)
                val requiredHeight = mStarCountNumber * mStarBitmapHeight +
                        (mStarCountNumber - 1) * mStarIntervalWidth +
                        paddingTop + paddingBottom
                val height = resolveSize(requiredHeight.toInt(), heightMeasureSpec)
                setMeasuredDimension(width, height)
            }

            StarOrientation.UNSPECIFIED -> setMeasuredDimension(0, 0)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (mStarSelectMethod) {
            StarSelectMethod.SLIDING -> {
                if (mStarOrientation == StarOrientation.HORIZONTAL) {
                    val newStarRating = event.x.coerceIn(0f, measuredWidth.toFloat()) /
                            (mStarBitmapWidth + mStarIntervalWidth)
                    setStarRating(newStarRating)
                } else if (mStarOrientation == StarOrientation.VERTICAL) {
                    val newStarRating = event.y.coerceIn(0f, measuredHeight.toFloat()) /
                            (mStarBitmapHeight + mStarIntervalWidth)
                    setStarRating(newStarRating)
                }
                performClick()
                return true
            }

            StarSelectMethod.CLICK -> {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (mStarOrientation == StarOrientation.HORIZONTAL) {
                        val newStarRating = round(
                            event.x.coerceIn(0f, measuredWidth.toFloat()) /
                                    (mStarBitmapWidth + mStarIntervalWidth)
                        )
                        setStarRating(newStarRating)
                    } else if (mStarOrientation == StarOrientation.VERTICAL) {
                        val newStarRating = round(
                            event.y.coerceIn(0f, measuredHeight.toFloat()) /
                                    (mStarBitmapHeight + mStarIntervalWidth)
                        )
                        setStarRating(newStarRating)
                    }
                }
                performClick()
                return true
            }

            StarSelectMethod.UNABLE -> {
                performClick()
                return super.onTouchEvent(event)
            }
        }
    }

    /**
     * Set star rating by [starRating]. If [mStarRating] is greater than
     * [mStarCountNumber], it will be set to [mStarCountNumber].
     */
    fun setStarRating(@FloatRange(from = 0.0) starRating: Float) {
        mStarRating = starRating.coerceAtMost(mStarCountNumber.toFloat())
        mStarSolidNumber = starRating.toInt()
        val extraSolidLength =
            ((starRating - mStarSolidNumber) * mStarBitmapWidth).toInt()
        var extraSolidStarPoint = 0
        when (mStarOrientation) {
            StarOrientation.HORIZONTAL -> {
                extraSolidStarPoint +=
                    (mStarSolidNumber * (mStarIntervalWidth + mStarBitmapWidth)).toInt()
                mExtraSrc.set(0, 0, extraSolidLength, mStarBitmapHeight.toInt())
                mExtraDst.set(
                    paddingStart + extraSolidStarPoint,
                    paddingTop,
                    paddingStart + extraSolidStarPoint + extraSolidLength,
                    paddingTop + mStarBitmapHeight.toInt()
                )
            }

            StarOrientation.VERTICAL -> {
                extraSolidStarPoint +=
                    (mStarSolidNumber * (mStarIntervalWidth + mStarBitmapHeight)).toInt()
                mExtraSrc.set(0, 0, mStarUnselectedBitmap.width, extraSolidLength)
                mExtraDst.set(
                    paddingStart,
                    paddingTop + extraSolidStarPoint,
                    paddingStart + mStarBitmapWidth.toInt(),
                    paddingTop + extraSolidStarPoint + extraSolidLength
                )
            }

            StarOrientation.UNSPECIFIED -> nothing_to_do()
        }
        invalidate()
    }

    /**
     * Set Star Select Method
     *
     * @param starSelectMethod Int
     */
    fun setStarSelectMethod(starSelectMethod: StarSelectMethod) {
        this.mStarSelectMethod = starSelectMethod
    }

    /**
     * Set bitmap size of [mStarSelectedBitmap] and [mStarUnselectedBitmap].
     *
     * @since 0.5.3
     */
    fun setStarBitmapSize(
        @FloatRange(from = 0.0) starWidth: Float,
        @FloatRange(from = 0.0) starHeight: Float
    ) {
        this.mStarBitmapWidth = starWidth
        this.mStarBitmapHeight = starHeight
        mStarSelectedBitmap =
            BmpUtils.scaleBitmap(
                mOriginalSelectedBitmap,
                starWidth.toInt(),
                starHeight.toInt()
            )
        mStarUnselectedBitmap =
            BmpUtils.scaleBitmap(
                mOriginalUnselectedBitmap,
                starWidth.toInt(),
                starHeight.toInt()
            )
    }

    /**
     * Set star bitmap interval.
     *
     * @param starSpaceWidth Int
     */
    fun setStarIntervalWidth(@FloatRange(from = 0.0) starSpaceWidth: Float) {
        this.mStarIntervalWidth = starSpaceWidth
    }

    /** Set the number of star. */
    fun setStarCountNumber(@IntRange(from = 0) starCountNumber: Int) {
        this.mStarCountNumber = starCountNumber
    }

    /** Set the bitmap of the be selected star. */
    fun setStarSelectedBitmap(bitmap: Bitmap) {
        mOriginalSelectedBitmap = bitmap
        mStarSelectedBitmap = BmpUtils.scaleBitmap(
            mOriginalSelectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set the bitmap of the be selected star by drawableId.
     *
     * @param drawableId Int
     */
    fun setStarSelectedBitmap(@DrawableRes drawableId: Int) {
        mOriginalSelectedBitmap = BmpUtils.getBitmapFromDrawable(drawableId, context)
        mStarSelectedBitmap = BmpUtils.scaleBitmap(
            mOriginalSelectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set the bitmap of the be unselected star.
     *
     * @param bitmap Bitmap
     */
    fun setStarUnselectedBitmap(bitmap: Bitmap) {
        mOriginalUnselectedBitmap = bitmap
        mStarUnselectedBitmap = BmpUtils.scaleBitmap(
            mOriginalUnselectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set the bitmap of the be unselected star by drawableId.
     *
     * @param drawableId Int
     */
    fun setStarUnselectedBitmap(@DrawableRes drawableId: Int) {
        mOriginalUnselectedBitmap = BmpUtils.getBitmapFromDrawable(drawableId, context)
        mStarUnselectedBitmap = BmpUtils.scaleBitmap(
            mOriginalUnselectedBitmap,
            mStarBitmapWidth.toInt(), mStarBitmapHeight.toInt()
        )
    }

    /**
     * Set star orientation.
     *
     * The setting is only valid when the [mStarOrientation] value is
     * [StarOrientation.UNSPECIFIED].
     *
     * @since 0.5.3
     */
    fun setStarOrientation(starOrientation: StarOrientation) {
        if (mStarOrientation == StarOrientation.UNSPECIFIED) {
            mStarOrientation = starOrientation
        }
    }

    init {
        val typedArray =
            context.obtainStyledAttributes(
                attrs,
                R.styleable.RatingView,
                defStyleAttr,
                defStyleRes
            )
        mStarIntervalWidth =
            typedArray.getDimension(
                R.styleable.RatingView_star_interval_width,
                mDefaultStarIntervalWidth
            )
        mStarBitmapWidth =
            typedArray.getDimension(
                R.styleable.RatingView_star_width,
                mDefaultStarBitmapWidth
            )
        mStarBitmapHeight =
            typedArray.getDimension(
                R.styleable.RatingView_star_height,
                mDefaultStarBitmapHeight
            )
        mStarCountNumber = typedArray.getInt(R.styleable.RatingView_star_count, mDefaultStarCount)
        mStarRating = typedArray.getFloat(R.styleable.RatingView_star_rating, mDefaultRating)
        mOriginalSelectedBitmap =
            BmpUtils.getBitmapFromDrawable(
                typedArray.getResourceId(
                    R.styleable.RatingView_star_selected,
                    R.drawable.ic_star_default_selected
                ),
                context
            )
        mStarSelectedBitmap = BmpUtils.scaleBitmap(
            mOriginalSelectedBitmap,
            mStarBitmapWidth.toInt(),
            mStarBitmapHeight.toInt()
        )
        mOriginalUnselectedBitmap = BmpUtils.getBitmapFromDrawable(
            typedArray.getResourceId(
                R.styleable.RatingView_star_unselected,
                R.drawable.ic_star_default_unselected
            ),
            context
        )
        mStarUnselectedBitmap = BmpUtils.scaleBitmap(
            mOriginalUnselectedBitmap,
            mStarBitmapWidth.toInt(),
            mStarBitmapHeight.toInt()
        )
        mStarOrientation = when (typedArray.getInt(
            R.styleable.RatingView_star_orientation, mDefaultOrientation
        )) {
            StarOrientation.HORIZONTAL.code -> StarOrientation.HORIZONTAL
            StarOrientation.VERTICAL.code -> StarOrientation.VERTICAL
            else -> StarOrientation.UNSPECIFIED
        }
        mStarSelectMethod = when (typedArray.getInt(
            R.styleable.RatingView_star_select_method, mDefaultSelectMethod
        )) {
            StarSelectMethod.UNABLE.code -> StarSelectMethod.UNABLE
            StarSelectMethod.CLICK.code -> StarSelectMethod.CLICK
            StarSelectMethod.SLIDING.code -> StarSelectMethod.SLIDING
            else -> StarSelectMethod.UNABLE
        }
        typedArray.recycle()
    }
}