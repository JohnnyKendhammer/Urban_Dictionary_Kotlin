package com.kendhammer.john.urbandictionarykotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kendhammer.john.urbandictionarykotlin.di.DaggerApiComponent
import com.kendhammer.john.urbandictionarykotlin.model.Definition
import com.kendhammer.john.urbandictionarykotlin.model.UrbanResponse
import com.kendhammer.john.urbandictionarykotlin.model.UrbanService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DescriptionListViewModel : ViewModel() {
    val urbanResponse = MutableLiveData<UrbanResponse>()
    val definitions = MutableLiveData<List<Definition>>()
    val wordLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    @Inject
    lateinit var urbanService: UrbanService

    init {
        DaggerApiComponent.create().inject(this)
    }
    private val disposable = CompositeDisposable()

    fun refresh(word: String) {
        fetchWords(word)
    }

    private fun fetchWords(word: String) {
        loading.value = true
        disposable.add(
            urbanService.getDefinitions(word)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<UrbanResponse>() {
                    override fun onSuccess(value: UrbanResponse?) {
                        urbanResponse.value = value
                        definitions.value = urbanResponse.value?.list
                        wordLoadError.value = false
                        loading.value = false
                    }

                    override fun onError(e: Throwable?) {
                        wordLoadError.value = true
                        loading.value = false
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}