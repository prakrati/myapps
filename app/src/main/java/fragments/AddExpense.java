package fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import model.SiteInfo;
import model.SiteLabour;
import model.SiteMaterial;
import model.SiteOtherExpense;
import model.UserData;
import umaahi.pravik.constructionnoteplus.R;

/**
 * Created by Vikrant on 3/7/2017.
 */

public class AddExpense extends Fragment implements View.OnClickListener{
    long startDate;
    long endDate;
    int lastValidYear;
    int lastValidMonth;
    int lastValidDayOfMonth;
    Calendar cal = Calendar.getInstance();
    private Calendar lastDate = Calendar.getInstance();
    private static final String TAG = "AddSiteActivity";
    private DatabaseReference mPostReference, mPostReference2;
    ;
    EditText sitename, location;

    Spinner expenseSelection;
    String defaultTextForSpinner = "Select Expense";
    String[] arrayForSpinner = { "Material", "Labour", "Other Expense"};
    int iCurrentSelection;
    LinearLayout materialExpense, labourExpense, otherExpense;
    ///////////
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    String siteID,siteName;
    String userId, name, email, img, dateStr;
    String siteNameStr, locationStr;
    Toolbar toolbar;
    private FloatingActionButton mSubmitButton;
    private static final String REQUIRED = "Required";
    ArrayList<SiteInfo> spacecrafts = new ArrayList<>();
    EditText materialName, materialPrice, materialQty, matPaidBy, labourName, labourDays,
            labourWages, setDate, setmydatelbr, OtherTxt, OtheAmtTxt, labourpaidby, setMatDate;
    TextView materialTotalCost, labourTotalCost, labourPaidBy, setLabDate, OtheExpDate,titleSiteName;
    Button materialSaveContiButton, materialAddMoreButton, labourSaveContBtn,
            laboutAddMoreBtn, otherExpSaveBtn, addmoreOtheBtn;
    LinearLayout materailcostLay, materialMainLayout, labourMainLayout,
            labourcostLay;
    ImageView datePicker, datePickerlbr;
    static final int DATE_PICKER_ID = 1111;
    public static final String[] MONTHS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    String mon, paidbyStr;
    String material, matQty = "0.0", matPrice = "0.0", matTotalCost,
            labourNameStr, labourDaysStr, labourWagesStr, labTotalCost, labourPaidByStr, otheExpName, otheExpAmt;
    DatePickerDialog datePickerDialog;

    private IntentIntegrator qrScan;

    public AddExpense() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        qrScan = new IntentIntegrator(getActivity());
        ///new
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        name = user.getDisplayName();
        email = user.getEmail();
        img = user.getPhotoUrl().toString();
        System.out.println("main ac>>>>" + user.getUid());
        mAuth = FirebaseAuth.getInstance();
       siteName = getArguments().getString("sitename");
        siteID = getArguments().getString("siteid");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mPostReference = FirebaseDatabase.getInstance().getReference()
                .child("user-site").child(userId);


     /*   if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                siteID = null;
            } else {
                siteID = extras.getString("siteid");
            }
        } else {
            siteID = (String) savedInstanceState.getSerializable("siteid");
        }*/
        ////
        // addsite=(Button)findViewById(R.id.add);
        //expense view

        // layout
        View view = getActivity().getLayoutInflater().inflate(R.layout.addexpenselayout, new LinearLayout(getActivity()), false);
        materialMainLayout = (LinearLayout) view.findViewById(R.id.materiallayout);
        labourMainLayout = (LinearLayout) view.findViewById(R.id.labourlayout);
        otherExpense = (LinearLayout) view.findViewById(R.id.otherexpenselayout);
        materailcostLay = (LinearLayout) view.findViewById(R.id.materialtotalcostlay);
        materailcostLay.setVisibility(View.GONE);

