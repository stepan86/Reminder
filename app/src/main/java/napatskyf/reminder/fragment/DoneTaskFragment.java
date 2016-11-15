package napatskyf.reminder.fragment;


import android.app.Activity;
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
import java.util.List;

import napatskyf.reminder.MainActivity;
import napatskyf.reminder.R;
import napatskyf.reminder.adapret.DoneTaskAdapter;
import napatskyf.reminder.database.DBHelper;
import napatskyf.reminder.model.Item;
import napatskyf.reminder.model.ModelTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends Fragment {
    RecyclerView recycleView;
    RecyclerView.LayoutManager layoutManager;
    DoneTaskAdapter adapter;
    OnTaskRestoreListner onTaskRestoreListner;

    public  void setClickListener(Activity clicklistener ) {
        onTaskRestoreListner = (OnTaskRestoreListner) clicklistener;
    }


    public interface OnTaskRestoreListner {
        void onTaskRestoreListner(ModelTask task);
    }

    public DoneTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_done_task, container, false);
        recycleView = (RecyclerView) rootView.findViewById(R.id.rvDoneTask);
        layoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(layoutManager);
        adapter = new DoneTaskAdapter(this);
        recycleView.setAdapter(adapter);
        return rootView;
    }


    public void moveTask(ModelTask task) {
        onTaskRestoreListner.onTaskRestoreListner(task);
    }


    @Override
    public void onResume() {
        super.onResume();
        addTaskFromDB((MainActivity) getActivity());
    }


    public void addTaskFromDB(MainActivity mainActivity) {
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(mainActivity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS ,
                new String[]{Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }


    public void findTasks(String title) {
        adapter.removeAllItems();
        List<ModelTask> tasks = new ArrayList<>();
        tasks.addAll(((MainActivity) getActivity()).dbHelper.query().getTasks(DBHelper.SELECTION_LIKE_TITLE +
                " AND "+DBHelper.SELECTION_STATUS
                ,new String[]{ "%"+title+"%",Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.TASK_DATE_COLUMN));
        for (int i = 0; i < tasks.size(); i++) {
            addTask(tasks.get(i), false);
        }
    }

    public void addTask(ModelTask newTask,boolean b) {
        int position = -1;

        for (int i=0;i<adapter.getItemCount();i++)
        {
            if(adapter.getItem(i).isTask())
            {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if(newTask.getDate() < task.getDate())
                {
                    position = i;
                    break;
                }
            }
        }

        if(position != -1 )
        {
            adapter.addItem(position,newTask);
        }else
        {
            adapter.addItem(newTask);
        }
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
                            addTask(((MainActivity) getActivity()).dbHelper.query().getTask(timeStamp),false);
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
}
