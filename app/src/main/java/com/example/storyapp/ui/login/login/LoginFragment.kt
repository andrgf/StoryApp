package com.example.storyapp.ui.login.login

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.storyapp.data.remote.auth.Login
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.util.LoginPref
import com.example.storyapp.networks.Result
import com.example.storyapp.repo.ViewModelFactory

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAnimation()
        showLoading(false)
        binding.btnLogin.isEnabled = false
        binding.apply {
            email.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    showButton()
                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            password.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    showButton()
                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            btnLogin.setOnClickListener {
                login()
            }

            tvRegister.setOnClickListener {
                navigateToRegister()
            }
        }
        onBackPressed()

    }

    private fun navigateToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun login() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel: LoginViewModel by viewModels {
            factory
        }

        viewModel.loginUser(email, password).observe(requireActivity()) {
            if (it != null) {
                when(it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        saveToken(it.data.loginResult)
                        showLoading(false)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showDialogError()
                        Log.d("Login", "${it.error}")
                    }
                }
            }
        }
    }

    private fun showDialogError() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Login Error!")
        builder.setPositiveButton(android.R.string.ok) { _, _ ->
        }
        builder.show()
    }

    fun showButton() {
        binding.apply {
            btnLogin.isEnabled =
                password.text.toString().isNotEmpty() && validPassword(password.text.toString()) &&
                        email.text.toString().isNotEmpty() && validEmail(email.text.toString())

        }
    }

    private fun validEmail(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    private fun validPassword(value: String): Boolean {
        return value.length >= 8
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
    }

    private fun saveToken(login: Login) {
        Log.d("Token", "Token Save")
        val prefLogin = LoginPref(requireContext())
        prefLogin.setToken(login.token)
        navigateToMain()
    }

    private fun navigateToMain() {
        val action = LoginFragmentDirections.actionLoginFragmentToMainFragment3()
        NavHostFragment.findNavController(this).navigate(action)
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }

            }
        )
    }

    private fun showAnimation() {
        ObjectAnimator.ofFloat(binding.tvApp, View.TRANSLATION_X, -40f, 40f).apply {
            duration = 4000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}