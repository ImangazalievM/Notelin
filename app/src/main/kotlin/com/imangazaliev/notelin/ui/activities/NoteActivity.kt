package com.imangazaliev.notelin.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.imangazaliev.notelin.R
import com.imangazaliev.notelin.mvp.model.Note
import com.imangazaliev.notelin.mvp.presenters.NotePresenter
import com.imangazaliev.notelin.mvp.views.NoteView
import com.imangazaliev.notelin.utils.formatDate
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : MvpAppCompatActivity(), NoteView {

    companion object {
        const val NOTE_DELETE_ARG = "note_id"

        fun buildIntent(activity: Activity, noteId: Long) : Intent{
            val intent = Intent(activity, NoteActivity::class.java)
            intent.putExtra(NOTE_DELETE_ARG, noteId)
            return intent
        }
    }

    @InjectPresenter
    lateinit var presenter: NotePresenter
    private var noteDeleteDialog: MaterialDialog? = null
    private var noteInfoDialog: MaterialDialog? = null

    @ProvidePresenter
    fun provideHelloPresenter(): NotePresenter {
        val noteId = intent.extras.getLong(NOTE_DELETE_ARG, -1)
        return NotePresenter(noteId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        etTitle.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                val editText = view as EditText
                editText.setSelection((editText.text.length))
            }
        }
    }

    override fun showNote(note: Note) {
        tvNoteDate.text = formatDate(note.changedAt)
        etTitle.setText(note.title)
        etText.setText(note.text)
    }

    override fun showNoteInfoDialog(noteInfo: String) {
        noteInfoDialog = MaterialDialog.Builder(this)
                .title(R.string.note_info)
                .positiveText(R.string.ok)
                .content(noteInfo)
                .onPositive { materialDialog, dialogAction -> presenter.hideNoteInfoDialog() }
                .cancelListener { presenter.hideNoteInfoDialog() }
                .show()
    }

    override fun hideNoteInfoDialog() {
        noteInfoDialog?.dismiss()
    }

    override fun showNoteDeleteDialog() {
        noteDeleteDialog = MaterialDialog.Builder(this)
                .title(getString(R.string.note_deletion_title))
                .content(getString(R.string.note_deletion_message))
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no))
                .onPositive { _, _ ->
                    presenter.hideNoteDeleteDialog()
                    presenter.deleteNote()
                }
                .onNegative { _, _ -> presenter.hideNoteDeleteDialog() }
                .cancelListener { presenter.hideNoteDeleteDialog() }
                .show()
    }


    override fun hideNoteDeleteDialog() {
        noteDeleteDialog?.dismiss()
    }

    override fun onNoteSaved() {
        Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
    }

    override fun onNoteDeleted() {
        Toast.makeText(this, R.string.note_deleted, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSaveNote -> presenter.saveNote(etTitle.text.toString(), etText.text.toString())

            R.id.menuDeleteNote -> presenter.showNoteDeleteDialog()

            R.id.menuNoteInfo -> presenter.showNoteInfoDialog()
        }
        return super.onOptionsItemSelected(item)
    }

}