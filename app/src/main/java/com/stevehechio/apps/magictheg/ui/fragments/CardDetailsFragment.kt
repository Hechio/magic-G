package com.stevehechio.apps.magictheg.ui.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.stevehechio.apps.magictheg.R
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.databinding.FragmentCardDetailsBinding
import com.stevehechio.apps.magictheg.utils.AppConstants
import com.stevehechio.apps.magictheg.utils.gone

class CardDetailsFragment : Fragment() {
private var _binding: FragmentCardDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardDetailsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CardsDetailsFragment", "onViewCreated: $arguments")
        arguments?.let {
            binding.clMain.setBackgroundColor(it.getInt(CARD_COLOR40))
            val isBooter = it.getBoolean(IS_BOOSTER)
           if (isBooter){
                val entity =  it.getSerializable(CARD_ENTITY) as CardsBoosterEntity
               binding.tvName.text = entity.name
               binding.tvColor.text = entity.colors?.get(0) ?: "UnKnown Color"
               binding.tvRarity.text = entity.rarity ?: "Undefine card rarity"
               binding.tvType.text = entity.text ?: "Undefine card type"
               val mUrl = entity.imageUrl
                   ?: AppConstants.defaultUrl
               loadImage(mUrl)
               binding.tvTextDes.text = entity.text ?: "Undefine card text"
               binding.tvFlavorDes.text = entity.flavor ?: "Undefine card flavor"
            }else {
               val entity =  it.getSerializable(CARD_ENTITY) as CardsEntity
               binding.tvName.text = entity.name
               binding.tvColor.text = entity.colors?.get(0) ?: "UnKnown color"
               binding.tvRarity.text = entity.rarity ?: "Undefine card rarity"
               binding.tvType.text = entity.text ?: "Undefine card type"
               val mUrl = entity.imageUrl
                   ?: AppConstants.defaultUrl
               loadImage(mUrl)
               binding.tvTextDes.text = entity.text  ?: "Undefine card text"
               binding.tvFlavorDes.text = entity.flavor ?: "Undefine card flavor"
            }

        }

    }

    private fun loadImage(mUrl: String) {
        Glide.with(requireContext())
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
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        //color: Int, color40: Int, cardsEntity: CardsEntity
        const val CARD_COLOR = "CARD_COLOR.CardDetailsFragment"
        const val IS_BOOSTER = "IS_BOOSTER.CardDetailsFragment"
        const val CARD_COLOR40 = "CARD_COLOR40.CardDetailsFragment"
        const val CARD_ENTITY = "CARD_ENTITY.CardDetailsFragment"
    }
}