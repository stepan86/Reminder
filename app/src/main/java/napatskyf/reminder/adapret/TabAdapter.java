package napatskyf.reminder.adapret;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import napatskyf.reminder.fragment.CurrentTaskFragment;
import napatskyf.reminder.fragment.DoneTaskFragment;

;

/**
 * Created by SERVER 1C 8 hlib on 12.09.2016.
 */
public class TabAdapter extends FragmentStatePagerAdapter {
    private int numberOfTabs;
    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;
    public static final int DONE_TASK_FRAGMENT_POSITION = 1;
    CurrentTaskFragment currentTaskFragment;
    DoneTaskFragment doneTaskFragment;

    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        currentTaskFragment = new CurrentTaskFragment();
        doneTaskFragment = new DoneTaskFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case CURRENT_TASK_FRAGMENT_POSITION:
               return currentTaskFragment;

            case DONE_TASK_FRAGMENT_POSITION:
                return doneTaskFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
