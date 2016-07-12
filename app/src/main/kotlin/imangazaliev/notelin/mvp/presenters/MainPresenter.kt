package imangazaliev.notelin.mvp.presenters

import android.app.Activity
import android.content.Intent
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import imangazaliev.notelin.NotelinApplication
import imangazaliev.notelin.bus.NoteDeleteAction
import imangazaliev.notelin.bus.NoteEditAction
import imangazaliev.notelin.mvp.models.Note
import imangazaliev.notelin.mvp.models.NoteDao
import imangazaliev.notelin.mvp.views.MainView
import imangazaliev.notelin.ui.activities.NoteActivity
import imangazaliev.notelin.utils.getNotesSortMethodName
import imangazaliev.notelin.utils.setNotesSortMethod
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import javax.inject.Inject

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    enum class SortNotesBy : Comparator<Note> {
        DATE {
            override fun compare(lhs: Note, rhs: Note) = lhs.changeDate!!.compareTo(rhs.changeDate)
        },
        NAME {
            override fun compare(lhs: Note, rhs: Note) = lhs.title!!.compareTo(rhs.title!!)
        },
    }

    @Inject
    lateinit var mNoteDao: NoteDao
    lateinit var mNotesList: MutableList<Note>

    init {
        NotelinApplication.graph.inject(this)
        EventBus.getDefault().register(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadAllNotes()
    }

    /**
     * Загружает все существующие заметки и передает во View
     */
    fun loadAllNotes() {
        mNotesList = mNoteDao.loadAllNotes()
        Collections.sort(mNotesList, getCurrentSortMethod())
        viewState.onNotesLoaded(mNotesList)
    }

    /**
     * Удаляет все существующие заметки
     */
    fun deleteAllNotes() {
        mNoteDao.deleteAllNotes()
        mNotesList.removeAll(mNotesList)
        viewState.onAllNotesDeleted()
    }

    /**
     * Удаляет заметку по позиции
     */
    fun deleteNoteByPosition(position: Int) {
        val note = mNotesList[position];
        mNoteDao.deleteNote(note)
        mNotesList.remove(note)
        viewState.onNoteDeleted()
    }

    fun openNewNote(activity: Activity) {
        val newNote = mNoteDao.createNote()
        mNotesList.add(newNote)
        sortNotesBy(getCurrentSortMethod())
        openNote(activity, mNotesList.indexOf(newNote))
    }

    /**
     * Открывает активити с заметкой по позиции
     */
    fun openNote(activity: Activity, position: Int) {
        val intent = Intent(activity, NoteActivity::class.java)
        intent.putExtra("note_position", position)
        intent.putExtra("note_id", mNotesList[position].id)
        activity.startActivity(intent)
    }

    /**
     * Ищет заметку по имени
     */
    fun search(query: String) {
        if (query.equals("")) {
            viewState.onSearchResult(mNotesList)
        } else {
            val searchResults = mNotesList.filter { it.title!!.startsWith(query, ignoreCase = true) }
            viewState.onSearchResult(searchResults)
        }
    }

    /**
     * Сортирует заметки
     */
    fun sortNotesBy(sortMethod: SortNotesBy) {
        mNotesList.sortWith(sortMethod)
        setNotesSortMethod(sortMethod.toString())
        viewState.updateView()
    }

    fun getCurrentSortMethod(): SortNotesBy {
        val defaultSortMethodName = SortNotesBy.DATE.toString()
        val currentSortMethodName = getNotesSortMethodName(defaultSortMethodName)
        return SortNotesBy.valueOf(currentSortMethodName)
    }
    /**
     * Срабатывает при сохранении заметки на экране редактирования
     */
    @Subscribe
    fun onNoteEdit(action: NoteEditAction) {
        val notePosition = action.position
        mNotesList[notePosition] = mNoteDao.getNoteById(mNotesList[notePosition].id) //обновляем заметку по позиции
        sortNotesBy(getCurrentSortMethod())
    }

    /**
     * Срабатывает при удалении заметки на экране редактирования
     */
    @Subscribe
    fun onNoteDelete(action: NoteDeleteAction) {
        mNotesList.removeAt(action.position)
        viewState.updateView()
    }

    /**
     * Показывает контекстное меню заметки
     */
    fun showNoteContextDialog(position: Int) {
        viewState.showNoteContextDialog(position)
    }

    /**
     * Прячет контекстное меню заметки
     */
    fun hideNoteContextDialog() {
        viewState.hideNoteContextDialog()
    }

    /**
     * Показывает диалог удаления заметки
     */
    fun showNoteDeleteDialog(position: Int) {
        viewState.showNoteDeleteDialog(position)
    }

    /**
     * Прячет диалог удаления заметки
     */
    fun hideNoteDeleteDialog() {
        viewState.hideNoteDeleteDialog()
    }

    /**
     * Показывает диалог с информацией о заметке
     */
    fun showNoteInfo(position: Int) {
        viewState.showNoteInfoDialog(mNotesList[position].getInfo())
    }

    /**
     * Прячет диалог с информацией о заметке
     */
    fun hideNoteInfoDialog() {
        viewState.hideNoteInfoDialog()
    }

}