        labourcostLay = (LinearLayout) view.findViewById(R.id.labourtotalcostlay);
        labourcostLay.setVisibility(View.GONE);
        // EditText
        titleSiteName = (TextView) view.findViewById(R.id.titlesitename);
        setMatDate = (EditText) view.findViewById(R.id.setmydate);
        setLabDate = (EditText) view.findViewById(R.id.setmydatelbr);
        materialName = (EditText) view.findViewById(R.id.materialname);
        materialPrice = (EditText) view.findViewById(R.id.materialuntiprice);
        materialQty = (EditText) view.findViewById(R.id.materiaqty);
        labourName = (EditText) view.findViewById(R.id.labourname);
        labourDays = (EditText) view.findViewById(R.id.labourdays);
        labourWages = (EditText) view.findViewById(R.id.lobourwages);
        matPaidBy = (EditText) view.findViewById(R.id.paidmatbyval);
        labourpaidby = (EditText) view.findViewById(R.id.labourpaidby);
        OtherTxt = (EditText) view.findViewById(R.id.otherexpname);
        OtheAmtTxt = (EditText) view.findViewById(R.id.amount);
        OtheExpDate = (EditText) view.findViewById(R.id.otheexpdate);

        // TextView
        materialTotalCost = (TextView) view.findViewById(R.id.materialtotalcost);
        labourTotalCost = (TextView) view.findViewById(R.id.labourtotalcost);

        // Button
        materialSaveContiButton = (Button) view.findViewById(R.id.savematerial);
        materialAddMoreButton = (Button) view.findViewById(R.id.addmorematerial);
        labourSaveContBtn = (Button) view.findViewById(R.id.savelabour);
        laboutAddMoreBtn = (Button) view.findViewById(R.id.addmorelabour);
        otherExpSaveBtn = (Button) view.findViewById(R.id.saveotherexpbtn);
        addmoreOtheBtn = (Button) view.findViewById(R.id.addmoreotherexp);

