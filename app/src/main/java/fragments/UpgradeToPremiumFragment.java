package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import umaahi.pravik.constructionnoteplus.R;

/**
 * Created by Vikrant on 3/27/2017.
 */

public class UpgradeToPremiumFragment extends Fragment {
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String userId,name,email,img;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId=user.getUid();
        name=user.getDisplayName();
        email=user.getEmail();
        img=user.getPhotoUrl().toString();
        // String s=user.get

        //return inflater.inflate(R.layout.myprofile_layout, container, false);
        View view = inflater.inflate(R.layout.upgrade_layout,container, false);

       // final TextView problem=(TextView)view.findViewById(R.id.problem);
        /*Button sendBtn=(Button) view.findViewById(R.id.send);
        sendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
               // writeNewPost(email,problem.getText().toString());
            }
        });*/
        //  member.setText(s);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles

    }
}
