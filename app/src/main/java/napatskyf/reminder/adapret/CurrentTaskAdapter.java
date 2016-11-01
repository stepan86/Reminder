package napatskyf.reminder.adapret;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.res.Resources;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import napatskyf.reminder.R;
import napatskyf.reminder.Utils;
import napatskyf.reminder.fragment.CurrentTaskFragment;
import napatskyf.reminder.fragment.TaskFragment;
import napatskyf.reminder.model.Item;
import napatskyf.reminder.model.ModelTask;


/**
 * Created by SERVER 1C 8 hlib on 24.10.2016.
 */

public class CurrentTaskAdapter extends TaskAdapter {

    //    List<Item> items = new ArrayList<>();
//
    private static final int TYPE_TASK = 0;
    private static final int TYPE_SEPARATOR = 1;

    public CurrentTaskAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }


//
//    public Item getItem(int position) {
//        return items.get(position);
//    }
//
//    public void addItem(Item item) {
//        items.add(item);
//        notifyItemInserted(getItemCount()-1);
//    }
//
//
//    public void addItem(int position,Item item) {
//        items.add(position,item);
//        notifyItemInserted(position);
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TASK:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_task, parent, false);
                TextView title = (TextView) view.findViewById(R.id.tvTaskTitle);
                TextView date = (TextView) view.findViewById(R.id.tvTaskDate);
                CircleImageView priority = (CircleImageView) view.findViewById(R.id.cvTaskPriority);
                return new TaskViewHolder(view, title, date, priority);
            case TYPE_SEPARATOR:
                break;
            default:
                return null;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Item item = items.get(position);
        if (item.isTask()) {
            holder.itemView.setEnabled(true);

            final ModelTask task = (ModelTask) item;
            final TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            final View itemView = taskViewHolder.itemView;
            //Resources resources = itemView.getResources();
            taskViewHolder.title.setText(task.getTitle());
            if (task.getDate() != 0) {
                taskViewHolder.date.setText(Utils.getFullDate(task.getDate()));
            } else {
                //    taskViewHolder.date.setText(null); ????
            }
            final Resources resources = itemView.getResources();
            itemView.setVisibility(View.VISIBLE);
            itemView.setBackgroundColor(resources.getColor(R.color.gray_50));
            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
            taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
            taskViewHolder.prioriry.setColorFilter(resources.getColor(task.getPriorityColor()));
            taskViewHolder.prioriry.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);

            taskViewHolder.prioriry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    task.setStatus(ModelTask.STATUS_DONE);

                 //   getTaskFragment().activity.dbHelper.update().status(task.getTimeStamp(),ModelTask.STATUS_DONE);

                    itemView.setBackgroundColor(resources.getColor(R.color.gray_200));
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
                    taskViewHolder.prioriry.setColorFilter(resources.getColor(task.getPriorityColor()));

                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.prioriry, "rotationY", -180f, 0f);
                    flipIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() == ModelTask.STATUS_DONE) {
                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView, "translationX", 0f, itemView.getWidth());
                                ObjectAnimator translationBackX = ObjectAnimator.ofFloat(itemView, "translationX", itemView.getWidth(), 0f);

                                taskViewHolder.prioriry.setImageResource(R.drawable.ic_check_circle_white_48dp);

                                AnimatorSet set = new AnimatorSet();

                                set.play(translationX).before(translationBackX);
                                set.start();
                                translationX.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        itemView.setVisibility(View.GONE);
                                        getTaskFragment().moveTask(task);
                                         //removeItem(position);
                                         removeItem(taskViewHolder.getLayoutPosition());
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }

                    });
                    flipIn.start();
                }
            });

        }
    }

//    @Override
//    public int getItemCount() {
//        return items.size();
//    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isTask()) {
            return TYPE_TASK;
        }
        return TYPE_SEPARATOR;
    }

//    private class TaskViewHolder extends RecyclerView.ViewHolder {
//    TextView title;
//    TextView date;
//
//        public TaskViewHolder(View itemView,TextView title , TextView date) {
//            super(itemView);
//            this.date = date;
//            this.title = title;
//        }
//    }

}
