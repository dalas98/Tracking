package id.hansjr.tracking.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.messaging.FirebaseMessaging;
import id.hansjr.tracking.R;
import id.hansjr.tracking.helper.SharedPrefManager;

public class AccountFragment extends Fragment {
    Button btnLogout;
    SharedPrefManager sharedPrefManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        sharedPrefManager = new SharedPrefManager(this.getActivity());
        btnLogout = view.findViewById(R.id.logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_LOGIN_VALIDATE,false);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(sharedPrefManager.getSpUsername());
                Intent i = new Intent(AccountFragment.this.getActivity(),LoginActivity.class);
                startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });
        return view;
    }
}
