package com.example.unseenmessage.activities

import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.unseenmessage.R
import com.example.unseenmessage.adapters.MainTabAdaptor
import com.example.unseenmessage.dao.AppDatabase
import com.example.unseenmessage.databinding.ActivityMainBinding
import com.example.unseenmessage.fragments.AllChatFragment
import com.example.unseenmessage.fragments.FacebookFragment
import com.example.unseenmessage.fragments.InstaFragment
import com.example.unseenmessage.fragments.WhatsAppFragment
import com.example.unseenmessage.models.MainModel
import com.example.unseenmessage.services.SmsService
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cus_del_dialog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    private lateinit var finalAdapterData: ArrayList<MainModel>

    override fun onResume() {
        super.onResume()


//        Allow Permission of Notification *****************************************************

        allowNotificationPermission()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


//        SetUpNavigation *************************************

        navigationClick()

        binding.navButton.setOnClickListener(View.OnClickListener {
            binding.drawer.openDrawer(
                Gravity.LEFT
            )
        })


//        ****************************************************************

//        Set Up Tab View And View Pager **************************************

        setUpTabWithViewPager()

//        ****************************************************************************


        deleteBtn.setOnClickListener {


            showDeleteDialog()


        }


    }


    private fun showDeleteDialog() {


        val progressDialogBack: Dialog = Dialog(this)
        progressDialogBack.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialogBack.setCancelable(false)
        progressDialogBack.setContentView(R.layout.cus_del_dialog)
        val buttonOkay = progressDialogBack.findViewById<TextView>(R.id.buttonOkay)
        val buttonCancel = progressDialogBack.findViewById<TextView>(R.id.buttonCancel)
        buttonOkay.setOnClickListener {

            val db = AppDatabase.getInstance(this)



            CoroutineScope(Dispatchers.IO).launch {

                db.RdbDao().deleteAllChatsChild()
                db.RdbDao().deleteAllChatsParent()
            }

            startActivity(Intent(this, MainActivity::class.java))

            progressDialogBack.dismiss()
        }
        buttonCancel.setOnClickListener { progressDialogBack.dismiss() }
        progressDialogBack.show()


    }


    private fun navigationClick() {
        binding.navigatView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { item ->
            binding.drawer.closeDrawer(GravityCompat.START)
            when (item.itemId) {

            }
            true
        })
    }


    private fun setUpTabWithViewPager() {

        finalAdapterData = ArrayList()
        finalAdapterData.add(MainModel("All", "All", 0, AllChatFragment()))
        finalAdapterData.add(MainModel("WHATSAPP", "WHATSAPP", 0, WhatsAppFragment()))
        finalAdapterData.add(MainModel("MESSENGER", "MESSENGER", 0, FacebookFragment()))
        finalAdapterData.add(MainModel("INSTAGRAM", "INSTAGRAM", 0, InstaFragment()))


        var mainAdapter = MainTabAdaptor(
            supportFragmentManager,
            this@MainActivity,
            finalAdapterData.size,
            finalAdapterData
        )


        binding.viewPager.setAdapter(mainAdapter)
        binding.tabView.setupWithViewPager(binding.viewPager)


        for (i in 0 until binding.tabView.getTabCount()) {
            val tab: TabLayout.Tab = binding.tabView.getTabAt(i)!!
            tab.customView = mainAdapter.getTabView(i)
        }
        binding.tabView.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (view != null) {

                    val imageView = view.findViewById<ImageView>(R.id.item_icon)
                    imageView.imageAlpha = 0xFF
                    val textView = view.findViewById<TextView>(R.id.item_name)
                    textView.setTextColor(resources.getColor(R.color.white))
                    val count = view.findViewById<TextView>(R.id.item_count)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val view = tab.customView
                if (view != null) {
                    val imageView = view.findViewById<ImageView>(R.id.item_icon)
                    imageView.imageAlpha = 0x2F
                    val textView = view.findViewById<TextView>(R.id.item_name)
                    textView.setTextColor(resources.getColor(R.color.gray))
                    val count = view.findViewById<TextView>(R.id.item_count)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


    }


    private fun allowNotificationPermission() {

        val isNotificationServiceRunning: Boolean = isNotificationServiceRunning()

        Log.d("ppppppppppp", "allowNotificationPermission: 11   $isNotificationServiceRunning")


        if (isNotificationServiceRunning) {


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Log.d("ppppppppppp", "allowNotificationPermission: 11")
                startForgroundService()
            } else {
                Log.d("ppppppppppp", "allowNotificationPermission: 11")

                startServiceChat()

            }


        } else {

            showNotiPermiDialog()
        }


    }


    private fun showNotiPermiDialog() {
        val progressDialogBack: Dialog = Dialog(this)
        progressDialogBack.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialogBack.setCancelable(false)
        progressDialogBack.setContentView(R.layout.cus_not_per_dialog)
        val buttonOkay = progressDialogBack.findViewById<TextView>(R.id.buttonOkay)

        buttonOkay.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            progressDialogBack.dismiss()
        }
        progressDialogBack.show()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun startForgroundService() {
        val serviceIntent = Intent(this@MainActivity, SmsService::class.java)
        startForegroundService(serviceIntent)
    }

    private fun startServiceChat() {

        val serviceIntent = Intent(this@MainActivity, SmsService::class.java)
        startService(serviceIntent)
    }

    private fun isNotificationServiceRunning(): Boolean {
        val contentResolver = contentResolver
        val enabledNotificationListeners =
            Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        val packageName = packageName
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(
            packageName
        )
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }


}