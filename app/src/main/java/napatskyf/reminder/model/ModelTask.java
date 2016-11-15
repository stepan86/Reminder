package napatskyf.reminder.model;

import java.util.Date;

import napatskyf.reminder.R;

/**
 * Created by SERVER 1C 8 hlib on 24.10.2016.
 */

public class ModelTask  implements Item {
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    public static final String PRIORITY_LEVEL[] = {"Low Priority","Normal Priority","High Priority"};

    public static final int STATUS_OVERDUE = 0;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;

    private String mTitle;
    private long mDate;
    private int mPriority;
    private int mStatus;
    private long mTimeStamp;
    public int dateStatus;

    @Override
    public boolean isTask() {
        return true;
    }

    public ModelTask(){
        this.mStatus = -1;
        this.mTimeStamp = new Date().getTime();
    }

    public ModelTask(String mTitle, long mDate,int mPriority,int mStatus,long timeStamp) {
        this.mTitle = mTitle;
        this.mDate = mDate;
        this.mPriority = mPriority;
        this.mStatus = mStatus;
        this.mTimeStamp = timeStamp;

    }

    public String getTitle() {
        return mTitle;
    }

    public long getDate() {
        return mDate;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDate(long mDate) {
        this.mDate = mDate;
    }

    public int getPriority() {
        return mPriority;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setPriority(int mPriority) {
        this.mPriority = mPriority;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public int getPriorityColor(){
        switch (getPriority()) {
            case PRIORITY_HIGH:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_high;
                } else {
                    return R.color.priority_high_selected;
                }
            case PRIORITY_NORMAL:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_normal;
                } else {
                    return R.color.priority_normal_selected;
                }
            case PRIORITY_LOW:
                if (getStatus() == STATUS_CURRENT || getStatus() == STATUS_OVERDUE) {
                    return R.color.priority_low;
                } else {
                    return R.color.priority_low_selected;
                }
            default:
                return 0;
        }
    }

    public long getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(long mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

}
