package com.lay.fastipc.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * author: qinlei
 * create by: 2023/1/24 08:47
 * description:
 */

data class Request(
    val type: Int,
    val serviceId: String?,
    val methodName: String?,
    val params: Array<Parameters?>?
) : Serializable, Parcelable {
    //=======================================

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArray(Parameters.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
        parcel.writeString(serviceId)
        parcel.writeString(methodName)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Request

        if (type != other.type) return false
        if (serviceId != other.serviceId) return false
        if (methodName != other.methodName) return false
        if (params != null) {
            if (other.params == null) return false
            if (!params.contentEquals(other.params)) return false
        } else if (other.params != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + (serviceId?.hashCode() ?: 0)
        result = 31 * result + (methodName?.hashCode() ?: 0)
        result = 31 * result + (params?.contentHashCode() ?: 0)
        return result
    }

    companion object CREATOR : Parcelable.Creator<Request> {
        override fun createFromParcel(parcel: Parcel): Request {
            return Request(parcel)
        }

        override fun newArray(size: Int): Array<Request?> {
            return arrayOfNulls(size)
        }
    }
}