package com.lay.fastipc.model

import android.os.Parcel
import android.os.Parcelable

/**
 * author: qinlei
 * create by: 2023/1/24 09:00
 * description:
 */
data class Parameters(
    val className: String?,//javabean类名
    val value: String?//将javabean转成json
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(className)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Parameters> {
        override fun createFromParcel(parcel: Parcel): Parameters {
            return Parameters(parcel)
        }

        override fun newArray(size: Int): Array<Parameters?> {
            return arrayOfNulls(size)
        }
    }
}