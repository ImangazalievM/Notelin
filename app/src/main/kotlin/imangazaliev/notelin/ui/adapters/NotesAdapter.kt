package imangazaliev.notelin.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import imangazaliev.notelin.R
import imangazaliev.notelin.mvp.models.Note
import imangazaliev.notelin.utils.formatDate
import java.util.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private var mNotesList: List<Note> = ArrayList()

    constructor(notesList: List<Note>) {
        mNotesList = notesList
    }

    /**
     * Создание новых View и ViewHolder элемента списка, которые впоследствии могут переиспользоваться
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NotesAdapter.ViewHolder {
        var v = LayoutInflater.from(viewGroup.context).inflate(R.layout.note_item_layout, viewGroup, false)
        return NotesAdapter.ViewHolder(v);
    }

    /**
     * Заполнение виджетов View данными из элемента списка с номером i
     */
    override
    fun onBindViewHolder(viewHolder: NotesAdapter.ViewHolder, i: Int) {
        var note = mNotesList[i];
        viewHolder.mNoteTitle.text = note.title;
        viewHolder.mNoteDate.text = formatDate(note.changeDate)
    }

    /**
     * Возвращает количество элементов
     */
    override fun getItemCount(): Int {
        return mNotesList.size
    }

    /**
     * Реализация класса ViewHolder, хранящего ссылки на виджеты.
     */
    class ViewHolder : RecyclerView.ViewHolder {

        var mNoteTitle: TextView
        var mNoteDate: TextView

        constructor(itemView: View) : super(itemView) {
            mNoteTitle = itemView.findViewById(R.id.tvItemNoteTitle) as TextView
            mNoteDate = itemView.findViewById(R.id.tvItemNoteDate) as TextView
        }

    }

}
