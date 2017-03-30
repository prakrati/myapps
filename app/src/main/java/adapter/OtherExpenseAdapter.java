package adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fragments.UpdateExpenseFragment;
import model.SiteOtherExpense;
import umaahi.pravik.constructionnoteplus.R;
import view.OtherExpenseViewHolder;

import static com.google.android.gms.internal.zzt.TAG;


/**
 * Created by Vikrant on 2/28/2017.
 */

public class OtherExpenseAdapter extends RecyclerView.Adapter<OtherExpenseViewHolder> {

    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference labourDBRefrence;
    private ChildEventListener mChildEventListener;

    private List<String> mCommentIds = new ArrayList<>();
    private List<SiteOtherExpense> mComments = new ArrayList<>();
    FragmentManager manager;
    FragmentTransaction fragmentTransaction;

    UpdateExpenseFragment frag;
    public Context context;
    private DatabaseReference mPostReference;
    public DatabaseReference mDatabase;
    private ValueEventListener mPostListener;
    private String mPostKey,userId,siteID,expId;
    private FirebaseAuth mAuth;
    String expName,expAmt,datestr;

    public OtherExpenseAdapter(final Context context, DatabaseReference ref) {
        mContext = context;
        mDatabaseReference = ref;


        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                SiteOtherExpense s = dataSnapshot.getValue(SiteOtherExpense.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mCommentIds.add(dataSnapshot.getKey());
                mComments.add(s);
                notifyItemInserted(mComments.size() - 1);
                // [END_EXCLUDE]
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                SiteOtherExpense newComment = dataSnapshot.getValue(SiteOtherExpense.class);
                String commentKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    // Replace with the new data
                    mComments.set(commentIndex, newComment);

                    // Update the RecyclerView
                    notifyItemChanged(commentIndex);
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + commentKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int commentIndex = mCommentIds.indexOf(commentKey);
                if (commentIndex > -1) {
                    // Remove data from the list
                    mCommentIds.remove(commentIndex);
                    mComments.remove(commentIndex);

                    // Update the RecyclerView
                    notifyItemRemoved(commentIndex);
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + commentKey);
                }
                // [END_EXCLUDE]
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.
                SiteOtherExpense movedComment = dataSnapshot.getValue(SiteOtherExpense.class);
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(mContext, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        ref.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;
    }

    @Override
    public OtherExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.otherexpensecard, parent, false);
        context = parent.getContext();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        siteID = SiteAdapter.SITE_ID;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        labourDBRefrence = FirebaseDatabase.getInstance().getReference()
                .child("user-labour").child(userId).child(siteID);
        context = parent.getContext();

        frag= new UpdateExpenseFragment();
        return new OtherExpenseViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final OtherExpenseViewHolder holder, int position) {
        final SiteOtherExpense s = mComments.get(position);
        holder.name.setText(s.getExpName());
        holder.amount.setText(s.getExpAmt());
        holder.date.setText(s.getExpDate());


        holder.edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something

                expName =   holder.name.getText().toString().trim();
                expAmt =  holder.amount.getText().toString().trim();
                datestr =  holder.date.getText().toString().trim();



                holder.name.setEnabled(true);
                holder.amount.setEnabled(true);
                holder.date.setEnabled(true);


                // holder.edit.setVisibility(View.GONE);
                holder.edit.setVisibility(View.GONE);
                holder.save.setVisibility(View.VISIBLE);
                holder.delete.setVisibility(View.VISIBLE);

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                // System.out.println(s.getMaterialId()+"==="+s.getSiteId());
                mDatabaseReference.child(s.getExpId()).removeValue();
            }
        });
        holder.save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                expId=s.getExpId();
                expName =   holder.name.getText().toString().trim();
                expAmt =  holder.amount.getText().toString().trim();
                datestr =  holder.date.getText().toString().trim();
                updateOtherExpense(userId, siteID,expName,datestr, expAmt);
                holder.name.setText(s.getExpName());
                holder.amount.setText(s.getExpAmt());

                holder.name.setEnabled(false);
                holder.amount.setEnabled(false);
                holder.date.setEnabled(false);
                holder.edit.setVisibility(View.VISIBLE);
                holder.save.setVisibility(View.GONE);
                holder.delete.setVisibility(View.GONE);

            }
        });
    }



    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);

        }
    }

    private void updateOtherExpense(String userId, String siteid, String expName, String date, String amt) {
        //  String key = mDatabase.child("user-labour").push().getKey();
        SiteOtherExpense post = new SiteOtherExpense(userId,siteID,expId,expName,date,amt);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/site/" + key, postValues);
        childUpdates.put("/user-otherExpense/" + userId + "/" +siteID + "/" +  expId, postValues);

        mDatabase.updateChildren(childUpdates);


    }
}
