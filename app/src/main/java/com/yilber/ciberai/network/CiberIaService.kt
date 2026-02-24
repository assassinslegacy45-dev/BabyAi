package com.yilber.ciberai.network

import com.yilber.ciberai.ResultadoAnalisis
import retrofit2.http.Body
import retrofit2.http.POST

data class IaRequest(
    val comando: String,
    val salida: String
)

data class IaResponse(
    val analisis: String,
    val siguientesPasos: List<String>
)

interface CiberIaService {
    @POST("/analizar")
    suspend fun analizar(@Body body: IaRequest): IaResponse
}

fun IaResponse.aResultadoAnalisis(): ResultadoAnalisis =
    ResultadoAnalisis(
        analisis = analisis,
        siguientesPasos = siguientesPasos
    )
