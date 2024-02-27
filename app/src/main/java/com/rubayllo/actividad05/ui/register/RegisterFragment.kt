package com.rubayllo.actividad05.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.rubayllo.actividad05.R
import com.rubayllo.actividad05.databinding.FragmentRegisterBinding
import com.rubayllo.actividad05.ui.login.LoginFragment
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    private lateinit var _binding: FragmentRegisterBinding
    private val binding: FragmentRegisterBinding get() = _binding

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observerViewModel()

        binding.btnRegisterUser.setOnClickListener {
            createFirebaseMailAndPasswordUser()
        }

    }

    private fun observerViewModel() {
        lifecycleScope.launch {
            viewModel.registerUIState.collect { state ->
                if (state.wasRegisterSuccessfully) {
                    Toast.makeText(
                        requireContext(),
                        "Usuario creado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToLoginFragment()
                }
                if (state.errorMessage != null) {
                    Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun createFirebaseMailAndPasswordUser() {
        val email = binding.etRegisterUserName.text.toString().trim()
        val password = binding.etRegisterUserPassword.text.toString()
        viewModel.createFirebaseMailAndPasswordUser(requireContext(), email, password)
    }

    private fun navigateToLoginFragment() {
        val loginFragment = LoginFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fcv_login, loginFragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}