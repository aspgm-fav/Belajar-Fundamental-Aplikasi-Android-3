package com.example.favoritelist.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.favoritelist.R
import com.example.favoritelist.data.model.User
import kotlinx.android.synthetic.main.list_item.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var onItemClickCallback: OnitemClickCallback? = null

    var listFavorite = ArrayList<User>()
        set(listFavorite){
            if (listFavorite.size > 0){
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    inner class FavoriteViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        fun bind(userModel: User) {
            with(itemView){
                tvUsername.text = userModel.login
                tvName.text = userModel.name

                Glide.with(context)
                    .load(userModel.avatar)
                    .apply(RequestOptions().override(60, 60))
                    .into(civAvatar)

                itemView.setOnClickListener{
                    onItemClickCallback?.onItemClicked(userModel)
                    Toast.makeText(context, "${userModel.login}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    interface OnitemClickCallback {
        fun onItemClicked(data: User)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return FavoriteViewHolder (mView)
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

}