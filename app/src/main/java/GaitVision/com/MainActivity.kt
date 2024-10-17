package GaitVision.com

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private val REQUEST_IMAGE_PICK = 1 // Request code for image selection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Existing confirm button logic
        val confirmVidBtn = findViewById<Button>(R.id.confirm_vid_btn)
        confirmVidBtn.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // New Open Gallery button logic to pick an image
        val openGalBtn = findViewById<Button>(R.id.open_gal_btn)
        openGalBtn.setOnClickListener {
            // Intent to open the gallery for image selection
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*" // Set to pick images
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }
    }

    // Handle the selected image from the gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            // Now you can use the selected image URI
            selectedImageUri?.let {
                // Display the selected image in an ImageView (add ImageView to your layout)
                val imageView: ImageView = findViewById(R.id.imageView)
                imageView.setImageURI(it)
            }
        }
    }
}
