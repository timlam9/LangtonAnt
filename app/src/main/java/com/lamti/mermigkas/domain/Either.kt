package com.lamti.mermigkas.domain

sealed class Either<out L, out R> {

    data class Error<out L>(val value: L) : Either<L, Nothing>() {

        override suspend fun <MR> mapSuccess(action: suspend (Nothing) -> MR): Either<L, MR> {
            return Error(value)
        }

        override fun <ML> mapError(action: (L) -> ML): Either<ML, Nothing> {
            return Error(action(value))
        }

        override fun fold(onError: ((L) -> Unit)?, onSuccess: (Nothing) -> Unit) {
            onError?.invoke(value)
        }
    }

    data class Success<out R>(val value: R) : Either<Nothing, R>() {

        override suspend fun <MR> mapSuccess(action: suspend (R) -> MR): Either<Nothing, MR> {
            return Success(action(value))
        }

        override fun <ML> mapError(action: (Nothing) -> ML): Either<ML, R> {
            return Success(value)
        }

        override fun fold(onError: ((Nothing) -> Unit)?, onSuccess: (R) -> Unit) {
            onSuccess(value)
        }
    }

    abstract fun <ML> mapError(action: (L) -> ML): Either<ML, R>

    abstract suspend fun <MR> mapSuccess(action: suspend (R) -> MR): Either<L, MR>

    abstract fun fold(onError: ((L) -> Unit)? = null, onSuccess: (R) -> Unit)
}
