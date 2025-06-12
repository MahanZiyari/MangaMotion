package ir.mahan.mangamotion.utils.constants


enum class MangaScreenQueryMaps(val queries: Map<String, String>) {

    POPULAR(buildMap {
        put(APIQueryParameters.TOP_MANGA_TYPE_Key, APIQueryParameters.TOP_MANGA_TYPE_MANGA)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    CURRENTLY_PUBLISHING(queries = buildMap {
        put(APIQueryParameters.MANGA_ORDER_BY_KEY, APIQueryParameters.MANGA_ORDER_BY_START_DATE)
        put(APIQueryParameters.TOP_MANGA_TYPE_Key, APIQueryParameters.TOP_MANGA_TYPE_MANGA)
        put(APIQueryParameters.MANGA_STATUS_KEY, APIQueryParameters.MANGA_STATUS_PUBLISHING)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    DOUJINS(queries = buildMap {
        put(APIQueryParameters.TOP_MANGA_TYPE_Key, APIQueryParameters.TOP_MANGA_TYPE_DOUJIN)
        put(APIQueryParameters.MANGA_ORDER_BY_KEY, APIQueryParameters.MANGA_ORDER_BY_POPULARITY)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.SWF_KEY, "1")
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    MANHWA(queries = buildMap {
        put(APIQueryParameters.TOP_MANGA_TYPE_Key, APIQueryParameters.TOP_MANGA_TYPE_MANHWA)
        put(APIQueryParameters.MANGA_ORDER_BY_KEY, APIQueryParameters.MANGA_ORDER_BY_SCORE)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),

    MANHUA(queries = buildMap {
        put(APIQueryParameters.TOP_MANGA_TYPE_Key, APIQueryParameters.TOP_MANGA_TYPE_MANHUA)
        put(APIQueryParameters.MANGA_ORDER_BY_KEY, APIQueryParameters.MANGA_ORDER_BY_SCORE)
        put(APIQueryParameters.SORT_KEY, APIQueryParameters.SORT_DESC)
        put(APIQueryParameters.LIMIT, LIMIT_NUMBER.toString())
    }),
}