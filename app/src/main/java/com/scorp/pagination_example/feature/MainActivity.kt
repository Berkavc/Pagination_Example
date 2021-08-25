package com.scorp.pagination_example.feature

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.scorp.domain.Person
import com.scorp.pagination_example.databinding.ActivityMainBinding
import com.scorp.pagination_example.utility.RecyclerViewPaginator
import com.scorp.pagination_example.utility.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainActivityViewModel: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var adapterRecyclerViewAdapter: MainActivityRecyclerViewAdapter
    private lateinit var recyclerViewPaginator: RecyclerViewPaginator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.lifecycleOwner = this
        binding.viewModel = mainActivityViewModel

        with(mainActivityViewModel) {
            observe(mutableListPerson, ::onPersonDataGathered)
            observe(isLoading, ::controlProgressBar)
            observe(isErrorState, ::errorState)
            observe(isRefreshClicked, ::refreshClicked)
        }

        mainActivityViewModel.gatherPersonList()

        arrangeUI()

    }

    private fun arrangeUI() {
        arrangeRecyclerView()
    }

    private fun arrangeRecyclerView() {
        adapterRecyclerViewAdapter = MainActivityRecyclerViewAdapter(
            this,
            mainActivityViewModel.mutableListPerson.value ?: mutableListOf()
        )
        with(binding.recyclerViewMainPage) {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = adapterRecyclerViewAdapter
            adapterRecyclerViewAdapter.onItemSelected = { position, item ->
                //Item onclick
            }
            recyclerViewPaginator = RecyclerViewPaginator(
                this,
                {
                    mainActivityViewModel.gatherPersonList()
                },
                { false }
            )
            addOnScrollListener(
                recyclerViewPaginator
            )
        }
    }

    private fun onPersonDataGathered(mutablePersonList: MutableList<Person?>?) {
        mutablePersonList?.let {
            binding.textViewMainPageEmptyDataTitle.visibility = View.GONE
            binding.buttonMainPageRefresh.visibility = View.GONE
            binding.recyclerViewMainPage.visibility = View.GONE
            val filteredList = it.filter { person ->
                person != null
            }
            if (filteredList.isEmpty()) {
                binding.textViewMainPageEmptyDataTitle.visibility = View.VISIBLE
                binding.buttonMainPageRefresh.visibility = View.VISIBLE
            } else {
                binding.textViewMainPageEmptyDataTitle.visibility = View.GONE
                binding.buttonMainPageRefresh.visibility = View.GONE
                binding.recyclerViewMainPage.visibility = View.VISIBLE
                adapterRecyclerViewAdapter.updateDataSource(it)
            }

        }
    }

    private fun controlProgressBar(isVisible: Boolean?) {
        if (isVisible == true) {
            recyclerViewPaginator.isLoading = true
            binding.progressBarMainPage.visibility = View.VISIBLE
        } else if (isVisible == false) {
            recyclerViewPaginator.isLoading = false
            binding.progressBarMainPage.visibility = View.GONE
        }
    }

    private fun errorState(isError: Boolean?) {
        if (isError == true) {
            binding.buttonMainPageRefresh.visibility = View.VISIBLE
            Toast.makeText(this, mainActivityViewModel.errorText, Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshClicked(isClicked: Boolean?) {
        if (isClicked == true) {
            mainActivityViewModel.gatherPersonList()
        }
    }

}