package br.com.agendou.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Review
import br.com.agendou.domain.usecases.review.*
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ReviewViewModel @Inject constructor(
    private val createReviewUseCase: CreateReviewUseCase,
    private val getReviewByIdUseCase: GetReviewByIdUseCase,
    private val updateReviewUseCase: UpdateReviewUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
    private val getAllReviewsUseCase: GetAllReviewsUseCase,
    private val getReviewsByClientUseCase: GetReviewsByClientUseCase,
    private val getReviewsByProfessionalUseCase: GetReviewsByProfessionalUseCase,
    private val getReviewsByAppointmentUseCase: GetReviewsByAppointmentUseCase
) : ViewModel() {

    private val _reviewState = MutableStateFlow<ReviewState>(ReviewState.Idle)
    val reviewState: StateFlow<ReviewState> = _reviewState

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    private val _review = MutableStateFlow<Review?>(null)
    val review: StateFlow<Review?> = _review

    fun createReview(review: Review) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                val result = createReviewUseCase(review)
                result.fold(
                    onSuccess = { 
                        _reviewState.value = ReviewState.Success
                        _review.value = it
                    },
                    onFailure = { 
                        _reviewState.value = ReviewState.Error(it.message ?: "Erro ao criar avaliação")
                    }
                )
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao criar avaliação")
            }
        }
    }

    fun getReviewById(id: String) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                val result = getReviewByIdUseCase(id)
                result.fold(
                    onSuccess = { 
                        _reviewState.value = ReviewState.Success
                        _review.value = it
                    },
                    onFailure = { 
                        _reviewState.value = ReviewState.Error(it.message ?: "Erro ao buscar avaliação")
                    }
                )
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao buscar avaliação")
            }
        }
    }

    fun updateReview(review: Review) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                val result = updateReviewUseCase(review)
                result.fold(
                    onSuccess = { 
                        _reviewState.value = ReviewState.Success
                        _review.value = it
                    },
                    onFailure = { 
                        _reviewState.value = ReviewState.Error(it.message ?: "Erro ao atualizar avaliação")
                    }
                )
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao atualizar avaliação")
            }
        }
    }

    fun deleteReview(id: String) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                val result = deleteReviewUseCase(id)
                result.fold(
                    onSuccess = { 
                        _reviewState.value = ReviewState.Success
                        _review.value = null
                    },
                    onFailure = { 
                        _reviewState.value = ReviewState.Error(it.message ?: "Erro ao excluir avaliação")
                    }
                )
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao excluir avaliação")
            }
        }
    }

    fun getAllReviews() {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                getAllReviewsUseCase().collect { reviewsList ->
                    _reviews.value = reviewsList
                    _reviewState.value = ReviewState.Success
                }
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao buscar avaliações")
            }
        }
    }

    fun getReviewsByClient(clientRef: DocumentReference) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                getReviewsByClientUseCase(clientRef).collect { reviewsList ->
                    _reviews.value = reviewsList
                    _reviewState.value = ReviewState.Success
                }
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao buscar avaliações por cliente")
            }
        }
    }

    fun getReviewsByProfessional(professionalRef: DocumentReference) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                getReviewsByProfessionalUseCase(professionalRef).collect { reviewsList ->
                    _reviews.value = reviewsList
                    _reviewState.value = ReviewState.Success
                }
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao buscar avaliações por profissional")
            }
        }
    }

    fun getReviewsByAppointment(appointmentRef: DocumentReference) {
        viewModelScope.launch {
            _reviewState.value = ReviewState.Loading
            try {
                getReviewsByAppointmentUseCase(appointmentRef).collect { reviewsList ->
                    _reviews.value = reviewsList
                    _reviewState.value = ReviewState.Success
                }
            } catch (e: Exception) {
                _reviewState.value = ReviewState.Error(e.message ?: "Erro ao buscar avaliações por agendamento")
            }
        }
    }

    sealed class ReviewState {
        object Idle : ReviewState()
        object Loading : ReviewState()
        object Success : ReviewState()
        data class Error(val message: String) : ReviewState()
    }
} 