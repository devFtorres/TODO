package com.ftorres.notes.core.util

import android.database.sqlite.SQLiteConstraintException
import java.io.IOException

object ErrorHandler {
    fun handleError(exception: Throwable): String {
        return when (exception) {
            is SQLiteConstraintException -> "Erro de dados duplicados"
            is IOException -> "Erro de conexão"
            else -> "Erro inesperado: ${exception.localizedMessage}"
        }
    }
}