package br.com.agendou.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Appointment
import br.com.agendou.domain.usecases.appointment.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppointmentViewModel @Inject constructor(
    private val createAppointmentUseCase: CreateAppointmentUseCase,
    private val getAppointmentByIdUseCase: GetAppointmentByIdUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase,
    private val getAllAppointmentsUseCase: GetAllAppointmentsUseCase,
    private val getAppointmentsByClientUseCase: GetAppointmentsByClientUseCase,
    private val getAppointmentsByProfessionalUseCase: GetAppointmentsByProfessionalUseCase,
    private val getAppointmentsByServiceUseCase: GetAppointmentsByServiceUseCase
) : ViewModel() {

    private val _appointmentState = MutableStateFlow<AppointmentState>(AppointmentState.Idle)
    val appointmentState: StateFlow<AppointmentState> = _appointmentState

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    private val _appointment = MutableStateFlow<Appointment?>(null)
    val appointment: StateFlow<Appointment?> = _appointment

    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                val result = createAppointmentUseCase(appointment)
                result.fold(
                    onSuccess = { 
                        _appointmentState.value = AppointmentState.Success
                        _appointment.value = it
                    },
                    onFailure = { 
                        _appointmentState.value = AppointmentState.Error(it.message ?: "Erro ao criar agendamento")
                    }
                )
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao criar agendamento")
            }
        }
    }

    fun getAppointmentById(id: String) {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                val result = getAppointmentByIdUseCase(id)
                result.fold(
                    onSuccess = { 
                        _appointmentState.value = AppointmentState.Success
                        _appointment.value = it
                    },
                    onFailure = { 
                        _appointmentState.value = AppointmentState.Error(it.message ?: "Erro ao buscar agendamento")
                    }
                )
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao buscar agendamento")
            }
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                val result = updateAppointmentUseCase(appointment)
                result.fold(
                    onSuccess = { 
                        _appointmentState.value = AppointmentState.Success
                        _appointment.value = it
                    },
                    onFailure = { 
                        _appointmentState.value = AppointmentState.Error(it.message ?: "Erro ao atualizar agendamento")
                    }
                )
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao atualizar agendamento")
            }
        }
    }

    fun deleteAppointment(id: String) {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                val result = deleteAppointmentUseCase(id)
                result.fold(
                    onSuccess = { 
                        _appointmentState.value = AppointmentState.Success
                        _appointment.value = null
                    },
                    onFailure = { 
                        _appointmentState.value = AppointmentState.Error(it.message ?: "Erro ao excluir agendamento")
                    }
                )
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao excluir agendamento")
            }
        }
    }

    fun getAllAppointments() {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                getAllAppointmentsUseCase().collect { appointmentsList ->
                    _appointments.value = appointmentsList
                    _appointmentState.value = AppointmentState.Success
                }
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao buscar agendamentos")
            }
        }
    }

    fun getAppointmentsByClient(clientRef: DocumentReference) {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                getAppointmentsByClientUseCase(clientRef).collect { appointmentsList ->
                    _appointments.value = appointmentsList
                    _appointmentState.value = AppointmentState.Success
                }
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao buscar agendamentos por cliente")
            }
        }
    }

    fun getAppointmentsByProfessional(professionalRef: DocumentReference) {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                getAppointmentsByProfessionalUseCase(professionalRef).collect { appointmentsList ->
                    _appointments.value = appointmentsList
                    _appointmentState.value = AppointmentState.Success
                }
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao buscar agendamentos por profissional")
            }
        }
    }

    fun getAppointmentsByService(serviceRef: DocumentReference) {
        viewModelScope.launch {
            _appointmentState.value = AppointmentState.Loading
            try {
                getAppointmentsByServiceUseCase(serviceRef).collect { appointmentsList ->
                    _appointments.value = appointmentsList
                    _appointmentState.value = AppointmentState.Success
                }
            } catch (e: Exception) {
                _appointmentState.value = AppointmentState.Error(e.message ?: "Erro ao buscar agendamentos por serviço")
            }
        }
    }

    sealed class AppointmentState {
        object Idle : AppointmentState()
        object Loading : AppointmentState()
        object Success : AppointmentState()
        data class Error(val message: String) : AppointmentState()
    }
} 