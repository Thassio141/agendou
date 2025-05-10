package br.com.agendou.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Service
import br.com.agendou.domain.usecases.service.ServiceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServiceViewModel(
    private val serviceUseCase : ServiceUseCase,
) : ViewModel() {

    private val _servicesState = MutableStateFlow<ServicesState>(ServicesState.Loading)
    val servicesState: StateFlow<ServicesState> = _servicesState

    private val _serviceDetailState = MutableStateFlow<ServiceDetailState>(ServiceDetailState.Loading)
    val serviceDetailState: StateFlow<ServiceDetailState> = _serviceDetailState

    private val _operationResult = MutableLiveData<Result<Unit>>()
    val operationResult: LiveData<Result<Unit>> = _operationResult

    fun getServices(providerId: String? = null) {
        viewModelScope.launch {
            _servicesState.value = ServicesState.Loading
            try {
                val services = getServicesUseCase(providerId)
                _servicesState.value = ServicesState.Success(services)
            } catch (e: Exception) {
                _servicesState.value = ServicesState.Error(e.message ?: "Erro ao buscar serviços")
            }
        }
    }

    fun getServiceById(serviceId: String) {
        viewModelScope.launch {
            _serviceDetailState.value = ServiceDetailState.Loading
            try {
                val service = serviceUseCase.getServiceById(serviceId)
                _serviceDetailState.value = ServiceDetailState.Success(service)
            } catch (e: Exception) {
                _serviceDetailState.value = ServiceDetailState.Error(e.message ?: "Erro ao buscar serviço")
            }
        }
    }

    fun createService(service: Service) {
        viewModelScope.launch {
            try {
                serviceUseCase.createService(service)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun updateService(service: Service) {
        viewModelScope.launch {
            try {
                serviceUseCase.updateService(service)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun deleteService(serviceId: String) {
        viewModelScope.launch {
            try {
                serviceUseCase.deleteService(serviceId)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    sealed class ServicesState {
        object Loading : ServicesState()
        data class Success(val services: List<Service>) : ServicesState()
        data class Error(val message: String) : ServicesState()
    }

    sealed class ServiceDetailState {
        object Loading : ServiceDetailState()
        data class Success(val service: Service) : ServiceDetailState()
        data class Error(val message: String) : ServiceDetailState()
    }
} 