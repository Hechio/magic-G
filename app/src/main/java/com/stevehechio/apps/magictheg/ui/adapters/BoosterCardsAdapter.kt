package com.stevehechio.apps.magictheg.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stevehechio.apps.magictheg.R
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.databinding.ItemCardsListBinding
import com.stevehechio.apps.magictheg.utils.AppConstants
import com.stevehechio.apps.magictheg.utils.GetColorNumber
import com.stevehechio.apps.magictheg.utils.gone
import java.util.*

/**
 * Created by stevehechio on 11/29/21
 */
class BoosterCardsAdapter(val context: Context):ListAdapter<CardsBoosterEntity,BoosterCardsAdapter.BoosterCardViewHolder>(
    CARD_BOOSTER_COMPARATOR) {

    private var lastPosition = -1
    var onClickLikedListener : OnClickItemListener? = null

    fun setOnClickedItem(onClickLikedListener : OnClickItemListener){
        this.onClickLikedListener = onClickLikedListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoosterCardViewHolder {
        return BoosterCardViewHolder(ItemCardsListBinding
            .inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: BoosterCardViewHolder, position: Int) {
        getItem(position)?.let { holder.bindViews(it) }
        setAnimation(holder.itemView,position)
    }
    private fun setAnimation(viewToAnimate: View, position: Int) {
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
    inner class BoosterCardViewHolder(val binding: ItemCardsListBinding): RecyclerView.ViewHolder(binding.root){


        fun bindViews(cardsEntity: CardsBoosterEntity){
            val mColors: IntArray = context.resources.getIntArray(R.array.mColors)
            val mColors40: IntArray = context.resources.getIntArray(R.array.mColors40)
            val mUrl = cardsEntity.imageUrl
                ?: AppConstants.defaultUrl
            Glide.with(context)
                .load(mUrl)
                .centerCrop()
                .dontAnimate()
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pb.gone()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.pb.gone()
                        return false
                    }
                })
                .into(binding.ivCard)

            val color = cardsEntity.colors?.get(0) ?: "Black"
            val i = GetColorNumber.getColorNumber(color)
            binding.cvBack.setCardBackgroundColor(mColors40[i])
            binding.cvColor.setCardBackgroundColor(mColors[i])
            binding.tvName.text = cardsEntity.name
            binding.tvRarity.text = cardsEntity.rarity
            binding.tvType.text = cardsEntity.type
            binding.root.setOnClickListener { onClickLikedListener?.onItemClicked(mColors[i],mColors40[i],cardsEntity = cardsEntity) }
        }
    }
    companion object {
        private val CARD_BOOSTER_COMPARATOR = object : DiffUtil.ItemCallback<CardsBoosterEntity>(){
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: CardsBoosterEntity,
                newItem: CardsBoosterEntity
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: CardsBoosterEntity,
                newItem: CardsBoosterEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }


        }
    }

    interface OnClickItemListener{
        fun onItemClicked(color: Int, color40: Int, cardsEntity: CardsBoosterEntity)
    }


}