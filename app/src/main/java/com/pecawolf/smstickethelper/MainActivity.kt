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
import java.time.*

class MainActivity: AppCompatActivity() {

  companion object {
    const val SMS_NUMBER = "90206"

    const val PRG = "PRG"
    const val BRN = "BRN"
    const val ZLN = "ZLN"

    val CITIES = mapOf(
      Pair(PRG, mapOf(
        Pair("90m", Ticket("DPT42", 42, Duration.ofMinutes(90))),
        Pair("30m", Ticket("DPT31", 31, Duration.ofMinutes(30))),
        Pair("24h", Ticket("DPT120", 120, Duration.ofHours(24))),
        Pair("72h", Ticket("DPT330", 330, Duration.ofHours(72)))
      )),
      Pair(BRN, mapOf(
        Pair("20m", Ticket("BRNO20", 20, Duration.ofMinutes(20))),
        Pair("75m", Ticket("BRNO", 29, Duration.ofMinutes(75))),
        Pair("24h", Ticket("BRNOD", 99, Duration.ofHours(24)))
      )),
      Pair(ZLN, mapOf(
        Pair("40m", Ticket("DSZO", 20, Duration.ofMinutes(40))),
        Pair("24h", Ticket("DSZO24", 90, Duration.ofHours(24))),
        Pair("24hZ", Ticket("DSZO24Z", 45, Duration.ofHours(24)))
      ))
    )

    const val SMS_PERMISSIONS_REQUEST = 719
  }

  var locked = false

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

    initViews()
  }

  private fun initViews() {
    findViewById<TextView>(R.id.prg_24).run {
      CITIES.getValue(PRG).getValue("30m").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          PRG,
          "${ticket.duration.toMinutes()}m",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.prg_32).run {
      CITIES.getValue(PRG).getValue("90m").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          PRG,
          "${ticket.duration.toMinutes()}m",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.prg_110).run {
      CITIES.getValue(PRG).getValue("24h").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          PRG,
          "${ticket.duration.toHours()}h",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.prg_310).run {
      CITIES.getValue(PRG).getValue("72h").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          PRG,
          "${ticket.duration.toHours()}h",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.brn_20).run {
      CITIES.getValue(BRN).getValue("20m").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          BRN,
          "${ticket.duration.toMinutes()}m",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.brn_29).run {
      CITIES.getValue(BRN).getValue("75m").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          BRN,
          "${ticket.duration.toMinutes()}m",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.brn_99).run {
      CITIES.getValue(BRN).getValue("24h").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          BRN,
          "${ticket.duration.toHours()}h",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.zln_40).run {
      CITIES.getValue(ZLN).getValue("40m").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          ZLN,
          "${ticket.duration.toMinutes()}m",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.zln_24).run {
      CITIES.getValue(ZLN).getValue("24h").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          ZLN,
          "${ticket.duration.toHours()}h",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
    findViewById<TextView>(R.id.zln_24_disc).run {
      CITIES.getValue(ZLN).getValue("24hZ").let { ticket ->
        text = String.format(
          resources.getString(R.string.button_template),
          ZLN,
          "${ticket.duration.toHours()}h",
          ticket.price
        )
        setOnClickListener { sendSms(ticket.code) }
      }
    }
  }

  private fun sendSms(smsBody: String) {
    //    Toast.makeText(this, smsBody, Toast.LENGTH_SHORT).show()
    if (locked) {
      return
    }
    locked = true
    if (checkPermission(Manifest.permission.SEND_SMS) && checkPermission(
        Manifest.permission.RECEIVE_SMS) && checkPermission(Manifest.permission.READ_PHONE_STATE)
    ) {
      try {
        val phoneNumber = (getSystemService(
          Context.TELEPHONY_SERVICE) as TelephonyManager
          ).line1Number

        AlertDialog.Builder(this)
          .setTitle(resources.getString(R.string.dialog_title))
          .setMessage(resources.getString(R.string.dialog_message, smsBody, SMS_NUMBER))
          .setPositiveButton(android.R.string.ok) { dialog, _ ->
            SmsManager.getDefault().sendTextMessage(SMS_NUMBER, phoneNumber, smsBody, null, null)
            Toast.makeText(
              this,
              "Message \"$smsBody\" sent to $SMS_NUMBER from $phoneNumber",
              Toast.LENGTH_LONG
            ).show()
            dialog.dismiss()
            locked = false
          }
          .setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
            locked = false
          }
          .show()
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
