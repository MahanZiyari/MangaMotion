package ir.mahan.mangamotion.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import ir.mahan.mangamotion.R
import timber.log.Timber


@HiltAndroidApp
class MangaMotionApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // initializing Calligraphy
        ViewPump.init(
            ViewPump.builder().addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Poppins-medium.ttf")
                        .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                        .build()
                )
            ).build()
        )
        // Timber
        Timber.plant(Timber.DebugTree())
    }
}