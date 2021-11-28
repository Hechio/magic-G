package com.stevehechio.apps.magictheg.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.stevehechio.apps.magictheg.R
import com.stevehechio.apps.magictheg.databinding.FragmentCardsBinding


class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    private var isBooster: Boolean? = null
    private var setCode: String? = null
    private var setName: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("CardsFragment", "onViewCreated: $arguments")

        setCode = arguments?.getString(SET_CODE)
        isBooster = arguments?.getBoolean(IS_BOOSTER)
        setName = arguments?.getString(SET_NAME)
        setUpViews()

    }

    private fun setUpViews() {
        binding.tvName.text = setName ?: "Cards in The Set"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
       const val IS_BOOSTER = "Hechio.IsBooster"
       const val SET_CODE = "Hechio.SetCode"
       const val SET_NAME = "Hechio.SetName"
    }
}