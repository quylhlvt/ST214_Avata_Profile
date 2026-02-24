package com.avatar.ocmaker.profile.dialog

import android.app.Activity
import android.graphics.Color
import com.avatar.ocmaker.profile.base.BaseDialog
import com.avatar.ocmaker.profile.utils.onSingleClick
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.DialogColorPickerBinding
import java.util.Locale


class ChooseColorDialog(context: Activity) : BaseDialog<DialogColorPickerBinding>(context, false) {
    var onDoneEvent: ((Int) -> Unit) = {}
    private var color = Color.WHITE
    override fun getContentView(): Int = R.layout.dialog_color_picker

    override fun initView() {
        binding.colorPickerView.hueSliderView = binding.hueSlider
        updateHexText(color)
    }

    override fun bindView() {
        binding.apply {

            colorPickerView.setOnColorChangedListener { newColor ->
                color = newColor
                updateHexText(color)
            }

            btnClose.onSingleClick { dismiss() }

            btnDone.onSingleClick {
                dismiss()
                onDoneEvent.invoke(color)
            }
        }
    }

    private fun updateHexText(color: Int) {
        val hex = color and 0xFFFFFF
        binding.txtColor.text =
            String.format(Locale.US, "#%06x", hex)
    }
}