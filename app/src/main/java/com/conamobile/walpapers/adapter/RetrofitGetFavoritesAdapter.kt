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
import com.conamobile.walpapers.models.RoomModel.UserModel
import com.conamobile.walpapers.util.RandomColor
import com.conamobile.walpapers.util.TransferFavoriteImage
import com.google.android.material.card.MaterialCardView

class RetrofitGetFavoritesAdapter(
    private val context: Context, private val lists: ArrayList<UserModel>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var itemCLick: ((UserModel) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val home = lists[position]

        if (holder is HomeViewHolder) {
            val imageItem = holder.recyclerItemImage
            val cardItem = holder.recyclerItemCardView

            Glide.with(context)
                .load(home.smallLink)
                .placeholder(RandomColor.randomColor())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageItem)

            //animation
            val animation: Animation = AnimationUtils.loadAnimation(context,
                if (position > 2) R.anim.recycler_up_from_bottom else R.anim.recycler_down_from_top)
            holder.itemView.startAnimation(animation)

            cardItem.setOnClickListener {
                TransferFavoriteImage.transferImage = imageItem.drawable.toBitmap()
                TransferFavoriteImage.id = home.id
                TransferFavoriteImage.smallLink = home.smallLink
                TransferFavoriteImage.bigLink = home.bigLink
                TransferFavoriteImage.date = home.date
                itemCLick.invoke(home)
            }

        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.itemView.clearAnimation()
    }


    override fun getItemCount(): Int = lists.size

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var recyclerItemImage: AppCompatImageView = view.findViewById(R.id.recyclerItemImageView)
        var recyclerItemCardView: MaterialCardView = view.findViewById(R.id.recyclerItemCardView)
    }
}