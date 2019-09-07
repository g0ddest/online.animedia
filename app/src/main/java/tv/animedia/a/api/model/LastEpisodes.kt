package tv.animedia.a.api.model

data class LastEpisodes (
    val response: List<Episode?>
)

data class Episode (
    val animeId: Long,
    val id: Long,
    val name: String,
    val pic: String,
    val seasonNum: Int,
    val title: String,
    val url_video: String?
)