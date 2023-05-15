package com.thisisnotyours.mvvm_recyclerview.response

import com.google.gson.annotations.SerializedName

data class CarListResponse(
    val status: String,
    val data: List<CarData>,
    @SerializedName("message")
    val msg: String?
)

data class CarData(
    val mdn: String,
    val car_version: String,
    val car_vin: String,
    val car_type: Int,
    val car_num: String,
    val car_regnum: String,
    val company_name: String,
    val driver_id1: String,
    val driver_id2: String,
    val driver_id3: String,
    val car_dist: Int,
    val speed_factor: Int,
    val rpm_factor: Int,
    val fare_id: Int,
    val city_id: Int,
    val daemon_id: Int,
    val firmware_id: Int,
    val vfare_use: Boolean,
    val car_update: Boolean,
    val fare_update: Boolean,
    val city_update: Boolean,
    val daemon_update: Boolean,
    val firmware_update: Boolean,
    val reg_id: String,
    val reg_dtti: String,
    val update_id: String,
    val update_dtti: String,
    val unit_sn: String?,
    val tollId: Int,
    val logfile_upload: String,
    val fwVersion: String?,
    val fwRelease: String?,
    val daemonVersion: String?,
    val konaiMid: String?,
    val konaiTid: String?,
    val firstConnect: String?,
    val lastConnect: String?,
    val firmwareVersion: String,
    val firmwareRelease: String,
    val firmwareName: String,
    val cityName: String,
    val fareName: String,
    val fareVersion: String,
    val logfile_update: Boolean
)
