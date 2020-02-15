package com.notarize.app

import android.os.Environment
import java.io.File

class FileUtilsImpl : FileUtils {

  companion object {

    private const val IMAGE_PREFIX = "Image_"
    private const val JPG_SUFFIX = ".jpg"
    private const val FOLDER_NAME = "Photo_session"
  }

  override fun createDirectoryIfNotExist() {
    val folder = File(Environment.getExternalStorageDirectory().toString() +
        File.separator + Environment.DIRECTORY_PICTURES + File.separator + FOLDER_NAME
    )
    if (!folder.exists()) {
      folder.mkdirs()
    }
  }

  override fun createFile() = File(
      Environment.getExternalStorageDirectory().toString() +
          File.separator + Environment.DIRECTORY_PICTURES + File.separator +
          FOLDER_NAME + File.separator + IMAGE_PREFIX + System.currentTimeMillis() + JPG_SUFFIX
  )
}