package com.yilber.ciberai.data

class AnalisisRepository(private val dao: AnalisisDao) {

    private val limiteHistorial = 200

    suspend fun guardar(
        comando: String,
        salida: String,
        analisis: String,
        siguientesPasos: List<String>
    ) {
        val entidad = AnalisisEntity(
            comando = comando,
            salida = salida.take(2000),
            analisis = analisis,
            siguientesPasos = siguientesPasos.joinToString("\n\n") { "- $it" }
        )
        dao.insertar(entidad)
        dao.borrarAntiguosManteniendo(limiteHistorial)
    }

    fun obtenerHistorial() = dao.obtenerTodos()
}
