package com.diagnal.diagnalprject

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserver: () -> Unit = {}
): T {
    var data: T? = null
    var latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }
    // Observe the LiveData with the created Observer
    this.observeForever(observer)

    try {
        afterObserver.invoke() // Invoke any action needed after observing
        // Wait for the value to be set
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }
    } finally {
        // Remove the Observer to avoid memory leaks
        removeObserver(observer)
    }

    // Return the value
    @Suppress("UNCHECKED_CAST")
    return data as T
}
