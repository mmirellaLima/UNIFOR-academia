package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt

class MenuAcessibilidadeDialog : BottomSheetDialogFragment() {

    private lateinit var tvPorcentagem: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.dialog_acessibilidade, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvPorcentagem = view.findViewById(R.id.tvPorcentagem)
        atualizarTexto()

        view.findViewById<ImageView>(R.id.btnDiminuir).setOnClickListener {
            mudarEscala(AcessibilidadePrefs.getEscala(requireContext()) - AcessibilidadePrefs.PASSO)
        }
        view.findViewById<ImageView>(R.id.btnAumentar).setOnClickListener {
            mudarEscala(AcessibilidadePrefs.getEscala(requireContext()) + AcessibilidadePrefs.PASSO)
        }
        view.findViewById<ImageView>(R.id.btnResetar).setOnClickListener {
            mudarEscala(1.0f)
        }
        view.findViewById<TextView>(R.id.btnFechar).setOnClickListener { dismiss() }
    }

    private fun mudarEscala(nova: Float) {
        val limitada = nova.coerceIn(AcessibilidadePrefs.MINIMO, AcessibilidadePrefs.MAXIMO)
        // arredonda para evitar 1.0000001
        val arredondada = (limitada * 10).roundToInt() / 10f
        AcessibilidadePrefs.setEscala(requireContext(), arredondada)
        atualizarTexto()
        // recria a tela para aplicar o novo tamanho
        requireActivity().recreate()
    }

    private fun atualizarTexto() {
        val pct = (AcessibilidadePrefs.getEscala(requireContext()) * 100).roundToInt()
        tvPorcentagem.text = "$pct%"
    }
}