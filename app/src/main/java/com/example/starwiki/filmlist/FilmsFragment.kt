package com.example.starwiki.filmlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwiki.R
import com.example.starwiki.SWApp
import com.example.starwiki.databinding.FilmsFragmentBinding
import com.google.android.material.snackbar.Snackbar

class FilmsFragment : Fragment() {

  private var _binding: FilmsFragmentBinding? = null
  private val binding get() = _binding!!

  private var ableToNavigate = true

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FilmsFragmentBinding.inflate(inflater, container, false)

    binding.lifecycleOwner = this

    val viewModel = ViewModelProvider(
      this, FilmViewModel.FACTORY(
        requireActivity().application as SWApp
      )
    )
      .get(FilmViewModel::class.java)

    val adapter = FilmListAdapter(FilmListener {
      if (ableToNavigate && findNavController().currentDestination?.id == R.id.FirstFragment) {
        ableToNavigate = false
        findNavController().navigate(
          FilmsFragmentDirections.actionFirstFragmentToSecondFragment(
            it.title,
            it
          )
        )
      }
    })

    binding.filmList.adapter = adapter
    binding.filmList.layoutManager = LinearLayoutManager(requireContext())

    val dividerItemDecoration = DividerItemDecoration(
      context,
      (binding.filmList.layoutManager as LinearLayoutManager).orientation
    )
    ResourcesCompat.getDrawable(resources, R.drawable.list_divider, null)?.let {
      dividerItemDecoration.setDrawable(it)
    }
    binding.filmList.addItemDecoration(dividerItemDecoration)

    binding.filmList.setHasFixedSize(true)

    viewModel.isConnected.observe(viewLifecycleOwner, { connected ->
      connected?.let {
        if (connected) {
          viewModel.online()
        } else {
          viewModel.offline()
        }
      }
    })

    viewModel.films.observe(viewLifecycleOwner, {
      it?.let {
        if (it.isEmpty() && binding.filmList.isEmpty()) {
          viewModel.loadFilms()
        } else if (it.isNotEmpty()) {
          adapter.submitList(it)
        }
      }
    })

    viewModel.isUpdating.observe(viewLifecycleOwner, {
      it?.let {
        binding.refreshLayout.isRefreshing = it
      }
    })

    viewModel.toastMessage.observe(viewLifecycleOwner, {
      it?.let {
        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        viewModel.onToastShown()
      }
    })

    findNavController().addOnDestinationChangedListener { _, destination, _ ->
      if (destination.id == R.id.SecondFragment) {
        ableToNavigate = false
      }
      if (destination.id == R.id.FirstFragment) {
        ableToNavigate = true
      }
    }

    binding.refreshLayout.setOnRefreshListener {
      viewModel.refreshFilms()
    }
    return binding.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}