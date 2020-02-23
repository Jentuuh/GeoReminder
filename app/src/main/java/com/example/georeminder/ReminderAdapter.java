package com.example.georeminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class ReminderAdapter extends ArrayAdapter<ReminderEntity> {
    private Context context;
    private AppDatabase db;
    private List<ReminderEntity> reminders;

    public ReminderAdapter(@NonNull Context context, List<ReminderEntity> list, AppDatabase db) {
        super(context, 0, list);
        this.context = context;
        this.db = db;
        reminders = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);

        final ReminderEntity currentReminder = reminders.get(position);

        TextView id = (TextView)listItem.findViewById(R.id.textView_id);
        id.setText(currentReminder.getID());

        Button deleteBtn = (Button)listItem.findViewById(R.id.delete_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                reminders.remove(position);
                                notifyDataSetChanged();
                                DatabasePopulator.deleteReminder(db, currentReminder);
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        return listItem;
    }
}