package com.example.google.csmia_temp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResetForm extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUESTAsset = 1889;
    ImageView taskSelfie,assetImage;
    int j = 0;
    int parameterCount = 0;


    private List<EditText> editTextList = new ArrayList<EditText>();
    private List<EditText> editTextListMeter = new ArrayList<EditText>();
    private List<EditText> editTextareaList = new ArrayList<EditText>();
    private List<TextView> textViewList = new ArrayList<TextView>();
    private List<EditText> editTextRemarkList = new ArrayList<EditText>();
    private List<Spinner> textSpinnerList = new ArrayList<Spinner>();
    private List<RadioButton> textRadioButtonList = new ArrayList<RadioButton>();
    private List<RadioGroup> textRadioGroupMeterList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupList = new ArrayList<RadioGroup>();
    ArrayList<String> previoudReadings = new ArrayList<>();
    private List<TextView> previousReadingChange = new ArrayList<TextView>();
    private List<TextView> textViewListIncident = new ArrayList<TextView>();
    private List<RadioGroup> textRadioGroupBranchingList = new ArrayList<RadioGroup>();
     String readingConstant ="";

    private static final String TAG = TaskDetails.class.getSimpleName();
    static final boolean LOG = new applicationClass().checkLog();
    LinearLayout.LayoutParams fittype, fittype1, fittype2, textLayout;
    private int year;
    private int month;
    private int day;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    int LINEARLAYOUTID=1;
    String date_time = "",updatedtime;
    TextView reading;
    public static final int DATE_DIALOG_ID = 999;
    DatePicker dpResult;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    EditText etUsername, etdate;
    Spinner sp1;
    CheckBox hobby;
    DatePickerDialog.OnDateSetListener datePickerListener;
    String   value = "", remark = "", unplanned = "";
    int Mandatory = 0;
    int sid = 0,SafeRange;
    String section = "";
    String FormType = "", Field_Limit_From = "",taskIDValue, Field_Limit_To = "", Threshold_From = "", Threshold_To = "", Validation_Type = "";
    int Id = 0;
    String Form_IdIntent, Record_id, activityname, AssetId,assetname, frequencyId,FormId;
    String Field_Id,Form_Id,Field_Label,Field_Type,Field_Options,sections,Asset_Name,Asset_Location,Asset_Status,Activity_Name,CheckActivity;

    String EditTextValue;
    private int EditTextId;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    SharedPreferences settings;

    int lastcreatedID;
    String selectRB = "";
    String companyId, FixedValue,SiteId, UserId,Scan_Type="";
    TextView textView;
    LinearLayout formLayout;
    //LoggerFile loggerFile = new LoggerFile();
    String[] loggerData;
    EditText editTextValue;
    String previousReadingDatabase="",User_Group_Id;
    int textviewId;


    Button submit;
    String field_Limit_Form1, field_Limit_To1, threshold_From1, threshold_To1, validation_Type1, Field_Option_Id,Critical1;
    String activityFrequencyId,TaskId, timeStarts,assetCode, imageDate, formStructureId, field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type, Critical;
    Map<String,Bitmap> drawableBitmap = new HashMap<String,Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        myDb = new DatabaseHelper(getApplicationContext());
        Log.d("TAGzsdfgvsdf","InResetForm");
        companyId = settings.getString("Company_Customer_Id", null);
        UserId = settings.getString("userId", null);
        //User_Group_Id = settings.getString("User_Group_Id", null);
        Scan_Type =myDb.ScanType(UserId);
        SiteId = myDb.Site_Location_Id(UserId);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        loggerData = new LoggerFile().loggerFunction(UserId);

        createCutomActionBarTitle();
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        Record_id = getIntent().getStringExtra("r_id");
        AssetId = getIntent().getStringExtra("AssetId");
        FormId = getIntent().getStringExtra("Form_Id");
        frequencyId = getIntent().getStringExtra("FrequencyId");
        unplanned = getIntent().getStringExtra("unplanned");
        timeStarts = getIntent().getStringExtra("StartDate");
        assetCode = getIntent().getStringExtra("AssetCode");
        TaskId = getIntent().getStringExtra("TaskId");
        Asset_Name = getIntent().getStringExtra("Asset_Name");
        Asset_Location=getIntent().getStringExtra("Asset_Location");
        Asset_Status =getIntent().getStringExtra("Asset_Status");
        Activity_Name = getIntent().getStringExtra("Activity_Name");
        User_Group_Id = getIntent().getStringExtra("User_Group_Id");
        int count=0;
        createForm();

    }

    private void createCutomActionBarTitle() {

        ActionBar mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_main)));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        activityname = getIntent().getStringExtra("Activity_Name");
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.dynamicform_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.titleFragment1);
        ((TextView) mTitleTextView.findViewById(R.id.titleFragment1)).setTextColor(Color.WHITE);

        assetname = getIntent().getStringExtra("AssetName");

        mTitleTextView.setText(activityname+BuildConfig.VERSION_NAME);
        mTitleTextView.setTextSize(14);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = simpleDateFormat.format(calendar.getTime());
        TextView mTitleTextView2 = (TextView) mCustomView.findViewById(R.id.titleFragment2);
        mTitleTextView2.setTextSize(14);
        ((TextView) mTitleTextView2.findViewById(R.id.titleFragment2)).setTextColor(Color.WHITE);
        mTitleTextView2.setText(assetname);

        //assign the view to the actionbar
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    public void createForm() {
        fittype1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype1.setMargins(10, 30, 10, 0);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        formLayout = new LinearLayout(this);
        formLayout.setOrientation(LinearLayout.VERTICAL);
        formLayout.setLayoutParams(fittype1);
        scrollView.addView(formLayout);

        textView = new TextView(this);
        textView.setText("Enter closing reading of " + Asset_Name + "\n");
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(fittype1);
        formLayout.addView(textView);

        fittype = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype.setMargins(10, 0, 10, 0);

        fittype2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype2.setMargins(10, 00, 100, 30);

        textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(10, 0, 10, 0);
        try {

            /*Getting Project Id from DataListActivity*/
            Form_IdIntent = getIntent().getStringExtra("Form_Id");
            myDb=new DatabaseHelper(getApplicationContext());
            db=myDb.getWritableDatabase();
            String formQuery = "select * from Form_Structure where Form_Id='" + Form_IdIntent + "' ORDER BY Display_Order ASC";
            Cursor cursor = db.rawQuery(formQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Id = cursor.getInt(cursor.getColumnIndex("Id"));
                    Field_Label = cursor.getString(cursor.getColumnIndex("Field_Label"));
                    Field_Type = cursor.getString(cursor.getColumnIndex("Field_Type"));
                    Field_Options =cursor.getString(cursor.getColumnIndex("Field_Options"));
                    Form_Id =cursor.getString(cursor.getColumnIndex("Form_Id"));
                    Mandatory = cursor.getInt(cursor.getColumnIndex("Mandatory"));
                    sid = cursor.getInt(cursor.getColumnIndex("sid"));
                    section = cursor.getString(cursor.getColumnIndex("sections"));
                    FormType = cursor.getString(cursor.getColumnIndex("FormType"));
                    Field_Id =cursor.getString(cursor.getColumnIndex("Field_Id"));
                    SafeRange =cursor.getInt(cursor.getColumnIndex("SafeRange"));

                    myDb=new DatabaseHelper(getApplicationContext());
                    db=myDb.getWritableDatabase();

                    String query = "Select Activity_Frequency_Id, Form_Id,Form_Structure_Id, Field_Limit_From ,Field_Limit_To ," +
                            "Threshold_From ,Threshold_To,Validation_Type,Critical FROM Parameter WHERE Activity_Frequency_Id = '"+frequencyId+"'" +
                            " AND Form_Structure_Id = '"+Field_Id+"'";
                    Cursor parameter =db.rawQuery(query, null);
                    parameterCount=parameter.getCount();
                    if (parameter.getCount() > 0) {
                        if (parameter.moveToNext()) {
                            do {

                                activityFrequencyId = parameter.getString(parameter.getColumnIndex("Activity_Frequency_Id"));
                                formStructureId = parameter.getString(parameter.getColumnIndex("Form_Structure_Id"));
                                field_Limit_Form = parameter.getString(parameter.getColumnIndex("Field_Limit_From"));
                                field_Limit_To = parameter.getString(parameter.getColumnIndex("Field_Limit_To"));
                                threshold_From = parameter.getString(parameter.getColumnIndex("Threshold_From"));
                                threshold_To = parameter.getString(parameter.getColumnIndex("Threshold_To"));
                                validation_Type = parameter.getString(parameter.getColumnIndex("Validation_Type"));
                                Critical = parameter.getString(parameter.getColumnIndex("Critical"));

                            } while (parameter.moveToNext());
                        }
                    }
                    parameter.close();
                    db.close();


                    previousReadingDatabase = myDb.lastMultiMeterReading(AssetId,Field_Id);
                    Log.d("fasdfdsfsdaf",previousReadingDatabase);

                    if (Field_Type.equals("meter")) {
                        try {
                            String[] label = Field_Label.split("\\|");
                            formLayout.addView(perviousReading(previousReadingDatabase, Id));
                            formLayout.addView(textView(label[0]));
                            formLayout.addView(editTextMeter(Mandatory, label[0], Id + 2, ""));
                            formLayout.addView(textView(label[1]));
                            formLayout.addView(radiogroupMeter(Field_Options, Id + 1, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("text")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            if (parameter.getCount()==0) {
                                formLayout.addView(editText(Mandatory, Field_Label, Id , "","","","","","",SafeRange));
                            } else {
                                formLayout.addView(editText(Mandatory, field_Limit_Form + " - " + field_Limit_To +" (Safe Range: "+ threshold_From + " - " +threshold_To+" )", Id, "", field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type,SafeRange));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("dropdown")) {

                        try {
                           /* formLayout.addView(textView(Field_Label));
                            formLayout.addView(qualifiaction(Field_Options, Id, ""));*/

                            if (parameterCount==0) {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, "", "", section));
                            } else {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id, section));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("branching")) {

                        try {
                            formLayout.addView(incidentlinearlayout(Mandatory, Field_Label, Id, Field_Options, section, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radio")) {
                        try {
                            /*formLayout.addView(textView(Field_Label));
                            formLayout.addView(radiogroup(Field_Options, Id, ""));*/

                            if (parameterCount==0) {
                                formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, "", ""));
                            } else {
                                formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                  /*  if (Field_Type.equals("dropdown")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(qualifiaction(Field_Options, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radio")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(radiogroup(Field_Options, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/
                    if (Field_Type.equals("textarea")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextarea(Field_Label, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("fixedtext")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextFixed(Mandatory, Field_Label, Id, FixedValue));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("datetime")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextDateTime(Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("remark")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextRemark(Field_Label, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();


        } catch (Exception e) {
            Log.d("Exp", e.toString());
        }

        try {
            if(new applicationClass().imageVariable().equals("yes")) {
                taskSelfie = new ImageView(this);
                taskSelfie.setImageResource(R.drawable.ic_cam);
                formLayout.addView(textView("Selfie"));
                formLayout.addView(taskSelfie);
                taskSelfie.getLayoutParams().height = 150;
                taskSelfie.getLayoutParams().width = 150;

                taskSelfie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });

                assetImage = new ImageView(this);

                assetImage.setImageResource(R.drawable.ic_cam);
                formLayout.addView(textView("Task Image"));
                formLayout.addView(assetImage);

                assetImage.getLayoutParams().height = 150;
                assetImage.getLayoutParams().width = 150;

                assetImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUESTAsset);
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        submit = new Button(this);
        submit.setText("Submit");
        submit.setTextColor(Color.WHITE);
        submit.setBackgroundColor(getResources().getColor(R.color.bg_main));
        formLayout.addView(submit);
        setContentView(scrollView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSubmit() == true && checkSubmit1() == true) {
                    String uuid = UUID.randomUUID().toString();
                    saveData(taskInsert());
                    Intent intent = new Intent(ResetForm.this, ResetDynamicForm.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("Form_Id", FormId);
                    intent.putExtra("AssetName", Asset_Name);
                    intent.putExtra("ActivityName", Activity_Name);
                    intent.putExtra("AssetId", AssetId);
                    intent.putExtra("FrequencyId", frequencyId);
                    intent.putExtra("StartDate", timeStarts);
                    intent.putExtra("AssetCode", assetCode);
                    intent.putExtra("TaskId", uuid);
                    intent.putExtra("Asset_Name", Asset_Name);
                    intent.putExtra("Asset_Location", Asset_Location);
                    intent.putExtra("Asset_Status", Asset_Status);
                    intent.putExtra("Activity_Name", activityname);
                    intent.putExtra("Scan_Type", "Barcode");
                    intent.putExtra("unplanned", "unplanned");
                    intent.putExtra("User_Group_Id", User_Group_Id);
                    startActivity(intent);
                    finish();
                }

                if (checkSubmit() == true && checkSubmit1() == false) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ResetForm.this);
                    alertDialog.setTitle("Reading Alert...");

                    alertDialog.setMessage("You are about to submit lower reading.!!");
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String uuid = UUID.randomUUID().toString();
                                    saveData(taskInsert());
                                    Intent intent = new Intent(ResetForm.this, ResetDynamicForm.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.putExtra("Form_Id", FormId);
                                    intent.putExtra("AssetName", Asset_Name);
                                    intent.putExtra("ActivityName", Activity_Name);
                                    intent.putExtra("AssetId", AssetId);
                                    intent.putExtra("FrequencyId", frequencyId);
                                    intent.putExtra("StartDate", timeStarts);
                                    intent.putExtra("AssetCode", assetCode);
                                    intent.putExtra("Asset_Name", Asset_Name);
                                    intent.putExtra("TaskId", uuid);
                                    intent.putExtra("Asset_Location", Asset_Location);
                                    intent.putExtra("Asset_Status", Asset_Status);
                                    intent.putExtra("Activity_Name", Activity_Name);
                                    intent.putExtra("Scan_Type", "Barcode");
                                    intent.putExtra("unplanned", "unplanned");
                                    intent.putExtra("User_Group_Id", User_Group_Id);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }
            }
        });
    }

    private String blockCharacters = "('~*#^|$%&!";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacters.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private TextView textViewIncident(String label) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("" + label + ": ");
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(textLayout);
        textViewListIncident.add(textView);
        return textView;
    }

    private TextView perviousReading(String reading,int id) {

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setId(id);
        textView.setText("Previous Reading: " + reading + "");
        textView.setLayoutParams(fittype1);
        textView.setLayoutParams(textLayout);
        textViewList.add(textView);
        previousReadingChange.add(textView);
        previoudReadings.add(reading);
        return textView;
    }



    private TextView textView(String label) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("" + label + ": ");
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(fittype1);
        textView.setLayoutParams(textLayout);
        textViewList.add(textView);
        return textView;
    }




    private EditText editTextMeter(final int mandatory, String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(fittype2);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);
        editTextListMeter.add(editText);
        return editText;
    }

    private EditText editText(final int mandatory, String name, int id, String setText, final String Field_Limit_From, final String Field_Limit_To, final String Threshold_From, final String Threshold_To, String Validation_Type,int SafeRange) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(SafeRange==1){
            editText.setHint(name);
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    Double val = Double.parseDouble(editText.getText().toString());
                    if (val > Double.parseDouble(Field_Limit_To) || val < Double.parseDouble(Field_Limit_From)) {
                        editText.setText("");
                        editText.setError("Invalid Reading.Please Enter valid Reading");
                    } else if ((val < Double.parseDouble(Threshold_From) && (val >= Double.parseDouble(Field_Limit_From))) || (val <= Double.parseDouble(Field_Limit_To)) && (val > Double.parseDouble(Threshold_To))) {
                        editText.setError("Alert will generate.");
                    } else {
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });
        editTextList.add(editText);
        return editText;
    }


/*
    private EditText editText(final int mandatory, String name, int id, String setText, final String Field_Limit_From, final String Field_Limit_To, final String Threshold_From, final String Threshold_To, String Validation_Type,int SafeRange) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(SafeRange==1){
            editText.setHint(name);
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    Double val = Double.parseDouble(editText.getText().toString());
                    if (val > Double.parseDouble(Field_Limit_To) || val < Double.parseDouble(Field_Limit_From)) {
                        editText.setText("");
                        editText.setError("Invalid Reading.Please Enter valid Reading");
                    } else if ((val < Double.parseDouble(Threshold_From) && (val >= Double.parseDouble(Field_Limit_From))) || (val <= Double.parseDouble(Field_Limit_To)) && (val > Double.parseDouble(Threshold_To))) {
                        editText.setError("Alert will generate.");
                    } else {
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });
        editTextList.add(editText);
        return editText;
    }
*/

/*
    private EditText editText(final int mandatory, String name, int id, String setText, final String Field_Limit_From, final String Field_Limit_To, final String Threshold_From, final String Threshold_To, String Validation_Type) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(fittype2);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);

            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);

            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    try {
                        Double val = Double.parseDouble(editText.getText().toString());
                        if (val > Double.parseDouble(Field_Limit_To) || val < Double.parseDouble(Field_Limit_From)) {
                            editText.setText("");
                            editText.setError("Invalid Reading.Please Enter valid Reading");
                        } else if ((val < Double.parseDouble(Threshold_From) && (val > Double.parseDouble(Field_Limit_From))) || (val < Double.parseDouble(Field_Limit_To)) && (val > Double.parseDouble(Threshold_To))) {
                            editText.setError("Alert will generate.");
                        } else {
                        }
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        editTextList.add(editText);
        return editText;
    }
*/

    private LinearLayout incidentlinearlayout(int Mandatory, String field_Label,int id,String field_Option,String section,String Value) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textViewIncident(field_Label));
        radioLayout.addView(radiogroupBranching(field_Option, Id, Value, section, radioLayout));
        return radioLayout;
    }



    private RadioGroup radiogroupBranching(String optionRadio, int id, String setText,final String Section_Id,final LinearLayout bracnhinglayout ) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButtonBranching(optionRadioList[i], i, setText,Section_Id,radioGroup.getCheckedRadioButtonId(),bracnhinglayout));
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String formQuery = "select * from Form_Structure where Form_Id='" + Section_Id + "' and Record_Status <> 'D' and sid='" + selectedId + "' ORDER BY Display_Order ASC";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(formQuery, null);
                Log.d("Dasdasdasdasdas", formQuery + " " + cursor.getCount());

                if (cursor.getCount() == 0) {
                    //Toast.makeText(getApplicationContext(), "No data", Toast.LENGTH_LONG).show();
                    removeRadioremark(bracnhinglayout, 1);
                } else {
                    sectionForm(Section_Id, selectedId, bracnhinglayout);
                }
            }
        });
        textRadioGroupBranchingList.add(radioGroup);
        return radioGroup;
    }


    public void sectionForm(String Sections_Intent,int Sid_Intent,LinearLayout formLayout){
        String formQuery = "select * from Form_Structure where Form_Id='" + Sections_Intent + "' and Record_Status <> 'D' and sid='"+Sid_Intent+"' ORDER BY Display_Order ASC";
        db=myDb.getWritableDatabase();
        Cursor cursor = db.rawQuery(formQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Id = cursor.getInt(cursor.getColumnIndex("Id"));
                Field_Label = cursor.getString(cursor.getColumnIndex("Field_Label"));
                Field_Type = cursor.getString(cursor.getColumnIndex("Field_Type"));
                Field_Options =cursor.getString(cursor.getColumnIndex("Field_Options"));
                Form_Id =cursor.getString(cursor.getColumnIndex("Form_Id"));
                Mandatory = cursor.getInt(cursor.getColumnIndex("Mandatory"));
                FixedValue = cursor.getString(cursor.getColumnIndex("FixedValue"));
                sid = cursor.getInt(cursor.getColumnIndex("sid"));
                section = cursor.getString(cursor.getColumnIndex("sections"));
                FormType = cursor.getString(cursor.getColumnIndex("FormType"));
                Field_Id =cursor.getString(cursor.getColumnIndex("Field_Id"));
                SafeRange =cursor.getInt(cursor.getColumnIndex("SafeRange"));

                try {
                    db=myDb.getWritableDatabase();
                    String query = "Select Activity_Frequency_Id, Form_Id,Field_Option_Id,Form_Structure_Id, Field_Limit_From ,Field_Limit_To ," +
                            "Threshold_From ,Threshold_To,Validation_Type,Critical FROM Parameter WHERE Activity_Frequency_Id = '"+frequencyId+"'" +
                            " AND Form_Structure_Id = '"+Field_Id+"'";
                    Cursor parameter =db.rawQuery(query,null);
                    parameterCount=parameter.getCount();
                    if (parameterCount > 0) {
                        if (parameter.moveToNext()) {
                            do {

                                activityFrequencyId = parameter.getString(parameter.getColumnIndex("Activity_Frequency_Id"));
                                formStructureId = parameter.getString(parameter.getColumnIndex("Form_Structure_Id"));
                                field_Limit_Form = parameter.getString(parameter.getColumnIndex("Field_Limit_From"));
                                field_Limit_To = parameter.getString(parameter.getColumnIndex("Field_Limit_To"));
                                threshold_From = parameter.getString(parameter.getColumnIndex("Threshold_From"));
                                threshold_To = parameter.getString(parameter.getColumnIndex("Threshold_To"));
                                validation_Type = parameter.getString(parameter.getColumnIndex("Validation_Type"));
                                Critical = parameter.getString(parameter.getColumnIndex("Critical"));
                                Field_Option_Id = parameter.getString(parameter.getColumnIndex("Field_Option_Id"));

                            } while (parameter.moveToNext());
                        }
                    }
                    parameter.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                previousReadingDatabase = myDb.lastMultiMeterReading(AssetId,Field_Id);

                if (Field_Type.equals("meter")) {
                    try {
                        String[] label = Field_Label.split("\\|");
                        formLayout.addView(perviousReading(previousReadingDatabase, Id));
                        formLayout.addView(textView(label[0]));
                        formLayout.addView(editTextMeter(Mandatory, label[0], Id + 2, ""));
                        formLayout.addView(textView(label[1]));
                        formLayout.addView(radiogroupMeter(Field_Options, Id + 1, ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("text")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        if (parameterCount==0) {
                            formLayout.addView(editText(Mandatory, Field_Label, Id , "","","","","","",SafeRange));
                        } else {
                            formLayout.addView(editText(Mandatory, field_Limit_Form + " - " + field_Limit_To +" (Safe Range: "+ threshold_From + " - " +threshold_To+" )", Id, "", field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type,SafeRange));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("fixedtext")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextFixed(Mandatory, Field_Label, Id, FixedValue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("dropdown")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(qualifiaction(Field_Options, Id, "", null, "", section));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("radio")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        if (parameterCount==0) {
                            formLayout.addView(radiogroup(Field_Options, Id, "", ""));
                        }else {
                            formLayout.addView(radiogroup(Field_Options, Id, "", Field_Option_Id));
                        }
                        /*if (parameterCount==0) {
                            formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, "", ""));
                        } else {
                            formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id));
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

              /*  if (Field_Type.equals("datetime")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextDateTime(Id, "",""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("dg")) {

                    try {
                        formLayout.addView(dglinearlayout(Mandatory, Field_Label, Id,"",""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
                if (Field_Type.equals("textarea")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextarea(Field_Label, Id, ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("remark")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextRemark(Field_Label, Id, ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

               /* if (Field_Type.equals("signature")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(Signature(Field_Label, Id, "Createform"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


    private RadioGroup radiogroup(String optionRadio, int id, String setText,final String Field_Option_Id) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);

        String[] optionRadioList = optionRadio.split(",");
        final TextView textView = new TextView(ResetForm.this);
        textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(layoutParams);

        textView.setVisibility(View.GONE);
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (!Field_Option_Id.equals("")) {
                    String[] label = Field_Option_Id.split("\\|");
                    for (int i = 0; i < label.length; i++) {
                        if (Integer.parseInt(label[i]) == selectedId) {
                            textView.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            textView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        textRadioGroupList.add(radioGroup);
        return radioGroup;
    }

    private void removeRadioremark(LinearLayout layout,int Id){

        for(TextView textView : textViewList){
            layout.removeView(textView);
        }
        for(EditText textView :editTextList ){
            layout.removeView(textView);
        }


        for(EditText textView :editTextareaList ){
            layout.removeView(textView);
        }
        for(Spinner textView :textSpinnerList ){
            layout.removeView(textView);
        }
        for(RadioGroup textView :textRadioGroupList ){
            layout.removeView(textView);
        }
       /* for(RadioGroup textView :textRadioGroupList ){
            layout.removeView(textView);
        }*/

    }

    private RadioButton radioButtonBranching(String strvalue, int SelectedId, String matchvalue,String Section_Id,int sid,LinearLayout bracnhinglayout) {   //965

        final RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(SelectedId);
        /*if(Completed.equals("Completed")) {
            radioButton.setEnabled(false);
        }*/

        if (matchvalue.equals(strvalue)) {
            radioButton.setChecked(true);
        }

        textRadioButtonList.add(radioButton);
        return radioButton;
    }


    private LinearLayout spinnerlinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id,String section) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(qualifiaction(field_option, id, setText, radioLayout, Field_Option_Id, section));
        return radioLayout;
    }

    private LinearLayout radiolinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        Log.d("ASDASDASDASDDAS", field_Label);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(radiogroup(field_option, id, setText, radioLayout, Field_Option_Id));
        return radioLayout;
    }

    private RadioGroup radiogroup(String optionRadio, int id, String setText, final LinearLayout linearLayout,final String Field_Option_Id) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);

        String[] optionRadioList = optionRadio.split(",");
        final TextView textView = new TextView(ResetForm.this);
        textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);
        textView.setVisibility(View.GONE);
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (!Field_Option_Id.equals("")) {
                    String[] label = Field_Option_Id.split("\\|");
                    for (int i = 0; i < label.length; i++) {
                        if (Integer.parseInt(label[i]) == selectedId) {
                            textView.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            textView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
        textRadioGroupList.add(radioGroup);
        return radioGroup;
    }

    private Spinner qualifiaction(String options, int sp_id, String qval,LinearLayout linearLayout, final String Field_Option_Id,final String Section_Id) {
        Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("-- Select One --");

        final TextView textView = new TextView(ResetForm.this);
        textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15,0,0,0);
        textView.setLayoutParams(layoutParams);
        //linearLayout.addView(textView);
        textView.setVisibility(View.GONE);

        for (String value : optionList) {
            spinnerArray.add(value);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qualifiactionSpinner.setAdapter(adapter);
        }

        qualifiactionSpinner.setLayoutParams(textLayout);
        textSpinnerList.add(qualifiactionSpinner.getSelectedItemPosition(), qualifiactionSpinner);
        for (String optValue : optionList) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            int selectionPosition = adapter.getPosition(qval);
            if (qval.equalsIgnoreCase(optValue))
                qualifiactionSpinner.setSelection(selectionPosition);
        }

        qualifiactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!Field_Option_Id.equals("")) {
                    String[] label = Field_Option_Id.split("\\|");
                    for (int i = 0; i < label.length; i++) {
                        if (Integer.parseInt(label[i]) == (position - 1)) {
                            textView.setVisibility(View.VISIBLE);
                            break;
                        } else {
                            textView.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return qualifiactionSpinner;
    }


    private RadioGroup radiogroupMeter(String optionRadio, int id, String setText) {

        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(fittype2);
        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        textRadioGroupMeterList.add(radioGroup);
            uomChange(textRadioGroupMeterList);
        return radioGroup;
    }

    private RadioGroup radiogroup(String optionRadio, int id, String setText) {

        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(fittype2);
        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        textRadioGroupList.add(radioGroup);
        return radioGroup;
    }


    private RadioButton radioButton(String strvalue, int SelectedId, String matchvalue) {   //965

        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(SelectedId);
        if (matchvalue.equals(strvalue)) {
            radioButton.setChecked(true);
        }
        textRadioButtonList.add(radioButton);
        return radioButton;
    }
    private Spinner qualifiaction(String options, int sp_id, String qval) {
        Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("-- Select One --");
        for (String value : optionList) {
            spinnerArray.add(value);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qualifiactionSpinner.setAdapter(adapter);
        }
        qualifiactionSpinner.setLayoutParams(fittype2);
        textSpinnerList.add(qualifiactionSpinner.getSelectedItemPosition(), qualifiactionSpinner);
        for (String optValue : optionList) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            int selectionPosition = adapter.getPosition(qval);
            if (qval.equalsIgnoreCase(optValue))
                qualifiactionSpinner.setSelection(selectionPosition);
        }
        return qualifiactionSpinner;
    }

    private EditText editTextFixed(final int mandatory, String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(fittype2);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);
        editText.setEnabled(false);
        editTextList.add(editText);
        return editText;
    }


    private EditText editTextarea(String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(fittype2);
        if (FormType.equalsIgnoreCase("Meter")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(3);
        editText.setMaxLines(5);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);
        editText.setText(setText);
        editTextareaList.add(editText);
        return editText;
    }
    private EditText editTextDateTime(final int Id, final String Value) {
        final EditText editText = new EditText(this);
        editText.setId(Id);

        editText.setLayoutParams(fittype2);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setKeyListener(null);
        //editText.setText(new applicationClass().yymmddhhmmss());
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Value.equals("D"))
                    datePicker(editText);
            }
        });
        editTextList.add(editText);
        return editText;
    }
    private void datePicker(final EditText editText){
        // Get Current Date
        try {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                    //date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //*************Call Time Picker Here ********************
                    timePicker(editText);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        } catch (Exception e) {
            Log.d("ua611 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua611", Toast.LENGTH_SHORT).show();
        }
    }
    private void timePicker(final EditText editText){
        try {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            date = date_time+" "+hourOfDay + ":" + minute + ":00";
                            editText.setText(date_time + " " + hourOfDay + ":" + minute + ":00");
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        } catch (Exception e) {
            Log.d("ua635 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua635", Toast.LENGTH_SHORT).show();
        }
    }


    private EditText editTextRemark(String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(fittype2);
        if (FormType.equalsIgnoreCase("Meter")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(3);
        editText.setMaxLines(5);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);
        editText.setFilters(new InputFilter[]{filter});
        editText.setText(setText);
        editTextRemarkList.add(editText);
        return editText;
    }
    public boolean checkSubmit1() {
        boolean checkRadio = false, editTextMeterCheck = false;

        if (textRadioGroupMeterList.size() == 0) checkRadio = true;
        if (editTextListMeter.size() == 0) editTextMeterCheck = true;

        if (previoudReadings.size() >= 1){    /* for meter reading form */
            for (String readingValue : previoudReadings) {
                if (readingValue != "No Previous Reading") {  /* if there is previous reading  */

                    for (EditText editLongText : editTextListMeter) {
                        edittextValidation.add(editLongText.getText().toString());
                        if (editLongText.getText().toString().equals("")) {
                            Snackbar snackbar = Snackbar.make(formLayout, "Please Enter Reading !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            editTextMeterCheck = false;
                            break;
                        } else {
                            editTextMeterCheck = true; }
                    }

                    int i = 0;
                    for (RadioGroup rdgrp : textRadioGroupMeterList) {
                        try {
                            if (rdgrp.getCheckedRadioButtonId() == -1) {
                                Snackbar snackbar = Snackbar.make(formLayout, "Please select UOM !!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                checkRadio = false;
                                break;
                            } else {
                                int cbid = rdgrp.getId();
                                String reading = previoudReadings.get(i);
                                String editValue = editTextListMeter.get(i).getText().toString();

                                try {
                                    int selectedId = rdgrp.getCheckedRadioButtonId();
                                    View rb1 = rdgrp.findViewById(selectedId);
                                    int idx = rdgrp.indexOfChild(rb1);
                                    RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                                    if (radioButton.isChecked()) {

                                        selectRB = radioButton.getText().toString();
                                    } else {
                                        Toast.makeText(getBaseContext(), selectRB + " c", Toast.LENGTH_SHORT).show();
                                    }

                                    if (!reading.trim().equals("No Previous Reading")) {

                                        String[] str_chk1 = reading.split(" ");
                                        Double a = Double.parseDouble(str_chk1[0]);
                                        String c = str_chk1[1].toString();
                                        if (c.equals("MW")) {
                                            a = a * 1000;
                                        }
                                        try {
                                            Double b = Double.parseDouble(editValue);

                                            if (selectRB.equals("MW")) {
                                                b = b * 1000;
                                            }
                                            if (b < a) {
                                                checkRadio = false;
                                                break;
                                            }
                                            if (b >= a) {
                                                checkRadio = true;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        checkRadio = true;
                                    }


                                } catch (NullPointerException e) {
                                    System.out.println("fbi540 ERROR==" + e);
                                }
                                i++;


                            }

                        } catch (NullPointerException e) {
                            System.out.println("fbi540 ERROR==" + e);
                        }
                    }
                }
                else {
                    checkRadio = true;
                    editTextMeterCheck = true;
                }
            }
        }
        if(LOG) Log.d(TAG,"checkSubmit1 :  "+"checkRadio"+checkRadio+"\n editTextMeterCheck"+editTextMeterCheck);
        if(checkRadio == true && editTextMeterCheck == true ) {
            return true;
        }
        else
            return false;
    }
    public boolean checkSubmit() {

        boolean checkRadio = false, editTextMeterCheck = false, editTextCheck = false;

        if (editTextList.size() == 0) editTextCheck = true;
        if (textRadioGroupMeterList.size() == 0) checkRadio = true;
        if (editTextListMeter.size() == 0) editTextMeterCheck = true;

        if (previoudReadings.size() >= 1){    /* for meter reading form */
            for (String readingValue : previoudReadings) {
                if (readingValue == "No Previous Reading") {     /* if there is no previous reading  */

                    for (EditText editLongText : editTextList) {
                        slnew = editLongText.getText().toString();
                        edittextValidation.add(editLongText.getText().toString());
                        if (slnew.equals("")) {
                            editTextCheck = false;
                            editLongText.setError("Please Enter Value");
                            Snackbar snackbar = Snackbar.make(formLayout, "Please Complete the form !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            break;
                        } else {
                            editTextCheck = true;
                        }
                    }
                    for (RadioGroup rdgrp : textRadioGroupMeterList) {

                        try {
                            if (rdgrp.getCheckedRadioButtonId() == -1) {
                                Snackbar snackbar = Snackbar.make(formLayout, "Please select UOM !!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                checkRadio = false;
                                break;
                            } else {
                                checkRadio = true;
                            }

                        } catch (NullPointerException e) {
                            System.out.println("fbi540 ERROR==" + e);
                        }
                    }

                    for (EditText editLongText : editTextListMeter) {
                        edittextValidation.add(editLongText.getText().toString());
                        if (editLongText.getText().toString().equals("")) {
                            Snackbar snackbar = Snackbar.make(formLayout, "Please Enter Reading !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            editTextMeterCheck = false;
                            break;
                        } else {
                            editTextMeterCheck = true;
                        }
                    }
                } else if (readingValue != "No Previous Reading") {  /* if there is previous reading  */
                    for (EditText editLongText : editTextList) {
                        slnew = editLongText.getText().toString();
                        edittextValidation.add(editLongText.getText().toString());
                        if (slnew.equals("")) {
                            editTextCheck = false;
                            editLongText.setError("Please Enter Value");
                            Snackbar snackbar = Snackbar.make(formLayout, "Please Complete the form !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            break;
                        } else {
                            editTextCheck = true;
                        }
                    }
                    for (RadioGroup rdgrp : textRadioGroupMeterList) {

                        try {
                            if (rdgrp.getCheckedRadioButtonId() == -1) {
                                Snackbar snackbar = Snackbar.make(formLayout, "Please select UOM !!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                checkRadio = false;
                                break;
                            } else {
                                checkRadio = true;
                            }

                        } catch (NullPointerException e) {
                            System.out.println("fbi540 ERROR==" + e);
                        }
                    }

                    for (EditText editLongText : editTextListMeter) {
                        edittextValidation.add(editLongText.getText().toString());
                        if (editLongText.getText().toString().equals("")) {
                            Snackbar snackbar = Snackbar.make(formLayout, "Please Enter Reading !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            editTextMeterCheck = false;
                            break;
                        } else
                            editTextMeterCheck = true;
                    }
                }
            }
        }
        else {   /* for asset checklist form */
            for (EditText editLongText : editTextList) {
                slnew = editLongText.getText().toString();
                edittextValidation.add(editLongText.getText().toString());
                if (slnew.equals("")) {
                    editTextCheck = false;
                    editLongText.setError("Please Enter Value");
                    Snackbar snackbar = Snackbar.make(formLayout, "Please Complete the form !!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    break;
                } else {
                    editTextCheck = true;
                }
            }
            for (RadioGroup rdgrp : textRadioGroupList) {

                try {
                    if (rdgrp.getCheckedRadioButtonId() == -1) {
                        Snackbar snackbar = Snackbar.make(formLayout, "Please select values !!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        checkRadio = false;
                        break;
                    } else {
                        checkRadio = true;
                    }

                } catch (NullPointerException e) {
                    System.out.println("fbi540 ERROR==" + e);
                }
            }
        }
        if(LOG) Log.d(TAG,"checkSubmit :  "+"checkRadio"+checkRadio+"\n editTextMeterCheck"+editTextMeterCheck+" \n editTextCheck"+editTextCheck);

        if(checkRadio == true && editTextMeterCheck == true && editTextCheck ==true) {
            return true;
        }
        else
            return false;
    }
    public boolean valueCheckData(){

        boolean dataValueCheck =false;
        boolean loopDestory = false;


        if(textRadioGroupMeterList.size()==0) {
            dataValueCheck = true;
        }else {
            EditText editTextValueCheck;
            for (RadioGroup radioGroup1 : textRadioGroupMeterList) {
                if(loopDestory){
                    break;
                }
                for (int k = 0; k < editTextListMeter.size(); k++) {
                    textviewId = editTextListMeter.get(k).getId();
                    if (textviewId - 1 == radioGroup1.getId()) {
                        editTextValueCheck = editTextListMeter.get(k);

                        int selectedId = radioGroup1.getCheckedRadioButtonId();


                        if (selectedId == 1) {
                            String value = editTextValueCheck.getText().toString();
                            int i = value.indexOf(".");
                            Log.d("valueChangeData", selectedId + " " + i + " " + value);
                            if (i < 0) {
                                dataValueCheck = false;
                                Snackbar snackbar = Snackbar.make(formLayout, "Please enter decimal value for MW", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                loopDestory = true;
                                break;
                            }else {
                                dataValueCheck = true;
                            }
                        }
                        else {
                            dataValueCheck = true;
                        }

                    }
                }


            }
        }
        return dataValueCheck;
    }
    public void uomChange(List<RadioGroup> radioGroups) {
        int i = 0;

        for(RadioGroup radioGroup1:radioGroups) {
            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    RadioButton radioButton = (RadioButton) radioGroup.getChildAt(checkedId);
                    for (int k = 0; k < previousReadingChange.size(); k++) {
                        textviewId = previousReadingChange.get(k).getId();
                        if (textviewId == radioGroup.getId()-1) {

                            reading = previousReadingChange.get(k);
                            readingConstant = previousReadingChange.get(k).getText().toString();
                        }
                    }
                    if (!reading.getText().toString().equals("Previous Reading: No Previous Reading")) {
                        String[] str_chk1 = reading.getText().toString().split(" ");
                        String[] valueReading = readingConstant.split(" ");
                        final Double a = Double.parseDouble(str_chk1[2]);
                        final String c = str_chk1[3].toString();

                        if (radioButton.getId() == 0) {
                            if (c.equals("MW")) {
                                Double a2 = a * 1000;
                                BigDecimal d = new BigDecimal(a2);
                                reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP)+ " KW");
                            }
                        } else if(radioButton.getId() == 1){
                            if (c.equals("KW")) {

                                Double a2 = a / 1000;
                                BigDecimal d = new BigDecimal(a2);
                                reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP) + " MW");
                            }
                        }

                    }


                }
            });


        }
    }


    String date;
    String slnew = "";
    ArrayList<String> edittextValidation = new ArrayList<>();

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            try {
                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Bitmap drawImage = photo.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(drawImage); //bmp is the bitmap to dwaw into

                    Calendar calendarImage = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    imageDate = simpleDateFormat1.format(calendarImage.getTime());
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(10);
                    paint.setTextAlign(Paint.Align.CENTER);
                    String printText = assetCode + "" + imageDate;
                    paint.setAntiAlias(true);
                    Log.d("DateAssetCode",printText);
                    canvas.drawText(assetCode, 55, 10, paint);
                    canvas.drawText(imageDate,45 , 20, paint);

                    drawableBitmap.put("Selfie",drawImage);
                    taskSelfie.setImageBitmap(drawImage);
                    taskSelfie.getLayoutParams().height = 500;
                    taskSelfie.getLayoutParams().width = 500;

                }
                else if (requestCode == CAMERA_REQUESTAsset && resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    Bitmap drawImage = photo.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(drawImage); //bmp is the bitmap to dwaw into

                    Calendar calendarImage = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    imageDate = simpleDateFormat1.format(calendarImage.getTime());
                    Paint paint = new Paint();
                    paint.setColor(Color.RED);
                    paint.setTextSize(7);
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setAntiAlias(true);
                    String printText = assetCode + " " + imageDate;
                    Log.d("DateAssetCode", printText);
                    // canvas.drawText(printText, 85, 100, paint);
                    canvas.drawText(assetCode, 55, 10, paint);
                    canvas.drawText(imageDate,45 , 20, paint);
                    drawableBitmap.put("AssetImage",drawImage);
                    assetImage.setImageBitmap(drawImage);
                    assetImage.getLayoutParams().height = 500;
                    assetImage.getLayoutParams().width = 500;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    public String taskInsert(){
        String uuid = "";
        String Remarks="";
        for (EditText editTextarea1 : editTextRemarkList) {
            try {
                Remarks = editTextarea1.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            uuid = UUID.randomUUID().toString();
            Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String rsdate = simpleDateFormat.format(calendar.getTime());

            ContentValues contentValues1 = new ContentValues();

            contentValues1.put("Auto_Id", uuid);
            contentValues1.put("Company_Customer_Id", companyId);
            contentValues1.put("Site_Location_Id", SiteId);
            contentValues1.put("Activity_Frequency_Id", frequencyId);
            contentValues1.put("Task_Scheduled_Date", "0000-00-00 00:00:00");
            contentValues1.put("Task_Status", "Unplanned");
            contentValues1.put("Task_Start_At", rsdate);
            contentValues1.put("Assigned_To", "U");
            contentValues1.put("EndDateTime", "[Unplanned]");
            contentValues1.put("Asset_Name", Asset_Name);
            contentValues1.put("Asset_Id",AssetId);
            contentValues1.put("Asset_Location", Asset_Location);
            contentValues1.put("Asset_Status", Asset_Status);
            contentValues1.put("Activity_Name", Activity_Name);
            contentValues1.put("Assigned_To_User_Id", UserId);
            contentValues1.put("From_Id",Form_IdIntent);
            contentValues1.put("Asset_Code",assetCode);
            contentValues1.put("Assigned_To_User_Group_Id",User_Group_Id);
            contentValues1.put("Scan_Type", Scan_Type);
            contentValues1.put("Remarks", Remarks);
            contentValues1.put("updatedStatus", "no");
            myDb=new DatabaseHelper(getApplicationContext());
            db=myDb.getWritableDatabase();
            db.insert("Task_Details", null, contentValues1);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  uuid;
    }

    @SuppressLint("ResourceType")
    public void saveData(String uuid) {
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        String rsdate1 = simpleDateFormat.format(calendar.getTime());

        HashMap<String,String> editText = new HashMap<>();
        ArrayList<String> radioButtonString = new ArrayList<String>();
        ArrayList<String> checkBox = new ArrayList<String>();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Id", uuid);
        contentValues.put("Asset_Id", AssetId);
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Task_Start_At",rsdate1);
        contentValues.put("Activity_Frequency_Id", frequencyId);
        contentValues.put("Site_Location_Id", SiteId);

        for (EditText editLongText : editTextListMeter) {
            try {
                int fieldId = editLongText.getId();
                slnew = editLongText.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues.put("Form_Structure_Id", myDb.getfieldId(fieldId-2));
                // contentValues.put("Reading", slnew);

                RadioGroup rdgrp;
                for (int i = 0; i < textRadioGroupMeterList.size(); i++) {
                    if (fieldId == textRadioGroupMeterList.get(i).getId() + 1) {
                        rdgrp = textRadioGroupMeterList.get(i);
                        int selectedId = rdgrp.getCheckedRadioButtonId();
                        View rb1 = rdgrp.findViewById(selectedId);
                        int idx = rdgrp.indexOfChild(rb1);
                        RadioButton radioButton = (RadioButton) rdgrp.getChildAt(selectedId);
                        if (radioButton.isChecked()) {
                            selectRB = radioButton.getText().toString();
                            radioButtonString.add(selectRB);
                        }
                        break;
                    }
                }
                contentValues.put("Reset", "0");
                contentValues.put("UOM", selectRB);
                contentValues.put("updatedStatus", "no");
                contentValues.put("Reading", slnew);
                myDb = new DatabaseHelper(getApplicationContext());
                db = myDb.getWritableDatabase();
                db.insert("Meter_Reading", null, contentValues);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       /* for (EditText editLongText : editTextListMeter) {
            try {
                int fieldId = editLongText.getId();
                slnew = editLongText.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues.put("Form_Structure_Id", myDb.getfieldId(fieldId-2));
                contentValues.put("Reading", slnew);

                RadioGroup rdgrp;
                for (int i = 0; i < textRadioGroupMeterList.size(); i++) {
                    if (fieldId == textRadioGroupMeterList.get(i).getId() + 1) {
                        rdgrp = textRadioGroupMeterList.get(i);
                        int selectedId = rdgrp.getCheckedRadioButtonId();
                        View rb1 = rdgrp.findViewById(selectedId);
                        int idx = rdgrp.indexOfChild(rb1);
                        RadioButton radioButton = (RadioButton) rdgrp.getChildAt(selectedId);
                        if (radioButton.isChecked()) {
                            selectRB = radioButton.getText().toString();
                            radioButtonString.add(selectRB);
                        }
                        break;
                    }
                }
                contentValues.put("Reset", "1");
                contentValues.put("UOM", selectRB);
                contentValues.put("updatedStatus", "no");

                myDb = new DatabaseHelper(getApplicationContext());
                db = myDb.getWritableDatabase();
                db.insert("Meter_Reading", null, contentValues);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
        for (EditText editTextList1 : editTextList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextList1.getId();
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextList1.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("UpdatedStatus", "no");
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Value", slnew);
                contentValues1.put("Parameter_Id", "");
                myDb = new DatabaseHelper(getApplicationContext());
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EditText editTextarea1 : editTextareaList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextarea1.getId();
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextarea1.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("UpdatedStatus", "no");
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Value", slnew);
                contentValues1.put("Parameter_Id", "");
                myDb = new DatabaseHelper(getApplicationContext());
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EditText editTextRemarks : editTextRemarkList) {
            try {
                ContentValues contentValuesRemarks = new ContentValues();
                int fieldIdRemarks = editTextRemarks.getId();
                slnew = editTextRemarks.getText().toString();
                String Form_Structure_Id = myDb.getfieldId(fieldIdRemarks);
                String formId = myDb.getFormId(Form_Structure_Id);
                editText.put(myDb.getfieldId(fieldIdRemarks), slnew);
                contentValuesRemarks.put("Task_Id", uuid);
                contentValuesRemarks.put("Form_Id", formId);
                contentValuesRemarks.put("Form_Structure_Id", Form_Structure_Id);
                contentValuesRemarks.put("Site_Location_Id", SiteId);
                contentValuesRemarks.put("Value", slnew);
                contentValuesRemarks.put("Parameter_Id", "");
                contentValuesRemarks.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValuesRemarks);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (RadioGroup rdgrp : textRadioGroupBranchingList) {
            String selectRB = "";
            int cbid = rdgrp.getId();

            try {
                int selectedId = rdgrp.getCheckedRadioButtonId();
                View rb1 = rdgrp.findViewById(selectedId);
                int idx = rdgrp.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                String Form_Structure_Id = myDb.getfieldId(cbid);

                String formId = myDb.getFormId(Form_Structure_Id);
                if (radioButton.isChecked()) {
                    selectRB = radioButton.getText().toString();
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("Task_Id", uuid);
                    contentValues2.put("Form_Id",  formId);
                    contentValues2.put("Form_Structure_Id",Form_Structure_Id);
                    contentValues2.put("Site_Location_Id", SiteId);
                    contentValues2.put("Parameter_Id", "");
                    contentValues2.put("Value", selectRB);
                    contentValues2.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues2);
                    db.close();
                }
            } catch (NullPointerException e) {
                System.out.println("fbi540 ERROR==" + e);
            } catch (Exception e) {
                System.out.println("fd1303 ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: fd1303", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                System.out.println("-------ee FD radio  " + "Id_" + cbid + " value " + selectRB);

            }
        }

        for (RadioGroup rdgrp : textRadioGroupList) {
            String selectRB = "";
            int cbid = rdgrp.getId();

            try {
                int selectedId = rdgrp.getCheckedRadioButtonId();
                View rb1 = rdgrp.findViewById(selectedId);
                int idx = rdgrp.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                String Form_Structure_Id = myDb.getfieldId(cbid);

                String formId = myDb.getFormId(Form_Structure_Id);
                if (radioButton.isChecked()) {
                    selectRB = radioButton.getText().toString();
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("Task_Id", uuid);
                    contentValues2.put("Form_Id", formId);
                    contentValues2.put("Form_Structure_Id", Form_Structure_Id);
                    contentValues2.put("Site_Location_Id", SiteId);
                    contentValues2.put("Parameter_Id", "");
                    contentValues2.put("Value", selectRB);
                    contentValues2.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues2);


                    String query = "Select * FROM Parameter" +
                            " WHERE Activity_Frequency_Id = '"+frequencyId+"'" + "AND Form_Structure_Id = '"+Form_Structure_Id+"'";

                    Cursor parameter =db.rawQuery(query, null);
                    if (parameter.getCount() > 0) {
                        try {
                            if (parameter.moveToNext()) {
                                do {
                                    validation_Type1 = parameter.getString(parameter.getColumnIndex("Validation_Type"));
                                    Critical1 = parameter.getString(parameter.getColumnIndex("Critical"));
                                    Field_Option_Id = parameter.getString(parameter.getColumnIndex("Field_Option_Id"));
                                } while (parameter.moveToNext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            if(selectedId == Integer.parseInt(Field_Option_Id)){
                                Toast.makeText(getApplicationContext(),"Alert Inserted",Toast.LENGTH_LONG).show();
                                InsertAlert(uuid,Form_Structure_Id);
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    db.close();
                }
            } catch (NullPointerException e) {
                System.out.println("fbi540 ERROR==" + e);
            } catch (Exception e) {
                System.out.println("fd1303 ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: fd1303", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                System.out.println("-------ee FD radio  " + "Id_" + cbid + " value " + selectRB);

            }
        }

        for (Spinner textSpinner : textSpinnerList) {
            try {
                int id_sp = textSpinner.getId();
                int item_pos = textSpinner.getSelectedItemPosition();
                String Form_Structure_Id = myDb.getfieldId(id_sp);
                String formId = myDb.getFormId(Form_Structure_Id);
                String sspinner = String.valueOf(textSpinner.getSelectedItem());
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("Task_Id", uuid);
                contentValues3.put("Form_Id", formId);
                contentValues3.put("Parameter_Id", "");
                contentValues3.put("Form_Structure_Id", Form_Structure_Id);
                contentValues.put("Site_Location_Id", SiteId);
                contentValues3.put("Value", sspinner);
                contentValues3.put("UpdatedStatus", "no");
                db=myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues3);
                String query = "Select * FROM Parameter" +
                        " WHERE Activity_Frequency_Id = '"+frequencyId+"'" + "AND Form_Structure_Id = '"+Form_Structure_Id+"'";
                Cursor parameter =db.rawQuery(query, null);
                if (parameter.getCount() > 0) {
                    try {
                        if (parameter.moveToNext()) {
                            do {
                                validation_Type1 = parameter.getString(parameter.getColumnIndex("Validation_Type"));
                                Critical1 = parameter.getString(parameter.getColumnIndex("Critical"));
                                Field_Option_Id = parameter.getString(parameter.getColumnIndex("Field_Option_Id"));
                            } while (parameter.moveToNext());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if(item_pos == Integer.parseInt(Field_Option_Id)){
                            InsertAlert(uuid,Form_Structure_Id);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(EditText editText1:editTextList){
            int fieldId = editText1.getId();
            slnew = editText1.getText().toString();
            String Form_Structure_Id = myDb.getfieldId(fieldId);
            String query = "Select Activity_Frequency_Id, Form_Id,Form_Structure_Id, Field_Limit_From ,Field_Limit_To ," +
                    "Threshold_From ,Threshold_To,Validation_Type,Critical FROM Parameter" +
                    " WHERE Activity_Frequency_Id = '"+frequencyId+"'" + "AND Form_Structure_Id = '"+Form_Structure_Id+"'";
            db= myDb.getWritableDatabase();
            Cursor parameter =db.rawQuery(query, null);
            if (parameter.getCount() > 0) {
                try {
                    if (parameter.moveToNext()) {
                        do {
                            field_Limit_Form1 = parameter.getString(parameter.getColumnIndex("Field_Limit_From"));
                            field_Limit_To1 = parameter.getString(parameter.getColumnIndex("Field_Limit_To"));
                            threshold_From1= parameter.getString(parameter.getColumnIndex("Threshold_From"));
                            threshold_To1 = parameter.getString(parameter.getColumnIndex("Threshold_To"));
                            validation_Type1 = parameter.getString(parameter.getColumnIndex("Validation_Type"));
                            Critical1 = parameter.getString(parameter.getColumnIndex("Critical"));
                        } while (parameter.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if ((Double.parseDouble(slnew) < Double.parseDouble(threshold_From1) && (Double.parseDouble(slnew) >= Double.parseDouble(field_Limit_Form1))) || (Double.parseDouble(slnew) <= Double.parseDouble(field_Limit_To1)) && (Double.parseDouble(slnew) > Double.parseDouble(threshold_To1))) {
                        InsertAlert(uuid,Form_Structure_Id);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            parameter.close();
            db.close();
        }
        try {
            Iterator it = drawableBitmap.entrySet().iterator();
            while (it.hasNext()) {
            Map.Entry<String,Bitmap> pair = (Map.Entry)it.next();

             myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(UserId));
             it.remove(); // avoids a ConcurrentModificationException
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void InsertAlert(String uuid,String Form_Structure_Id){
        String formId = myDb.getFormId(Form_Structure_Id);
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("Task_Id", uuid);
        contentValues2.put("Form_Id", formId);
        contentValues2.put("Form_Structure_Id", Form_Structure_Id);
        contentValues2.put("Alert_Type", validation_Type1);
        contentValues2.put("Created_By_Id", UserId);
        contentValues2.put("Assigned_To_User_Group_Id",User_Group_Id);
        contentValues2.put("Critical", Critical1);
        contentValues2.put("TaskType", "");
        contentValues2.put("ViewFlag", "no");
        contentValues2.put("Asset_Name",Asset_Name);
        contentValues2.put("Activity_Name",Activity_Name);
        contentValues2.put("Site_Location_Id",SiteId);
        contentValues2.put("Activity_Frequency_Id",activityFrequencyId);
        contentValues2.put("Task_Status","Unplanned");
        contentValues2.put("Task_Scheduled_Date","0000-00-00 00:00:00");
        contentValues2.put("Task_Start_At",new applicationClass().yymmddhhmmss());
        contentValues2.put("UpdatedStatus", "no");
        db.insert("AlertMaster", null, contentValues2);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ResetForm.this, AssetsActivity.class);
        intent.putExtra("site_id",SiteId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


}



