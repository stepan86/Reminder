package napatskyf.reminder.model;

import napatskyf.reminder.R;

/**
 * Created by SERVER 1C 8 hlib on 09.11.2016.
 */

public class ModelSeparator  implements  Item{
    public static final int TYPE_OVERDUE = R.string.separator_overdue;
    public static final int TYPE_TODAY = R.string.separator_today;
    public static final int TYPE_TOMORROW = R.string.separator_tomorrow;
    public static final int TYPE_FUTURE = R.string.separator_future;

    public  int type;

    public ModelSeparator(int type) {
        this.type = type;
    }

    @Override
    public boolean isTask() {
        return false;
    }
}
