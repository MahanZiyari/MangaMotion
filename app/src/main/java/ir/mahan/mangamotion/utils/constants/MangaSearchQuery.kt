package ir.mahan.mangamotion.utils.constants

enum class MangaSearchQuery(query: String) {
    UNAPPROVED("unapproved"),
    PAGE("page"),
    LIMIT("limit"),
    Q("q"),
    TYPE("type"),
    SCORE("score"),
    MIN_SCORE("min_score"),
    MAX_SCORE("max_score"),
    STATUS("status"),
    SFW("sfw"),
    GENRES("genres"),
    GENRES_EXCLUDE("genres_exclude"),
    ORDER_BY("order_by"),
    SORT("sort"),
    LETTER("letter"),
    MAGAZINES("magazines"),
    START_DATE("start_date"),
    END_DATE("end_date"),
}