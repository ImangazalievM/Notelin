package imangazaliev.notelin.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import imangazaliev.notelin.R
import imangazaliev.notelin.mvp.common.MvpAppCompatActivity
import imangazaliev.notelin.mvp.models.Note
import imangazaliev.notelin.mvp.presenters.NotePresenter
import imangazaliev.notelin.mvp.views.NoteView
import imangazaliev.notelin.utils.formatDate
import kotlinx.android.synthetic.main.activity_note.*

class NoteActivity : MvpAppCompatActivity(), NoteView {

    @InjectPresenter
    lateinit var mPresenter: NotePresenter
    private var mNoteDeleteDialog: MaterialDialog? = null
    private var mNoteInfoDialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        //перемещаем курсор в конец поля ввода
        etTitle.onFocusChangeListener = View.OnFocusChangeListener() { view, hasFocus ->
            if (hasFocus) {
                var editText = view as EditText
                editText.setSelection((editText.text.length))
            }
        };

        val noteId = intent.extras.getLong("note_id", -1)
        val notePosition = intent.extras.getInt("note_position", -1)
        mPresenter.showNote(noteId, notePosition)
    }

    override fun showNote(note: Note) {
        tvNoteDate.text = formatDate(note.changeDate)
        etTitle.setText(note.title)
        etText.setText(note.text)
    }

    override fun showNoteInfoDialog(noteInfo: String) {
        mNoteInfoDialog = MaterialDialog.Builder(this)
                .title("Информация о заметке")
                .positiveText("Ок")
                .content(noteInfo)
                .onPositive { materialDialog, dialogAction -> mPresenter.hideNoteInfoDialog() }
                .cancelListener { mPresenter.hideNoteInfoDialog() }
                .show()
    }

    override fun hideNoteInfoDialog() {
        mNoteInfoDialog?.dismiss()
    }

    override fun showNoteDeleteDialog() {
        mNoteDeleteDialog = MaterialDialog.Builder(this)
                .title("Удаление заметки")
                .content("Вы действительно хотите удалить заметку")
                .positiveText("Да")
                .negativeText("Нет")
                .onPositive { materialDialog, dialogAction ->
                    mPresenter.hideNoteDeleteDialog()
                    mPresenter.deleteNote()
                }
                .onNegative { materialDialog, dialogAction -> mPresenter.hideNoteDeleteDialog() }
                .cancelListener { mPresenter.hideNoteDeleteDialog() }
                .show()
    }


    override fun hideNoteDeleteDialog() {
        mNoteDeleteDialog?.dismiss()
    }

    override fun onNoteSaved() {
        Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
    }

    override fun onNoteDeleted() {
        Toast.makeText(this, R.string.note_is_deleted, Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.note, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSaveNote -> mPresenter.saveNote(etTitle.text.toString(), etText.text.toString())

            R.id.menuDeleteNote -> mPresenter.showNoteDeleteDialog()

            R.id.menuNoteInfo -> mPresenter.showNoteInfoDialog()
        }
        return super.onOptionsItemSelected(item)
    }

}