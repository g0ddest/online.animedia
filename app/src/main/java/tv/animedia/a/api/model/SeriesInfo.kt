package tv.animedia.a.api.model

data class SeriesInfo (
    val total: Int?,
    val response: List<Series?>
)

data class Series (
    val id: Long,
    val old: String?,
    val pic: String?,
    val title: String?,
    val categories: String,
    val description: String,
    val season: List<String>
)