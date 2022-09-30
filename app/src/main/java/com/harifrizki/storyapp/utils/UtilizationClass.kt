package com.harifrizki.storyapp.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.module.errorpage.ConnectionErrorActivity
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

fun isNetworkConnected(context: Context?): Boolean {
    val result: Boolean
    val connectivityManager: ConnectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities =
        connectivityManager.activeNetwork ?: return false
    val activeNetwork =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    result = when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

fun isValidEmail(target: CharSequence?): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(target!!).matches()
}

fun checkBuildOS(buildOs: Int?): Boolean {
    return Build.VERSION.SDK_INT >= buildOs!!
}

fun getMaxShimmerList(): Int {
    return MAX_ITEM_LIST_SHIMMER
}

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, IMAGE_FORMAT_JPG, storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }
}

fun createCustomTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, IMAGE_FORMAT_JPG, storageDir)
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createCustomTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)
    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()

    return myFile
}

private val requestOptions =
    RequestOptions().centerCrop().placeholder(R.drawable.default_wait_image)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
        .priority(Priority.HIGH)
        .dontAnimate()
        .dontTransform()

fun doGlide(
    context: Context?,
    imageView: ImageView?,
    imageUrl: String?,
    imageError: Int? = R.drawable.default_wait_image
) {
    imageView?.let {
        Glide.with(context!!).applyDefaultRequestOptions(requestOptions)
            .load(imageUrl)
            .error(imageError)
            .into(it)
    }
}

fun doGlide(
    context: Context?,
    imageView: ImageView?,
    uri: Uri?,
    imageError: Int? = R.drawable.default_wait_image
) {
    Glide.with(context!!).applyDefaultRequestOptions(requestOptions)
        .load(uri)
        .error(imageError)
        .into(imageView!!)
}

@SuppressLint("UseCompatLoadingForDrawables")
private fun drawableBackgroundLightGrayForShimmer(context: Context?): Drawable {
    return context?.resources?.getDrawable(
        R.drawable.frame_background_light_gray_shimmer,
        null
    )!!
}

@SuppressLint("UseCompatLoadingForDrawables")
private fun drawableBackgroundGrayForShimmer(context: Context?): Drawable {
    return context?.resources?.getDrawable(
        R.drawable.frame_background_gray_shimmer,
        null
    )!!
}

private fun colorTransparentForShimmer(context: Context?): Int {
    return context?.resources?.getColor(
        R.color.transparent, null
    )!!
}

fun layoutStartDrawableShimmer(
    constraintLayouts: Array<ConstraintLayout>?,
    context: Context?
) {
    constraintLayouts?.forEach {
        it.apply {
            background =
                drawableBackgroundLightGrayForShimmer(
                    context
                )
        }
    }
}

fun widgetStartDrawableShimmer(
    textViews: Array<TextView>?,
    context: Context?
) {
    textViews?.forEach {
        it.apply {
            background =
                drawableBackgroundGrayForShimmer(
                    context
                )
            setTextColor(colorTransparentForShimmer(context))
            setCompoundDrawables(
                null,
                null,
                null,
                null
            )
        }
    }
}

fun widgetStartDrawableShimmer(
    imageViews: Array<ImageView>?,
    context: Context?
) {
    imageViews?.forEach {
        it.apply {
            background = drawableBackgroundGrayForShimmer(
                context
            )
            setColorFilter(
                ContextCompat.getColor(
                    context!!,
                    R.color.transparent
                ),
                PorterDuff.Mode.MULTIPLY
            )
        }
    }
}

fun widgetStartDrawableShimmer(
    buttons: Array<Button>?,
    context: Context?
) {
    buttons?.forEach {
        it.apply {
            backgroundTintList = ContextCompat.getColorStateList(
                context!!, R.color.light_gray
            )
            setTextColor(colorTransparentForShimmer(context))
        }
    }
}

fun goToErrorPage(
    context: Context,
    resultLauncher: ActivityResultLauncher<Intent>,
    errorState: ErrorState? = ErrorState.IS_ERROR_RESPONSE_API,
    generalResponse: GeneralResponse
) {
    Intent(context, ConnectionErrorActivity::class.java).apply {
        putExtra(ERROR_STATE, errorState?.name)
        putExtra(GENERAL_RESPONSE, generalResponse)
        resultLauncher.launch(this)
    }
}

fun makeSpannable(
    isSpanBold: Boolean? = true,
    text: String?,
    regex: String? = SPAN_REGEX,
    color: Int? = ZERO,
    onClicked: ((Any?) -> Unit)? = null
): SpannableStringBuilder {
    val stringBuffer = StringBuffer()
    val spannableStringBuilder = SpannableStringBuilder()

    val pattern = Pattern.compile(regex!!)
    val matcher = pattern.matcher(text!!)

    var id = 0
    while (matcher.find()) {
        stringBuffer.setLength(0)
        val group = matcher.group()

        val spanText = group.substring(1, group.length - 1)
        matcher.appendReplacement(stringBuffer, spanText)

        spannableStringBuilder.append(stringBuffer.toString())
        val start = spannableStringBuilder.length - spanText.length

        if (isSpanBold!!)
            spannableStringBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        if (color != ZERO)
            spannableStringBuilder.setSpan(
                color?.let { ForegroundColorSpan(it) },
                start,
                spannableStringBuilder.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        val finalId = id
        val clickableSpan = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }

            override fun onClick(p0: View) {
                onClicked?.invoke(finalId)
            }
        }
        spannableStringBuilder.setSpan(
            clickableSpan,
            start,
            spannableStringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        id++
    }

    stringBuffer.setLength(0)
    matcher.appendTail(stringBuffer)
    spannableStringBuilder.append(stringBuffer.toString())
    return spannableStringBuilder
}