package com.scorp.pagination_example.feature

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.scorp.domain.Person
import com.scorp.pagination_example.databinding.ItemRecyclerviewBinding

class MainActivityRecyclerViewAdapter(
    private val context: Context,
    private var mutableListPerson: MutableList<Person?>
) :
    RecyclerView.Adapter<MainActivityRecyclerViewAdapter.MainRecyclerViewHolder>() {

    internal var onItemSelected: (position: Int, item: Person?) -> Unit =
        { _, _ -> }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
        val binding = ItemRecyclerviewBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainRecyclerViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return mutableListPerson.size
    }

    override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
        mutableListPerson[position]?.let {
            holder.bindItems(
                context,
                holder,
                it,
                position,
                onItemSelected
            )
        }

    }

    class MainRecyclerViewHolder(private val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(
            context: Context,
            holder: MainRecyclerViewHolder,
            item: Person,
            position: Int,
            onItemSelected: (Int, Person) -> Unit
        ) {
            val itemText = item.fullName + " " + item.id
            binding.textViewRecyclerViewName.text = itemText
        }
    }

    fun updateDataSource(
        newDataSource: MutableList<Person?>
    ) {
        this.mutableListPerson = newDataSource
        notifyDataSetChanged()
    }

}
