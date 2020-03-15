package com.kendhammer.john.urbandictionarykotlin.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kendhammer.john.urbandictionarykotlin.R
import com.kendhammer.john.urbandictionarykotlin.viewmodel.DescriptionListViewModel

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {
    private lateinit var viewModel: DescriptionListViewModel
    private val wordsAdapter = DecriptionListAdapter(arrayListOf())
    private lateinit var rvSearchResults: RecyclerView
    private lateinit var etQuery: EditText
    private lateinit var tvListError: TextView
    private lateinit var pbLoadingView: ProgressBar
    private lateinit var btnSearch: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        //Create ViewModel
        viewModel = ViewModelProviders.of(this).get(DescriptionListViewModel::class.java)
        //Set the views of the Fragment
        setView(view)

        //Set adapter for RecyclerView
        rvSearchResults.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wordsAdapter
        }

        //Observe Changes in Data
        observeViewModel()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSearch = view.findViewById(R.id.btn_search)
        btnSearch.setOnClickListener {
            val query = etQuery.text
            if (query != null) {
                viewModel.refresh(query.toString())
                etQuery.text = null
                hideSoftKeyboard(view)
            }
        }
    }

    private fun setView(view: View) {
        rvSearchResults = view.findViewById(R.id.rv_searchResults)
        etQuery = view.findViewById(R.id.et_query)
        pbLoadingView = view.findViewById(R.id.pb_loading_view)
        tvListError = view.findViewById(R.id.tv_list_error)
        tvListError.visibility = View.GONE
        pbLoadingView.visibility = View.GONE

    }

    private fun observeViewModel() {
        viewModel.definitions.observe(this, Observer { definitions ->
            definitions?.let {
                rvSearchResults.visibility = View.VISIBLE
                wordsAdapter.updateWordList(it)
            }
        })

        viewModel.wordLoadError.observe(this, Observer { isError ->
            isError?.let { tvListError.visibility = if (it) View.VISIBLE else View.GONE }
        })

        viewModel.loading.observe(this, Observer { isLoading ->
            isLoading?.let {
                pbLoadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    tvListError.visibility = View.GONE
                    rvSearchResults.visibility = View.GONE
                }
            }

        })
    }

    private fun hideSoftKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

