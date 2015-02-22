package fr.esir.maintasks;

import android.app.Activity;
import android.content.res.Resources;
import android.widget.ImageView;

/**
 * Created by Nicolas on 22/02/2015.
 */
public class ImageActivity {
    ImageView heating;
    private Activity activity;

    public ImageActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setImage(boolean heat) {
        heating = (ImageView) activity.findViewById(R.id.regul_state);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (heat)
                    heating.setImageResource(R.drawable.on);
                else
                    heating.setImageResource(R.drawable.off);
            }
        });
    }
}
