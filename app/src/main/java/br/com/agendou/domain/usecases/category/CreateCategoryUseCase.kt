package br.com.agendou.domain.usecases.category

import br.com.agendou.data.repository.CategoryRepository
import br.com.agendou.domain.models.Category
import javax.inject.Inject

class CreateCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(category: Category): Result<Category> {
        return categoryRepository.createCategory(category)
    }
} 