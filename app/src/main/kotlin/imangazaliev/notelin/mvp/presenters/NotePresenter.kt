package imangazaliev.notelin.mvp.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import imangazaliev.notelin.NotelinApplication
import imangazaliev.notelin.mvp.models.Note
import imangazaliev.notelin.mvp.models.NoteModel
import imangazaliev.notelin.mvp.views.NoteView
import imangazaliev.notelin.utils.DateUtils
import java.util.*
import javax.inject.Inject

@InjectViewState
class NotePresenter : MvpPresenter<NoteView> {

    @Inject
    lateinit var mNoteWrapper: NoteModel
    lateinit var mNote: Note

    constructor() : super() {
        NotelinApplication.graph.inject(this)
    }

    fun showNote(noteId: Long) {
        mNote = mNoteWrapper.getNoteById(noteId)
        viewState.showNote(mNote)
    }

    fun saveNote(title: String, text: String) {
        mNote.title = title
        mNote.text = text
        mNote.changeDate = Date()
        mNoteWrapper.saveNote(mNote)
    }

    fun deleteNote() {
        mNoteWrapper.deleteNote(mNote)
    }

    fun showNoteDeleteDialog() {
        viewState.showNoteDeleteDialog()
    }

    fun hideNoteDeleteDialog() {
        viewState.hideNoteDeleteDialog()
    }

    fun showNoteInfoDialog() {
        val noteInfo = "Название:\n${mNote.title}\n" +
                "Время создания:\n${DateUtils.formatDate(mNote.changeDate)}\n" +
                "Время изменения:\n${DateUtils.formatDate(mNote.changeDate)}";
        viewState.showNoteInfoDialog(noteInfo)
    }

    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }

}
