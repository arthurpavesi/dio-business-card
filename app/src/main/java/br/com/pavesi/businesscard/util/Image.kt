package br.com.pavesi.businesscard.util

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import br.com.pavesi.businesscard.R
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class Image {

    companion object {

        fun share(context: Context, view: View) {
            val bitmap = getScreenShotFromView(view)

            bitmap?.let {
                saveMediaStorage(context, bitmap)
            }
        }

        private fun getScreenShotFromView(view: View): Bitmap? {
            var screenShot: Bitmap? = null

            try {
                screenShot = Bitmap.createBitmap(view.measuredWidth,
                    view.measuredHeight,
                    Bitmap.Config.ARGB_8888)

                val canvas = Canvas(screenShot)
                view.draw(canvas)
            } catch (e: Exception) {
                Log.e("Error -> ", "Failed to capture image : ", e)
            }

            return screenShot
        }

        private fun saveMediaStorage(context: Context, bitmap: Bitmap) {
            val filename = "${System.currentTimeMillis()}.jpg"

            var fos: OutputStream? = null

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver?.also { resolver ->

                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                    }

                    val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    fos = imageUri?.let {
                        shareIntent(context, imageUri)
                        resolver.openOutputStream(it)
                    }
                }

            } else {
                val imageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val image = File(imageDir, filename)
                shareIntent(context, Uri.fromFile(image))
                fos = FileOutputStream(image)
            }

            fos?.use {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

                Toast.makeText(context, R.string.label_success_capturing_image, Toast.LENGTH_SHORT).show()
            }

        }

        private fun shareIntent(context: Context, imageUri: Uri) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, imageUri)
                type = "image/jpg"
            }

            context.startActivity(Intent.createChooser(shareIntent,
                context.resources.getText(R.string.label_share)))
        }

    }
}