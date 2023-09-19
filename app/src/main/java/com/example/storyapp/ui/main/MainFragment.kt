package com.example.storyapp.ui.main

import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentMainBinding
import com.example.storyapp.model.LoginPref
import com.example.storyapp.repo.ViewModelFactory

class MainFragment : Fragment() {
    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar
            .setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                    logout()
                    true
                }
                else -> false
            }
        }

        navigateToLogin()
        addStory()

    }


    private fun addStory() {
        binding.btnUpload.setOnClickListener {
            checkPermissionLauncher()
            val action = MainFragmentDirections.actionMainFragmentToStoryFragment()
            findNavController().navigate(action)
        }

    }


    private fun navigateToLogin() {
        val prefLogin = LoginPref(requireContext())
        val token = prefLogin.getToken()
        if (token.isNullOrEmpty()) {
            val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
            findNavController().navigate(action)
        }
    }

    private fun logout() {
        val prefLogin = LoginPref(requireContext())
        prefLogin.clearToken()
        val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun checkPermissionLauncher() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.CAMERA
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            permissionResultCallback.launch(android.Manifest.permission.CAMERA)
        } else {
            Log.d("Permission", "isGranted")
        }
    }

    private val permissionResultCallback = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        when (it) {
            true -> {
                Toast.makeText(
                    requireContext(),
                    "Permission has been granted by user",
                    Toast.LENGTH_SHORT
                ).show()
            }
            false -> {
                Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}