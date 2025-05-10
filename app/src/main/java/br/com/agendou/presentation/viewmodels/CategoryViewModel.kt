package br.com.agendou.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Category
import br.com.agendou.domain.usecases.category.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoryViewModel @Inject constructor(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
) : ViewModel() {

    private val _categoryState = MutableStateFlow<CategoryState>(CategoryState.Idle)
    val categoryState: StateFlow<CategoryState> = _categoryState

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category

    fun createCategory(category: Category) {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                val result = createCategoryUseCase(category)
                result.fold(
                    onSuccess = { 
                        _categoryState.value = CategoryState.Success
                        _category.value = it
                    },
                    onFailure = { 
                        _categoryState.value = CategoryState.Error(it.message ?: "Erro ao criar categoria")
                    }
                )
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Erro ao criar categoria")
            }
        }
    }

    fun getCategoryById(id: String) {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                val result = getCategoryByIdUseCase(id)
                result.fold(
                    onSuccess = { 
                        _categoryState.value = CategoryState.Success
                        _category.value = it
                    },
                    onFailure = { 
                        _categoryState.value = CategoryState.Error(it.message ?: "Erro ao buscar categoria")
                    }
                )
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Erro ao buscar categoria")
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                val result = updateCategoryUseCase(category)
                result.fold(
                    onSuccess = { 
                        _categoryState.value = CategoryState.Success
                        _category.value = it
                    },
                    onFailure = { 
                        _categoryState.value = CategoryState.Error(it.message ?: "Erro ao atualizar categoria")
                    }
                )
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Erro ao atualizar categoria")
            }
        }
    }

    fun deleteCategory(id: String) {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                val result = deleteCategoryUseCase(id)
                result.fold(
                    onSuccess = { 
                        _categoryState.value = CategoryState.Success
                        _category.value = null
                    },
                    onFailure = { 
                        _categoryState.value = CategoryState.Error(it.message ?: "Erro ao excluir categoria")
                    }
                )
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Erro ao excluir categoria")
            }
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            _categoryState.value = CategoryState.Loading
            try {
                getAllCategoriesUseCase().collect { categoriesList ->
                    _categories.value = categoriesList
                    _categoryState.value = CategoryState.Success
                }
            } catch (e: Exception) {
                _categoryState.value = CategoryState.Error(e.message ?: "Erro ao buscar categorias")
            }
        }
    }

    sealed class CategoryState {
        object Idle : CategoryState()
        object Loading : CategoryState()
        object Success : CategoryState()
        data class Error(val message: String) : CategoryState()
    }
} 