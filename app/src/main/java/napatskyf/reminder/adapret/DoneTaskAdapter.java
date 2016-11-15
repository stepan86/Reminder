package napatskyf.reminder.adapret;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import napatskyf.reminder.MainActivity;
import napatskyf.reminder.R;
import napatskyf.reminder.Utils;
import napatskyf.reminder.fragment.DoneTaskFragment;
import napatskyf.reminder.model.Item;
import napatskyf.reminder.model.ModelTask;

/**
 * Created by SERVER 1C 8 hlib on 25.10.2016.
 */

public class DoneTaskAdapter extends TaskAdapter {
DoneTaskFragment taskFragment;
    public DoneTaskAdapter(DoneTaskFragment taskFragment) {
        this.taskFragment = taskFragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_task, parent, false);
        TextView title = (TextView) view.findViewById(R.id.tvTaskTitle);
        TextView date = (TextView) view.findViewById(R.id.tvTaskDate);
        CircleImageView priority = (CircleImageView) view.findViewById(R.id.cvTaskPriority);
        return new TaskViewHolder(view, title, date, priority);
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
            itemView.setBackgroundColor(resources.getColor(R.color.gray_200));
            taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_disabled_material_light));
            taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_disabled_material_light));
            taskViewHolder.prioriry.setColorFilter(resources.getColor(task.getPriorityColor()));
            taskViewHolder.prioriry.setImageResource(R.drawable.ic_check_circle_white_48dp);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    android.os.Handler handler = new android.os.Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            taskFragment.removeTaskDialog(taskViewHolder.getLayoutPosition());
                        }
                    },1000);
                    return true;
                }
            });

            taskViewHolder.prioriry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    task.setStatus(ModelTask.STATUS_CURRENT);
                    ((MainActivity) taskFragment.getActivity()).dbHelper.update().status(task.getTimeStamp(),ModelTask.STATUS_CURRENT);
                    itemView.setBackgroundColor(resources.getColor(R.color.gray_50));
                    taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
                    taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
                    taskViewHolder.prioriry.setColorFilter(resources.getColor(task.getPriorityColor()));

                    ObjectAnimator flipIn = ObjectAnimator.ofFloat(taskViewHolder.prioriry, "rotationY", 180f, 0f);
                    taskViewHolder.prioriry.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);
                    flipIn.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (task.getStatus() != ModelTask.STATUS_DONE) {
                                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView, "translationX", 0f, -itemView.getWidth());
                                ObjectAnimator translationBackX = ObjectAnimator.ofFloat(itemView, "translationX", -itemView.getWidth(), 0f);


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
                                        taskFragment.moveTask(task);
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
}
