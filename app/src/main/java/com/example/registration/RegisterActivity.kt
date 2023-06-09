package com.example.registration

import android.content.Intent
import android.widget.Button
import android.widget.EditText
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.rememberImagePainter
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class RegisterActivity : AppCompatActivity() {
    lateinit var registerProfilePicture: SimpleDraweeView
    lateinit var registerUsername: EditText
    lateinit var registerPassword: EditText
    lateinit var registerFullName: EditText
    lateinit var registerEmailAddress: EditText
    lateinit var registerDOB: EditText
    lateinit var registerPhone: EditText
    lateinit var buttonSubmitRegister: Button
    lateinit var buttonBackToMain: Button

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var photoUri: Uri
    private var shouldShowCamera: MutableState<Boolean> = mutableStateOf(false)
    private var shouldShowPhoto: MutableState<Boolean> = mutableStateOf(false)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("cameraDebug", "Permission granted")
            shouldShowCamera.value = true
        } else {
            Log.d("cameraDebug", "Permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize Fresco before setting view.
        Fresco.initialize(this)

        setContentView(R.layout.activity_register)
        initializeComponents()
    }

    private fun initializeComponents() {
        bindingProfilePicture()
        registerUsername = findViewById<EditText>(R.id.registerUsername)
        registerPassword = findViewById<EditText>(R.id.registerPassword)
        registerFullName = findViewById<EditText>(R.id.registerFullName)
        registerEmailAddress = findViewById<EditText>(R.id.registerEmailAddress)
        registerDOB = findViewById<EditText>(R.id.registerDOB)
        registerPhone = findViewById<EditText>(R.id.registerPhone)
        buttonSubmitRegister = findViewById<Button>(R.id.buttonSubmitRegister)
        buttonBackToMain = findViewById<Button>(R.id.buttonBackToMain)

        registerProfilePicture.setOnClickListener {
            // Open Camera
            setContent {
                if (shouldShowCamera.value) {
                    CameraView(
                        outputDirectory = outputDirectory,
                        executor = cameraExecutor,
                        onImageCaptured = ::handleImageCapture,
                        onError = { Log.e("kilo", "View error:", it) }
                    )
                }

                if (shouldShowPhoto.value) {
                    Image(
                        painter = rememberImagePainter(photoUri),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            requestCameraPermission()
            outputDirectory = getOutputDirectory()
            cameraExecutor = Executors.newSingleThreadExecutor()
        }

        buttonBackToMain.setOnClickListener {
            // Navigate To Main
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        buttonSubmitRegister.setOnClickListener {
            // Navigate To Preview
            var intent = Intent(this, PreviewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun bindingProfilePicture() {
        registerProfilePicture = findViewById<SimpleDraweeView>(R.id.registerProfilePicture)
        val intent = intent
        val url:String? = intent.getStringExtra("registerProfilePicture")
        var imageUri: Uri
        if(url != null){
            Log.d("cameraDebug", url)
            val file = File(url)
            imageUri = Uri.fromFile(file)
        }
        else{
            imageUri = Uri.parse("https://t3.ftcdn.net/jpg/03/46/83/96/360_F_346839683_6nAPzbhpSkIpb8pmAwufkC7c5eD7wYws.jpg")
        }
        registerProfilePicture.setImageURI(imageUri)
    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                Log.i("cameraDebug", "Permission previously granted")
                shouldShowCamera.value = true
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.CAMERA
            ) -> Log.i("cameraDebug", "Show camera permissions dialog")

            else -> requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun handleImageCapture(uri: Uri) {
        Log.i("cameraDebug", "Image captured: $uri")
        shouldShowCamera.value = false

        photoUri = uri
        shouldShowPhoto.value = true
        registerProfilePicture.setImageURI(photoUri)
        cameraExecutor.shutdown()
        var intent:Intent = Intent(this,RegisterActivity::class.java)
        intent.putExtra("registerProfilePicture", photoUri.path)
        startActivity(intent)
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}