package com.thisisnotyours.mvvm_recyclerview.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.thisisnotyours.mvvm_recyclerview.R
import com.thisisnotyours.mvvm_recyclerview.databinding.ActivityLoginBinding
import com.thisisnotyours.mvvm_recyclerview.repository.LoginRepository
import com.thisisnotyours.mvvm_recyclerview.viewModel.LoginViewModel
import com.thisisnotyours.mvvm_recyclerview.viewModel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val TAG = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    val loginRepository = LoginRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.titleView.text = "MVVM Pattern Coroutine Test"

        val viewModelFactory = LoginViewModelFactory(loginRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)

        viewModel.getId().observe(this, Observer { s ->
            if (s != binding.etId.text.toString()) {
                val cursorPosition = binding.etId.selectionStart
                binding.etId.setText(s)
                binding.etId.setSelection(cursorPosition)
            }
        })

        viewModel.getPassword().observe(this, Observer { s ->
            if (s != binding.etPassword.text.toString()) {
                val cursorPosition = binding.etPassword.selectionStart
                binding.etPassword.setText(s)
                binding.etPassword.setSelection(cursorPosition)
            }
        })

        binding.etId.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val newId = editable.toString()
                if (newId != viewModel.getId().value) {
                    viewModel.setId(newId)
                }
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val newPassword = editable.toString()
                if (newPassword != viewModel.getPassword().value) {
                    viewModel.setPassword(newPassword)
                }
            }
        })

        binding.loginBtn.setOnClickListener {
            viewModel.performLogin(binding.etId.text.toString(), binding.etPassword.text.toString())
        }

        viewModel.loginSuccessLiveData.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                val getToken = viewModel.tokenLiveData.value
                if (!getToken.isNullOrEmpty()) {
                    Log.d(TAG+"getToken", getToken)
                    val i = Intent(this, MainActivity::class.java)
                    i.putExtra("token", getToken)
                    startActivity(i)
                }
            }
        })

        viewModel.toastLiveData.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }
}