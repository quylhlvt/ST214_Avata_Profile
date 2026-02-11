package com.avatar.ocmaker.profile.ui.my_creation

import androidx.recyclerview.widget.RecyclerView
import com.avatar.ocmaker.profile.base.AbsBaseAdapter
import com.avatar.ocmaker.profile.base.AbsBaseDiffCallBack
import com.avatar.ocmaker.profile.utils.hide
import com.avatar.ocmaker.profile.utils.onSingleClick
import com.avatar.ocmaker.profile.utils.shimmer
import com.avatar.ocmaker.profile.utils.show
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerDrawable
import com.avatar.ocmaker.profile.R
import com.avatar.ocmaker.profile.databinding.ItemMyDesignBinding

class DesignAdapter :
    AbsBaseAdapter<String, ItemMyDesignBinding>(R.layout.item_my_design, DiffCallBack()) {
    var onClick: ((Int, String) -> Unit)? = null
    var arrCheckTick = arrayListOf<Int>()
    var checkLongClick = false
    override fun bind(
        binding: ItemMyDesignBinding,
        position: Int,
        data: String,
        holder: RecyclerView.ViewHolder
    ) {
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(shimmer)
        }

        Glide.with(binding.root).load(data).placeholder(shimmerDrawable).into(binding.imvImage)
        binding.imvImage.onSingleClick {
            onClick?.invoke(position,"item")
        }
        binding.btnDelete.onSingleClick {
            onClick?.invoke(position,"delete")
        }
        binding.imvImage.setOnLongClickListener  {
            onClick?.invoke(position, "longclick")
            true
        }
        binding.btnSelect.onSingleClick {
            onClick?.invoke(position, "tick")
        }

        if(checkLongClick){
            binding.btnSelect.show()
            if (position in arrCheckTick) {
                binding.btnSelect.setImageResource(R.drawable.imv_check_true)
            } else {
                binding.btnSelect.setImageResource(R.drawable.imv_check_false)
            }
            binding.btnDelete.hide()
        }else{
            binding.btnSelect.hide()
            binding.btnDelete.show()
        }
    }

    class DiffCallBack : AbsBaseDiffCallBack<String>() {
        override fun itemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun contentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem != newItem
        }
    }
}