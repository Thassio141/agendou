package br.com.agendou.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.WorkSchedule
import br.com.agendou.domain.usecases.workschedule.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class WorkScheduleViewModel @Inject constructor(
    private val createWorkScheduleUseCase: CreateWorkScheduleUseCase,
    private val getWorkScheduleByIdUseCase: GetWorkScheduleByIdUseCase,
    private val updateWorkScheduleUseCase: UpdateWorkScheduleUseCase,
    private val deleteWorkScheduleUseCase: DeleteWorkScheduleUseCase,
    private val getAllWorkSchedulesUseCase: GetAllWorkSchedulesUseCase,
    private val getWorkSchedulesByProfessionalUseCase: GetWorkSchedulesByProfessionalUseCase
) : ViewModel() {

    private val _workScheduleState = MutableStateFlow<WorkScheduleState>(WorkScheduleState.Idle)
    val workScheduleState: StateFlow<WorkScheduleState> = _workScheduleState

    private val _workSchedules = MutableStateFlow<List<WorkSchedule>>(emptyList())
    val workSchedules: StateFlow<List<WorkSchedule>> = _workSchedules

    private val _workSchedule = MutableStateFlow<WorkSchedule?>(null)
    val workSchedule: StateFlow<WorkSchedule?> = _workSchedule

    fun createWorkSchedule(workSchedule: WorkSchedule) {
        viewModelScope.launch {
            _workScheduleState.value = WorkScheduleState.Loading
            try {
                val result = createWorkScheduleUseCase(workSchedule)
                result.fold(
                    onSuccess = { 
                        _workScheduleState.value = WorkScheduleState.Success
                        _workSchedule.value = it
                    },
                    onFailure = { 
                        _workScheduleState.value = WorkScheduleState.Error(it.message ?: "Erro ao criar agenda de trabalho")
                    }
                )
            } catch (e: Exception) {
                _workScheduleState.value = WorkScheduleState.Error(e.message ?: "Erro ao criar agenda de trabalho")
            }
        }
    }

    fun getWorkScheduleById(id: String) {
        viewModelScope.launch {
            _workScheduleState.value = WorkScheduleState.Loading
            try {
                val result = getWorkScheduleByIdUseCase(id)
                result.fold(
                    onSuccess = { 
                        _workScheduleState.value = WorkScheduleState.Success
                        _workSchedule.value = it
                    },
                    onFailure = { 
                        _workScheduleState.value = WorkScheduleState.Error(it.message ?: "Erro ao buscar agenda de trabalho")
                    }
                )
            } catch (e: Exception) {
                _workScheduleState.value = WorkScheduleState.Error(e.message ?: "Erro ao buscar agenda de trabalho")
            }
        }
    }

    fun updateWorkSchedule(workSchedule: WorkSchedule) {
        viewModelScope.launch {
            _workScheduleState.value = WorkScheduleState.Loading
            try {
                val result = updateWorkScheduleUseCase(workSchedule)
                result.fold(
                    onSuccess = { 
                        _workScheduleState.value = WorkScheduleState.Success
                        _workSchedule.value = it
                    },
                    onFailure = { 
                        _workScheduleState.value = WorkScheduleState.Error(it.message ?: "Erro ao atualizar agenda de trabalho")
                    }
                )
            } catch (e: Exception) {
                _workScheduleState.value = WorkScheduleState.Error(e.message ?: "Erro ao atualizar agenda de trabalho")
            }
        }
    }

    fun deleteWorkSchedule(id: String) {
        viewModelScope.launch {
            _workScheduleState.value = WorkScheduleState.Loading
            try {
                val result = deleteWorkScheduleUseCase(id)
                result.fold(
                    onSuccess = { 
                        _workScheduleState.value = WorkScheduleState.Success
                        _workSchedule.value = null
                    },
                    onFailure = { 
                        _workScheduleState.value = WorkScheduleState.Error(it.message ?: "Erro ao excluir agenda de trabalho")
                    }
                )
            } catch (e: Exception) {
                _workScheduleState.value = WorkScheduleState.Error(e.message ?: "Erro ao excluir agenda de trabalho")
            }
        }
    }

    fun getAllWorkSchedules() {
        viewModelScope.launch {
            _workScheduleState.value = WorkScheduleState.Loading
            try {
                getAllWorkSchedulesUseCase().collect { workSchedulesList ->
                    _workSchedules.value = workSchedulesList
                    _workScheduleState.value = WorkScheduleState.Success
                }
            } catch (e: Exception) {
                _workScheduleState.value = WorkScheduleState.Error(e.message ?: "Erro ao buscar agendas de trabalho")
            }
        }
    }

    fun getWorkSchedulesByProfessional(professionalRef: DocumentReference) {
        viewModelScope.launch {
            _workScheduleState.value = WorkScheduleState.Loading
            try {
                getWorkSchedulesByProfessionalUseCase(professionalRef).collect { workSchedulesList ->
                    _workSchedules.value = workSchedulesList
                    _workScheduleState.value = WorkScheduleState.Success
                }
            } catch (e: Exception) {
                _workScheduleState.value = WorkScheduleState.Error(e.message ?: "Erro ao buscar agendas de trabalho por profissional")
            }
        }
    }

    sealed class WorkScheduleState {
        object Idle : WorkScheduleState()
        object Loading : WorkScheduleState()
        object Success : WorkScheduleState()
        data class Error(val message: String) : WorkScheduleState()
    }
} 