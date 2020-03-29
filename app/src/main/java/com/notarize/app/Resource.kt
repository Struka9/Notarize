package com.notarize.app

/**
* Created by Oscar Presidente on 6/15/18
* oscar.rene1989@gmail.com
*/
data class Resource<out T>(val status: Status, val data: T? = null, val message: String? = null)

enum class Status {
    SUCCESS, LOADING, IDLE, ERROR,
}
