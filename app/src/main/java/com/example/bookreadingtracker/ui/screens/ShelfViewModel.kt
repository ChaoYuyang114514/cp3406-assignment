package com.example.bookreadingtracker.ui.screens
import androidx.lifecycle.*
import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@HiltViewModel
class ShelfViewModel @Inject constructor(
    private val repo: BookRepository
): ViewModel() {
    val books:StateFlow<List<Book>> =
        repo.observeSavedBooks().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onRemoveFromShelf(id: String) {
        viewModelScope.launch { repo.toggleShelf(id) }
    }
}
