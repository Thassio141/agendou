package br.com.agendou.data.repository

import br.com.agendou.domain.models.Category
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CategoryRepositoryImpl(
    firestore: FirebaseFirestore
) : CategoryRepository {

    private val categoriesCollection = firestore.collection("categories")

    override suspend fun createCategory(category: Category): Result<Category> = try {
        val categoryWithTimestamps = category.copy(
            createdAt = Timestamp.now(),
        )
        val docRef = categoriesCollection.document(category.id)
        docRef.set(categoryWithTimestamps).await()
        Result.success(categoryWithTimestamps)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCategoryById(id: String): Result<Category> = try {
        val doc = categoriesCollection.document(id).get().await()
        val category = doc.toObject(Category::class.java)
        if (category != null) {
            Result.success(category)
        } else {
            Result.failure(Exception("Category not found"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateCategory(category: Category): Result<Category> = try {
        val categoryWithTimestamp = category.copy()
        categoriesCollection.document(category.id).set(categoryWithTimestamp).await()
        Result.success(categoryWithTimestamp)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteCategory(id: String): Result<Unit> = try {
        categoriesCollection.document(id).delete().await()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun getAllCategories(): Flow<List<Category>> = callbackFlow {
        val subscription = categoriesCollection
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val categories = snapshot?.documents?.mapNotNull { it.toObject(Category::class.java) } ?: emptyList()
                trySend(categories)
            }
        awaitClose { subscription.remove() }
    }
} 