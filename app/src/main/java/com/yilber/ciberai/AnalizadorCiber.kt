package com.yilber.ciberai

data class ResultadoAnalisis(
    val analisis: String,
    val siguientesPasos: List<String>
)

object AnalizadorCiber {

    fun ofrecerSugerencias(comando: String, salida: String): ResultadoAnalisis {
        val comandoLower = comando.lowercase()
        val salidaLower = salida.lowercase()

        return when {
            "nmap" in comandoLower -> analizarNmap(comandoLower, salidaLower)
            "sqlmap" in comandoLower -> analizarSqlmap(comandoLower, salidaLower)
            "hydra" in comandoLower -> analizarHydra(comandoLower, salidaLower)
            "error" in salidaLower || "failed" in salidaLower || "timeout" in salidaLower ->
                analizarErroresGenericos(comandoLower, salidaLower)
            else -> analisisGenerico(comando, salida)
        }
    }

    private fun analizarNmap(comando: String, salida: String): ResultadoAnalisis {
        val abiertos = Regex("""(\d+)/tcp\s+open""").findAll(salida).map { it.groupValues[1] }.toList()
        val analisis = buildString {
            appendLine("Se ha detectado un escaneo de puertos con nmap.")
            if (abiertos.isNotEmpty()) {
                appendLine("Puertos abiertos detectados: ${abiertos.joinToString(\", \")}.")
            } else {
                appendLine("No se detectan puertos abiertos en la salida proporcionada.")
            }
        }.trim()

        val pasos = mutableListOf<String>()

        if (abiertos.isNotEmpty()) {
            pasos += "Prioriza el análisis de los servicios expuestos en los puertos: ${abiertos.joinToString(\", \")}."
            pasos += "Lanza un escaneo de versiones más profundo, por ejemplo:\n" +
                    "nmap -sV -sC -p${abiertos.joinToString(\",\")} <objetivo>"
            pasos += "Cruza los servicios detectados con vulnerabilidades conocidas (CVE, exploit-db, searchsploit)."
        } else {
            pasos += "Repite el escaneo con opciones más agresivas en un entorno controlado, por ejemplo:\n" +
                    "nmap -A -T4 <objetivo>"
        }

        pasos += "Valida que todos los ataques se realizan únicamente en laboratorios o sistemas autorizados."

        return ResultadoAnalisis(analisis, pasos)
    }

    private fun analizarSqlmap(comando: String, salida: String): ResultadoAnalisis {
        val analisis = buildString {
            appendLine("Se ha detectado el uso de sqlmap para explotación de inyecciones SQL.")
            if ("is vulnerable" in salida || "parameter" in salida && "appears to be injectable" in salida) {
                appendLine("La salida indica posible vulnerabilidad SQLi en el objetivo.")
            }
        }.trim()

        val pasos = listOf(
            "Si se confirma la inyección SQL, documenta el vector exacto (parámetro, payload, tipo de DB).",
            "Extrae información mínima necesaria para la prueba de concepto (por ejemplo, versión de la base de datos).",
            "Evalúa el impacto real: acceso a datos sensibles, escalada, corrupción de datos.",
            "Propón contramedidas: validación de entradas, consultas parametrizadas, WAF, reducción de superficie de ataque."
        )

        return ResultadoAnalisis(analisis, pasos)
    }

    private fun analizarHydra(comando: String, salida: String): ResultadoAnalisis {
        val exito = "login:" in salida.lowercase() || "password:" in salida.lowercase()
        val analisis = buildString {
            appendLine("Se ha detectado el uso de hydra para fuerza bruta de credenciales.")
            if (exito) {
                appendLine("Parece haberse encontrado al menos un par usuario/contraseña válido.")
            } else {
                appendLine("No se observan credenciales exitosas en la salida proporcionada.")
            }
        }.trim()

        val pasos = mutableListOf<String>()

        if (exito) {
            pasos += "Registra las credenciales encontradas solo en el informe del laboratorio, nunca las reutilices fuera del alcance autorizado."
            pasos += "Prueba si las credenciales permiten escalada de privilegios o acceso lateral a otros sistemas del laboratorio."
        } else {
            pasos += "Revisa el diccionario usado y el número de intentos, podría ser insuficiente o poco realista."
        }

        pasos += "Analiza contramedidas defensivas: bloqueo de cuentas, MFA, listas de contraseñas prohibidas, políticas de rotación."

        return ResultadoAnalisis(analisis, pasos)
    }

    private fun analizarErroresGenericos(comando: String, salida: String): ResultadoAnalisis {
        val analisis = buildString {
            appendLine("Se ha detectado una ejecución con errores o fallos de conexión.")
            appendLine("Comando: $comando")
            appendLine("Fragmento de salida:\n${salida.take(400)}")
        }.trim()

        val pasos = listOf(
            "Verifica conectividad básica con ping o traceroute hacia el objetivo.",
            "Revisa puertos y firewall local para descartar bloqueos.",
            "Ajusta timeouts y reintenta el comando con más verbosidad (por ejemplo, -v, -vv según la herramienta).",
            "Consulta la documentación específica de la herramienta para el mensaje de error exacto."
        )

        return ResultadoAnalisis(analisis, pasos)
    }

    private fun analisisGenerico(comando: String, salida: String): ResultadoAnalisis {
        val analisis = buildString {
            appendLine("No se reconoce una herramienta específica, se realiza un análisis genérico.")
            appendLine("Comando: $comando")
            if (salida.isNotBlank()) {
                appendLine("Resumen de salida (primeros 400 caracteres):")
                appendLine(salida.take(400))
            }
        }.trim()

        val pasos = listOf(
            "Identifica si el comando pertenece a reconocimiento, explotación o post-explotación.",
            "Busca patrones de éxito (tokens, credenciales, versiones de servicios) o mensajes de error clave.",
            "Define el siguiente paso ofensivo controlado o la contramedida defensiva adecuada.",
            "Si es una herramienta nueva, crea una nota en tu laboratorio documentando parámetros y comportamiento."
        )

        return ResultadoAnalisis(analisis, pasos)
    }
}
