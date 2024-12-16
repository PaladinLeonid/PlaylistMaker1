package com.example.playlistmaker1.ui.player


import Utils
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker1.Creator
import com.example.playlistmaker1.R
import com.example.playlistmaker1.domain.api.MediaPlayerInteract
import com.example.playlistmaker1.domain.model.PlayerStatus
import com.example.playlistmaker1.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaPlayerInteractorImplement: MediaPlayerInteract
    private lateinit var screenReceiver: ScreenReceiver
    private lateinit var handler: Handler
    private var songBridge: String? = null
    private var playerStatus = PlayerStatus.DEFAULT

    private lateinit var backButton: ImageButton
    private lateinit var coverImageView: ImageView
    private lateinit var trackNameTextView: TextView
    private lateinit var artistNameTextView: TextView
    private lateinit var collectionNameTextView: TextView
    private lateinit var releaseDateTextView: TextView
        private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var trackTimeTextView: TextView
    private lateinit var playButton: ImageButton
    private lateinit var addToPlaylistButton: ImageButton
    private lateinit var addToFavoritesButton: ImageButton
    private lateinit var currentTrackTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)


        backButton = findViewById(R.id.backButton)
        coverImageView = findViewById(R.id.cover)
        trackNameTextView = findViewById(R.id.track_name_player)
        artistNameTextView = findViewById(R.id.artistName)
        collectionNameTextView = findViewById(R.id.collectionNameValue)
        releaseDateTextView = findViewById(R.id.releaseDateValue)
        genre = findViewById(R.id.trackGenreValue)
        country = findViewById(R.id.countryValue)
        trackTimeTextView = findViewById(R.id.durationValue)
        playButton = findViewById(R.id.playButton)
        currentTrackTime = findViewById(R.id.currentTrackTime)
        addToPlaylistButton = findViewById(R.id.buttonAddCollection)
        addToFavoritesButton = findViewById(R.id.favorite_button)

        mediaPlayerInteractorImplement = Creator.createPlayer()
        handler = Handler(Looper.getMainLooper())

        screenReceiver = ScreenReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(screenReceiver, filter)

        val track = intent.getSerializableExtra(TRACK_DATA) as? Track
        track?.let {
            updateUI(it)
            preparePlayer(it)
        }

        backButton.setOnClickListener {
            mediaPlayerInteractorImplement.stop()
            onBackPressed()
        }

        playButton.setOnClickListener {
            PlayerStatus.PREPARED
            PlayerStatus.PAUSED
            playbackControl()
            startCountdown()
        }
    }

    private fun startCountdown() {
        handler.post(object : Runnable {
            @SuppressLint("DefaultLocale")
            override fun run() {
                if (mediaPlayerInteractorImplement.isPlaying()) {
                    val currentPositionMillis = mediaPlayerInteractorImplement.getCurrentPosition()
                    val minutes = (currentPositionMillis / 1000) / 60
                    val seconds = (currentPositionMillis / 1000) % 60
                    val formattedTime = String.format("%02d:%02d", minutes, seconds)
                    currentTrackTime.text = formattedTime
                    handler.postDelayed(this, 1000)
                } else {
                    handler.removeCallbacks(this)
                    playButton.setBackgroundResource(R.drawable.play_button)
                    currentTrackTime.text = getString(R.string.placeholderCurrentTrack)
                }
            }
        })
    }
    private fun updateUI(track: Track) {
        trackNameTextView.text = track.trackName
        artistNameTextView.text = track.artistName
        collectionNameTextView.text = track.collectionName
        releaseDateTextView.text = formatReleaseDate(track.releaseDate)
        genre.text = track.genre
        country.text = track.country
        trackTimeTextView.text = formatTrackTime(track.trackTimeMillis.toLong())

        songBridge = track.previewUrl

        val artworkUrl = track.getCoverArtwork()

        Glide.with(this)
            .load(artworkUrl)
            .placeholder(R.drawable.track_placeholder)
            .transform(RoundedCorners(Utils.spToPx(this, 8f).toInt()))
            .into(coverImageView)
    }

    private fun formatReleaseDate(releaseDate: Date?): String {
        return releaseDate?.let {
            val dateFormat = SimpleDateFormat(PATTERN_DATE_FORMAT, Locale.getDefault())
            dateFormat.format(it)
        } ?: getString(R.string.not_specified)
    }

    @SuppressLint("DefaultLocale")
    private fun formatTrackTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        return String.format(FORMAT_TIME_TS, minutes, seconds)
    }

    @Deprecated("This method use back button.")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun preparePlayer(track: Track) {
        songBridge = track.previewUrl

        if (!songBridge.isNullOrEmpty()) {
            mediaPlayerInteractorImplement.execute(track)
            playerStatus = PlayerStatus.PREPARED
        } else {
            Log.e("CheckBridgeUrl", "Url is empty")
        }
    }

    private fun startPlayer() {
        mediaPlayerInteractorImplement.play()
        playButton.setBackgroundResource(R.drawable.play_on_button)
        playerStatus = PlayerStatus.PLAYING
    }

    private fun pausePlayer() {
        if (mediaPlayerInteractorImplement.isPlaying()) {
            mediaPlayerInteractorImplement.pause()
            playButton.setBackgroundResource(R.drawable.play_button)
            playerStatus = PlayerStatus.PAUSED
        }
    }

    private fun playbackControl() {
        when (playerStatus) {
            PlayerStatus.PLAYING -> pausePlayer()
            PlayerStatus.PREPARED, PlayerStatus.PAUSED -> startPlayer()
            PlayerStatus.DEFAULT -> {
                Log.e("ErrorState", "PlayerErrorState")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        mediaPlayerInteractorImplement.stop()
        try {
            unregisterReceiver(screenReceiver)
        } catch (e: IllegalArgumentException) {
            Log.w("PlayerActivityError", "Receiver was not registered. Check Who False", e)
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(screenReceiver, IntentFilter(Intent.ACTION_SCREEN_OFF))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(screenReceiver)
    }


    companion object {
        const val FORMAT_TIME_TS = "%02d:%02d"
        const val PATTERN_DATE_FORMAT = "yyyy"
        const val TRACK_DATA = "TRACK_DATA"
    }
}

class ScreenReceiver: BroadcastReceiver() {

    private var playbackCallback: ((Boolean) -> Unit)? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_SCREEN_OFF) {
            playbackCallback?.invoke(true)
        }
    }
}

