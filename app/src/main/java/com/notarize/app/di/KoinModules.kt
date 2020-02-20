package com.notarize.app.di

import com.notarize.app.views.take_photo.TakePhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        TakePhotoViewModel(get())
    }
}