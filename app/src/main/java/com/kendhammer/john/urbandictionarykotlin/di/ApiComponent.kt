package com.kendhammer.john.urbandictionarykotlin.di

import com.kendhammer.john.urbandictionarykotlin.model.UrbanService
import com.kendhammer.john.urbandictionarykotlin.viewmodel.DescriptionListViewModel
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(service: UrbanService)

    fun inject(viewModel: DescriptionListViewModel)
}