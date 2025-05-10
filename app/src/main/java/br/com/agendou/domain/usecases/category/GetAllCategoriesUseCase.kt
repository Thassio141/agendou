package br.com.agendou.domain.usecases.category

import br.com.agendou.data.repository.CategoryRepository
import br.com.agendou.domain.models.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return categoryRepository.getAllCategories()
    }
} 