package com.example.unseenmessage.repos

import androidx.lifecycle.LiveData
import com.example.unseenmessage.interfaces.RDBdao
import com.example.unseenmessage.models.ChildTable
import com.example.unseenmessage.models.ParentTable

class ChatRepo(private val rdBdao: RDBdao) {

    suspend fun addParentDao(parentTable: ParentTable) {

        rdBdao.insertParent(parentTable)
    }

    suspend fun addChildDao(childTable: ChildTable) {

        rdBdao.insertChild(childTable)
    }

    val readAllParentChats: LiveData<List<ParentTable>> = rdBdao.getAllParents()

    fun readAllByPAKAGE(pkg: String): LiveData<List<ParentTable>> {
        val readAllByPkgChats: LiveData<List<ParentTable>> = rdBdao.getAllParentByPkg(pkg)

        return readAllByPkgChats
    }

}