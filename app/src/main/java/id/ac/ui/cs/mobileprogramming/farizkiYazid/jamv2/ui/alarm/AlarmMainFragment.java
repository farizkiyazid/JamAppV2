package id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.ui.alarm;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.R;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.adapter.AlarmsAdapter;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.model.Alarm;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.service.LoadAlarmsReceiver;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.service.LoadAlarmsService;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.util.AlarmUtils;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.view.DividerItemDecoration;
import id.ac.ui.cs.mobileprogramming.farizkiYazid.jamv2.view.EmptyRecyclerView;

public final class AlarmMainFragment extends Fragment implements LoadAlarmsReceiver.OnAlarmsLoadedListener {

    private LoadAlarmsReceiver mReceiver;
    private AlarmsAdapter mAdapter;

    public AlarmMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment.
     *
     * @return A new instance of fragment SecondFragment.
     */
    public static AlarmMainFragment newInstance() {
        return new AlarmMainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new LoadAlarmsReceiver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_alarm_main, container, false);

        final EmptyRecyclerView rv = v.findViewById(R.id.recycler);
        mAdapter = new AlarmsAdapter();
        rv.setEmptyView(v.findViewById(R.id.empty_view));
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new DividerItemDecoration(getContext()));
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setItemAnimator(new DefaultItemAnimator());

        final FloatingActionButton fab = v.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AlarmUtils.checkAlarmPermissions(getActivity());
            final Intent i = AddEditAlarmActivity.buildAddEditAlarmActivityIntent(getContext(), AddEditAlarmActivity.ADD_ALARM);
            startActivity(i);
        });

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter(LoadAlarmsService.ACTION_COMPLETE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
        LoadAlarmsService.launchLoadAlarmsService(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onAlarmsLoaded(ArrayList<Alarm> alarms) {
        mAdapter.setAlarms(alarms);
    }

}
