package napatskyf.reminder.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
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
import napatskyf.reminder.model.ModelTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends TaskFragment {
//    RecyclerView recyclerView;
//    RecyclerView.LayoutManager layoutManager;

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

    @Override
    public void moveTask(ModelTask task) {
        onTaskRestoreListner.onTaskRestoreListner(task);
    }


    @Override
    public void addTaskFromDB(MainActivity mainActivity) {
//        List<ModelTask> tasks = new ArrayList<>();
//        tasks.addAll(activity.dbHelper.query().getTasks(DBHelper.SELECTION_STATUS ,
//                new String[]{Integer.toString(ModelTask.STATUS_DONE)}, DBHelper.TASK_DATE_COLUMN));
//        for (int i = 0; i < tasks.size(); i++) {
//            addTask(tasks.get(i), false);
//        }
    }
}
