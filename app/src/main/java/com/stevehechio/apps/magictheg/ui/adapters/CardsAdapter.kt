package com.stevehechio.apps.magictheg.ui.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stevehechio.apps.magictheg.R
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.databinding.ItemCardsListBinding
import com.stevehechio.apps.magictheg.utils.GetColorNumber
import com.stevehechio.apps.magictheg.utils.gone
import java.util.*

/**
 * Created by stevehechio on 11/28/21
 */
class CardsAdapter(val context: Context): PagingDataAdapter<CardsEntity,CardsAdapter.CardsViewHolder>(
    CARD_COMPARATOR) {
    private var lastPosition = -1
    var onClickLikedListener : OnClickItemListener? = null

    fun setOnClickedItem(onClickLikedListener : OnClickItemListener){
        this.onClickLikedListener = onClickLikedListener
    }

    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        getItem(position)?.let { holder.bindViews(it) }
        setAnimation(holder.itemView,position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        return CardsViewHolder(ItemCardsListBinding
            .inflate(LayoutInflater.from(parent.context),parent,false))
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
    inner class CardsViewHolder(val binding: ItemCardsListBinding):
        RecyclerView.ViewHolder(binding.root){
        private val mColors = listOf(
            R.color.red, R.color.blue,R.color.green, R.color.black, R.color.white)
        private val mColors40 = listOf(
            R.color.red_40, R.color.blue_40,R.color.green_40, R.color.black_40, R.color.white_40)
            fun bindViews(cardsEntity: CardsEntity){
                val mUrl = cardsEntity.imageUrl
                    ?: "https://firebasestorage.googleapis.com/v0/b/nurture-bfc09.appspot.com/o/others%2Funknown.png?alt=media&token=000c7a7e-8a10-4f88-830e-1402af969d8a"
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
                binding.root.setOnClickListener { onClickLikedListener?.onItemClicked(mColors[i],mColors40[i],cardsEntity) }
            }
        }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<CardsEntity>(){
            override fun areItemsTheSame(oldItem: CardsEntity, newItem: CardsEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CardsEntity, newItem: CardsEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface OnClickItemListener{
        fun onItemClicked(color: Int, color40: Int, cardsEntity: CardsEntity)
    }

}