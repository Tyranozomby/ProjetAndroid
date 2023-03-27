package com.example.automessages.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;

import com.example.automessages.Contact;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                // Extract the SMS message from the bundle
                SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                if (messages != null && messages.length > 0) {
                    // Extract the sender's phone number and message body from the SMS message
                    String phoneNumber = messages[0].getOriginatingAddress();

                    // Check if the sender is in the list of contacts
                    phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber.replace("+33", "0"), "FR");
                    if (Contact.getAllSelected(context).stream().map(Contact::getPhoneNumber).anyMatch(phoneNumber::equals)) {
                        SmsSender.sendSMS(phoneNumber, context);
                    }
                }
            }
        }
    }
}
