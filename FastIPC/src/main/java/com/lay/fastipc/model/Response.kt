package com.lay.fastipc.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 * author: qinlei
 * create by: 2023/1/24 09:06
 * description:
 */
data class Response(
    val value:String?,
    val result:Int
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(value)
        parcel.writeInt(result)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Response> {
        override fun createFromParcel(parcel: Parcel): Response {
            return Response(parcel)
        }

        override fun newArray(size: Int): Array<Response?> {
            return arrayOfNulls(size)
        }
    }

}