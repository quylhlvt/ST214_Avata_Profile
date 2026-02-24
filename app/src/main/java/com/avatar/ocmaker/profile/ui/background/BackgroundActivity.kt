package com.avatar.ocmaker.profile.ui.background

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.avatar.ocmaker.profile.base.AbsBaseActivity
import com.avatar.ocmaker.profile.custom.Draw
import com.avatar.ocmaker.profile.custom.DrawView
import com.avatar.ocmaker.profile.custom.DrawableDraw
import com.avatar.ocmaker.profile.custom.listener.listenerdraw.OnDrawListener
import com.avatar.ocmaker.profile.data.repository.ApiRepository
import com.avatar.ocmaker.profile.dialog.ChooseColorDialog
import com.avatar.ocmaker.profile.dialog.DialogExit
import com.avatar.ocmaker.profile.dialog.DialogSpeech
import com.avatar.ocmaker.profile.ui.background.adapter.BackGroundTextAdapter
import com.avatar.ocmaker.profile.ui.background.adapter.ColorAdapter
import com.avatar.ocmaker.profile.ui.background.adapter.ColorTextAdapter
import com.avatar.ocmaker.profile.ui.background.adapter.FontAdapter
import com.avatar.ocmaker.profile.ui.background.adapter.ImageAdapter
import com.avatar.ocmaker.profile.ui.background.adapter.StikerAdapter
import com.avatar.ocmaker.profile.ui.main.MainActivity
import com.avatar.ocmaker.profile.ui.succes.SuccessActivity
import com.avatar.ocmaker.profile.utils.DataHelper
import com.avatar.ocmaker.profile.utils.dpToPx
import com.avatar.ocmaker.profile.utils.hide
import com.avatar.ocmaker.profile.utils.hideKeyboard
import com.avatar.ocmaker.profile.utils.newIntent
import com.avatar.ocmaker.profile.utils.onSingleClick
import com.avatar.ocmaker.profile.utils.pickImage
import com.avatar.ocmaker.profile.utils.saveBitmap
import com.avatar.ocmaker.profile.utils.setLayoutParam
import com.avatar.ocmaker.profile.utils.show
import com.avatar.ocmaker.profile.utils.showSystemUI
import com.avatar.ocmaker.profile.utils.showToast
import com.avatar.ocmaker.profile.utils.viewToBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.ObjectKey
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.ActivityBackgroundBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundActivity : AbsBaseActivity<ActivityBackgroundBinding>() {
    @Inject
    lateinit var apiRepository: ApiRepository
    val adapterBGText by lazy { BackGroundTextAdapter() }
    val adapterColor by lazy { ColorAdapter() }
    val adapterColorText by lazy { ColorTextAdapter() }
    val adapterFont by lazy { FontAdapter() }
    val adapterImage by lazy { ImageAdapter() }
    val adapterStiker by lazy { StikerAdapter() }
    var path = ""
    val viewModel: BackGroundViewModel by viewModels()
    override fun getLayoutId(): Int = R.layout.activity_background

    //    private fun applyGradientToLoadingText() {
//        binding.txtContent.post {
//            binding.txtContent.gradientHorizontal(
//                "#01579B".toColorInt(),
//                "#2686C6".toColorInt()
//            )
//        }
//        binding.txtTitle.setTextColor(ContextCompat.getColor(this,R.color.white))
//
//    }
    override fun onRestart() {
        super.onRestart()

    }



    override fun initView() {
//        binding.txtContent.post {
//            binding.txtContent.gradientHorizontal(
//                startColor = "#01579B".toColorInt(),
//                endColor   = "#2686C6".toColorInt()
//            )
//
//        }
        binding.txtTitle.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.txtTitle.isSelected = true
        if (DataHelper.arrBlackCentered.isEmpty()) {
//            GlobalScope.launch(Dispatchers.IO) {
//                getData(apiRepository)
//            }
            startActivity(
                newIntent(
                    applicationContext,
                    MainActivity::class.java
                )
            )
            finish()
        } else {
            path = intent.getStringExtra("path").toString()
            initRcv()
            initDrawView()
            initData()
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            if (imeVisible) {
                // 👉 Bàn phím HIỆN
                setLayoutParam(
                    binding.ctl,
                    0f,
                    0f,
                    dpToPx(200f, applicationContext),
                    0f
                )
            } else {
                binding.root.postDelayed({
                    showSystemUI()
                }, 50)
                // 👉 Bàn phím ẨN
                setLayoutParam(
                    binding.ctl,
                    0f,
                    0f,
                    dpToPx(0f, applicationContext),
                    0f
                )
            }
            insets
        }
    }


    fun initRcv() {
        binding.apply {
//            iclBg.rcvColor.itemAnimator = null
//            iclBg.rcvColor.adapter = adapterColor
//
//            iclBg.rcvImage.itemAnimator = null
//            iclBg.rcvImage.adapter = adapterImage

            iclText.rcvColor.itemAnimator = null
            iclText.rcvColor.adapter = adapterColorText

            iclText.rcvFont.itemAnimator = null
            iclText.rcvFont.adapter = adapterFont

            iclStiker.rcv.itemAnimator = null
            iclStiker.rcv.adapter = adapterStiker

            iclTextBG.rcv.itemAnimator = null
            iclTextBG.rcv.adapter = adapterBGText
        }
    }

    fun initData() {
        showLoading()
        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.loadDataDefault(this@BackgroundActivity)
            viewModel.updatePathDefault(path)
//            addDrawable(viewModel.pathDefault, true)

            withContext(Dispatchers.Main) {
                Glide.with(this@BackgroundActivity).load(viewModel.pathDefault).into(binding.imvBackground)
                adapterImage.submitList(viewModel.backgroundImageList)
                adapterColor.submitList(viewModel.backgroundColorList)
                adapterStiker.submitList(viewModel.stickerList)
                adapterBGText.submitList(viewModel.speechList)
                adapterFont.submitList(viewModel.textFontList)
                adapterColorText.submitList(viewModel.textColorList)
                delay(200)
//                clearFocus()
                dismissLoading()
                binding.iclText.edt.typeface = ResourcesCompat.getFont(
                    binding.root.context, viewModel.textFontList[0].color
                )
            }
        }
    }

    private fun dismissLoading() {
        binding.llLoading.hide()
//        binding.animationView.hide()
    }

    private fun showLoading() {
        binding.llLoading.show()
//        applyGradientToLoadingText()
//        binding.animationView.show()
    }

    private fun clearFocus() {
        binding.drawView.hideSelect()
    }
    fun hideKeyboard() {
        hideKeyboard(binding.iclText.edt)
        binding.root.postDelayed({
            showSystemUI()
        }, 100)
    }
    private fun addDrawable(
        path: String, isCharacter: Boolean = false, bitmapText: Bitmap? = null
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            val bitmapDefault = if (isCharacter){Glide.with(this@BackgroundActivity).load(path).signature(
                ObjectKey(
                    File(
                        path
                    ).lastModified()
                )
            ).submit().get().toBitmap()}else{
                bitmapText
                    ?: Glide.with(this@BackgroundActivity).load(path).override(512, 512)
                        .encodeQuality(50)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE).submit()
                        .get().toBitmap()
            }
            val drawableEmoji =
                viewModel.loadDrawableEmoji(this@BackgroundActivity, bitmapDefault, isCharacter)

            withContext(Dispatchers.Main) {
                drawableEmoji.let { binding.drawView.addDraw(it) }
            }
        }
    }

    var checkSee = true

    @SuppressLint("UseCompatLoadingForDrawables", "ClickableViewAccessibility")
    override fun initAction() {
        binding.apply {


            main.onSingleClick {
                viewModel.setIsFocusEditText(false)
                hideKeyboard()
                clearFocus()
            }

//            btnBg.onSingleClick {
//                hideKeyboard()
//                selectBottomTab(btnBg)
////                llBG.show()
//                linearbg.show()
//                llStiker.hide()
//                llText.hide()
//                llTextBG.hide()
//            }
            btnItem.onSingleClick {
                hideKeyboard()
                selectBottomTab(btnItem)
//                llBG.hide()
                llStiker.show()
                llText.hide()
                linearbg.hide()
                llTextBG.hide()
            }
            btnBgText.onSingleClick {
                hideKeyboard()
                selectBottomTab(btnBgText)
//                llBG.hide()
                linearbg.hide()
                llStiker.hide()
                llTextBG.show()
                llText.hide()
            }
            btnText.onSingleClick {
                hideKeyboard()
                selectBottomTab(btnText)
//                llBG.hide()
                linearbg.hide()
                llStiker.hide()
                llTextBG.hide()
                llText.show()
            }
//            binding.apply {
//                btnImage.onSingleClick {
//                    iclBg.apply {
//                        rcvImage.show()
//                        rcvColor.hide()
//                    }
//                    btnImage.setBackgroundResource(R.drawable.img_select)
//                    txtImgage.setTextColor( ContextCompat.getColor(this@BackgroundActivity, R.color.white))
//                    btnColor.setBackgroundResource(R.drawable.img_unselect)
//                    txtColor.setTextColor( ContextCompat.getColor(this@BackgroundActivity, R.color.app_color3))
//                }
//                btnColor.onSingleClick {
//                    iclBg.apply {
//                        rcvColor.show()
//                        rcvImage.hide()
//                    }
//                    txtColor.setTextColor( ContextCompat.getColor(this@BackgroundActivity, R.color.white))
//                    btnColor.setBackgroundResource(R.drawable.img_select)
//                    txtImgage.setTextColor( ContextCompat.getColor(this@BackgroundActivity, R.color.app_color3))
//                    btnImage.setBackgroundResource(R.drawable.img_unselect)
//                }
//            }
            imvBack.onSingleClick {
                var dialog = DialogExit(
                    this@BackgroundActivity,
                    "exit"
                )
                dialog.onClick = {
                    dialog.dismiss()
                    finish()
                }
                dialog.show()
            }
            btnReset.onSingleClick {
                viewModel.setIsFocusEditText(false)
                hideKeyboard()
                var dialog = DialogExit(
                    this@BackgroundActivity,
                    "reset"
                )
                dialog.onClick = {
                    lifecycleScope.launch {
                        showLoading()
                        withContext(Dispatchers.IO) {
                            viewModel.loadDataDefault(this@BackgroundActivity)
                            viewModel.resetDraw()
                            withContext(Dispatchers.Main) {
                                binding.drawView.removeAllDraw()
                                binding.iclText.edt.setText("")
                                addDrawable(viewModel.pathDefault, true)
                                clearFocusBG()

                                viewModel.textColorList[adapterColorText.posSelect].isSelected =
                                    false
                                viewModel.textColorList[1].isSelected = true
                                adapterColorText.posSelect = 1
                                iclText.edt.setTextColor("#000000".toColorInt())
                                adapterColorText.submitList(viewModel.textColorList)

                                if (adapterFont.posSelect > 0) {
                                    viewModel.textFontList[adapterFont.posSelect].isSelected = false
                                    adapterFont.posSelect = 0
                                    viewModel.textFontList[0].isSelected = true
                                    iclText.edt.typeface = ResourcesCompat.getFont(
                                        binding.root.context, viewModel.textFontList[0].color
                                    )
                                    adapterFont.submitList(viewModel.textFontList)
                                }
                                dismissLoading()
                            }

                        }
                    }
                    dialog.dismiss()
                }
                dialog.show()
            }
            btnSave.onSingleClick {
                hideKeyboard()
                binding.llLoading.show()
//                applyGradientToLoadingText()
//                binding.animationView.show()
                clearFocus()
                lifecycleScope.launch(Dispatchers.IO) {
                    delay(200)
                    saveBitmap(
                        this@BackgroundActivity,
                        viewToBitmap(binding.drawView),
                        "",
                        false
                    ) { it, path, _ ->
                        if (it) {
                            binding.llLoading.visibility = View.GONE
//                            binding.animationView.hide()
                            startActivity(
                                Intent(
                                    this@BackgroundActivity,
                                    SuccessActivity::class.java
                                ).putExtra("path", path)
                            )

                        } else {
                            llLoading.visibility = View.GONE
                            showToast(
                                this@BackgroundActivity,
                                R.string.save_failed
                            )
                        }
                    }
                }
            }
            adapterImage.onClick = { pos ->
                if (pos == 0) {
                    pickImage(pickImageLauncher)
                } else {
                    clearFocusBG()
                    Glide.with(applicationContext).load(viewModel.backgroundImageList[pos].path)
                        .into(binding.imvBackground)
                    viewModel.backgroundImageList[pos].isSelected = true
                    adapterImage.posSelect = pos
                    adapterImage.submitList(viewModel.backgroundImageList)
                }
            }
            adapterColor.onClick = { pos ->
                if (pos == 0) {
                    val dialog =
                        ChooseColorDialog(this@BackgroundActivity)
                    dialog.show()

                    dialog.onDoneEvent = { color ->
                        clearFocusBG()
                        binding.imvBackground.setBackgroundColor(color)
                        viewModel.backgroundColorList[0].isSelected = true
                        adapterColor.posSelect = 0
                        adapterColor.submitList(viewModel.backgroundColorList)
                    }
                } else {
                    clearFocusBG()
                    binding.imvBackground.setBackgroundColor(viewModel.backgroundColorList[pos].color)
                    viewModel.backgroundColorList[pos].isSelected = true
                    adapterColor.posSelect = pos
                    adapterColor.submitList(viewModel.backgroundColorList)
                }
            }
            adapterStiker.onClick = { path ->
                addDrawable(path)
            }
            adapterBGText.onClick = {
                var dialog = DialogSpeech(
                    this@BackgroundActivity,
                    viewModel.speechList[it].path
                )
                dialog.show()
                dialog.onDoneClick = { bitmap ->
                    addDrawable("", false, bitmap)
                }
            }
            adapterColorText.onClick = { pos ->
                if (pos == 0) {
                    val dialog =
                        ChooseColorDialog(this@BackgroundActivity)
                    dialog.show()

                    dialog.onDoneEvent = { color ->
                        viewModel.textColorList[adapterColorText.posSelect].isSelected = false
                        binding.iclText.edt.setTextColor(color)
                        viewModel.textColorList[0].isSelected = true
                        adapterColorText.posSelect = 0
                        adapterColorText.submitList(viewModel.textColorList)
                    }
                } else {
                    viewModel.textColorList[adapterColorText.posSelect].isSelected = false
                    binding.iclText.edt.setTextColor(viewModel.textColorList[pos].color)
                    viewModel.textColorList[pos].isSelected = true
                    adapterColorText.posSelect = pos
                    adapterColorText.submitList(viewModel.textColorList)
                }
            }
            adapterFont.onClick = { pos ->
                viewModel.textFontList[adapterFont.posSelect].isSelected = false
                adapterFont.posSelect = pos
                viewModel.textFontList[pos].isSelected = true
                iclText.edt.typeface = ResourcesCompat.getFont(
                    binding.root.context, viewModel.textFontList[pos].color
                )
                adapterFont.submitList(viewModel.textFontList)
            }
        }
        binding.iclText.imvTick.onSingleClick {
            showSystemUI()
            handleDoneText()
        }
        binding.iclText.edt.addTextChangedListener {
            binding.tvGetText.text = it.toString().trim()
        }
    }

    private fun selectBottomTab(selectedBtn: ImageView) {
        // Danh sách tất cả 4 nút tab
        val tabs = listOf(
//            binding.btnBg to Pair(R.drawable.imv_bg, R.drawable.imv_bg_true),
            binding.btnItem to Pair(R.drawable.imv_item, R.drawable.imv_item_true),
            binding.btnBgText to Pair(R.drawable.imv_bg_text, R.drawable.imv_bg_text_true),
            binding.btnText to Pair(R.drawable.imv_text, R.drawable.imv_text_true)
        )

        tabs.forEach { (btn, icons) ->
            val (normalIcon, selectedIcon) = icons
            if (btn == selectedBtn) {
                // Nút được chọn
                btn.isSelected = true
                btn.setImageResource(selectedIcon)
//                btn.updateMargin(this@BackgroundActivity, bottomDp = 15)
            } else {
                // Các nút khác → reset
                btn.isSelected = false
                btn.setImageResource(normalIcon)
//                btn.updateMargin(this@BackgroundActivity, bottomDp = 0)
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                clearFocusBG()
                viewModel.backgroundImageList[0].isSelected = true
                adapterImage.posSelect = 0
                adapterImage.submitList(viewModel.backgroundImageList)
                Glide.with(applicationContext).load(uri).into(binding.imvBackground)
            }
        }
    }

    fun handleDoneText() {
        showSystemUI()
        hideKeyboard()
        binding.root.postDelayed({
            viewModel.setIsFocusEditText(false)
            showSystemUI()
            binding.apply {
                if (iclText.edt.text.toString().trim() == "") {
                    showToast(
                        applicationContext,
                        R.string.null_edt
                    )
                } else {
//                    tvGetText.text = iclText.edt.text.toString().trim()
                    tvGetText.typeface = ResourcesCompat.getFont(
                        binding.root.context, viewModel.textFontList[adapterFont.posSelect].color
                    )
                    tvGetText.setTextColor(iclText.edt.textColors)
                    val bitmap =
                        viewToBitmap(tvGetText)
                    val drawableEmoji =
                        viewModel.loadDrawableEmoji(this@BackgroundActivity, bitmap, isText = true)
                    binding.drawView.addDraw(drawableEmoji)

                    iclText.edt.text = null

                    viewModel.textColorList[adapterColorText.posSelect].isSelected = false
                    viewModel.textColorList[1].isSelected = true
                    adapterColorText.posSelect = 1
                    iclText.edt.setTextColor("#000000".toColorInt())
                    adapterColorText.submitList(viewModel.textColorList)

                    if (adapterFont.posSelect > 0) {
                        viewModel.textFontList[adapterFont.posSelect].isSelected = false
                        adapterFont.posSelect = 0
                        viewModel.textFontList[0].isSelected = true
                        iclText.edt.typeface = ResourcesCompat.getFont(
                            binding.root.context, viewModel.textFontList[0].color
                        )
                        adapterFont.submitList(viewModel.textFontList)
                    }
                }
            }
        }, 300)

    }

    fun clearFocusBG() {
        binding.imvBackground.setBackgroundColor(getColor(R.color.transparent))
        binding.imvBackground.setImageBitmap(null)
        if (adapterColor.posSelect >= 0) {
            viewModel.backgroundColorList[adapterColor.posSelect].isSelected = false
            adapterColor.posSelect = -1
            adapterColor.submitList(viewModel.backgroundColorList)
        }

        if (adapterImage.posSelect >= 0) {
            viewModel.backgroundImageList[adapterImage.posSelect].isSelected = false
            adapterImage.posSelect = -1
            adapterImage.submitList(viewModel.backgroundImageList)
        }

    }

    private fun initDrawView() {
        binding.drawView.apply {
            setConstrained(true)
            setLocked(false)
            setOnDrawListener(object :
                OnDrawListener {
                override fun onAddedDraw(draw: Draw) {
                    viewModel.updateCurrentCurrentDraw(draw)
                    viewModel.addDrawView(draw)
                    viewModel.setIsFocusEditText(false)
                }

                override fun onClickedDraw(draw: Draw) {
                    viewModel.setIsFocusEditText(false)
                    hideKeyboard()
                }

                override fun onDeletedDraw(draw: Draw) {
                    if (draw.isCharacter) {

                    } else {
                        viewModel.deleteDrawView(draw)
                        viewModel.setIsFocusEditText(false)
                    }

                }

                override fun onDragFinishedDraw(draw: Draw) {
                    viewModel.setIsFocusEditText(false)
                }

                override fun onTouchedDownDraw(draw: Draw) {
                    viewModel.updateCurrentCurrentDraw(draw)
                    viewModel.setIsFocusEditText(false)
                }

                override fun onZoomFinishedDraw(draw: Draw) {}

                override fun onFlippedDraw(draw: Draw) {
                    viewModel.setIsFocusEditText(false)
                }

                override fun onDoubleTappedDraw(draw: Draw) {}

                override fun onHideOptionIconDraw() {}

                override fun onUndoDeleteDraw(draw: List<Draw?>) {}

                override fun onUndoUpdateDraw(draw: List<Draw?>) {}

                override fun onUndoDeleteAll() {}

                override fun onRedoAll() {}

                override fun onReplaceDraw(draw: Draw) {}

                override fun onEditText(draw: DrawableDraw) {}

                override fun onReplace(draw: Draw) {}
            })
        }
    }

}