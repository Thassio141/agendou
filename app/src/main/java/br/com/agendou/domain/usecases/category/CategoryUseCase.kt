package br.com.agendou.domain.usecases.category

import br.com.agendou.domain.models.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CategoryUseCase {
    suspend fun createCategory(category: Category): Result<Category>
    suspend fun getCategoryById(id: String): Result<Category>
    suspend fun updateCategory(category: Category): Result<Category>
    suspend fun deleteCategory(id: String): Result<Unit>
    fun getAllCategories(): Flow<List<Category>>
} 