        materialSaveContiButton.setOnClickListener(this);
        materialAddMoreButton.setOnClickListener(this);
        labourSaveContBtn.setOnClickListener(this);
        laboutAddMoreBtn.setOnClickListener(this);
        setMatDate.setOnClickListener(this);
        setLabDate.setOnClickListener(this);
        OtheExpDate.setOnClickListener(this);
        otherExpSaveBtn.setOnClickListener(this);
        addmoreOtheBtn.setOnClickListener(this);
        //
        //sitename = (EditText) findViewById(R.id.entersitename);
        //  location = (EditText) findViewById(R.id.enterlocation);
        expenseSelection = (Spinner) view.findViewById(R.id.spinner);
        materialExpense = (LinearLayout) view.findViewById(R.id.materiallayout);
        labourExpense = (LinearLayout) view.findViewById(R.id.labourlayout);
        otherExpense = (LinearLayout) view.findViewById(R.id.otherexpenselayout);
        //  mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, arrayForSpinner);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseSelection.setAdapter(adp1);
        ////////////

titleSiteName.setText("Project Name : "+siteName);
        expenseSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                iCurrentSelection = expenseSelection.getSelectedItemPosition();
                System.out.println("position >>" + iCurrentSelection);
                if (iCurrentSelection == 0) {

                    materialExpense.setVisibility(View.VISIBLE);
                    labourExpense.setVisibility(View.GONE);
                    otherExpense.setVisibility(View.GONE);
                } else if (iCurrentSelection == 1) {
                    labourExpense.setVisibility(View.VISIBLE);
                    materialExpense.setVisibility(View.GONE);
                    otherExpense.setVisibility(View.GONE);

                } else if (iCurrentSelection == 2) {
                    otherExpense.setVisibility(View.VISIBLE);
                    labourExpense.setVisibility(View.GONE);
                    materialExpense.setVisibility(View.GONE);
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        labourDays.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    System.out.println("not null");
                    labourcostLay.setVisibility(View.VISIBLE);
                    labourNameStr = labourName.getText().toString().trim();
                    labourDaysStr = labourDays.getText().toString().trim();
                    labourWagesStr = labourWages.getText().toString().trim();
                    labTotalCost = labourTotalCost.getText().toString();
                    // System.out.println("material>>>>"+material +matQty
                    // +matPrice +matTotalCost);
                    if (labourNameStr.equalsIgnoreCase("")
                            || labourDaysStr.equalsIgnoreCase("")
                            || labourWagesStr.equalsIgnoreCase("")) {
						/*
						 * Toast t=Toast.makeText(getApplicationContext(),
						 * "Fields can not be empty", 1000); t.show();
						 */
                    } else {

                        double cost = Double.parseDouble(labourWagesStr)
                                * Double.parseDouble(labourDaysStr);
                        String matcostStr = String.valueOf(cost).toString();
                        labourTotalCost.setText(matcostStr);

                    }

                } else {
                    System.out.println("null");
                    labourcostLay.setVisibility(View.GONE);

                }
                // Field2.setText("");
            }
        });

        labourWages.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    System.out.println("not null");
                    labourcostLay.setVisibility(View.VISIBLE);
                    labourNameStr = labourName.getText().toString().trim();
                    labourDaysStr = labourDays.getText().toString().trim();
                    labourWagesStr = labourWages.getText().toString().trim();
                    labTotalCost = labourTotalCost.getText().toString();
                    // System.out.println("material>>>>"+material +matQty
                    // +matPrice +matTotalCost);
                    if (labourNameStr.equalsIgnoreCase("")
                            || labourDaysStr.equalsIgnoreCase("")
                            || labourWagesStr.equalsIgnoreCase("")) {
						/*
						 * Toast t=Toast.makeText(getApplicationContext(),
						 * "Fields can not be empty", 1000); t.show();
						 */
                    } else {

                        double cost = Double.parseDouble(labourWagesStr)
                                * Double.parseDouble(labourDaysStr);
                        String matcostStr = String.valueOf(cost).toString();
                        labourTotalCost.setText(matcostStr);

                    }

                } else {
                    System.out.println("null");
                    labourcostLay.setVisibility(View.GONE);

                }
                // Field1.setText("");
            }
        });

        // for material calculations
        materialPrice.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    System.out.println("not null");
                    materailcostLay.setVisibility(View.VISIBLE);
                    material = materialName.getText().toString().trim();
                    matQty = materialQty.getText().toString().trim();
                    matPrice = materialPrice.getText().toString();
                    matTotalCost = materialTotalCost.getText().toString();
                    // System.out.println("material>>>>"+material +matQty
                    // +matPrice +matTotalCost);
                    if (material.equalsIgnoreCase("")
                            || matQty.equalsIgnoreCase("")
                            || matPrice.equalsIgnoreCase("")) {
						/*
						 * Toast t=Toast.makeText(getApplicationContext(),
						 * "Fields can not be empty", 1000); t.show();
						 */
                    } else {

                        double cost = Double.parseDouble(matPrice)
                                * Double.parseDouble(matQty);
                        String matcostStr = String.valueOf(cost).toString();
                        materialTotalCost.setText(matcostStr);

                    }

                } else {
                    System.out.println("null");
                    materailcostLay.setVisibility(View.GONE);

                }
                // Field2.setText("");
            }
        });

        materialQty.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() != 0) {
                    System.out.println("not null");
                    materailcostLay.setVisibility(View.VISIBLE);
                    material = materialName.getText().toString().trim();
                    matQty = materialQty.getText().toString().trim();
                    matPrice = materialPrice.getText().toString();
                    matTotalCost = materialTotalCost.getText().toString();
                    // System.out.println("material>>>>"+material +matQty
                    // +matPrice +matTotalCost);
                    if (material.equalsIgnoreCase("")
                            || matQty.equalsIgnoreCase("")
                            || matPrice.equalsIgnoreCase("")) {
						/*
						 * Toast t=Toast.makeText(getApplicationContext(),
						 * "Fields can not be empty", 1000); t.show();
						 */
                    } else {

                        double cost = Double.parseDouble(matPrice)
                                * Double.parseDouble(matQty);
                        String matcostStr = String.valueOf(cost).toString();
                        materialTotalCost.setText(matcostStr);

                    }

                } else {
                    System.out.println("null");
                    materailcostLay.setVisibility(View.GONE);

                }
                // Field1.setText("");
            }
        });

        return view;
    }

    private void setEditingEnabled(boolean enabled) {
        sitename.setEnabled(enabled);
        location.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    public void getDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        setMatDate.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void getOtheXpDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        OtheExpDate.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void getLabourDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

        // date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        setLabDate.setText(dayOfMonth + "/"
                                + (monthOfYear + 1) + "/" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void addSite() {

        siteNameStr = sitename.getText().toString();
        locationStr = location.getText().toString();

        if (TextUtils.isEmpty(siteNameStr)) {
            sitename.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(locationStr)) {
            location.setError(REQUIRED);
            return;
        }
        setEditingEnabled(false);
        Toast.makeText(getActivity(), "Posting...", Toast.LENGTH_SHORT).show();

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
                            //  writeNewPost(userId, siteNameStr, locationStr, "000");
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);

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


    private void writeNewPostMaterial(String userId, String siteid, String matNAme, String date, String price, String Qty, String paidby, String total) {
        String key = mDatabase.child("user-material").push().getKey();
        SiteMaterial post = new SiteMaterial(userId, siteid, key, matNAme, date, price, Qty, paidby, total);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/site/" + key, postValues);
        childUpdates.put("/user-material/" + userId + "/" + siteID + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        materialName.setText("");
        materialQty.setText("");
        materialPrice.setText("");
        materialTotalCost.setText("");
        matPaidBy.setText("");
    }

    private void writeNewPostLabour(String userId, String siteid, String labName, String date, String days, String wages, String paidby, String total) {
        String key = mDatabase.child("user-labour").push().getKey();
        SiteLabour post = new SiteLabour(userId, siteid, key, labName, date, days, wages, paidby, total);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/site/" + key, postValues);
        childUpdates.put("/user-labour/" + userId + "/" +siteID + "/" +  key, postValues);

        mDatabase.updateChildren(childUpdates);

        labourName.setText("");
        labourDays.setText("");
        labourWages.setText("");
        labourTotalCost.setText("");
    }

    private void writeNewPostOther(String userId, String siteid, String expName, String date, String amot) {
        String key = mDatabase.child("user-labour").push().getKey();
        SiteOtherExpense post = new SiteOtherExpense(userId, siteid, key, expName, date, amot);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        //childUpdates.put("/site/" + key, postValues);
        childUpdates.put("/user-otherExpense/" + userId + "/" +siteID + "/" +  key, postValues);

        mDatabase.updateChildren(childUpdates);

        OtherTxt.setText("");
        OtheAmtTxt.setText("");
        labourWages.setText("");
        labourTotalCost.setText("");
    }

    @Override
    public void onClick(View v) {
        if (v == setMatDate) {
            //datePickerMyGTC();
            getDate();
        } else if (v == setLabDate) {
            //datePickerMyGTC();
            getLabourDate();
        }
        if (v == OtheExpDate) {
            //datePickerMyGTC();
            getOtheXpDate();
        }
        if (v == otherExpSaveBtn) {

            qrScan.initiateScan();
            //datePickerMyGTC(); OtherTxt, OtheAmtTxt otheExpName,otheExpAmt
            /*otheExpName = OtherTxt.getText().toString().trim();
            otheExpAmt = OtheAmtTxt.getText().toString().trim();
            dateStr=OtheExpDate.getText().toString().trim();
            if (otheExpName.equalsIgnoreCase("") || otheExpAmt.equalsIgnoreCase("")) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Fields can not be empty", Toast.LENGTH_SHORT);
                t.show();
            } else {

                writeNewPostOther(userId, siteid, otheExpName, otheExpAmt, dateStr);

                finish();
                // addReadMaterialInfo();

            }*/
        } else if (v == addmoreOtheBtn) {
            //datePickerMyGTC();
            getLabourDate();
        } else if (v == materialSaveContiButton) {

            material = materialName.getText().toString().trim();
            matQty = materialQty.getText().toString().trim();
            matPrice = materialPrice.getText().toString();
            matTotalCost = materialTotalCost.getText().toString();
            paidbyStr = matPaidBy.getText().toString().trim();
            dateStr = setMatDate.getText().toString().trim();
            // System.out.println("material>>>>"+material +matQty +matPrice
            // +matTotalCost);
            if (material.equalsIgnoreCase("") || matQty.equalsIgnoreCase("")
                    || matPrice.equalsIgnoreCase("") || paidbyStr.equalsIgnoreCase("")) {
                Toast t = Toast.makeText(getActivity(),
                        "Fields can not be empty", Toast.LENGTH_SHORT);
                t.show();
            } else {

                writeNewPostMaterial(userId, siteID, material, dateStr, matPrice, matQty, paidbyStr, matTotalCost);

                //finish();
                // addReadMaterialInfo();

            }

        } else if (v == labourSaveContBtn) {
            labourNameStr = labourName.getText().toString().trim();
            labourDaysStr = labourDays.getText().toString().trim();
            labourWagesStr = labourWages.getText().toString().trim();
            labTotalCost = labourTotalCost.getText().toString();


            if (labourNameStr.equalsIgnoreCase("")
                    || labourDaysStr.equalsIgnoreCase("")
                    || labourWagesStr.equalsIgnoreCase("")) {
                Toast t = Toast.makeText(getActivity(),
                        "Fields can not be empty", Toast.LENGTH_SHORT);
                t.show();
            } else {

                labourNameStr = labourName.getText().toString().trim();
                labourDaysStr = labourDays.getText().toString().trim();
                labourWagesStr = labourWages.getText().toString().trim();
                labTotalCost = labourTotalCost.getText().toString();
                dateStr = setLabDate.getText().toString();
                labourPaidByStr = labourpaidby.getText().toString();
                if (labourNameStr.equalsIgnoreCase("")
                        || labourDaysStr.equalsIgnoreCase("")
                        || labourWagesStr.equalsIgnoreCase("")) {
                    Toast t = Toast.makeText(getActivity(),
                            "Fields can not be empty", Toast.LENGTH_SHORT);
                    t.show();
                } else {

                    writeNewPostLabour(userId, siteID, labourNameStr, dateStr, labourDaysStr, labourWagesStr, labourPaidByStr, labTotalCost);

                    //finish();
                }

            }
        } else if (v == laboutAddMoreBtn) {
            labourNameStr = labourName.getText().toString().trim();
            labourDaysStr = labourDays.getText().toString().trim();
            labourWagesStr = labourWages.getText().toString().trim();
            labTotalCost = labourTotalCost.getText().toString();
            dateStr = setLabDate.getText().toString();
            labourPaidByStr = labourpaidby.getText().toString();
            if (labourNameStr.equalsIgnoreCase("")
                    || labourDaysStr.equalsIgnoreCase("")
                    || labourWagesStr.equalsIgnoreCase("")) {
                Toast t = Toast.makeText(getActivity(),
                        "Fields can not be empty", Toast.LENGTH_SHORT);
                t.show();
            } else {

                writeNewPostLabour(userId, siteID, labourNameStr, dateStr, labourDaysStr, labourWagesStr, labourPaidByStr, labTotalCost);


            }
        } else if (v == materialAddMoreButton) {

            material = materialName.getText().toString().trim();
            matQty = materialQty.getText().toString().trim();
            matPrice = materialPrice.getText().toString();
            matTotalCost = materialTotalCost.getText().toString();
            paidbyStr = matPaidBy.getText().toString().trim();
            dateStr = setMatDate.getText().toString().trim();
            // System.out.println("material>>>>"+material +matQty +matPrice
            // +matTotalCost);
            if (material.equalsIgnoreCase("") || matQty.equalsIgnoreCase("")
                    || matPrice.equalsIgnoreCase("") || paidbyStr.equalsIgnoreCase("")) {
                Toast t = Toast.makeText(getActivity(),
                        "Fields can not be empty", Toast.LENGTH_SHORT);
                t.show();
            } else {
                System.out.println(dateStr + "********");
                writeNewPostMaterial(userId, siteID, material, dateStr, matPrice, matQty, paidbyStr, matTotalCost);


            }
        }
    }
}
    /* final void openProductSearch(String upc) {
         Uri uri = Uri.parse("http://www.google." + LocaleManager.getProductSearchCountryTLD() +
                 "/m/products?q=" + upc + "&source=zxing");
         launchIntent(new Intent(Intent.ACTION_VIEW, uri));
     }*/
  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    OtherTxt.setText(obj.getString("name"));
                    OtheAmtTxt.setText(obj.getString("address"));
                    Toast.makeText(getActivity(), obj.getString("name")+obj.getString("address"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(getActivity(), result.getContents()+"====="+result.getFormatName(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/
