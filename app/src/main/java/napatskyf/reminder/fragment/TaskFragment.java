package napatskyf.reminder.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import napatskyf.reminder.MainActivity;
import napatskyf.reminder.R;
import napatskyf.reminder.adapret.CurrentTaskAdapter;
import napatskyf.reminder.adapret.TaskAdapter;
import napatskyf.reminder.database.DBHelper;
import napatskyf.reminder.model.Item;
import napatskyf.reminder.model.ModelTask;

/**
 * Created by SERVER 1C 8 hlib on 24.10.2016.
 */

public abstract class TaskFragment extends Fragment {
    protected RecyclerView recycleView;
    protected RecyclerView.LayoutManager layoutManager;
    protected TaskAdapter adapter;

    public MainActivity activity = (MainActivity) getActivity();

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(getActivity() !=null)
//        {
//     //     activity  = (MainActivity) getActivity();
//        }
//        //addTaskFromDB(activity);
//    }



    public abstract void moveTask(ModelTask task);

    public abstract void addTaskFromDB(MainActivity mainActivity);




}
