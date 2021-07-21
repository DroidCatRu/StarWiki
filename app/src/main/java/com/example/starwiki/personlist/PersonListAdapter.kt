package com.example.starwiki.personlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starwiki.data.models.PersonDB
import com.example.starwiki.databinding.PersonItemBinding

class PersonListAdapter(private val clickListener: PersonListener) : ListAdapter<PersonDB,
    PersonListAdapter.ViewHolder>(PersonDiffCallback()) {

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position)!!, clickListener)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.from(parent)
  }

  class ViewHolder private constructor(private val binding: PersonItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PersonDB, clickListener: PersonListener) {
      binding.person = item
      binding.clickListener = clickListener
      binding.executePendingBindings()
    }

    companion object {

      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = PersonItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
      }
    }
  }
}

class PersonDiffCallback : DiffUtil.ItemCallback<PersonDB>() {

  override fun areItemsTheSame(oldItem: PersonDB, newItem: PersonDB): Boolean {
    return oldItem.personId == newItem.personId
  }

  override fun areContentsTheSame(oldItem: PersonDB, newItem: PersonDB): Boolean {
    return oldItem == newItem
  }
}

class PersonListener(val clickListener: (person: PersonDB) -> Unit) {

  fun onClick(person: PersonDB) = clickListener(person)
}