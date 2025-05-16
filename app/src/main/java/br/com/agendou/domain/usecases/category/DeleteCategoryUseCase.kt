package br.com.agendou.domain.usecases.category

import br.com.agendou.data.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase @Inject constructor(
    private val categoryRepository: CategoryRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return categoryRepository.deleteCategory(id)
    }
} 