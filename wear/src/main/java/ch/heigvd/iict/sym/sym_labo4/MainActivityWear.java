package ch.heigvd.iict.sym.sym_labo4;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wear.widget.BoxInsetLayout;
import android.support.wearable.activity.WearableActivity;

import com.bozapro.circularsliderrange.CircularSliderRange;
import com.bozapro.circularsliderrange.ThumbEvent;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import ch.heigvd.iict.sym.sym_labo4.widgets.CircularSliderRangeFixed;

import static ch.heigvd.iict.sym.wearcommon.Constants.BLUE_KEY;
import static ch.heigvd.iict.sym.wearcommon.Constants.GREEN_KEY;
import static ch.heigvd.iict.sym.wearcommon.Constants.RED_KEY;

public class MainActivityWear extends WearableActivity implements
        CircularSliderRange.OnSliderRangeMovedListener, DataClient.OnDataChangedListener {

    private static final String TAG = MainActivityWear.class.getSimpleName();
    private static final int ANGLE_OFFSET = 90;

    private BoxInsetLayout mContainerView           = null;

    private CircularSliderRangeFixed redSlider      = null;
    private CircularSliderRangeFixed greenSlider    = null;
    private CircularSliderRangeFixed blueSlider     = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wear);
        setAmbientEnabled();

        //link to GUI
        this.mContainerView = findViewById(R.id.container);
        this.redSlider      = findViewById(R.id.circular_red);
        this.greenSlider    = findViewById(R.id.circular_green);
        this.blueSlider     = findViewById(R.id.circular_blue);

        //events
        this.redSlider.setOnSliderRangeMovedListener(this);
        this.greenSlider.setOnSliderRangeMovedListener(this);
        this.blueSlider.setOnSliderRangeMovedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }


    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {
        for (DataEvent event : dataEventBuffer) {
            DataItem item = event.getDataItem();
            DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
            if (item.getUri().getPath().compareTo("/red") == 0) {
                redSlider.setEndAngle(convertRGBValueToEndAngle(dataMap.getInt(RED_KEY)));
            } else if (item.getUri().getPath().compareTo("/green") == 0) {
                greenSlider.setEndAngle(convertRGBValueToEndAngle(dataMap.getInt(GREEN_KEY)));
            } else if (item.getUri().getPath().compareTo("/blue") == 0) {
                blueSlider.setEndAngle(convertRGBValueToEndAngle(dataMap.getInt(BLUE_KEY)));
            }
        }
    }


    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateBackgroundColor();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateBackgroundColor();
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        if (isAmbient()) {
            mContainerView.setBackgroundColor(getResources().getColor(android.R.color.black));
        } else {
            int r = convertEndAngleToRGBComponent(redSlider.getEndAngle());
            int g = convertEndAngleToRGBComponent(greenSlider.getEndAngle());
            int b = convertEndAngleToRGBComponent(blueSlider.getEndAngle()); //use real color...
            mContainerView.setBackgroundColor(Color.argb(255, r,g,b));
        }
    }

    @Override public void onStartSliderMoved(double pos) { /* NOTHING TO DO */ }
    @Override public void onEndSliderMoved(double pos) { /* NOTHING TO DO */ }
    @Override public void onStartSliderEvent(ThumbEvent event) { /* NOTHING TO DO */ }
    @Override public void onEndSliderEvent(ThumbEvent event) {
        //one of the slider was moved
        //DO SOMETHING
    }

    /**
     * Method used to convert a n angle into the corresponding RGB color component (r, g or b)
     * @param endAngle The angle in degree 0-359
     * @return The color component 0-255
     */
    private int convertEndAngleToRGBComponent(double endAngle) {
        return (int) Math.round(255 * ((endAngle + ANGLE_OFFSET) % 360) / 360.0);
    }

    /**
     *  Method used to convert a RGB color component (r, g or b) into the corresponding angle
     *  for the slider (endAngle)
     * @param colorComponent The color component 0-255
     * @return The angle in degree 0-359
     */
    private double convertRGBValueToEndAngle(int colorComponent) {
        return ((((double)colorComponent)/ 255.0) * 360.0) - ANGLE_OFFSET;
    }

}
