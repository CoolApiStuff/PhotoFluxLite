



package com.example.photofluxlite

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import com.example.photofluxlite.ui.theme.PhotoFluxLiteTheme
import com.google.ai.client.generativeai.GeminiNano
import com.google.ai.client.generativeai.GeminiNanoClient

class MainActivity : ComponentActivity() {
    private lateinit var imageView: ImageView
    private lateinit var uploadButton: Button
    private lateinit var brightnessButton: Button
    private lateinit var contrastButton: Button
    private lateinit var saturationButton: Button
    private lateinit var cropButton: Button
    private lateinit var aiButton: Button

    private val requestPermissionLauncher = registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            openGallery()
        } else {
            // Permission denied
        }
    }

    private val pickImageLauncher = registerForActivityResult(StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            imageView.setImageURI(selectedImageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        uploadButton = findViewById(R.id.uploadButton)
        brightnessButton = findViewById(R.id.brightnessButton)
        contrastButton = findViewById(R.id.contrastButton)
        saturationButton = findViewById(R.id.saturationButton)
        cropButton = findViewById(R.id.cropButton)
        aiButton = findViewById(R.id.aiButton)

        uploadButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                openGallery()
            }
        }

        brightnessButton.setOnClickListener {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val editedBitmap = Flux1Lite.adjustBrightness(bitmap, 20)
            imageView.setImageBitmap(editedBitmap)
        }

        contrastButton.setOnClickListener {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val editedBitmap = Flux1Lite.adjustContrast(bitmap, 1.2f)
            imageView.setImageBitmap(editedBitmap)
        }

        saturationButton.setOnClickListener {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val editedBitmap = Flux1Lite.adjustSaturation(bitmap, 1.2f)
            imageView.setImageBitmap(editedBitmap)
        }

        cropButton.setOnClickListener {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            val editedBitmap = Flux1Lite.crop(bitmap, 50, 50, bitmap.width - 50, bitmap.height - 50)
            imageView.setImageBitmap(editedBitmap)
        }

        aiButton.setOnClickListener {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            analyzeImageWithAI(bitmap)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    private fun analyzeImageWithAI(bitmap: Bitmap) {
        val client = GeminiNanoClient.Builder().build()
        val geminiNano = GeminiNano(client)
        geminiNano.analyzeImage(bitmap) { result ->
            // Handle the AI analysis result
            // For example, display suggestions or apply automatic edits
        }
    }
}



