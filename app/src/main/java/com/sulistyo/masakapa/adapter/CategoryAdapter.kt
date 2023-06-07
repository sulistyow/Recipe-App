package com.sulistyo.masakapa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.sulistyo.masakapa.model.Category
import java.util.*

class CategoryAdapter(
    @LayoutRes private val layoutId: Int,
    private val bindingInterface: CategoryAdapterBindingInterface<Category>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>(),Filterable {

    var dataList: MutableList<Category> = arrayListOf()
    var filteredData: MutableList<Category> = arrayListOf()

    init {
        filteredData = dataList
    }

    fun updateData(list: List<Category>, isClear: Boolean) {
        if (isClear) this.dataList = arrayListOf()
        this.dataList.addAll(list)
        this.filteredData = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return bindingInterface.getDataId(filteredData[position])
    }

    override fun getItemCount(): Int = filteredData.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            bindingInterface.bindData(filteredData[position], view)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString: String = constraint.toString()
                filteredData = (if (charString.isEmpty()) {
                    dataList
                } else {
                    val filteredList: MutableList<Category> = arrayListOf()
                    for (s: Category in dataList) {
                        when {
                            s.strCategory.lowercase(Locale.getDefault())
                                .contains(charString.lowercase(Locale.getDefault())) -> filteredList.add(s)
                        }
                    }
                    filteredList
                })
                val filterResults = FilterResults()
                filterResults.values = filteredData
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredData = results?.values as MutableList<Category>
                notifyDataSetChanged()
            }

        }

    }

}

interface CategoryAdapterBindingInterface<T : Category> {
    fun bindData(item: T, view: View)
    fun getDataId(item: T): Int
}