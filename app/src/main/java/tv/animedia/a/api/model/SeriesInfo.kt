package tv.animedia.a.api.model

data class SeriesInfo (
    val total: Int?,
    val response: List<Series?>
)

data class Series (
    val id: Long,
    val old: String? = null,
    val pic: String? = null,
    val title: String? = null,
    val categories: String? = null,
    val description: String? = null,
    val season: List<String> = ArrayList()
)