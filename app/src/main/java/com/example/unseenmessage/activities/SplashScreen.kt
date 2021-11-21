package com.example.unseenmessage.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.unseenmessage.databinding.ActivitySplashScreenBinding


class SplashScreen : AppCompatActivity() {

    lateinit var binding: ActivitySplashScreenBinding

    // Identifier for the permission request
    private val READ_CONTACTS_PERMISSIONS_REQUEST = 1

    private var check_perm = false


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)




        binding.getStarted.setOnClickListener {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
            ) {

                getPermissionToReadUserContacts()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }


        }


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun getPermissionToReadUserContacts() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // The permission is NOT already granted.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS
                )
            ) {
            }
            // This will show the standard permission request dialog UI
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSIONS_REQUEST
            )
        }
    }

    /**
     * @param requestCode
     * @param permissions
     * @param grantResults Callback with the request from calling requestPermissions(...)
     */
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.size == 1 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                check_perm = true
                Toast.makeText(this, "Read Contacts permission granted", Toast.LENGTH_SHORT).show()
            } else {
                check_perm = false
                getPermissionToReadUserContacts()
                Toast.makeText(this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


}