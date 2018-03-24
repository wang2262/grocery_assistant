package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by yuanyuanji on 3/24/18.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /*
        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        */

        // the default time to receive notification is 4:00 pm.
        int hour = 16;
        int minute = 0;
        //Create and return a new instance of TimePickerDialog
       // com.android.internal.R.style.Theme_DeviceDefault_Dialog_Alert,
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        System.out.println("hoursOfDay: " + hourOfDay);
        System.out.println("minutesOfDay: " + minute);
    }
}
