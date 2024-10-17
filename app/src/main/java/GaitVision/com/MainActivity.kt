package GaitVision.com

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val REQUEST_VIDEO_PICK = 1 // Request code for video selection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Existing confirm button logic
        val confirmVidBtn = findViewById<Button>(R.id.confirm_vid_btn)
        confirmVidBtn.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // New Open Gallery button logic
        val openGalBtn = findViewById<Button>(R.id.open_gal_btn)
        openGalBtn.setOnClickListener {
            // Intent to open the gallery for video selection
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            intent.type = "video/*"
            startActivityForResult(intent, REQUEST_VIDEO_PICK)
        }
    }

    // Handle the selected video from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_PICK && resultCode == RESULT_OK && data != null) {
            val selectedVideoUri: Uri? = data.data
            // You can now use the selected video URI
            selectedVideoUri?.let {
                // You can start SecondActivity and pass the video URI if needed
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("VIDEO_URI", it.toString())
                startActivity(intent)
            }
        }
    }
}
