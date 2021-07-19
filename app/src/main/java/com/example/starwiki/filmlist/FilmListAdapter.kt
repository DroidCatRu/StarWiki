package com.example.starwiki.filmlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.starwiki.data.Film
import com.example.starwiki.databinding.FilmItemBinding

class FilmListAdapter(val clickListener: FilmListener) : ListAdapter<Film,
    FilmListAdapter.ViewHolder>(FilmDiffCallback()) {

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    holder.bind(getItem(position)!!, clickListener)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder.from(parent)
  }

  class ViewHolder private constructor(val binding: FilmItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Film, clickListener: FilmListener) {
      binding.film = item
      binding.clickListener = clickListener
      binding.executePendingBindings()
    }

    companion object {

      fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = FilmItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(view)
      }
    }
  }
}

class FilmDiffCallback : DiffUtil.ItemCallback<Film>() {

  override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean {
    return oldItem.episodeId == newItem.episodeId
  }

  override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean {
    return oldItem == newItem
  }
}

class FilmListener(val clickListener: (film: Film) -> Unit) {

  fun onClick(film: Film) = clickListener(film)
}