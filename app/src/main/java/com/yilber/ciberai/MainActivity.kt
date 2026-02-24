package com.yilber.ciberai

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yilber.ciberai.data.AnalisisRepository
import com.yilber.ciberai.data.CiberDatabase
import com.yilber.ciberai.databinding.ActivityMainBinding
import com.yilber.ciberai.network.CiberIaClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: AnalisisRepository
    private lateinit var iaClient: CiberIaClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = CiberDatabase.getInstance(this)
        repository = AnalisisRepository(db.analisisDao())
        // URL base del backend de IA (puedes cambiarla)
        iaClient = CiberIaClient(baseUrl = "http://10.0.2.2:8000")

        setupUi()
    }

    private fun setupUi() {
        binding.buttonAnalizar.setOnClickListener {
            val comando = binding.inputComando.text.toString()

            lifecycleScope.launch {
                val salidaEfectiva = withContext(Dispatchers.IO) {
                    if (binding.checkRoot.isChecked) {
                        val rootResult = RootExecutor.runAsRoot(comando)
                        val combinado = (rootResult.stdout + "\n" + rootResult.stderr).trim()
                        combinado.ifBlank { "Sin salida capturada desde root. Código: ${rootResult.exitCode}" }
                    } else {
                        binding.inputSalida.text.toString()
                    }
                }

                // Reflejar en la UI la salida usada para el análisis
                binding.inputSalida.setText(salidaEfectiva)

                // Primero intentamos IA remota (ligera para el móvil)
                val resultado = withContext(Dispatchers.IO) {
                    val remoto = iaClient.intentarAnalisisRemoto(comando, salidaEfectiva)
                    remoto ?: AnalizadorCiber.ofrecerSugerencias(comando, salidaEfectiva)
                }

                binding.textAnalisis.text = resultado.analisis
                binding.textSugerencias.text =
                    resultado.siguientesPasos.joinToString("\n\n") { "- $it" }

                withContext(Dispatchers.IO) {
                    repository.guardar(
                        comando = comando,
                        salida = salidaEfectiva,
                        analisis = resultado.analisis,
                        siguientesPasos = resultado.siguientesPasos
                    )
                }
            }
        }

        binding.buttonHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }
    }
}
