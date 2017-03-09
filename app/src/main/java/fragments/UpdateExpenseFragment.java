package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import adapter.SiteAdapter;
import model.SiteInfo;
import umaahi.pravik.constructionnoteplus.R;

/**
 * Created by Vikrant on 3/4/2017.
 */

public class UpdateExpenseFragment extends Fragment {
    private DatabaseReference mPostReference;
    public DatabaseReference mDatabase;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private FirebaseAuth mAuth;

    String userId, name, email, img;
    EditText sitename,location;
    Button add;
    RecyclerView siteRecylerView;
    SiteAdapter siteAdapter;
    String siteNameStr,locationStr;
    private Button mSubmitButton;
    private static final String REQUIRED = "Required";
    protected SiteAdapter mAdapter;

    private LinearLayoutManager mManager;
    RecyclerView mRecyclerView;
    //LinearLayout noRecordLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("user-material").child(userId);
        View view = inflater.inflate(R.layout.update_site,container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylerview_site_update);
       // noRecordLayout= (LinearLayout) view.findViewById(R.id.norecordlayout);
        ImageButton edit = (ImageButton) view.findViewById(R.id.siteedit);
        sitename= (EditText) view.findViewById(R.id.sitename);
        location= (EditText) view.findViewById(R.id.sitelocation);
        mRecyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
               sitename.setEnabled(true);
                location.setEnabled(true);
            }
        });

        return view;
    }
    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("user-site").push().getKey();
        SiteInfo post = new SiteInfo(userId,key, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/user-site/" + userId + "/" + key, postValues);
        //childUpdates.put("/user-material/" + userId + "/" + key,postValues);
        mDatabase.updateChildren(childUpdates);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                SiteInfo post = dataSnapshot.getValue(SiteInfo.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //  Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // [START_EXCLUDE]
                Toast.makeText(getActivity(), "Failed to load post.",
                        Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        };
        mPostReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        // Listen for comments
        siteAdapter = new SiteAdapter(getActivity(), mPostReference);
        mRecyclerView.setAdapter(siteAdapter);
        System.out.println("sixe>>>>>"+siteAdapter.getItemCount());
        if(siteAdapter.getItemCount()==0)
        {
           // noRecordLayout.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.GONE);
        }
        else
        {
           // noRecordLayout.setGravity(Gravity.RIGHT);
            // mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Add Site");
    }
}
