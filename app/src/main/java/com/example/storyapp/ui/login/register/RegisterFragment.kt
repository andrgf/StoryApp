package com.example.storyapp.ui.login.register

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storyapp.databinding.FragmentRegisterBinding
import com.example.storyapp.networks.Result
import com.example.storyapp.repo.ViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showAnimation()
        showLoading(false)
        binding.btnRegister.isEnabled = false
        binding.apply {
            name.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    showButton()
                }
                override fun afterTextChanged(p0: Editable?) {}
            })

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

            btnRegister.setOnClickListener {
                register()
            }

            tvLogin.setOnClickListener {
                navigateToLogin()
            }
        }
    }

    private fun navigateToLogin() {
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun register() {

        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        viewModel.registerUser(name, email, password).observe(requireActivity()) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        Log.d("Register", "Register Success")
                        navigateToLogin()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(requireContext(), "Register Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun showButton() {
        binding.apply {
            btnRegister.isEnabled =
                name.text.toString().isNotEmpty() &&
                        password.text.toString().isNotEmpty() && validPassword(password.text.toString()) &&
                        email.text.toString().isNotEmpty() && validEmail(email.text.toString())

        }
    }

    private fun validEmail(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

    private fun validPassword(value: String) : Boolean {
        return value.length >= 8
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
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