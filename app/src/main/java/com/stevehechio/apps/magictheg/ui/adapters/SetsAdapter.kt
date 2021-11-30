package com.stevehechio.apps.magictheg.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.stevehechio.apps.magictheg.R
import com.stevehechio.apps.magictheg.data.local.entities.SetsEntity
import com.stevehechio.apps.magictheg.databinding.ItemSetsListBinding
import java.util.*
import android.content.Context


/**
 * Created by stevehechio on 11/28/21
 */
class SetsAdapter(val context: Context) : PagingDataAdapter<SetsEntity, SetsAdapter.SetsViewHolder>(SET_COMPARATOR) {
    private var lastPosition = -1
    var onClickLikedListener :OnClickItemListener? = null

    fun setOnClickedItem(onClickLikedListener :OnClickItemListener){
        this.onClickLikedListener = onClickLikedListener
    }

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        getItem(position)?.let { holder.bindViews(it) }
        setAnimation(holder.itemView,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        return SetsViewHolder(ItemSetsListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration =
                Random().nextInt(501).toLong() //to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim)
            lastPosition = position
        }
    }

    inner class SetsViewHolder(private val binding: ItemSetsListBinding):
        RecyclerView.ViewHolder(binding.root){

        private val images = listOf(R.drawable.ic_dia_cards,R.drawable.ic_drop_card,
            R.drawable.ic_drop_dia_card,R.drawable.ic_show_data_cards)
        fun bindViews(setsEntity: SetsEntity){
            val androidColors: IntArray = context.resources.getIntArray(R.array.cardColors)
            val randomColor = androidColors[Random().nextInt(androidColors.size)]

            binding.cvSet.setCardBackgroundColor(randomColor)
            binding.cvSet2.strokeColor = randomColor
            binding.tvBooster.setBackgroundColor(randomColor)
            binding.ivSet.setImageResource(images.random())
            binding.tvCode.text = setsEntity.code
            binding.tvName.text = setsEntity.name
            binding.tvType.text = setsEntity.type
            binding.cvBooster.setOnClickListener { onClickLikedListener?.onItemClicked(
                true,setsEntity.code, setsEntity.name) }
            binding.root.setOnClickListener { onClickLikedListener?.onItemClicked(
                false,setsEntity.code, setsEntity.name) }
        }
    }

    companion object {
        private val SET_COMPARATOR = object : DiffUtil.ItemCallback<SetsEntity>(){
            override fun areItemsTheSame(oldItem: SetsEntity, newItem: SetsEntity): Boolean {
                return oldItem.code == newItem.code
            }

            override fun areContentsTheSame(oldItem: SetsEntity, newItem: SetsEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnClickItemListener{
        fun onItemClicked(isBooster: Boolean, code: String, name: String)
    }
}