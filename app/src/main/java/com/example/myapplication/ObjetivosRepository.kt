package com.example.myapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

object ObjetivosRepository {

    // TODO: remover o "usuario-teste" quando o login do Firebase estiver ligado
    private fun uid(): String =
        FirebaseAuth.getInstance().currentUser?.uid ?: "usuario-teste"

    private fun doc() =
        FirebaseFirestore.getInstance().collection("users").document(uid())

    fun salvar(objetivos: Collection<String>) {
        doc().set(mapOf("objetivos" to objetivos.toList()), SetOptions.merge())
    }

    // devolve null se não conseguir ler (sem internet, erro etc.)
    fun carregar(onResult: (List<String>?) -> Unit) {
        doc().get()
            .addOnSuccessListener { snapshot ->
                @Suppress("UNCHECKED_CAST")
                onResult(snapshot.get("objetivos") as? List<String>)
            }
            .addOnFailureListener { onResult(null) }
    }
}