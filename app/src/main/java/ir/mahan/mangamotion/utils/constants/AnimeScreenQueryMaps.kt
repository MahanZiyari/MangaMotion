package ir.mahan.mangamotion.utils.constants


enum class AnimeScreenQueryMaps(val queries: Map<String, String>) {

    TOP(buildMap {
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_ANIME_TYPE_TV)
        put(APIQueryParameters.TOP_ANIME_FILTER_KEY, APIQueryParameters.TOP_ANIME_FILTER_BY_POPULARITY)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    LATEST(queries = buildMap {
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_ANIME_TYPE_PV)
        put(APIQueryParameters.ORDER_BY_KEY, APIQueryParameters.ORDER_BY_START_DATE)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    FOR_KIDS(queries = buildMap {
//        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_ANIME_TYPE_MOVIE)
        put(APIQueryParameters.ANIME_RATING_KEY, APIQueryParameters.ANIME_RATING_PG)
        put(APIQueryParameters.ORDER_BY_KEY, APIQueryParameters.ORDER_BY_SCORE)
//        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    })
}