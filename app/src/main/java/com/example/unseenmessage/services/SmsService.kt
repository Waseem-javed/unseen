package com.example.unseenmessage.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.provider.ContactsContract
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.unseenmessage.R
import com.example.unseenmessage.UtilsKeys
import com.example.unseenmessage.activities.MainActivity
import com.example.unseenmessage.dao.AppDatabase
import com.example.unseenmessage.interfaces.RDBdao
import com.example.unseenmessage.models.ChildTable
import com.example.unseenmessage.models.ParentTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch


@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
class SmsService : NotificationListenerService() {

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val NOTIFICATION_CHANNEL_ID = "com.example.whatsapp_recoverdata"
    private val channelName = "My Background Service"
    private val pkgLastNotificationWhen: MutableMap<String, Long> = HashMap()


    private lateinit var db: RDBdao

    //    Observer *********************************************************************
//    var directoryFileObserver: DirectoryFileObserver? = null
    override fun onCreate() {
        super.onCreate()
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: ")
        db = AppDatabase.getInstance(application).RdbDao()
        createNotification("Started", "")
        return START_STICKY
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
//        observers()
        Log.d(TAG, "onListenerConnected: ")
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onNotificationPosted(sbn: StatusBarNotification) {

        Log.d(TAG, "onNotificationPosted: ")


        if (sbn.notification.flags and Notification.FLAG_GROUP_SUMMARY !== 0) {
            Log.d(TAG, "Ignore the notification FLAG_GROUP_SUMMARY")
            return
        }

        val lastWhen = pkgLastNotificationWhen[sbn.packageName]
        if (lastWhen != null && lastWhen >= sbn.notification.`when`) {
            Log.d(TAG, "Ignore Old notification")
            return
        }
        pkgLastNotificationWhen.put(sbn.packageName, sbn.notification.`when`)



        time = System.currentTimeMillis().toInt()

        ////////////////////////////whatsapp msg show/////////////
        if (sbn.packageName == WA_PACKAGE) {


            val notification = sbn.notification
            val bundle = notification.extras

            user_name = bundle.getString(NotificationCompat.EXTRA_TITLE)!!
            user_sms = bundle.getString(NotificationCompat.EXTRA_TEXT)!!

            if (user_name.equals("WhatsApp")) {
                return
            } else {
                Log.e(
                    TAG,
                    "onNotificationPostedMessage is:  ${user_name}  and  $user_sms ,, ${sbn.notification.`when`} "
                )


                var phon = findPhoneNumberIfName(user_name)

                Log.d(TAG, "onNotificationPosted  111: $phon   ,,  $user_name   ,,   $user_sms")

                if (phon.equals("")) {

                    Log.d(TAG, "onNotificationPosted 222: $phon   ,,  $user_name   ,,   $user_sms")

                    if (user_name!!.contains(":")) {

                        if (user_name!!.contains("messages):")) {

                            var full_name = user_name
                            val first_name: String? = full_name!!.substringBefore(":")


                            val fi = first_name!!.lastIndexOf("(")
                            val li = first_name!!.lastIndexOf(")")

                            val first_nameoc: String? = first_name!!.removeRange(fi, li + 1)

                            val sender_name: String? = full_name!!.substringAfter(":")

                            Log.d(
                                TAG,
                                "onNotificationPosted: sub111    $sender_name   ;;;    ....   $first_nameoc"
                            )

                            insertIntoRoom(
                                first_nameoc!!,
                                first_nameoc,
                                time!!,
                                UtilsKeys.WhatsAppKey,
                                "${sender_name}: $user_sms"
                            )


                        } else {

                            var full_name = user_name
                            val first_name: String? = full_name!!.substringBefore(":")
                            val second_name: String? = full_name!!.substringAfter(":")

                            Log.d(
                                TAG,
                                "onNotificationPosted: sub222  $first_name   ....   $second_name"
                            )

                            insertIntoRoom(
                                first_name!!,
                                first_name,
                                time!!,
                                "${UtilsKeys.WhatsAppKey}",
                                "${second_name}: $user_sms"
                            )


                        }

                    }


                } else {

                    Log.d(
                        TAG,
                        "onNotificationPosted: subLASTTT  $phon   ....   $user_name"
                    )
                    insertIntoRoom(phon, user_name, time!!, "${UtilsKeys.WhatsAppKey}", user_sms!!)

                }

            }
        }

//        Messenger ?????????????????????????????????????????????????????????????????????????
        else if (sbn.packageName == FA_PACKAGE) {

            val notification = sbn.notification
            val bundle = notification.extras

            user_name = bundle.getString(NotificationCompat.EXTRA_TITLE)!!
            user_sms = bundle.getString(NotificationCompat.EXTRA_TEXT)!!

//            Messenger

            Log.d(TAG, "onNotificationPosted: ${user_name}    SMS  :  $user_sms")

        }

    }


    @SuppressLint("Range")
    private fun findPhoneNumberIfName(Uname: String?): String {

        var userPhone: String = ""

        val projection = arrayOf(
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.MIMETYPE,
            "account_type",
            ContactsContract.Data.DATA1,
            ContactsContract.Data.PHOTO_URI
        )
        val selection = ContactsContract.Data.MIMETYPE + " =? and account_type=?"
        val selectionArgs = arrayOf(
            "vnd.android.cursor.item/vnd.com.whatsapp.profile",
            "com.whatsapp"
        )
        val cr = contentResolver
        val c = cr.query(
            ContactsContract.Data.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )
        while (c!!.moveToNext()) {
            @SuppressLint("Range") val id =
                c!!.getString(c!!.getColumnIndex(ContactsContract.Data.CONTACT_ID))
            @SuppressLint("Range") val numberW =
                c!!.getString(c!!.getColumnIndex(ContactsContract.Data.DATA1))
            val parts = numberW.split("@").toTypedArray()
            val numberPhone = parts[0]
            val number = "Tel : + " + numberPhone.substring(0, 2) + " " + numberPhone.substring(
                2,
                numberPhone.length
            )

            var name = ""
            val mCursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                arrayOf(ContactsContract.Contacts.DISPLAY_NAME),
                ContactsContract.Contacts._ID + " =?",
                arrayOf(id),
                null
            )
            while (mCursor!!.moveToNext()) {
                name =
                    mCursor!!.getString(mCursor!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                if (name.equals(Uname)) {

                    Log.d("findPhoneNumberIfName", "  findPhoneNumberIfName: " + number)
                    userPhone = numberPhone

                }
            }
            mCursor!!.close()
        }

        return userPhone
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createNotification(title1: String?, message1: String?) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            channelName,
            NotificationManager.IMPORTANCE_NONE
        )
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
        manager.createNotificationChannel(chan)
        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification: Notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.drawable.rect_btn)
            .setContentTitle("$title1  $message1")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(2, notification)
    }

