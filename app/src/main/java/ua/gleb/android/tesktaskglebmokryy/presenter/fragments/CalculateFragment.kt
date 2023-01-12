package ua.gleb.android.tesktaskglebmokryy.presenter.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.NumberPicker
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ua.gleb.android.tesktaskglebmokryy.R
import ua.gleb.android.tesktaskglebmokryy.databinding.FragmentCalculateBinding
import ua.gleb.android.tesktaskglebmokryy.presenter.viewModels.CalculateViewModel
import ua.gleb.android.tesktaskglebmokryy.utils.tag

@AndroidEntryPoint
class CalculateFragment : Fragment(R.layout.fragment_calculate) {

    private val binding by viewBinding<FragmentCalculateBinding>()
    private val viewModel by viewModels<CalculateViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.initClickListeners()
        binding.setupGendersPicker()
        binding.setupHeightPicker()
        binding.setupWeightPicker()
    }

    private fun FragmentCalculateBinding.initClickListeners() {
        calculateBtn.setOnClickListener {
            if (viewModel.validateName(nameEditText.text.toString())) {
                viewModel.savedWeight = weightPicker.value
                viewModel.savedHeight = heightPicker.value
                viewModel.calculateBMI(
                    height = heightPicker.value,
                    weight = weightPicker.value
                )
                setupInterstitial()
            } else {
                Snackbar.make(
                    requireView(), getString(R.string.enter_name), Snackbar.LENGTH_SHORT
                ).show()
            }
        }
        backBtn.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun FragmentCalculateBinding.setupHeightPicker() {
        heightPicker.apply {
            minValue = MIN_HEIGHT
            maxValue = MAX_HEIGHT
            value = viewModel.savedHeight ?: return
        }
    }

    private fun FragmentCalculateBinding.setupWeightPicker() {
        weightPicker.apply {
            minValue = MIN_WEIGHT
            maxValue = MAX_WEIGHT
            value = viewModel.savedWeight ?: return
        }
    }

    private fun FragmentCalculateBinding.setupGendersPicker() {
        genderPicker.apply {
            val gendersArray = resources.getStringArray(R.array.genders)
            displayedValues = gendersArray
            minValue = FIRST_GENDERS_ELEMENT_VALUE
            maxValue = gendersArray.size - 1
        }
    }

    private fun startDetailsFragment() {
        findNavController().navigate(
            R.id.calculate_to_details,
            bundleOf(
                BMI_BUNDLE to viewModel.bmiEntity,
                NAME_BUNDLE to binding.nameEditText.text.toString()
            )
        )
    }

    private fun setupInterstitial() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            requireContext(), INTERSTITIAL_AD_TEST_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.fullScreenContentCallback =
                        getAdContentListener()
                    interstitialAd.show(requireActivity())
                }
            }
        )
    }

    private fun getAdContentListener() = object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            startDetailsFragment()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            startDetailsFragment()
            Log.e(this@CalculateFragment.tag(), adError.cause.toString())
        }

        override fun onAdImpression() {
            startDetailsFragment()
        }
    }

    companion object {
        const val BMI_BUNDLE = "BMI_BUNDLE"
        const val NAME_BUNDLE = "NAME"

        private const val MIN_HEIGHT = 100
        private const val MAX_HEIGHT = 220

        private const val MIN_WEIGHT = 30
        private const val MAX_WEIGHT = 200

        private const val FIRST_GENDERS_ELEMENT_VALUE = 0

        private const val INTERSTITIAL_AD_TEST_ID = "ca-app-pub-3940256099942544/1033173712"
    }
}
