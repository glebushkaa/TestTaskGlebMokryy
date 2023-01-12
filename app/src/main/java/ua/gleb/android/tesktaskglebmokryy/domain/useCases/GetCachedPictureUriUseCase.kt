package ua.gleb.android.tesktaskglebmokryy.domain.useCases

import ua.gleb.android.tesktaskglebmokryy.data.PictureFileHandler
import javax.inject.Inject

class GetCachedPictureUriUseCase @Inject constructor(
    private val pictureFileHandler: PictureFileHandler
) {
    suspend fun getCachedPictureUri() = pictureFileHandler.getCachedPictureUri()
}