package com.diagnal.diagnalprject.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diagnal.diagnalprject.R
import com.diagnal.diagnalprject.model.Content

class ContentListAdapter (private val context: Context) : RecyclerView.Adapter<ContentListAdapter.ViewHolder>(),Filterable {
    private var contentList = mutableListOf<Content>()
    private var filteredContentList = mutableListOf<Content>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listing_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val content = filteredContentList[position]
        forImageBind(holder.ivPosterImage, content.posterImage)
        holder.tvPosterName.text = content.name
    }

    private fun forImageBind(ivPosterImage: ImageView, posterImageValue: String) {
        when (posterImageValue) {
            "poster1.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_one)
            }

            "poster2.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_two)
            }

            "poster3.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_three)
            }

            "poster4.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_four)
            }

            "poster5.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_five)
            }

            "poster6.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_six)
            }

            "poster7.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_seven)
            }

            "poster8.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_eight)
            }
            "poster9.jpg" -> {
                ivPosterImage.setImageResource(R.drawable.poster_nine)
            }

            else -> {
                ivPosterImage.setImageResource(R.drawable.placeholder_for_missing_posters)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredContentList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(newContentList: List<Content>) {
        contentList.addAll(newContentList)
        filteredContentList.addAll(newContentList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPosterName: TextView = itemView.findViewById(R.id.tvPosterName)
        val ivPosterImage: ImageView = itemView.findViewById(R.id.ivPosterImage)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                val query = constraint?.toString()?.toLowerCase()?.trim()

                val filteredList = if (query.isNullOrEmpty()) {
                    contentList
                } else {
                    contentList.filter { it.name.toLowerCase().contains(query) }
                }

                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredContentList.clear()
                filteredContentList.addAll(results?.values as List<Content>)
                notifyDataSetChanged()
            }
        }
    }


}