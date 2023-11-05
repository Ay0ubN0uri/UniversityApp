package com.a00n.universityapp.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.a00n.universityapp.data.entities.Role

class RoleItemCallBack : DiffUtil.ItemCallback<Role>() {
    override fun areItemsTheSame(oldItem: Role, newItem: Role): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Role, newItem: Role): Boolean {
        return oldItem == newItem
    }
}