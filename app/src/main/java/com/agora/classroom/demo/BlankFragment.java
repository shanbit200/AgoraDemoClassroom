package com.agora.classroom.demo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btn_broadcast,btn_groupchat,btn_onetoone;

    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        btn_broadcast = view.findViewById(R.id.btn_broadcast);
        btn_groupchat = view.findViewById(R.id.btn_groupchat);
        btn_onetoone = view.findViewById(R.id.btn_onetoone);
        btn_broadcast.setOnClickListener(this);
        btn_groupchat.setOnClickListener(this);
        btn_onetoone.setOnClickListener(this);

        return  view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_broadcast:
                Navigation.findNavController(view).navigate(R.id.action_blankFragment_to_broadCastFragment, null);
                break;
            case R.id.btn_groupchat:
                Navigation.findNavController(view).navigate(R.id.action_blankFragment_to_groupChatFragment, null);
                break;
            case R.id.btn_onetoone:
                Navigation.findNavController(view).navigate(R.id.action_blankFragment_to_ontoOneChatFragment, null);
                break;
        }
    }
}
