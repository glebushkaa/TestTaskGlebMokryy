package ua.gleb.android.tesktaskglebmokryy.data

import android.util.Log
import ua.gleb.android.tesktaskglebmokryy.domain.entities.BMIEntity
import javax.inject.Inject

class BMICalculator @Inject constructor() {

    fun calculateBMI(height: Int, weight: Int): BMIEntity {
        val heightDouble = height.toDouble().div(100)
        val weightDouble = weight.toDouble()

        val bodyMassIndex = weightDouble.div(
            heightDouble.times(heightDouble)
        )
        val ponderalIndex = weightDouble.div(
            heightDouble.times(heightDouble).times(heightDouble)
        )
        return BMIEntity(bodyMassIndex = bodyMassIndex, ponderalIndex = ponderalIndex)
    }

}