package ir.mahan.mangamotion.utils.constants

object APIQueryParameters {
    //--Top Mangas--//
    //      Keys
    const val TYPE_KEY = "type"
    const val MANGA_ORDER_BY_KEY = "order_by"
    const val MANGA_STATUS_KEY  = "status"
    const val SORT_KEY = "sort"
    const val SWF_KEY = "swf"
    const val START_DATE_KEY = "start_date"
    const val LIMIT = "limit"
    //      Values
    const val TOP_MANGA_TYPE_MANGA = "manga"
    const val TOP_MANGA_TYPE_NOVEL = "novel"
    const val TOP_MANGA_TYPE_LIGHT_NOVEL = "lightnovel"
    const val TOP_MANGA_TYPE_ONESHOT = "oneshot"
    const val TOP_MANGA_TYPE_DOUJIN = "doujin"
    const val TOP_MANGA_TYPE_MANHWA = "manhwa"
    const val TOP_MANGA_TYPE_MANHUA = "manhua"

    const val TOP_MANGA_FILTER_PUBLISHING = "publishing"
    const val TOP_MANGA_FILTER_UPCOMING = "upcoming"
    const val TOP_MANGA_FILTER_BY_POPULARITY = "bypopularity"

    const val TOP_MANGA_FILTER_FAVORITE = "favorite"

    const val MANGA_ORDER_BY_MAL_ID = "mal_id"
    const val MANGA_ORDER_BY_TITLE = "title"
    const val MANGA_ORDER_BY_START_DATE = "start_date"
    const val MANGA_ORDER_BY_END_DATE = "end_date"
    const val MANGA_ORDER_BY_CHAPTERS = "chapters"
    const val MANGA_ORDER_BY_VOLUMES = "volumes"
    const val MANGA_ORDER_BY_SCORE = "score"
    const val MANGA_ORDER_BY_SCORED_BY = "scored_by"
    const val MANGA_ORDER_BY_RANK = "rank"
    const val MANGA_ORDER_BY_POPULARITY = "popularity"
    const val MANGA_ORDER_BY_MEMBERS = "members"
    const val MANGA_ORDER_BY_FAVORITES = "favorites"

    const val SORT_ASC = "asc"
    const val SORT_DESC = "desc"

    const val MANGA_STATUS_PUBLISHING  = "publishing"
    const val MANGA_STATUS_COMPLETE  = "complete"
    const val MANGA_STATUS_HIATUS  = "hiatus"
    const val MANGA_STATUS_DISCONTINUED  = "discontinued"
    const val MANGA_STATUS_UPCOMING  = "upcoming"
    ///////////////////////////////////////////////////////////////////////////
    // Anime
    ///////////////////////////////////////////////////////////////////////////
    // Anime types
    const val TOP_ANIME_TYPE_TV = "tv"
    const val TOP_ANIME_TYPE_MOVIE = "movie"
    const val TOP_ANIME_TYPE_OVA = "ova"
    const val TOP_ANIME_TYPE_SPECIAL = "special"
    const val TOP_ANIME_TYPE_ONA = "ona"
    const val TOP_ANIME_TYPE_MUSIC = "music"
    const val TOP_ANIME_TYPE_CM = "cm"
    const val TOP_ANIME_TYPE_PV = "pv"
    const val TOP_ANIME_TYPE_TV_SPECIAL = "tv_special"
    //Filters
    const val TOP_ANIME_FILTER_KEY = "filter"
    const val TOP_ANIME_FILTER_AIRING = "airing"
    const val TOP_ANIME_FILTER_UPCOMING = "upcoming"
    const val TOP_ANIME_FILTER_BY_POPULARITY = "bypopularity"
    const val TOP_ANIME_FILTER_FAVORITE = "favorite"

}
