package com.yilber.ciberai.network

import com.google.gson.GsonBuilder
import com.yilber.ciberai.ResultadoAnalisis
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CiberIaClient(
    baseUrl: String
) {

    private val service: CiberIaService

    init {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder().create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        service = retrofit.create(CiberIaService::class.java)
    }

    suspend fun intentarAnalisisRemoto(
        comando: String,
        salida: String
    ): ResultadoAnalisis? {
        return try {
            val resp = service.analizar(IaRequest(comando, salida))
            resp.aResultadoAnalisis()
        } catch (e: Exception) {
            null
        }
    }
}
