package ua.gleb.android.tesktaskglebmokryy.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class BMIEntity(
    val bodyMassIndex: Double,
    val ponderalIndex: Double,
) : Parcelable