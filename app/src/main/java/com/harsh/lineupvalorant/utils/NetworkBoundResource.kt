package com.harsh.lineupvalorant.utils

import kotlinx.coroutines.flow.*

/*
 Inline makes the higher order function more efficient (Higher order functions are those functions who takes other functions as input)
 1) Function call overhead doesn't occur.
 2) It also saves the overhead of push/pop variables on the stack when function is called.
 3) It also saves overhead of a return call from a function.
 */
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading(data))

        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            query().map { Resource.Error(throwable, it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}