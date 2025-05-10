package br.com.agendou.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Service
import br.com.agendou.domain.usecases.service.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServiceViewModel @Inject constructor(
    private val createServiceUseCase: CreateServiceUseCase,
    private val getServiceByIdUseCase: GetServiceByIdUseCase,
    private val updateServiceUseCase: UpdateServiceUseCase,
    private val deleteServiceUseCase: DeleteServiceUseCase,
    private val getAllServicesUseCase: GetAllServicesUseCase,
    private val getServicesByProfessionalUseCase: GetServicesByProfessionalUseCase,
    private val getServicesByCategoryUseCase: GetServicesByCategoryUseCase
) : ViewModel() {

    private val _serviceState = MutableStateFlow<ServiceState>(ServiceState.Idle)
    val serviceState: StateFlow<ServiceState> = _serviceState

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services

    private val _service = MutableStateFlow<Service?>(null)
    val service: StateFlow<Service?> = _service

    fun createService(service: Service) {
        viewModelScope.launch {
            _serviceState.value = ServiceState.Loading
            try {
                val result = createServiceUseCase(service)
                result.fold(
                    onSuccess = { 
                        _serviceState.value = ServiceState.Success
                        _service.value = it
                    },
                    onFailure = { 
                        _serviceState.value = ServiceState.Error(it.message ?: "Erro ao criar serviço")
                    }
                )
            } catch (e: Exception) {
                _serviceState.value = ServiceState.Error(e.message ?: "Erro ao criar serviço")
            }
        }
    }

    fun getServiceById(id: String) {
        viewModelScope.launch {
            _serviceState.value = ServiceState.Loading
            try {
                val result = getServiceByIdUseCase(id)
                result.fold(
                    onSuccess = { 
                        _serviceState.value = ServiceState.Success
                        _service.value = it
                    },
                    onFailure = { 
                        _serviceState.value = ServiceState.Error(it.message ?: "Erro ao buscar serviço")
                    }
                )
            } catch (e: Exception) {
                _serviceState.value = ServiceState.Error(e.message ?: "Erro ao buscar serviço")
            }
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            _serviceState.value = ServiceState.Loading
            try {
                val result = updateServiceUseCase(service)
                result.fold(
                    onSuccess = { 
                        _serviceState.value = ServiceState.Success
                        _service.value = it
                    },
                    onFailure = { 
                        _serviceState.value = ServiceState.Error(it.message ?: "Erro ao atualizar serviço")
                    }
                )
            } catch (e: Exception) {
                _serviceState.value = ServiceState.Error(e.message ?: "Erro ao atualizar serviço")
            }
        }
    }

    fun deleteService(id: String) {
        viewModelScope.launch {
            _serviceState.value = ServiceState.Loading
            try {
                val result = deleteServiceUseCase(id)
                result.fold(
                    onSuccess = { 
                        _serviceState.value = ServiceState.Success
                        _service.value = null
                    },
                    onFailure = { 
                        _serviceState.value = ServiceState.Error(it.message ?: "Erro ao excluir serviço")
                    }
                )
            } catch (e: Exception) {
                _serviceState.value = ServiceState.Error(e.message ?: "Erro ao excluir serviço")
            }
        }
    }

    fun getAllServices() {
        viewModelScope.launch {
            _serviceState.value = ServiceState.Loading
            try {
                getAllServicesUseCase().collect { servicesList ->
                    _services.value = servicesList
                    _serviceState.value = ServiceState.Success
                }
            } catch (e: Exception) {
                _serviceState.value = ServiceState.Error(e.message ?: "Erro ao buscar serviços")
            }
        }
    }

    fun getServicesByProfessional(professionalRef: DocumentReference) {
        viewModelScope.launch {
            _serviceState.value = ServiceState.Loading
            try {
                getServicesByProfessionalUseCase(professionalRef).collect { servicesList ->
                    _services.value = servicesList
                    _serviceState.value = ServiceState.Success
                }
            } catch (e: Exception) {
                _serviceState.value = ServiceState.Error(e.message ?: "Erro ao buscar serviços por profissional")
            }
        }
    }

    fun getServicesByCategory(categoryRef: DocumentReference) {
        viewModelScope.launch {
            _serviceState.value = ServiceState.Loading
            try {
                getServicesByCategoryUseCase(categoryRef).collect { servicesList ->
                    _services.value = servicesList
                    _serviceState.value = ServiceState.Success
                }
            } catch (e: Exception) {
                _serviceState.value = ServiceState.Error(e.message ?: "Erro ao buscar serviços por categoria")
            }
        }
    }

    sealed class ServiceState {
        object Idle : ServiceState()
        object Loading : ServiceState()
        object Success : ServiceState()
        data class Error(val message: String) : ServiceState()
    }
} 