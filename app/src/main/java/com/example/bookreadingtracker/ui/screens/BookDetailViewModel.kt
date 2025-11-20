package com.example.bookreadingtracker.ui.screens
import androidx.lifecycle.*
import com.example.bookreadingtracker.data.repository.BookRepository
import com.example.bookreadingtracker.domain.model.Book
import com.example.bookreadingtracker.domain.model.Note
import com.example.bookreadingtracker.domain.usecase.UpsertNote
import com.example.bookreadingtracker.domain.usecase.DeleteNote
import com.example.bookreadingtracker.domain.usecase.GetNotesForBook
import com.example.bookreadingtracker.domain.model.ReadingSession
import com.example.bookreadingtracker.domain.usecase.GetReadingSessions
import com.example.bookreadingtracker.domain.usecase.LogReading
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class BookDetailUiState(val isLoading:Boolean=true, val book:Book?=null, val error:String?=null)

@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repo: BookRepository,
    savedStateHandle: SavedStateHandle,
    private val upsertNote: UpsertNote,
    private val deleteNote: DeleteNote,
    private val getNotesForBook: GetNotesForBook,
    private val getReadingSessions: GetReadingSessions,
    private val logReading: LogReading
): ViewModel() {
    private val id:String = savedStateHandle["bookId"] ?: ""

    private val _s = MutableStateFlow(BookDetailUiState())
    val uiState:StateFlow<BookDetailUiState> = _s

    val notes: StateFlow<List<Note>> =
        getNotesForBook(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sessions: StateFlow<List<ReadingSession>> =
        getReadingSessions(id).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init { load() }

    private fun load(){
        viewModelScope.launch {
            try{
                val b = repo.getByIdFromCache(id)
                if(b!=null) _s.value = BookDetailUiState(false,b,null)
                else _s.value = BookDetailUiState(false,null,"Not found")
                launch { runCatching { repo.refreshBook(id) } }
            } catch(e:Exception){
                _s.value = BookDetailUiState(false,null,e.message)
            }
        }
    }

    fun onToggleShelf(){ viewModelScope.launch{ repo.toggleShelf(id) } }

    fun addNote(content: String) {
        val now = System.currentTimeMillis()
        val note = Note(id = 0, bookId = id, createdAt = now, location = null, content = content)
        viewModelScope.launch { upsertNote(note) }
    }

    fun removeNote(noteId: Long) {
        viewModelScope.launch { deleteNote(noteId) }
    }

    fun onLogReading(minutes: Int) {
        viewModelScope.launch { logReading(id, minutes) }
    }
}
