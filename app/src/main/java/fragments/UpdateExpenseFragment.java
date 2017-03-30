package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import adapter.LabourAdapter;
import adapter.MaterialAdapter;
import adapter.OtherExpenseAdapter;
import model.SiteInfo;
import model.SiteLabour;
import model.SiteMaterial;
import model.SiteOtherExpense;
import umaahi.pravik.constructionnoteplus.R;

/**
 * Created by Vikrant on 3/4/2017.
 */

public class UpdateExpenseFragment extends Fragment {
    private DatabaseReference materialDBrefrence;
    private DatabaseReference labourDBrefrence;
    private DatabaseReference otherExpDBRef;
    public DatabaseReference mDatabase;
    private ValueEventListener mPostListener;
    private String mPostKey;
    private FirebaseAuth mAuth;

    String userId, name, email, img;
    EditText sitename,location;
    Button add;
    RecyclerView siteRecylerView;
    MaterialAdapter siteAdapter;
    String siteNameStr,locationS;
    private Button mSubmitButton;
    private static final String REQUIRED = "Required";
    protected MaterialAdapter mAdapter;
    protected LabourAdapter labourAdapter;

    private LinearLayoutManager mManager;
    RecyclerView mRecyclerView;
    //LinearLayout noRecordLayout;
    String[] arrayForSpinner = { "Material", "Labour", "Other Expense"};
    Spinner expenseSelection;
    public static int iCurrentSelection;
    String siteID,siteName,locationStr,option;
    public static String expenseType="material";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        option=getArguments().getString("option");
        siteName = getArguments().getString("sitename");
        siteID = getArguments().getString("siteid");
        locationS = getArguments().getString("location");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        materialDBrefrence = FirebaseDatabase.getInstance().getReference()
                .child("user-material").child(userId).child(siteID);

        View view = inflater.inflate(R.layout.update_site,container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recylerview_site_update);
        expenseSelection = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, arrayForSpinner);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseSelection.setAdapter(adp1);
       // noRecordLayout= (LinearLayout) view.findViewById(R.id.norecordlayout);
        final ImageButton edit = (ImageButton) view.findViewById(R.id.siteedit);
        final ImageButton save = (ImageButton) view.findViewById(R.id.savesite);
        sitename= (EditText) view.findViewById(R.id.sitename);
        location= (EditText) view.findViewById(R.id.sitelocation);
        sitename.setText(siteName);
        location.setText(locationS);
        mRecyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);
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
                save.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
            }
        });
        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // do something
                updateSite(userId, sitename.getText().toString(), location.getText().toString(), "000");
                sitename.setEnabled(false);
                location.setEnabled(false);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
            }
        });
        expenseSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iCurrentSelection = expenseSelection.getSelectedItemPosition();
                //((TextView)adapterView.getChildAt(0)).setTextColor(Color.rgb(255, 255, 255));
                System.out.println("position >>" + iCurrentSelection);
                if (iCurrentSelection == 0) {
                    expenseType="material";
                    //getMaterial();

                } else if (iCurrentSelection == 1) {
                    expenseType="labour";
                    // getLabour();
                } else if (iCurrentSelection == 2) {
                    expenseType="other";
                }
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        if(iCurrentSelection==0) {
                            System.out.println("%%%%%material");
                            SiteMaterial post = dataSnapshot.getValue(SiteMaterial.class);
                        }
                        else  if(iCurrentSelection==1)
                        {
                            System.out.println("%%%%%labour");
                            SiteLabour post = dataSnapshot.getValue(SiteLabour.class);
                        }
                        else
                        {
                            System.out.println("%%%%%labour");
                            SiteOtherExpense post = dataSnapshot.getValue(SiteOtherExpense.class);
                        }

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
                materialDBrefrence.addValueEventListener(postListener);
                // [END post_value_event_listener]

                // Keep copy of post listener so we can remove it when app stops
                mPostListener = postListener;

                // Listen for comments
                System.out.println("expenseType>>"+expenseType);
                if(iCurrentSelection==0) {


                    // Listen for comments
                    siteAdapter = new MaterialAdapter(getActivity(), materialDBrefrence);
                    siteAdapter.notifyDataSetChanged();
                    mRecyclerView.invalidate();
                    mRecyclerView.setAdapter(siteAdapter);
                }
                else  if(iCurrentSelection==1) {

                    labourDBrefrence = FirebaseDatabase.getInstance().getReference()
                            .child("user-labour").child(userId).child(siteID);
                    LabourAdapter siteAdapter = new LabourAdapter(getActivity(), labourDBrefrence);
                    siteAdapter.notifyDataSetChanged();
                    mRecyclerView.invalidate();
                    mRecyclerView.setAdapter(siteAdapter);
                }
                else   {

                    otherExpDBRef = FirebaseDatabase.getInstance().getReference()
                            .child("user-otherExpense").child(userId).child(siteID);
                    OtherExpenseAdapter siteAdapter = new OtherExpenseAdapter(getActivity(), otherExpDBRef);
                    siteAdapter.notifyDataSetChanged();
                    mRecyclerView.invalidate();
                    mRecyclerView.setAdapter(siteAdapter);
                }
                System.out.println("sixe>>>>>"+siteAdapter.getItemCount());
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        return view;
    }
    private void updateSite(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        SiteInfo post = new SiteInfo(userId,siteID, username, title, body);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/user-site/" + userId + "/" + siteID, postValues);
        //childUpdates.put("/user-material/" + userId + "/" + key,postValues);
        mDatabase.updateChildren(childUpdates);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    @Override
    public void onStart() {
        super.onStart();

        // Add value event listener to the post
        // [START post_value_event_listener]

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

}
