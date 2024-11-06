package com.example.playlistmaker1
import Track
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.name_artist)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackLogo: ImageView = itemView.findViewById(R.id.track_image)
    private fun getTrackDuration(milliseconds: String): String {
        val totalSeconds = milliseconds.toLong().div(1000)
        val minutes = (totalSeconds / 60) % 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
    companion object {
        fun create(parent: ViewGroup): TrackViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.track_view, parent, false)
            return TrackViewHolder(view)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = getTrackDuration(model.trackTimeMillis.toString())
        artistName.requestLayout()
        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            .fitCenter()
            .placeholder(R.drawable.track_placeholder)
            .centerCrop()
            .transform(RoundedCorners(Utils.dpToPx(2f, itemView.context)))
            .into(trackLogo)
        itemView.setOnClickListener {

        }
    }
}