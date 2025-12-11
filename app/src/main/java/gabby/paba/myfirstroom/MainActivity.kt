package gabby.paba.myfirstroom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import gabby.paba.myfirstroom.database.Note
import gabby.paba.myfirstroom.database.NoteRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var DB : NoteRoomDatabase
    private lateinit var adapterN : AdapterNote
    private var arNote : MutableList<Note> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        DB = NoteRoomDatabase.getDatabase(this)
        adapterN = AdapterNote(arNote)

        val _fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        _fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahData::class.java))
        }

        val _rvNotes = findViewById<RecyclerView>(R.id.rvNotes)

        _rvNotes.layoutManager = LinearLayoutManager(this)
        _rvNotes.adapter = adapterN

    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val note = DB.funnoteDao().selectAll()
            Log.d("data ROOM", note.toString())
            adapterN.isiData(note)

            adapterN.setOnItemClickCallback(
                object : AdapterNote.OnItemClickCallback {
                    override fun delData(dtNote: Note) {
                        CoroutineScope(Dispatchers.IO).async {
                            DB.funnoteDao().delete(dtNote)
                            val note = DB.funnoteDao().selectAll()
                            Log.d("data ROOM2", note.toString())

                            withContext(Dispatchers.Main){
                                adapterN.isiData(note)
                            }
                        }
                    }
                }
            )
        }
    }
}