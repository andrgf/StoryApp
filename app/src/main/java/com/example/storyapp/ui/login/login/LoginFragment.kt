package com.example.storyapp.ui.login.login

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
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.storyapp.data.remote.auth.Login
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.model.LoginPref
import com.example.storyapp.model.Result
import com.example.storyapp.repo.ViewModelFactory

class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
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

    }

    private fun navigateToRegister() {
        binding.tvRegister.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun login() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val factory : ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel : LoginViewModel by viewModels {
            factory
        }

        viewModel.loginUser(email, password)
        showLoading(true)
        viewModel.responseLogin.observe(viewLifecycleOwner) {login ->
            when(login) {
                is Result.Success -> {
                    val loginResult = login.data
                    saveToken(loginResult)
                    showLoading(false)
                    Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    showDialogError()
                    showLoading(false)
                    Log.d("Login", "Error")
                }
                is Result.Loading -> {
                    showLoading(true)
                    Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    Log.d("Login", "Loading")
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

    private fun validPassword(value: String) : Boolean {
        return value.length >= 8
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

}