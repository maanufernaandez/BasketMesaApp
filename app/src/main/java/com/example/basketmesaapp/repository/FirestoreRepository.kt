package com.example.basketmesaapp.repository

import android.util.Log
import com.example.basketmesaapp.model.Partido
import com.example.basketmesaapp.model.Sancion
import com.example.basketmesaapp.model.TarifaReglaRemota
import com.example.basketmesaapp.model.TipoCalculoTarifa
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

    suspend fun sembrarReglasTarifaSiVacio() {
        val snapshot = tarifasReglasCollection.limit(1).get().await()
        if (!snapshot.isEmpty) return

        val batch = db.batch()
        reglasSemilla().forEach { regla ->
            val docId = UUID.randomUUID().toString()
            batch.set(tarifasReglasCollection.document(docId), regla)
        }
        batch.commit().await()
    }

    private fun reglasSemilla(): List<TarifaReglaRemota> = listOf(
        // ---------- ÁRBITRO ----------
        TarifaReglaRemota("", "Árbitro", 0, "1ª División", listOf("1ªdivision"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 91.0),
        TarifaReglaRemota("", "Árbitro", 1, "2ª División Femenina", listOf("2ªdivision", "femenin"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 56.0),
        TarifaReglaRemota("", "Árbitro", 2, "2ª División Masculina", listOf("2ªdivision", "masculin"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 42.0),
        TarifaReglaRemota("", "Árbitro", 3, "Senior 1ª", listOf("senior", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 29.30, numeroOficialesReferencia = 1, valorConReferencia = 58.60),
        TarifaReglaRemota("", "Árbitro", 4, "Senior 2ª", listOf("senior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 23.25, numeroOficialesReferencia = 1, valorConReferencia = 46.50),
        TarifaReglaRemota("", "Árbitro", 5, "Junior 1ª", listOf("junior", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 22.35, numeroOficialesReferencia = 1, valorConReferencia = 44.70),
        TarifaReglaRemota("", "Árbitro", 6, "Junior 2ª", listOf("junior", "2ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 18.0, numeroOficialesReferencia = 1, valorConReferencia = 36.0),
        TarifaReglaRemota("", "Árbitro", 7, "Cadete 1ª", listOf("cadete", "1ª"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 16.45, numeroOficialesReferencia = 1, valorConReferencia = 24.65),
        TarifaReglaRemota("", "Árbitro", 8, "Veteranos", listOf("veteran"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 16.45, numeroOficialesReferencia = 1, valorConReferencia = 32.90),
        TarifaReglaRemota("", "Árbitro", 9, "Copa Navarra", listOf("copanavarra"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 43.85),
        TarifaReglaRemota("", "Árbitro", 10, "Selección", listOf("seleccion"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 10.0),

        // ---------- OFICIAL DE MESA ----------
        TarifaReglaRemota("", "Oficial de Mesa", 0, "Selección Navarra - Junior", listOf("seleccionnavarra", "junior"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 25.0),
        TarifaReglaRemota("", "Oficial de Mesa", 1, "Selección Navarra - Cadete", listOf("seleccionnavarra", "cadete"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 17.60),
        TarifaReglaRemota("", "Oficial de Mesa", 2, "Selección Navarra - Infantil", listOf("seleccionnavarra", "infantil"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 17.60),
        TarifaReglaRemota("", "Oficial de Mesa", 3, "Selección Navarra - Mini", listOf("seleccionnavarra", "mini"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 13.40),
        TarifaReglaRemota("", "Oficial de Mesa", 4, "Selección Navarra - Otros", listOf("seleccionnavarra"), TipoCalculoTarifa.FIJO.name, valorPorDefecto = 0.0),
        TarifaReglaRemota("", "Oficial de Mesa", 5, "LF Challenge", listOf("lfchallenge"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 64.0, numeroOficialesReferencia = 4, valorConReferencia = 48.0),
        TarifaReglaRemota("", "Oficial de Mesa", 6, "Liga EBA", listOf("ligaeba"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 38.83, numeroOficialesReferencia = 4, valorConReferencia = 29.12),
        TarifaReglaRemota("", "Oficial de Mesa", 7, "Copa Navarra", listOf("copanavarra"), TipoCalculoTarifa.SEGUN_NUMERO_OFICIALES.name, valorPorDefecto = 25.45, numeroOficialesReferencia = 3, valorConReferencia = 16.65),
        TarifaReglaRemota("", "Oficial de Mesa", 8, "2ª División Femenina", listOf("2ªdivisionfemenin"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION.name, valorPorDefecto = 31.60, valorConReferencia = 31.60, valorSolitarioSinAutorizacion = 47.40),
        TarifaReglaRemota("", "Oficial de Mesa", 9, "2ª División Masculina", listOf("2ªdivisionmasculin"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION.name, valorPorDefecto = 25.0, valorConReferencia = 25.0, valorSolitarioSinAutorizacion = 37.50),
        TarifaReglaRemota("", "Oficial de Mesa", 10, "Senior 1ª", listOf("senior", "1ª"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION.name, valorPorDefecto = 19.70, valorConReferencia = 19.70, valorSolitarioSinAutorizacion = 29.55),
        TarifaReglaRemota("", "Oficial de Mesa", 11, "Junior 1ª", listOf("junior", "1ª"), TipoCalculoTarifa.SOLITARIO_CON_AUTORIZACION.name, valorPorDefecto = 17.0, valorConReferencia = 17.0, valorSolitarioSinAutorizacion = 25.50)
    )
}