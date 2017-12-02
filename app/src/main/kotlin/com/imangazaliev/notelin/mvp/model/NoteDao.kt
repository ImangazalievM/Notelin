package com.imangazaliev.notelin.mvp.model

import com.reactiveandroid.query.Delete
import com.reactiveandroid.query.Select
import org.jetbrains.annotations.Nullable
import java.util.*

class NoteDao {

    fun createNote(): Note {
        val note = Note("New note", Date())
        note.save()
        return note
    }

    @Nullable
    fun saveNote(note: Note): Long = note.save()

    fun loadAllNotes(): MutableList<Note> = Select.from(Note::class.java).fetch()

    fun getNoteById(noteId: Long): Note? = Select.from(Note::class.java).where("id = ?", noteId).fetchSingle()

    fun deleteAllNotes() {
        Delete.from(Note::class.java).execute()
    }

    fun deleteNote(note: Note) {
        note.delete()
    }

}
