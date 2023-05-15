package com.thisisnotyours.mvvm_recyclerview.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonObject
import com.thisisnotyours.mvvm_recyclerview.R
import com.thisisnotyours.mvvm_recyclerview.adapter.CarAdapter
import com.thisisnotyours.mvvm_recyclerview.databinding.ActivityMainBinding
import com.thisisnotyours.mvvm_recyclerview.repository.MainRepository
import com.thisisnotyours.mvvm_recyclerview.response.Resource
import com.thisisnotyours.mvvm_recyclerview.viewModel.MainViewModel
import com.thisisnotyours.mvvm_recyclerview.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    val mainRepository = MainRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.titleView.text = "MVVM Pattern Coroutine Test"

        val viewModelFactory = MainViewModelFactory(mainRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        
        val token = intent.getStringExtra("token")

        //edit: LiveData의 새로운 값이 EditText에 입력된 값과 다르면 EditText를 업데이트
        viewModel.getCarType().observe(this, Observer { s ->
            val etCarTypeValue = binding.etInputCarType.text.toString().toIntOrNull() ?: 0  //edit: EditText에서 가져온 값을 문자열에서 정수로 안전하게 변환하려면 toIntOrNull() 함수사용. 기본값을 0으로 설정
            if (s != etCarTypeValue) {
                val cursorPosition = binding.etInputCarType.selectionStart
                binding.etInputCarType.setText(s)
                binding.etInputCarType.setSelection(cursorPosition)
            }
        })

        viewModel.getCarNum().observe(this, Observer { s ->
            if (s != binding.etInputCarNum.text.toString()) {
                val cursorPosition = binding.etInputCarNum.selectionStart
                binding.etInputCarNum.setText(s)
                binding.etInputCarNum.setSelection(cursorPosition)
            }
        })

        viewModel.getMdn().observe(this, Observer { s ->
            if (s != binding.etInputMdn.text.toString()) {
                val cursorPosition = binding.etInputMdn.selectionStart
                binding.etInputMdn.setText(s)
                binding.etInputMdn.setSelection(cursorPosition)
            }
        })

        //edit: TextWatcher를 사용하여 EditText의 값이 변경될 때마다 호출되는 콜백을 설정하여 '새로운 값 업데이트'
        binding.etInputCarType.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                val newInput = editable.toString().toString().toIntOrNull() ?: 0
                if (newInput != viewModel.getCarType().value) {
                    viewModel.setCarType(newInput)
                }
            }
        })

        binding.etInputCarNum.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val newInput = p0.toString()
                if (newInput != viewModel.getCarNum().value) {
                    viewModel.setCarNum(newInput)
                }
            }
        })

        binding.etInputMdn.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                val newInput = p0.toString()
                if (newInput != viewModel.getMdn().value) {
                    viewModel.setMdn(newInput)
                }
            }
        })

        binding.searchBtn.setOnClickListener {
            if (token != null) {
                val paging = JsonObject().apply {
                    addProperty("limit", 10)
                    addProperty("offset", 1)
                }
                viewModel.fetchCarListData(token, paging)

                //데이터가 있으면 리사이클러뷰 set
                viewModel.carListLiveData.observe(this, Observer { resource ->
                    when (resource) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            binding.recycler.layoutManager = LinearLayoutManager(this)
                            binding.recycler.adapter = CarAdapter(resource.data ?: emptyList())
                        }
                        is Resource.Error -> {
                            Toast.makeText(this, "Err: ${resource.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                        is Resource.Retry -> {
                            Toast.makeText(this, "데이터를 가져오지 못했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }
        }

        viewModel.toastMsgLiveData.observe(this, Observer { toastMsg ->
            Toast.makeText(this, toastMsg, Toast.LENGTH_SHORT).show()
            when (toastMsg) {
               "데이터 없음" -> {
                   //UI 표출
                }
            }
        })
    }
}