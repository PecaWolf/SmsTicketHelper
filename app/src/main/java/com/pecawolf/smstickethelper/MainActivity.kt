package com.pecawolf.smstickethelper

import android.*
import android.content.*
import android.content.pm.*
import android.os.*
import android.support.v4.app.*
import android.support.v4.content.*
import android.support.v7.app.*
import android.telephony.*
import android.view.*
import android.widget.*

class MainActivity: AppCompatActivity() {

  companion object {
    const val SMS_NUMBER = "90206"

    const val PRG_90M = "DPT32"
    const val PRG_30M = "DPT24"
    const val PRG_24H = "DPT110"
    const val PRG_72H = "DPT310"

    const val BRN_20M = "BRNO20"
    const val BRN_75M = "BRNO"
    const val BRN_24H = "BRNOD"

    const val ZLN_40M = "DSZO"
    const val ZLN_24H = "DSZO24"
    const val ZLN_24H_DISC = "DSZO24Z"

    const val SMS_PERMISSIONS_REQUEST = 719
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    ActivityCompat.requestPermissions(
      this,
      arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_PHONE_STATE
      ),
      SMS_PERMISSIONS_REQUEST
    )

    findViewById<View>(R.id.prg_32).setOnClickListener { sendSms(PRG_90M) }
    findViewById<View>(R.id.prg_24).setOnClickListener { sendSms(PRG_30M) }
    findViewById<View>(R.id.prg_110).setOnClickListener { sendSms(PRG_24H) }
    findViewById<View>(R.id.prg_310).setOnClickListener { sendSms(PRG_72H) }
    findViewById<View>(R.id.brn_20).setOnClickListener { sendSms(BRN_20M) }
    findViewById<View>(R.id.brn_29).setOnClickListener { sendSms(BRN_75M) }
    findViewById<View>(R.id.brn_99).setOnClickListener { sendSms(BRN_24H) }
    findViewById<View>(R.id.zln_40).setOnClickListener { sendSms(ZLN_40M) }
    findViewById<View>(R.id.zln_24).setOnClickListener { sendSms(ZLN_24H) }
    findViewById<View>(R.id.zln_24_disc).setOnClickListener { sendSms(ZLN_24H_DISC) }
  }

  private fun sendSms(smsBody: String) {
    if (checkPermission(Manifest.permission.SEND_SMS) && checkPermission(
        Manifest.permission.RECEIVE_SMS) && checkPermission(Manifest.permission.READ_PHONE_STATE)
    ) {
      try {
        val phoneNumber = (getSystemService(
          Context.TELEPHONY_SERVICE) as TelephonyManager
          ).line1Number

        SmsManager.getDefault().sendTextMessage(SMS_NUMBER, phoneNumber, smsBody, null, null)
        Toast.makeText(this, "Message \"$smsBody\" sent to $SMS_NUMBER from $phoneNumber",
          Toast.LENGTH_LONG).show()
      } catch (ex: Exception) {
        Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
        ex.printStackTrace()
      }
    } else {
      Toast.makeText(this, "Missing Permissions", Toast.LENGTH_SHORT).show()
    }
  }

  private fun checkPermission(permission: String) = (ContextCompat.checkSelfPermission(
    this, permission
  ) == PackageManager.PERMISSION_GRANTED)

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    val id = item.itemId

    return if (id == R.id.action_settings) {
      true
    } else super.onOptionsItemSelected(item)

  }
}
