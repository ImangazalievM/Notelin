package imangazaliev.notelin.mvp.presenters

import android.content.Context
import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import imangazaliev.notelin.NotelinApplication
import imangazaliev.notelin.mvp.common.SortDate
import imangazaliev.notelin.mvp.common.SortName
import imangazaliev.notelin.mvp.models.Note
import imangazaliev.notelin.mvp.models.NoteModel
import imangazaliev.notelin.mvp.views.MainView
import imangazaliev.notelin.ui.activities.NoteActivity
import imangazaliev.notelin.utils.DateUtils
import imangazaliev.notelin.utils.PrefsUtils
import java.util.*
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView> {

    enum class SortNotesBy {
        DATE, NAME
    }

    @Inject
    lateinit var mNoteWrapper: NoteModel
    lateinit var mNotesList: ArrayList<Note>

    constructor() : super() {
        NotelinApplication.graph.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadAllNotes()
    }

    /**
     * Создает новую заметку
     */
    fun createNote(): Note {
        val note = mNoteWrapper.createNote()
        mNotesList.add(note)
        return note
    }

    /**
     * Загружает все существующие заметки и передает во View
     */
    fun loadAllNotes() {
        mNotesList = mNoteWrapper.loadAllNotes() as ArrayList<Note>
        val sortMethod = getSortMethod(SortNotesBy.valueOf(PrefsUtils.getNotesSortMethod(SortNotesBy.DATE.toString())))
        Collections.sort(mNotesList, sortMethod)
        viewState.onNotesLoaded(mNotesList.clone() as ArrayList<Note>)
    }

    /**
     * Удаляет все существующие заметки
     */
    fun deleteAllNotes() {
        mNoteWrapper.deleteAllNotes()
        mNotesList.removeAll(mNotesList)
        viewState.onAllNotesDeleted()
    }

    /**
     * Удаляет заметку по позиции
     */
    fun deleteNoteByPosition(position: Int) {
        val note = mNotesList[position];
        mNoteWrapper.deleteNote(note)
        mNotesList.remove(note)
        viewState.onNoteDeleted(note)
    }

    fun openNote(context: Context, position: Int) {
        openNote(context, mNotesList[position])
    }

    fun openNote(context: Context, note: Note) {
        val intent = Intent(context, NoteActivity::class.java)
        intent.putExtra("note_id", note.id)
        context.startActivity(intent)
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
        val note = mNotesList[position]
        val noteInfo = "Название:\n${note.title}\n" +
                "Время создания:\n${DateUtils.formatDate(note.changeDate)}\n" +
                "Время изменения:\n${DateUtils.formatDate(note.changeDate)}";
        viewState.showNoteInfoDialog(noteInfo)
    }

    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }

    fun sortNotesBy(sortMethod: SortNotesBy) {
        Collections.sort(mNotesList, getSortMethod(sortMethod))
        viewState.updateView()
        PrefsUtils.setNotesSortMethod(sortMethod.toString())
    }

    fun getSortMethod(sortBy: SortNotesBy): Comparator<Note> {
        when (sortBy) {
            SortNotesBy.NAME -> return SortName()
            SortNotesBy.DATE -> return SortDate()
        }
    }

    fun search(query: String) {
        if (query.equals("")) {
            viewState.onSearchResult(mNotesList)
        } else {
            val searchResults = mNotesList.filter { note -> note.title!!.startsWith(query) }
            viewState.onSearchResult(searchResults as ArrayList<Note>)
        }
    }

}
