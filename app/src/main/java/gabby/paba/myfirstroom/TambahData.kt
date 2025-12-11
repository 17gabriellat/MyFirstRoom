package gabby.paba.myfirstroom

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import gabby.paba.myfirstroom.database.Note
import gabby.paba.myfirstroom.database.NoteRoomDatabase
import gabby.paba.myfirstroom.helper.DateHelper.getCurrentDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class TambahData : AppCompatActivity() {
    private lateinit var _etJudul: EditText
    private lateinit var _etDeskripsi: EditText
    private lateinit var _btnTambah: Button
    private lateinit var _btnUpdate: Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tambah_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val DB : NoteRoomDatabase = NoteRoomDatabase.getDatabase(this)

        var tanggal : String = getCurrentDate()

        _etJudul = findViewById(R.id.etJudul)
        _etDeskripsi = findViewById(R.id.etDeskripsi)

        var judul = _etJudul.text.toString()
        var deskripsi = _etDeskripsi.text.toString()

        _btnTambah.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.funnoteDao().insert(
                    Note(
                        0,
                        judul,
                        deskripsi,
                        tanggal
                    )
                )
            }
        }

        var iID : Int = 0
        var iAddEdit : Int = 0

        iID = intent.getIntExtra("noteId", 0)
        iAddEdit = intent.getIntExtra("addEdit", 0)

        if(iAddEdit == 0){
            _btnTambah.visibility = View.VISIBLE
            _btnUpdate.visibility = View.GONE
            _etJudul.isEnabled = true
        }else{
            _btnTambah.visibility = View.GONE
            _btnUpdate.visibility = View.VISIBLE
            _etJudul.isEnabled = false

            CoroutineScope(Dispatchers.IO).async {
                val noteItem = DB.funnoteDao().getNote(iID)
                _etJudul.setText(noteItem.judul)
                _etDeskripsi.setText(noteItem.deskripsi)
            }
        }

        _btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).async {
                DB.funnoteDao().update(
                    _etJudul.text.toString(),
                    _etDeskripsi.text.toString(),
                    iID
                )
                finish()
            }
        }
    }
}