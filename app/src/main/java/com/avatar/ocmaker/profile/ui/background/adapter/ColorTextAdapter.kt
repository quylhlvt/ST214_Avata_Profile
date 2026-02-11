package com.avatar.ocmaker.profile.ui.background.adapter

import androidx.recyclerview.widget.RecyclerView
import com.avatar.ocmaker.profile.base.AbsBaseAdapter
import com.avatar.ocmaker.profile.base.AbsBaseDiffCallBack
import com.avatar.ocmaker.profile.data.model.SelectedModel
import com.avatar.ocmaker.profile.utils.hide
import com.avatar.ocmaker.profile.utils.onSingleClick
import com.avatar.ocmaker.profile.utils.show
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.ItemColorEdtBinding

class ColorTextAdapter :
    AbsBaseAdapter<SelectedModel, ItemColorEdtBinding>(R.layout.item_color_edt, DiffCallBack()) {
    var onClick: ((Int) -> Unit)? = null
    var posSelect = 1
    override fun bind(
        binding: ItemColorEdtBinding,
        position: Int,
        data: SelectedModel,
        holder: RecyclerView.ViewHolder
    ) {
        binding.imvColor.onSingleClick {

            onClick?.invoke(position)
        }
        binding.imvColor.setBackgroundColor(data.color)
        if(position == 0){
            binding.btnAddColor.show()
        }else{
            binding.btnAddColor.hide()
        }
        if(data.isSelected){
            binding.apply {
                vFocus.show()
                cardColor.cardElevation=  3f
            }
        }else{
            binding.apply {
                vFocus.hide()
                cardColor.cardElevation=  0f
            }
        }
    }

    class DiffCallBack : AbsBaseDiffCallBack<SelectedModel>() {
        override fun itemsTheSame(
            oldItem: SelectedModel,
            newItem: SelectedModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun contentsTheSame(
            oldItem: SelectedModel,
            newItem: SelectedModel
        ): Boolean {
            return oldItem.path != newItem.path || oldItem.isSelected != newItem.isSelected
        }

    }
}