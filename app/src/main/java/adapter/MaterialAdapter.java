package adapter;

import android.content.Context;
import android.content.Intent;
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
import model.SiteMaterial;
import umaahi.pravik.constructionnoteplus.R;
import view.UpdateExpenseViewHolder;

import static adapter.SiteAdapter.OPTION_CHK;
import static com.google.android.gms.internal.zzt.TAG;
import static fragments.UpdateExpenseFragment.iCurrentSelection;


/**
 * Created by Vikrant on 2/28/2017.
 */

public class MaterialAdapter extends RecyclerView.Adapter<UpdateExpenseViewHolder> {

    private Context mContext;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference materialDBrefrence;
    private ChildEventListener mChildEventListener;

    private List<String> ids = new ArrayList<>();
    private List<String> mCommentIds = new ArrayList<>();
    private List<SiteMaterial> materialList = new ArrayList<>();
   // private List<SiteLabour> labourList = new ArrayList<>();
    FragmentManager manager;
    FragmentTransaction fragmentTransaction;
    String labName,LabQty,labPrice,labtotal,labPaidBy,labourID;
    UpdateExpenseFragment frag;
    public Context context;
    private DatabaseReference mPostReference;
    public DatabaseReference mDatabase;
    private ValueEventListener mPostListener;
    private String mPostKey,userId,siteID,materialId;
    private FirebaseAuth mAuth;
    String materialName,materialQty,materialPrice,materialTotal,materialPaidBy,dateStr;
    public MaterialAdapter(final Context context, DatabaseReference ref) {
        mContext = context;
        mDatabaseReference = ref;
        System.out.println("vvvvvvvv>>"+iCurrentSelection);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                SiteMaterial s = dataSnapshot.getValue(SiteMaterial.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mCommentIds.add(dataSnapshot.getKey());
                materialList.add(s);
                notifyItemInserted(materialList.size() - 1);
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
                    materialList.set(commentIndex, newComment);

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
                    materialList.remove(commentIndex);

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
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        siteID = SiteAdapter.SITE_ID;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        materialDBrefrence = FirebaseDatabase.getInstance().getReference()
                .child("user-material").child(userId).child(siteID);
        context = parent.getContext();

        frag= new UpdateExpenseFragment();
        return new UpdateExpenseViewHolder(view);

    }


    @Override
    public void onBindViewHolder(final UpdateExpenseViewHolder holder, int position) {


            final SiteMaterial s = materialList.get(position);
            holder.name.setText(s.getMaterialName());
            holder.paidBy.setText(s.getMaterialPaidBy());
            holder.total.setText(s.getMaterialTotalCost());
            holder.qty.setText(s.getMaterialQty());
            holder.price.setText(s.getMaterialUnitPrice());
        holder.date.setText(s.getMaterialDate());
if(OPTION_CHK.equalsIgnoreCase("view and share"))
{
    holder.edit.setBackgroundResource(R.drawable.share);
}
            holder.edit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // do something
                    materialName = holder.name.getText().toString().trim();
                    materialPaidBy = holder.paidBy.getText().toString().trim();
                    materialTotal = holder.total.getText().toString();
                    materialQty = holder.qty.getText().toString();
                    materialPrice = holder.price.getText().toString().trim();
                    dateStr = holder.date.getText().toString().trim();
                    if(OPTION_CHK.equalsIgnoreCase("view and share"))
                    {
                        shareApp(materialName,dateStr,materialQty,materialPrice,materialTotal,materialPaidBy);
                    }
                    else {


                        holder.name.setEnabled(true);
                        holder.paidBy.setEnabled(true);
                        holder.total.setEnabled(false);
                        holder.qty.setEnabled(true);
                        holder.price.setEnabled(true);
                        holder.date.setEnabled(true);

                        holder.edit.setVisibility(View.GONE);
                        holder.save.setVisibility(View.VISIBLE);
                        holder.delete.setVisibility(View.VISIBLE);
                    }

                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // do something
                    System.out.println(s.getMaterialId()+"==="+s.getSiteId());
                    mDatabaseReference.child(s.getMaterialId()).removeValue();
                }
            });
            holder.save.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    // do something
                    materialId=s.getMaterialId();
                    materialName =   holder.name.getText().toString().trim();
                    materialPaidBy =  holder.paidBy.getText().toString().trim();

                    materialQty =   holder.qty.getText().toString();
                    materialPrice =  holder.price.getText().toString().trim();
                    dateStr= holder.date.getText().toString().trim();
                    float tot=Float.parseFloat(materialQty)*Float.parseFloat(materialPrice);
                    materialTotal=String.valueOf(tot);
                    holder.total.setText(materialTotal);
                    updateMaterial(userId, siteID,  materialName,dateStr, materialPrice, materialQty, materialPaidBy, materialTotal);

                    holder.name.setText(s.getMaterialName());
                    holder.paidBy.setText(s.getMaterialPaidBy());
                    holder.total.setText(s.getMaterialTotalCost());
                    holder.qty.setText(s.getMaterialQty());
                    holder.price.setText(s.getMaterialUnitPrice());
                    holder.name.setEnabled(false);
                    holder.paidBy.setEnabled(false);
                    holder.total.setEnabled(false);
                    holder.qty.setEnabled(false);
                    holder.price.setEnabled(false);
                    holder.date.setEnabled(false);
                    holder.edit.setVisibility(View.VISIBLE);
                    holder.save.setVisibility(View.GONE);
                    holder.delete.setVisibility(View.GONE);


                }
            });



        }





    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);

        }
    }
    private void updateMaterial(String userId, String siteid, String matNAme, String date, String price, String Qty, String paidby, String total) {
       // String key = mDatabase.child("user-material").push().getKey();
        SiteMaterial post = new SiteMaterial(userId, siteid, materialId, matNAme, date, price, Qty, paidby, total);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/site/" + key, postValues);
        childUpdates.put("/user-material/" + userId + "/" + siteID + "/" + materialId, postValues);

        mDatabase.updateChildren(childUpdates);


    }
    private void shareApp(String material,String date,String qty,String price,String total, String paidby)
    {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Construction Notes+");
            String sAux = "\n"+"Material Name= "+material+" Date="+date+"\n\n";
            sAux = sAux + " Qty="+qty+ " Price="+price+" Total="+total+" Paid By="+paidby+ "\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            mContext.startActivity(Intent.createChooser(i, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }
}
