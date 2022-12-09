package com.kanyideveloper.core_database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.annotation.Nullable

object LivedataTestUtil {
    fun <T> getValue(liveData: LiveData<T>): T? {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer: Observer<T> = object : Observer<T> {
            override fun onChanged(@Nullable o: T) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        try {
            latch.await(2, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            throw AssertionError("Interrupted while waiting for LiveData value")
        }
        return data[0] as T?
    }
}
