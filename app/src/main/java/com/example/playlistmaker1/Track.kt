import java.io.Serializable
import java.util.Date


data class Track (
    val trackName: String,
    val artistName: String,
    val artworkUrl100: String,
    val trackTimeMillis: Long,
    val collectionName: String,
    val releaseDate: Date,
    val genre: String,
    val country: String
) : Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}