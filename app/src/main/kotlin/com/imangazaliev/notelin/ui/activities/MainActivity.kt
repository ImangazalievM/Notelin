package com.imangazaliev.notelin.ui.activities

import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.pawegio.kandroid.onQueryChange
import com.imangazaliev.notelin.R
import com.imangazaliev.notelin.mvp.model.Note
import com.imangazaliev.notelin.mvp.presenters.MainPresenter
import com.imangazaliev.notelin.mvp.views.MainView
import com.imangazaliev.notelin.ui.adapters.NotesAdapter
import com.imangazaliev.notelin.ui.commons.ItemClickSupport
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter
    private var noteContextDialog: MaterialDialog? = null
    private var noteDeleteDialog: MaterialDialog? = null
    private var noteInfoDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        with(ItemClickSupport.addTo(notesList)) {
            setOnItemClickListener { _, position, _ -> presenter.openNote(position) }
            setOnItemLongClickListener { _, position, _ -> presenter.showNoteContextDialog(position); true }
        }

        newNoteFab.setOnClickListener { presenter.openNewNote() }
    }

    override fun onNotesLoaded(notes: List<Note>) {
        notesList.adapter = NotesAdapter(notes)
        updateView()
    }

    override fun updateView() {
        notesList.adapter.notifyDataSetChanged()
        if (notesList.adapter.itemCount == 0) {
            notesList.visibility = View.GONE
            tvNotesIsEmpty.visibility = View.VISIBLE
        } else {
            notesList.visibility = View.VISIBLE
            tvNotesIsEmpty.visibility = View.GONE
        }
    }

    override fun onNoteDeleted() {
        updateView()
        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
    }

    override fun onAllNotesDeleted() {
        updateView()
        Toast.makeText(this, R.string.all_notes_deleted, Toast.LENGTH_SHORT).show()
    }

    override fun onSearchResult(notes: List<Note>) {
        notesList.adapter = NotesAdapter(notes)
    }

    override fun showNoteContextDialog(notePosition: Int) {
        noteContextDialog = MaterialDialog.Builder(this)
                .items(R.array.main_note_context)
                .itemsCallback { _, _, position, _ ->
                    onContextDialogItemClick(position, notePosition)
                    presenter.hideNoteContextDialog()
                }
                .cancelListener { presenter.hideNoteContextDialog() }
                .show()
    }

    override fun hideNoteContextDialog() {
        noteContextDialog?.dismiss()
    }

    override fun showNoteDeleteDialog(notePosition: Int) {
        noteDeleteDialog = MaterialDialog.Builder(this)
                .title(getString(R.string.note_deletion_title))
                .content(getString(R.string.note_deletion_message))
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no))
                .onPositive { _, _ ->
                    presenter.deleteNoteByPosition(notePosition)
                    noteInfoDialog?.dismiss()
                }
                .onNegative { _, _ -> presenter.hideNoteDeleteDialog() }
                .cancelListener { presenter.hideNoteDeleteDialog() }
                .show()
    }

    override fun hideNoteDeleteDialog() {
        noteDeleteDialog?.dismiss()
    }

    override fun showNoteInfoDialog(noteInfo: String) {
        noteInfoDialog = MaterialDialog.Builder(this)
                .title(R.string.note_info)
                .positiveText(getString(R.string.ok))
                .content(noteInfo)
                .onPositive { materialDialog, dialogAction -> presenter.hideNoteInfoDialog() }
                .cancelListener { presenter.hideNoteInfoDialog() }
                .show()
    }

    override fun hideNoteInfoDialog() {
        noteInfoDialog?.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        initSearchView(menu)
        return true
    }

    override fun openNoteScreen(noteId: Long) {
        startActivity(NoteActivity.buildIntent(this, noteId))
    }

    private fun initSearchView(menu: Menu) {
        val searchViewMenuItem = menu.findItem(R.id.action_search)
        val searchView = searchViewMenuItem.actionView as SearchView
        searchView.onQueryChange { query -> presenter.search(query) }
        searchView.setOnCloseListener { presenter.search(""); false }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuDeleteAllNotes -> presenter.deleteAllNotes()
            R.id.menuSortByName -> presenter.sortNotesBy(MainPresenter.SortNotesBy.NAME)
            R.id.menuSortByDate -> presenter.sortNotesBy(MainPresenter.SortNotesBy.DATE)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onContextDialogItemClick(contextItemPosition: Int, notePosition: Int) {
        when (contextItemPosition) {
            0 -> presenter.openNote(notePosition)
            1 -> presenter.showNoteInfo(notePosition)
            2 -> presenter.showNoteDeleteDialog(notePosition)
        }
    }

}
