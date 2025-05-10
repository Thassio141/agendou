package br.com.agendou.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.User
import br.com.agendou.domain.usecases.user.*
import com.google.firebase.firestore.DocumentReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val getUsersByCategoryUseCase: GetUsersByCategoryUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<UserState>(UserState.Idle)
    val userState: StateFlow<UserState> = _userState

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun createUser(user: User) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val result = createUserUseCase(user)
                result.fold(
                    onSuccess = { 
                        _userState.value = UserState.Success
                        _user.value = it
                    },
                    onFailure = { 
                        _userState.value = UserState.Error(it.message ?: "Erro ao criar usuário")
                    }
                )
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro ao criar usuário")
            }
        }
    }

    fun getUserById(id: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val result = getUserByIdUseCase(id)
                result.fold(
                    onSuccess = { 
                        _userState.value = UserState.Success
                        _user.value = it
                    },
                    onFailure = { 
                        _userState.value = UserState.Error(it.message ?: "Erro ao buscar usuário")
                    }
                )
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro ao buscar usuário")
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val result = updateUserUseCase(user)
                result.fold(
                    onSuccess = { 
                        _userState.value = UserState.Success
                        _user.value = it
                    },
                    onFailure = { 
                        _userState.value = UserState.Error(it.message ?: "Erro ao atualizar usuário")
                    }
                )
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro ao atualizar usuário")
            }
        }
    }

    fun deleteUser(id: String) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                val result = deleteUserUseCase(id)
                result.fold(
                    onSuccess = { 
                        _userState.value = UserState.Success
                        _user.value = null
                    },
                    onFailure = { 
                        _userState.value = UserState.Error(it.message ?: "Erro ao excluir usuário")
                    }
                )
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro ao excluir usuário")
            }
        }
    }

    fun getAllUsers() {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                getAllUsersUseCase().collect { usersList ->
                    _users.value = usersList
                    _userState.value = UserState.Success
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro ao buscar usuários")
            }
        }
    }

    fun getUsersByCategory(categoryRef: DocumentReference) {
        viewModelScope.launch {
            _userState.value = UserState.Loading
            try {
                getUsersByCategoryUseCase(categoryRef).collect { usersList ->
                    _users.value = usersList
                    _userState.value = UserState.Success
                }
            } catch (e: Exception) {
                _userState.value = UserState.Error(e.message ?: "Erro ao buscar usuários por categoria")
            }
        }
    }

    sealed class UserState {
        object Idle : UserState()
        object Loading : UserState()
        object Success : UserState()
        data class Error(val message: String) : UserState()
    }
} 