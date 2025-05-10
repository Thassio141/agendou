package br.com.agendou.data.util

import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface Identifiable { var id: String }

inline fun <reified T : Any> Query.toFlow(): Flow<List<T>> = callbackFlow {
    val registration = this@toFlow.addSnapshotListener { snapshot, error ->
        if (error != null) {
            close(error)
            return@addSnapshotListener
        }
        val list = snapshot
            ?.documents
            ?.mapNotNull { doc ->
                doc.toObject(T::class.java)?.apply {
                    if (this is Identifiable) this.id = doc.id
                }
            }
            ?: emptyList()
        trySend(list)
    }
    awaitClose { registration.remove() }
}
