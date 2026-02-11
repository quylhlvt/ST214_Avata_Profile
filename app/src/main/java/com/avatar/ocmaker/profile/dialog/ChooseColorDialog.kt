package com.avatar.ocmaker.profile.dialog

import android.app.Activity
import android.graphics.Color
import com.avatar.ocmaker.profile.base.BaseDialog
import com.avatar.ocmaker.profile.utils.onSingleClick
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.DialogColorPickerBinding


class ChooseColorDialog(context: Activity) : BaseDialog<DialogColorPickerBinding>(context, false) {
    var onDoneEvent: ((Int) -> Unit) = {}
    private var color = Color.WHITE
    override fun getContentView(): Int = R.layout.dialog_color_picker

    override fun initView() {
        binding.apply {
            colorPickerView.apply {
                hueSliderView = hueSlider
            }
            txtColor.post { txtColor.text = String.format("#%06X", 0xFFFFFF and color) }
        }
    }

    override fun bindView() {
        binding.apply {
            colorPickerView.setOnColorChangedListener { newColor -> color = newColor
                txtColor.post { txtColor.text = String.format("#%06X", 0xFFFFFF and color) } }
            btnClose.onSingleClick { dismiss() }
            btnDone.onSingleClick {
                dismiss()
                onDoneEvent.invoke(color)
            }
        }
    }


}