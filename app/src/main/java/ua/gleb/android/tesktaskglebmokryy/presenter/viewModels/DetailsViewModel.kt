package ua.gleb.android.tesktaskglebmokryy.presenter.viewModels

import android.graphics.Bitmap
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.gleb.android.tesktaskglebmokryy.domain.entities.BMIEntity
import ua.gleb.android.tesktaskglebmokryy.domain.useCases.CachePictureUseCase
import ua.gleb.android.tesktaskglebmokryy.domain.useCases.GetCachedPictureUriUseCase
import ua.gleb.android.tesktaskglebmokryy.presenter.fragments.DetailsFragment
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val cachePictureUseCase: CachePictureUseCase,
    private val getCachedPictureUriUseCase: GetCachedPictureUriUseCase
) : ViewModel() {

    var cacheUriLiveData = MutableLiveData<Uri>()

    var bmiEntity: BMIEntity? = null
    var name: String? = null
    var bmiStatus = NORMAL_BMI

    suspend fun cachePicture(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            cachePictureUseCase.cachePicture(bitmap)
        }
    }

    suspend fun getCachedPictureUri() {
        withContext(Dispatchers.IO) {
            cacheUriLiveData.postValue(
                getCachedPictureUriUseCase.getCachedPictureUri()
            )
        }
    }

    fun validateBMI() {
        val bodyMassIndex = bmiEntity?.bodyMassIndex ?: run {
            bmiStatus = IS_NOT_CALCULATED
            return
        }
        bmiStatus = when {
            bodyMassIndex in MIN_NORMAL_BMI..MAX_NORMAL_BMI -> NORMAL_BMI
            bodyMassIndex < MIN_NORMAL_BMI -> UNDERWEIGHT
            else -> OVERWEIGHT
        }
    }

    fun formatBodyMassIndexText() =
        DecimalFormat(INDEX_FORMAT).format(bmiEntity?.bodyMassIndex).toString()

    fun changePonderalIndexText(baseText: String) = baseText.replace(
        PONDERAL_SYMBOL, DecimalFormat(INDEX_FORMAT).format(bmiEntity?.ponderalIndex)
    )

    fun changeStatusText(baseText: String) =
        baseText.replace(NAME_SYMBOL, name.orEmpty().trim().uppercase())
            .replace(BMI_STATUS_SYMBOL, bmiStatus)

    fun changeMainTextFontSize(
        text: String
    ): SpannableStringBuilder {
        val smallSizeText = RelativeSizeSpan(MAIN_TEXT_SIZE_SPAN)
        val ssBuilder = SpannableStringBuilder(text)
        ssBuilder.setSpan(
            smallSizeText,
            MAIN_TEXT_START_VALUE,
            text.substringAfter(MAIN_TEXT_DIVIDER).length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return ssBuilder
    }

    private companion object {
        const val NORMAL_BMI = "NORMAL"
        const val OVERWEIGHT = "OVERWEIGHT"
        const val UNDERWEIGHT = "UNDERWEIGHT"

        const val IS_NOT_CALCULATED = "IS NOT CALCULATED"

        const val MIN_NORMAL_BMI = 18.5
        const val MAX_NORMAL_BMI = 25.0

        const val INDEX_FORMAT = "##.##"
        const val PONDERAL_SYMBOL = "+"
        const val NAME_SYMBOL = "*"
        const val BMI_STATUS_SYMBOL = "_"

        const val MAIN_TEXT_SIZE_SPAN = 2f
        const val MAIN_TEXT_START_VALUE = 0
        const val MAIN_TEXT_DIVIDER = '.'
    }
}