package napatskyf.reminder.adapret;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import napatskyf.reminder.fragment.TaskFragment;
import napatskyf.reminder.model.Item;

/**
 * Created by SERVER 1C 8 hlib on 24.10.2016.
 */

public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Item> items;
    TaskFragment taskFragment;

    public TaskAdapter(TaskFragment taskFragment) {
        this.items = new ArrayList<>();
        this.taskFragment = taskFragment;
    }



    public Item getItem(int position) {
        return items.get(position);
    }

    public void addItem(Item item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }


    public void addItem(int position, Item item) {
        items.add(position, item);
        notifyItemInserted(position);
    }


    public void removeItem(int position) {
        if (position >= 0 && position <= getItemCount() - 1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected class TaskViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView date;
        protected CircleImageView prioriry;

        public TaskViewHolder(View itemView, TextView title, TextView date,CircleImageView prioriry) {
            super(itemView);
            this.date = date;
            this.title = title;
            this.prioriry = prioriry;
        }
    }


    public TaskFragment getTaskFragment() {
        return taskFragment;
    }
}
