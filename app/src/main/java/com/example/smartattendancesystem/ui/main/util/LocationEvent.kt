package com.example.smartattendancesystem.ui.main.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationEvent @Inject constructor(
    private val externalCoroutine : CoroutineScope
) {

    /*private val channel = Channel<LocationState>(CONFLATED)


    suspend fun produceEvent(state: LocationState){
        externalCoroutine.launch {
            channel.send(state)
        }
    }

    fun consumeEvent(): Flow<LocationState> = flow{
        emit(channel.receive())
    }.shareIn(
        externalCoroutine,
        replay = 0,
        started = SharingStarted.WhileSubscribed()
    )*/
}