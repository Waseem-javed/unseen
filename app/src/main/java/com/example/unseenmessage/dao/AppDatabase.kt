package com.example.unseenmessage.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.unseenmessage.interfaces.RDBdao
import com.example.unseenmessage.models.ChildTable
import com.example.unseenmessage.models.ParentTable

@Database(entities = [ParentTable::class, ChildTable::class], version = 3, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun RdbDao(): RDBdao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase {
            val instance = INSTANCE

            if (instance != null) {
                return instance
            }

            synchronized(this) {

                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "ChatDb"
                )
                    .build()

                INSTANCE = instance
                return instance

            }

        }
    }


}