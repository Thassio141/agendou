package br.com.agendou.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Appointment
import br.com.agendou.domain.usecases.appointment.AppointmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class AppointmentViewModel(
    private val appointmentUseCase: AppointmentUseCase
) : ViewModel() {

    private val _appointmentsState = MutableStateFlow<AppointmentsState>(AppointmentsState.Loading)
    val appointmentsState: StateFlow<AppointmentsState> = _appointmentsState

    private val _appointmentDetailState =
        MutableStateFlow<AppointmentDetailState>(AppointmentDetailState.Loading)
    val appointmentDetailState: StateFlow<AppointmentDetailState> = _appointmentDetailState

    private val _operationResult = MutableLiveData<Result<Unit>>()
    val operationResult: LiveData<Result<Unit>> = _operationResult

    fun getAppointments(userId: String, isProvider: Boolean = false, date: Date? = null) {
        viewModelScope.launch {
            _appointmentsState.value = AppointmentsState.Loading
            try {
                val appointments = getAppointmentsUseCase(userId, isProvider, date)
                _appointmentsState.value = AppointmentsState.Success(appointments)
            } catch (e: Exception) {
                _appointmentsState.value = AppointmentsState.Error(e.message ?: "Erro ao buscar agendamentos")
            }
        }
    }

    fun getAppointmentById(appointmentId: String) {
        viewModelScope.launch {
            _appointmentDetailState.value = AppointmentDetailState.Loading
            try {
                val appointment = appointmentUseCase.getAppointmentById(appointmentId)
                _appointmentDetailState.value = AppointmentDetailState.Success(appointment)
            } catch (e: Exception) {
                _appointmentDetailState.value = AppointmentDetailState.Error(e.message ?: "Erro ao buscar agendamento")
            }
        }
    }

    fun createAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                appointmentUseCase.createAppointment(appointment)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun updateAppointment(appointment: Appointment) {
        viewModelScope.launch {
            try {
                appointmentUseCase.updateAppointment(appointment)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun cancelAppointment(appointmentId: String) {
        viewModelScope.launch {
            try {
                cancelAppointmentUseCase(appointmentId)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    sealed class AppointmentsState {
        object Loading : AppointmentsState()
        data class Success(val appointments: List<Appointment>) : AppointmentsState()
        data class Error(val message: String) : AppointmentsState()
    }

    sealed class AppointmentDetailState {
        object Loading : AppointmentDetailState()
        data class Success(val appointment: Appointment) : AppointmentDetailState()
        data class Error(val message: String) : AppointmentDetailState()
    }
}