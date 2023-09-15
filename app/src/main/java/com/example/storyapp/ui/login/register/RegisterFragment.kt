package com.example.storyapp.ui.login.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.storyapp.databinding.FragmentRegisterBinding
import com.example.storyapp.repo.ViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel : RegisterViewModel by viewModels {
            factory
        }

        viewModel.registerUser(name, email, password)
        showLoading(true)
        viewModel.responseRegister.observe(viewLifecycleOwner) {
            if(it) {
                Toast.makeText(requireContext(), "Register Success", Toast.LENGTH_SHORT).show()
                showLoading(false)
                navigateToLogin()
            } else {
                showLoading(false)
                Toast.makeText(requireContext(), "Register Invalid", Toast.LENGTH_SHORT).show()
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
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}