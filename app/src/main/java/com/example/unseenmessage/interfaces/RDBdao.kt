package com.example.unseenmessage.interfaces

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.unseenmessage.models.ChildTable
import com.example.unseenmessage.models.ParentTable

@Dao
interface RDBdao {


    //    Inertion ****************************************************************************************
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParent(parentTable: ParentTable)

    @Insert
    suspend fun insertChild(childTable: ChildTable)

    //    Inertion ****************************************************************************************


//    @Query(value = "Select * from ParentTable")
//    fun getAllParent(): List<ParentTable>


    //    selection ****************************************************************************************


    @Query(value = "Select * from ParentTable ORDER BY datetime DESC")
    fun getAllParents(): LiveData<List<ParentTable>>


    @Query("SELECT * from ChildTable where parentPhoneNumber =:phone_num")
    @Transaction
    suspend fun getAllChildByPhone(phone_num: String): List<ChildTable>

    @Query("SELECT * from ParentTable where pkg_name =:pkg_name")
    @Transaction
    fun getAllParentByPkg(pkg_name: String): LiveData<List<ParentTable>>

    //    Selection ****************************************************************************************


//    //    Update Db For Fav and UnFav
//    @Query("UPDATE FileModel SET fav=:fav WHERE filePath = :path")
//    suspend fun update(fav: String?, path: String)

    //    Deletion ****************************************************************************************

    @Query("DELETE  FROM ParentTable")
    fun deleteAllChatsParent(): Int

    @Query("DELETE  FROM ChildTable")
    fun deleteAllChatsChild(): Int

}