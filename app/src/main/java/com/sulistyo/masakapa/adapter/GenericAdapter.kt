package com.sulistyo.masakapa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

class GenericAdapter<T : Any>(
    @LayoutRes private val layoutId: Int,
    private val bindingInterface: GenericAdapterBindingInterface<T>
) : RecyclerView.Adapter<GenericAdapter<T>.ViewHolder>() {

    var dataList: MutableList<T> = arrayListOf()

    fun updateData(list: List<T>, isClear: Boolean) {
        if (isClear) this.dataList = arrayListOf()
        this.dataList.addAll(list)
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
        return bindingInterface.getDataId(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            bindingInterface.bindData(dataList[position], view)
        }
    }

}

interface GenericAdapterBindingInterface<T : Any> {
    fun bindData(item: T, view: View)
    fun getDataId(item: T): Int
}