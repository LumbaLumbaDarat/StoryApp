package com.harifrizki.storyapp.module.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.View
import android.widget.EditText
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.components.*
import com.harifrizki.storyapp.components.AppBar.Companion.USE_ONLY_TITLE
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.databinding.ComponentsAppBarBinding
import com.harifrizki.storyapp.databinding.ComponentsEmptyDataBinding
import com.harifrizki.storyapp.utils.ErrorState
import com.harifrizki.storyapp.utils.NotificationType.*
import com.harifrizki.storyapp.utils.goToErrorPage
import com.harifrizki.storyapp.utils.isNetworkConnected

open class BaseActivity : AppCompatActivity() {

    private val appBar by lazy {
        AppBar()
    }
    private val loading by lazy {
        Loading()
    }
    private val notification by lazy {
        Notification()
    }
    private val option by lazy {
        Option()
    }
    private val bottomOption by lazy {
        BottomOption()
    }
    private val empty by lazy {
        EmptyData()
    }

    private var context: Context? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private var rootView: View? = null
    private var sfl: ShimmerFrameLayout? = null

    @SuppressLint("SourceLockedOrientationActivity")
    fun create(
        buildContext: Context,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        this.apply {
            context = buildContext
            resultLauncher = activityResultLauncher
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

        createLoading()
        createNotification()
        createOption()
    }

    private fun createLoading() {
        loading.apply {
            create(context)
        }
    }

    private fun createNotification() {
        notification.apply {
            create(context)
        }
    }

    private fun createOption() {
        option.apply {
            create(context)
        }
    }

    fun createEmpty(binding: ComponentsEmptyDataBinding?) {
        empty.create(binding)
    }

    fun createRootView(view: View?) {
        this.rootView = view
    }

    fun createRootView(view: View?, sfl: ShimmerFrameLayout?) {
        this.rootView = view
        this.sfl = sfl
    }

    fun createAppBar(
        binding: ComponentsAppBarBinding?,
        title: String?,
        subTitle: String?,
        icon: Int? = R.drawable.ic_round_settings_24,
        useMode: Int? = USE_ONLY_TITLE,
        onClick: (() -> Unit)? = null
    ) {
        appBar.apply {
            create(context, binding)
            title(title)
            subTitle(subTitle)
            setBar(icon = icon, useMode = useMode)
            onClickIconAppBar = {
                onClick?.invoke()
            }
        }
    }

    fun setThemeForSwipeRefreshLayoutLoadingAnimation(
        context: Context?,
        swipeRefreshLayout: SwipeRefreshLayout?
    ) {
        swipeRefreshLayout?.apply {
            context?.resources?.getColor(R.color.white, null)?.let {
                setColorSchemeColors(
                    it
                )
            }
            context?.resources?.getColor(R.color.primary, null)?.let {
                setProgressBackgroundColorSchemeColor(
                    it
                )
            }
        }
    }

    fun networkConnected(
        isGoToErrorActivity: Boolean = true
    ): Boolean {
        return if (isNetworkConnected(context))
            true
        else {
            if (isGoToErrorActivity) {
                context?.let { context ->
                    resultLauncher?.let { result ->
                        goToErrorPage(
                            context,
                            result,
                            ErrorState.IS_NO_NETWORK,
                            GeneralResponse()
                        )
                    }
                }
            }
            false
        }
    }

    fun isResponseSuccess(generalResponse: GeneralResponse?): Boolean {
        return if (generalResponse?.error == true) true
        else {
            showError(message = generalResponse?.message)
            false
        }
    }

    fun showError(
        titleNotification: String? = context?.getString(R.string.notification_error_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_ERROR)
            onClickButton = { onClick?.invoke() }
            show()
        }
    }

    fun showInformation(
        titleNotification: String? = context?.getString(R.string.notification_information_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_INFORMATION)
            onClickButton = { onClick?.invoke() }
            show()
        }
    }

    fun showSuccess(
        titleNotification: String? = context?.getString(R.string.notification_success_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_SUCCESS)
            onClickButton = { onClick?.invoke() }
            show()
        }
    }

    fun dismissNotification() {
        notification.dismiss()
    }

    fun showLoading(message: String? = getString(R.string.message_login)) {
        loading.setMessage(true, message, Color.BLACK)
        loading.show()
    }

    fun dismissLoading() {
        loading.dismiss()
    }

    fun wasError(generalResponse: GeneralResponse?) {
        context?.let { context ->
            resultLauncher?.let { resultLauncher ->
                generalResponse?.let { generalResponse ->
                    goToErrorPage(
                        context,
                        resultLauncher,
                        generalResponse = generalResponse
                    )
                }
            }
        }
    }

    fun resetEditText(ties: Array<EditText>?) {
        ties?.forEach {
            it.text?.clear()
        }
    }
}