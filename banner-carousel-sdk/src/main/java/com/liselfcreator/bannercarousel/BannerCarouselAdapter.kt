package com.liselfcreator.bannercarousel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.liselfcreator.bannercarousel.R

/**
 * 轮播卡片适配器，支持无限循环、可选顶部状态和底部信息。
 */
internal class BannerCarouselAdapter(
    private val data: List<BannerModel>,
    private val showStatus: Boolean,
    private val showBottom: Boolean,
    private val onItemClickListener: ((position: Int, model: BannerModel) -> Unit)?,
    private val imageLoader: BannerImageLoader
) : RecyclerView.Adapter<BannerCarouselAdapter.PagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_banner_carousel_page, parent, false)
        (itemView as? ViewGroup)?.clipToPadding = false
        (itemView as? ViewGroup)?.clipChildren = false
        return PagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        val realPosition = getRealPosition(position, data.size)
        val model = data.getOrNull(realPosition) ?: return

        imageLoader.load(holder.bannerImage, model.imgUrl, R.drawable.ic_default_icon_placeholder)

        if (showStatus) {
            holder.bookComplete.visibility = View.VISIBLE
            holder.bookComplete.setImageResource(
                when (model.status) {
                    BannerStatus.COMPLETED -> R.drawable.ic_completed
                    BannerStatus.ONGOING -> R.drawable.ic_ongoing
                }
            )
        } else {
            holder.bookComplete.visibility = View.GONE
        }

        if (showBottom) {
            holder.bottomLayout.visibility = View.VISIBLE
            holder.readerStarView.text =
                model.score ?: holder.itemView.context.getString(R.string.bcv_default_score)
            holder.readerNumView.text =
                model.views ?: holder.itemView.context.getString(R.string.bcv_default_views)
        } else {
            holder.bottomLayout.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(realPosition, model)
        }
    }

    override fun getItemCount(): Int = if (data.isNotEmpty()) Int.MAX_VALUE else 0

    class PagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bannerImage: ImageView = itemView.findViewById(R.id.img_banner)
        val bookComplete: ImageView = itemView.findViewById(R.id.image_book_completed)
        val readerNumView: TextView = itemView.findViewById(R.id.readerNumView)
        val readerStarView: TextView = itemView.findViewById(R.id.readerStarView)
        val bottomLayout: View = itemView.findViewById(R.id.bottom_layout)
    }

    companion object {
        fun getRealPosition(position: Int, pageSize: Int): Int {
            if (pageSize == 0) return 0
            return (position + pageSize) % pageSize
        }

        fun getOriginalPosition(pageSize: Int): Int {
            if (pageSize == 0) return 0
            return Int.MAX_VALUE / 2 - ((Int.MAX_VALUE / 2) % pageSize)
        }
    }
}

