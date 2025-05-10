package br.com.agendou.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.User
import br.com.agendou.domain.usecases.auth.AuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Initial)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _passwordRecoveryState = MutableLiveData<Result<Unit>>()
    val passwordRecoveryState: LiveData<Result<Unit>> = _passwordRecoveryState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val user = authUseCase.signIn(email, password)
                _authState.value = AuthState.Authenticated(user)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Erro ao realizar login")
            }
        }
    }

    fun register(user: User, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading
            try {
                val registeredUser = authUseCase.signUp(user, password)
                _registerState.value = RegisterState.Success(registeredUser)
                _authState.value = AuthState.Authenticated(registeredUser)
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Erro ao realizar cadastro")
            }
        }
    }

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            try {
                authUseCase.resetPassword(email)
                _passwordRecoveryState.value = Result.success(Unit)
            } catch (e: Exception) {
                _passwordRecoveryState.value = Result.failure(e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                authUseCase.signOut()
                _authState.value = AuthState.Initial
            } catch (e: Exception) {
                _authState.value = AuthState.Initial
            }
        }
    }

    sealed class AuthState {
        object Initial : AuthState()
        object Loading : AuthState()
        data class Authenticated(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }

    sealed class RegisterState {
        object Initial : RegisterState()
        object Loading : RegisterState()
        data class Success(val user: User) : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
} 