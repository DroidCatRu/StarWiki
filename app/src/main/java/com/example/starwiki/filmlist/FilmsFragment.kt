package com.example.starwiki.filmlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwiki.data.SWDatabase
import com.example.starwiki.data.SWRepository
import com.example.starwiki.data.getNetworkService
import com.example.starwiki.databinding.FilmsFragmentBinding

class FilmsFragment : Fragment() {

  private var _binding: FilmsFragmentBinding? = null
  private val binding get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FilmsFragmentBinding.inflate(inflater, container, false)

    val adapter = FilmListAdapter(FilmListener { id ->
      Toast.makeText(context, "$id", Toast.LENGTH_SHORT).show()
    })

    binding.filmList.adapter = adapter
    binding.filmList.layoutManager = LinearLayoutManager(requireContext())
    binding.filmList.setHasFixedSize(true)

    val database = SWDatabase.getInstance(requireActivity())
    val repository = SWRepository(getNetworkService(), database.filmDao)
    val viewModel = ViewModelProvider(this, FilmViewModel.FACTORY(repository, requireActivity().application))
      .get(FilmViewModel::class.java)

    binding.lifecycleOwner = this

    viewModel.films.observe(viewLifecycleOwner, {
      it?.let {
        if (it.isEmpty()) {
          viewModel.loadFilms()
        }
        adapter.submitList(it)
      }
    })

    viewModel.isUpdating.observe(viewLifecycleOwner, {
      it?.let {
        binding.refreshLayout.isRefreshing = it
      }
    })

    binding.refreshLayout.setOnRefreshListener {
      viewModel.loadFilms()
    }
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}