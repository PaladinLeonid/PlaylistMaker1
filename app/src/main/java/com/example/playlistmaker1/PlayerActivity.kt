package com.example.playlistmaker1

import Track
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.example.playlistmaker1.SearchActivity.Companion.TRACK_DATA
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
    private lateinit var currentTimeTextView: TextView // TextView для отображения текущего времени
    private lateinit var addToPlaylistButton: ImageView
    private lateinit var addToFavoritesButton: ImageView

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    private lateinit var timeUpdateHandler: Handler
    private lateinit var timeUpdateRunnable: Runnable

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
        currentTimeTextView = findViewById(R.id.currentTrackTime) // Инициализация TextView для текущего времени
        addToPlaylistButton = findViewById(R.id.buttonAddCollection)
        addToFavoritesButton = findViewById(R.id.favorite_button)

        val track = intent.getSerializableExtra(TRACK_DATA) as? Track

        track?.let {
            updateUI(it)
            setupMediaPlayer(it.previewUrl) // Убедитесь, что у вас есть поле previewUrl в классе Track
        }

        // Инициализация обработчика и runnable для обновления времени
        timeUpdateHandler = Handler(Looper.getMainLooper())
        timeUpdateRunnable = Runnable {
            updateCurrentTime()
            timeUpdateHandler.postDelayed(timeUpdateRunnable, 1000) // Обновление каждую секунду
        }

        backButton.setOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            if (isPlaying) {
                pauseAudio()
            } else {
                playAudio()
            }
        }
    }

    private fun updateUI(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        collectionNameTextView.text = track.collectionName

        releaseDateTextView.text = formatReleaseDate(track.releaseDate.toString())

        trackTimeTextView.text = formatTrackTime(track.trackTimeMillis.toLong())
        primaryGenreNameTextView.text = if (track.genre.isNullOrEmpty()) "Не указано" else track.genre
        countryTextView.text = if (track.country.isNullOrEmpty()) "Не указано" else track.country
        val artworkUrl = track.getCoverArtwork()

        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.track_placeholder)

            .into(coverImageView)
    }

    private fun setupMediaPlayer(previewUrl: String) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playAudio()
            }
            setOnCompletionListener {
                this@PlayerActivity.isPlaying = false
                playButton.setImageResource(R.drawable.play_button) // Измените иконку на "play"
                timeUpdateHandler.removeCallbacks(timeUpdateRunnable) // Остановите обновление времени
                playButton.setBackgroundResource(R.drawable.play_button)
            }
        }
    }
    private fun playAudio() {
        mediaPlayer?.start()
        isPlaying = true
        playButton.setBackgroundResource(R.drawable.play_on_button)


        timeUpdateHandler.post(timeUpdateRunnable)
    }

    private fun pauseAudio() {
        mediaPlayer?.pause()
        isPlaying = false
        playButton.setBackgroundResource(R.drawable.play_button)


        timeUpdateHandler.removeCallbacks(timeUpdateRunnable)
    }





    private fun updateCurrentTime() {
        mediaPlayer?.let {
            val currentPosition = it.currentPosition / 1000 // Получаем позицию в секундах
            currentTimeTextView.text = formatTime(currentPosition) // Обновите ваш TextView с текущим временем
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null


        timeUpdateHandler.removeCallbacks(timeUpdateRunnable)
    }

    private fun formatReleaseDate(releaseDate: String): String {

        return SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(releaseDate))
    }

    @SuppressLint("DefaultLocale")
    private fun formatTrackTime(trackTimeMillis: Long): String {
        val minutes = (trackTimeMillis / 1000) / 60
        val seconds = (trackTimeMillis / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}