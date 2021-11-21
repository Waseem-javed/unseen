package com.example.unseenmessage.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ParentTable(

    @PrimaryKey()
    @ColumnInfo(name = "phone_number", index = true) val phone_number: String,
    @ColumnInfo(name = "user_name", index = true) val user_name: String,
    @ColumnInfo(name = "date", index = true) val date: String,
    @ColumnInfo(name = "time", index = true) val time: String,
    @ColumnInfo(name = "datetime", index = true) val datetime: String,
    @ColumnInfo(name = "pkg_name", index = true) val pkg_name: String,
    @ColumnInfo(name = "user_sms", index = true) val user_sms: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(phone_number)
        parcel.writeString(user_name)
        parcel.writeString(date)
        parcel.writeString(time)
        parcel.writeString(datetime)
        parcel.writeString(pkg_name)
        parcel.writeString(user_sms)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParentTable> {
        override fun createFromParcel(parcel: Parcel): ParentTable {
            return ParentTable(parcel)
        }

        override fun newArray(size: Int): Array<ParentTable?> {
            return arrayOfNulls(size)
        }
    }
}
