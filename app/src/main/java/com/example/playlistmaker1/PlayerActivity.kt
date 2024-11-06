package com.example.playlistmaker1

import Track
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Suppress("DEPRECATION")
class PlayerActivity : AppCompatActivity() {
    private lateinit var backButton: TextView
    private lateinit var coverImageView: ImageView
    private lateinit var trackNameTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var collectionNameTextView: TextView
    private lateinit var releaseDateTextView: TextView
    private lateinit var primaryGenreNameTextView: TextView
    private lateinit var countryTextView: TextView
    private lateinit var trackTimeTextView: TextView
    private lateinit var playButton: ImageView
    private lateinit var addToPlaylistButton: ImageView
    private lateinit var addToFavoritesButton: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        supportActionBar?.hide()
        backButton = findViewById(R.id.backButton)
        coverImageView = findViewById(R.id.cover)
        trackNameTextView = findViewById(R.id.track_name_player)
        artistNameTextView = findViewById(R.id.artistName)
        collectionNameTextView = findViewById(R.id.collectionNameValue)
        releaseDateTextView = findViewById(R.id.releaseDateValue)
        primaryGenreNameTextView = findViewById(R.id.trackGenreValue)
        countryTextView = findViewById(R.id.countryValue)
        trackTimeTextView = findViewById(R.id.durationValue)
        playButton = findViewById(R.id.playButton)
        addToPlaylistButton = findViewById(R.id.buttonAddCollection)
        addToFavoritesButton = findViewById(R.id.favorite_button)


        val track = intent.getSerializableExtra(TRACK_DATA) as? Track

        track?.let {
            updateUI(it)
        }

        backButton.setOnClickListener {
           finish()
        }
    }

    private fun updateUI(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        collectionNameTextView.text = track.collectionName

        releaseDateTextView.text = formatReleaseDate(track.releaseDate)

        trackTimeTextView.text = formatTrackTime(track.trackTimeMillis.toLong())
        primaryGenreNameTextView.text = if (track.genre.isNullOrEmpty()) "Не указано" else track.genre
        countryTextView.text = if (track.country.isNullOrEmpty()) "Не указано" else track.country
        val artworkUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(Utils.spToPx(this, 8f).toInt()))
            .into(coverImageView)
    }




    private fun formatReleaseDate(releaseDate: Date?): String {
        if (releaseDate == null) {
            return "Не указано"
        }


        val dateFormat = SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(releaseDate)
    }

    private fun formatTrackTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format(FORMAT_TIME_TS, minutes, seconds)
    }



    companion object {
        const val FORMAT_TIME_TS = "%02d:%02d"
        const val PATTERN_DATE_FORMAT = "yyyy"
        const val TRACK_DATA = "TRACK_DATA"
    }
}