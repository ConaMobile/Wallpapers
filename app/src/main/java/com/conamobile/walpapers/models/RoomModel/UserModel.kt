package com.conamobile.walpapers.models.RoomModel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserModel {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "smallLink")
    var smallLink: String? = null

    @ColumnInfo(name = "bigLink")
    var bigLink: String? = null

    @ColumnInfo(name = "date")
    var date: String? = null
}