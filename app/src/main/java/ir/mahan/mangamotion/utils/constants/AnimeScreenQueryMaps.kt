package ir.mahan.mangamotion.utils.constants


enum class AnimeScreenQueryMaps(val queries: Map<String, String>) {

    TOP(buildMap {
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_ANIME_TYPE_TV)
        put(APIQueryParameters.TOP_ANIME_FILTER_KEY, APIQueryParameters.TOP_ANIME_FILTER_BY_POPULARITY)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    CURRENTLY_PUBLISHING(queries = buildMap {
        put(APIQueryParameters.MANGA_ORDER_BY_KEY, APIQueryParameters.MANGA_ORDER_BY_START_DATE)
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_MANGA_TYPE_MANGA)
        put(APIQueryParameters.MANGA_STATUS_KEY, APIQueryParameters.MANGA_STATUS_PUBLISHING)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    })
}