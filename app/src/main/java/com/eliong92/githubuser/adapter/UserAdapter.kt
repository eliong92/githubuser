package com.eliong92.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eliong92.githubuser.R
import com.eliong92.githubuser.usecase.UserViewObject
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val items: MutableList<UserViewObject> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindVenue(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun refresh(items: List<UserViewObject>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name = itemView.name_txt
        private val avatar = itemView.avatar

        fun bindVenue(user: UserViewObject) {
            name.text = user.name
            Glide.with(itemView)
                .load(user.avatar)
                .centerCrop()
                .into(avatar)
        }
    }
}