package napatskyf.reminder.fragment;


import android.app.Activity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import napatskyf.reminder.MainActivity;
import napatskyf.reminder.R;
import napatskyf.reminder.adapret.CurrentTaskAdapter;
import napatskyf.reminder.database.DBHelper;
import napatskyf.reminder.dialog.EditTaskDialogFragment;
import napatskyf.reminder.model.Item;
import napatskyf.reminder.model.ModelSeparator;
import napatskyf.reminder.model.ModelTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentTaskFragment extends Fragment {

    RecyclerView recycleView;
    RecyclerView.LayoutManager layoutManager;

    private CurrentTaskAdapter adapter;


    public CurrentTaskFragment() {
        // Required empty public constructor
    }

    OnTaskDoneListener onTaskDoneListener;

    public interface OnTaskDoneListener {
        void onTaskDone(ModelTask task);

    }

    public void setClickListener(Activity clicklistener) {
        onTaskDoneListener = (OnTaskDoneListener) clicklistener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);
        recycleView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTask);
        layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        adapter = new CurrentTaskAdapter(CurrentTaskFragment.this);
        recycleView.setAdapter(adapter);
        return rootView;
    }

    public void moveTask(ModelTask task) {
        onTaskDoneListener.onTaskDone(task);
    }

    public void addTaskFromDB(MainActivity mainActivity) {
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(mainActivity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS + " OR "
                + DBHelper.SELECTION_STATUS, new String[]{Integer.toString(ModelTask.STATUS_CURRENT), Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }


    public void findTasks(String title) {

        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(((MainActivity) getActivity()).dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE +
                " AND " + DBHelper.SELECTION_STATUS +
                " OR " + DBHelper.SELECTION_STATUS, new String[]{"%" + title + "%", Integer.toString(ModelTask.STATUS_CURRENT), Integer.toString(ModelTask.STATUS_OVERDUE)}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }


    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        addTaskFromDB((MainActivity) getActivity());
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addTaskFromDB((MainActivity) getActivity());
    }


//    public void addTask(ModelTask newTask,boolean saveToDB) {
//        int position = -1;
//
//        for (int i=0;i<adapter.getItemCount();i++)
//        {
//            if(adapter.getItem(i).isTask())
//            {
//                ModelTask task = (ModelTask) adapter.getItem(i);
//                if(newTask.getDate() < task.getDate())
//                {
//                    position = i;
//                    break;
//                }
//            }
//        }
//
//        if(position != -1 )
//        {
//            adapter.addItem(position,newTask);
//        }else
//        {
//            adapter.addItem(newTask);
//        }
//
//        if(saveToDB)
//        {
//            activity.dbHelper.saveTask(newTask);
//        }
//    }

    public void addTask(ModelTask newTask, boolean b) {
        int position = -1;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }


        ModelSeparator separator = null;
        if (newTask.getDate() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.getDate());

            if (calendar.get(Calendar.DAY_OF_YEAR) < Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.dateStatus = ModelSeparator.TYPE_OVERDUE;
                if (!adapter.containsSeparatorOverdue) {
                    adapter.containsSeparatorOverdue = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_OVERDUE);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                newTask.dateStatus = ModelSeparator.TYPE_TODAY;
                if (!adapter.containsSeparatorToday) {
                    adapter.containsSeparatorToday = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TODAY);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.dateStatus = ModelSeparator.TYPE_TOMORROW;
                if (!adapter.containsSeparatorTomorrow) {
                    adapter.containsSeparatorTomorrow = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_TOMORROW);
                }
            } else if (calendar.get(Calendar.DAY_OF_YEAR) > Calendar.getInstance().get(Calendar.DAY_OF_YEAR) + 1) {
                newTask.dateStatus = ModelSeparator.TYPE_TOMORROW;
                if (!adapter.containsSeparatorFuture) {
                    adapter.containsSeparatorFuture = true;
                    separator = new ModelSeparator(ModelSeparator.TYPE_FUTURE);
                }
            }
        }


        if (position != -1) {
            if (!adapter.getItem(position - 1).isTask()) {
                if (position - 2 >= 0 && adapter.getItem(position - 2).isTask()) {
                    ModelTask task = (ModelTask) adapter.getItem(position - 2);
                    if (task.dateStatus == newTask.dateStatus) {
                        position -= 1;
                    }
                } else if (position - 2 < 0 && newTask.getDate() == 0) {
                    position -= 1;
                }
            }

            if (separator != null) {
                adapter.addItem(position - 1, separator);
            }
            adapter.addItem(position, newTask);
        } else {
            if (separator != null) {
                adapter.addItem(separator);
            }
            adapter.addItem(newTask);
        }

//        if(position != -1 )
//        {
//            adapter.addItem(position,newTask);
//        }else
//        {
//            adapter.addItem(newTask);
//        }
    }


    public void removeTaskDialog(final int location) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(R.string.dialog_removing_messege);
        Item item = adapter.getItem(location);

        if (item.isTask()) {
            ModelTask task = (ModelTask) item;
            final long timeStamp = task.getTimeStamp();
            final boolean[] isRemoved = {false};
            dialog.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removeItem(location);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator), R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dilog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isRemoved[0] = false;
                            addTask(((MainActivity) getActivity()).dbHelper.query().getTask(timeStamp), false);
                        }
                    });
                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            if (isRemoved[0]) {
                                ((MainActivity) getActivity()).dbHelper.removedTask(timeStamp);
                            }
                        }
                    });
                    snackbar.show();
                    dialog.dismiss();
                }
            });

            dialog.setNegativeButton(R.string.dilog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            dialog.show();
        }

    }


    public void udaterTask(ModelTask newTask) {
        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.getTimeStamp() == task.getTimeStamp()) {
                    adapter.removeItem(i);
                    addTask(newTask, false);
                }
            }
        }

    }


    public void showTaskDialog(ModelTask task)
    {
        EditTaskDialogFragment dialogFragment = EditTaskDialogFragment.newInstance(task);
        dialogFragment.setClickListener(getActivity());
        dialogFragment.show(getFragmentManager(),"EditTaskDialogFragment");
    }

}