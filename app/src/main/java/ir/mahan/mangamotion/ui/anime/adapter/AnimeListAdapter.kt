package ir.mahan.mangamotion.ui.anime.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import ir.mahan.mangamotion.data.model.anime.ResponseAnimeList
import ir.mahan.mangamotion.databinding.MangaVerticalBinding
import ir.mahan.mangamotion.databinding.TopAnimeItemLayoutBinding
import ir.mahan.mangamotion.utils.BaseDiffUtils
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import timber.log.Timber
import javax.inject.Inject


open class AnimeListAdapter @Inject constructor() :
    RecyclerView.Adapter<AnimeListAdapter.ViewHolder>() {

    // Binding
    private lateinit var context: Context

    // Properties
    private var isTopBinding: Boolean = true
    private var items: List<ResponseAnimeList.Data> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = if (isTopBinding)
            TopAnimeItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        else
            MangaVerticalBinding.inflate(LayoutInflater.from(context), parent, false)
        //val  binding = MangaVerticalBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.hashCode().toLong()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }


    inner class ViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResponseAnimeList.Data, index: Int) {
            when (binding) {
                is TopAnimeItemLayoutBinding -> binding.initViews(item, index)
                is MangaVerticalBinding -> binding.initViews(item, index)
            }
        }


        // Root Click Listener
        private var onItemClickListener: ((Int) -> Unit)? = null
        fun setOnItemClickListener(listener: (Int) -> Unit) {
            onItemClickListener = listener
        }
    }// ViewHolder End

    private fun TopAnimeItemLayoutBinding.initViews(item: ResponseAnimeList.Data, index: Int) {
//        Timber.tag(DEBUG_TAG).d("Binding Anime Item: ${item.malId}")
        animeCoverImg.load(item.images?.webp?.largeImageUrl) {
            error(android.R.drawable.stat_notify_error)
            crossfade(true)
            crossfade(500)
        }
    }

    private fun MangaVerticalBinding.initViews(item: ResponseAnimeList.Data, index: Int) {
//        Timber.tag(DEBUG_TAG).d("Binding Manga Item: ${item.malId}")
        coverImg.load(item.images?.webp?.largeImageUrl) {
            error(android.R.drawable.stat_notify_error)
            crossfade(true)
            crossfade(500)
        }
        indexTxt.text = index.plus(1).toString()
        titleTxt.text = item.title
        //genreTxt.text = item.genres?.map { it!!.name }?.joinToString(", ")
        genreTxt.text = item.score?.toString() ?: ""
    }

    // DiffUtils
    fun setItems(newData: List<ResponseAnimeList.Data>) {
//        Timber.tag(DEBUG_TAG).d("Adapter Items: ${newData.size}")
        val adapterDiffUtils = BaseDiffUtils(items, newData)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = newData
        diffUtils.dispatchUpdatesTo(this)
    }

    fun changeBindMode(isTopBinding: Boolean) {
        this.isTopBinding = isTopBinding
    }

}