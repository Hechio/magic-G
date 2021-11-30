package com.stevehechio.apps.magictheg.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.stevehechio.apps.magictheg.R
import com.stevehechio.apps.magictheg.data.Resource
import com.stevehechio.apps.magictheg.data.local.entities.CardsBoosterEntity
import com.stevehechio.apps.magictheg.data.local.entities.CardsEntity
import com.stevehechio.apps.magictheg.databinding.FragmentCardsBinding
import com.stevehechio.apps.magictheg.ui.adapters.BoosterCardsAdapter
import com.stevehechio.apps.magictheg.ui.adapters.CardsAdapter
import com.stevehechio.apps.magictheg.ui.adapters.SetsAdapter
import com.stevehechio.apps.magictheg.ui.viewmodels.CardsViewModel
import com.stevehechio.apps.magictheg.utils.gone
import com.stevehechio.apps.magictheg.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class CardsFragment : Fragment() {
    private var _binding: FragmentCardsBinding? = null
    private val binding get() = _binding!!

    private var isBooster: Boolean? = null
    private var setCode: String? = null
    private var setName: String? = null

    private lateinit var mAdapter: CardsAdapter
    private lateinit var mAdapter2: BoosterCardsAdapter
    private lateinit var viewModel: CardsViewModel


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
        isBooster = arguments?.getBoolean(IS_BOOSTER) ?: false
        setName = arguments?.getString(SET_NAME)
        setUpViews()

    }

    private fun setUpViews() {
        val title = if (isBooster == true){
            "Booster: $setName"
        }else {
            setName ?: "Cards in The Set"
        }
        binding.tvName.text = title
        mAdapter = CardsAdapter(requireContext())
        mAdapter2 = BoosterCardsAdapter(requireContext())
        viewModel = ViewModelProvider(this).get(CardsViewModel::class.java)
        binding.rv.apply {
            adapter = if (isBooster == true) mAdapter2 else mAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
        mAdapter.setOnClickedItem(object : CardsAdapter.OnClickItemListener{
            override fun onItemClicked(color: Int, color40: Int, cardsEntity: CardsEntity) {
                val bundle = Bundle()
                bundle.putInt(CardDetailsFragment.CARD_COLOR,color)
                bundle.putInt(CardDetailsFragment.CARD_COLOR40,color40)
                bundle.putBoolean(CardDetailsFragment.IS_BOOSTER,false)
                bundle.putSerializable(CardDetailsFragment.CARD_ENTITY,cardsEntity)
                findNavController().navigate(R.id.cardDetailsFragment,bundle)
            }

        })
        mAdapter2.setOnClickedItem(object : BoosterCardsAdapter.OnClickItemListener{
            override fun onItemClicked(color: Int, color40: Int, cardsEntity: CardsBoosterEntity) {
                val bundle = Bundle()
                bundle.putInt(CardDetailsFragment.CARD_COLOR,color)
                bundle.putInt(CardDetailsFragment.CARD_COLOR40,color40)
                bundle.putBoolean(CardDetailsFragment.IS_BOOSTER,true)
                bundle.putSerializable(CardDetailsFragment.CARD_ENTITY,cardsEntity)
                findNavController().navigate(R.id.cardDetailsFragment,bundle)
            }

        })
        if (setCode == null)return
        fetchCardsInSet(setCode!!,isBooster ?: false)
    }

    @SuppressLint("SetTextI18n")
    fun fetchCardsInSet(setCode: String, isBooster: Boolean){
        if (isBooster){
            loadBoosterCards(setCode)
        } else{
            lifecycleScope.launch {
                viewModel.fetchMagicCards(setCode).distinctUntilChanged().collectLatest {
                    Log.d("CardsFragment", "fetch cards : $it")
                    mAdapter.submitData(it)
                }
            }

            lifecycleScope.launch {
                mAdapter.loadStateFlow.collect { loadState ->
                    binding.rv.isVisible =  loadState.source.refresh is LoadState.NotLoading ||
                            loadState.mediator?.refresh is LoadState.NotLoading
                    binding.pb.isVisible = loadState.mediator?.refresh is LoadState.Loading

                    val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                    errorState?.let {
                        binding.tvError.text = "\uD83D\uDE28 Wooops ${it.error}"
                    }
                }
            }
        }


    }

    private fun loadBoosterCards(setCode: String){
        viewModel.getBoosterLiveData().observe(viewLifecycleOwner, { response ->
            when(response){
                is Resource.Success ->{
                    mAdapter2.submitList(response.data)
                    stopLoadingWithSucess()
                }
                is Resource.Failure -> {
                    val error = response.cause
                    binding.tvError.text = error
                    stopLoadingWithError()
                }
                is Resource.Loading -> {
                    if (mAdapter2.itemCount < 1){
                        startLoading()
                    }
                }
                else -> {
                    binding.tvError.text = getString(R.string.something_went_wrong)
                    stopLoadingWithError()
                }
            }

        })
        viewModel.fetchBoosterCards(setCode)
    }

    private fun stopLoadingWithSucess() {
        binding.rv.visible()
        binding.pb.gone()
        binding.tvError.gone()
    }
    private fun stopLoadingWithError() {
        binding.rv.gone()
        binding.pb.gone()
        binding.tvError.visible()
    }

    private fun startLoading() {
        binding.rv.gone()
        binding.pb.visible()
        binding.tvError.gone()
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
       const val IS_BOOSTER = "Hechio.IsBooster"
       const val SET_CODE = "Hechio.SetCode"
       const val SET_NAME = "Hechio.SetName"
    }
}