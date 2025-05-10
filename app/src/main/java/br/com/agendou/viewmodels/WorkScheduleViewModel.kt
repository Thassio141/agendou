package br.com.agendou.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.WorkSchedule
import br.com.agendou.domain.usecases.workschedule.WorkScheduleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class WorkScheduleViewModel(
    private val workScheduleUseCase: WorkScheduleUseCase
) : ViewModel() {

    private val _workSchedulesState = MutableStateFlow<WorkSchedulesState>(WorkSchedulesState.Loading)
    val workSchedulesState: StateFlow<WorkSchedulesState> = _workSchedulesState

    private val _operationResult = MutableLiveData<Result<Unit>>()
    val operationResult: LiveData<Result<Unit>> = _operationResult

    fun getWorkSchedules(providerId: String, date: Date? = null) {
        viewModelScope.launch {
            _workSchedulesState.value = WorkSchedulesState.Loading
            try {
                val workSchedules = getWorkSchedulesUseCase(providerId, date)
                _workSchedulesState.value = WorkSchedulesState.Success(workSchedules)
            } catch (e: Exception) {
                _workSchedulesState.value = WorkSchedulesState.Error(e.message ?: "Erro ao buscar horários")
            }
        }
    }

    fun createWorkSchedule(workSchedule: WorkSchedule) {
        viewModelScope.launch {
            try {
                workScheduleUseCase.createWorkSchedule(workSchedule)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun updateWorkSchedule(workSchedule: WorkSchedule) {
        viewModelScope.launch {
            try {
                workScheduleUseCase.updateWorkSchedule(workSchedule)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun deleteWorkSchedule(workScheduleId: String) {
        viewModelScope.launch {
            try {
                workScheduleUseCase.deleteWorkSchedule(workScheduleId)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    sealed class WorkSchedulesState {
        object Loading : WorkSchedulesState()
        data class Success(val workSchedules: List<WorkSchedule>) : WorkSchedulesState()
        data class Error(val message: String) : WorkSchedulesState()
    }
} 