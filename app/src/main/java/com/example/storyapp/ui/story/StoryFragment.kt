package com.example.storyapp.ui.story

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storyapp.BuildConfig
import com.example.storyapp.databinding.FragmentStoryBinding
import com.example.storyapp.model.LoginPref
import com.example.storyapp.model.Result
import com.example.storyapp.repo.ViewModelFactory
import com.example.storyapp.util.handleSamplingAndRotationBitmap
import com.example.storyapp.util.reduceFileImage
import com.example.storyapp.util.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException

class StoryFragment : Fragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private val storyViewModel : StoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoading(false)
        storyGallery()
        storyCamera()
        upload()

    }

    private fun upload() {
        binding.btnUpload.setOnClickListener {
            if (getFile != null) {
                showLoading(true)
                val file = reduceFileImage(getFile as File)
                val descriptionText = binding.etDescription.text
                if (!descriptionText.isNullOrEmpty()) {
                    val description = descriptionText.toString().toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )

                    val prefLogin = LoginPref(requireContext())
                    var token = prefLogin.getToken().toString()
                    token = "Bearer $token"
                    storyViewModel.uploadStory(token, imageMultipart, description).observe(viewLifecycleOwner) {
                        if (it != null) {
                            when (it) {
                                is Result.Success -> {
                                    showLoading(false)
                                    Toast.makeText(requireContext(), "Upload Success", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(StoryFragmentDirections.actionStoryFragmentToMainFragment())
                                }
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Error -> {
                                    showLoading(false)
                                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                } else {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Description not empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                showLoading(false)
                Toast.makeText(requireContext(), "Image not empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private lateinit var currentPhoto: String
    private fun storyCamera() {
        binding.btnCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(requireActivity().packageManager)

            File.createTempFile("temp_", ".jpg", requireContext().applicationContext.cacheDir).also {
                val photoUri: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.storyapp",
                    it
                )
                currentPhoto = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhoto)
            getFile = myFile

            try {
                val bitmap = handleSamplingAndRotationBitmap(requireContext(), Uri.fromFile(myFile))
                binding.imageView.setImageBitmap(bitmap)
                Log.d("storyapp", "camera inserted into iv")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun storyGallery() {
        binding.btnGallery.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherGallery.launch(chooser)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val selected: Uri = it.data?.data as Uri
            val myFile = uriToFile(selected, requireContext())
            getFile = myFile
            binding.imageView.setImageURI(selected)
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar2.isVisible = state
    }

}