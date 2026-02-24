package com.yilber.ciberai

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yilber.ciberai.data.AnalisisRepository
import com.yilber.ciberai.data.CiberDatabase
import com.yilber.ciberai.databinding.ActivityHistorialBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HistorialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialBinding
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dao = CiberDatabase.getInstance(this).analisisDao()
        val repository = AnalisisRepository(dao)

        adapter = HistorialAdapter()
        binding.recyclerHistorial.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistorial.adapter = adapter

        binding.buttonCerrar.setOnClickListener { finish() }

        lifecycleScope.launch {
            repository.obtenerHistorial().collectLatest { lista ->
                adapter.submitList(lista)
            }
        }
    }
}
