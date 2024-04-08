package com.diagnal.diagnalprject.ui.view

import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diagnal.diagnalprject.R
import com.diagnal.diagnalprject.repository.ContentListRepo
import com.diagnal.diagnalprject.ui.adapter.ContentListAdapter
import com.diagnal.diagnalprject.viewmodel.ContentListViewModal


class MainActivity : AppCompatActivity() {
    private lateinit var rvContent: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var contentListAdapter: ContentListAdapter
    private lateinit var contentListViewModal: ContentListViewModal
    private lateinit var layoutManager: GridLayoutManager
    private var isLoading = false
    private var currentPage = 1 // Track current page number
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar= findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        fetchIdAndData()
    }

    private fun fetchIdAndData() {
        rvContent = findViewById(R.id.rvContent)
        progressBar = findViewById(R.id.progressBar)
        layoutManager = GridLayoutManager(this, 3)
        rvContent.layoutManager = layoutManager
        val repository = ContentListRepo(this)
        contentListViewModal = ViewModelProvider(this).get(ContentListViewModal::class.java)
        contentListViewModal.init(repository)

        contentListAdapter = ContentListAdapter(this)
        rvContent.adapter = contentListAdapter
        // Pagination Logic
        fetchData(currentPage)
        setUpPagination()
    }

    private fun fetchData(currentPage: Int) {
        contentListViewModal.myData.observe(this, Observer { data ->
            if (data != null) {
                progressBar.visibility = View.VISIBLE
                rvContent.postDelayed({
                    contentListAdapter.addData(data.page.contentItems.content);
                    isLoading = false
                    progressBar.visibility = View.GONE
                }, 1000) // Simulating delay of 1 second
            }
        })

        contentListViewModal.loadData(currentPage)
    }


    private fun setUpPagination() {
        rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (!isLoading && !isLoading) {
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        loadMoreItems()
                    }
                }
            }
        })
    }

    private fun loadMoreItems() {
        isLoading = true
        currentPage++
        fetchData(currentPage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val searchItem = menu!!.findItem(R.id.app_bar_search)
        val searchView: SearchView? = searchItem.actionView as SearchView?
        searchView?.setOnSearchClickListener {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }
        searchView?.setOnCloseListener {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            false
        }
        searchView?.queryHint = Html.fromHtml("<font color = #ffffff>" + resources.getString(R.string.hintSearchMess) + "</font>");
        searchView?.maxWidth = Integer.MAX_VALUE;
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                contentListAdapter.filter.filter(newText)
                return false
            }
        })

        return true

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button click
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }



}