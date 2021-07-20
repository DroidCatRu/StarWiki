package com.example.starwiki.personlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.starwiki.SWApp
import com.example.starwiki.databinding.PersonsFragmentBinding
import com.example.starwiki.filmlist.FilmViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class PersonsFragment : Fragment() {

  private var _binding: PersonsFragmentBinding? = null

  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  private var episodeId = 0

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {

    _binding = PersonsFragmentBinding.inflate(inflater, container, false)
    binding.lifecycleOwner = this

    episodeId = PersonsFragmentArgs.fromBundle(requireArguments()).episodeId

    binding.textviewSecond.text = episodeId.toString()
    binding.executePendingBindings()

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

    viewModel.isLoading.observe(viewLifecycleOwner, {
      it?.let {
        Log.d("Button enabled", (!it).toString())
        binding.buttonSecond.isEnabled = !it
        binding.executePendingBindings()
      }
    })

    viewModel.persons.observe(viewLifecycleOwner, {
      it?.let {
        Snackbar.make(binding.root, "Persons: ${it.size}", Snackbar.LENGTH_SHORT).show()
      }
    })

    binding.buttonSecond.setOnClickListener {
      viewModel.loadPersons()
    }

    return binding.root

  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}