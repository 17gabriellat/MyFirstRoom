package gabby.paba.myfirstroom

import android.os.Bundle
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
    }
}