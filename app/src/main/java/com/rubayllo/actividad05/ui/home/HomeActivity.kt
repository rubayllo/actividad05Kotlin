package com.rubayllo.actividad05.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rubayllo.actividad05.data.network.DealsApi
import com.rubayllo.actividad05.data.network.model.DealsModel
import com.rubayllo.actividad05.databinding.ActivityHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityHomeBinding
    private val binding: ActivityHomeBinding get() = _binding

    private val adapter = HomeRVAdapter()

    // ViewModel
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRecyclerView()
        observeViewModelStates()

        val storeId = 1

        viewModel.getInfoDeals(storeId)

    }

    private fun observeViewModelStates() {
        lifecycleScope.launch {
            viewModel.homeUIState.collect { state ->
                if (state.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }

                if (state.gameList != null) {
                    adapter.submitList(state.gameList)
                }

                if (state.errorMessage != null) {
                    Snackbar.make(binding.root, state.errorMessage, Snackbar.LENGTH_LONG).show()
                }

            }
        }
    }


    private fun setRecyclerView() {
        binding.rvDeals.layoutManager = LinearLayoutManager(this)
        binding.rvDeals.adapter = adapter
    }
}