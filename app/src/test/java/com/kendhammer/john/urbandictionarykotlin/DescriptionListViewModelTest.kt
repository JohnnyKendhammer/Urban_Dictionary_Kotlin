package com.kendhammer.john.urbandictionarykotlin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.kendhammer.john.urbandictionarykotlin.model.Definition
import com.kendhammer.john.urbandictionarykotlin.model.UrbanResponse
import com.kendhammer.john.urbandictionarykotlin.model.UrbanService
import com.kendhammer.john.urbandictionarykotlin.viewmodel.DescriptionListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class DescriptionListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var urbanService: UrbanService

    @InjectMocks
    var descriptionListViewModel = DescriptionListViewModel()

    private var testSingle: Single<UrbanResponse>? = null

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun getDefinitionSuccess() {

        val definition = Definition(
            "Dog",
            "Man's best friend",
            "My dog is the best",
            "Johnny",
            "3/14/20",
            100,
            0,
            listOf()
        )

        val definitionList = arrayListOf(definition)
        val urbanResponse = UrbanResponse(definitionList)

        testSingle = Single.just(urbanResponse)

        Mockito.`when`(urbanService.getDefinitions("Test")).thenReturn(testSingle)

        descriptionListViewModel.refresh("Test")

        Assert.assertEquals(
            UrbanResponse(arrayListOf(definition)),
            descriptionListViewModel.urbanResponse.value
        )
        Assert.assertEquals(
            "Johnny",
            descriptionListViewModel.urbanResponse.value?.list?.get(0)?.author
        )
        Assert.assertEquals(false, descriptionListViewModel.wordLoadError.value)
        Assert.assertEquals(false, descriptionListViewModel.loading.value)
    }

    @Test
    fun getCountriesFailed() {
        testSingle = Single.error(Throwable())

        Mockito.`when`(urbanService.getDefinitions("Test")).thenReturn(testSingle)

        descriptionListViewModel.refresh("Test")
        Assert.assertEquals(true, descriptionListViewModel.wordLoadError.value)
    }


    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {

            override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
                return super.scheduleDirect(run, 0, null)
            }

            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }
        RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler -> immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { scheduler -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler -> immediate }

    }

}