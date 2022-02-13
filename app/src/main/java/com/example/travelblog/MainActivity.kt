package com.example.travelblog

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travelblog.adapter.MainAdapter
import com.example.travelblog.databinding.ActivityMainBinding
import com.example.travelblog.http.Blog
import com.example.travelblog.http.BlogHttpClient
import com.example.travelblog.repository.BlogRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SORT_TITLE = 0
        private const val SORT_DATE = 1
    }

    private var currentSort = SORT_DATE

    private lateinit var binding: ActivityMainBinding
    private val adapter = MainAdapter { blog ->
        BlogDetailsActivity.start(this, blog)
    }

    private val repository by lazy { BlogRepository(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialToolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.sort) {
                onSortClicked()
            }
            false
        }

        val searchItem = binding.materialToolbar.menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                adapter.filter(newText)
                return true
            }
        })

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        binding.refreshLayout.setOnRefreshListener {
            loadDataFromNetwork()
        }

        loadDataFromDatabase()
        loadDataFromNetwork()

//        startActivity(Intent(this, BlogDetailsActivity::class.java))

    }

    private fun loadDataFromNetwork() {
        binding.refreshLayout.isRefreshing = true
        repository.loadDataFromNetwork(
            onSuccess = { blogList: List<Blog> ->
                runOnUiThread {
                    binding.refreshLayout.isRefreshing = false
                    adapter.setData(blogList)
                    sortData()
                }
            },
            onError = {
                runOnUiThread {
                    binding.refreshLayout.isRefreshing = false
                    showErrorSnackbar()
                }

            }
        )
    }

    private fun loadDataFromDatabase() {
        repository.loadDataFromDatabase { blogList: List<Blog> ->
            runOnUiThread {
                adapter.setData(blogList)
                sortData()
            }
        }
    }

    private fun onSortClicked() {
        val items = arrayOf("Title", "Date")
        MaterialAlertDialogBuilder(this)
            .setTitle("Sort order")
            .setSingleChoiceItems(items, currentSort) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                currentSort = which
                sortData()
            }.show()
    }

    private fun sortData() {
        if (currentSort == SORT_TITLE) {
            adapter.sortByTitle()
        } else if (currentSort == SORT_DATE) {
            adapter.sortByDate()
        }
    }


    private fun showErrorSnackbar() {
        Snackbar.make(
            binding.rootView,
            "Error during loading blog articles", Snackbar.LENGTH_INDEFINITE
        ).run {
            setActionTextColor(ContextCompat.getColor(context, R.color.pink_1))
            setAction("Retry") {
                loadDataFromNetwork()
                dismiss()
            }
        }.show()
    }
}