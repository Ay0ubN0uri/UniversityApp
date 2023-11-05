package com.a00n.universityapp.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.a00n.universityapp.data.entities.Filiere

class FiliereItemCallBack : DiffUtil.ItemCallback<Filiere>() {
    override fun areItemsTheSame(oldItem: Filiere, newItem: Filiere): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Filiere, newItem: Filiere): Boolean {
        return oldItem == newItem
    }
}