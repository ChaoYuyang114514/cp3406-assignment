package com.example.bookreadingtracker.ui.screens
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.usecase.SearchBooks
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

data class DiscoverUiState(
    val query:String="",
    val isLoading:Boolean=false,
    val results:List<Book> = emptyList(),
    val errorMessage:String?=null
)

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val searchBooks: SearchBooks,
    private val toggleShelf: com.example.bookreadingtracker.domain.usecase.ToggleShelf,
    private val repo: com.example.bookreadingtracker.data.repository.BookRepository
): ViewModel() {
    var uiState by mutableStateOf(DiscoverUiState())
        private set

    fun onQueryChange(q:String){ uiState=uiState.copy(query=q) }

    fun onSearch(){
        val q = uiState.query.trim()
        if(q.isBlank()) return
        viewModelScope.launch {
            uiState = uiState.copy(isLoading=true, errorMessage=null)
            try{
                val r = searchBooks(q)
                uiState = uiState.copy(isLoading=false, results=r, errorMessage = if(r.isEmpty()) "No results" else null)
            } catch(e:Exception){
                uiState = uiState.copy(isLoading=false, errorMessage=e.message)
            }
        }
    }

    fun onAddToShelf(b: Book) {
        viewModelScope.launch {
            runCatching {
                repo.cacheBook(b)
                toggleShelf(b.id)
            }
                .onFailure { e -> uiState = uiState.copy(errorMessage = e.message) }
        }
    }
}
