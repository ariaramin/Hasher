package com.ariaramin.hasher.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ariaramin.hasher.R
import com.ariaramin.hasher.databinding.FragmentHomeBinding
import com.ariaramin.hasher.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setAutoCompleteTextView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.generateButton.setOnClickListener {
            onButtonClicked()
        }
    }

    private fun onButtonClicked() {
        if (binding.editTextText.text.isEmpty()) {
            showErrorSnackBar(getString(R.string.empty_field_error))
        } else {
            lifecycleScope.launch {
                applyAnimations()
                navigateToSuccessFragment(getHashData())
            }
        }
    }

    private fun showErrorSnackBar(message: String) {
        val snackBar = Snackbar.make(
            binding.rootLayout,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackBar.setAction(getString(R.string.ok)) {}
        snackBar.setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.red))
        snackBar.setActionTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackBar.show()
    }

    private fun navigateToSuccessFragment(hash: String) {
        val direction = HomeFragmentDirections.actionNavigationHomeToSuccessFragment(hash)
        findNavController().navigate(direction)
    }

    private fun getHashData(): String {
        val algorithm = binding.autoCompleteTextView.text.toString()
        val text = binding.editTextText.text.toString()
        return viewModel.getHash(algorithm, text)
    }

    private suspend fun applyAnimations() {
        // disable the button
        binding.generateButton.isClickable = false

        // Textview and button animations
        binding.textView.animate().alpha(0f).duration = 400L
        binding.textView2.animate().alpha(0f).duration = 400L
        binding.generateButton.animate().alpha(0f).duration = 400L

        // Edit text animations
        binding.algorithmTextInputLayout.animate()
            .alpha(0f)
            .translationXBy(1400f)
            .duration = 400L
        binding.editTextText.animate()
            .alpha(0f)
            .translationXBy(-1400f)
            .duration = 400L

        delay(300)

        // success background animations
        binding.successBackground.animate().alpha(1f).duration = 400L
        binding.successBackground.animate().rotationBy(720f).duration = 600L
        binding.successBackground.animate().scaleXBy(900f).duration = 800L
        binding.successBackground.animate().scaleYBy(900f).duration = 800L

        // success animation view
        binding.successAnimationView.animate().alpha(1f).duration = 1000L
        binding.successAnimationView.playAnimation()

        delay(2000)

        binding.successAnimationView.animate().alpha(0f).duration = 1000L
        binding.successAnimationView.pauseAnimation()
    }

    private fun setAutoCompleteTextView() {
        val hashAlgorithms = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.drop_down_item_layout, hashAlgorithms)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.setDropDownBackgroundResource(R.color.primary_color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}