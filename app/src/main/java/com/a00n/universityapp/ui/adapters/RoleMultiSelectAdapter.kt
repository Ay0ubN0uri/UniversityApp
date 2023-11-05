package com.a00n.universityapp.ui.adapters


import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView

class RoleMultiSelectAdapter(private val data: MutableList<String>) : BaseAdapter(), Filterable {
    private val originalData: List<String> = ArrayList(data)
    private val itemFilter = ItemFilter()

    override fun getCount(): Int {
        return data.size // Return the number of items in the filtered data
    }

    override fun getItem(position: Int): String? {
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
        textView.text = getItem(position) // Set the text of the dropdown item to the current item in the filtered data
        return view
    }

    override fun getFilter(): Filter {
        return itemFilter // Get the custom filter used for filtering data in the AutoCompleteTextView
    }

    // Custom filter class responsible for filtering the data based on user input
    private inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            val filteredList = ArrayList<String>()

            if (TextUtils.isEmpty(constraint)) {
                // If the constraint is empty, show all original data items
                filteredList.addAll(originalData)
            } else {
                // Filter the original data based on the constraint (user input)
                for (item in originalData) {
//                    if (!selectedItems.contains(item) && item.toLowerCase().contains(constraint.toString().toLowerCase())) {
//                        // Add items to the filtered list that match the constraint and are not selected yet
//                        filteredList.add(item)
//                    }
                }
            }

            filterResults.values = filteredList
            filterResults.count = filteredList.size
            return filterResults
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val filteredList = results?.values as? List<String>
            if (filteredList != null) {
                data.clear()
                data.addAll(filteredList)
                notifyDataSetChanged()
            }
        }
    }
}