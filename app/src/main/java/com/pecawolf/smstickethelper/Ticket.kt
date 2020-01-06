package com.pecawolf.smstickethelper

import java.time.*

class Ticket(
  val code: String,
  val price: Int,
  val duration: Duration
)