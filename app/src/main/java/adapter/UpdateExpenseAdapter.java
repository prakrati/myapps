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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import fragments.AddExpense;
import model.SiteMaterial;
import umaahi.pravik.constructionnoteplus.R;
import view.UpdateExpenseViewHolder;

import static com.google.android.gms.internal.zzt.TAG;


/**
 * Created by Vikrant on 2/28/2017.
 */

public class UpdateExpenseAdapter extends RecyclerView.Adapter<UpdateExpenseViewHolder> {

    private Context mContext;
    private DatabaseReference mDatabaseReference,materialDBRefrence;
    private ChildEventListener mChildEventListener;

    private List<String> mCommentIds = new ArrayList<>();
    private List<SiteMaterial> mComments = new ArrayList<>();
    FragmentManager manager;
    FragmentTransaction fragmentTransaction;
    private DatabaseReference mPostReference;
    AddExpense frag;
    public Context context;

    public UpdateExpenseAdapter(final Context context, DatabaseReference ref) {
        mContext = context;
        mDatabaseReference = ref;


        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                SiteMaterial s = dataSnapshot.getValue(SiteMaterial.class);

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
                SiteMaterial newComment = dataSnapshot.getValue(SiteMaterial.class);
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
                SiteMaterial movedComment = dataSnapshot.getValue(SiteMaterial.class);
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
    public UpdateExpenseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.update_site_card_item, parent, false);
        context = parent.getContext();

        frag= new AddExpense();
        return new UpdateExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UpdateExpenseViewHolder holder, int position) {
        final SiteMaterial s = mComments.get(position);
        holder.authorView.setText(s.getMaterialName());
        holder.bodyView.setText(s.getMaterialPaidBy());

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

}