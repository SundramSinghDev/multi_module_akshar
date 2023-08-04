package com.pronted.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

object EventFlow {

    var eventFlow = MutableSharedFlow<Pair<String, Any>>()

    fun publishEvent(key: String, any: Any) {
        CoroutineScope(Dispatchers.IO).launch {
            eventFlow.emit(Pair(key, any))
        }
    }

    fun <T> getEvent(key: String): Flow<T> {
        return eventFlow.filter { it.first == key }.map {
            it.second as T
        }
    }
}