package GaitVision.com

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.VideoView
import android.widget.MediaController
import androidx.activity.ComponentActivity

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val uploadCSVBtn = findViewById<Button>(R.id.upload_csv_btn)
        uploadCSVBtn.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        val videoView = findViewById<VideoView>(R.id.video_viewer)
        val packageName = "android.resource://" + packageName + "/" + R.raw.test_video
        val uri = Uri.parse(packageName)
        videoView.setVideoURI(uri)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)

    }
}