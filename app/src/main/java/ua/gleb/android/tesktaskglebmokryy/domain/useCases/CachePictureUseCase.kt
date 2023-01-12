package ua.gleb.android.tesktaskglebmokryy.domain.useCases

import android.graphics.Bitmap
import ua.gleb.android.tesktaskglebmokryy.data.PictureFileHandler
import javax.inject.Inject

class CachePictureUseCase @Inject constructor(
    private val pictureFileHandler: PictureFileHandler
) {
    suspend fun cachePicture(bitmap: Bitmap) =
        pictureFileHandler.cachePicture(bitmap)
}