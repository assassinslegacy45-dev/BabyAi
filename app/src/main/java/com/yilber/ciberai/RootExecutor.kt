package com.yilber.ciberai

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

data class RootResult(
    val stdout: String,
    val stderr: String,
    val exitCode: Int
)

object RootExecutor {

    /**
     * Ejecuta un comando como root usando su. Diseñado para ser llamado
     * desde un dispatcher de IO para no bloquear la UI.
     */
    fun runAsRoot(command: String): RootResult {
        return try {
            val process = Runtime.getRuntime().exec("su")

            val outputWriter = OutputStreamWriter(process.outputStream)
            outputWriter.write("$command\n")
            outputWriter.write("exit\n")
            outputWriter.flush()
            outputWriter.close()

            val stdoutBuilder = StringBuilder()
            val stderrBuilder = StringBuilder()

            BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                var line: String?
                var count = 0
                while (reader.readLine().also { line = it } != null && count < 5000) {
                    stdoutBuilder.appendLine(line)
                    count++
                }
            }

            BufferedReader(InputStreamReader(process.errorStream)).use { reader ->
                var line: String?
                var count = 0
                while (reader.readLine().also { line = it } != null && count < 5000) {
                    stderrBuilder.appendLine(line)
                    count++
                }
            }

            val exitCode = process.waitFor()

            RootResult(
                stdout = stdoutBuilder.toString().trim(),
                stderr = stderrBuilder.toString().trim(),
                exitCode = exitCode
            )
        } catch (e: Exception) {
            RootResult(
                stdout = "",
                stderr = "Error al ejecutar como root: ${e.message}",
                exitCode = -1
            )
        }
    }
}

