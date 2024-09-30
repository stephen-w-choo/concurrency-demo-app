package com.example.concurrencyeducation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


enum class CounterType {
    SYNCHRONOUS,
    UNSAFE,
    ATOMIC,
    MUTEX
}

class CounterViewModel: ViewModel() {
    private val _synchronousCounter = MutableStateFlow(0)
    private val _asyncUnsafeCounter = MutableStateFlow(0)
    private val _atomicCounter = MutableStateFlow(0)
    private val _mutexCounter = MutableStateFlow(0)

    private val _counterType = MutableStateFlow(CounterType.SYNCHRONOUS)
    val counterType = _counterType.asStateFlow()

    val count = counterType.flatMapLatest { type ->
        when (type) {
            CounterType.SYNCHRONOUS -> _synchronousCounter
            CounterType.UNSAFE -> _asyncUnsafeCounter
            CounterType.ATOMIC -> _atomicCounter
            CounterType.MUTEX -> _mutexCounter
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = _synchronousCounter.value
    )

    val increment: StateFlow<() -> Unit> = counterType
        .map { type ->
            when (type) {
                CounterType.SYNCHRONOUS -> ::incrementSynchronousCounter
                CounterType.UNSAFE -> ::incrementAsyncUnsafeCounter
                CounterType.ATOMIC -> ::incrementAtomicCounter
                CounterType.MUTEX -> ::incrementMutexCounter
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = ::incrementSynchronousCounter
        )

    /**
     * Synchronous counter
     */
    private fun incrementSynchronousCounter() {
        _synchronousCounter.value = 0

        for (i in 0 until 1000) {
            _synchronousCounter.value += 1
        }
    }

    /**
     * Asynchronous, thread-unsafe counter
     */
    private fun incrementAsyncUnsafeCounter() {
        _asyncUnsafeCounter.value = 0

        for (i in 0 until 1000) {
            viewModelScope.launch(Dispatchers.Default) {
                _asyncUnsafeCounter.value += 1
            }
        }
    }

    /**
     * Counter using atomic values
     */
    private fun incrementAtomicCounter() {
        _atomicCounter.value = 0

        for (i in 0 until 1000) {
            viewModelScope.launch {
                _atomicCounter.update { _atomicCounter.value + 1 }
            }
        }
    }

    /**
     * Counter using a mutex
     */
    private val counterMutex = Mutex()

    private fun incrementMutexCounter() {
        _mutexCounter.value = 0

        for (i in 0 until 1000) {
            viewModelScope.launch(Dispatchers.Default) {
                // Only one coroutine at a time can execute this block
                counterMutex.withLock {
                    _mutexCounter.value += 1
                }
            }
        }
    }

    fun setCounterType(counterType: CounterType) {
        _counterType.value = counterType
    }
}