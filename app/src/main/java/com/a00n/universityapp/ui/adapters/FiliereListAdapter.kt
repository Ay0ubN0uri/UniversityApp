package com.a00n.universityapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.a00n.universityapp.data.entities.Filiere
import com.a00n.universityapp.databinding.FiliereItemBinding

class FiliereListAdapter(val onClickListener: (View, Filiere) -> Unit) :
    ListAdapter<Filiere, FiliereListAdapter.FiliereViewHolder>(FiliereItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiliereViewHolder {
        val binding = FiliereItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FiliereViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FiliereViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class FiliereViewHolder(private val binding: FiliereItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filiere: Filiere) {
            binding.filiereMaterialCardView.setOnClickListener { view ->
                onClickListener(view, filiere)
            }
            binding.filiere = filiere
        }
    }
}