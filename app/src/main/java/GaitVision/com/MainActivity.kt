package GaitVision.com

import GaitVision.com.databinding.ActivityMainBinding
import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.activity.result.ActivityResultLauncher

class MainActivity : ComponentActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private var videoUri: Uri?=null
    private val REQUESTCODE_CAMERA=1
    private val REQUESTCODE_GALLERY=2

    private val getResult: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                videoUri = it

                val intent= Intent(this, SecondActivity::class.java).apply {
                    putExtra("VIDEO_URI", videoUri.toString())
                }
                startActivity(intent)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        //mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mBinding.confirmVidBtn.setOnClickListener{startActivity(Intent(this,SecondActivity::class.java))}
        mBinding.openGalBtn.setOnClickListener{startIntentFromGallary()}
        mBinding.cameraBtn.setOnClickListener{startActivity(Intent(this,FourthActivity::class.java ))}
    }

    private fun initClicks()
    {
        mBinding.openGalBtn.setOnClickListener{
            startIntentFromGallary()
        }
    }

    private fun startIntentFromGallary() {
       getResult.launch("video/*")
    }


}