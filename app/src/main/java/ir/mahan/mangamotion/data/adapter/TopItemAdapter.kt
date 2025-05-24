package ir.mahan.mangamotion.data.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ir.mahan.mangamotion.data.model.ResponseTopManga
import ir.mahan.mangamotion.databinding.ItemTopBinding
import ir.mahan.mangamotion.utils.BaseDiffUtils
import ir.mahan.mangamotion.utils.constants.DEBUG_TAG
import timber.log.Timber
import javax.inject.Inject


class TopItemAdapter @Inject constructor() : RecyclerView.Adapter<TopItemAdapter.ViewHolder>() {

    // Binding
    private lateinit var binding: ItemTopBinding
    private lateinit var context: Context

    // Properties
    private var items: List<ResponseTopManga.Data> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemTopBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder()
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = position
    override fun getItemId(position: Int) = position.hashCode().toLong()
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], position)
    }


    /**
     * View holder
     *
     * @constructor Create empty View holder
     */
    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResponseTopManga.Data, index: Int) {
            binding.apply {
                coverImg.load(item.images?.webp?.largeImageUrl) {
                    error(android.R.drawable.stat_notify_error)
                    crossfade(true)
                    crossfade(500)
                }
                indexTxt.text = index.plus(1).toString()
                titleTxt.text = item.title
                //genreTxt.text = item.genres?.map { it!!.name }?.joinToString(", ")
                genreTxt.text = item.score.toString()
            }

        }


        // Root Click Listener
        private var onItemClickListener: ((Int) -> Unit)? = null
        fun setOnItemClickListener(listener: (Int) -> Unit) {
            onItemClickListener = listener
        }


    }// ViewHolder End

    // DiffUtils
    fun setItems(newData: List<ResponseTopManga.Data>) {
        Timber.tag(DEBUG_TAG).d("Adapter Items: ${items.size}")
        val adapterDiffUtils = BaseDiffUtils(items, newData)
        val diffUtils = DiffUtil.calculateDiff(adapterDiffUtils)
        items = newData
        diffUtils.dispatchUpdatesTo(this)
    }
}