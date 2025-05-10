package br.com.agendou.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.agendou.domain.models.Review
import br.com.agendou.domain.usecases.review.ReviewUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewUseCase: ReviewUseCase
) : ViewModel() {

    private val _reviewsState = MutableStateFlow<ReviewsState>(ReviewsState.Loading)
    val reviewsState: StateFlow<ReviewsState> = _reviewsState

    private val _operationResult = MutableLiveData<Result<Unit>>()
    val operationResult: LiveData<Result<Unit>> = _operationResult

    fun getReviews(providerId: String) {
        viewModelScope.launch {
            _reviewsState.value = ReviewsState.Loading
            try {
                val reviews = reviewUseCase.getAllReviews(providerId)
                _reviewsState.value = ReviewsState.Success(reviews)
            } catch (e: Exception) {
                _reviewsState.value = ReviewsState.Error(e.message ?: "Erro ao buscar avaliações")
            }
        }
    }

    fun createReview(review: Review) {
        viewModelScope.launch {
            try {
                reviewUseCase.createReview(review)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun updateReview(review: Review) {
        viewModelScope.launch {
            try {
                reviewUseCase.updateReview(review)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            try {
                reviewUseCase.deleteReview(reviewId)
                _operationResult.value = Result.success(Unit)
            } catch (e: Exception) {
                _operationResult.value = Result.failure(e)
            }
        }
    }

    sealed class ReviewsState {
        object Loading : ReviewsState()
        data class Success(val reviews: List<Review>) : ReviewsState()
        data class Error(val message: String) : ReviewsState()
    }
} 