package com.a00n.universityapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.a00n.universityapp.data.entities.Student
import com.a00n.universityapp.databinding.StudentItemBinding


class StudentListAdapter(val onClickListener: (View, Student) -> Unit) :
    ListAdapter<Student, StudentListAdapter.StudentViewHolder>(StudentItemCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val binding = StudentItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class StudentViewHolder(private val binding: StudentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student) {
            binding.studentMaterialCardView.setOnClickListener { view ->
                this@StudentListAdapter.onClickListener(view, student)
            }
            binding.student = student
        }
    }
}