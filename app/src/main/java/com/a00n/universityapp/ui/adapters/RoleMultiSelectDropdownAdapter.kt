package com.a00n.universityapp.ui.adapters

import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.a00n.universityapp.data.entities.Role

class RoleMultiSelectDropdownAdapter(private var data: MutableList<Role> = mutableListOf<Role>()) :
    BaseAdapter(),
    Filterable {

    private var originalData: List<Role> = ArrayList(data)
    var selectedItems = mutableSetOf<Role>()
    private val roleFilter = RoleFilter()

    fun setOriginalData(roles: List<Role>) {
        originalData = roles
    }


    fun setData(roles: MutableList<Role>) {
        data = roles
        originalData = data
    }

    override fun getCount(): Int {
        return data.size // Return the number of items in the filtered data
    }

    override fun getItem(position: Int): Role {
        return data[position] // Get an item from the filtered data at a given position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // Get the ID of an item in the filtered data at a given position
    }

    // Method responsible for creating and updating the view for each item in the dropdown
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text =
            getItem(position).name // Set the text of the dropdown item to the current item in the filtered data
        return view
    }


    override fun getFilter(): Filter {
        return roleFilter
    }

    private inner class RoleFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            Log.i("info", "ay0ub: $originalData")
            val filteredResults = mutableListOf<Role>()
            val results = FilterResults()
            if (TextUtils.isEmpty(constraint)) {
                filteredResults.addAll(originalData)
            } else {
                Log.i("info", "performFiltering: $selectedItems")
                for (item in originalData) {
                    if (!selectedItems.contains(item) && item.name.toLowerCase()
                            .contains(constraint.toString().toLowerCase())
                    ) {
                        // Add items to the filtered list that match the constraint and are not selected yet
                        filteredResults.add(item)
                    }
                }
            }
            results.values = filteredResults
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val filteredList = results?.values as? List<Role>
            Log.i("info", "publishResults: $filteredList")
            if (filteredList != null) {
                data.clear()
                data.addAll(filteredList)
                notifyDataSetChanged()
            }
        }

    }
}