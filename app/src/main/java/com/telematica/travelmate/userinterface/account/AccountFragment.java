package com.telematica.travelmate.userinterface.account;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telematica.travelmate.R;
import com.telematica.travelmate.model.User;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountFragment extends Fragment {

    TextView userNameTextView;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        userNameTextView = (TextView) view.findViewById(R.id.usernameTextView);
        userNameTextView.setText(User.getInstance().getName());
        return view;
    }

}
