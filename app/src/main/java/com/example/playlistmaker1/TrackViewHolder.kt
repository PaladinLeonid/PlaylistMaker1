package com.example.playlistmaker1


import android.annotation.SuppressLint
import android.view.View
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

    @SuppressLint("SuspiciousIndentation")
    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

            Glide.with(itemView)
                .load(model.artImg)
                .fitCenter()
                .placeholder(R.drawable.track_placeholder)
                .centerCrop()
                .transform(RoundedCorners(Utils.dpToPx(2f, itemView.context)))
                .into(trackLogo)
        }
    }
