package cn.ucai.fulicenter.views;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import cn.ucai.filicenter.R;
import cn.ucai.fulicenter.utils.MFGT;


/**
 * Created by clawpo on 16/8/3.
 */
public class DisplayUtils {
    public static void initBack(final Activity activity){
        activity.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.finish(activity);
            }
        });
    }

    public static void initBackWithTitle(final Activity activity, final String title){
        initBack(activity);
        ((TextView)activity.findViewById(R.id.tvTitle)).setText(title);
    }
}
