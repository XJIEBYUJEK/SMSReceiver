package com.example.smsreceiver

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class SMSReceiver : BroadcastReceiver() {
    companion object {
        private val TAG by lazy { SMSReceiver::class.java.simpleName }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!intent?.action.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) return
        val extractMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        extractMessages.forEach { smsMessage ->
            val builder = NotificationCompat.Builder(context!!, "test")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(smsMessage.displayOriginatingAddress)
                .setContentText(smsMessage.displayMessageBody)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(context)){
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                notify(1, builder.build())
            }
            Log.v(TAG, smsMessage.displayMessageBody)
            Toast.makeText(context, smsMessage.displayMessageBody, Toast.LENGTH_SHORT).show()
            Log.v(TAG, smsMessage.displayOriginatingAddress)
        }

    }
}