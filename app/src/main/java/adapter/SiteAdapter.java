package adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import fragments.UpdateExpenseFragment;
import fragments.ViewExpenseFragment;
import model.SiteInfo;
import umaahi.pravik.constructionnoteplus.R;
import view.SiteViewHolder;

import static com.google.android.gms.internal.zzt.TAG;


/**
 * Created by Vikrant on 2/28/2017.
 */

public class SiteAdapter extends RecyclerView.Adapter<SiteViewHolder> {

    private Context mContext;
    private DatabaseReference mDatabaseReference,materialDBRefrence;
    private ChildEventListener mChildEventListener;

    private List<String> mCommentIds = new ArrayList<>();
    private List<SiteInfo> mComments = new ArrayList<>();
    FragmentManager manager;
    FragmentTransaction fragmentTransaction;
    private DatabaseReference mPostReference;
    AddExpense addExpenseFragment;
    UpdateExpenseFragment updateExpenseFrag;
    ViewExpenseFragment viewExpenseFragment;
     public Context context;
    public static  String SITE_ID ;
    public static  String OPTION_CHK ;
    public SiteAdapter(final Context context, DatabaseReference ref) {
        mContext = context;
        mDatabaseReference = ref;


        // Create child event listener
        // [START child_event_listener_recycler]
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                SiteInfo s = dataSnapshot.getValue(SiteInfo.class);

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
                SiteInfo newComment = dataSnapshot.getValue(SiteInfo.class);
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
                SiteInfo movedComment = dataSnapshot.getValue(SiteInfo.class);
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
    public SiteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.site_card_item, parent, false);
         context = parent.getContext();

        addExpenseFragment= new AddExpense();
        updateExpenseFrag=new UpdateExpenseFragment();
        viewExpenseFragment=new ViewExpenseFragment();
        return new SiteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SiteViewHolder holder, int position) {
       final SiteInfo s = mComments.get(position);
        holder.authorView.setText(s.getSiteName());
        holder.bodyView.setText(s.getLocation());
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.site_options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:


                                 manager = ((AppCompatActivity)context).getSupportFragmentManager();
                                Bundle args = new Bundle();
                                SITE_ID=s.getSiteId();
                                args.putString("siteid", s.getSiteId());
                                args.putString("sitename", s.getSiteName());
                                addExpenseFragment.setArguments(args);
                                 fragmentTransaction = manager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, addExpenseFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                                break;
                            case R.id.menu2:
                                //handle menu2 click

                                mDatabaseReference.child(s.getSiteId()).removeValue();

                               /* DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query applesQuery = ref.child("user-material").orderByChild(s.getSiteId());

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });*/

                               // mPostReference.removeValue();

                                break;
                            case R.id.menu3:

                                OPTION_CHK="update";
                                manager = ((AppCompatActivity)context).getSupportFragmentManager();
                                Bundle args1 = new Bundle();
                                SITE_ID=s.getSiteId();
                                args1.putString("option", "edit");
                                args1.putString("siteid", s.getSiteId());
                                args1.putString("sitename", s.getSiteName());
                                args1.putString("location", s.getLocation());
                                updateExpenseFrag.setArguments(args1);
                                fragmentTransaction = manager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, updateExpenseFrag);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                                break;
                            case R.id.menu4:
                                //handle menu2 click
                                OPTION_CHK="view and share";
                                manager = ((AppCompatActivity)context).getSupportFragmentManager();
                                Bundle args2 = new Bundle();
                                SITE_ID=s.getSiteId();
                                args2.putString("option", "share");
                                args2.putString("siteid", s.getSiteId());
                                args2.putString("sitename", s.getSiteName());
                                args2.putString("location", s.getLocation());
                                viewExpenseFragment.setArguments(args2);
                                fragmentTransaction = manager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, viewExpenseFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

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

}