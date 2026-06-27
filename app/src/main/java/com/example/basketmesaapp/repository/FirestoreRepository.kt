package com.example.basketmesaapp.repository

import android.util.Log
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val partidosCollection = db.collection("partidos")
    private val sancionesCollection = db.collection("sanciones")

    fun getPartidos(): Flow<List<Partido>> {
        val uid = auth.currentUser?.uid ?: ""
        return callbackFlow {
            if (uid.isEmpty()) {
                trySend(emptyList())
                close()
                return@callbackFlow
            }
            val listener = partidosCollection.whereEqualTo("userId", uid).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val partidos = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Partido::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(partidos)
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun guardarPartido(partido: Partido) {
        val docId = if (partido.id.isBlank()) UUID.randomUUID().toString() else partido.id
        partidosCollection.document(docId).set(partido).await()
    }

    suspend fun eliminarPartido(partidoId: String) {
        try {
            partidosCollection.document(partidoId).delete().await()
        } catch (e: Exception) {
            Log.e("Firestore", "Error eliminando partido $partidoId", e)
            throw e
        }
    }

    fun getSanciones(): Flow<List<Sancion>> {
        val uid = auth.currentUser?.uid ?: ""
        return callbackFlow {
            if (uid.isEmpty()) {
                trySend(emptyList())
                close()
                return@callbackFlow
            }
            val listener = sancionesCollection.whereEqualTo("userId", uid).addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val sanciones = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Sancion::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(sanciones)
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun guardarSancion(sancion: Sancion) {
        val docId = if (sancion.id.isBlank()) UUID.randomUUID().toString() else sancion.id
        sancionesCollection.document(docId).set(sancion).await()
    }

    suspend fun eliminarSancion(sancionId: String) {
        try {
            sancionesCollection.document(sancionId).delete().await()
        } catch (e: Exception) {
            Log.e("Firestore", "Error eliminando sanción $sancionId", e)
            throw e
        }
    }
}