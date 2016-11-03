package napatskyf.reminder.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import napatskyf.reminder.MainActivity;
import napatskyf.reminder.R;
import napatskyf.reminder.Utils;
import napatskyf.reminder.model.ModelTask;

/**
 * Created by SERVER 1C 8 hlib on 13.09.2016.
 */
public class AddingTaskDialogFragment extends DialogFragment  {

    public   AddingTaskListener addingTaskListener;
    private Spinner mSpinerPriority;

    public interface AddingTaskListener {
        void onTaskAdded(ModelTask newTask);
        void onTaskAddingCancel();
    }


    public  void setClickListener(Activity clicklistener ) {
       addingTaskListener = (AddingTaskListener) clicklistener;
    }


//    @Override
//    public void onAttach(Activity context) {
//        super.onAttach(context);
//        try {
//            addingTaskListener = (AddingTaskListener) context;
//        }catch (ClassCastException e){
//            throw new ClassCastException(context.toString()+"must implement AddingTaskListener");
//        }
//    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        final AlertDialog.Builder dialogBilder = new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_title);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View conteiner = inflater.inflate(R.layout.dialog_task,null);

        TextInputLayout textInputTitle = (TextInputLayout) conteiner.findViewById(R.id.tilDialogTaskTitle);
        final EditText editTextTitle = textInputTitle.getEditText();

        textInputTitle.setHint(getResources().getText(R.string.task_title));

        TextInputLayout textInputDate = (TextInputLayout) conteiner.findViewById(R.id.tilDialogTaskDate);
        final EditText editTextDate = textInputDate.getEditText();

        //final EditText editTextDate = (EditText) conteiner.findViewById(R.id.editTextDate);

        textInputDate.setHint(getResources().getString(R.string.task_date));

        final TextInputLayout textInputTime = (TextInputLayout) conteiner.findViewById(R.id.tilDialogTaskTime);
        final EditText editTextTime = textInputTime.getEditText();

        textInputTime.setHint(getResources().getString(R.string.task_time));
        final ModelTask task = new ModelTask();
        task.setTimeStamp(System.currentTimeMillis());
        task.setStatus(ModelTask.STATUS_CURRENT);
        mSpinerPriority = (Spinner) conteiner.findViewById(R.id.spinerPriority);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,ModelTask.PRIORITY_LEVEL);
        mSpinerPriority.setAdapter(adapter);
        task.setPriority(0);
        mSpinerPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                task.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dialogBilder.setView(conteiner);


        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,calendar.get(Calendar.HOUR_OF_DAY)+1);

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           //     Toast.makeText(getActivity(),"Task added",Toast.LENGTH_SHORT).show();
                DialogFragment dateDialogFragment = new DatePickerFragmant(){
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int  monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR,year);
                        calendar.set(Calendar.MONTH,monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        editTextDate.setText(Utils.getDate(calendar.getTimeInMillis()));
                        super.onDateSet(datePicker, year, monthOfYear, dayOfMonth);
                    }

                    @Override
                    public void onCancel(DialogInterface dialog) {
                        editTextDate.setText("");
                        super.onCancel(dialog);
                    }
                };
             dateDialogFragment.show(getFragmentManager(),"DatePickerFragment");
            }
        });




        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment timeDialogFragment = new TimePickerFragment(){

                    @Override
                     public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        // calendar.set(0,0,0,i,i1);

                        calendar.set(Calendar.HOUR_OF_DAY, i);
                        calendar.set(Calendar.MINUTE, i1);
                        calendar.set(Calendar.SECOND, 0);

                         editTextTime.setText(Utils.getTime(calendar.getTimeInMillis()));
                         super.onTimeSet(timePicker, i, i1);
                     }

                     @Override
                     public void onCancel(DialogInterface dialog) {
                         editTextTime.setText("");
                         super.onCancel(dialog);
                     }
                 };
              timeDialogFragment.show(getFragmentManager(),"TimePickerFragment");
            }


        });

        dialogBilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                task.setmTitle(editTextTitle.getText().toString());
                if(editTextDate.length()!= 0 || editTextTime.length()!= 0){
                    task.setDate(calendar.getTimeInMillis());

                }

                addingTaskListener.onTaskAdded(task);
               // Toast.makeText(getActivity(), "Task added",Toast.LENGTH_SHORT).show();



            }
        });

        dialogBilder.setNegativeButton(R.string.dilog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addingTaskListener.onTaskAddingCancel();
                dialogInterface.cancel();
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = dialogBilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                final Button positionButton = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
                if(editTextTitle.length() == 0){
                    positionButton.setEnabled(false);
                    editTextTitle.setError(getResources().getString(R.string.dialod_error_epty_title));
                }

                editTextTitle.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if(charSequence.length() == 0){
                            positionButton.setEnabled(false);
                            editTextTitle.setError(getResources().getString(R.string.dialod_error_epty_title));
                        } else {
                            positionButton.setEnabled(true);
                            textInputTime.setErrorEnabled(false);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        });



        return alertDialog;
    }
}

