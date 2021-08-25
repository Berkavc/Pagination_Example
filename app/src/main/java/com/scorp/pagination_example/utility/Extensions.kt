package com.scorp.pagination_example.utility

import androidx.annotation.MainThread
import androidx.lifecycle.*
import java.util.concurrent.atomic.AtomicBoolean

/*
 * Base Extension class for kotlin feel there are some sample extensions here feel free to use or add new extensions in this class.
 */

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, body: (T?) -> Unit) =
    liveData.observe(this, Observer(body))

class CustomMutableSingleLiveData<T> : MutableLiveData<T>() {
    private val pending = AtomicBoolean()

    /**
     * Adds the given observer to the observers list within the lifespan of the given
     * owner. The events are dispatched on the main thread. If LiveData already has data
     * set, it will be delivered to the observer.
     *
     * @param owner The LifecycleOwner which controls the observer
     * @param observer The observer that will receive the events
     * @see MutableLiveData.observe
     */
    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { t ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(t)
            }
        })
    }

    /**
     * Sets the value. If there are active observers, the value will be dispatched to them.
     *
     * @param value The new value
     * @see MutableLiveData.setValue
     */
    @MainThread
    override fun setValue(value: T?) {
        pending.set(true)
        super.setValue(value)
    }

}

fun <T : Any?> MutableLiveData<T>.default(initialValue: T?) = apply { setValue(initialValue) }
