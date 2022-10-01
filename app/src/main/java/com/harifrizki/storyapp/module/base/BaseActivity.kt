package com.harifrizki.storyapp.module.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.harifrizki.storyapp.R
import com.harifrizki.storyapp.components.*
import com.harifrizki.storyapp.components.AppBar.Companion.USE_ONLY_TITLE
import com.harifrizki.storyapp.data.remote.response.GeneralResponse
import com.harifrizki.storyapp.databinding.ComponentsAppBarBinding
import com.harifrizki.storyapp.databinding.ComponentsDetailStoryBinding
import com.harifrizki.storyapp.databinding.ComponentsEmptyDataBinding
import com.harifrizki.storyapp.model.Menu
import com.harifrizki.storyapp.utils.*
import com.harifrizki.storyapp.utils.MenuCode.*
import com.harifrizki.storyapp.utils.NotificationType.*

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
    private val optionList by lazy {
        OptionList()
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
    private var detailStory: ComponentsDetailStoryBinding? = null
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
        createOptionList()
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

    private fun createOptionList() {
        optionList.apply {
            create(context)
        }
    }

    fun createEmpty(binding: ComponentsEmptyDataBinding?) {
        empty.create(binding)
    }

    fun createRootView(view: View?, sfl: ShimmerFrameLayout?) {
        this.rootView = view
        this.sfl = sfl
    }

    fun createRootView(detailStory: ComponentsDetailStoryBinding?, sfl: ShimmerFrameLayout?) {
        this.detailStory = detailStory
        this.sfl = sfl
    }

    fun createAppBar(
        binding: ComponentsAppBarBinding?,
        title: String?,
        subTitle: String?,
        icon: Int? = R.drawable.ic_round_settings_24,
        useMode: Int? = USE_ONLY_TITLE,
        onClick: ((View) -> Unit)? = { }
    ) {
        appBar.apply {
            create(context, binding)
            title(title)
            subTitle(subTitle)
            setBar(icon = icon, useMode = useMode)
            onClickIconAppBar = {
                onClick?.invoke(it)
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
        return if (generalResponse?.error == false) true
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

    fun showWarning(
        titleNotification: String? = context?.getString(R.string.notification_warning_title),
        message: String?,
        buttonTitle: String? = context?.getString(R.string.ok),
        onClick: (() -> Unit)? = { dismissNotification() }
    ) {
        notification.apply {
            message?.let { notification(it) }
            titleNotification(titleNotification)
            buttonTitle(buttonTitle)
            notificationType(NOTIFICATION_WARNING)
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

    fun showOptionList(
        view: View?,
        menus: ArrayList<Menu>?,
        background: Int?,
        xPosition: Int?,
        yPosition: Int?,
        onClickMenu: ((Menu) -> Unit)?
    ) {
        optionList.apply {
            setBackground(background?.let { context?.getDrawable(it) })
            setMenus(menus)
            show(view, xPosition, yPosition)
            onClick = {
                it?.let { menu -> onClickMenu?.invoke(menu) }
            }
        }
    }

    fun dismissOptionList() {
        optionList.dismiss()
    }

    fun showBottomOption(
        title: String?,
        menus: ArrayList<Menu>?,
        onClickMenu: ((Menu) -> Unit)?
    ) {
        bottomOption.apply {
            this.title = title
            options = menus
            onClick = {
                it?.let { menu -> onClickMenu?.invoke(menu) }
            }
            show(supportFragmentManager, null)
        }
    }

    fun dismissBottomOption() {
        bottomOption.dismiss()
    }

    fun showOption(
        titleOption: String?,
        message: String?,
        optionAnimation: String? = LOTTIE_QUESTION_JSON,
        titlePositive: String? = context?.getString(R.string.ok),
        titleNegative: String? = context?.getString(R.string.cancel),
        colorButtonPositive: Int? = R.color.primary,
        onPositive: (() -> Unit)?,
        onNegative: (() -> Unit)? = { dismissOption() }
    ) {
        option.apply {
            titleOption(titleOption)
            message?.let { option(it) }
            optionAnimation(optionAnimation)
            buttonPositive(titlePositive)
            buttonPositive(colorButtonPositive)
            buttonNegative(titleNegative)
            onClickPositive = {
                onPositive?.invoke()
            }
            onClickNegative = {
                onNegative?.invoke()
            }
            show()
        }
    }

    fun dismissOption() {
        option.dismiss()
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

    fun showRootView() {
        rootView?.visibility = View.VISIBLE
    }

    fun showDetailStory() {
        detailStory?.root?.visibility = View.VISIBLE
    }

    fun showView(views: Array<View>?) {
        views?.forEach {
            it.visibility = View.VISIBLE
        }
    }

    fun dismissRootView() {
        rootView?.visibility = View.GONE
    }

    fun dismissDetailStory() {
        detailStory?.root?.visibility = View.GONE
    }

    fun dismissView(views: Array<View>?) {
        views?.forEach {
            it.visibility = View.GONE
        }
    }

    fun showEmptyError(
        title: String?,
        message: String?,
        animation: String? = LOTTIE_ERROR_JSON,
        useAnimation: Boolean? = true,
        directShow: Boolean? = false
    ) {
        empty.apply {
            message(title, message)
            animation(animation, useAnimation)
            if (directShow!!) show()
        }
    }

    fun showEmpty() {
        empty.show()
    }

    fun dismissEmpty() {
        empty.dismiss()
    }

    fun shimmerOn(
        shimmerFrameLayout: ShimmerFrameLayout?,
        isOn: Boolean?
    ) {
        if (isOn!!) {
            shimmerFrameLayout?.startShimmer()
            shimmerFrameLayout?.visibility = View.VISIBLE
        } else {
            shimmerFrameLayout?.stopShimmer()
            shimmerFrameLayout?.visibility = View.GONE
        }
    }

    fun loadingList(
        isOn: Boolean?,
        isGetData: Boolean? = false
    ) {
        if (isOn!!) {
            if (detailStory != null) dismissDetailStory()
            else dismissRootView()
            dismissEmpty()
            shimmerOn(sfl, true)
        } else {
            shimmerOn(sfl, false)
            if (isGetData!!) {
                if (detailStory != null) showDetailStory()
                else showRootView()
                dismissEmpty()
            } else {
                if (detailStory != null) dismissDetailStory()
                else dismissRootView()
                showEmpty()
            }
        }
    }

    fun enableAccess(buttons: Array<Button>?) {
        buttons!!.forEach {
            it.isEnabled = true
        }
    }

    fun disableAccess(buttons: Array<Button>?) {
        buttons!!.forEach {
            it.isEnabled = false
        }
    }

    fun enableAccess(imageView: Array<ImageView>?) {
        imageView!!.forEach {
            it.isEnabled = true
        }
    }

    fun disableAccess(imageView: Array<ImageView>?) {
        imageView!!.forEach {
            it.isEnabled = false
        }
    }

    fun mainMenus(): ArrayList<Menu> {
        val menus: ArrayList<Menu> = ArrayList()
        menus.add(
            Menu(
                MENU_ADD_STORY,
                context?.getString(R.string.main_menu_add_story),
                R.drawable.ic_round_note_add_24,
                true,
                R.color.primary,
                R.color.primary
            )
        )
        menus.add(
            Menu(
                MENU_SETTING_LANGUAGE,
                context?.getString(R.string.main_menu_setting_language),
                R.drawable.ic_round_settings_24,
                true,
                R.color.primary,
                R.color.primary
            )
        )
        return menus
    }

    fun imageMenus(): ArrayList<Menu> {
        val menus: ArrayList<Menu> = ArrayList()
        menus.add(
            Menu(
                MENU_CAMERA,
                context?.getString(R.string.menu_add_image_from_camera),
                R.drawable.ic_round_photo_camera_24,
                true,
                R.color.primary,
                R.color.primary
            )
        )
        menus.add(
            Menu(
                MENU_GALLERY,
                context?.getString(R.string.menu_add_image_from_gallery),
                R.drawable.ic_round_folder_24,
                true,
                R.color.primary,
                R.color.primary
            )
        )
        return menus
    }
}