
package napatskyf.reminder.fragment;


import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import napatskyf.reminder.MainActivity;
import napatskyf.reminder.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {

    public MainActivity activity;

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SplashTask splashTask = new SplashTask();
        splashTask.execute();
        final View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageSplash);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runAnimations(view);
            }
        });
        activity = (MainActivity) getActivity();
        runAnimations(view);
//

        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    class SplashTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(6);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getActivity().getFragmentManager().popBackStack();

            return null;
        }
    }


    private void runAnimations(View view) {
        Animation a = AnimationUtils.loadAnimation(activity, R.anim.translatealpha);
        a.reset();
        TextView tv = (TextView) view.findViewById(R.id.firstTextView);
        tv.clearAnimation();
        tv.startAnimation(a);

        a = AnimationUtils.loadAnimation(activity, R.anim.translatealpha);
        a.reset();
        tv = (TextView) view.findViewById(R.id.secondTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
//
        a = AnimationUtils.loadAnimation(activity, R.anim.scalerotate);
        a.reset();
        tv = (TextView) view.findViewById(R.id.thirdTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
//
        a = AnimationUtils.loadAnimation(activity, R.anim.translatealpha);
        a.reset();
        tv = (TextView) view.findViewById(R.id.fourthTextView);
        tv.clearAnimation();
        tv.startAnimation(a);

        a = AnimationUtils.loadAnimation(activity, R.anim.scalerotate);
        a.reset();
        tv = (TextView) view.findViewById(R.id.fifthTextView);
        tv.clearAnimation();
        tv.startAnimation(a);
    }


}
