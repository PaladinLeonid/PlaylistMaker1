package com.example.playlistmaker1


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    private lateinit var searchField: EditText
    private lateinit var noInternet: LinearLayout
    private var searchText: String? = null
    private var searchQuery: String = ""
    private var lastQuery: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var noSong: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var adapter: TrackAdapter

    private lateinit var searchHistory: SearchHistory
    private lateinit var yourSearch: TextView
    private lateinit var clearHistory: Button
    @SuppressLint("ServiceCast", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchField = findViewById(R.id.search_edit_field)
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        val clearIcon = findViewById<ImageView>(R.id.search_clear_btn)
        hideKeyboard(window.decorView.rootView)
        noSong = findViewById(R.id.no_song)
        noInternet = findViewById(R.id.no_internet)
        recyclerView = findViewById(R.id.trackList)


        recyclerView.layoutManager = LinearLayoutManager(this)
        supportActionBar?.hide()

        searchField.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("SearchActivity", "Search query: $searchQuery")
                performSearch(searchQuery) { trackFound ->
                    if (trackFound) {
                        noSong.visibility = View.GONE
                    } else {
                        noSong.visibility = View.VISIBLE
                    }
                }
                true
            } else {
                false
            }

        }

        updateButton = findViewById(R.id.update_button)
        updateButton.setOnClickListener {
            lastQuery?.let { query ->
                performSearch(query) { trackFound ->
                    if (trackFound) {
                        noSong.visibility = View.GONE
                    } else {
                        noSong.visibility = View.VISIBLE
                    }
                }
            }
        }
        backButton.setOnClickListener {
            finish()
        }
        clearIcon.setOnClickListener {
            searchField.text.clear()
            searchField.clearFocus()
            adapter.updateTracks(emptyList())
            noInternet.visibility = View.GONE
            noSong.visibility = View.GONE
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
        yourSearch = findViewById(R.id.yourSearch)
        clearHistory = findViewById(R.id.clean_history_button)

        adapter = TrackAdapter(mutableListOf()) { track: Track->
            searchHistory.saveToHistory(track)
            if (searchQuery.isEmpty()) {
                adapter.updateTracks(searchHistory.getSearchHistory())
            }
        }
            searchHistory = SearchHistory(getSharedPreferences("SEARCH_HISTORY", MODE_PRIVATE))
        val historyTracks = searchHistory.getSearchHistory()
        if (historyTracks.isNotEmpty()) {
            adapter.updateTracks(historyTracks)
            yourSearch.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            noSong.visibility = View.GONE
            clearHistory.visibility = View.VISIBLE
        } else {
            noSong.visibility = View.GONE
            recyclerView.visibility = View.GONE
            yourSearch.visibility = View.GONE
            clearHistory.visibility = View.GONE
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        clearHistory.setOnClickListener {
            searchHistory.clearHistory()
            yourSearch.visibility = View.GONE
            clearHistory.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }


        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchQuery = s.toString()
                clearButtonVisibility(s, clearIcon)
                searchText = s.toString()
            }


            override fun afterTextChanged(s: Editable?) {
                clearHistory.visibility = View.GONE
                clearHistory.setOnClickListener {
                    hideKeyboard(window.decorView.rootView)
                    searchField.text.clear()
                    clearIcon.visibility = View.GONE
                    yourSearch.visibility = View.VISIBLE
                    adapter.updateTracks(searchHistory.getSearchHistory())
                    recyclerView.visibility = View.VISIBLE
                    clearHistory.visibility = View.VISIBLE
                    searchField.requestFocus()
                }
            }

        }
        searchField.addTextChangedListener(simpleTextWatcher)

    }

    private fun clearButtonVisibility(s: CharSequence?, clearIcon: ImageView?) {
        val clearIcon = findViewById<ImageView>(R.id.search_clear_btn)
            clearIcon.isVisible = !s.isNullOrEmpty()
        }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchField.setText(savedInstanceState.getString(EXTRA_SEARCH_TEXT, ""))
    }


    companion object {
        const val EXTRA_SEARCH_TEXT = "searchText"
    }


    private fun performSearch(query: String, callback: (Boolean) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.iTunes_link))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ItunesApi::class.java)
        val call = api.search(query)
        noInternet = findViewById(R.id.no_internet)
        noSong = findViewById(R.id.no_song)
        recyclerView = findViewById(R.id.trackList)
        noSong.visibility = View.GONE
        call.enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                noInternet.visibility = View.GONE
                if (response.isSuccessful) {
                    val trackResponse = response.body()
                    Log.d("SearchActivity", "Response Body: ${trackResponse.toString()}")
                    val tracks = trackResponse?.results ?: emptyList()
                    Log.d("SearchActivity", "Tracks found: ${tracks.size}")
                    if (tracks.isNotEmpty()) {
                        adapter.updateTracks(tracks)
                        recyclerView.visibility = View.VISIBLE
                        noSong.visibility = View.GONE
                        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(searchField.windowToken, 0)
                        callback(true)
                    } else {
                        recyclerView.visibility = View.GONE
                        noSong.visibility = View.VISIBLE
                        callback(false)
                    }
                } else {
                    Log.e("SearchActivity", "Response Error: ${response.code()} - ${response.errorBody()?.string()}")
                    recyclerView.visibility = View.GONE
                    noSong.visibility = View.VISIBLE
                    callback(false)
                }

            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                Log.e("SearchActivity", "Network Error: ${t.message}")
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(searchField.windowToken, 0)
                noSong.visibility = View.GONE
                noInternet.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                lastQuery = searchQuery
                callback(true)
            }
        })
    }
}
