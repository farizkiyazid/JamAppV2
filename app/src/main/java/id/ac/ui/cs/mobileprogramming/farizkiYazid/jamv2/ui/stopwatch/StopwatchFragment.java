package id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.ui.stopwatch;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.R;

public class StopwatchFragment extends Fragment implements View.OnClickListener {

    public static Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    public StopwatchFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of this fragment
     * @return A new instance of fragment FirstFragment.
     */
    public static StopwatchFragment newInstance() {
        return new StopwatchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Right", "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Right", "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);

        chronometer = view.findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 600000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(getActivity(), "Sudah 10 menit!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button startButton = view.findViewById(R.id.startButton);
        Button pauseButton = view.findViewById(R.id.pauseButton);
        Button resetButton = view.findViewById(R.id.resetButton);
        startButton.setOnClickListener(this);
        pauseButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startButton:
                startChronometer(v);
                break;
            case R.id.pauseButton:
                pauseChronometer(v);
                break;
            case R.id.resetButton:
                resetChronometer(v);
                break;
        }
    }

    private void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    private void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    private void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

}