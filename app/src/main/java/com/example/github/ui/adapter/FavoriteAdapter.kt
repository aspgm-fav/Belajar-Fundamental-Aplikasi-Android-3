package com.example.github.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.github.ui.activity.DetailActivity
import com.example.github.R
import com.example.github.data.model.User
import kotlinx.android.synthetic.main.list_item.view.*

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var onItemClickCallback: OnitemClickCallback? = null

    var listFavorite = ArrayList<User>()

        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceType")
        fun bind(userModel: User){
            with(itemView){
                tvUsername.text = userModel.login

                Glide.with(context)
                    .load(userModel.avatar)
                    .apply(RequestOptions().override(60, 60))
                    .into(civAvatar)

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(userModel)
                    val intent = Intent(context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_STATE, userModel)
                        putExtra(DetailActivity.EXTRA_FAV, "favorite")
                    }
                    context.startActivity(intent)
                    Toast.makeText(context, userModel.login, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    interface OnitemClickCallback {
        fun onItemClicked(data: User)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return FavoriteViewHolder(mView)
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

}