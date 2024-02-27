package com.rubayllo.actividad05.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rubayllo.actividad05.R
import com.rubayllo.actividad05.databinding.FragmentLoginBinding
import com.rubayllo.actividad05.ui.home.HomeActivity
import com.rubayllo.actividad05.ui.register.RegisterFragment
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerViewModel()
        isDataStoreRegisterUser()

        binding.btnLoginAcces.setOnClickListener {
            singInFirebaseEmailAndPassword()
        }

        binding.btnRegister.setOnClickListener {
            navigateToRegisterFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.restoreLoginUIState()
    }

    private fun observerViewModel() {
        lifecycleScope.launch {
            viewModel.loginUiState.collect { state ->
                if (state.wasLoginSuccessfully) {
                    navigateToHomeActivity()
                }
                if (state.errorMessage != null) {
                    Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
                }
                if (state.email != null) {
                    binding.etUserName.setText(state.email)
                }
                if (state.password != null) {
                    binding.etUserPassword.setText(state.password)
                }
            }
        }
    }

    private fun isDataStoreRegisterUser() {
        // Observar los flujos de datos
        viewModel.getAllDataUser(requireContext())
    }

    private fun singInFirebaseEmailAndPassword() {
        val email = binding.etUserName.text.toString().trim()
        val password = binding.etUserPassword.text.toString().trim()
        viewModel.singInFirebaseEmailAndPassword(requireContext(), email, password)
    }

    private fun navigateToHomeActivity() {
        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToRegisterFragment() {
        val registerFragment = RegisterFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fcv_login, registerFragment)
        transaction.setTransition(TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}