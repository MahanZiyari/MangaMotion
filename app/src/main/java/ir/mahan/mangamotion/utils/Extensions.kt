package ir.mahan.mangamotion.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.createBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import ir.mahan.mangamotion.utils.constants.EMAIL_REGEX

fun String.checkForEmailMatching() = this.matches(EMAIL_REGEX.toRegex())


fun RecyclerView.setup(newAdapter: RecyclerView.Adapter<*>, newLayoutManager: LayoutManager) {
    this.apply {
        adapter = newAdapter
        layoutManager = newLayoutManager
        setHasFixedSize(true)
    }
}

fun Drawable.toBitmap(): Bitmap {
    // Check if the drawable is already a BitmapDrawable
    if (this is BitmapDrawable) {
        return this.bitmap
    }
    // Get the intrinsic width and height of the drawable
    val width = this.intrinsicWidth
    val height = this.intrinsicHeight
    // Create a new bitmap with the specified width and height
    val bitmap = createBitmap(width, height)
    // Create a canvas from the bitmap
    val canvas = Canvas(bitmap)
    // Set the bounds of the drawable
    this.setBounds(0, 0, canvas.width, canvas.height)
    // Draw the drawable onto the canvas
    this.draw(canvas)

    return bitmap
}