package com.harifrizki.storyapp.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.utils.EMPTY_STRING
import com.harifrizki.storyapp.utils.ZERO

class TextEditText : AppCompatEditText {

    private var name: String = EMPTY_STRING
    private var message: String = EMPTY_STRING
    private var paddingSize: Int = 50

    private lateinit var backgroundDrawable: MaterialShapeDrawable
    private lateinit var messagePaint: Paint

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

    private fun init(context: Context) {
        name = context.getString(R.string.this_field)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
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

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let { validText(it) }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
    }

    private fun validText(charSequence: CharSequence): Boolean {
        return if (charSequence.toString().length > ZERO) {
            message = EMPTY_STRING
            true
        }  else false
    }

    fun name(string: String) {
        name = string.ifEmpty { context.getString(R.string.this_field) }
    }

    fun valid(): Boolean {
        val text: String = text.toString().trim()
        return if (text.isEmpty()) {
            message = context.getString(
                R.string.message_error_empty,
                name
            )
            requestFocus()
            false
        } else validText(text)
    }
}