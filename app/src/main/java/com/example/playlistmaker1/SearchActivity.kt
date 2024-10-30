package com.example.playlistmaker1

import Track
import android.content.Intent
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var searchField: EditText
    private lateinit var buttonClear: MaterialButton
    private lateinit var updateButton: MaterialButton
    private lateinit var focusButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var noSong: LinearLayout
    private lateinit var noInternet: LinearLayout
    private lateinit var adapter: TrackAdapter
    private var searchQuery: String = ""
    private var lastQuery: String? = null
    private lateinit var searchHistory: SearchHistory
    private lateinit var yourSearch: TextView
    private lateinit var historyCleaner: Button


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        hideKeyboard(window.decorView.rootView)
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        searchField = findViewById(R.id.search_edit_field)
        focusButton = findViewById(R.id.focusButton)
        buttonClear = findViewById(R.id.search_clear_btn)
        updateButton = findViewById(R.id.update_button)
        recyclerView = findViewById(R.id.trackList)
        noSong = findViewById(R.id.no_song)
        noInternet = findViewById(R.id.no_internet)
        yourSearch = findViewById(R.id.yourSearch)
        historyCleaner = findViewById(R.id.historyCleaner)
        supportActionBar?.hide()

        backButton.setOnClickListener {
            finish()
        }

        adapter = TrackAdapter(mutableListOf()) { track ->
            openTrack(track)
            searchHistory.saveToHistory(track)
            if (searchQuery.isEmpty())
                adapter.updateTracks(searchHistory.getSearchHistory())
        }
        

        searchHistory = SearchHistory(getSharedPreferences("SEARCH_HISTORY", MODE_PRIVATE))

        val historyTracks = searchHistory.getSearchHistory()

        if (historyTracks.isNotEmpty()) {
            adapter.updateTracks(historyTracks)
            yourSearch.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            noSong.visibility = View.GONE
            historyCleaner.visibility = View.VISIBLE
        } else {
            noSong.visibility = View.GONE
            recyclerView.visibility = View.GONE
            yourSearch.visibility = View.GONE
            historyCleaner.visibility = View.GONE
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        historyCleaner.setOnClickListener {
            searchHistory.clearHistory()
            yourSearch.visibility = View.GONE
            historyCleaner.visibility = View.GONE
            recyclerView.visibility = View.GONE
        }

        searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()

                buttonClear.visibility = if (s.isNullOrEmpty()) {
                    View.GONE
                } else {
                    View.VISIBLE
                }

                if (searchQuery.isEmpty()) {

                    yourSearch.visibility = View.VISIBLE
                    adapter.updateTracks(searchHistory.getSearchHistory())
                    recyclerView.visibility = View.VISIBLE
                    historyCleaner.visibility = View.VISIBLE
                } else {
                    yourSearch.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    historyCleaner.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        buttonClear.visibility = View.GONE
        buttonClear.setOnClickListener {
            hideKeyboard(window.decorView.rootView)
            searchField.text.clear()
            buttonClear.visibility = View.GONE
            yourSearch.visibility = View.VISIBLE
            adapter.updateTracks(searchHistory.getSearchHistory())
            recyclerView.visibility = View.VISIBLE
            historyCleaner.visibility = View.VISIBLE
            noInternet.visibility =View.GONE
            searchField.requestFocus()
        }

        searchField.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Log.d("SearchActivity", "Search query: $searchQuery")
                performSearch(searchQuery) { trackFound ->
                    if (trackFound) {
                        noSong.visibility = View.GONE
                        yourSearch.visibility = View.GONE
                        historyCleaner.visibility = View.GONE
                    } else {
                        noSong.visibility = View.VISIBLE
                    }
                }
                true
            } else {
                false
            }
        }

        focusButton.setOnClickListener {
            searchField.requestFocus()
            showKeyboard(searchField)
        }
        searchField.requestFocus()
        searchField.postDelayed({ showKeyboard(searchField) }, 100)
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        updateButton.setOnClickListener {
            lastQuery?.let { query ->
                noInternet.visibility = View.GONE
                performSearch(query) { trackFound ->
                    if (trackFound) {
                        noSong.visibility = View.GONE
                        yourSearch.visibility = View.GONE
                        historyCleaner.visibility = View.GONE
                    } else {
                        noInternet.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun openTrack(track: Track) {
        val trackIntent = Intent(this, PlayerActivity:: class.java)
        trackIntent.putExtra("track",track)
        startActivity(trackIntent)
    }

    private fun performSearch(query: String, callback: (Boolean) -> Unit) {

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.iTunes_link))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ItunesApi::class.java)

        val call = api.search(query)

        call.enqueue(object : Callback<TrackResponse> {

            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                noInternet.visibility = View.GONE

                if (response.isSuccessful) {

                    val trackResponse = response.body()

                    val tracks = trackResponse?.results ?: emptyList()


                    if (tracks.isNotEmpty()) {
                        adapter.updateTracks(tracks)
                        recyclerView.visibility = View.VISIBLE
                        hideKeyboard(window.decorView.rootView)
                        noSong.visibility = View.GONE
                        yourSearch.visibility = View.GONE
                        historyCleaner.visibility = View.GONE
                        callback(true)
                    } else {
                        recyclerView.visibility = View.GONE
                        noSong.visibility = View.VISIBLE
                        callback(false)
                    }

                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                noInternet.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                hideKeyboard(window.decorView.rootView)
                noSong.visibility = View.GONE
                yourSearch.visibility = View.GONE
                historyCleaner.visibility = View.GONE
                lastQuery = searchQuery
                callback(true)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, searchQuery)
        outState.putBoolean(IS_CLEAR_BUTTON_VISIBLE_KEY, buttonClear.visibility == View.VISIBLE)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            searchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY, "")
            searchField.setText(searchQuery)
            searchField.setSelection(searchQuery.length)
        }
    }
    private fun showKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreState(savedInstanceState)
    }

    companion object {
        private const val SEARCH_QUERY_KEY = "search_query"
        private const val IS_CLEAR_BUTTON_VISIBLE_KEY = "isClearButtonVisible"
    }

}