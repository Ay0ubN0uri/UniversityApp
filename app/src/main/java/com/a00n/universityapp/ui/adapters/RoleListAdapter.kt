package com.a00n.universityapp.ui.adapters

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.a00n.universityapp.R
import com.a00n.universityapp.data.entities.Role
import com.a00n.universityapp.databinding.RoleItemBinding
import com.google.android.material.snackbar.Snackbar

class RoleListAdapter(val onClickListener: (View, Role) -> Unit) :
    ListAdapter<Role, RoleListAdapter.RoleViewHolder>(RoleItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val binding = RoleItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RoleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, holder.itemView.context)
    }

    inner class RoleViewHolder(private val binding: RoleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(role: Role, context: Context) {
            binding.roleMaterialCardView.setOnClickListener { view ->
                this@RoleListAdapter.onClickListener(view, role)
            }
            binding.role = role
        }
    }
}