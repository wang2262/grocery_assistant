package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by yuanyuanji on 3/24/18.
 */

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private TimePickerFragmentListener timePickerFragmentListener = null;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // the default time to receive notification is 4:00 pm.
        int hour = 16;
        int minute = 0;

        onAttachToParentFragment(getTargetFragment());
        //Create and return a new instance of TimePickerDialog
       // com.android.internal.R.style.Theme_DeviceDefault_Dialog_Alert,
        return new TimePickerDialog(getActivity(),this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // pass hour and minute to the invoking fragment.
        if (timePickerFragmentListener != null) {
            timePickerFragmentListener.onTimeSetDone(hourOfDay, minute);
        }

    }

    public interface TimePickerFragmentListener {
        void onTimeSetDone(int hourOfDay, int minute);

    }

    public void onAttachToParentFragment(Fragment parentFragment){
        if (parentFragment != null) {
            try{
                timePickerFragmentListener = (TimePickerFragmentListener) parentFragment;
            } catch (ClassCastException e) {
                throw new ClassCastException(parentFragment.toString() + " must implement TimePickerFragmentListener interface.");
            }

        }
    }

}
