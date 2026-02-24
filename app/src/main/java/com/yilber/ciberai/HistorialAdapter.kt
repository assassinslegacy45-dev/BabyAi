package com.yilber.ciberai

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yilber.ciberai.data.AnalisisEntity
import com.yilber.ciberai.databinding.ItemAnalisisBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistorialAdapter :
    ListAdapter<AnalisisEntity, HistorialAdapter.AnalisisViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AnalisisEntity>() {
            override fun areItemsTheSame(oldItem: AnalisisEntity, newItem: AnalisisEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AnalisisEntity, newItem: AnalisisEntity): Boolean =
                oldItem == newItem
        }

        private val formatter =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }

    inner class AnalisisViewHolder(
        private val binding: ItemAnalisisBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AnalisisEntity) {
            binding.textFecha.text = formatter.format(Date(item.creadoEn))
            binding.textComando.text = item.comando
            binding.textAnalisisItem.text = item.analisis
            binding.textSiguientesPasosItem.text = item.siguientesPasos
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalisisViewHolder {
        val binding = ItemAnalisisBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnalisisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnalisisViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
