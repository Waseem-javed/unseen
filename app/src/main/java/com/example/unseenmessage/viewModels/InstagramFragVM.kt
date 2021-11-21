package com.example.unseenmessage.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.unseenmessage.UtilsKeys
import com.example.unseenmessage.dao.AppDatabase
import com.example.unseenmessage.models.ParentTable
import com.example.unseenmessage.repos.ChatRepo

class InstagramFragVM(application: Application) : AndroidViewModel(application) {


    private val chatRepo: ChatRepo
    val allChats: LiveData<List<ParentTable>>

    init {

        val dao = AppDatabase.getInstance(application).RdbDao()

        chatRepo = ChatRepo(dao)

        allChats = chatRepo.readAllByPAKAGE("${UtilsKeys.InstagramKey}")

    }

}