
package com.example.playlistmaker1

import Track
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var artwork: ImageView
    private lateinit var trackTime: TextView
    private lateinit var collection: TextView
    private lateinit var release: TextView
    private lateinit var country: TextView
    private lateinit var genre:TextView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)


        genre = findViewById(R.id.track_genre)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        artwork = findViewById(R.id.track_cover)
        trackTime = findViewById(R.id.track_duration_time_info)
        collection = findViewById(R.id.track_album_info)
        release = findViewById(R.id.track_year_info)
        country = findViewById(R.id.track_country_info)


        val track = intent.getSerializableExtra(TRACK_DATA) as? Track

        track?.let {
            updateUI(it)
        }

        findViewById<ImageView>(R.id.arrow).setOnClickListener {
            finish()
        }
    }

    private fun updateUI(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        collection.text = track.collectionName

        release.text = formatReleaseDate(track.releaseDate)
        genre.text = track.genre
        country.text = track.country
        trackTime.text = formatTrackTime(track.trackTimeMillis.toLong())

        val artworkUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.track_placeholder)
            .into(artwork)
    }


    private fun formatReleaseDate(releaseDate: Date?): String {
        if (releaseDate == null) {
            return UNKNOWN_DATE
        }

        val dateFormat = SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(releaseDate)
    }

    private fun formatTrackTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format(FORMAT_TIME_TS, minutes, seconds)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        const val UNKNOWN_DATE = "Не указано"
        const val FORMAT_TIME_TS = "%02d:%02d"
        const val PATTERN_DATE_FORMAT = "yyyy"
        const val TRACK_DATA = "TRACK_DATA"
    }
}
