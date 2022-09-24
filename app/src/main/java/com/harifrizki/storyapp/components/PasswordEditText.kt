package com.harifrizki.storyapp.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.utils.EMPTY_STRING
import com.harifrizki.storyapp.utils.isValidEmail
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class PasswordEditText : AppCompatEditText, View.OnTouchListener {

    private var message: String = EMPTY_STRING
    private var paddingSize: Int = 50
    private var isHidePassword: Boolean = true

    private val MINIMUM_PASSWORD_LENGTH: Int = 6

    private lateinit var backgroundDrawable: MaterialShapeDrawable
    private lateinit var messagePaint: Paint
    private lateinit var visibilityPassword: Drawable

    constructor(context: Context) : super(context) {
        init(context = context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context = context)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyAttributeSet: Int) : super(
        context,
        attributeSet,
        defStyAttributeSet
    ) {
        init(context = context)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawText(message, paddingLeft.toFloat(), height.toFloat() - 20F, messagePaint)
        background = backgroundDrawable
        setPadding(paddingSize, paddingSize, paddingSize, paddingSize)
    }

    override fun onTouch(view: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float
            var isClearButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                clearButtonEnd = (visibilityPassword.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < clearButtonEnd -> isClearButtonClicked = true
                }
            } else {
                clearButtonStart =
                    (width - paddingEnd - visibilityPassword.intrinsicWidth).toFloat()
                when {
                    event.x > clearButtonStart -> isClearButtonClicked = true
                }
            }
            return if (isClearButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (isHidePassword) {
                            isHidePassword = false
                            visibilityPassword()
                        } else {
                            isHidePassword = true
                            hidePassword()
                        }
                        true
                    }
                    else -> false
                }
            } else false
        }
        return false
    }

    private fun init(context: Context) {
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        setOnTouchListener(this)
        backgroundDrawable = MaterialShapeDrawable(
            ShapeAppearanceModel().toBuilder().setAllCornerSizes(20F).build()
        ).apply {
            setTint(context.getColor(R.color.secondary))
            paintStyle = Paint.Style.FILL
        }

        messagePaint = Paint().apply {
            color = context.getColor(R.color.red)
            textSize = 30F
        }

        hidePassword()

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let { validPassword(it) }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun visibilityPassword() {
        visibilityPassword =
            ContextCompat.getDrawable(context, R.drawable.ic_round_visibility_off_24) as Drawable
        DrawableCompat.setTint(visibilityPassword, context.getColor(R.color.primary))
        setButtonDrawables(endOfTheText = visibilityPassword)
        transformationMethod = null
    }

    private fun hidePassword() {
        visibilityPassword =
            ContextCompat.getDrawable(context, R.drawable.ic_round_visibility_24) as Drawable
        DrawableCompat.setTint(visibilityPassword, context.getColor(R.color.primary))
        setButtonDrawables(endOfTheText = visibilityPassword)
        transformationMethod = PasswordTransformationMethod()
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun validPassword(charSequence: CharSequence): Boolean {
        return if (charSequence.toString().length < MINIMUM_PASSWORD_LENGTH) {
            message = context.getString(R.string.message_to_short_password)
            false
        } else {
            message = EMPTY_STRING
            true
        }
    }

    fun valid(): Boolean {
        val text: String = text.toString().trim()
        return if (text.isEmpty()) {
            message = context.getString(
                R.string.message_error_empty,
                context.getString(R.string.password)
            )
            requestFocus()
            false
        } else validPassword(text)
    }
}