import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.example.vibes.R
import com.example.vibes.activity.Musictype
import com.example.vibes.adapter.GridAdapter

class Chooseartist : AppCompatActivity() {

    lateinit var gridView: GridView
    lateinit var sharedPreferences: SharedPreferences
    private var numberNames = arrayOf("Atif Aslam", "KK", "Atif Aslam", "KK", "Atif Aslam",
        "KK", "Atif Aslam", "KK", "Atif Aslam", "KK","Atif Aslam", "KK", "Atif Aslam", "KK", "Atif Aslam",
        "KK", "Atif Aslam", "KK", "Atif Aslam", "KK")
    private var numberImages = intArrayOf(
        R.drawable.atif_aslam,R.drawable.kk,R.drawable.atif_aslam,R.drawable.kk,
        R.drawable.atif_aslam,R.drawable.kk,R.drawable.atif_aslam,R.drawable.kk,
        R.drawable.atif_aslam,R.drawable.kk,R.drawable.atif_aslam,R.drawable.kk,
        R.drawable.atif_aslam,R.drawable.kk,R.drawable.atif_aslam,R.drawable.kk,
        R.drawable.atif_aslam,R.drawable.kk,)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chooseartist)

        gridView = findViewById(R.id.gridView)
        sharedPreferences = getSharedPreferences("ArtistPrefs", Context.MODE_PRIVATE)

        val mainAdapter = GridAdapter(
            context = this,
            numbersInWords = numberNames,
            numberImage = numberImages
        )

        gridView.adapter = mainAdapter

        val btnartistnext = findViewById<Button>(R.id.btnartistnext)
        btnartistnext.setOnClickListener {
            val selectedItems = mainAdapter.getSelectedItems()

            if (selectedItems.isNotEmpty()) {
                // SharedPreferences me selected artists ko store karein
                val editor = sharedPreferences.edit()
                editor.putStringSet("selectedArtists", selectedItems)
                editor.apply()

                // Next activity me jaane ke liye
                val i = Intent(this, Musictype::class.java)
                startActivity(i)
            } else {
                Toast.makeText(this, "Kripya kam se kam ek artist select karein", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
