package ir.mahan.mangamotion.utils

fun String.checkForEmailMatching() = this.matches(EMAIL_REGEX.toRegex())