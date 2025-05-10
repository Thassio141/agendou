package br.com.agendou.data.repository

import br.com.agendou.domain.models.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun createCategory(category: Category): Result<Category>
    suspend fun getCategoryById(id: String): Result<Category>
    suspend fun updateCategory(category: Category): Result<Category>
    suspend fun deleteCategory(id: String): Result<Unit>
    fun getAllCategories(): Flow<List<Category>>
} 