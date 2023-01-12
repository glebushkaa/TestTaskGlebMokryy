package ua.gleb.android.tesktaskglebmokryy.presenter.fragments

import android.content.Intent
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ua.gleb.android.tesktaskglebmokryy.R
import ua.gleb.android.tesktaskglebmokryy.databinding.FragmentDetailsBinding
import ua.gleb.android.tesktaskglebmokryy.databinding.NativeAdBinding
import ua.gleb.android.tesktaskglebmokryy.domain.entities.BMIEntity
import ua.gleb.android.tesktaskglebmokryy.presenter.fragments.CalculateFragment.Companion.BMI_BUNDLE
import ua.gleb.android.tesktaskglebmokryy.presenter.fragments.CalculateFragment.Companion.NAME_BUNDLE
import ua.gleb.android.tesktaskglebmokryy.presenter.viewModels.DetailsViewModel


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val binding by viewBinding<FragmentDetailsBinding>()
    private val viewModel by viewModels<DetailsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getArgs()
        viewModel.validateBMI()
        initObservers()
        binding.initClickListeners()
        binding.setDetailsCardViews()
        setupAd()
    }

    private fun FragmentDetailsBinding.setDetailsCardViews() {
        statusBMIText.text = viewModel.changeStatusText(getString(R.string.status_index))
        ponderalIndexText.text = viewModel.changePonderalIndexText(
            getString(R.string.ponderal_index)
        )
        mainPart.text = viewModel.changeMainTextFontSize(viewModel.formatBodyMassIndexText())
    }

    private fun setupAd() {
        val adLoader = AdLoader.Builder(requireContext(), NATIVE_AD_TEST_ID)
            .forNativeAd { ad -> setAdViews(ad) }.build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun setAdViews(ad: NativeAd) {
        val adBinding = NativeAdBinding.inflate(layoutInflater)
        adBinding.apply {
            bodyText.text = ad.body
            adView.bodyView = bodyText

            headText.text = ad.headline
            adView.bodyView = headText

            adIcon.setImageDrawable(ad.icon?.drawable)
            adView.iconView = adIcon

            adView.callToActionView = installBtn

            adView.setNativeAd(ad)
            binding.adContainer.removeAllViews()
            binding.adContainer.addView(adView)
        }
    }

    private fun FragmentDetailsBinding.initClickListeners() {
        shareBtn.setOnClickListener {
            lifecycleScope.launch {
                viewModel.cachePicture(bmiDetailsContainer.drawToBitmap())
                viewModel.getCachedPictureUri()
            }
        }
        rateBtn.setOnClickListener {
            startPlayStore()
        }
        backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservers() {
        viewModel.cacheUriLiveData.observe(viewLifecycleOwner) {
            shareDetails(it)
        }
    }

    private fun startPlayStore() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(PLAY_STORE_LINK)
        startActivity(intent)
    }

    private fun shareDetails(uri: Uri) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = PICTURE_SHARE_TYPE
            flags = FLAG_GRANT_READ_URI_PERMISSION or FLAG_GRANT_WRITE_URI_PERMISSION
        }
        startActivity(
            Intent.createChooser(shareIntent, getString(R.string.share_via))
        )
    }

    private fun getArgs() {
        arguments?.let {
            viewModel.bmiEntity = if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(BMI_BUNDLE, BMIEntity::class.java)
            } else {
                it.getParcelable(BMI_BUNDLE)
            }
            viewModel.name = it.getString(NAME_BUNDLE)
        }
    }

    private companion object {
        const val PICTURE_SHARE_TYPE = "image/*"
        const val PLAY_STORE_LINK = "https://play.google.com"
        const val NATIVE_AD_TEST_ID = "ca-app-pub-3940256099942544/2247696110"
    }
}