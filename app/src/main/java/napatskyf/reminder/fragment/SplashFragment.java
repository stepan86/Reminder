
package napatskyf.reminder.fragment;


import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.concurrent.TimeUnit;

import napatskyf.reminder.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends Fragment {


    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SplashTask splashTask = new SplashTask();
        splashTask.execute();
       View view = inflater.inflate(R.layout.fragment_splash, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageSplash);
//
        Drawable drawable  = imageView.getDrawable();

        if(drawable instanceof Animatable){
            ((Animatable) drawable).start();
        }

        return view;
    }

    class SplashTask  extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            getActivity().getFragmentManager().popBackStack();

            return null;
        }
    }

}
