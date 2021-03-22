package com.akiniyalocts.imgurapiexample.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.akiniyalocts.imgurapiexample.BuildConfig
import com.akiniyalocts.imgurapiexample.R
import com.akiniyalocts.imgurapiexample.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.theartofdev.edmodo.cropper.CropImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import permissions.dispatcher.PermissionRequest
import permissions.dispatcher.ktx.constructPermissionsRequest

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()

    /**
     * A permission request for storage to access the users camera roll.
     * https://github.com/permissions-dispatcher/PermissionsDispatcher/tree/master/ktx
     */
    private var cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>(){
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity().setAspectRatio(16,9).getIntent(this@MainActivity)
        }
        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            viewModel = this@MainActivity.viewModel
            lifecycleOwner = this@MainActivity
        }
        setContentView(binding.root)
        var uploadImageButton=findViewById<Button>(R.id.uploadImageButton)
        cropActivityResultLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let {
                uploadImageButton.isEnabled=true
                viewModel.selectedImageUri(it)
            }
        }
    }
    fun clickCargaImagen(view:View){
        cropActivityResultLauncher.launch(null)
    }


    companion object{
        private const val REQUEST_IMAGE = 2021
    }
}