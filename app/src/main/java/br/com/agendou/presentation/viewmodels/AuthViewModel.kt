package br.com.agendou.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.User
import br.com.agendou.domain.usecases.auth.*
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase,
    private val loginAnonymouslyUseCase: LoginAnonymouslyUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val _currentUser = MutableStateFlow<FirebaseUser?>(getCurrentUserUseCase())
    val currentUser: StateFlow<FirebaseUser?> = _currentUser

    init {
        // Observar alterações no usuário autenticado
        viewModelScope.launch {
            getCurrentUserUseCase.getUserFlow().collect { user ->
                _currentUser.value = user
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = loginUseCase(email, password)
                result.fold(
                    onSuccess = { 
                        _authState.value = AuthState.Success
                    },
                    onFailure = { 
                        _authState.value = AuthState.Error(it.message ?: "Erro ao fazer login")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao fazer login")
            }
        }
    }

    fun register(email: String, password: String, user: User) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = registerUseCase(email, password, user)
                result.fold(
                    onSuccess = { 
                        _authState.value = AuthState.Success
                    },
                    onFailure = { 
                        _authState.value = AuthState.Error(it.message ?: "Erro ao registrar")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao registrar")
            }
        }
    }

    fun loginWithGoogle(credential: AuthCredential) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = loginWithGoogleUseCase(credential)
                result.fold(
                    onSuccess = { 
                        _authState.value = AuthState.Success
                    },
                    onFailure = { 
                        _authState.value = AuthState.Error(it.message ?: "Erro ao fazer login com Google")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao fazer login com Google")
            }
        }
    }

    fun loginAnonymously() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = loginAnonymouslyUseCase()
                result.fold(
                    onSuccess = { 
                        _authState.value = AuthState.Success
                    },
                    onFailure = { 
                        _authState.value = AuthState.Error(it.message ?: "Erro ao fazer login anônimo")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao fazer login anônimo")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = logoutUseCase()
                result.fold(
                    onSuccess = { 
                        _authState.value = AuthState.Idle
                    },
                    onFailure = { 
                        _authState.value = AuthState.Error(it.message ?: "Erro ao fazer logout")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao fazer logout")
            }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = resetPasswordUseCase(email)
                result.fold(
                    onSuccess = { 
                        _authState.value = AuthState.PasswordResetSent
                    },
                    onFailure = { 
                        _authState.value = AuthState.Error(it.message ?: "Erro ao resetar senha")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao resetar senha")
            }
        }
    }

    fun updateUserProfile(name: String, photoUri: String? = null) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = updateUserProfileUseCase(name, photoUri)
                result.fold(
                    onSuccess = { 
                        _authState.value = AuthState.ProfileUpdated
                    },
                    onFailure = { 
                        _authState.value = AuthState.Error(it.message ?: "Erro ao atualizar perfil")
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao atualizar perfil")
            }
        }
    }

    fun checkAuthState() {
        val isAuthenticated = isUserAuthenticatedUseCase()
        if (isAuthenticated) {
            _authState.value = AuthState.Success
        } else {
            _authState.value = AuthState.Idle
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        object PasswordResetSent : AuthState()
        object ProfileUpdated : AuthState()
        data class Error(val message: String) : AuthState()
    }
} 