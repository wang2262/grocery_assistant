package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by yuanyuanji on 3/21/18.
 */

public class NotificationSettingDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.dialog_notification_setting, null);

        CheckBox checkBox = view.findViewById(R.id.notification_setting_checkbox);

        NumberPicker closeInDays = view.findViewById(R.id.notification_setting_closeInDays);
        closeInDays.setMinValue(0);
        closeInDays.setMaxValue(12);
        closeInDays.setWrapSelectorWheel(true);

        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        Button notificationSettingTime = view.findViewById(R.id.notification_setting_time);

        notificationSettingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });


        builder.setView(view)
                .setTitle("Notification Setting")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("I am in Positive: " + i);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("I am in Negative: " + i);
                    }
                });
        return builder.create();
    }
}
