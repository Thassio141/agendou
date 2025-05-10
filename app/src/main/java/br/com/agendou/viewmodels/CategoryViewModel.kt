package br.com.agendou.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Category
import br.com.agendou.domain.usecases.category.CategoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryUseCase: CategoryUseCase
) : ViewModel() {

    private val _categoriesState = MutableStateFlow<CategoriesState>(CategoriesState.Loading)
    val categoriesState: StateFlow<CategoriesState> = _categoriesState

    private val _categoryDetailState = MutableStateFlow<CategoryDetailState>(CategoryDetailState.Loading)
    val categoryDetailState: StateFlow<CategoryDetailState> = _categoryDetailState

    fun getCategories() {
        viewModelScope.launch {
            _categoriesState.value = CategoriesState.Loading
            try {
                val categories = getCategoriesUseCase()
                _categoriesState.value = CategoriesState.Success(categories)
            } catch (e: Exception) {
                _categoriesState.value = CategoriesState.Error(e.message ?: "Erro ao buscar categorias")
            }
        }
    }

    fun getCategoryById(categoryId: String) {
        viewModelScope.launch {
            _categoryDetailState.value = CategoryDetailState.Loading
            try {
                val category = categoryUseCase.getCategoryById(categoryId)
                _categoryDetailState.value = CategoryDetailState.Success(category)
            } catch (e: Exception) {
                _categoryDetailState.value = CategoryDetailState.Error(e.message ?: "Erro ao buscar categoria")
            }
        }
    }

    sealed class CategoriesState {
        object Loading : CategoriesState()
        data class Success(val categories: List<Category>) : CategoriesState()
        data class Error(val message: String) : CategoriesState()
    }

    sealed class CategoryDetailState {
        object Loading : CategoryDetailState()
        data class Success(val category: Category) : CategoryDetailState()
        data class Error(val message: String) : CategoryDetailState()
    }
} 