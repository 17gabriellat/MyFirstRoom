package gabby.paba.myfirstroom

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gabby.paba.myfirstroom.database.Note

class AdapterNote(private val listNotes: MutableList<Note>) : RecyclerView.Adapter<AdapterNote.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback{
        fun delData(dtNote: Note)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun isiData(list : List<Note>){
        listNotes.clear()
        listNotes.addAll(list)
        notifyDataSetChanged()
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvJudul: TextView = itemView.findViewById(R.id.tvJudul)
        var _tvTanggal: TextView = itemView.findViewById(R.id.tvTanggal)
        var _tvDeskripsi: TextView = itemView.findViewById(R.id.tvDeskripsi)
        var _btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        var _btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterNote.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var note : Note = listNotes[position]

        holder._tvJudul.setText(note.judul)
        holder._tvTanggal.setText(note.tanggal)
        holder._tvDeskripsi.setText(note.deskripsi)

        holder._btnEdit.setOnClickListener {
            val intent = Intent(it.context, TambahData::class.java)
            intent.putExtra("noteId", note.id)
            intent.putExtra("addEdit", 1)
            it.context.startActivity(intent)
        }

        holder._btnDelete.setOnClickListener {
            onItemClickCallback.delData(note)
        }
    }
}
