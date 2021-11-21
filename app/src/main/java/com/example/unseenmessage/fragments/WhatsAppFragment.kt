package com.example.unseenmessage.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unseenmessage.R
import com.example.unseenmessage.adapters.AllChatAdapter
import com.example.unseenmessage.interfaces.CallBack
import com.example.unseenmessage.models.ParentTable
import com.example.unseenmessage.viewModels.WhatsAppFragVM
import kotlinx.android.synthetic.main.fragment_whats_app.view.*


class WhatsAppFragment : Fragment(), CallBack {


    lateinit var viewModel: WhatsAppFragVM

    private var allChatAdapter: AllChatAdapter? = null

    private var allChatList: List<ParentTable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vieww: View = inflater.inflate(R.layout.fragment_whats_app, container, false)

        allChatList = ArrayList()

        viewModel = ViewModelProvider(this).get(WhatsAppFragVM::class.java)


        val linearLayoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        vieww.recyclerView.layoutManager = linearLayoutManager
        allChatAdapter = AllChatAdapter(this)
        vieww.recyclerView.adapter = allChatAdapter

        // Observe the model


        viewModel.allChats.observe(viewLifecycleOwner, Observer { parentTable ->


            allChatList = parentTable
            Log.d("OBSERVING_DATA", "onViewCreated: ${parentTable.size}")

            allChatAdapter!!.setValues(parentTable)


        })

        return vieww
    }


    override fun click(position: Int) {


    }
}