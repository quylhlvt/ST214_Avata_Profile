package com.avatar.ocmaker.profile.ui.customview

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.avatar.ocmaker.profile.base.AbsBaseAdapter
import com.avatar.ocmaker.profile.base.AbsBaseDiffCallBack
import com.avatar.ocmaker.profile.data.model.BodyPartModel
import com.avatar.ocmaker.profile.utils.onClickCustom
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.ItemNavigationBinding

class NavAdapter(context: Context) : AbsBaseAdapter<BodyPartModel, ItemNavigationBinding>(R.layout.item_navigation, DiffNav()) {
    val ct= context
    var posNav = 0
    var onClick: ((Int) -> Unit)? = null

    class DiffNav : AbsBaseDiffCallBack<BodyPartModel>() {
        override fun itemsTheSame(oldItem: BodyPartModel, newItem: BodyPartModel): Boolean {
            return oldItem.icon == newItem.icon
        }

        override fun contentsTheSame(oldItem: BodyPartModel, newItem: BodyPartModel): Boolean {
            return oldItem.icon != newItem.icon
        }

    }

    fun setPos(pos: Int) {
        posNav = pos
    }

    override fun bind(
        binding: ItemNavigationBinding,
        position: Int,
        data: BodyPartModel,
        holder: RecyclerView.ViewHolder
    ) {
        Glide.with(binding.root).load(data.icon).encodeQuality(90).override(256).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(binding.imv)
        if (posNav == position) {
            binding.bg.strokeColor = ContextCompat.getColor(
                binding.root.context,
                R.color.app_color2
            )
        } else {
            binding.bg.strokeColor = ContextCompat.getColor(
                binding.root.context,
                R.color.white
            )
        }

        binding.root.onClickCustom {
            onClick?.invoke(position)
        }
    }

}