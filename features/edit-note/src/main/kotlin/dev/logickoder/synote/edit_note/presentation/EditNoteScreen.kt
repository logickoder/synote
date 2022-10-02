package dev.logickoder.synote.edit_note.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.logickoder.synote.edit_note.R
import dev.logickoder.synote.notes.api.NoteAction
import dev.logickoder.synote.ui.theme.SynoteTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
internal fun EditNoteScreen(
    title: String,
    content: String,
    editedAt: LocalDateTime,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    performAction: (NoteAction, Boolean) -> Unit,
    onTitleChanged: (String) -> Unit,
    onContentChanged: (String) -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val actionMessage = stringResource(R.string.edit_note_action_performed)
    val undoMessage = stringResource(R.string.edit_note_undo)
    Scaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        topBar = {
            EditNoteAppBar(
                modifier = Modifier.fillMaxWidth(),
                navigateBack = navigateBack,
                performAction = {
                    coroutineScope.launch {
                        val result = scaffoldState.snackbarHostState.showSnackbar(
                            message = "$actionMessage ${it}d",
                            actionLabel = undoMessage
                        )
                        when (result) {
                            SnackbarResult.Dismissed -> {
                            }
                            SnackbarResult.ActionPerformed -> {
                                performAction(it, true)
                            }
                        }
                    }
                    performAction(it, false)
                }
            )
        },
        content = { padding ->
            Column(
                content = {
                    OutlinedTextField(
                        value = title,
                        onValueChange = onTitleChanged,
                        textStyle = MaterialTheme.typography.h6,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.edit_note_title),
                                style = MaterialTheme.typography.h6,
                            )
                        }
                    )
                    Divider()
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        value = content,
                        onValueChange = onContentChanged,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            backgroundColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                        ),
                        placeholder = {
                            Text(text = stringResource(R.string.edit_note_note))
                        }
                    )
                }
            )
        },
        bottomBar = {
            EditNoteBottomBar(
                modifier = Modifier.fillMaxWidth(),
                editedAt = editedAt,
            )
        }
    )
}

@Preview
@Composable
private fun EditNoteScreenPreview() = SynoteTheme {
    EditNoteScreen(
        title = "Stub note",
        content = "111111111111111",
        editedAt = LocalDateTime.now(),
        navigateBack = {},
        onContentChanged = {},
        onTitleChanged = {},
        performAction = { _, _ -> }
    )
}