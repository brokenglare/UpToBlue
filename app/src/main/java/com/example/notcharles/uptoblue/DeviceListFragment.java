package com.example.notcharles.uptoblue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.notcharles.uptoblue.dummy.DummyContent;
import com.example.notcharles.uptoblue.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DeviceListFragment extends Fragment implements AbsListView.OnItemClickListener {

    private ArrayList<Device> deviceList;
    private OnListFragmentInteractionListener mListener;
    private static BluetoothAdapter myAdapter;
    private AbsListView mListView;
    private ArrayAdapter<Device> mAdapter;

    public static DeviceListFragment newInstance(BluetoothAdapter adapter) {
        DeviceListFragment fragment = new DeviceListFragment();
        myAdapter = adapter;
        return fragment;
    }

    private final BroadcastReceiver bReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Device newDevice = new Device(device.getName(), device.getAddress(),
                        false);
                mAdapter.add(newDevice);
            }
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeviceListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("DEVICELIST", "Super called for DeviceListenerFragment onCreate\n");
        deviceList = new ArrayList<Device>();

        // Scan for devices and add to list


        // If there are no devices, add an empty device to be displayed.
        if (deviceList.size() == 0) {
            deviceList.add(new Device("No Devices Found", "", false));
        }

        Log.d("DEVICELIST", "DeviceList populated\n");

        mAdapter = new DeviceListAdapter(getActivity(), deviceList, myAdapter);

        Log.d("DEVICELIST", "Adapter created\n");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        ToggleButton scan = (ToggleButton)view.findViewById(R.id.DiscoverButton);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        mListView.setOnItemClickListener(this);

        scan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                if(b) {
                    mAdapter.clear();
                    getActivity().registerReceiver(bReceiver, filter);
                    myAdapter.startDiscovery();
                }
                else {
                    getActivity().unregisterReceiver(bReceiver);
                    myAdapter.cancelDiscovery();
                }
            }
        });

        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnListFragmentInteractionListener) context;
        } catch(ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("DEVICELIST", "onItemClick position: " + position + " id: " + id + "name: "
                + deviceList.get(position).getDeviceName() + "\n");
        if (mListener != null) {
            mListener.onListFragmentInteraction(deviceList.get(position).getDeviceName());
        }
    }

    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String id);
    }
}
