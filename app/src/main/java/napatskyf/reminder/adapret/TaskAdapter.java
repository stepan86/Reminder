package napatskyf.reminder.adapret;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import napatskyf.reminder.fragment.CurrentTaskFragment;
import napatskyf.reminder.model.Item;
import napatskyf.reminder.model.ModelSeparator;
import napatskyf.reminder.model.ModelTask;

/**
 * Created by SERVER 1C 8 hlib on 24.10.2016.
 */

public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Item> items;


    public TaskAdapter() {
        this.items = new ArrayList<>();

    }

    public boolean containsSeparatorOverdue;
    public boolean containsSeparatorToday;
    public boolean containsSeparatorTomorrow;
    public boolean containsSeparatorFuture;

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
            if (position - 1 >= 0) {
                if (!getItem(position-1).isTask() ) {
                    ModelSeparator separator = (ModelSeparator) getItem(position - 1);
                    items.remove(position - 1);
                    notifyItemRemoved(position - 1);
                    checkSeparators(separator.type);
                } else if (getItemCount() - 1 >= 0 && !getItem(getItemCount() - 1).isTask()) {
                    ModelSeparator separator = (ModelSeparator) getItem(getItemCount() - 1);
                    checkSeparators(separator.type);
                    int locationTemp = getItemCount() - 1;
                    items.remove(locationTemp);
                    notifyItemRemoved(locationTemp);
                }
            }
        }
    }

    public void checkSeparators(int type) {
        switch (type) {
            case ModelSeparator.TYPE_OVERDUE:
                containsSeparatorOverdue = false;
                break;
            case ModelSeparator.TYPE_TODAY:
                containsSeparatorToday = false;
                break;
            case ModelSeparator.TYPE_TOMORROW:
                containsSeparatorTomorrow = false;
                break;
            case ModelSeparator.TYPE_FUTURE:
                containsSeparatorFuture = false;
                break;
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

        public TaskViewHolder(View itemView, TextView title, TextView date, CircleImageView prioriry) {
            super(itemView);
            this.date = date;
            this.title = title;
            this.prioriry = prioriry;
        }
    }


//    public TaskFragment getTaskFragment() {
//        return taskFragment;
//    }


    protected class SeparatorViewHolder extends RecyclerView.ViewHolder {

        protected TextView type;

        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    public void removeAllItems() {
        if (getItemCount() != 0) {
            items = new ArrayList<>();
            notifyDataSetChanged();
            containsSeparatorOverdue = false;
            containsSeparatorToday = false;
            containsSeparatorTomorrow = false;
            containsSeparatorFuture = false;
        }
    }

}
