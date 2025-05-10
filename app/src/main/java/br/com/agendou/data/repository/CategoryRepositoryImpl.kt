package br.com.agendou.data.repository

import br.com.agendou.data.util.toFlow
import br.com.agendou.domain.models.Category
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class CategoryRepositoryImpl(
    firestore: FirebaseFirestore
) : CategoryRepository {

    private val col = firestore.collection("categories")

    override suspend fun createCategory(category: Category): Result<Category> = runCatching {
        val now    = Timestamp.now()
        val docRef = col.document()
        val toSave = category.copy(createdAt = now)
        docRef.set(toSave).await()
        toSave.copy(id = docRef.id)
    }

    override suspend fun getCategoryById(id: String): Result<Category> = runCatching {
        val snap = col.document(id).get().await()
        snap.toObject(Category::class.java)
            ?.apply { this.id = snap.id }
            ?: error("Category not found")
    }

    override suspend fun updateCategory(category: Category): Result<Category> = runCatching {
        require(category.id.isNotBlank())
        val toSave = category.copy(createdAt = category.createdAt)
        col.document(category.id).set(toSave, SetOptions.merge()).await()
        toSave
    }

    override suspend fun deleteCategory(id: String): Result<Unit> = runCatching {
        col.document(id).delete().await()
    }

    override fun getAllCategories(): Flow<List<Category>> =
        col.orderBy("createdAt", Query.Direction.DESCENDING).toFlow()
}