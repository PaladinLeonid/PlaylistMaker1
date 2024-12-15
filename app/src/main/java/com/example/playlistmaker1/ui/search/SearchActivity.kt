package com.example.playlistmaker1.ui.search

import TrackAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.playlistmaker1.Constants.CLICK_DEBOUNCE_DELAY
import com.example.playlistmaker1.Constants.SEARCH_DEBOUNCE_DELAY
import com.example.playlistmaker1.Creator
import com.example.playlistmaker1.NetworkUtil
import com.example.playlistmaker1.NetworkUtil.isConnected
import com.example.playlistmaker1.R
import com.example.playlistmaker1.domain.api.SearchHistoryManage
import com.example.playlistmaker1.domain.implement.SearchTrackInteractImplement
import com.example.playlistmaker1.ui.player.PlayerActivity
import com.google.android.material.button.MaterialButton


class SearchActivity : AppCompatActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var editTextClearBtn: Button
    private lateinit var updateInternet: MaterialButton
    private var searchQuery: String = ""
    private var lastQuery: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TrackAdapter
    private lateinit var noSong: View
    private lateinit var noInternet: View
    private lateinit var historyCleaner: Button
    private lateinit var youSearch: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchTrackInteractImplement: SearchTrackInteractImplement
    private lateinit var searchHistoryManage: SearchHistoryManage


    private val handler = Handler(Looper.getMainLooper())


    @SuppressLint("ResourceType", "MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var isClickable = true
        val handler = Handler(Looper.getMainLooper())
        searchTrackInteractImplement = Creator.createSearchTracksUseCase()


        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        searchEditText = findViewById(R.id.search_edit_text)
        searchEditText.setText(searchQuery)
        editTextClearBtn = findViewById(R.id.et_clear_btn)
        noSong = findViewById(R.id.no_song)
        noInternet = findViewById(R.id.no_internet)
        updateInternet = findViewById(R.id.update_internet_btn)
        historyCleaner = findViewById(R.id.historyCleaner)
        youSearch = findViewById(R.id.yourSearch)
        progressBar = findViewById(R.id.progressBar)


        backButton.setOnClickListener {
            finish()
        }
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        searchHistoryManage = Creator.createManageSearchHistoryUseCase(sharedPreferences)
        val history = searchHistoryManage.getSearchHistory()
        searchEditText = findViewById(R.id.search_edit_text)

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = NetworkUtil.isConnected(connectivityManager)



        adapter = TrackAdapter { track ->
            searchHistoryManage.saveToHistory(track)
            if (searchQuery.isEmpty() or (searchQuery == "")) {
                adapter.updateTracks(searchHistoryManage.getSearchHistory())
            }

            val intent = Intent(this, PlayerActivity::class.java).apply {
                putExtra(TRACK_DATA, track)
            }
            if (isClickable) {
                isClickable = false
                startActivity(intent)
                handler.postDelayed({ isClickable = true }, CLICK_DEBOUNCE_DELAY)
            }

        }




        editTextClearBtn.setOnClickListener {
            searchEditText.text.clear()
            searchQuery = ""
            searchEditText.clearFocus()
            val tracksHistory = searchHistoryManage.getSearchHistory()

            if (tracksHistory.isNotEmpty()) {
                adapter.updateTracks(tracksHistory)
                recyclerView.adapter = adapter
                youSearch.visibility = View.VISIBLE
                historyCleaner.visibility = View.VISIBLE
                editTextClearBtn.visibility = View.GONE
            } else {
                adapter.updateTracks(emptyList())
            }

        }


        updateInternet.setOnClickListener {
            searchTrackInteractImplement.execute(searchQuery) { tracks ->
                runOnUiThread {
                    if (isConnected) {
                        searchDebounce()
                        if (tracks.isNullOrEmpty() and searchQuery.isNotBlank()) {
                            noSong.visibility = View.VISIBLE
                            recyclerView.visibility = View.GONE
                            editTextClearBtn.visibility = View.VISIBLE
                        } else if (tracks.isNullOrEmpty() and searchQuery.isBlank()) {
                            val tracksHistory = searchHistoryManage.getSearchHistory()

                            if (tracksHistory.isNotEmpty()) {
                                adapter.updateTracks(tracksHistory)
                                recyclerView.adapter = adapter
                                youSearch.visibility = View.VISIBLE
                                historyCleaner.visibility = View.VISIBLE
                                editTextClearBtn.visibility = View.GONE
                                noSong.visibility = View.GONE
                            }

                        } else if (!tracks.isNullOrEmpty() and !searchQuery.isBlank()) {
                            noSong.visibility = View.GONE
                            adapter.updateTracks(tracks!!)
                            recyclerView.adapter = adapter
                            recyclerView.visibility = View.VISIBLE
                        }
                    } else {
                        searchDebounce()
                        noSong.visibility = View.GONE
                    }

                }
            }
        }

        if (searchHistoryManage.getSearchHistory().isNotEmpty()) {
            youSearch.visibility = View.VISIBLE
            historyCleaner.visibility = View.VISIBLE
            adapter.updateTracks(searchHistoryManage.getSearchHistory())
        } else {
            youSearch.visibility = View.GONE
            historyCleaner.visibility = View.GONE

        }

        historyCleaner.setOnClickListener {
            searchHistoryManage.clearHistory()
            recyclerView.visibility = View.GONE
            youSearch.visibility = View.GONE
            historyCleaner.visibility = View.GONE
        }

        recyclerView = findViewById(R.id.tracks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter



        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchEditText.text.toString()

                editTextClearBtn.visibility = if (query.isNotEmpty()) View.VISIBLE else View.GONE
                searchQuery = query

                if (isConnected) {
                    val historySearch = searchHistoryManage.getSearchHistory()
                    searchTrackInteractImplement.execute(query) { tracks ->
                        runOnUiThread {
                            if (!historySearch.isNullOrEmpty() && query.isBlank()) {
                                adapter.updateTracks(historySearch)
                            } else {
                                adapter.updateTracks(tracks!!)
                            }
                        }
                    }
                } else {
                    noInternet.visibility = View.VISIBLE
                }
                true
            } else {
                false
            }
        }


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString()


                if (searchQuery.isBlank()) {

                    noSong.visibility = View.GONE
                    noInternet.visibility = View.GONE

                    val tracks = searchHistoryManage.getSearchHistory()
                    if (!tracks.isNullOrEmpty()) {
                        adapter.updateTracks(tracks)
                        recyclerView.adapter = adapter
                        recyclerView.visibility = View.VISIBLE
                        youSearch.visibility = View.VISIBLE
                        historyCleaner.visibility = View.VISIBLE
                        noSong.visibility = View.GONE
                    } else if (tracks.isNullOrEmpty() and searchQuery.isBlank()) {

                        adapter.updateTracks(emptyList())
                    } else {
                        noSong.visibility = View.GONE
                    }
                    editTextClearBtn.visibility = View.GONE
                } else {

                    youSearch.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                    historyCleaner.visibility = View.GONE
                    editTextClearBtn.visibility = View.VISIBLE
                    searchDebounce()
                }
                noInternet.visibility = View.GONE
            }
        })

    }


    private val searchRunnable = Runnable {
        searchTrackInteractImplement = Creator.createSearchTracksUseCase()
        searchTrackInteractImplement.execute(searchQuery) { tracks ->
            runOnUiThread {
                if (tracks.isNullOrEmpty()) {
                    Log.d("UserSearch", "No results found for: $searchQuery")

                    if (!isConnected(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)) {
                        noInternet.visibility = View.VISIBLE
                        noInternet.visibility = View.GONE
                    } else if (isConnected(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager) and searchQuery.isBlank()) {
                        noInternet.visibility = View.GONE
                        editTextClearBtn.visibility = View.GONE
                        noSong.visibility = View.GONE
                    } else if (searchQuery.isNotBlank()) {
                        noInternet.visibility = View.GONE
                        noSong.visibility = View.VISIBLE
                    }
                } else {
                    adapter.updateTracks(tracks)
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                    noSong.visibility = View.GONE
                }
            }
        }

    }

    private fun searchDebounce() {
        if (!isConnected(getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)) {
            noInternet.visibility = View.VISIBLE
        } else {
            noInternet.visibility = View.GONE
        }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchTrackInteractImplement.shutdown()
    }


    companion object {
        const val TRACK_DATA = "TRACK_DATA"
        const val SEARCH_KEY_ON_STATE = "search_query"

    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_KEY_ON_STATE, searchQuery)
    }

}