package dev.logickoder.synote.notes.impl.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.logickoder.synote.notes.api.NoteId
import java.time.LocalDateTime

@Entity(tableName = "notes")
internal data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: NoteId = NoteId(0L),
    val title: String = "",
    val content: String = "",
    val dateCreated: LocalDateTime = LocalDateTime.now(),
    val dateModified: LocalDateTime? = null,
)