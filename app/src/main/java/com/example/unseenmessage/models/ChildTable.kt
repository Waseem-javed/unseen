package com.example.unseenmessage.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = ParentTable::class,
        parentColumns = ["phone_number"],
        childColumns = ["parentPhoneNumber"]
    )]
)
data class ChildTable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true) var SmsID: Int,
    @ColumnInfo(name = "parentPhoneNumber", index = true) val parentPhoneNumber: String,
    @ColumnInfo(name = "user_name", index = true) val user_name: String,
    @ColumnInfo(name = "date", index = true) val date: String,
    @ColumnInfo(name = "time", index = true) val time: String,
    @ColumnInfo(name = "datetime", index = true) val datetime: String,
    @ColumnInfo(name = "pkg_name", index = true) val pkg_name: String,
    @ColumnInfo(name = "user_sms", index = true) val user_sms: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
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
        parcel.writeInt(SmsID)
        parcel.writeString(parentPhoneNumber)
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

    companion object CREATOR : Parcelable.Creator<ChildTable> {
        override fun createFromParcel(parcel: Parcel): ChildTable {
            return ChildTable(parcel)
        }

        override fun newArray(size: Int): Array<ChildTable?> {
            return arrayOfNulls(size)
        }
    }
}
