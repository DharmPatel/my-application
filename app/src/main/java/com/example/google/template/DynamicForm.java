package com.example.google.template;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class DynamicForm extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUESTAsset = 1889;
    ImageView taskSelfie,assetImage;
    int j = 0;
    double totalfeedback= 0.0;
    int ID=-1;
    int scoringval =0,TotalOfScore = 0;
    boolean on = false;
    private static final String  emailId = "support@punctualiti.in", emailPwd = "oriole579";
    Map.Entry<String,String> emailPair;
    HashMap<String,String> emaildata = new HashMap<>();
    Iterator emailvals;
    private List<RadioGroup> textRadioGroupRadioRadio = new ArrayList<RadioGroup>();
    private List<TextView> CalculationList = new ArrayList<TextView>();
    private List<EditText> editTextAddList = new ArrayList<EditText>();
    private List<EditText> editTextaddition = new ArrayList<EditText>();
    private List<EditText> editTextWithCbList = new ArrayList<EditText>();
    private List<TextView> textViewListBranch = new ArrayList<TextView>();
    private List<EditText> editTextList = new ArrayList<EditText>();
    private List<EditText> editTextDGTimeList = new ArrayList<EditText>();
    Map<Integer,EditText> editTextListFeedback = new HashMap<Integer,EditText>();
    private List<EditText> editTextConsumptionData = new ArrayList<EditText>();
    private List<EditText> editTextConsumptionShow = new ArrayList<EditText>();
    private List<EditText> editTextDateTimeList = new ArrayList<EditText>();
    private List<EditText> editTextListMeter = new ArrayList<EditText>();
    private List<EditText> editTextareaList = new ArrayList<EditText>();
    private List<ImageView> imageSignature = new ArrayList<ImageView>();
    private List<EditText> editTextConsumption = new ArrayList<EditText>();
    private List<EditText> editTextRemarkList = new ArrayList<EditText>();
    private List<TextView> textViewList = new ArrayList<TextView>();
    private List<Spinner> textSpinnerList = new ArrayList<Spinner>();
    private List<RadioButton> textRadioButtonList = new ArrayList<RadioButton>();
    private List<RadioGroup> textRadioGroupMeterList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupBranchingList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupConsumption = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupScoreList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioFeedbackList = new ArrayList<RadioGroup>();
    ArrayList<String> previoudReadings = new ArrayList<>();
    private List<TextView> previousReadingChange = new ArrayList<TextView>();
    private List<TextView> textViewListIncident = new ArrayList<TextView>();
    Map<Integer,ImageView> drawableRadioImage = new HashMap<Integer,ImageView>();
    String readingConstant ="",EmployeeName="";
    LinearLayout.LayoutParams fittype, fittype1, fittype2, textLayout,textBranchType;
    TextView reading;
    DatabaseHelper myDb;
    SQLiteDatabase db;
    Spinner sp1;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    String date_time = "";
    String unplanned,FixedValue;
    int Mandatory = 0,calculationvalue;
    int sid = 0;
    int parameterCount = 0;
    String section = "";
    String FormType = "";
    String TAG = DynamicForm.class.getSimpleName();
    int Id = 0,SafeRange;
    static final boolean LOG = new applicationClass().checkLog();
    String Form_IdIntent, PPM_Intent, Record_id,AssetId,frequencyId,label_value;
    String Field_Id,Reading,Remarks,UOM,Value,Form_Id,Field_Label,Field_Type,Field_Options,sections,Asset_Name,Asset_Location,Asset_Status,Completed="",Activity_Name;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String selectRB = "";
    String companyId, SiteId, User_Id,User_Group_Id,Scan_Type="",taskStatus="";
    TextView textView;
    LinearLayout formLayout;
    String[] loggerData;
    String previousReadingDatabase="";
    int textviewId;
    Button submit;
    String field_Limit_Form1, field_Limit_To1, threshold_From1, threshold_To1, validation_Type1, Critical1,Field_Option_Id;
    String activityFrequencyId,TaskId, Task_Scheduled_Date,assetCode, formStructureId, field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type, Critical;
    Map<String,Bitmap> drawableBitmap = new HashMap<String,Bitmap>();
    Button mClear, mGetSign, mCancel;
    Dialog dialog;
    LinearLayout mContent;
    View view;
    signature mSignature;
    Bitmap mBitmap;
    File root = new File(Environment.getExternalStorageDirectory(), "DigitSign");
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = root + pic_name + ".png";
    applicationClass applicationClass;
    String firstRadio;
    String SecondRadio;
    double editTextFirstValue = 0 ;
    double editTextSecondValue = 0;
    String intentFromReset;
    String uuid1, ClosingTaskId;
    Double subFactor=0.0, addFactor=0.0, multiFactor=1.0, divFactor=1.0, firstVal = 0.0, secondVal = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        myDb = new DatabaseHelper(getApplicationContext());
        uuid1 = UUID.randomUUID().toString();
        User_Id = settings.getString("userId", null);
        SiteId = myDb.Site_Location_Id(User_Id);
        final Calendar c = Calendar.getInstance();
        loggerData = new LoggerFile().loggerFunction(User_Id);
        PPM_Intent = getIntent().getStringExtra("PPMTask");
        Record_id = getIntent().getStringExtra("r_id");
        AssetId = getIntent().getStringExtra("AssetId");
        frequencyId = getIntent().getStringExtra("FrequencyId");
        unplanned = getIntent().getStringExtra("unplanned");
        taskStatus = getIntent().getStringExtra("Status");
        Task_Scheduled_Date = getIntent().getStringExtra("StartDate");
        assetCode = getIntent().getStringExtra("AssetCode");
        TaskId = getIntent().getStringExtra("TaskId");
        ClosingTaskId = getIntent().getStringExtra("ClosingTaskId");
        Asset_Name = getIntent().getStringExtra("AssetName");
        Asset_Location=getIntent().getStringExtra("Asset_Location");
        Asset_Status =getIntent().getStringExtra("Asset_Status");
        Activity_Name = getIntent().getStringExtra("ActivityName");
        Completed = getIntent().getStringExtra("Completed");
        User_Group_Id = getIntent().getStringExtra("User_Group_Id");
        Form_IdIntent = getIntent().getStringExtra("Form_Id");
        intentFromReset = getIntent().getStringExtra("FromReset");
        Scan_Type = myDb.ScanType(User_Id);
        applicationClass = new applicationClass();
        EmployeeName = myDb.EmployeeName(User_Id);

        //createCutomActionBarTitle();
        try{
            /*if(Completed.equals("Completed") ){
                getData();
            }else if(Completed.equals("Delayed")){
                getData();
            }else{
                createForm();
            }*/
            if (Completed == null){
                Completed = "Pending";
            }
            if(LOG) Log.d("StatusVal",Completed);
            if (Completed.equals("Completed") || Completed.equals("Delayed")){
                getData();
            }else {
                createForm();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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

    private void createCutomActionBarTitle() {

        ActionBar mActionBar = getActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.bg_main)));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.dynamicform_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.titleFragment1);
        ((TextView) mTitleTextView.findViewById(R.id.titleFragment1)).setTextColor(Color.WHITE);
        mTitleTextView.setText(Activity_Name+BuildConfig.VERSION_NAME);
        mTitleTextView.setTextSize(14);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = simpleDateFormat.format(calendar.getTime());
        TextView mTitleTextView2 = (TextView) mCustomView.findViewById(R.id.titleFragment2);
        mTitleTextView2.setTextSize(14);
        ((TextView) mTitleTextView2.findViewById(R.id.titleFragment2)).setTextColor(Color.WHITE);
        mTitleTextView2.setText(Asset_Name);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
    public void getData()
    {
        fittype1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype1.setMargins(10, 30, 10, 0);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        formLayout = new LinearLayout(this);
        formLayout.setOrientation(LinearLayout.VERTICAL);
        //formLayout.setLayoutParams(fittype1);
        scrollView.addView(formLayout);
        toolbar(formLayout);
        textView = new TextView(this);
        textView.setTextSize(12);
        textView.setLayoutParams(fittype1);
        formLayout.addView(textView);

        fittype = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype.setMargins(10, 0, 10, 0);

        fittype2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype2.setMargins(10, 20, 100, 30);

        textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(10, 0, 10, 0);


        try {

            /*Getting Project Id from DataListActivity*/
            Form_IdIntent = getIntent().getStringExtra("Form_Id");
            db=myDb.getWritableDatabase();
            String formQuery = "Select a.* from Form_Structure a where a.Form_Id='" + Form_IdIntent +"' ORDER BY Display_Order ASC";
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
                    SafeRange =cursor.getInt(cursor.getColumnIndex("Field_Id"));
                    calculationvalue =cursor.getInt(cursor.getColumnIndex("Calculation"));

                    try {
                        String query = "Select Activity_Frequency_Id, Form_Id,Form_Structure_Id,Field_Option_Id, Field_Limit_From ,Field_Limit_To ," +
                                "Threshold_From ,Threshold_To,Validation_Type,Critical FROM Parameter WHERE Activity_Frequency_Id = '"+frequencyId+"'" +
                                " AND Form_Structure_Id = '"+Field_Id+"'";
                        db=myDb.getWritableDatabase();
                        Cursor parameter =db.rawQuery(query, null);
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
                                    Field_Option_Id=parameter.getString(parameter.getColumnIndex("Field_Option_Id"));
                                } while (parameter.moveToNext());
                            }
                        }
                        parameter.close();
                        db.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        db=myDb.getWritableDatabase();
                        String formQuery1 = "select Value from Data_Posting where Form_Structure_Id='"+Field_Id+"' and Task_Id='"+TaskId+"'";
                        Cursor DataPosting =db.rawQuery(formQuery1, null);
                        if (DataPosting.getCount() > 0) {
                            if (DataPosting.moveToNext()) {
                                do {

                                    Value =DataPosting.getString(DataPosting.getColumnIndex("Value"));

                                } while (DataPosting.moveToNext());
                            }
                        }
                        DataPosting.close();
                        db.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        db=myDb.getWritableDatabase();
                        String formQuery2 = "select Reading,UOM from Meter_Reading where Form_Structure_Id='"+Field_Id+"' and Task_Id='"+TaskId+"'";
                        Cursor Meter_Reading =db.rawQuery(formQuery2, null);
                        if (Meter_Reading.getCount() > 0) {
                            if (Meter_Reading.moveToNext()) {
                                do {

                                    Reading =Meter_Reading.getString(Meter_Reading.getColumnIndex("Reading"));
                                    UOM =Meter_Reading.getString(Meter_Reading.getColumnIndex("UOM"));

                                } while (Meter_Reading.moveToNext());
                            }
                        }
                        Meter_Reading.close();
                        db.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        db=myDb.getWritableDatabase();
                        String formQueryRemarks = "select Remarks from Task_Details where From_Id='"+Form_Id+"' and Auto_Id='"+TaskId+"'";
                        Cursor cursorRemarks =db.rawQuery(formQueryRemarks, null);
                        if (cursorRemarks.getCount() > 0) {
                            if (cursorRemarks.moveToNext()) {
                                do {

                                    Remarks =cursorRemarks.getString(cursorRemarks.getColumnIndex("Remarks"));

                                } while (cursorRemarks.moveToNext());
                            }
                        }
                        cursorRemarks.close();
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
                            formLayout.addView(editTextMeter(Mandatory, label[0], Id + 2,Reading));
                            formLayout.addView(textView(label[1]));
                            formLayout.addView(radiogroupMeter(Field_Options, Id + 1, UOM));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("branchingText")) {
                        try {
                            formLayout.addView(branchTextlinearlayout(Mandatory, Field_Label, Id, section,sid));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("textwithcb")) {

                        try {
                            if (parameterCount==0) {
                                formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, Value, "", "", "", "", "", SafeRange,calculationvalue));
                            } else {
                                formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, Value, field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type, SafeRange,calculationvalue));
                            }
                            //formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("textview")) {

                        try {
                            formLayout.addView(textViewLabel(Field_Label));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("textscore")){
                        formLayout.addView(textViewScore("",Id));
                    }
                    if (Field_Type.equals("addReading")) {
                        try {
                            formLayout.addView(meterTextlinearlayout(Mandatory, Field_Label, Id, Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("consumption")) {
                        try {
                            formLayout.addView(consumptionlinearlayout(Mandatory, Field_Label, Field_Options, Id, "", Value, Field_Id, calculationvalue));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("incident")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", "", section));
                            } else {
                                formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id, section));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radioScore")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(radioScorelinearlayout(Field_Label,Field_Id,Field_Options,Id,Value,""));
                            } else {
                                formLayout.addView(radioScorelinearlayout(Field_Label,Field_Id,Field_Options,Id,Value,Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiofeedback")) {
                        try {
                            formLayout.addView(radiofeedbacklinearlayout(Field_Label, Field_Options, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("feedbackscore")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(Score(Field_Label, Id, "", calculationvalue));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("name")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextareaName(Field_Label, Id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("branching")) {

                        try {
                            formLayout.addView(sectionLinearLayout(Mandatory, Field_Label, Id, Field_Options, section, Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("dg")) {

                        try {
                            formLayout.addView(dglinearlayout(Mandatory, Field_Label, Id, "", Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }if (Field_Type.equals("radiowithradio")) {

                        try {
                            formLayout.addView(radiowithradiolinearlayout(Mandatory, Field_Label, Field_Options, Id, "", Value, Field_Id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("text")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            if (parameterCount==0) {
                                formLayout.addView(editText(Mandatory, Field_Label, Id ,Value,"","","","","",SafeRange,calculationvalue));
                            } else {
                                formLayout.addView(editText(Mandatory, field_Limit_Form + " - " + field_Limit_To +" (Safe Range: "+ threshold_From + " - " +threshold_To+" )", Id,Value, field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type,SafeRange,calculationvalue));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiowithimageandremark")) {
                        try {
                            formLayout.addView(radiowithImageandRemarklinearlayout(Field_Label, Field_Options, Id, Value, sid));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiowithimage")) {
                        try {
                            formLayout.addView(radiowithImagelinearlayout(Mandatory,Field_Label, Field_Options, Id, Value, sid));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiowithremark")) {
                        try {
                            formLayout.addView(radiowithRemarklinearlayout( Field_Label, Field_Options, Id, Value, sid));

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
                           /* formLayout.addView(textView(Field_Label));
                            formLayout.addView(qualifiaction(Field_Options, Id, Value));*/
                            if (parameterCount==0) {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, Value, "", section));
                            } else {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, Value, "", section));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radio")) {
                        try {
                           /* formLayout.addView(textView(Field_Label));
                            formLayout.addView(radiogroup(Field_Options, Id, Value));*/
                            if (parameterCount==0) {
                                formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, Value, ""));
                            } else {
                                formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, Value, Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("datetime")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextDateTime(Id, "D"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("textarea")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextarea(Field_Label, Id, Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("remark")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextRemark(Field_Label, Id, Remarks));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("signature")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(Signature(Field_Label, Id, "getData"));
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
            e.printStackTrace();
        }


        if(applicationClass.imageVariable().equals("yes")) {

            byte[] image="".getBytes();

            taskSelfie = new ImageView(this);
            taskSelfie.setImageResource(R.drawable.ic_cam);
            formLayout.addView(textView("Selfie"));
            formLayout.addView(taskSelfie);
            taskSelfie.getLayoutParams().height = 150;
            taskSelfie.getLayoutParams().width = 150;
            assetImage = new ImageView(this);

            assetImage.setImageResource(R.drawable.ic_cam);
            formLayout.addView(textView("Task Image"));
            formLayout.addView(assetImage);

            assetImage.getLayoutParams().height = 150;
            assetImage.getLayoutParams().width = 150;

            try {
                db=myDb.getWritableDatabase();
                String formQuery1 = "select Image_Selfie from Task_Selfie where Task_Id='"+TaskId+"' and Image_Type='Selfie'";
                Cursor img =db.rawQuery(formQuery1, null);
                if (img.getCount() > 0) {
                    if (img.moveToNext()) {
                        do {
                            image = img.getBlob(0);
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            taskSelfie.setImageBitmap(bitmap);
                            taskSelfie.getLayoutParams().height = 350;
                            taskSelfie.getLayoutParams().width = 350;

                        } while (img.moveToNext());
                    }
                }

                img.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                db=myDb.getWritableDatabase();
                String formQuery2 = "select Image_Selfie from Task_Selfie where Task_Id='"+TaskId+"' and Image_Type='AssetImage'";
                Cursor img2 =db.rawQuery(formQuery2, null);
                if (img2.getCount() > 0) {
                    if (img2.moveToNext()) {
                        do {
                            image = img2.getBlob(0);
                            ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
                            Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
                            assetImage.setImageBitmap(bitmap1);
                            assetImage.getLayoutParams().height = 350;
                            assetImage.getLayoutParams().width = 350;

                        } while (img2.moveToNext());
                    }
                }

                img2.close();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        submit = new Button(this);
        submit.setText("Back");
        submit.setTextColor(Color.WHITE);
        submit.setBackgroundColor(getResources().getColor(R.color.bg_main));
        formLayout.addView(submit);
        setContentView(scrollView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(myDb.Verify(User_Id)==1){
                    updateData(taskInsertVerify(TaskId));
                    Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                    intent.putExtra("TAB", "TAB3");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {*/
                Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                intent.putExtra("TAB", "TAB3");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                // }

            }
        });

    }
    public void createForm() {
        fittype1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype1.setMargins(10, 30, 10, 0);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        formLayout = new LinearLayout(this);
        formLayout.setOrientation(LinearLayout.VERTICAL);
        //formLayout.setLayoutParams(fittype1);
        scrollView.addView(formLayout);
        toolbar(formLayout);
        textView = new TextView(this);
        textView.setTextSize(12);
        textView.setLayoutParams(fittype1);
        formLayout.addView(textView);

        if (intentFromReset == null){
            intentFromReset = "";
        }else if (intentFromReset.equals("Closing")){
            textView = new TextView(this);
            textView.setText("Enter closing reading of " + Asset_Name + "\n");
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(R.color.bg_main));
            textView.setLayoutParams(fittype1);
            formLayout.addView(textView);
        }else if (intentFromReset.equals("Opening")){
            textView = new TextView(this);
            textView.setText("Enter opening reading of " + Asset_Name + "\n");
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(R.color.bg_main));
            textView.setLayoutParams(fittype1);
            formLayout.addView(textView);
        }

        fittype = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype.setMargins(10, 0, 10, 0);

        fittype2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fittype2.setMargins(10, 20, 100, 30);

        textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(10, 0, 10, 0);
        try {

            db=myDb.getWritableDatabase();
            String formQuery = "select * from Form_Structure where Form_Id='" + Form_IdIntent + "' and Record_Status <> 'D' ORDER BY Display_Order ASC";

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
                    Field_Id =cursor.getString(cursor.getColumnIndex("Field_Id"));
                    SafeRange =cursor.getInt(cursor.getColumnIndex("SafeRange"));
                    calculationvalue =cursor.getInt(cursor.getColumnIndex("Calculation"));

                    try {
                        db=myDb.getWritableDatabase();
                        String query = "Select Activity_Frequency_Id, Form_Id,Form_Structure_Id,Field_Option_Id, Field_Limit_From ,Field_Limit_To ," +
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
                            if (!intentFromReset.equals("Opening")) {
                                formLayout.addView(perviousReading(previousReadingDatabase, Id));
                            }
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
                                formLayout.addView(editText(Mandatory, Field_Label, Id , "","","","","","",SafeRange,calculationvalue));
                            } else {
                                formLayout.addView(editText(Mandatory, field_Limit_Form + " - " + field_Limit_To +" (Safe Range: "+ threshold_From + " - " +threshold_To+" )", Id, "", field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type,SafeRange,calculationvalue));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("branchingText")) {
                        try {
                            formLayout.addView(branchTextlinearlayout(Mandatory, Field_Label, Id, section, sid));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("textwithcb")) {

                        try {
                            if (parameterCount==0) {
                                formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, "", "", "", "", "", "", SafeRange,calculationvalue));
                            } else {
                                formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, "", field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type, SafeRange,calculationvalue));
                            }
                            //formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("textview")) {

                        try {
                            formLayout.addView(textViewLabel(Field_Label));
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
                    if (Field_Type.equals("textscore")){
                        formLayout.addView(textViewScore("",Id));
                    }
                    if (Field_Type.equals("addReading")) {
                        try {
                            formLayout.addView(meterTextlinearlayout(Mandatory, Field_Label, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("incident")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", "", section));
                            } else {
                                formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id, section));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("dropdown")) {

                        try {
                            if (parameterCount==0) {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, "", "", section));
                            } else {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id, section));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radio")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, "", ""));
                            } else {
                                formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radioScore")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(radioScorelinearlayout(Field_Label,Field_Id,Field_Options,Id,"",""));
                            } else {
                                formLayout.addView(radioScorelinearlayout(Field_Label,Field_Id,Field_Options,Id,"",Field_Option_Id));
                            }
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
                    if (Field_Type.equals("dg")) {

                        try {
                            formLayout.addView(dglinearlayout(Mandatory, Field_Label, Id, "", " , , "));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("consumption")) {

                        try {
                            formLayout.addView(consumptionlinearlayout(Mandatory, Field_Label, Field_Options, Id, "", " , , ", Field_Id, calculationvalue));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("textarea")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextarea(Field_Label, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("name")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextareaName(Field_Label, Id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiowithimageandremark")) {
                        try {
                            formLayout.addView(radiowithImageandRemarklinearlayout(Field_Label, Field_Options, Id, "", sid));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiowithimage")) {
                        try {
                            formLayout.addView(radiowithImagelinearlayout(Mandatory,Field_Label, Field_Options, Id, "", sid));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiowithremark")) {
                        try {
                            formLayout.addView(radiowithRemarklinearlayout( Field_Label, Field_Options, Id, "", sid));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiofeedback")) {
                        try {

                            formLayout.addView(radiofeedbacklinearlayout(Field_Label, Field_Options, Id, ""));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("feedbackscore")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            if(LOG) Log.d("Tasdasdasdasd",calculationvalue+"");
                            formLayout.addView(Score(Field_Label, Id, "", calculationvalue));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radiowithradio")) {

                        try {
                            formLayout.addView(radiowithradiolinearlayout(Mandatory, Field_Label, Field_Options, Id, "", " , , ", Field_Id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("remark")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextRemark(Field_Label, Id, Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("branching")) {

                        try {
                            formLayout.addView(sectionLinearLayout(Mandatory, Field_Label, Id, Field_Options, section, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("signature")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(Signature(Field_Label, Id, "Createform"));
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
            e.printStackTrace();
        }

        if(applicationClass.imageVariable().equals("yes")) {
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
        submit = new Button(this);
        submit.setText("Submit");
        submit.setTextColor(Color.WHITE);
        submit.setBackgroundColor(getResources().getColor(R.color.bg_main));
        formLayout.addView(submit);
        setContentView(scrollView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertCheck();
                if (checkSubmitEditTextAdd() == true && checkSubmitEditText() == true && checkSubmitEditTextMeter() == true && checkSubmitRadio() == true && checkSubmitRadioMeter() == true && checkSpinnerValue() == true && checkSubmitRemark() == true) {
                    if (checkSubmitLowerReading() == true) {
                        if (intentFromReset.equals("Closing")){
                            saveData(taskInsert(taskStatus));
                            Intent intent = new Intent(DynamicForm.this,DynamicForm.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("FromReset","Opening");
                            intent.putExtra("Form_Id", Form_IdIntent);
                            intent.putExtra("AssetName", Asset_Name);
                            intent.putExtra("ActivityName", Activity_Name);
                            intent.putExtra("AssetId", AssetId);
                            intent.putExtra("FrequencyId", frequencyId);
                            intent.putExtra("StartDate", Task_Scheduled_Date);
                            intent.putExtra("AssetCode", assetCode);
                            intent.putExtra("TaskId", uuid1);
                            intent.putExtra("Asset_Name", Asset_Name);
                            intent.putExtra("Asset_Location", Asset_Location);
                            intent.putExtra("Asset_Status", Asset_Status);
                            //intent.putExtra("Activity_Name", Activity_Name);
                            intent.putExtra("Scan_Type", "Barcode");
                            intent.putExtra("unplanned", "unplanned");
                            intent.putExtra("User_Group_Id", User_Group_Id);
                            startActivity(intent);
                            finish();
                        }else if(intentFromReset.equals("Opening")){
                            Toast.makeText(getApplicationContext(), "Reset Done", Toast.LENGTH_SHORT).show();
                            saveData(taskInsert(taskStatus));
                            Intent intent = new Intent(DynamicForm.this, HomePage.class);
                            intent.putExtra("User_Id", User_Id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(DynamicForm.this);
                            alertDialog.setTitle("Reading Alert...");

                            alertDialog.setMessage("You are about to submit lower reading.!!");
                            alertDialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if(LOG) Log.d("taskStatusVal","1:"+taskStatus);
                                            if (PPM_Intent.equals("PPMPending")){
                                                saveData(taskInsert(taskStatus));
                                                Intent intent = new Intent(DynamicForm.this, ppm_activity.class);
                                                intent.putExtra("TAB", "TAB3");
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }else {
                                                saveData(taskInsert(taskStatus));
                                                Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                                                intent.putExtra("TAB", "TAB3");
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                                finish();
                                            }
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

                    } else {
                        if (intentFromReset.equals("Closing")){
                            saveData(taskInsert(taskStatus));
                            Intent intent = new Intent(getApplicationContext(),DynamicForm.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("FromReset","Opening");
                            intent.putExtra("Form_Id", Form_IdIntent);
                            intent.putExtra("AssetName", Asset_Name);
                            intent.putExtra("ActivityName", Activity_Name);
                            intent.putExtra("AssetId", AssetId);
                            intent.putExtra("FrequencyId", frequencyId);
                            intent.putExtra("StartDate", Task_Scheduled_Date);
                            intent.putExtra("AssetCode", assetCode);
                            intent.putExtra("TaskId", uuid1);
                            intent.putExtra("Asset_Name", Asset_Name);
                            intent.putExtra("Asset_Location", Asset_Location);
                            intent.putExtra("Asset_Status", Asset_Status);
                            intent.putExtra("Activity_Name", Activity_Name);
                            intent.putExtra("Scan_Type", "Barcode");
                            intent.putExtra("unplanned", "unplanned");
                            intent.putExtra("User_Group_Id", User_Group_Id);
                            startActivity(intent);
                            finish();
                        }else if(intentFromReset.equals("Opening")){
                            Toast.makeText(getApplicationContext(), "Reset Done", Toast.LENGTH_SHORT).show();
                            saveData(taskInsert(taskStatus));
                            Intent intent = new Intent(DynamicForm.this, HomePage.class);
                            intent.putExtra("User_Id", User_Id);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }else {
                            if (PPM_Intent.equals("PPMPending")){
                                saveData(taskInsert(taskStatus));
                                Intent intent = new Intent(DynamicForm.this, ppm_activity.class);
                                intent.putExtra("TAB", "TAB3");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else {
                                saveData(taskInsert(taskStatus));
                                Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                                intent.putExtra("TAB", "TAB3");
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                }
            }
        });
    }
    public  void  toolbar(LinearLayout linearLayout){
        Toolbar toolbar = new Toolbar(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        toolbar.setLayoutParams(layoutParams);
        toolbar.setPopupTheme(R.style.toolbarStyle);
        toolbar.setBackgroundColor(getResources().getColor(R.color.Login));

        LinearLayout linearLayoutToolbar = new LinearLayout(this);
        linearLayoutToolbar.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutToolbar.setLayoutParams(parms);
        TextView textViewActivity = new TextView(this);
        textViewActivity.setText(Activity_Name + BuildConfig.VERSION_NAME);
        textViewActivity.setTextColor(Color.WHITE);
        textViewActivity.setTextSize(15);
        //toolbar.addView(textViewActivity);

        TextView textViewAsset = new TextView(this);
        textViewAsset.setText(Asset_Name);
        textViewAsset.setPadding(50, 0, 0, 0);
        textViewAsset.setTextColor(Color.WHITE);

        linearLayoutToolbar.addView(textViewActivity);
        linearLayoutToolbar.addView(textViewAsset);

        toolbar.addView(linearLayoutToolbar);

        toolbar.setVisibility(View.VISIBLE);
        linearLayout.addView(toolbar, 0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    private TextView perviousReading(String reading,int id) {

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setId(id);
        textView.setEnabled(false);
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
    private TextView textViewLabel(String label) {
        TextView textView = new TextView(this);
        textView.setTextSize(30);
        textView.setText("" + label + ": ");
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(textLayout);
        textViewList.add(textView);
        return textView;
    }
    private TextView textViewIncident(String label) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("" + label + ": ");
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(textLayout);
        textViewListIncident.add(textView);
        return textView;
    }
    private TextView textViewScore(String label,int Id) {
        TextView textView = new TextView(this);
        textView.setId(Id);
        textView.setTextSize(20);
        textView.setText(label);
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(fittype1);
        textView.setLayoutParams(textLayout);
        CalculationList.add(textView);
        return textView;
    }
    private LinearLayout incidentlinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id,String section) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(incidentSpinner(field_option, id, setText, radioLayout, Field_Option_Id, section));
        return radioLayout;
    }

    private Spinner incidentSpinner(String options, int sp_id, String qval,LinearLayout linearLayout, final String Field_Option_Id,final String Section_Id) {
        final Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("-- Select One --");

        final TextView textView = new TextView(DynamicForm.this);
        textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);
        textView.setVisibility(View.GONE);

        for (String value : optionList) {
            spinnerArray.add(value);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qualifiactionSpinner.setAdapter(adapter);
        }
        if(Completed.equals("Completed")){
            qualifiactionSpinner.setEnabled(false);
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
                String SpinnerValue = String.valueOf(qualifiactionSpinner.getSelectedItem());
                int SpinnerId = qualifiactionSpinner.getId();
                if (Completed.equals("Completed")) {
                    qualifiactionSpinner.setEnabled(false);
                    qualifiactionSpinner.setClickable(false);
                } else {
                    editor = settings.edit();
                    editor.putString("IncidentSpValue", SpinnerValue);
                    editor.putInt("IncidentSpId", SpinnerId);
                    editor.commit();
                    String formQuery = "select * from Form_Structure where Form_Id='" + Section_Id + "' and Record_Status <> 'D' and sid='" + (position - 1) + "' ORDER BY Display_Order ASC";
                    db = myDb.getWritableDatabase();
                    Cursor cursor = db.rawQuery(formQuery, null);
                    if (cursor.getCount() > 0) {
                        Intent intent = new Intent(DynamicForm.this, IncidentForm.class);
                        intent.putExtra("Section_Id", Section_Id);
                        intent.putExtra("sid", (position - 1));
                        intent.putExtra("FrequencyId", frequencyId);
                        intent.putExtra("Activity_Name", Activity_Name);
                        intent.putExtra("Asset_Name", Asset_Name);
                        intent.putExtra("TaskId", TaskId);
                        intent.putExtra("Scan_Type", Scan_Type);
                        intent.putExtra("User_Id", User_Id);
                        intent.putExtra("unplanned", unplanned);
                        intent.putExtra("AssetId", AssetId);
                        intent.putExtra("Form_Id", Form_IdIntent);
                        intent.putExtra("AssetCode", assetCode);
                        intent.putExtra("Asset_Location", Asset_Location);
                        intent.putExtra("Asset_Status", Asset_Status);
                        intent.putExtra("User_Group_Id", User_Group_Id);
                        intent.putExtra("Incident", 0);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return qualifiactionSpinner;
    }


    private LinearLayout branchTextlinearlayout(int Mandatory, String field_Label,int id,String section,int sid) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        textBranchType = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textBranchType.setMargins(0,0,0,25);
        radioLayout.setLayoutParams(textBranchType);
        radioLayout.setBackgroundColor(Color.parseColor("#e0e0e0"));
        radioLayout.addView(textViewBranch(radioLayout,id,field_Label,section,sid));
        return radioLayout;
    }
    private LinearLayout meterTextlinearlayout(int Mandatory, String field_Label,int id,String Value) {
        final LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        String[] label = field_Label.split(",");
        for(int i = 0; i< label.length;i++){
            radioLayout.addView(textView(label[i]));
            radioLayout.addView(editText(Mandatory,label[i],id,Value));
        }
        return radioLayout;
    }
    private EditText editText(final int mandatory, String name,final int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(SafeRange==1){
            editText.setHint(name);
        }
        if(Completed.equals("Completed")){
            editText.setEnabled(false);
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(editTextAddList!=null){
                    editTextCalc(id);
                }
            }
        });

        try {
            editTextAddList.add(editText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return editText;
    }

    public void AlertCheck(){
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
                    Double val = Double.parseDouble(slnew);
                    if (val > Double.parseDouble(field_Limit_To1) || val < Double.parseDouble(field_Limit_Form1)) {
                        editText1.setText("");
                        editText1.setError("Invalid Reading.Please Enter valid Reading");
                    } else if ((val < Double.parseDouble(threshold_From1) && (val >= Double.parseDouble(field_Limit_Form1))) || (val <= Double.parseDouble(field_Limit_To1)) && (val > Double.parseDouble(threshold_To1))) {
                        editText1.setError("Alert will generate.");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            parameter.close();
            db.close();
        }
    }


    private void editTextCalc(int id) {
        EditText addedValue = new EditText(this);
        addedValue.setId(id);
        ArrayList<String> valuesgot = new ArrayList<>();
        try {
            int Add=0;
            for(int i=0;i< editTextAddList.size()-1;i++){
                String value = editTextAddList.get(i).getText().toString();
                valuesgot.add(value);
                if(value.equals("")){
                    Add += 0;
                } else {
                    Add+=Integer.parseInt(value);
                }
            }
            if(editTextAddList.size() >= 2) {
                if (editTextAddList != null && !editTextAddList.isEmpty()) {
                    try {
                        editTextAddList.get(editTextAddList.size()-1).setText(Add+"");
                        editTextAddList.get(editTextAddList.size()-1).setEnabled(false);
                        addedValue.setText(valuesgot.get(0)+","+valuesgot.get(1)+","+Add);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            editTextaddition.add(addedValue);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private TextView textViewBranch(final LinearLayout linearLayout, final int id, String label, final String Section_Id, final int sid) {
        final TextView textView = new TextView(this);
        final LinearLayout linLayout = new LinearLayout(getApplicationContext());
        linLayout.setOrientation(LinearLayout.VERTICAL);
        linLayout.setLayoutParams(textLayout);
        textView.setTextSize(20);
        textView.setText(label+":");
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(textLayout);
        textViewListBranch.add(textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final int childCount = linLayout.getChildCount();
                    if(LOG) Log.d("ChildCount"," "+childCount+" "+on+" "+id+" "+ID);
                    if(ID != id) {
                        ID = id;
                        if (childCount == 0) {
                            try {
                                linearLayout.addView(linLayout);
                                linLayout.setVisibility(View.VISIBLE);
                                if(LOG) Log.d("Test", "Section " + linLayout.getVisibility());
                                textView.setTextColor(Color.GRAY);
                                sectionForm(Section_Id, sid, linLayout);
                                on = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (linLayout.getVisibility()==View.GONE && childCount != 0) {
                            try {
                                linearLayout.addView(linLayout);
                                linLayout.setVisibility(View.VISIBLE);
                                if(LOG) Log.d("Test", "GetSection " + linLayout.getVisibility());
                                textView.setTextColor(Color.GRAY);
                                //sectionForm(Section_Id, sid, linLayout);
                                on = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (linLayout.getVisibility()==View.VISIBLE && childCount != 0) {
                            try {
                                linLayout.setVisibility(View.GONE);
                                if(LOG) Log.d("Test", "RemoveView " + linLayout.getVisibility());
                                textView.setTextColor(getResources().getColor(R.color.bg_main));
                                //removeRadioremark(linLayout, 1);
                                linearLayout.removeView(linLayout);
                                on = false;
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if(ID==id){
                        if (linLayout.getVisibility()==View.GONE && childCount != 0) {
                            try {
                                linearLayout.addView(linLayout);
                                linLayout.setVisibility(View.VISIBLE);
                                if(LOG) Log.d("Test", "GetSection " + linLayout.getVisibility());
                                textView.setTextColor(Color.GRAY);
                                //sectionForm(Section_Id, sid, linLayout);
                                on = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (linLayout.getVisibility()==View.VISIBLE && childCount != 0) {
                            try {
                                linLayout.setVisibility(View.GONE);
                                if(LOG) Log.d("Test", "RemoveView " + linLayout.getVisibility());
                                textView.setTextColor(getResources().getColor(R.color.bg_main));
                                //removeRadioremark(linLayout, 1);
                                linearLayout.removeView(linLayout);
                                on = false;
                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        return textView;
    }

    private EditText editTextareaName(String name, int id) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(textLayout);
        //if(Completed.equals("Completed")){
        editText.setEnabled(false);
        //}
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(2);
        editText.setMaxLines(5);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);
        editText.setFilters(new InputFilter[]{filter});
        editText.setText(EmployeeName);
        editTextareaList.add(editText);
        return editText;
    }

    private LinearLayout checkboxlinearlayout(int Mandatory, String field_Label,int id,String Value,String field_Limit_Form,String field_Limit_To,String threshold_From, String threshold_To,String validation_Type,int SafeRange, final int calculationvalue) {
        LinearLayout checkboxLinear = new LinearLayout(getApplicationContext());
        checkboxLinear.setOrientation(LinearLayout.VERTICAL);
        checkboxLinear.setLayoutParams(textLayout);
        String[] CbLabel = field_Label.split("\\|");
        checkboxLinear.addView(textViewIncident(CbLabel[0]));
        checkboxLinear.addView(checkBox(checkboxLinear, CbLabel[0], CbLabel[1], Value, id, field_Limit_To, field_Limit_Form, threshold_To, threshold_From, SafeRange,calculationvalue));
        checkboxLinear.addView(editTextWithCb(Mandatory, CbLabel[0], id, Value, field_Limit_To, field_Limit_Form, threshold_To, threshold_From, SafeRange,calculationvalue));
        return checkboxLinear;
    }

    public CheckBox checkBox(final LinearLayout checkboxLayout, final String Label1, final String Label2, String Value, final int id, final String field_Limit_To, final String field_Limit_Form, final String threshold_To, final String threshold_From, final int SafeRange, final int calculationvalue){
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(Label2);
        if(Value.equals(Label2)){
            checkBox.setChecked(true);
        }
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    removeEditTextWithCb(checkboxLayout, Label2, id);
                } else {
                    for (EditText editTextCb : editTextWithCbList) {
                        if (editTextCb.getId() == id) {
                            editTextWithCbList.clear();
                            checkboxLayout.addView(editTextWithCb(Mandatory, Label1, id, "", field_Limit_To, field_Limit_Form, threshold_To, threshold_From, SafeRange,calculationvalue));
                        }

                    }
                }
            }
        });
        return checkBox;
    }

    private void removeEditTextWithCb(LinearLayout layout,String cbLabel,int id) {

        for(EditText editTextCb : editTextWithCbList){
            if(editTextCb.getId()==id){
                editTextCb.setText(cbLabel);
                layout.removeView(editTextCb);
            }

        }

    }

    private EditText editTextWithCb(final int mandatory, String name, int id, String setText,final String Field_Limit_To,final String Field_Limit_From,final String Threshold_To,final String Threshold_From,final int SafeRange,final int calculationvalue) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);

        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(Completed.equals("Completed")){
            editText.setEnabled(false);
        }
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
        editTextWithCbList.add(editText);
        return editText;
    }


    private LinearLayout radiofeedbacklinearlayout(String field_Label,String field_option,int id,String setText) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(radiofeedbackgroup(field_Label,field_option, id, setText, radioLayout, Field_Option_Id));


        return radioLayout;
    }

    private RadioGroup radiofeedbackgroup(String labelvalue, String optionRadio, final int id, String setText, final LinearLayout linearLayout, final String Field_Option_Id) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        radioGroup.setLayoutParams(textLayout);

        final ImageView imageView = new ImageView(DynamicForm.this);
        final int idimageview = id+5;
        linearLayout.addView(imageView);
        imageView.setId(idimageview);


        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == 3){
                    label_value = myDb.getfieldLabel(id);
                    imageView.setVisibility(View.VISIBLE);
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, idimageview);
                    drawableRadioImage.put(idimageview, imageView);
                } else {
                    imageView.setVisibility(View.GONE);
                    drawableRadioImage.remove(idimageview);
                }

                feedbackCalucate();
            }
        });
        textRadioFeedbackList.add(radioGroup);
        return radioGroup;
    }


    private EditText editTextConsumption(final int Id,final String getData, final String Value) {
        final EditText editText = new EditText(this);
        editText.setId(Id);

        editText.setLayoutParams(textLayout);
        // DecimalFormat precision = new DecimalFormat("0.0000");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        /*editText.setFocusable(false);
        editText.setKeyListener(null);*/
        editText.setText(Value);
        if(Completed.equals("Completed") || !Value.equals("")){
            editText.setEnabled(false);
        }
        //editText.setText(new applicationClass().yymmddhhmmss());
    /*    editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getData.equals("D"))
                    datePicker(editText);
            }
        });*/
        editTextConsumption.add(editText);
        return editText;
    }

    private EditText Score(String name, int id, String setText,int calculate) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setEnabled(false);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(LOG) Log.d("Teasreasdasd",calculate+"");
        editTextListFeedback.put(calculate, editText);
        return editText;
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void feedbackCalucate(){
        double totalfeedbackRadio= 0.0;
        for (RadioGroup rdgrp : textRadioFeedbackList) {
            String selectRB = "";
            int cbid = rdgrp.getId();
            //double totalfeedback= 0.0;
            try {
                int selectedId = rdgrp.getCheckedRadioButtonId();
                View rb1 = rdgrp.findViewById(selectedId);
                int idx = rdgrp.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                String Form_Structure_Id = myDb.getfieldId(cbid);
                if (radioButton.isChecked()) {
                    selectRB = radioButton.getText().toString();
                    totalfeedbackRadio += myDb.feedbackScore(selectRB);

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

        totalfeedback +=totalfeedbackRadio;
        if(LOG)Log.d("Totalscore",totalfeedback+" "+ textRadioFeedbackList.size() +" "+totalfeedbackRadio );

        Iterator it = editTextListFeedback.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer,EditText> pair = (Map.Entry)it.next();
            EditText score = pair.getValue();
            score.setId(pair.getValue().getId());
            double totalvalue = textRadioFeedbackList.size() * 4;
            if(LOG)Log.d("Calasdasdasdaxsd",pair.getKey()+"");
            if(LOG)Log.d("Testinvalues", totalvalue + " " + totalfeedbackRadio + " " + totalfeedbackRadio / totalvalue + " " + (totalfeedbackRadio / totalvalue)*pair.getKey());
            DecimalFormat df2 = new DecimalFormat("#.##");
            score.setText(df2.format(totalfeedbackRadio)+"");
            editTextListFeedback.replace(pair.getKey(),score);

                        /*myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(User_Id));
                        it.remove(); // avoids a ConcurrentModificationException*/
        }
    }


    private RadioGroup radiogroupMeterConsumption(String optionRadio, int id, String setText,final int calucationvalue,final LinearLayout linearLayout) {

        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());

        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        textRadioGroupConsumption.add(radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(linearLayout != null){
                    uomChangeConsumption(textRadioGroupConsumption, calucationvalue,linearLayout);
                }


            }
        });

        //uomChange(textRadioGroupMeterList);
        return radioGroup;
    }

    /*public void uomChangeConsumption(List<RadioGroup> radioGroups,final int calucationvalue,LinearLayout linearLayout) {

        //final EditText editTextConsumptionValue,consumptionShowEdittext;

        final int childCount = linearLayout.getChildCount();
        if(LOG)Log.d("Vasdasdasd",childCount+" ");
        View editTextFirstView,editTextSecondView,RadioGroupFirstView,RadioGroupSecondView,editTextConsumptionView;

        editTextFirstView = linearLayout.getChildAt(1);
        editTextSecondView = linearLayout.getChildAt(5);
        RadioGroupFirstView = linearLayout.getChildAt(3);
        RadioGroupSecondView = linearLayout.getChildAt(7);
        editTextConsumptionView = linearLayout.getChildAt(9);

        RadioGroup RadioGroupFirst,RadioGroupSecond;
        EditText editTextFirst,editTextSecond,editTextConsumption;

        RadioGroupFirst = (RadioGroup) RadioGroupFirstView;
        RadioGroupSecond = (RadioGroup) RadioGroupSecondView;


        editTextFirst = (EditText) editTextFirstView;
        editTextSecond = (EditText) editTextSecondView;
        editTextConsumption = (EditText) editTextConsumptionView;


        try {
            int selectedId = RadioGroupFirst.getCheckedRadioButtonId();
            View rb1 = RadioGroupFirst.findViewById(selectedId);
            int idx = RadioGroupFirst.indexOfChild(rb1);
            RadioButton radioButton = (RadioButton) RadioGroupFirst.getChildAt(idx);

            int selectedId2 = RadioGroupSecond.getCheckedRadioButtonId();
            View rb2 = RadioGroupSecond.findViewById(selectedId2);
            int idx2 = RadioGroupSecond.indexOfChild(rb2);
            RadioButton radioButton2 = (RadioButton) RadioGroupSecond.getChildAt(idx2);

            if (LOG) {
                int selectedId3 =

                        Log.d("ValuewOnEditText",radioButton.getText().toString() +" "+editTextFirst.getText().toString() +" "+radioButton2.getText().toString() +" "+editTextSecond.getText().toString() +" "+editTextConsumption.getText().toString());
            }

            if (radioButton.isChecked()&& radioButton2.isChecked() && !editTextFirst.getText().toString().equals("") && !editTextSecond.getText().toString().equals("")) {
                //selectRB = radioButton.getText().toString();
                // Log.d("Consumption",selectRB);
                firstRadio = radioButton.getText().toString();
                SecondRadio = radioButton2.getText().toString();

                firstVal = Double.parseDouble(editTextFirst.getText().toString());
                secondVal = myDb.Conversion(Double.parseDouble(editTextSecond.getText().toString()),firstRadio,SecondRadio);

                *//*if(radioButton.getText().toString().equals("KWH") && radioButton2.getText().toString().equals("MWH")){
                    editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                    editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString()) * 1000;
                    SecondRadio = "KWH";

                }else if(radioButton.getText().toString().equals("MWH") &&  radioButton2.getText().toString().equals("KWH")) {
                    editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                    editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString()) / 1000;
                    SecondRadio = "MWH";

                }else if (radioButton.getText().toString().equals(radioButton2.getText().toString())) {
                    editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                    editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString());
                }*//*


                //KWH -> GWH && GWH -> KWH
            }*//*if(radioButton.getText().toString().equals("KWH") && radioButton2.getText().toString().equals("GWH")) {
                editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString())*1000000;
                SecondRadio = "KWH";
            }else if(radioButton.getText().toString().equals("GWH") && radioButton2.getText().toString().equals("KWH")) {
                editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString()) / 1000000;
                SecondRadio = "GWH";
            }else if (radioButton.getText().toString().equals(radioButton2.getText().toString())) {
                editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString());


                //MWH -> GWH && GWH -> MWH
            }if(radioButton.getText().toString().equals("MWH") && radioButton2.getText().toString().equals("GWH")) {
                editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString())*1000;
                SecondRadio = "MWH";
            }else if(radioButton.getText().toString().equals("GWH") && radioButton2.getText().toString().equals("MWH")) {
                editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString()) / 1000;
                SecondRadio = "GWH";
            }else if (radioButton.getText().toString().equals(radioButton2.getText().toString())) {
                editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString());

            }else  if(radioButton.getText().toString().equals(radioButton2.getText().toString())) {
                editTextFirstValue = Double.parseDouble(editTextFirst.getText().toString());
                editTextSecondValue = Double.parseDouble(editTextSecond.getText().toString());
            }*//*



            if(secondVal < firstVal) {
                Toast.makeText(getApplicationContext(), "Intial Reading Can't be Greater Than Final Reading", Toast.LENGTH_LONG).show();
                editTextConsumption.setText("");
                radioButton2.setChecked(false);
                //editTextSecond.setText("");
                for(EditText editText:editTextConsumptionData){
                    if(editText.getId() == editTextConsumption.getId()){
                        //editTextConsumptionValue = editText;
                        editText.setText("");
                    }
                }
            }else {
                double diff = secondVal - firstVal ;
                double diffSeconds = diff * calucationvalue;
                DecimalFormat format = new DecimalFormat("0.0000");
                editTextConsumption.setText(format.format(diffSeconds) + " " + firstRadio);

                for(EditText editText:editTextConsumptionData){
                    if(editText.getId() == editTextConsumption.getId()){
                        //editTextConsumptionValue = editText;
                        editText.setText(firstVal + ","+secondVal+","+diffSeconds+" "+firstRadio+","+ SecondRadio+","+ firstRadio);

                        //editText.setText("");
                    }
                }


                if(LOG)Log.d("TestValuesGot",firstVal + ","+secondVal+","+diffSeconds+" "+firstRadio+","+ SecondRadio+","+ firstRadio);

            }
        } catch (NullPointerException e) {
            System.out.println("fbi540 ERROR==" + e);
        } catch (Exception e) {
            System.out.println("fd1303 ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: fd1303", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }*/

    public void uomChangeConsumption(List<RadioGroup> radioGroups,final int calucationvalue,LinearLayout linearLayout) {

        //final EditText editTextConsumptionValue,consumptionShowEdittext;
        final int childCount = linearLayout.getChildCount();
        if(LOG) Log.d("Vasdasdasd",childCount+" ");
        View editTextFirstView,editTextSecondView,RadioGroupFirstView,RadioGroupSecondView,editTextConsumptionView;

        editTextFirstView = linearLayout.getChildAt(1);
        editTextSecondView = linearLayout.getChildAt(5);
        RadioGroupFirstView = linearLayout.getChildAt(3);
        RadioGroupSecondView = linearLayout.getChildAt(7);
        editTextConsumptionView = linearLayout.getChildAt(9);

        RadioGroup RadioGroupFirst,RadioGroupSecond;
        EditText editTextFirst,editTextSecond,editTextConsumption;

        RadioGroupFirst = (RadioGroup) RadioGroupFirstView;
        RadioGroupSecond = (RadioGroup) RadioGroupSecondView;

        editTextFirst = (EditText) editTextFirstView;
        editTextSecond = (EditText) editTextSecondView;
        editTextConsumption = (EditText) editTextConsumptionView;

        try {
            int selectedId = RadioGroupFirst.getCheckedRadioButtonId();
            View rb1 = RadioGroupFirst.findViewById(selectedId);
            int idx = RadioGroupFirst.indexOfChild(rb1);
            RadioButton radioButton = (RadioButton) RadioGroupFirst.getChildAt(idx);

            int selectedId2 = RadioGroupSecond.getCheckedRadioButtonId();
            View rb2 = RadioGroupSecond.findViewById(selectedId2);
            int idx2 = RadioGroupSecond.indexOfChild(rb2);
            RadioButton radioButton2 = (RadioButton) RadioGroupSecond.getChildAt(idx2);

            if(LOG) {
                int selectedId3 = Log.d("ValuewOnEditText", radioButton.getText().toString() + " " + editTextFirst.getText().toString() + " " + radioButton2.getText().toString() + " " + editTextSecond.getText().toString() + " " + editTextConsumption.getText().toString());
            }

            if (radioButton.isChecked() && radioButton2.isChecked() && !editTextFirst.getText().toString().equals("") && !editTextSecond.getText().toString().equals("")) {
                //selectRB = radioButton.getText().toString();
                if(LOG) Log.d("Consumption",selectRB);
                firstRadio = radioButton.getText().toString();
                SecondRadio = radioButton2.getText().toString();

                firstVal = Double.parseDouble(editTextFirst.getText().toString());
                secondVal = myDb.Conversion(Double.parseDouble(editTextSecond.getText().toString()),firstRadio,SecondRadio);
                }


            if(secondVal < firstVal) {
                Toast.makeText(getApplicationContext(), "Initial Reading Can't be Greater Than Final Reading", Toast.LENGTH_LONG).show();
                editTextConsumption.setText("");
                radioButton2.setChecked(false);
                //editTextSecond.setText("");
                for(EditText editText:editTextConsumptionData){
                    if(editText.getId() == editTextConsumption.getId()){
                        //editTextConsumptionValue = editText;
                        editText.setText("");
                    }
                }
            } else {
                double diff = secondVal - firstVal;
                double diffSeconds = diff * calucationvalue;
                DecimalFormat format = new DecimalFormat("0.0000");
                editTextConsumption.setText(format.format(diffSeconds) + " " + firstRadio);

                for(EditText editText:editTextConsumptionData){
                    if(editText.getId() == editTextConsumption.getId()){
                        //editTextConsumptionValue = editText;
                        editText.setText(firstVal + ","+secondVal+","+diffSeconds+" "+firstRadio+","+ SecondRadio+","+ firstRadio);
                        //editText.setText("");
                    }
                }
                if(LOG) Log.d("TestValuesGot",firstVal + ","+secondVal+","+diffSeconds+" "+firstRadio+","+ SecondRadio+","+ firstRadio);
            }

        } catch (NullPointerException e) {
            System.out.println("fbi540 ERROR==" + e);
        } catch (Exception e) {
            System.out.println("fd1303 ERROR==" + e);
            Toast.makeText(getApplicationContext(), "Error code: fd1303", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private LinearLayout consumptionlinearlayout(int Mandatory, String field_Label,String Field_Options, int id,String getData,String Value,String Field_Id,int calcutionvalue) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        String previousReading = "No Previous Reading";
        if(!Completed.equals("Completed")){
            previousReading = myDb.lastFinalReading(Field_Id,frequencyId);
        }
        String[] label = field_Label.split(",");
        for(int i = 0;i<label.length-1;i++){
            String[] labelValue = label[i].split("\\|");
            if(LOG)Log.d("TestingValue", label[i] + " " + " " + labelValue);
            if(i == 0){
                if(previousReading.equals("No Previous Reading")){
                    if(Completed.equals("Completed")) {
                     /*   radioLayout.addView(textView(labelValue[0]));
                        radioLayout.addView(editTextConsumption(id, Readingvalue[0], Readingvalue[0]));
                        radioLayout.addView(textView(labelValue[1]));
                        radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i, UOMvalue[0],calcutionvalue));*/
                    }else {
                        radioLayout.addView(textView(labelValue[0]));
                        radioLayout.addView(editTextConsumption(id,"", ""));
                        radioLayout.addView(textView(labelValue[1]));
                        radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i, "",calcutionvalue,null));
                    }


                }else{
                    String[] previousReadingSplite = previousReading.split(",");
                    if(LOG)Log.d("Teasdasdasxdcasdasd",previousReading + " "+ previousReadingSplite[0] +" "+ previousReadingSplite[1]);
                    radioLayout.addView(textView(labelValue[0]));
                    radioLayout.addView(editTextConsumption(id, getData, previousReadingSplite[0]));
                    radioLayout.addView(textView(labelValue[1]));
                    radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i,  previousReadingSplite[1],calcutionvalue,null));
                }

            }else {
                if(Completed.equals("Completed")) {
                   /* radioLayout.addView(textView(labelValue[0]));
                    radioLayout.addView(editTextConsumption(id, getData, Readingvalue[1]));
                    radioLayout.addView(textView(labelValue[1]));
                    radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i, UOMvalue[2],calcutionvalue));*/
                }else {
                    radioLayout.addView(textView(labelValue[0]));
                    radioLayout.addView(editTextConsumption(id, getData, ""));
                    radioLayout.addView(textView(labelValue[1]));
                    radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i, "",calcutionvalue,radioLayout));
                }

            }

            /*radioLayout.addView(textView(label[1]));
            radioLayout.addView(editTextConsumption(id, getData, value[1]));
            radioLayout.addView(textView(label[2]));*/
        }

       /* radioLayout.addView(textView(label[0]));
        radioLayout.addView(editTextConsumption(id, getData, value[0]));
        radioLayout.addView(textView(label[1]));
        radioLayout.addView(editTextConsumption(id, getData, value[1]));
        radioLayout.addView(textView(label[2]));*/
        if(Completed.equals("Completed")) {
            /*radioLayout.addView(textView(label[label.length - 1]));
            radioLayout.addView(editTextConsumptionReading(Mandatory, label[label.length - 1], id,Readingvalue[2]+" "+UOMvalue[2] , calcutionvalue));*/
        }else {
            radioLayout.addView(textView(label[label.length - 1]));
            radioLayout.addView(editTextConsumptionReading(Mandatory, label[label.length - 1], id, "", calcutionvalue));
        }
        return radioLayout;
    }

    private EditText editTextConsumptionReading(final int mandatory, String name, int id,String SetText, final int calucationvalue) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setFocusable(false);
        editText.setText(SetText);
        editText.setKeyListener(null);
        editText.setClickable(true);
        editText.setHint("Consumption");
        if(Completed.equals("Completed")){
            editText.setEnabled(false);
        }
        final EditText editText1 = new EditText(this);
        editText1.setId(id);
        editTextConsumptionShow.add(editText);
        editTextConsumptionData.add(editText1);
        return editText;
    }


    private LinearLayout dglinearlayout(int Mandatory, String field_Label, int id, String getData, String Value) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        String[] value = Value.split(",");
        String[] label = field_Label.split("\\|");
        if(LOG)Log.d("abcdValueee", field_Label + ":" + Value+" "+label[0]+" "+label[1]);
        radioLayout.addView(textView(label[0]));
        radioLayout.addView(editTextDGTime(id, getData, value[0]));
        radioLayout.addView(textView(label[1]));
        radioLayout.addView(editTextDGTime(id, getData, value[1]));
        return radioLayout;
    }

    private EditText editTextDGTime(final int Id,final String getData, final String Value) {
        final EditText editText = new EditText(this);
        editText.setId(Id);
        editText.setLayoutParams(textLayout);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setKeyListener(null);
        editText.setText(Value);
        if(Completed.equals("Completed")){
            editText.setEnabled(false);
        }
        //editText.setText(new applicationClass().yymmddhhmmss());
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getData.equals("D"))
                    datePicker(editText);
            }
        });
        editTextDGTimeList.add(editText);
        return editText;
    }

    private EditText editTextMeter(final int mandatory, String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setText(setText);
        editText.setLayoutParams(fittype2);
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
            editText.setEnabled(false);
        }
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);
        editTextListMeter.add(editText);
        return editText;
    }

    private EditText editText(final int mandatory, String name, int id, String setText, final String Field_Limit_From, final String Field_Limit_To, final String Threshold_From, final String Threshold_To, String Validation_Type,int SafeRange,final int calculationvalue) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(SafeRange==1){
            editText.setHint(name);
        }
        if(Completed.equals("Completed")){
            editText.setEnabled(false);
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

    private LinearLayout radiowithImagelinearlayout(int Mandatory,String field_Label,String field_option,final int id,String setText,final int Field_Option_Id) {
        final LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        radioLayout.addView(radioGroup);
        String[] optionRadioList = field_option.split(",");

        /*textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);*/

        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        final ImageView imageView = new ImageView(DynamicForm.this);
        final int idimageview = id+5;
        radioLayout.addView(imageView);
        imageView.setId(idimageview);
        imageView.setImageResource(R.drawable.ic_cam);
        imageView.getLayoutParams().height = 150;
        imageView.getLayoutParams().width = 150;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LOG)Log.d("asdasdcasdas","Imagasdcxada Clicked" + idimageview);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, idimageview);
            }
        });
        imageView.setVisibility(View.GONE);
        if (Mandatory == 1){
            imageView.setVisibility(View.VISIBLE);
            drawableRadioImage.put(idimageview, imageView);
        }else {
            imageView.setVisibility(View.GONE);
            drawableRadioImage.remove(idimageview);
        }
        textRadioGroupList.add(radioGroup);
        return radioLayout;
    }

    private LinearLayout radiowithRemarklinearlayout( String field_Label,String field_option,final int id,String setText,final int Field_Option_Id) {
        final LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        radioLayout.addView(radioGroup);
        String[] optionRadioList = field_option.split(",");

        /*textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);*/

        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (Field_Option_Id == selectedId) {
                    radioLayout.addView(editTextarea("Remark", id, ""));
                } else {
                    radioLayout.removeView(editTextarea("Remark",id,""));
                }
            }
        });
        textRadioGroupList.add(radioGroup);
        return radioLayout;
    }

    private LinearLayout radiowithImageandRemarklinearlayout(String field_Label,String field_option,final int id,String setText,final int Field_Option_Id) {
        final LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        radioLayout.addView(radioGroup);
        String[] optionRadioList = field_option.split(",");

        /*textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);*/

        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        final ImageView imageView = new ImageView(DynamicForm.this);
        final int idimageview = id+5;
        radioLayout.addView(imageView);
        imageView.setId(idimageview);
        imageView.setImageResource(R.drawable.ic_cam);
        imageView.getLayoutParams().height = 150;
        imageView.getLayoutParams().width = 150;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LOG)Log.d("asdasdcasdas","Imagasdcxada Clicked" + idimageview);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, idimageview);
            }
        });
        imageView.setVisibility(View.GONE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (Field_Option_Id == selectedId) {
                    imageView.setVisibility(View.VISIBLE);
                    drawableRadioImage.put(idimageview, imageView);
                    radioLayout.addView(editTextarea("Remark", id, ""));
                } else {
                    imageView.setVisibility(View.GONE);
                    drawableRadioImage.remove(idimageview);
                }
            }
        });
        textRadioGroupList.add(radioGroup);
        return radioLayout;
    }

    private LinearLayout radiowithradiolinearlayout(int Mandatory, String field_Label,String Field_Options, int id,String getData,String Value,String Field_Id) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);

        String[] label = field_Label.split(",");
        String[] FieldOption = Field_Options.split("\\|");
        radioLayout.addView(textView(label[0]));
        radioLayout.addView(textView(label[1]));
        radioLayout.addView(radiogroupRadioRadio(FieldOption[0], id, getData));
        radioLayout.addView(textView(label[2]));
        radioLayout.addView(radiogroupRadioRadio(FieldOption[1], id + 1, getData));
        return radioLayout;
    }

    private RadioGroup radiogroupRadioRadio(String optionRadio, int id, String setText) {

        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());

        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        textRadioGroupRadioRadio.add(radioGroup);
        return radioGroup;
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
        if (!intentFromReset.equals("Opening")) {
            uomChange(textRadioGroupMeterList);
        }
        return radioGroup;
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

        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(radiogroup(field_option, id, setText, radioLayout, Field_Option_Id));
        return radioLayout;
    }

    private LinearLayout radioScorelinearlayout(String field_Label, String form_strucure_id,String field_option,int id,String setText,String Field_Option_Id) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        if(LOG) Log.d("ASDASDASDASDDAS", field_Label+"Form_Struct_Id:"+form_strucure_id);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(radiogroupScore(field_option, id, setText, Field_Option_Id));
        return radioLayout;
    }

    private RadioGroup radiogroupScore(final String optionRadio, final int id, String setText, final String Field_Option_Id) {

        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);

        String[] optionRadioList = optionRadio.split(",");
        final TextView textView = new TextView(DynamicForm.this);
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

            @SuppressLint("NewApi")
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
        textRadioGroupScoreList.add(radioGroup);
        return radioGroup;
    }


    private RadioGroup radiogroup(String optionRadio, int id, String setText,final String Field_Option_Id) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);

        String[] optionRadioList = optionRadio.split(",");
        final TextView textView = new TextView(DynamicForm.this);
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

    private RadioGroup radiogroup(String optionRadio, int id, String setText, final LinearLayout linearLayout,final String Field_Option_Id) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);

        String[] optionRadioList = optionRadio.split(",");
        final TextView textView = new TextView(DynamicForm.this);
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
  /*  private RadioGroup radiogroup(String optionRadio, int id, String setText) {
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
    }*/
    private RadioButton radioButton(String strvalue, int SelectedId, String matchvalue) {   //965

        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(SelectedId);
        if(Completed.equals("Completed")||Completed.equals("Delayed")) {
           radioButton.setEnabled(false);
        }
        if (matchvalue.equals(strvalue)) {
            radioButton.setChecked(true);
        }
        textRadioButtonList.add(radioButton);
        return radioButton;
    }

    private Spinner qualifiaction(String options, int sp_id, String qval,LinearLayout linearLayout, final String Field_Option_Id,final String Section_Id) {
        Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("-- Select One --");

        final TextView textView = new TextView(DynamicForm.this);
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
        if(Completed.equals("Completed")){
            qualifiactionSpinner.setEnabled(false);
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

/*    private Spinner qualifiaction(String options, int sp_id, String qval) {
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
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
            qualifiactionSpinner.setEnabled(false);
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
    }*/


/*
    public void sectionFormSpinner(String Sections_Intent,LinearLayout formLayout,int position){//,int Sid_Intent and sid='"+Sid_Intent+"'
        String formQuery = "select * from Form_Structure where Form_Id='" + Sections_Intent + "' and Record_Status <> 'D'  ORDER BY Display_Order ASC";//and sid='" + position + "'
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
                            "Threshold_From ,Threshold_To,Validation_Type,Critical FROM Parameter WHERE Activity_Frequency_Id = '"+Frequency_Id+"'" +
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
                Log.d("Testinbvalsdasd",Field_Type +" "+ formLayout.getId());
                */
/*if (Field_Type.equals("meter")) {
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
                }*//*


                if (Field_Type.equals("date")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextDate(Id, "", Value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("time")) {

                    try {
                        //formLayout.addView(textView(Field_Label));
                        formLayout.addView(timeeditText(Field_Label,Id, Value));
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
                if (Field_Type.equals("dropdownbranching")) {

                    try {
                        */
/*formLayout.addView(textView(Field_Label));
                        formLayout.addView(qualifiaction(Field_Options, Id, "", null, "", section));*//*

                        if (parameterCount==0) {
                            formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", "",section,sid));
                        } else {
                            formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id, section,sid));
                        }
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
                        formLayout.addView(radiogroup(Field_Options, Id, "", null, Field_Option_Id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



                if (Field_Type.equals("datetime")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextDateTime(Id, "", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
              */
/*  if (Field_Type.equals("dg")) {

                    try {
                        formLayout.addView(dglinearlayout(Mandatory, Field_Label, Id,"",""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*//*

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
                        formLayout.addView(editTextRemark(Field_Label, Id, Value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (Field_Type.equals("signature")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(Signature(Field_Label, Id, "Createform"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }
*/

    private EditText editTextDateTime(final int Id, final String Value) {
        final EditText editText = new EditText(this);
        editText.setId(Id);

        editText.setLayoutParams(fittype2);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setKeyListener(null);
        editText.setText(applicationClass.yymmddhhmmss());
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Value.equals("D"))
                    datePicker(editText);
            }
        });
        editTextDateTimeList.add(editText);
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
    private Date parseDate(String date) {
        SimpleDateFormat inputParser = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }
    private EditText editTextarea(String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(fittype2);
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
            editText.setEnabled(false);
        }
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
        editTextareaList.add(editText);
        return editText;
    }

    private EditText editTextRemark(String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(fittype2);
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
           editText.setEnabled(false);
        }
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

    private ImageView Signature(String name, final int id, final String setText) {
        byte[] image="".getBytes();
        final ImageView Sign = new ImageView(this);
        Sign.setId(id);
        Sign.setImageResource(R.drawable.sign);
        formLayout.addView(Sign);
        Sign.getLayoutParams().height = 150;
        Sign.getLayoutParams().width = 150;

            Sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!setText.equals("getData")) {
                        dialog_action(Sign, id);
                    }
                }
            });

        try {
            db=myDb.getWritableDatabase();
            String formQuery1 = "select Image_Selfie from Task_Selfie where Task_Id='"+TaskId+"' and Image_Type='"+name+"'";
            Cursor img =db.rawQuery(formQuery1, null);
            if (img.getCount() > 0) {
                if (img.moveToNext()) {
                    do {
                        image = img.getBlob(0);
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(image);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Sign.setImageBitmap(bitmap);
                        Sign.getLayoutParams().height = 350;
                        Sign.getLayoutParams().width = 350;

                    } while (img.moveToNext());
                }
            }

            img.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageSignature.add(Sign);
        return Sign;
    }

    public void dialog_action(final ImageView sign,final int id) {

        dialog = new Dialog(DynamicForm.this);
        // Removing the features of Normal Dialogs
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);

        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        mSignature.clear();
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.v("log_tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath, sign,id);
                mSignature.clear();
               /* dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
                // Calling the same class*/
                //recreate();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
                // Calling the same class
                //recreate();
            }
        });
        dialog.show();
    }

    public class signature extends View {

        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath,ImageView sign,int fieldId) {
            if (mBitmap == null) {
                mBitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(mBitmap);
            try {
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                mBitmap.compress(Bitmap.CompressFormat.PNG, 40, mFileOutStream);
                Bitmap drawImage = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
                sign.setImageBitmap(drawImage);
                sign.getLayoutParams().height = 500;
                sign.getLayoutParams().width = 500;
                dialog.dismiss();
                mSignature.clear();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }

        }


        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
    public boolean checkSubmitLowerReading() {
        boolean checkRadio = false;
        try {
            if (previoudReadings.size() >= 1){    /* for meter reading form */
                for (String readingValue : previoudReadings) {
                    if (readingValue != "No Previous Reading") {  /* if there is previous reading  */

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
                                                    checkRadio = true;
                                                    break;
                                                }
                                                if (b >= a) {
                                                    checkRadio = false;
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
                        checkRadio = false;
                    }
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if(checkRadio == true) {
            return true;
        }
        else
            return false;
    }

    public boolean checkSubmitEditTextAdd() {
        boolean editTextCheck = false;
        try {
            if (editTextAddList.size() == 0){
                editTextCheck = true;
            }else {
                for (EditText editLongText : editTextAddList) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(editTextCheck==true){
            return  true;
        }else return false;
    }

    public boolean checkSubmitEditText() {
        boolean editTextCheck = false;
        try {
            if (editTextList.size() == 0){
                editTextCheck = true;
            }else {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(editTextCheck==true){
            return  true;
        }else return false;
    }

    //For Unplanned Mandatory Remark
    public boolean checkSubmitRemark() {
        boolean remarkCheck = false;
        // unplanned == null
        if(Completed.equals("Pending") ){
            remarkCheck = true;
        } else {
        try {

            if (editTextRemarkList.size() == 0){
                remarkCheck = true;
            }else {
                for (EditText editLongText : editTextRemarkList) {
                    slnew = editLongText.getText().toString();
                    //edittextValidation.add(editLongText.getText().toString());
                    if (slnew.equals("")) {
                        remarkCheck = false;
                        editLongText.setError("Please Enter Value");
                        Snackbar snackbar = Snackbar.make(formLayout, "Please Complete the form !!", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        break;
                    } else {
                        remarkCheck = true;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        }
        if(remarkCheck==true){
            return  true;
        }else return false;
    }

    public boolean checkSubmitEditTextMeter() {
        boolean editTextMeterCheck = false;
        try {
            if (editTextListMeter.size() == 0) {
                editTextMeterCheck = true;
            }else {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(editTextMeterCheck==true){
            return  true;
        }else return false;
    }

    public boolean checkSubmitRadio() {
        boolean checkRadio = false;
        try {
            if (textRadioGroupList.size() == 0) {
                checkRadio = true;
            }else {
                for (RadioGroup rdgrp : textRadioGroupList) {

                    try {
                        if (rdgrp.getCheckedRadioButtonId() == -1) {
                            Snackbar snackbar = Snackbar.make(formLayout, "Please select value !!", Snackbar.LENGTH_SHORT);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(checkRadio==true){
            return  true;
        }else return false;
    }

    public boolean checkSubmitRadioMeter() {
        boolean checkRadioMeter = false;
        try {
            if (textRadioGroupMeterList.size() == 0) {
                checkRadioMeter = true;
            }else {
                for (RadioGroup rdgrp : textRadioGroupMeterList) {

                    try {
                        if (rdgrp.getCheckedRadioButtonId() == -1) {
                            Snackbar snackbar = Snackbar.make(formLayout, "Please select UOM !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            checkRadioMeter = false;
                            break;
                        } else {
                            checkRadioMeter = true;
                        }

                    } catch (NullPointerException e) {
                        System.out.println("fbi540 ERROR==" + e);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(checkRadioMeter==true){
            return  true;
        }else return false;
    }

    public boolean checkSpinnerValue(){

        boolean spinnerBoolean = true;

        if(textSpinnerList.size() !=0) {
            spinnerBoolean = false;
            for (Spinner textSpinner : textSpinnerList) {
                try {
                    int id_sp = textSpinner.getId();
                    String sspinner = String.valueOf(textSpinner.getSelectedItem());

                    if (sspinner.equalsIgnoreCase("-- Select One --")) {
                        Snackbar snackbar = Snackbar.make(formLayout, "Please select value from dropdown", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        spinnerBoolean = false;

                        break;
                    } else {
                        spinnerBoolean = true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return spinnerBoolean;
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
                        if(LOG) Log.d("Rasfjdskgfm"," "+radioButton.getText().toString());

                        Double cal = myDb.Conversion(a,radioButton.getText().toString(),c);

                        BigDecimal d = new BigDecimal(cal);
                        reading.setText("Previous Reading: " + d.setScale(3, BigDecimal.ROUND_HALF_UP)+ " "+radioButton.getText().toString());

                        /*if (radioButton.getId() == 0) {
                            if (c.equals("MW")) {
                                Double a2 = a * 1000;
                                BigDecimal d = new BigDecimal(a2);
                                reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP)+ " KW");
                                //reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP)+ " KW");
                            }
                        } else if(radioButton.getId() == 1){
                            if (c.equals("KW")) {

                                Double a2 = a / 1000;
                                BigDecimal d = new BigDecimal(a2);
                                reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP) + " MW");
                                //reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP) + " MW");
                            }
                        }*/

                    }
                }
            });
        }
    }

/*
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
                                //reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP)+ " KW");
                            }
                        } else if(radioButton.getId() == 1){
                            if (c.equals("KW")) {

                                Double a2 = a / 1000;
                                BigDecimal d = new BigDecimal(a2);
                                reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP) + " MW");
                                //reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP) + " MW");
                            }
                        }

                    }
                }
            });


        }
    }
*/


    String date;
    String slnew = "";
    ArrayList<String> edittextValidation = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap drawImage = photo.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(drawImage); //bmp is the bitmap to dwaw into
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(10);
            paint.setTextAlign(Paint.Align.CENTER);
            String printText = assetCode + "" + applicationClass.yymmddhhmm();
            paint.setAntiAlias(true);
            canvas.drawText(assetCode, 55, 10, paint);
            canvas.drawText(applicationClass.yymmddhhmm(),45 , 20, paint);

            drawableBitmap.put("Selfie",drawImage);
            taskSelfie.setImageBitmap(drawImage);
            taskSelfie.getLayoutParams().height = 500;
            taskSelfie.getLayoutParams().width = 500;

        }
        else if (requestCode == CAMERA_REQUESTAsset && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap drawImage = photo.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(drawImage); //bmp is the bitmap to dwaw into
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(7);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            String printText = assetCode + " " +applicationClass.yymmddhhmm();
            canvas.drawText(assetCode, 55, 10, paint);
            canvas.drawText(applicationClass.yymmddhhmm(),45 , 20, paint);
            drawableBitmap.put("AssetImage",drawImage);
            assetImage.setImageBitmap(drawImage);
            assetImage.getLayoutParams().height = 500;
            assetImage.getLayoutParams().width = 500;

        }else if(resultCode == Activity.RESULT_OK) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap drawImage = photo.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(drawImage); //bmp is the bitmap to dwaw into
            Paint paint = new Paint();
            String textval = label_value;
            int lengthval = textval.length();
            if(LOG)Log.d("valuestring", String.valueOf(textval.indexOf(" ",lengthval/2)));

            String firstText = textval.substring(0,textval.indexOf(" ",lengthval/2));
            String SecondText = textval.substring(textval.indexOf(" ",lengthval/2), lengthval);
            if(LOG)Log.d("kjsdfghkjdfhg  ", firstText);
            paint.setColor(Color.RED);
            paint.setTextSize(7);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            //String printText = assetCode + " " + new applicationClass().yymmddhhmm();
            //canvas.drawText(assetCode, 55, 10, paint);
            canvas.drawText(new applicationClass().yymmddhhmm(), 45, 20, paint);
            //drawableBitmap.put("AssetImage", drawImage);
            //AssetImages(drawImage, Id, "Createform", formLayout);
            //AssetImages(null, Id, "Createform", formLayout);
            //sumitbuttonShift(formLayout);
            Iterator it = drawableRadioImage.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer,ImageView> pair = (Map.Entry)it.next();

                if(LOG)Log.d("Tsdasdasd",requestCode+ " "+pair.getKey());
                if(requestCode == pair.getKey()){
                    ImageView imageView = pair.getValue();
                    imageView.setId(pair.getKey());
                    imageView.setImageBitmap(drawImage);
                    imageView.getLayoutParams().height = 500;
                    imageView.getLayoutParams().width = 500;
                    drawableRadioImage.replace(pair.getKey(),imageView);
                }
                //myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(User_Id));
                //it.remove(); // avoids a ConcurrentModificationException
            }

        }
    }

    public String taskInsert(String taskStatus){
        String Remarks="";

        for (EditText editTextarea1 : editTextRemarkList) {
            try {
                Remarks = editTextarea1.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ContentValues contentValues1 = new ContentValues();
        if (unplanned == null) {
            if(LOG)Log.d("PPMIntent",PPM_Intent);
            if (PPM_Intent.equals("PPMPending")){
                myDb.updatedPPMTaskDetails(TaskId,taskStatus,Completed, applicationClass.yymmddhhmmss(), Scan_Type, User_Id, User_Group_Id, Remarks);
            }else {
                myDb.updatedTaskDetails(TaskId, taskStatus, Completed, applicationClass.yymmddhhmmss(), Scan_Type, User_Id, Remarks,0);
            }


        } else {
            contentValues1.put("Auto_Id", TaskId);
            contentValues1.put("Company_Customer_Id", companyId);
            contentValues1.put("Site_Location_Id", SiteId);
            contentValues1.put("Activity_Frequency_Id", frequencyId);
            contentValues1.put("Task_Scheduled_Date", "0000-00-00 00:00:00");
            contentValues1.put("Task_Status", "Unplanned");
            contentValues1.put("Task_Start_At",applicationClass.yymmddhhmmss());
            contentValues1.put("Assigned_To", "U");
            contentValues1.put("EndDateTime", "[ Unplanned ]");
            contentValues1.put("Asset_Name", Asset_Name);
            contentValues1.put("Asset_Id",AssetId);
            contentValues1.put("From_Id",Form_IdIntent);
            contentValues1.put("Asset_Code",assetCode);
            contentValues1.put("Asset_Location", Asset_Location);
            contentValues1.put("Asset_Status", Asset_Status);
            contentValues1.put("Activity_Name", Activity_Name);
            contentValues1.put("Assigned_To_User_Id", User_Id);
            contentValues1.put("Assigned_To_User_Group_Id",User_Group_Id);
            contentValues1.put("Scan_Type", Scan_Type);
            contentValues1.put("Remarks", Remarks);
            contentValues1.put("UpdatedStatus", "no");
            db=myDb.getWritableDatabase();
            db.insert("Task_Details", null, contentValues1);
            db.close();
        }
        return  TaskId;
    }



/*
    public String taskInsert(){
        String Remarks="";
        for (EditText editTextarea1 : editTextRemarkList) {
            try {
                Remarks = editTextarea1.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ContentValues contentValues1 = new ContentValues();
        if (unplanned == null) {
            //myDb.updatedTaskDetails(TaskId,"Completed",new applicationClass().yymmddhhmmss(),Scan_Type,User_Id,Remarks);
            myDb.updatedTaskDetails(TaskId, taskStatus, Completed, applicationClass.yymmddhhmmss(), Scan_Type, User_Id, Remarks);

        } else {
            contentValues1.put("Auto_Id", TaskId);
            contentValues1.put("Company_Customer_Id", companyId);
            contentValues1.put("Site_Location_Id", SiteId);
            contentValues1.put("Activity_Frequency_Id", frequencyId);
            contentValues1.put("Task_Scheduled_Date", "0000-00-00 00:00:00");
            contentValues1.put("Task_Status", "Unplanned");
            contentValues1.put("Task_Start_At",new applicationClass().yymmddhhmmss());
            contentValues1.put("Assigned_To", "U");
            contentValues1.put("EndDateTime", "[ Unplanned ]");
            contentValues1.put("Asset_Name", Asset_Name);
            contentValues1.put("Asset_Id",AssetId);
            contentValues1.put("From_Id",Form_IdIntent);
            contentValues1.put("Asset_Code",assetCode);
            contentValues1.put("Asset_Location", Asset_Location);
            contentValues1.put("Asset_Status", Asset_Status);
            contentValues1.put("Activity_Name", Activity_Name);
            contentValues1.put("Assigned_To_User_Id", User_Id);
            contentValues1.put("Assigned_To_User_Group_Id",User_Group_Id);
            contentValues1.put("Scan_Type", Scan_Type);
            contentValues1.put("Remarks", Remarks);
            contentValues1.put("UpdatedStatus", "no");
            db=myDb.getWritableDatabase();
            db.insert("Task_Details", null, contentValues1);
            db.close();
        }
        return  TaskId;
    }
*/

    private LinearLayout sectionLinearLayout(int Mandatory, String field_Label,int id,String field_Option,String section,String Value) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textViewIncident(field_Label));
        radioLayout.addView(radiogroupBranching(field_Option, Id, Value, section, String.valueOf(sid), radioLayout));
        return radioLayout;
    }

    private TextView SectiontextView(String label) {
        if(LOG)Log.d("jgfjhgd", label);
        TextView textView = new TextView(this);
        textView.setTextSize(15);
        textView.setText("" + label + ": ");
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(fittype1);
        textView.setLayoutParams(textLayout);
        textViewList.add(textView);
        return textView;
    }

    private Spinner qualifiactionBranching(String options, int sp_id, String qval, final String Field_Option_Id, final LinearLayout linearLayout, final String Section_Id, final int sid) {
        final Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        final LinearLayout layoutSpinner = new LinearLayout(this);
        layoutSpinner.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layout1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout1.setMargins(15,0,0,0);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        if(LOG)Log.d("in", "spinnercontentBranching");
        spinnerArray.add("-- Select One --");

        for (String value : optionList) {
            spinnerArray.add(value);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            qualifiactionSpinner.setAdapter(adapter);
        }
        if (Completed.equals("Completed")) {
            qualifiactionSpinner.setEnabled(false);
        }
        qualifiactionSpinner.setLayoutParams(textLayout);
        textSpinnerList.add(qualifiactionSpinner.getSelectedItemPosition(), qualifiactionSpinner);
        for (String optValue : optionList) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, spinnerArray);
            int selectionPosition = adapter.getPosition(qval);
            if(LOG)Log.d("dropdownSelectedValue:", qval);
            if (qval.equalsIgnoreCase(optValue))
                qualifiactionSpinner.setSelection(selectionPosition);
        }

        qualifiactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(LOG)Log.d("section spinner value: ",position+"section id: "+Section_Id+" sid value: "+sid);
                String formQuery = "select * from Form_Structure where Form_Id='" + Section_Id + "' and Record_Status <> 'D' and sid='" + position + "'ORDER BY Display_Order ASC";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(formQuery, null);
                if(LOG)Log.d("SpinnerQuery",formQuery+" cursr count:"+cursor.getCount());

                if (cursor.getCount() == 0) {
                    if(LOG)Log.d("Teasreqasd1","Teasdasd1");
                    //layoutSpinner.removeAllViews();
                    linearLayout.removeView(layoutSpinner);
                    removeRadioremark(layoutSpinner, 0);
                } else {
                    if (position!=0) {
                        linearLayout.removeView(layoutSpinner);
                        removeRadioremark(layoutSpinner, 0);
                        linearLayout.addView(layoutSpinner);
                        sectionForm(Section_Id, position, layoutSpinner);
                    } else {
                        //layoutSpinner.removeAllViews();
                        linearLayout.removeView(layoutSpinner);
                        removeRadioremark(layoutSpinner, 1);
                    }
                }

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


    private RadioGroup radiogroupBranching(String optionRadio, int id, String setText, final String sectionId, final String sectionvalue, final LinearLayout sectionLayout) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        final LinearLayout layoutRadioButton = new LinearLayout(this);
        layoutRadioButton.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(textLayout);
        String[] optionRadioList = optionRadio.split(",");
        if (optionRadioList.length > 2) {
            radioGroup.setOrientation(RadioGroup.VERTICAL);
        } else {
            radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        }
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButtonBranching(optionRadioList[i], i, setText, sectionId, radioGroup.getCheckedRadioButtonId(), sectionLayout));
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String formQuery = "select * from Form_Structure where Form_Id='" + sectionId + "' and Record_Status <> 'D' and sid='" + selectedId + "'ORDER BY Display_Order ASC";
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(formQuery, null);
                if(LOG)Log.d("Teasreqasd","Teasdasd");
                //Log.d("QueryValues", formQuery + " CursorCount:" + cursor.getCount() + " SectionValue:" + sectionvalue + " SelectedId:" + selectedId);
                if (cursor.getCount() == 0) {
                    if(LOG)Log.d("Teasreqasd","Teasdasd");
                    layoutRadioButton.removeAllViews();
                    sectionLayout.removeView(layoutRadioButton);
                    removeRadioremark(layoutRadioButton, 0);
                } else {
                    if (selectedId == Integer.parseInt(sectionvalue)) {
                        sectionLayout.addView(layoutRadioButton);
                        sectionForm(sectionId, selectedId, layoutRadioButton);
                    } else {
                        layoutRadioButton.removeAllViews();
                        sectionLayout.removeView(layoutRadioButton);
                        removeRadioremark(layoutRadioButton, 1);
                    }
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
                calculationvalue =cursor.getInt(cursor.getColumnIndex("Calculation"));

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


                try {
                    db = myDb.getWritableDatabase();
                    String formQuery1 = "select Value from Data_Posting where Form_Structure_Id='" + Field_Id + "' and Task_Id='" + TaskId + "'";
                    Cursor DataPosting = db.rawQuery(formQuery1, null);
                    if (DataPosting.getCount() > 0) {
                        if (DataPosting.moveToNext()) {
                            do {

                                Value = DataPosting.getString(DataPosting.getColumnIndex("Value"));

                            } while (DataPosting.moveToNext());
                        }
                    } else {

                        Value = "";
                    }
                    DataPosting.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    db = myDb.getWritableDatabase();
                    String formQuery2 = "select Reading,UOM from Meter_Reading where Form_Structure_Id='" + Field_Id + "' and Task_Id='" + TaskId + "'";
                    Cursor Meter_Reading = db.rawQuery(formQuery2, null);
                    if (Meter_Reading.getCount() > 0) {
                        if (Meter_Reading.moveToNext()) {
                            do {

                                Reading = Meter_Reading.getString(Meter_Reading.getColumnIndex("Reading"));
                                UOM = Meter_Reading.getString(Meter_Reading.getColumnIndex("UOM"));

                            } while (Meter_Reading.moveToNext());
                        }
                    } else {
                        Reading = "";
                        UOM = "";
                    }
                    Meter_Reading.close();
                    db.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    db = myDb.getWritableDatabase();
                    String formQueryRemarks = "select Remarks from Task_Details where From_Id='" + Form_Id + "' and Auto_Id='" + TaskId + "'";
                    Cursor cursorRemarks = db.rawQuery(formQueryRemarks, null);
                    if (cursorRemarks.getCount() > 0) {
                        if (cursorRemarks.moveToNext()) {
                            do {

                                Remarks = cursorRemarks.getString(cursorRemarks.getColumnIndex("Remarks"));

                            } while (cursorRemarks.moveToNext());
                        }
                    } else {
                        Remarks = "";
                    }
                    cursorRemarks.close();
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
                        formLayout.addView(editTextMeter(Mandatory, label[0], Id + 2, Reading));
                        formLayout.addView(textView(label[1]));
                        formLayout.addView(radiogroupMeter(Field_Options, Id + 1, UOM));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("text")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        if (parameterCount==0) {
                            formLayout.addView(editText(Mandatory, Field_Label, Id , Value,"","","","","",SafeRange,calculationvalue));
                        } else {
                            formLayout.addView(editText(Mandatory, field_Limit_Form + " - " + field_Limit_To +" (Safe Range: "+ threshold_From + " - " +threshold_To+" )", Id, Value, field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type,SafeRange,calculationvalue));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("branchingText")) {
                    try {
                        formLayout.addView(branchTextlinearlayout(Mandatory, Field_Label, Id, section,sid));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("textwithcb")) {

                    try {
                        if (parameterCount==0) {
                            formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, Value, "", "", "", "", "", SafeRange,calculationvalue));
                        } else {
                            formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, Value, field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type, SafeRange,calculationvalue));
                        }
                        //formLayout.addView(checkboxlinearlayout(Mandatory, Field_Label, Id, ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("textview")) {

                    try {
                        formLayout.addView(textViewLabel(Field_Label));
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
                if (Field_Type.equals("textscore")){
                    formLayout.addView(textViewScore("",Id));
                }
                if (Field_Type.equals("addReading")) {
                    try {
                        formLayout.addView(meterTextlinearlayout(Mandatory, Field_Label, Id, Value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("consumption")) {
                    try {
                        formLayout.addView(consumptionlinearlayout(Mandatory, Field_Label, Field_Options, Id, "", Value, Field_Id, calculationvalue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("dropdown")) {

                    try {
                        formLayout.addView(SectiontextView(Field_Label));
                        if (parameterCount==0) {
                            formLayout.addView(qualifiaction(Field_Options, Id, Value, null,"",section));
                        }else {
                            formLayout.addView(qualifiaction(Field_Options, Id, Value, null, Field_Option_Id, section));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("radio")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        if (parameterCount==0) {
                            formLayout.addView(radiogroup(Field_Options, Id, Value, ""));
                        }else {
                            formLayout.addView(radiogroup(Field_Options, Id, Value, Field_Option_Id));
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
                if (Field_Type.equals("radioScore")) {
                    try {
                        if (parameterCount==0) {
                            formLayout.addView(radioScorelinearlayout(Field_Label,Field_Id,Field_Options,Id,Value,""));
                        } else {
                            formLayout.addView(radioScorelinearlayout(Field_Label,Field_Id,Field_Options,Id,Value,Field_Option_Id));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("radiofeedback")) {
                    try {
                        formLayout.addView(radiofeedbacklinearlayout(Field_Label, Field_Options, Id, ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("feedbackscore")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(Score(Field_Label, Id, "", calculationvalue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("name")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextareaName(Field_Label, Id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("branching")) {
                    try {
                        formLayout.addView(sectionLinearLayout(Mandatory, Field_Label, Id, Field_Options, section, Value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("datetime")) {

                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextDateTime(Id, "D"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("dg")) {

                    try {
                        formLayout.addView(dglinearlayout(Mandatory, Field_Label, Id, "", Value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("radiowithimageandremark")) {
                    try {
                        formLayout.addView(radiowithImageandRemarklinearlayout(Field_Label, Field_Options, Id, Value, sid));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("radiowithimage")) {
                    try {
                        formLayout.addView(radiowithImagelinearlayout(Mandatory,Field_Label, Field_Options, Id, Value, sid));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("radiowithremark")) {
                    try {
                        formLayout.addView(radiowithRemarklinearlayout( Field_Label, Field_Options, Id, Value, sid));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("radiowithradio")) {

                    try {
                        formLayout.addView(radiowithradiolinearlayout(Mandatory, Field_Label, Field_Options, Id, "", Value, Field_Id));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("section")) {
                    try {
                        formLayout.addView(SectiontextView(Field_Label));
                        //formLayout.addView(sectionLinearLayout(Mandatory, Field_Label, Id, Field_Options, section, String.valueOf(sid), ""));
                        if (parameterCount==0) {
                            formLayout.addView(qualifiactionBranching(Field_Options, Id, Value, "", formLayout, section, sid));
                        } else {
                            formLayout.addView(qualifiactionBranching(Field_Options, Id, Value, Field_Option_Id, formLayout, section, sid));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("textarea")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextarea(Field_Label, Id, Value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (Field_Type.equals("remark")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextRemark(Field_Label, Id, Remarks));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (Field_Type.equals("signature")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(Signature(Field_Label, Id, "Createform"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }


    private void removeRadioremark(LinearLayout layout,int Id){

        for(TextView textView : textViewList){
            layout.removeView(textView);
        }
        for(EditText textView :editTextList ){
            layout.removeView(textView);
        }
        for(EditText textView :editTextDateTimeList ){
            layout.removeView(textView);
        }
        for (EditText editTextDGTime : editTextDGTimeList) {
            layout.removeView(editTextDGTime);
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
        for(RadioGroup radioSection :textRadioGroupBranchingList ){
            layout.removeView(radioSection);
        }
        for (TextView textView : textViewListBranch) {
            layout.removeView(textView);
        }

    }

    private RadioButton radioButtonBranching(String strvalue, int SelectedId, String matchvalue,String Section_Id,int sid,LinearLayout bracnhinglayout) {   //965

        final RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(SelectedId);
        if(Completed.equals("Completed")) {
            radioButton.setEnabled(false);
        }

        if (matchvalue.equals(strvalue)) {
            radioButton.setChecked(true);
        }

        textRadioButtonList.add(radioButton);
        return radioButton;
    }

    public String taskInsertVerify(String TaskId){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Verified",2);
        db=myDb.getWritableDatabase();
        db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);
        db.close();
        return TaskId;
    }

    @SuppressLint("ResourceType")
    public void updateData(String uuid) {

        HashMap<String,String> editText = new HashMap<>();
        ArrayList<String> radioButtonString = new ArrayList<String>();
        ArrayList<String> checkBox = new ArrayList<String>();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Task_Start_At",applicationClass.yymmddhhmmss());

        for (EditText editLongText : editTextListMeter) {
            try {
                int fieldId = editLongText.getId();
                slnew = editLongText.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues.put("Form_Structure_Id", myDb.getfieldId(fieldId-2));
                for (int i = 0; i < textRadioGroupMeterList.size(); i++) {
                    if (fieldId == textRadioGroupMeterList.get(i).getId() + 1) {
                        RadioGroup rdgrp = textRadioGroupMeterList.get(i);
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
                contentValues.put("UOM", selectRB);
                contentValues.put("Reading", slnew);
                db = myDb.getWritableDatabase();
                db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (ImageView imageView : imageSignature) {

            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            myDb.insertBitmap(bitmap, uuid,myDb.getfieldLabel(imageView.getId()),myDb.Site_Location_Id(User_Id));

        }

        for (EditText editTextList1 : editTextList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextList1.getId();
                slnew = editTextList1.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", Form_Id);
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Value", slnew);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EditText editTextSignature : editTextDateTimeList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextSignature.getId();
                slnew = editTextSignature.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", Form_Id);
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Value", slnew);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EditText editTextarea1 : editTextareaList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextarea1.getId();
                slnew = editTextarea1.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", Form_Id);
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Value", slnew);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);
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
                editText.put(myDb.getfieldId(fieldIdRemarks), slnew);
                contentValuesRemarks.put("Task_Id", uuid);
                contentValuesRemarks.put("Form_Id", Form_Id);
                contentValuesRemarks.put("Form_Structure_Id", myDb.getfieldId(fieldIdRemarks));
                contentValuesRemarks.put("Site_Location_Id", SiteId);
                contentValuesRemarks.put("Value", slnew);
                contentValuesRemarks.put("Parameter_Id", "");
                contentValuesRemarks.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
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
                if (radioButton.isChecked()) {
                    selectRB = radioButton.getText().toString();
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("Task_Id", uuid);
                    contentValues2.put("Form_Id", Form_Id);
                    contentValues2.put("Form_Structure_Id", myDb.getfieldId(cbid));
                    contentValues2.put("Site_Location_Id", SiteId);
                    contentValues2.put("Parameter_Id", "");
                    contentValues2.put("Value", selectRB);
                    contentValues2.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);


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
                String sspinner = String.valueOf(textSpinner.getSelectedItem());
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("Task_Id", uuid);
                contentValues3.put("Form_Id", Form_Id);
                contentValues3.put("Parameter_Id", "");
                contentValues3.put("Form_Structure_Id", myDb.getfieldId(id_sp));
                contentValues.put("Site_Location_Id", SiteId);
                contentValues3.put("Value", sspinner);
                contentValues3.put("UpdatedStatus", "no");
                db=myDb.getWritableDatabase();
                db.update("Task_Details", contentValues, "Task_Id ='" + TaskId + "'", null);
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
        Iterator it = drawableBitmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,Bitmap> pair = (Map.Entry)it.next();

            myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(User_Id));
            it.remove(); // avoids a ConcurrentModificationException
        }
    }

    @SuppressLint("ResourceType")
    public void saveData(String uuid) {

        HashMap<String,String> editText = new HashMap<>();
        ArrayList<String> radioButtonString = new ArrayList<String>();
        ArrayList<String> checkBox = new ArrayList<String>();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Id", uuid);
        contentValues.put("Asset_Id", AssetId);
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Task_Start_At",applicationClass.yymmddhhmmss());
        contentValues.put("Activity_Frequency_Id", frequencyId);
        contentValues.put("Site_Location_Id", SiteId);


        for (EditText editLongText : editTextListMeter) {
            try {
                int fieldId = editLongText.getId();
                slnew = editLongText.getText().toString();

                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues.put("Form_Structure_Id", myDb.getfieldId(fieldId-2));
                for (int i = 0; i < textRadioGroupMeterList.size(); i++) {
                    if (fieldId == textRadioGroupMeterList.get(i).getId() + 1) {
                        RadioGroup rdgrp = textRadioGroupMeterList.get(i);
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
                if (intentFromReset.equals("Closing")){
                    contentValues.put("Task_Id",ClosingTaskId);
                    contentValues.put("Reset", "0");
                }else {
                    contentValues.put("Task_Id",TaskId);
                    contentValues.put("Reset", "1");
                }
                //contentValues.put("Reset", "0");
                contentValues.put("UOM", selectRB);
                contentValues.put("updatedStatus", "no");
                contentValues.put("Reading", slnew);
                emaildata.put(myDb.getfieldId(fieldId-2),slnew+" "+selectRB);
                db = myDb.getWritableDatabase();
                db.insert("Meter_Reading", null, contentValues);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EditText editTextSignature : editTextConsumptionData) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextSignature.getId();
                slnew = editTextSignature.getText().toString();

                String[] edittextSplit = slnew.split("\\s+");

                if(LOG)Log.d("TestingValuesSplit",edittextSplit[0]+" "+edittextSplit[1]);
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Value", edittextSplit[0]);
                contentValues1.put("UOM", edittextSplit[1]);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId),edittextSplit[0]+" "+edittextSplit[1]);
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (EditText editTextRemarks : editTextaddition) {
            try {
                ContentValues contentValuesRemarks = new ContentValues();
                int fieldIdRemarks = editTextRemarks.getId();
                String Form_Structure_Id = myDb.getfieldId(fieldIdRemarks);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextRemarks.getText().toString();
                if(LOG) Log.d("ValueAdded"," "+slnew);
                editText.put(Form_Structure_Id, slnew);
                contentValuesRemarks.put("Task_Id", uuid);
                contentValuesRemarks.put("Form_Id", formId);
                contentValuesRemarks.put("Form_Structure_Id", Form_Structure_Id);
                contentValuesRemarks.put("Site_Location_Id", SiteId);
                contentValuesRemarks.put("Value", slnew);
                contentValuesRemarks.put("Parameter_Id", "");
                contentValuesRemarks.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldIdRemarks),slnew);
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValuesRemarks);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (EditText editTextList1 : editTextWithCbList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextList1.getId();
                slnew = editTextList1.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Value", slnew);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId),slnew);
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for(int i = 0; i< textRadioGroupRadioRadio.size();i++){
            String selectRB = "",selectRB2="";
            RadioGroup radioGroup = textRadioGroupRadioRadio.get(i);
            i= i+1;
            RadioGroup radioGroup2 = textRadioGroupRadioRadio.get(i);
            int cbid = radioGroup.getId();

            try {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                View rb1 = radioGroup.findViewById(selectedId);
                int idx = radioGroup.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(idx);
                String Form_Structure_Id = myDb.getfieldId(cbid);

                int selectedId2 = radioGroup2.getCheckedRadioButtonId();
                View rb2 = radioGroup2.findViewById(selectedId2);
                int idx2 = radioGroup2.indexOfChild(rb2);
                RadioButton radioButton2 = (RadioButton) radioGroup2.getChildAt(idx2);

                if (radioButton.isChecked()&& radioButton2.isChecked()) {
                    selectRB = radioButton.getText().toString();
                    selectRB2 = radioButton2.getText().toString();
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("Task_Id", uuid);
                    contentValues2.put("Form_Id",  myDb.getFormId(myDb.getfieldId(cbid)));
                    contentValues2.put("Form_Structure_Id", myDb.getfieldId(cbid));
                    contentValues2.put("Site_Location_Id", SiteId);
                    contentValues2.put("Parameter_Id", "");
                    contentValues2.put("Value", selectRB+","+selectRB2);
                    contentValues2.put("UpdatedStatus", "no");
                    emaildata.put(myDb.getfieldId(cbid),selectRB+","+selectRB2);
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

        for (TextView textView : CalculationList){
            int fieldId = textView.getId();
            try {
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = textView.getText().toString();
                editText.put(myDb.getfieldId(fieldId), slnew);
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("Value", TotalOfScore);
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId),String.valueOf(TotalOfScore));
                db=myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (EditText editTextList1 : editTextDGTimeList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextList1.getId();
                slnew = editTextList1.getText().toString();
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);

                editText.put(Form_Structure_Id, slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Value", slnew);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId),slnew);
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (ImageView imageView : imageSignature) {

            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            myDb.insertBitmap(bitmap, uuid,myDb.getfieldLabel(imageView.getId()),myDb.Site_Location_Id(User_Id));

        }

        for (EditText editTextList1 : editTextList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextList1.getId();
                slnew = editTextList1.getText().toString();
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);

                editText.put(Form_Structure_Id, slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id",Form_Structure_Id);
                contentValues1.put("Value", slnew);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId),slnew);
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EditText editTextSignature : editTextDateTimeList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextSignature.getId();

                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextSignature.getText().toString();
                editText.put(Form_Structure_Id, slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Value", slnew);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId),slnew);
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
                if(LOG)Log.d("Field_Id",Form_Structure_Id);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextarea1.getText().toString();
                editText.put(Form_Structure_Id, slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Remark", slnew);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId),slnew);
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
                String Form_Structure_Id = myDb.getfieldId(fieldIdRemarks);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextRemarks.getText().toString();
                editText.put(Form_Structure_Id, slnew);
                contentValuesRemarks.put("Task_Id", uuid);
                contentValuesRemarks.put("Form_Id", formId);
                contentValuesRemarks.put("Form_Structure_Id", Form_Structure_Id);
                contentValuesRemarks.put("Site_Location_Id", SiteId);
                contentValuesRemarks.put("Value", slnew);
                contentValuesRemarks.put("Parameter_Id", "");
                contentValuesRemarks.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldIdRemarks),slnew);
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
                if (radioButton.isChecked()) {
                    selectRB = radioButton.getText().toString();
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("Task_Id", uuid);
                    contentValues2.put("Form_Id",  myDb.getFormId(myDb.getfieldId(cbid)));
                    contentValues2.put("Form_Structure_Id", myDb.getfieldId(cbid));
                    contentValues2.put("Site_Location_Id", SiteId);
                    contentValues2.put("Parameter_Id", "");
                    contentValues2.put("Value", selectRB);
                    contentValues2.put("UpdatedStatus", "no");
                    emaildata.put(myDb.getfieldId(cbid),selectRB);
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues2);
                    String query = "Select * FROM Parameter" +
                            " WHERE Activity_Frequency_Id = '" + frequencyId + "'" + "AND Form_Structure_Id = '" + Form_Structure_Id + "'";

                    Cursor parameter = db.rawQuery(query, null);
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
                            if (selectedId == Integer.parseInt(Field_Option_Id)) {
                                Toast.makeText(getApplicationContext(), "Alert Inserted", Toast.LENGTH_LONG).show();
                                InsertAlert(uuid, Form_Structure_Id);
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
                if(LOG)Log.d("Field_Id",Form_Structure_Id);
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
                    emaildata.put(myDb.getfieldId(cbid),selectRB);
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

        for (RadioGroup rdgrp : textRadioGroupScoreList) {
            String selectRB = "";
            int cbid = rdgrp.getId();
            int i;

            try {
                int selectedId = rdgrp.getCheckedRadioButtonId();
                View rb1 = rdgrp.findViewById(selectedId);
                int idx = rdgrp.indexOfChild(rb1);
                RadioButton radioButton = (RadioButton) rdgrp.getChildAt(idx);
                String Form_Structure_Id = myDb.getfieldId(cbid);
                String formId = myDb.getFormId(Form_Structure_Id);

                if (radioButton.isChecked()) {
                    scoringval = myDb.formScore(Form_Structure_Id,selectedId);
                    if(LOG) Log.d("FinalValofScore", String.valueOf(scoringval));
                    TotalOfScore = TotalOfScore+scoringval;
                    if(LOG) Log.d("TotalOfScore:", String.valueOf(TotalOfScore));
                    selectRB = radioButton.getText().toString();
                    ContentValues contentValues2 = new ContentValues();
                    contentValues2.put("Task_Id", uuid);
                    contentValues2.put("Form_Id", formId);
                    contentValues2.put("Form_Structure_Id", Form_Structure_Id);
                    contentValues2.put("Site_Location_Id", SiteId);
                    contentValues2.put("Parameter_Id", "");
                    contentValues2.put("Value", selectRB);
                    contentValues2.put("UpdatedStatus", "no");
                    emaildata.put(myDb.getfieldId(cbid),selectRB);
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
                            String[] label = Field_Option_Id.split("\\|");
                            for (i = 0; i < label.length; i++) {
                                if (Integer.parseInt(label[i]) == selectedId) {
                                    Toast.makeText(getApplicationContext(),"Alert Inserted",Toast.LENGTH_LONG).show();
                                    InsertAlert(uuid,Form_Structure_Id);
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }

                    db.close();
                }

            } catch (NullPointerException e) {
                if(LOG) System.out.println("fbi540 ERROR==" + e);
            } catch (Exception e) {
                if(LOG) System.out.println("fd1303 ERROR==" + e);
                Toast.makeText(getApplicationContext(), "Error code: fd1303", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                if(LOG) System.out.println("-------ee FD radio  " + "Id_" + cbid + " value " + selectRB);

            }
        }

        for (Spinner textSpinner : textSpinnerList) {
            try {
                int id_sp = textSpinner.getId();
                int item_pos = textSpinner.getSelectedItemPosition();
                String Form_Structure_Id = myDb.getfieldId(id_sp);
                String sspinner = String.valueOf(textSpinner.getSelectedItem());
                String formId = myDb.getFormId(Form_Structure_Id);
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("Task_Id", uuid);
                contentValues3.put("Form_Id", formId);
                contentValues3.put("Parameter_Id", "");
                contentValues3.put("Form_Structure_Id",Form_Structure_Id);
                contentValues.put("Site_Location_Id", SiteId);
                contentValues3.put("Value", sspinner);
                contentValues3.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(id_sp),sspinner);
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
        Iterator it = drawableBitmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,Bitmap> pair = (Map.Entry)it.next();

            myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(User_Id));
            it.remove(); // avoids a ConcurrentModificationException
        }

        Iterator itRadioImage = drawableRadioImage.entrySet().iterator();
        while (itRadioImage.hasNext()) {
            Map.Entry<Integer,ImageView> pair = (Map.Entry)itRadioImage.next();
            Bitmap bm=((BitmapDrawable)pair.getValue().getDrawable()).getBitmap();
            // pair.
            String Form_Structure_Id = myDb.getfieldId(pair.getKey()-5);
            myDb.insertRadioBitmap(bm, uuid, myDb.Site_Location_Id(User_Id), Form_Structure_Id);
            //myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(User_Id));
            itRadioImage.remove(); // avoids a ConcurrentModificationException
        }

    }

    public void emailAlert(String TaskID){
        try {
            StringBuilder sb =new StringBuilder();
            sb.append("*** This is an automatically generated email, please do not reply ***");
            sb.append('\n');
            sb.append('\n');
            sb.append("User : ");
            sb.append(myDb.UserName(User_Id));
            sb.append('\n');
            sb.append("Group : ");
            sb.append(myDb.UserGroupName(User_Group_Id));
            sb.append('\n');
            sb.append("Site Name : ");
            sb.append(myDb.SiteName(User_Id));
            sb.append('\n');
            sb.append("Activity Name : ");
            sb.append(Activity_Name);
            sb.append('\n');
            sb.append("Asset Name : ");
            sb.append(Asset_Name);
            sb.append('\n');

            if (unplanned == null) {
                sb.append("Task Scheduled Date : ");
                sb.append(Task_Scheduled_Date);
                sb.append('\n');
                sb.append("Task Performance Date : ");
                sb.append(new applicationClass().yymmddhhmm());
                sb.append('\n');
                sb.append("Task Type : ");
                sb.append("Completed");
            } else {
                sb.append("Task Performance Date : ");
                sb.append(new applicationClass().yymmddhhmm());
                sb.append('\n');
                sb.append("Task Type : ");
                sb.append("Unplanned");
            }
            sb.append('\n');
            sb.append('\n');

            String formQuery = "SELECT td.Asset_Name,td.Activity_Name,fs.Field_Id,fs.Field_Label,fs.Form_Id,dp.Value,am.Alert_Type AS Validation_Type,am.Alert_Type,td.Task_Start_At,td.Task_Scheduled_Date,td.Task_Status,pv.Field_Limit_From  || ' - ' ||  pv.Field_Limit_To AS Range,pv.Threshold_From  || ' - ' ||  pv.Threshold_To AS Safe_Range FROM Task_Details td INNER JOIN Data_Posting dp ON dp.Task_Id=td.Auto_Id INNER JOIN AlertMaster am ON (am.Task_Id=td.Auto_Id AND am.Form_Structure_Id=dp.Form_Structure_Id) INNER JOIN Form_Structure fs ON dp.Form_Structure_Id = fs.Field_Id INNER JOIN Parameter pv ON (pv.Activity_Frequency_Id=td.Activity_Frequency_Id AND pv.Form_Structure_Id=dp.Form_Structure_Id) WHERE dp.Task_Id = '"+TaskID+"' GROUP BY dp.Task_Id,dp.Form_Structure_Id ORDER BY fs.Form_Id,fs.Display_Order";
            db = myDb.getWritableDatabase();
            Cursor cursorEmailAlert  = db.rawQuery(formQuery, null);

            if(cursorEmailAlert.moveToFirst()){
                int indx = 1;
                do{
                    emailvals = emaildata.entrySet().iterator();
                    while (emailvals.hasNext()){
                        emailPair = (Map.Entry)emailvals.next();
                        String Field_IdEmail =cursorEmailAlert.getString(cursorEmailAlert.getColumnIndex("Field_Id"));
                        String SafeRange = cursorEmailAlert.getString(cursorEmailAlert.getColumnIndex("Safe_Range"));


                        if(Field_IdEmail.equals(emailPair.getKey())){
                            sb.append(indx+". ");
                            sb.append(cursorEmailAlert.getString(cursorEmailAlert.getColumnIndex("Field_Label")));
                            sb.append(": "+emailPair.getValue());
                            if(!SafeRange.trim().equals("-")) {
                                sb.append("   Safe Range: " + SafeRange);
                            }
                            sb.append('\n');
                        }
                    }
                    indx++;
                }while (cursorEmailAlert.moveToNext());
                emailBackgroundData(sb.toString());
            }
            cursorEmailAlert.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void emailBackgroundData(final String value){
        {
            try{
                final String id =  myDb.emailList(SiteId);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            com.example.google.template.emailfiles.GMailSender sender = new com.example.google.template.emailfiles.GMailSender(
                                    emailId,
                                    emailPwd);
                            sender.sendMail(getResources().getString(R.string.application_name)+" "+myDb.SiteName(User_Id) + " Alert Report " + Task_Scheduled_Date, value,
                                    emailId,
                                    id);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void InsertAlert(String uuid,String Form_Structure_Id){
        String formId = myDb.getFormId(Form_Structure_Id);


        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("Task_Id", uuid);
        contentValues2.put("Form_Id", formId);
        contentValues2.put("Form_Structure_Id", Form_Structure_Id);
        contentValues2.put("Alert_Type", validation_Type1);
        contentValues2.put("Created_By_Id", User_Id);
        contentValues2.put("Assigned_To_User_Group_Id",User_Group_Id);
        contentValues2.put("Critical", Critical1);
        contentValues2.put("TaskType", "");
        contentValues2.put("ViewFlag", "no");
        contentValues2.put("Asset_Name",Asset_Name);
        contentValues2.put("Activity_Name",Activity_Name);
        contentValues2.put("Site_Location_Id",SiteId);
        contentValues2.put("Activity_Frequency_Id", activityFrequencyId);
        if (unplanned == null) {
            if(taskStatus.equals("Completed")) {
                contentValues2.put("Task_Status", "Completed");
                contentValues2.put("Task_Scheduled_Date", Task_Scheduled_Date);
            } else if(taskStatus.equals("Delayed")){
                contentValues2.put("Task_Status", "Delayed");
                contentValues2.put("Task_Scheduled_Date", Task_Scheduled_Date);
            }
        }
        else {
            contentValues2.put("Task_Status","Unplanned");
            contentValues2.put("Task_Scheduled_Date","0000-00-00 00:00:00");
        }
        contentValues2.put("Task_Start_At",applicationClass.yymmddhhmmss());
        contentValues2.put("UpdatedStatus", "no");
        if(db.isOpen()){
            db.insert("AlertMaster", null, contentValues2);
        }else {
            db = myDb.getWritableDatabase();
            db.insert("AlertMaster", null, contentValues2);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
        intent.putExtra("TAB","TAB2");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }


}

