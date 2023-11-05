package com.a00n.universityapp.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.a00n.universityapp.R
import com.a00n.universityapp.data.entities.Student

class StudentDropdownAdapter(private var data: MutableList<Student>) : BaseAdapter(), Filterable {

    fun setData(students: MutableList<Student>) {
        data = students
    }

    override fun getCount(): Int {
        return data.count()
    }

    override fun getItem(position: Int): Student {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.filiere_dropdown_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.filiereDropdownTextView)
        textView.text =
            "${getItem(position).firstName.capitalize()} ${getItem(position).lastName.capitalize()}"
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val res = FilterResults()
                res.values = data
                return res
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.i("info", "publishResults: hello man")
            }

        }
    }
}