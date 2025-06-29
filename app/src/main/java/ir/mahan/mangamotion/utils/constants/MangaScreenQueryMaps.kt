package ir.mahan.mangamotion.utils.constants


enum class MangaScreenQueryMaps(val queries: Map<String, String>) {

    POPULAR(buildMap {
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_MANGA_TYPE_MANGA)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    CURRENTLY_PUBLISHING(queries = buildMap {
        put(APIQueryParameters.ORDER_BY_KEY, APIQueryParameters.ORDER_BY_START_DATE)
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_MANGA_TYPE_MANGA)
        put(APIQueryParameters.STATUS_KEY, APIQueryParameters.MANGA_STATUS_PUBLISHING)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    DOUJINS(queries = buildMap {
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_MANGA_TYPE_DOUJIN)
        put(APIQueryParameters.ORDER_BY_KEY, APIQueryParameters.ORDER_BY_POPULARITY)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.SWF_KEY, "1")
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    MANHWA(queries = buildMap {
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_MANGA_TYPE_MANHWA)
        put(APIQueryParameters.ORDER_BY_KEY, APIQueryParameters.ORDER_BY_SCORE)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    MANHUA(queries = buildMap {
        put(APIQueryParameters.TYPE_KEY, APIQueryParameters.TOP_MANGA_TYPE_MANHUA)
        put(APIQueryParameters.ORDER_BY_KEY, APIQueryParameters.ORDER_BY_SCORE)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),
}