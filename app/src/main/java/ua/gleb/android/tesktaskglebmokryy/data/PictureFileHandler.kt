package ua.gleb.android.tesktaskglebmokryy.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.gleb.android.tesktaskglebmokryy.R
import ua.gleb.android.tesktaskglebmokryy.utils.tag
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PictureFileHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun cachePicture(bitmap: Bitmap) =
        suspendCoroutine { continuation ->
            runCatching {
                val file = File(
                    context.filesDir.absolutePath, BMI_DETAILS_FILE_NAME
                )
                if (file.exists()) file.delete()
                val fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.flush()
                fos.close()
                continuation.resume(Unit)
            }.onFailure {
                Log.e(
                    this@PictureFileHandler.tag(),
                    context.getString(R.string.picture_cache_exception),
                    it
                )
                continuation.resume(Unit)
            }
        }

    suspend fun getCachedPictureUri() = suspendCoroutine<Uri> { continuation ->
        runCatching {
            val file = File(context.filesDir.absolutePath, BMI_DETAILS_FILE_NAME)
            val uri = FileProvider.getUriForFile(
                context, PROVIDER_AUTHORITY, file
            )
            continuation.resume(uri)
        }.onFailure {
            Log.e(
                this@PictureFileHandler.tag(),
                context.getString(R.string.path_out_provider_exception),
                it
            )
            continuation.resumeWithException(it)
        }
    }

    private companion object {
        const val BMI_DETAILS_FILE_NAME = "bmi_detaisl.png"
        const val PROVIDER_AUTHORITY = "ua.gleb.android.fileprovider.tesktaskglebmokryy"
    }

}