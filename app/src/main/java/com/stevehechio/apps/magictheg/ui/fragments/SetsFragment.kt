package com.stevehechio.apps.magictheg.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
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
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.stevehechio.apps.magictheg.R
import com.stevehechio.apps.magictheg.data.RemotePresentationState
import com.stevehechio.apps.magictheg.data.asRemotePresentationState
import com.stevehechio.apps.magictheg.databinding.FragmentSetsBinding
import com.stevehechio.apps.magictheg.ui.adapters.SetsAdapter
import com.stevehechio.apps.magictheg.ui.viewmodels.SetsViewModel
import com.stevehechio.apps.magictheg.utils.gone
import com.stevehechio.apps.magictheg.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SetsFragment : Fragment() {
    private var _binding: FragmentSetsBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: SetsViewModel
    lateinit var mAdapter: SetsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        fetchMagicSets()
    }

    private fun setUpViews() {
        mAdapter = SetsAdapter()
        viewModel = ViewModelProvider(this).get(SetsViewModel::class.java)
        binding.rv.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(),2)
        }
        mAdapter.setOnClickedItem(object : SetsAdapter.OnClickItemListener{
            override fun onItemClicked(isBooster: Boolean, code: String, name: String) {
                val bundle = Bundle()
                bundle.putBoolean(CardsFragment.IS_BOOSTER,isBooster)
                bundle.putString(CardsFragment.SET_CODE,code)
                bundle.putString(CardsFragment.SET_NAME,name)
                findNavController().navigate(R.id.cardsFragment,bundle)
            }

        })
    }


    @SuppressLint("SetTextI18n")
    private fun fetchMagicSets(){
        lifecycleScope.launch {
            viewModel.fetchMagicSets().distinctUntilChanged().collectLatest {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}