package ch.heigvd.iict.sym.sym_labo4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;

import static ch.heigvd.iict.sym.wearcommon.Constants.BLUE_KEY;
import static ch.heigvd.iict.sym.wearcommon.Constants.GREEN_KEY;
import static ch.heigvd.iict.sym.wearcommon.Constants.RED_KEY;

public class WearSynchronizedActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = WearSynchronizedActivity.class.getSimpleName();

    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;

    private DataClient mDataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearsynchronized);

        red = findViewById(R.id.red);
        green = findViewById(R.id.green);
        blue = findViewById(R.id.blue);

        red.setOnSeekBarChangeListener(this);

        green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /* A IMPLEMENTER */

    /*
     *  Code utilitaire fourni
     */

    /**
     * Method used to update the background color of the activity
     * @param r The red composant (0...255)
     * @param g The green composant (0...255)
     * @param b The blue composant (0...255)
     */
    private void updateColor(int r, int g, int b) {
        View rootView = findViewById(android.R.id.content);
        rootView.setBackgroundColor(Color.argb(255, r,g,b));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        PutDataMapRequest putDataMapReqr = PutDataMapRequest.create("/red");
        PutDataMapRequest putDataMapReqg = PutDataMapRequest.create("/green");
        PutDataMapRequest putDataMapReqb = PutDataMapRequest.create("/blue");
        putDataMapReqr.getDataMap().putInt(RED_KEY, red.getProgress());
        putDataMapReqg.getDataMap().putInt(GREEN_KEY, red.getProgress());
        putDataMapReqb.getDataMap().putInt(BLUE_KEY, red.getProgress());
        PutDataRequest putDataReqr = putDataMapReqr.asPutDataRequest();
        PutDataRequest putDataReqg = putDataMapReqg.asPutDataRequest();
        PutDataRequest putDataReqb = putDataMapReqb.asPutDataRequest();
        Task<DataItem> putDataTaskr = mDataClient.putDataItem(putDataReqr);
        Task<DataItem> putDataTaskg = mDataClient.putDataItem(putDataReqg);
        Task<DataItem> putDataTaskb = mDataClient.putDataItem(putDataReqb);
    }
}
