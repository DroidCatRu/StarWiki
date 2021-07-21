package com.example.starwiki.personlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starwiki.R
import com.example.starwiki.SWApp
import com.example.starwiki.databinding.PersonsFragmentBinding
import com.google.android.material.snackbar.Snackbar

class PersonsFragment : Fragment() {

  private var _binding: PersonsFragmentBinding? = null
  private val binding get() = _binding!!

  private var episodeId = 0

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    _binding = PersonsFragmentBinding.inflate(inflater, container, false)

    episodeId = PersonsFragmentArgs.fromBundle(requireArguments()).episodeId

    val adapter = PersonListAdapter(personClickListener)

    binding.personList.adapter = adapter
    binding.personList.layoutManager = LinearLayoutManager(requireContext())

    val dividerItemDecoration = DividerItemDecoration(
      context,
      (binding.personList.layoutManager as LinearLayoutManager).orientation
    )

    ResourcesCompat.getDrawable(resources, R.drawable.list_divider, null)?.let {
      dividerItemDecoration.setDrawable(it)
    }

    binding.personList.addItemDecoration(dividerItemDecoration)
    binding.personList.setHasFixedSize(true)

    binding.lifecycleOwner = this

    val viewModel = ViewModelProvider(
      this, PersonViewModel.FACTORY(
        episodeId,
        requireActivity().application as SWApp
      )
    ).get(PersonViewModel::class.java)

    viewModel.toastMessage.observe(viewLifecycleOwner, {
      it?.let {
        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        viewModel.onToastShown()
      }
    })

    viewModel.isConnected.observe(viewLifecycleOwner, { connected ->
      connected?.let {
        if (connected) {
          viewModel.online()
        } else {
          viewModel.offline()
        }
      }
    })

    viewModel.isLoading.observe(viewLifecycleOwner, {
      it?.let {
        binding.refreshLayout.isRefreshing = it
      }
    })

    viewModel.persons.observe(viewLifecycleOwner, {
      it?.let {
        if (it.isEmpty() && binding.personList.isEmpty()) {
          viewModel.loadPersons()
        } else if (it.isNotEmpty()) {
          adapter.submitList(it)
        }
      }
    })

    binding.refreshLayout.setOnRefreshListener {
      viewModel.refreshPersons()
    }

    return binding.root

  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private val personClickListener = PersonListener {
    Snackbar.make(binding.root, "Person clicked: ${it.name}", Snackbar.LENGTH_SHORT).show()
  }
}