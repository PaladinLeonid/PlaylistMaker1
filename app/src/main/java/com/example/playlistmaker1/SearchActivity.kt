package com.example.playlistmaker1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible


class SearchActivity : AppCompatActivity() {
    private lateinit var searchField: EditText
    private var searchText: String? = null

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        searchField = findViewById(R.id.search_edit_field)
        val backButton = findViewById<ImageView>(R.id.arrow_back)
        val clearIcon = findViewById<ImageView>(R.id.search_clear_btn)
        supportActionBar?.hide()

        backButton.setOnClickListener {
            finish()
        }
        clearIcon.setOnClickListener {
            searchField.text.clear()
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButtonVisibility(s, clearIcon)
                searchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        searchField.addTextChangedListener(simpleTextWatcher)
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_SEARCH_TEXT, searchText)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchField.setText(savedInstanceState.getString(EXTRA_SEARCH_TEXT,""))
    }
    private fun clearButtonVisibility(s: CharSequence?, clearButton: ImageView) {
        val clearIcon = findViewById<ImageView>(R.id.search_clear_btn)
        clearIcon.isVisible = !s.isNullOrEmpty()
    }
    companion object {
        const val EXTRA_SEARCH_TEXT = "searchText"
    }

}

