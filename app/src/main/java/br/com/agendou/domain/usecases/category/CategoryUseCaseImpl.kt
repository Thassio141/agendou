package br.com.agendou.domain.usecases.category

import br.com.agendou.common.ErrorMessages
import br.com.agendou.data.repository.CategoryRepository
import br.com.agendou.domain.models.Category
import br.com.agendou.domain.usecases.auth.AuthUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val authUseCase: AuthUseCase
) : CategoryUseCase {

    override suspend fun createCategory(category: Category): Result<Category> {
        return try {
            if (!authUseCase.isUserAuthenticated) {
                return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            }
            categoryRepository.createCategory(category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategoryById(id: String): Result<Category> = 
        categoryRepository.getCategoryById(id)

    override suspend fun updateCategory(category: Category): Result<Category> {
        return try {
            if (!authUseCase.isUserAuthenticated) {
                return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            }
            categoryRepository.updateCategory(category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(id: String): Result<Unit> {
        return try {
            if (!authUseCase.isUserAuthenticated) {
                return Result.failure(Exception(ErrorMessages.USER_NOT_AUTHENTICATED))
            }
            categoryRepository.deleteCategory(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getAllCategories(): Flow<List<Category>> = 
        categoryRepository.getAllCategories()
} 