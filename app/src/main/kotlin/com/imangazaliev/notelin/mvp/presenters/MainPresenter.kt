package com.imangazaliev.notelin.mvp.presenters

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.imangazaliev.notelin.NotelinApplication
import com.imangazaliev.notelin.bus.NoteDeleteAction
import com.imangazaliev.notelin.bus.NoteEditAction
import com.imangazaliev.notelin.mvp.model.Note
import com.imangazaliev.notelin.mvp.model.NoteDao
import com.imangazaliev.notelin.mvp.views.MainView
import com.imangazaliev.notelin.utils.getNotesSortMethodName
import com.imangazaliev.notelin.utils.setNotesSortMethod
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    enum class SortNotesBy : Comparator<Note> {
        DATE {
            override fun compare(note1: Note, note2: Note) = note1.changedAt!!.compareTo(note2.changedAt)
        },
        NAME {
            override fun compare(note1: Note, note2: Note) = note1.title!!.compareTo(note2.title!!)
        },
    }

    @Inject
    lateinit var noteDao: NoteDao
    private lateinit var notesList: MutableList<Note>

    init {
        NotelinApplication.graph.inject(this)
        EventBus.getDefault().register(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAllNotes()
    }

    fun deleteAllNotes() {
        noteDao.deleteAllNotes()
        notesList.removeAll(notesList)
        viewState.onAllNotesDeleted()
    }

    fun deleteNoteByPosition(position: Int) {
        val note = notesList[position]
        noteDao.deleteNote(note)
        notesList.remove(note)
        viewState.onNoteDeleted()
    }

    fun openNewNote() {
        val newNote = noteDao.createNote()
        notesList.add(newNote)
        sortNotesBy(getCurrentSortMethod())
        viewState.openNoteScreen(newNote.id)
    }

    fun openNote(position: Int) {
        viewState.openNoteScreen(notesList[position].id)
    }

    fun search(query: String) {
        if (query == "") {
            viewState.onSearchResult(notesList)
        } else {
            val searchResults = notesList.filter { it.title!!.startsWith(query, ignoreCase = true) }
            viewState.onSearchResult(searchResults)
        }
    }

    fun sortNotesBy(sortMethod: SortNotesBy) {
        notesList.sortWith(sortMethod)
        setNotesSortMethod(sortMethod.toString())
        viewState.updateView()
    }

    @Subscribe
    fun onNoteEdit(action: NoteEditAction) {
        val notePosition = getNotePositionById(action.noteId)
        notesList[notePosition] = noteDao.getNoteById(action.noteId)!!
        sortNotesBy(getCurrentSortMethod())
    }

    @Subscribe
    fun onNoteDelete(action: NoteDeleteAction) {
        Log.d("Notelin", "onDeleted" + action.noteId);
        val notePosition = getNotePositionById(action.noteId)
        notesList.removeAt(notePosition)
        viewState.updateView()
    }

    fun showNoteContextDialog(position: Int) {
        viewState.showNoteContextDialog(position)
    }

    fun hideNoteContextDialog() {
        viewState.hideNoteContextDialog()
    }

    fun showNoteDeleteDialog(position: Int) {
        viewState.showNoteDeleteDialog(position)
    }

    fun hideNoteDeleteDialog() {
        viewState.hideNoteDeleteDialog()
    }

    fun showNoteInfo(position: Int) {
        viewState.showNoteInfoDialog(notesList[position].getInfo())
    }

    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }

    private fun loadAllNotes() {
        notesList = noteDao.loadAllNotes()
        Collections.sort(notesList, getCurrentSortMethod())
        viewState.onNotesLoaded(notesList)
    }

    private fun getCurrentSortMethod(): SortNotesBy {
        val defaultSortMethodName = SortNotesBy.DATE.toString()
        val currentSortMethodName = getNotesSortMethodName(defaultSortMethodName)
        return SortNotesBy.valueOf(currentSortMethodName)
    }

    private fun getNotePositionById(noteId: Long) = notesList.indexOfFirst { it.id == noteId }

}
