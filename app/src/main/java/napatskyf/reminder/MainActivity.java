package napatskyf.reminder;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.MenuPopupWindow;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import napatskyf.reminder.adapret.TabAdapter;
import napatskyf.reminder.database.DBHelper;
import napatskyf.reminder.dialog.AddingTaskDialogFragment;
import napatskyf.reminder.dialog.EditTaskDialogFragment;
import napatskyf.reminder.fragment.CurrentTaskFragment;
import napatskyf.reminder.fragment.DoneTaskFragment;
import napatskyf.reminder.fragment.SplashFragment;
import napatskyf.reminder.model.ModelTask;


public class MainActivity extends AppCompatActivity implements AddingTaskDialogFragment.AddingTaskListener , CurrentTaskFragment.OnTaskDoneListener,DoneTaskFragment.OnTaskRestoreListner , EditTaskDialogFragment.onEditTaskListener {
    FragmentManager fragmentManager;
    PreferenceHelper preferenceHelper;
    DoneTaskFragment doneTaskFragment;
    CurrentTaskFragment currentTaskFragment;
    TabAdapter tabAdapter;
    Context context;
    public  DBHelper dbHelper;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getFragmentManager();
        PreferenceHelper.getInstance().init(getApplicationContext());
        preferenceHelper = PreferenceHelper.getInstance();
        runSplash();
        dbHelper = new DBHelper(getApplicationContext());
        context = this;
        setUI();
    }

    public void runSplash() {
        if (!preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
            SplashFragment splashFragment = new SplashFragment();
            fragmentManager.beginTransaction().replace(R.id.content_frame, splashFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_splash);
        menuItem.setChecked(preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_splash) {
            item.setChecked(!item.isChecked());
            preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
            setSupportActionBar(toolbar);
        }

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabAdapter = new TabAdapter(fragmentManager, 2);
        viewPager.setAdapter(tabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        currentTaskFragment = (CurrentTaskFragment) tabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        doneTaskFragment = (DoneTaskFragment) tabAdapter.getItem(TabAdapter.DONE_TASK_FRAGMENT_POSITION);
        //DoneTaskFragment done = new DoneTaskFragment();
        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentTaskFragment.findTasks(newText);
                doneTaskFragment.findTasks(newText);
                return false;
            }
        });
        doneTaskFragment.setClickListener(this);
        currentTaskFragment.setClickListener(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddingTaskDialogFragment dialogFragment = new AddingTaskDialogFragment();
                dialogFragment.setClickListener((MainActivity) context);
                dialogFragment.show(fragmentManager, "AddingTaskDialogFragment");
            }
        });
    }

    @Override
    public void onTaskAdded(ModelTask task) {
         currentTaskFragment.addTask(task,true);
        dbHelper.saveTask(task);
        //    Toast.makeText(this,"Task added",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, "Task adding cancel", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTaskDone(ModelTask task) {
       // doneTaskFragment = new CurrentTaskFragment();
        doneTaskFragment.addTask(task,false);
    }

    @Override
    public void onTaskRestoreListner(ModelTask task) {
        currentTaskFragment.addTask(task,false);
    }


    @Override
    public void onTaskListner(ModelTask task) {
        currentTaskFragment.udaterTask(task);
        dbHelper.update().task(task);
    }
}

