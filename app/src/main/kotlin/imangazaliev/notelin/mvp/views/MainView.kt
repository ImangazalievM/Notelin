package imangazaliev.notelin.mvp.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import imangazaliev.notelin.mvp.models.Note
import java.util.*

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView : MvpView {

    fun onNotesLoaded(notes: ArrayList<Note>)

    fun updateView()

    fun onSearchResult(notes: ArrayList<Note>)

    fun onAllNotesDeleted()

    fun onNoteDeleted()

    fun showNoteInfoDialog(noteInfo: String)

    fun hideNoteInfoDialog()

    fun showNoteDeleteDialog(notePosition: Int)

    fun hideNoteDeleteDialog()

    fun showNoteContextDialog(notePosition: Int)

    fun hideNoteContextDialog()

}