package ua.gleb.android.tesktaskglebmokryy.presenter.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.gleb.android.tesktaskglebmokryy.domain.entities.BMIEntity
import ua.gleb.android.tesktaskglebmokryy.domain.useCases.CalculateBMIUseCase
import javax.inject.Inject

@HiltViewModel
class CalculateViewModel @Inject constructor(
    private val calculateBMIUseCase: CalculateBMIUseCase
) : ViewModel() {

    var bmiEntity: BMIEntity? = null

    var savedWeight: Int? = null
    var savedHeight: Int? = null

    fun calculateBMI(height: Int, weight: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            bmiEntity = calculateBMIUseCase.calculateBMI(height, weight)
        }
    }

    fun validateName(name: String) = name.trim().isNotEmpty()
}