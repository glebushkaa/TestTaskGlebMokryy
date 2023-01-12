package ua.gleb.android.tesktaskglebmokryy.domain.useCases

import ua.gleb.android.tesktaskglebmokryy.data.BMICalculator
import javax.inject.Inject

class CalculateBMIUseCase @Inject constructor(
    private val bmiCalculator: BMICalculator
) {
    fun calculateBMI(height: Int, weight: Int) =
        bmiCalculator.calculateBMI(height = height, weight = weight)
}