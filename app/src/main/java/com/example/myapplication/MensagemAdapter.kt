package com.example.myapplication

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MensagemAdapter(
    private val mensagens: List<Mensagem>
) : RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder>() {

    class MensagemViewHolder(val texto: TextView) : RecyclerView.ViewHolder(texto.parent as ViewGroup)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mensagem, parent, false)
        return MensagemViewHolder(item.findViewById(R.id.tvMensagem))
    }

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        val mensagem = mensagens[position]
        holder.texto.text = mensagem.texto

        val params = holder.texto.layoutParams as FrameLayout.LayoutParams
        params.gravity = if (mensagem.enviada) Gravity.END else Gravity.START
        holder.texto.layoutParams = params

        holder.texto.setBackgroundResource(
            if (mensagem.enviada) R.drawable.bg_msg_enviada else R.drawable.bg_msg_recebida
        )
    }

    override fun getItemCount() = mensagens.size
}