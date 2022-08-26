package dev.logickoder.synote.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumble.appyx.navmodel.backstack.BackStack
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.logickoder.synote.core.Navigation
import dev.logickoder.synote.data.model.NoteEntity
import dev.logickoder.synote.data.repository.AuthRepository
import dev.logickoder.synote.data.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NotesRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _notes = MutableStateFlow(emptyList<NoteEntity>())
    val notes: Flow<List<NoteEntity>>
        get() = _notes

    fun getNotes() {
        viewModelScope.launch {
            launch {
                authRepository.currentUser.take(1).collectLatest {
                    repository.refreshNotes(it!!.id)
                }
            }
            launch {
                repository.notes.collectLatest {
                    _notes.emit(it)
                }
            }
        }
    }

    fun editNote(noteId: String?, backStack: BackStack<Navigation.Route>) {

    }

    fun search(text: String) {
        viewModelScope.launch {
            repository.notes.take(1).flowOn(Dispatchers.Main).collectLatest { notes ->
                val list = if (text.isNotBlank()) {
                    notes.filter {
                        it.title.contains(text, ignoreCase = true)
                                || it.content.contains(text, ignoreCase = true)
                    }
                } else notes
                _notes.emit(list)
            }
        }
    }
}