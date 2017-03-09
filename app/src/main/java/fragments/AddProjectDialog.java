package fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import model.UserData;
import umaahi.pravik.constructionnoteplus.R;

import static com.google.android.gms.internal.zzt.TAG;


/**
 * Created by Vikrant on 3/6/2017.
 */

public class AddProjectDialog extends DialogFragment
{
    //private View pic;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String userId, siteNameStr,locationStr;
    private DatabaseReference mPostReference;

    private ValueEventListener mPostListener;
    EditText project,location;
    SiteAdapter siteAdapter;
    private static final String REQUIRED = "Required";
    public AddProjectDialog()
    {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
          mDatabase = FirebaseDatabase.getInstance().getReference();
        View view = getActivity().getLayoutInflater().inflate(R.layout.add_site_dialog, new LinearLayout(getActivity()), false);

        // Retrieve layout elements
         project = (EditText) view.findViewById(R.id.project);
         location = (EditText) view.findViewById(R.id.location);
        Button save = (Button) view.findViewById(R.id.save);
        Button cancel = (Button) view.findViewById(R.id.cancel);

        // Set values
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
                //((FragmentDialog)getActivity()).showDialog(); mDatabase.child("user").child(userId).addListenerForSingleValueEvent(
                addSite();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // When button is clicked, call up to owning activity.
               dismiss();
            }
        });

        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setContentView(view);
        return builder;
    }
    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("user-site").push().getKey();
        SiteInfo post = new SiteInfo(userId,key, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/user-site/" + userId + "/" + key, postValues);
        childUpdates.put("/user-material/" + userId + "/" + key,postValues);
        mDatabase.updateChildren(childUpdates);


    }
    @Override
    public void onStop() {
        super.onStop();

        // Remove post value event listener
        if (mPostListener != null) {
            mPostReference.removeEventListener(mPostListener);
        }

        // Clean up comments listener
//        siteAdapter.cleanupListener();
    }


    private void setEditingEnabled(boolean enabled) {
        project.setEnabled(enabled);
        location.setEnabled(enabled);
        if (enabled) {
           // mSubmitButton.setVisibility(View.VISIBLE);
        } else {
           // mSubmitButton.setVisibility(View.GONE);
        }
    }

    public void addSite() {

        siteNameStr = project.getText().toString();
        locationStr = location.getText().toString();

        if (TextUtils.isEmpty(siteNameStr)) {
            project.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(locationStr)) {
            location.setError(REQUIRED);
            return;
        }
        setEditingEnabled(false);
        Toast.makeText(getActivity(), "saving...", Toast.LENGTH_SHORT).show();

        mDatabase.child("user").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        UserData user = dataSnapshot.getValue(UserData.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(getActivity(),
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Write new post
                            writeNewPost(userId, siteNameStr, locationStr, "000");
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                       dismiss();
                        // finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });


        //Storing values to firebase


    }
}
