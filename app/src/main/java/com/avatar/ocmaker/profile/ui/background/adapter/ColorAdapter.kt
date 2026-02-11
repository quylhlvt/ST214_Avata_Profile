package com.avatar.ocmaker.profile.ui.background.adapter

import androidx.recyclerview.widget.RecyclerView
import com.avatar.ocmaker.profile.base.AbsBaseAdapter
import com.avatar.ocmaker.profile.base.AbsBaseDiffCallBack
import com.avatar.ocmaker.profile.data.model.SelectedModel
import com.avatar.ocmaker.profile.utils.hide
import com.avatar.ocmaker.profile.utils.onSingleClick
import com.avatar.ocmaker.profile.utils.show
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.ItemColorBgBinding

class ColorAdapter :
    AbsBaseAdapter<SelectedModel, ItemColorBgBinding>(R.layout.item_color_bg, DiffCallBack()) {
    var onClick: ((Int) -> Unit)? = null
    var posSelect = -1
    override fun bind(
        binding: ItemColorBgBinding,
        position: Int,
        data: SelectedModel,
        holder: RecyclerView.ViewHolder
    ) {
        binding.imvColor.onSingleClick {
            onClick?.invoke(position)
        }
        if(position==0){
            binding.imvColor.setBackgroundResource(R.drawable.imv_add_color)
        }else{
            binding.imvColor.setBackgroundColor(data.color)
        }
        if (data.isSelected) {
            binding.vFocus1.show()
        } else {
            binding.vFocus1.hide()
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