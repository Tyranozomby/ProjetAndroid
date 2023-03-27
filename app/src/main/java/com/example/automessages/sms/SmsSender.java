package com.example.automessages.sms;

import android.content.Context;
import android.telephony.SmsManager;

import com.example.automessages.Answer;

public class SmsSender {
    private static final SmsManager smsManager = SmsManager.getDefault();

    public static void sendSMS(String phoneNumber, Context context) {
        smsManager.sendTextMessage(phoneNumber, null, Answer.getAnswer(context), null, null);
    }
}
