package com.emirpetek.emirpetek.havadurumu.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CRUDAnswer(@SerializedName("success")
                 @Expose
                 val success: Int,
                 @SerializedName("message")
                 @Expose
                 val message: String) {
}