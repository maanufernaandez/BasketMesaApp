package com.example.basketmesaapp.repository

import android.util.Log
import com.example.basketmesaapp.model.DesplazamientoRemoto
import com.example.basketmesaapp.model.DietaRemota
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.utils.DataConstants
import com.example.basketmesaapp.utils.TarifaDefinitions
import com.example.basketmesaapp.utils.toTarifaReglaRemota
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
    private val tarifasReglasCollection = db.collection("tarifas_reglas")
    private val metaCollection = db.collection("app_meta")

    private val desplazamientosCollection = db.collection("desplazamientos_reglas")
    private val dietasCollection = db.collection("dietas_reglas")

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

    fun getReglasTarifa(): Flow<List<TarifaReglaRemota>> {
        return callbackFlow {
            val listener = tarifasReglasCollection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val reglas = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(TarifaReglaRemota::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(reglas)
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun guardarReglaTarifa(regla: TarifaReglaRemota) {
        val docId = if (regla.id.isBlank()) UUID.randomUUID().toString() else regla.id
        tarifasReglasCollection.document(docId).set(regla).await()
    }

    suspend fun eliminarReglaTarifa(reglaId: String) {
        tarifasReglasCollection.document(reglaId).delete().await()
    }

    fun getDesplazamientos(): Flow<List<DesplazamientoRemoto>> {
        return callbackFlow {
            val listener = desplazamientosCollection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val reglas = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(DesplazamientoRemoto::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(reglas)
            }
            awaitClose { listener.remove() }
        }
    }

    fun getDietas(): Flow<List<DietaRemota>> {
        return callbackFlow {
            val listener = dietasCollection.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val reglas = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(DietaRemota::class.java)?.apply { id = doc.id }
                } ?: emptyList()
                trySend(reglas)
            }
            awaitClose { listener.remove() }
        }
    }

    suspend fun sembrarDesplazamientosSiVacio() {
        val snapshot = desplazamientosCollection.limit(1).get().await()
        if (!snapshot.isEmpty) return

        val batch = db.batch()
        DataConstants.preciosDesplazamiento.forEach { (localidad, precios) ->
            val docId = UUID.randomUUID().toString()
            val regla = DesplazamientoRemoto(localidad = localidad, conductor = precios.first, acompanante = precios.second)
            batch.set(desplazamientosCollection.document(docId), regla)
        }
        batch.commit().await()
    }

    suspend fun sembrarDietasSiVacio() {
        val snapshot = dietasCollection.limit(1).get().await()
        if (!snapshot.isEmpty) return

        val batch = db.batch()
        DataConstants.dietasPorCategoria.forEach { (categoria, importe) ->
            val docId = UUID.randomUUID().toString()
            val regla = DietaRemota(categoria = categoria, importe = importe)
            batch.set(dietasCollection.document(docId), regla)
        }
        batch.commit().await()
    }

    suspend fun sembrarReglasTarifaSiVacio() {
        val versionDoc = metaCollection.document("tarifas_reglas_version").get().await()
        val versionGuardada = versionDoc.getLong("version") ?: 0L

        Log.d("SiembraTarifas", "Versión guardada en Firestore: $versionGuardada, versión del código: ${TarifaDefinitions.VERSION}")

        if (versionGuardada >= TarifaDefinitions.VERSION) {
            Log.d("SiembraTarifas", "Ya está al día, no se resiembra")
            return
        }
        Log.d("SiembraTarifas", "Hay una versión nueva: borrando y resembrando")

        // Hay una versión nueva: borra las reglas viejas antes de sembrar las nuevas.
        val actuales = tarifasReglasCollection.get().await()
        if (!actuales.isEmpty) {
            val batchDelete = db.batch()
            actuales.documents.forEach { batchDelete.delete(it.reference) }
            batchDelete.commit().await()
        }

        val batchInsert = db.batch()
        reglasSemilla().forEach { regla ->
            val docId = UUID.randomUUID().toString()
            batchInsert.set(tarifasReglasCollection.document(docId), regla)
        }
        batchInsert.set(metaCollection.document("tarifas_reglas_version"), mapOf("version" to TarifaDefinitions.VERSION))
        batchInsert.commit().await()
    }

    private fun reglasSemilla(): List<TarifaReglaRemota> =
        (TarifaDefinitions.ARBITRO + TarifaDefinitions.OFICIAL_MESA).map { it.toTarifaReglaRemota() }
}