//    private fun observers() {
//        val imagepath =
//            Environment.getExternalStorageDirectory().absolutePath + "/WhatsApp/Media/WhatsApp Images/"
//        if (Build.VERSION.SDK_INT >= 29) {
//            directoryFileObserver = DirectoryFileObserver(File(imagepath), this)
//        } else {
//            directoryFileObserver = DirectoryFileObserver(imagepath, this)
//        }
//        directoryFileObserver.startWatching()
//    }

    companion object {
        private const val TAG = "SmsService"
        private const val WA_PACKAGE = "com.whatsapp"
        private const val FA_PACKAGE = "com.facebook.orca"
        var user_name: String? = null
        var user_sms: String? = null
        var user_phone: String? = null
        var time: Int? = null

    }


    //    ROOM DATA BASE ************************   __________________  *********************************************

    private fun insertIntoRoom(
        phon: String,
        userName: String?,
        time: Int,
        pkg: String,
        user_sms: String
    ) {


        Log.d(TAG, "insertIntoRoom: $userName  ,, $phon   ,,  $pkg ,, $user_sms")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification(user_name, user_sms)
        }


        scope.launch {
            val parentTable =
                ParentTable(
                    phon, userName!!, UtilsKeys.getCurrentDate(),
                    UtilsKeys.getCurentTime(),
                    UtilsKeys.getDateTime(),
                    pkg,
                    user_sms
                )

            val childTable = ChildTable(
                0,
                phon,
                userName,
                UtilsKeys.getCurrentDate(),
                UtilsKeys.getCurentTime(),
                UtilsKeys.getDateTime(),
                pkg,
                user_sms
            )
//            var childEntity = ChildEntity(0, orignalPath, orignalPath)
//            if (!db.RdbDao().getAllParents().value!!.contains(parentTable)) {


            try {
                db.insertParent(parentTable)

                db.insertChild(childTable)
//                Child
//                    db.fileDao().insertFileList(childEntity)

//                    getDataFromRoom()
            } catch (ex: Exception) {
                Log.e(TAG, "insertIntoRoom: ${ex.message}")
            }

        }


    }
//    ROOM DATA BASE ************************   __________________  *********************************************
}