package com.example.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.ItemsItem
import com.example.githubuser.R
import com.example.githubuser.activity.DetailActivity

class UserAdapter(private val listUser: List<ItemsItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.findViewById(R.id.tvItem)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_user, viewGroup, false)
        )

    override fun getItemCount() = listUser.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.tvItem.text = listUser[position].login
        Glide.with(viewHolder.imageView.context).load(listUser[position].avatarUrl)
            .into(viewHolder.imageView)

        viewHolder.itemView.setOnClickListener {
            val context = viewHolder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USERNAME, listUser[position].login)
            intent.putExtra(DetailActivity.EXTRA_URL, listUser[position].avatarUrl)
            context.startActivity(intent)
        }
    }
}