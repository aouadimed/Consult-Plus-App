package com.example.consultplus.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Booking(@SerializedName("_id") @Expose val ID : String
, @SerializedName("date") @Expose val date: String
, @SerializedName("time") @Expose val time: String
, @SerializedName("statu") @Expose val status: Int
, @SerializedName("doctor") @Expose val doctorId: String
, @SerializedName("patient") @Expose val patient:  Fullnames
)

data class Fullnames(@SerializedName("_id") @Expose val ID : String
, @SerializedName("firstname") @Expose val firstname: String
, @SerializedName("lastname") @Expose val lastname: String
)

data class PatientBooking(@SerializedName("_id") @Expose val ID : String
                   , @SerializedName("date") @Expose val date: String
                   , @SerializedName("time") @Expose val time: String
                   , @SerializedName("statu") @Expose val status: Int
                   , @SerializedName("doctor") @Expose val doctor:  Fullnames
                   , @SerializedName("patient") @Expose val patient: String
)