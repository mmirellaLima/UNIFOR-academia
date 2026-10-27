package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private val mensagens = mutableListOf<Mensagem>()
    private lateinit var adapter: MensagemAdapter
    private lateinit var rvMensagens: RecyclerView
    private lateinit var etMensagem: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        com.example.myapplication.navigation.AppNavigation.styleSystemBars(this)
        setContentView(R.layout.activity_chat)

        val cabecalho = findViewById<View>(R.id.cabecalho)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.chat)) { v, insets ->
            val barras = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val teclado = insets.getInsets(WindowInsetsCompat.Type.ime())
            v.setPadding(barras.left, 0, barras.right, maxOf(barras.bottom, teclado.bottom))
            cabecalho.setPadding(
                cabecalho.paddingLeft, barras.top, cabecalho.paddingRight, cabecalho.paddingBottom
            )
            insets
        }

        rvMensagens = findViewById(R.id.rvMensagens)
        etMensagem = findViewById(R.id.etMensagem)

        savedInstanceState?.getStringArrayList("messages")?.forEach { mensagens.add(Mensagem(it, enviada = true)) }
        adapter = MensagemAdapter(mensagens)
        rvMensagens.layoutManager = LinearLayoutManager(this).apply { stackFromEnd = true }
        rvMensagens.adapter = adapter

        findViewById<View>(R.id.btnVoltar).setOnClickListener { finish() }
        findViewById<View>(R.id.btnEnviar).setOnClickListener { enviarMensagem() }
        com.example.myapplication.navigation.LegacyNavigation.bind(this, 2)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putStringArrayList("messages", ArrayList(mensagens.map { it.texto }))
        super.onSaveInstanceState(outState)
    }

    private fun enviarMensagem() {
        val texto = etMensagem.text.toString().trim()
        if (texto.isEmpty()) return

        mensagens.add(Mensagem(texto, enviada = true))
        adapter.notifyItemInserted(mensagens.size - 1)
        rvMensagens.scrollToPosition(mensagens.size - 1)
        etMensagem.text.clear()
    }
}
