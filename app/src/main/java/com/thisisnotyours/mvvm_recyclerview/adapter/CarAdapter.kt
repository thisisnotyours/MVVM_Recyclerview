package com.thisisnotyours.mvvm_recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thisisnotyours.mvvm_recyclerview.data.Car
import com.thisisnotyours.mvvm_recyclerview.databinding.RecyclerCarItemBinding

class CarAdapter(private var carList: List<Car> = listOf()) : RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(val binding: RecyclerCarItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerCarItemBinding.inflate(inflater, parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.binding.index.text = (position+1).toString()
        holder.binding.carNum.text = car.carNum
        holder.binding.carRegnum.text = car.carRegnum
        holder.binding.mdn.text = car.mdn
        holder.binding.companyName.text = car.companyName
    }

    override fun getItemCount(): Int = carList.size

    //edit:  for 리사이클러뷰 업데이트
    fun updateCarList(newList: List<Car>) {  //이 함수는 기존의 리스트를 비우고 새로운 리스트를 설정하는 역할을 함
        carList = newList.toMutableList()  //newList의 아이템들로 새로운 MutableList를 생성. (기존의 아이템을 clear 함)
        notifyDataSetChanged()  //어댑터에게 데이터셋이 변경되었음을 알려줌
    }
}