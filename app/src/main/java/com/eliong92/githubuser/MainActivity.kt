package com.eliong92.githubuser

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eliong92.githubuser.adapter.UserAdapter
import com.eliong92.githubuser.databinding.ActivityMainBinding
import com.eliong92.githubuser.viewmodel.MainViewModel
import com.eliong92.githubuser.viewmodel.MainViewModelProvider
import com.eliong92.githubuser.viewmodel.MainViewState
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModelProvider: MainViewModelProvider
    private lateinit var viewModel: MainViewModel

    private lateinit var binding: ActivityMainBinding

    private val adapter = UserAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, mainViewModelProvider).get(MainViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setupRecyclerView()
        observeState()
    }

    private fun observeState() {
        viewModel.state.observe(this, { state ->
            when (state) {
                is MainViewState.OnSuccess -> {
                    adapter.refresh(state.users)
                }

                is MainViewState.OnError -> {
                    showToast(
                        text = "Something went wrong. Please try again!",
                        length = Toast.LENGTH_SHORT
                    )
                }

                is MainViewState.OnUserNotFound -> {
                    showToast(
                        text = "User not found. Please try another query!",
                        length = Toast.LENGTH_LONG
                    )
                }
            }
        })
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                (recyclerView.layoutManager as? LinearLayoutManager)?.let {
                    val itemCount = recyclerView.adapter?.itemCount ?: 0
                    if(it.findLastCompletelyVisibleItemPosition() == itemCount - 1) {
                        viewModel.onLoadMore()
                    }
                }
            }
        })
    }

    private fun showToast(
        text: String,
        length: Int
    ) {
        Toast.makeText(this, text, length).show()
    }
}