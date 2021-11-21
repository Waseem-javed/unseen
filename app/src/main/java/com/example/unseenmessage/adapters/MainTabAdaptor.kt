package com.example.unseenmessage.adapters

import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.unseenmessage.R
import com.example.unseenmessage.activities.MainActivity
import com.example.unseenmessage.models.MainModel
import java.lang.String
import java.util.*

class MainTabAdaptor(
    var supportFragmentManager: FragmentManager,
    var context: MainActivity,
    var size: Int,
    var mainModelList: ArrayList<MainModel>
) : FragmentPagerAdapter(supportFragmentManager) {

    override fun getCount(): Int {
        return size
    }

    override fun getItem(position: Int): Fragment {
        val mainModel: MainModel = mainModelList.get(position)
        return mainModel.frag
    }


    fun getTabView(position: Int): View? {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        val v: View = LayoutInflater.from(context).inflate(R.layout.custom_tab, null)
        val mainModel: MainModel = mainModelList.get(position)
        val tv = v.findViewById<View>(R.id.item_name) as TextView
        val textView = v.findViewById<TextView>(R.id.item_count)
        if (mainModel.count !== 0) {
            textView.visibility = View.VISIBLE
            textView.setText(String.valueOf(mainModel.count))
        } else {
            textView.visibility = View.GONE
        }
        tv.setText(mainModel.label)
        val img = v.findViewById<View>(R.id.item_icon) as ImageView
        if (mainModel.tab_name.equals("All")) {
            img.visibility = View.INVISIBLE
        } else if (mainModel.tab_name.equals("WHATSAPP")) {
//            img.setImageResource(R.drawable.harvest)
        } else if (mainModel.tab_name.equals("MESSENGER")) {
//            img.setImageResource(R.drawable.drink)
        } else if (mainModel.tab_name.equals("INSTAGRAM")) {
//            img.setImageResource(R.drawable.milkshake)
        }
        if (position != 0) {
            img.imageAlpha = 0x3F
            tv.setTextColor(context.getResources().getColor(R.color.gray))
        }
        return v
    }

}