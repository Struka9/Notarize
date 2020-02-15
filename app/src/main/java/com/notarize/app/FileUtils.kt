package com.notarize.app

import java.io.File

interface FileUtils {

  fun createDirectoryIfNotExist()

  fun createFile(): File
}