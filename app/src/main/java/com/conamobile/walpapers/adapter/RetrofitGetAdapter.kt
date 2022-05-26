package com.conamobile.walpapers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.conamobile.walpapers.R
import com.conamobile.walpapers.models.RandomAllModels.RandomAllModels
import com.conamobile.walpapers.util.RandomColor
import com.conamobile.walpapers.util.TransferImage
import com.google.android.material.card.MaterialCardView


class RetrofitGetAdapter(var context: Context, var items: ArrayList<RandomAllModels>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var itemCLick: ((RandomAllModels) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val home = items[position]

        if (holder is HomeViewHolder) {
            val imageItem = holder.recyclerItemImage
            val cardItem = holder.recyclerItemCardView

            Glide.with(context)
                .load(home.urls.small)
                .placeholder(RandomColor.randomColor())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageItem)

            //animation
            val animation: Animation = AnimationUtils.loadAnimation(context,
                if (position > 3) R.anim.recycler_up_from_bottom
                else R.anim.recycler_down_from_top)
            holder.itemView.startAnimation(animation)


            cardItem.setOnClickListener {
                TransferImage.smallLink = home.urls.small
                TransferImage.imageLink = home.urls.full
                itemCLick.invoke(home)
                try {
                    TransferImage.transferImage = imageItem.drawable.toBitmap()
                } catch (e: IllegalArgumentException) {
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var recyclerItemImage: AppCompatImageView = view.findViewById(R.id.recyclerItemImageView)
        var recyclerItemCardView: MaterialCardView = view.findViewById(R.id.recyclerItemCardView)
    }
}