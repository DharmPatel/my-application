package com.example.google.csmia_temp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.google.csmia_temp.Helpdesk.HelpDeskClient;
import com.example.google.csmia_temp.Helpdesk.HelpdeskApi;
import com.example.google.csmia_temp.Helpdesk.Model.GenerateTicket;
import com.example.google.csmia_temp.Helpdesk.Model.HelpdeskInsert;
import com.example.google.csmia_temp.Helpdesk.Model.MuiltiticketResponse;
import com.example.google.csmia_temp.Helpdesk.Model.ServiceDetail;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;


public class DynamicForm extends AppCompatActivity {
    ProgressDialog progressDialog ;
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUESTAsset = 1889;
    private static final int CAMERA_REQUEST_PPM = 1890;
    ImageView taskSelfie,assetImage;
    int j = 0,val = 1,posVal=0;
    double totalfeedback= 0.0;
    String uuidChecklist = "";
    int ID=-1,cbId1=-1;
    int scoringval =0,TotalOfScore = 100;
    boolean on = false,cbIms = false;
    private static final String  emailId = "support@punctualiti.in", emailPwd = "oriole579";
    Map.Entry<String,String> emailPair;
    HashMap<String,String> emaildata = new HashMap<>();
    Iterator emailvals;
    private List<EditText> editText_Number_List = new ArrayList<EditText>();
    private List<EditText> editText_Number_List1 = new ArrayList<EditText>();
    private List<CheckBox> CheckboxListNumber = new ArrayList<CheckBox>();
    private List<CheckBox> CheckboxListNumber1 = new ArrayList<CheckBox>();
    private List<CheckBox> CheckboxListDateTime = new ArrayList<CheckBox>();
    private List<CheckBox> CheckboxListRadio = new ArrayList<CheckBox>();
    private List<EditText> editText_Text_List = new ArrayList<EditText>();
    private List<EditText> editText_Text_List1 = new ArrayList<EditText>();
    private List<CheckBox> CheckboxListText = new ArrayList<CheckBox>();

    private List<RadioGroup> textRadioGroupSectionList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupRadioRadio = new ArrayList<RadioGroup>();
    private List<EditText> editTextRadioRemarkList = new ArrayList<EditText>();
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
    private List<EditText> editTextareaRemarkList = new ArrayList<EditText>();
    private List<ImageView> imageSignature = new ArrayList<ImageView>();
    private List<EditText> editTextConsumption = new ArrayList<EditText>();
    private List<EditText> editTextRemarkList = new ArrayList<EditText>();
    private List<TextView> textViewList = new ArrayList<TextView>();
    private List<CheckBox> checkBoxList = new ArrayList<CheckBox>();
    private List<CheckBox> imsCheckList = new ArrayList<>();
    private List<CheckBox> CheckboxList = new ArrayList<CheckBox>();
    private List<Spinner> textSpinnerList = new ArrayList<Spinner>();
    private List<RadioButton> textRadioButtonList = new ArrayList<RadioButton>();
    private List<RadioGroup> textRadioGroupMeterList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupBranchingList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupConsumption = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioGroupScoreList = new ArrayList<RadioGroup>();
    private List<RadioGroup> textRadioFeedbackList = new ArrayList<RadioGroup>();
    ArrayList<String> previoudReadings = new ArrayList<>();
    private List<TextView> previousReadingChange = new ArrayList<TextView>();
    private List<TextView> textViewListIncident = new ArrayList<TextView>();
    private HashMap<CheckBox,Integer> CheckboxListhash = new HashMap<>();
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
    String Field_Id,Reading,Remarks,UOM,Value,Form_Id,IMS_Id,Field_Label,Field_Type,Field_Options,sections,Asset_Name,Asset_Location,Asset_Status,Completed="",Activity_Name;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String selectRB = "";
    String uuid = "", resp = "";
    String companyId, SiteId, User_Id,User_Group_Id,Scan_Type="",taskStatus="";
    TextView textView;
    LinearLayout formLayout;
    String[] loggerData;
    String previousReadingDatabase="",OldTaskId="";
    int textviewId;
    Button submit;
    String Checklist,field_Limit_Form1, field_Limit_To1, threshold_From1, threshold_To1, validation_Type1, Critical1,Field_Option_Id;
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
    boolean success;
    double editTextFirstValue = 0 ;
    double editTextSecondValue = 0;
    String intentFromReset;
    String uuid1, ClosingTaskId;
    Double subFactor=0.0, addFactor=0.0, multiFactor=1.0, divFactor=1.0, firstVal = 0.0, secondVal = 0.0;
    String Recipient = "";
    String Message = "";
    String UserName ="" ;//"Oriole";
    String Password = "";//"566e1fdf78XX";
    String type = "";//"2";
    String source = "";//"InfoSM";
    String mainURL = "";//http://103.233.79.246//submitsms.jsp";
    String Master_DB,Site_DB_Name;
    String Building=null,Floor=null,Room=null;
    String Building_id,Floor_id,Room_id,employee_name,user_mailid;
    Boolean dataInsert;
    String Product;
    ServiceDetail serviceDetail;
    GenerateTicket generateTicket;
    List<ServiceDetail> serviceDetaillist;
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
        OldTaskId = getIntent().getStringExtra("OldTaskId");
        ClosingTaskId = getIntent().getStringExtra("ClosingTaskId");
        Asset_Name = getIntent().getStringExtra("AssetName");
        Asset_Location=getIntent().getStringExtra("Asset_Location");
        Asset_Status =getIntent().getStringExtra("Asset_Status");
        Activity_Name = getIntent().getStringExtra("ActivityName");
        Completed = getIntent().getStringExtra("Completed");
        User_Group_Id = getIntent().getStringExtra("User_Group_Id");
        Form_IdIntent = getIntent().getStringExtra("Form_Id");
        intentFromReset = getIntent().getStringExtra("FromReset");
        serviceDetaillist =new ArrayList<>();
        Scan_Type = myDb.ScanType(User_Id);
        applicationClass = new applicationClass();
        EmployeeName = myDb.EmployeeName(User_Id);
        Checklist = getIntent().getStringExtra("IntentValue");
        Log.d("ChecklistValue","1: "+Checklist+" "+PPM_Intent+" "+Completed+""+Asset_Location);
        //createCutomActionBarTitle();
        site_details();
        setAsset_Location();
        userName();
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
        TextView mTitleTextView = mCustomView.findViewById(R.id.titleFragment1);
        ((TextView) mTitleTextView.findViewById(R.id.titleFragment1)).setTextColor(Color.WHITE);
        mTitleTextView.setText(Activity_Name+BuildConfig.VERSION_NAME);
        mTitleTextView.setTextSize(14);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String date = simpleDateFormat.format(calendar.getTime());
        TextView mTitleTextView2 = mCustomView.findViewById(R.id.titleFragment2);
        mTitleTextView2.setTextSize(14);
        ((TextView) mTitleTextView2.findViewById(R.id.titleFragment2)).setTextColor(Color.WHITE);
        mTitleTextView2.setText(Asset_Name);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
    public void getData() {
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
                    //FormType = cursor.getString(cursor.getColumnIndex("FormType"));
                    Field_Id =cursor.getString(cursor.getColumnIndex("Field_Id"));
                    SafeRange =cursor.getInt(cursor.getColumnIndex("Field_Id"));
                    calculationvalue =cursor.getInt(cursor.getColumnIndex("Calculation"));
                    IMS_Id = cursor.getString(cursor.getColumnIndex("IMS_Id"));

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
                                    Log.d("sdjkfhjksdfhk",Value);

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
                    if (Field_Type.equals("numberwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(SingleCBNumberlinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id));
                            } else {
                                formLayout.addView(SingleCBNumberlinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("numbercommentwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(SingleCBNumberlinearLayout1(Field_Label,Field_Options,Id,Value,Field_Option_Id));
                            } else {
                                formLayout.addView(SingleCBNumberlinearLayout1(Field_Label,Field_Options,Id,Value,Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("sectionwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(cbwithtimelinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id,section,sid));
                            } else {
                                formLayout.addView(cbwithtimelinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id,section,sid));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radioremarkwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(radioremarkcblinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id));
                            } else {
                                formLayout.addView(radioremarkcblinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("cbwithtext")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(SingleCBTextlinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id));
                            } else {
                                formLayout.addView(SingleCBTextlinearLayout(Field_Label,Field_Options,Id,Value,Field_Option_Id));
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
                    /*if (Field_Type.equals("checkbox")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            String[] optionList = Value.split("\\r?\\n");
                            for (int i = 0; i < optionList.length; i++) {
                                formLayout.addView(checkBox(optionList[i],Id));
                                Log.d("CheckedValues",optionList[i]);
                            }
                            //formLayout.addView(checkBox(Value,Id));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/

                    if (Field_Type.equals("checkbox")){
                        try {
                            formLayout.addView(textView(Field_Label));
                            String[] optionList = Value.split(",");
                            for (int i = 0; i < optionList.length; i++) {
                                formLayout.addView(multiselectionView(formLayout, optionList[i], Id, 0,IMS_Id));
                            }
                            //Log.d("checkedval",Value);
                            /*String[] optionList = Field_Options.split(",");
                            for (int i = 0; i < optionList.length; i++) {
                                formLayout.addView(multiselectionView(optionList[i],Id));
                            }*/

                        }catch (Exception e){
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
                            formLayout.addView(consumptionlinearlayout(Mandatory, Field_Label, Field_Options, Id, "", Reading, Field_Id, calculationvalue));
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
                    if (Field_Type.equals("activitywithinactivity")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(activitylinearlayout(Field_Label, Field_Options, Id, Value, "", section));
                            } else {
                                formLayout.addView(activitylinearlayout(Field_Label, Field_Options, Id, Value, Field_Option_Id, section));
                            }

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
                    if (Field_Type.equals("radiogroupwithremark")) {
                        try {
                            formLayout.addView(radiogroupwithRemarklinearlayout( Field_Label, Field_Options, Id, Value, sid));

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
                            formLayout.addView(editTextarea1(Field_Label, Id, Value));
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
               /* Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                intent.putExtra("TAB", "TAB3");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();*/
                if (PPM_Intent != null){
                    Intent intent = new Intent(DynamicForm.this, ppm_activity.class);
                    intent.putExtra("TAB", "TAB2");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else{
                    if (Checklist != null){
                        Intent intent = new Intent(DynamicForm.this, CheckList.class);
                        intent.putExtra("TAB", "TAB2");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                        intent.putExtra("TAB", "TAB3");
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }
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
                    IMS_Id = cursor.getString(cursor.getColumnIndex("IMS_Id"));
                    Log.d("Dyform",Field_Label+" "+Field_Options+" "+Field_Id+" "+IMS_Id);

                    if (PPM_Intent==null) {
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

                                        Log.d("Dyfor121m",activityFrequencyId+" "+formStructureId+" "+Field_Option_Id);

                                    } while (parameter.moveToNext());
                                }
                            }
                            parameter.close();
                            db.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            db=myDb.getWritableDatabase();
                            String query = "Select Activity_Frequency_Id, Form_Id,Form_Structure_Id,Field_Option_Id, Field_Limit_From ,Field_Limit_To ," +
                                    "Threshold_From ,Threshold_To,Validation_Type,Critical FROM Parameter WHERE Form_Structure_Id = '"+Field_Id+"'";
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

                                        Log.d("Dyfor121m",activityFrequencyId+" "+formStructureId+" "+Field_Option_Id);

                                    } while (parameter.moveToNext());
                                }
                            }
                            parameter.close();
                            db.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                    if (Field_Type.equals("numberwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(SingleCBNumberlinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            } else {
                                formLayout.addView(SingleCBNumberlinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("numbercommentwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(SingleCBNumberlinearLayout1(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            } else {
                                formLayout.addView(SingleCBNumberlinearLayout1(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("sectionwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(cbwithtimelinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id,section,sid));
                            } else {
                                formLayout.addView(cbwithtimelinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id,section,sid));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("radioremarkwithcb")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(radioremarkcblinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            } else {
                                formLayout.addView(radioremarkcblinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("cbwithtext")){
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(SingleCBTextlinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            } else {
                                formLayout.addView(SingleCBTextlinearLayout(Field_Label,Field_Options,Id,"",Field_Option_Id));
                            }
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
                    if (Field_Type.equals("checkbox")){
                        try {
                            formLayout.addView(textView(Field_Label));
                            String[] optionList = Field_Options.split(",");
                            for (int i = 0; i < optionList.length; i++) {
                                formLayout.addView(multiselectionView(formLayout,optionList[i],Id,i+1,IMS_Id));
                            }

                        }catch (Exception e){
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
                            formLayout.addView(editTextarea1(Field_Label, Id, Value));
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
                    if (Field_Type.equals("radiogroupwithremark")) {
                        try {
                            formLayout.addView(radiogroupwithRemarklinearlayout( Field_Label, Field_Options, Id, "", sid));

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
                            formLayout.addView(editTextRemark(Field_Label, Id, ""));
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
                    if (Field_Type.equals("section")) {
                        try {
                            formLayout.addView(sectionLinearLayout(Mandatory, Field_Label, Id, Field_Options, section, sid, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("activitywithinactivity")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(activitylinearlayout(Field_Label, Field_Options, Id, "", "", section));
                            } else {
                                formLayout.addView(activitylinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id, section));
                            }

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
            try {
                //Log.d("Taslkdfsf",PPM_Intent);

                if (PPM_Intent!=null) {
                    if(PPM_Intent.trim().equalsIgnoreCase("PPMPending")){
                        taskSelfie = new ImageView(this);
                        taskSelfie.setImageResource(R.drawable.ic_cam);
                        formLayout.addView(textView("Service Report"));
                        formLayout.addView(taskSelfie);
                        taskSelfie.getLayoutParams().height = 150;
                        taskSelfie.getLayoutParams().width = 150;

                        taskSelfie.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                    cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                } else {
                                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                }
                                Log.d("jdskh","124654 "+cameraIntent.getDataString()+" "+Build.VERSION.SDK_INT);*/
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST_PPM);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else {

                        taskSelfie = new ImageView(this);
                        taskSelfie.setImageResource(R.drawable.ic_cam);
                        formLayout.addView(textView("Selfie"));
                        formLayout.addView(taskSelfie);
                        taskSelfie.getLayoutParams().height = 150;
                        taskSelfie.getLayoutParams().width = 150;

                        taskSelfie.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                    cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                } else {
                                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                }
                                Log.d("jdskh","124654 "+cameraIntent.getDataString()+" "+Build.VERSION.SDK_INT);*/
                                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, CAMERA_REQUESTAsset);
                            }
                        });
                    }
                } else {

                    taskSelfie = new ImageView(this);
                    taskSelfie.setImageResource(R.drawable.ic_cam);
                    formLayout.addView(textView("Selfie"));
                    formLayout.addView(taskSelfie);
                    taskSelfie.getLayoutParams().height = 150;
                    taskSelfie.getLayoutParams().width = 150;

                    taskSelfie.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                    cameraIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                                } else {
                                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                }
                                Log.d("jdskh","124654 "+cameraIntent.getDataString()+" "+Build.VERSION.SDK_INT);*/
                                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUESTAsset);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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
                try {
                    AlertCheck();
                    if (checkSubmitConsumption() == true && checkSubmitEditTextAdd() == true && checkSubmitEditText() == true && checkSubmitEditTextMeter() == true && checkSubmitRadio() == true && checkSubmitRadioMeter() == true && checkSpinnerValue() == true && checkSubmitRemark() == true) {
                        if (checkSubmitLowerReading() == true) {
                            if (intentFromReset.equals("Closing")){
                                saveData(taskInsert(taskStatus));
                                emailAlert(ClosingTaskId);
                                sendMultiTicketData();
                                new Connection().execute(ClosingTaskId);
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
                                sendMultiTicketData();
                                emailAlert(TaskId);
                                new Connection().execute(TaskId);
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
                                                if (PPM_Intent != null){
                                                    saveData(taskInsert(taskStatus));
                                                    sendMultiTicketData();
                                                    emailAlert(TaskId);
                                                    new Connection().execute(TaskId);
                                                    Intent intent = new Intent(DynamicForm.this, ppm_activity.class);
                                                    intent.putExtra("TAB", "TAB3");
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }else {
                                                    Log.d("ChecklistValue",Checklist);
                                                    saveData(taskInsert(taskStatus));
                                                    emailAlert(TaskId);
                                                    new Connection().execute(TaskId);
                                                    if(Checklist == null){
                                                        sendMultiTicketData();
                                                        Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                                                        intent.putExtra("TAB", "TAB3");
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }else {
                                                        Intent intent = new Intent(DynamicForm.this, CheckList.class);
                                                        intent.putExtra("TAB", "TAB2");
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }

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
                                emailAlert(ClosingTaskId);
                                sendMultiTicketData();
                                new Connection().execute(ClosingTaskId);
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
                                sendMultiTicketData();
                                emailAlert(TaskId);
                                new Connection().execute(TaskId);
                                Intent intent = new Intent(DynamicForm.this, HomePage.class);
                                intent.putExtra("User_Id", User_Id);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }else {
                                if (PPM_Intent != null){
                                    saveData(taskInsert(taskStatus));
                                    emailAlert(TaskId);
                                    new Connection().execute(TaskId);
                                    Intent intent = new Intent(DynamicForm.this, ppm_activity.class);
                                    intent.putExtra("TAB", "TAB3");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    saveData(taskInsert(taskStatus));
                                    emailAlert(TaskId);
                                    new Connection().execute(TaskId);
                                    if (Checklist == null){
                                        sendMultiTicketData();
                                        Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                                        intent.putExtra("TAB", "TAB3");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Intent intent = new Intent(DynamicForm.this, CheckList.class);
                                        intent.putExtra("TAB", "TAB2");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
////////////////////////////NumberWithCB//////////////////////////////////////
    private LinearLayout SingleCBNumberlinearLayout(String field_label, String field_options, int id, String value, String field_option_id) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(textLayout);
        String[] CbLabel = field_label.split("\\|");
        linearLayout.addView(textView(CbLabel[0]));
        linearLayout.addView(checkBoxNumber(linearLayout,CbLabel[1],id,field_options));
        return linearLayout;
    }
    public CheckBox checkBoxNumber(final LinearLayout linearLayout, String optionValue, final int id, final String field_Options){
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(optionValue);
        checkBox.setId(id);
        checkBox.setChecked(true);
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    removeEditTextNumber(linearLayout,id);
                }else {
                    linearLayout.addView(EditText_Number("Remark",id,""));
                }
            }
        });
        CheckboxListNumber.add(checkBox);
        return checkBox;
    }
    private  void removeEditTextNumber(LinearLayout linearLayout, int Id){
        for(EditText textView :editText_Number_List ){
            if (textView.getId() == Id){
                linearLayout.removeView(textView);
            }
        }
    }
    private EditText EditText_Number(String name, int id, String setText){
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(fittype2);
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
            editText.setEnabled(false);
        }
       /* if (FormType.equalsIgnoreCase("Meter")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }*/
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        //editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(3);
        editText.setMaxLines(5);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);
        editText.setFilters(new InputFilter[]{filter});
        editText.setText(setText);
        editText_Number_List.add(editText);
        return editText;
    }
    private void getEditTextNumberValue( String Task_Id) {

        for (EditText editTextList1 : editText_Number_List) {
            try {
                int fieldId = editTextList1.getId();
                String Form_Structure_Id2 = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id2);
                slnew = editTextList1.getText().toString();
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("Task_Id", Task_Id);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id2);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("Value", slnew);
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                String Query = "select * from Data_Posting where Form_Structure_Id = '"+Form_Structure_Id2+"' and Task_Id = '"+Task_Id+"'";
                Cursor cursor = db.rawQuery(Query,null);
                Log.d("editTextQuery",Query+" "+cursor.getCount());
                if (cursor.getCount() == 0){
                    db.insert("Data_Posting", null, contentValues1);
                }

                db.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
///////////////////////////NumberWithCB End///////////////////////////////////////////

    ////////////////////////////Number&TextWithCB//////////////////////////////////////
    private LinearLayout SingleCBNumberlinearLayout1(String field_label, String field_options, int id, String value, String field_option_id) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(textLayout);
        String[] CbLabel = field_label.split("\\|");
        linearLayout.addView(textView(CbLabel[0]));
        linearLayout.addView(checkBoxNumber1(linearLayout,CbLabel[1],id,field_options));
        return linearLayout;
    }
    public CheckBox checkBoxNumber1(final LinearLayout linearLayout, String optionValue, final int id, final String field_Options){
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(optionValue);
        checkBox.setId(id);
        checkBox.setChecked(true);
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    removeEditTextNumber(linearLayout,id);
                }else {
                    linearLayout.addView(EditText_Number1("Remark",id,""));
                    linearLayout.addView(editTextarea("Remark", id, ""));                }
            }
        });
        CheckboxListNumber1.add(checkBox);
        return checkBox;
    }
    private void getEditTextNumberValue1( String Task_Id) {

        for (EditText editTextList1 : editText_Number_List1) {
            try {
                int fieldId = editTextList1.getId();
                String Form_Structure_Id2 = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id2);
                slnew = editTextList1.getText().toString();
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("Task_Id", Task_Id);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id2);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("Value", slnew);
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                String Query = "select * from Data_Posting where Form_Structure_Id = '"+Form_Structure_Id2+"' and Task_Id = '"+Task_Id+"'";
                Cursor cursor = db.rawQuery(Query,null);
                Log.d("editTextQuery",Query+" "+cursor.getCount());
                if (cursor.getCount() == 0){
                    db.insert("Data_Posting", null, contentValues1);
                }

                db.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getEditTextTextValue1( String Task_Id) {

        for (EditText editTextList1 : editText_Text_List1) {
            try {
                int fieldId = editTextList1.getId();
                String Form_Structure_Id2 = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id2);
                slnew = editTextList1.getText().toString();
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("Task_Id", Task_Id);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id2);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("Value", slnew);
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                String Query = "select * from Data_Posting where Form_Structure_Id = '"+Form_Structure_Id2+"' and Task_Id = '"+Task_Id+"'";
                Cursor cursor = db.rawQuery(Query,null);
                Log.d("editTextQuery",Query+" "+cursor.getCount());
                if (cursor.getCount() == 0){
                    db.insert("Data_Posting", null, contentValues1);
                }

                db.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private EditText EditText_Number1(String name, int id, String setText){
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(fittype2);
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
            editText.setEnabled(false);
        }
       /* if (FormType.equalsIgnoreCase("Meter")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }*/
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        //editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(3);
        editText.setMaxLines(5);
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);
        editText.setFilters(new InputFilter[]{filter});
        editText.setText(setText);
        editText_Number_List1.add(editText);
        return editText;
    }
    private EditText EditText_Text1(String name, int id, String setText){
        final EditText editText = new EditText(this);
        editText.setId(id);

        // editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(fittype2);
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
            editText.setEnabled(false);
        }
       /* if (FormType.equalsIgnoreCase("Meter")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }*/
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
        editText_Text_List1.add(editText);
        return editText;
    }


///////////////////////////Number&TextWithCB End///////////////////////////////////////////

//////////////////////////SectionWithCB/////////////////////////////////////////////
    private LinearLayout cbwithtimelinearLayout(String field_label, String field_options, int id, String value, String field_option_id,String sections, int sid) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(textLayout);
        String[] CbLabel = field_label.split("\\|");
        linearLayout.addView(textView(CbLabel[0]));
        linearLayout.addView(checkBoxTime(linearLayout,CbLabel[1],id,field_options,value,sections,sid));
        return linearLayout;
    }

    public CheckBox checkBoxTime(final LinearLayout linearLayout, String optionValue, final int id, final String field_Options, String Value, final String sections, final int sid){
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(optionValue);
        checkBox.setId(id);
        checkBox.setChecked(true);
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        final LinearLayout sectionLayout = new LinearLayout(this);
        sectionLayout.setOrientation(LinearLayout.VERTICAL);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sectionLayout.setVisibility(View.GONE);
                    linearLayout.removeView(sectionLayout);
                    //removeRadioremark(sectionLayout,1);
                    //sectionLayout.removeAllViews();
                }else {
                    String formQuery = "select * from Form_Structure where Form_Id='" + sections + "' and Record_Status <> 'D' and sid='" + sid + "'ORDER BY Display_Order ASC";
                    db = myDb.getWritableDatabase();
                    Cursor cursor = db.rawQuery(formQuery, null);
                    Log.d("onclickValue",":2:"+cursor.getCount()+" "+formQuery);
                    if (cursor.getCount() != 0){
                        sectionLayout.setVisibility(View.VISIBLE);
                        linearLayout.addView(sectionLayout);
                        sectionForm(sections,sid,sectionLayout);
                    }
                }
            }
        });
        CheckboxListDateTime.add(checkBox);
        return checkBox;
    }

    /////////////////////////////////SectionWithCB End///////////////////////////////////////////
    ////////////////////////////////RadioRemarkWithCB///////////////////////////////////////////
    private LinearLayout radioremarkcblinearLayout(String field_label, String field_options, int id, String value, String field_option_id) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(textLayout);
        String[] CbLabel = field_label.split("\\|");
        linearLayout.addView(textView(CbLabel[0]));
        linearLayout.addView(radiocheckBox(linearLayout,CbLabel[1],id,field_options));
        return linearLayout;
    }
    public CheckBox radiocheckBox(final LinearLayout linearLayout, String optionValue, final int id, final String field_Options){
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(optionValue);
        checkBox.setId(id);
        checkBox.setChecked(true);
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        final LinearLayout Layout = new LinearLayout(this);
        Layout.setOrientation(LinearLayout.VERTICAL);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Layout.setVisibility(View.GONE);
                    linearLayout.removeView(Layout);
                    Layout.removeAllViews();
                    removeradiocb(Layout,id);

                }else {
                    Layout.setVisibility(View.VISIBLE);
                    linearLayout.addView(Layout);
                    Layout.addView(radiowithRemarklinearlayout( "Remark", field_Options, id, "", sid));
                }
            }
        });
        CheckboxListRadio.add(checkBox);
        return checkBox;
    }
    private  void removeradiocb(LinearLayout linearLayout, int Id){
        for(RadioGroup textView :textRadioGroupList ){
            if (textView.getId() == Id){
                linearLayout.removeView(textView);
            }
        }
    }
    //////////////////////////////////RadioRemarkWithCb End///////////////////////////////////////
    //////////////////////////////////CbWithText//////////////////////////////////////////
    private LinearLayout SingleCBTextlinearLayout(String field_label, String field_options, int id, String value, String field_option_id) {
        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(textLayout);
        String[] CbLabel = field_label.split("\\|");
        linearLayout.addView(textView(CbLabel[0]));
        linearLayout.addView(checkBoxText(linearLayout,CbLabel[1],id,field_options));
        return linearLayout;
    }
    public CheckBox checkBoxText(final LinearLayout linearLayout, String optionValue, final int id, final String field_Options){
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(optionValue);
        checkBox.setId(id);
        checkBox.setChecked(true);
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    removeEditTextText(linearLayout,id);
                }else {
                    linearLayout.addView(EditText_Text("Remark",id,""));
                }
            }
        });
        CheckboxListText.add(checkBox);
        return checkBox;
    }
    private EditText EditText_Text(String name, int id, String setText){
        final EditText editText = new EditText(this);
        editText.setId(id);

        // editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setLayoutParams(fittype2);
        if(Completed.equals("Completed")||Completed.equals("Delayed")){
            editText.setEnabled(false);
        }
       /* if (FormType.equalsIgnoreCase("Meter")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }*/
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
        editText_Text_List.add(editText);
        return editText;
    }
    private  void removeEditTextText(LinearLayout linearLayout, int Id){
        for(EditText textView :editText_Text_List ){
            if (textView.getId() == Id){
                linearLayout.removeView(textView);
            }
        }
    }
    private void getEditTextTextValue( String Task_Id) {

        for (EditText editTextList1 : editText_Text_List) {
            try {
                int fieldId = editTextList1.getId();
                String Form_Structure_Id2 = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id2);
                slnew = editTextList1.getText().toString();
                ContentValues contentValues1 = new ContentValues();
                contentValues1.put("Task_Id", Task_Id);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id2);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("Value", slnew);
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                String Query = "select * from Data_Posting where Form_Structure_Id = '"+Form_Structure_Id2+"' and Task_Id = '"+Task_Id+"'";
                Cursor cursor = db.rawQuery(Query,null);
                Log.d("editTextQuery",Query+" "+cursor.getCount());
                if (cursor.getCount() == 0){
                    db.insert("Data_Posting", null, contentValues1);
                }

                db.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //////////////////////////////////CbWithText End//////////////////////////////////////
    public CheckBox checkBox(String optionValue, int id){
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setId(id);
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        checkBox.setText(optionValue);
        checkBox.setLayoutParams(fittype1);
        checkBox.setLayoutParams(textLayout);
        checkBoxList.add(checkBox);
        CheckboxListhash.put(checkBox,id);
        return checkBox;
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

    private LinearLayout activitylinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id,String section) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(activitySpinner(field_option, id, setText, radioLayout, Field_Option_Id, section));
        return radioLayout;
    }

    private Spinner activitySpinner(String options, int sp_id, String qval,LinearLayout linearLayout, final String Field_Option_Id,final String Section_Id) {
        final Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        //spinnerArray.add("-- Select One --");

        /*final TextView textView = new TextView(DynamicForm.this);
        textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(15, 0, 0, 0);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);
        textView.setVisibility(View.GONE);*/

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
                /*String formQuery = "select * from Form_Structure where Form_Id='" + Section_Id + "' and Record_Status <> 'D' and sid='" + (position+1) + "' ORDER BY Display_Order ASC";
                //Log.d("Teasdasdasdasd",formQuery+ " "+SpinnerValue);
                db = myDb.getWritableDatabase();
                Cursor cursor = db.rawQuery(formQuery, null);
                if (cursor.getCount() > 0) {
                    //sectionForm(Section_Id, (position+1), layoutSpinner);
                }*/

                if (Completed.equals("Completed")) {
                    qualifiactionSpinner.setEnabled(false);
                    qualifiactionSpinner.setClickable(false);
                } else {
                    editor = settings.edit();
                    editor.putString("IncidentSpValue", SpinnerValue);
                    editor.putInt("IncidentSpId", SpinnerId);
                    editor.commit();

                    String formQuery = "select * from Form_Structure where Form_Id='" + Section_Id + "' and Record_Status <> 'D' and sid='" + (position+1) + "' ORDER BY Display_Order ASC";
                    Log.d("FormQueryPrint",formQuery+ " "+SpinnerValue);
                    db = myDb.getWritableDatabase();
                    Cursor cursor = db.rawQuery(formQuery, null);
                    String updatedActId="",act_Name="";
                    if (cursor.getCount() > 0) {
                        String updateActQuery = "Select * from Activity_Master where Form_Id = '"+Section_Id+"'";
                        SQLiteDatabase sb = myDb.getReadableDatabase();
                        Cursor c = sb.rawQuery(updateActQuery,null);
                        if (c.moveToFirst()) {
                            do {
                                updatedActId =c.getString(c.getColumnIndex("Activity_Id"));
                                act_Name = c.getString(c.getColumnIndex("Activity_Name"));
                            }while (c.moveToNext());
                        }
                        //String act_Name = myDb.sectionActName(Section_Id);
                        //Log.d("Activity_Nameee","2: "+act_Name+",3: "+updatedActId);
                        Intent intent = new Intent(DynamicForm.this, DynamicForm.class);
                        intent.putExtra("Section_Id", Section_Id);
                        intent.putExtra("sid", (position+1));
                        intent.putExtra("FrequencyId",frequencyId);
                        //Log.d("dsfgdfg",Activity_Name);
                        intent.putExtra("ActivityName", act_Name);
                        intent.putExtra("AssetName", Asset_Name);
                        intent.putExtra("TaskId", TaskId);
                        intent.putExtra("Scan_Type", Scan_Type);
                        intent.putExtra("User_Id", User_Id);
                        intent.putExtra("StartDate",Task_Scheduled_Date);
                        intent.putExtra("AssetId", AssetId);
                        intent.putExtra("ActivityId",updatedActId);
                        intent.putExtra("Form_Id", Section_Id);
                        intent.putExtra("AssetCode", assetCode);
                        intent.putExtra("Asset_Location", Asset_Location);
                        intent.putExtra("Asset_Status", Asset_Status);
                        intent.putExtra("User_Group_Id", User_Group_Id);
                        if(Completed.equals("Pending")) {
                            intent.putExtra("Completed", "Pending");
                            intent.putExtra("Status", "Completed");
                        } else if(Completed.equals("Missed")) {
                            intent.putExtra("Completed", "Missed");
                            intent.putExtra("Status", "Delayed");
                        } else if(Completed.equals("Unplanned")) {
                            intent.putExtra("unplanned", "unplanned");
                            intent.putExtra("Completed", "Unplanned");
                        }
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
    private EditText editText(final int mandatory, String name, final int id, String setText) {
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

    private LinearLayout multicheckLayout(String field_option, int id, int cbId,String IMS_Id) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(multiselectionView(radioLayout,field_option,id,cbId,IMS_Id));
        return radioLayout;
    }

    public CheckBox multiselectionView(final LinearLayout checkboxLayout, final String optionValue, final int id, final int cbId, final String IMS_Id) {
        final CheckBox checkBox = new CheckBox(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(50,0,0,0);
        checkboxLayout.setLayoutParams(layoutParams);
        checkBox.setId(id);
        checkBox.setTag(cbId);
        if(Completed.equals("Completed")){
            checkBox.setEnabled(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Log.d("IMS432455","1 "+IMS_Id);
                    checkboxLayout.addView(editTextRadioRemark("Remark",id,""));
                    dataInsert=true;
                    if (!IMS_Id.equals("null")) {
                        checkboxLayout.addView(check("IMS",cbId,IMS_Id,id,optionValue));
                    }else {
                        if((Form_Id!=null)) {
                            InsertHelpdk(Field_Id,optionValue);
                        }
                    }
                    int selectedTag = (int) compoundButton.getTag();
                    int fieldId = compoundButton.getId();
                    String Form_Structure_Id = myDb.getfieldId(fieldId);
                    scoringval = myDb.formScore(Form_Structure_Id,selectedTag);
                    if(LOG)Log.d("FinalValofScore", String.valueOf(scoringval));
                    TotalOfScore = TotalOfScore+scoringval;
                    if(LOG)Log.d("TotalOfScore:", String.valueOf(TotalOfScore)+" Score val: "+String.valueOf(scoringval));
                    checkBox.setEnabled(false);
                }
            }
        });
        checkBox.setText(optionValue);
        checkBox.setLayoutParams(layoutParams);
        checkBoxList.add(checkBox);
        return checkBox;
    }

    public CheckBox check(String Label,final int cbId,String IMS_Id,final int id,String optionValue) {
        final CheckBox cbCheck = new CheckBox(this);
        String[] IMS = IMS_Id.split("\\|");
        List<String> data = new ArrayList<>();
        cbCheck.setVisibility(View.GONE);
        dataInsert=true;
        for (String IM : IMS) {
            if (!IM.equals("null")) {
                if (Integer.parseInt(IM) == cbId) {
                    cbCheck.setId(id);
                    cbCheck.setVisibility(View.VISIBLE);
                    cbCheck.setText(Label);
                    cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                cbCheck.setEnabled(false);
                        }
                    });

                    Log.d("CBChecked","213 "+cbCheck.isChecked());
                }
            }else {
                if(dataInsert) {
                    Log.d("CBChecked", "213 " + cbCheck.isChecked());
                    int elementNo = Integer.parseInt(data.get(data.size() - 1));
                    ArrayList<Integer> index = new ArrayList<>();
                    for (int i = 1; i <= elementNo; i++) {
                        index.add(i);
                    }
                    ArrayList<Integer> dataIntType = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        dataIntType.add(Integer.parseInt(data.get(i)));
                    }
                    index.removeAll(dataIntType);
                    Log.d("index Data", "" + index);
                    if (index != null) {
                        if (index.contains(cbId)) {
                            if (Form_Id != null ) {
                                dataInsert=false;
                                InsertHelpdk(Field_Id, optionValue);
                            }
                        }
                    }
                }
            }
        }
        imsCheckList.add(cbCheck);
        return cbCheck;
    }
    private void site_details() {
        db=myDb.getWritableDatabase();
        String Site_Location_Id=myDb.Site_Location_Id(User_Id);
        String query_priority_id = "SELECT Site_DB_Name, Master_DB FROM Site_Details WHERE  Auto_Id='"+Site_Location_Id+"'";
        Cursor cursor = db.rawQuery(query_priority_id, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                try {
                    do {
                        Site_DB_Name = cursor.getString(cursor.getColumnIndex("Site_DB_Name"));
                        Master_DB = cursor.getString(cursor.getColumnIndex("Master_DB"));
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
    }
    private void setAsset_Location(){
        db=myDb.getWritableDatabase();
//("CREATE TABLE Asset_Location (Id INTEGER PRIMARY KEY,Asset_Id TEXT,Building_Id TEXT,Floor_Id TEXT,Room_Id TEXT,building_code TEXT,building_name TEXT,floor_code TEXT,floor_name TEXT,room_area TEXT,Site_Location_Id TEXT)");
        String query_priority_id = "SELECT Asset_Id,Building_Id,Floor_Id,Room_Id FROM Asset_Details WHERE Asset_Id='"+AssetId+"'";
        Cursor cursor = db.rawQuery(query_priority_id, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                try {
                    do {
                        Building_id = cursor.getString(cursor.getColumnIndex("Building_Id"));
                        Floor_id = cursor.getString(cursor.getColumnIndex("Floor_Id"));
                        Room_id = cursor.getString(cursor.getColumnIndex("Room_Id"));
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
    }
    private void userName() {
        String query_email = "SELECT Username FROM Login_Details WHERE User_Id ='" + User_Id + "'";
        Cursor cursor = db.rawQuery(query_email, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                try {
                    do {
                        //   ticket.setSubCategoryName(cursor.getString(cursor.getColumnIndex("sub_category_name")));
//                        user_mailid = cursor.getString(cursor.getColumnIndex("EmailId"));
                        employee_name = cursor.getString(cursor.getColumnIndex("Username"));
                        Log.d("name" ,""+ employee_name );
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        cursor.close();
    }


    private void sendMultiTicketData(){
        if (isNetworkConnected()) {
            progressDialog = new ProgressDialog(DynamicForm.this);
//            show_pDialog();
            SiteId=myDb.Site_Location_Id(User_Id);
            try {
                generateTicket = new GenerateTicket();
                generateTicket.setBuildingId(Building_id);
                generateTicket.setFloorId(Floor_id);
                generateTicket.setRoomId(Room_id);
                generateTicket.setAssetId(AssetId);
                generateTicket.setDescp("descp");
                generateTicket.setSiteLocationId(SiteId);
                generateTicket.setUserId(User_Id);
                generateTicket.setSelectedSiteDbName(Site_DB_Name);
                generateTicket.setMainDbName(Master_DB);
                generateTicket.setEmployeeName(employee_name);
                generateTicket.setUserMailid("NA");
                generateTicket.setServiceDetails(serviceDetaillist);
                generateTicket.setUserType("Facilities");
                Retrofit retrofit = HelpDeskClient.getClient();
                HelpdeskApi helpdeskApi = retrofit.create(HelpdeskApi.class);
                Call<MuiltiticketResponse> call = helpdeskApi.sendMultiTicket(generateTicket );

                //  Log.d("MADHURI:::::","" + UserId1 + ",  " + SiteId1+ ",  " + Building_id1+ ",  " + Floor_id1+ ",  " + Room_id1+ ",  " + Asset_id1+ ",  " + Category_id+ ",  " + service_sub_cat+ ",  " + issue_id+ ",  " + User_type+ ",  " + Mail_on_closure+ ",  " + et_desc+ ",  " + Site_DB_Name+ ",  " + Master_DB+ ",  " + parts+ ",  " + size);
                if(!serviceDetaillist.isEmpty()){
                    call.enqueue(new Callback<MuiltiticketResponse>() {

                    @Override
                    public void onResponse(Call<MuiltiticketResponse> call, retrofit2.Response<MuiltiticketResponse> response) {
                        try {
                            if (response.code() == 200) {
                                if (response.isSuccessful()) {
                                    String ticket_message;
                                    MuiltiticketResponse muiltiticketResponse = response.body();
                                    ticket_message = muiltiticketResponse.getRespone();
                                    Toast.makeText(DynamicForm.this,ticket_message , Toast.LENGTH_SHORT).show();
                                }

                            } else if (response.code() == 404) {
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                            Log.d("qwresponse", "" + response.code());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    @Override
                    public void onFailure(Call<MuiltiticketResponse> call, Throwable t) {
                        progressDialog.dismiss();
                        //Toast.makeText(TicketDetailsActivity.this,"Internet connection not found...plz check connection ",Toast.LENGTH_SHORT).show();
                        Toast.makeText(DynamicForm.this, "Error!!", Toast.LENGTH_SHORT).show();
                   /*     onBackPressed();*/
                    }
                });}else {
                    progressDialog.dismiss();
                    Toast.makeText(DynamicForm.this, "There is not proper data available to gerenate ticket!! ", Toast.LENGTH_SHORT).show();
                    /*Snackbar snackbar = Snackbar.make(formLayout, "There is not proper data available to gerenate ticket!! ", Snackbar.LENGTH_SHORT);
                    snackbar.show();*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(DynamicForm.this, "Please check your internet connection !!", Toast.LENGTH_SHORT).show();
          /*  Snackbar snackbar = Snackbar.make(formLayout, "Please check your internet connection !!", Snackbar.LENGTH_SHORT);
            snackbar.show();*/
        }
    }
    private void InsertHelpdk(String Field_Id,String OptionValue){
            if(!OptionValue.equalsIgnoreCase("IMS")){
            String query = "SELECT Auto_Id,Product,Component,Department,Option_Selected,Ticket_For,Assigned_To,IncidentSource,LoggedBy FROM Form_Ticket WHERE Form_section_Id = '"+Field_Id+"' AND Ticket_For = 'Facilities'";
            SQLiteDatabase db1 = myDb.getWritableDatabase();
            Cursor ticketDetails = db1.rawQuery(query, null);
            try {
                if(LOG)Log.d("ticketDetails",query+" "+ticketDetails.getCount());
                if (ticketDetails.getCount() > 0) {
                    if (ticketDetails.moveToNext()) {
                        String Ticket_Id = ticketDetails.getString(ticketDetails.getColumnIndex("Auto_Id"));
                        String Type = ticketDetails.getString(ticketDetails.getColumnIndex("Department"));
                        String Subject = ticketDetails.getString(ticketDetails.getColumnIndex("Component"));
                        String Group_Id = ticketDetails.getString(ticketDetails.getColumnIndex("Assigned_To"));
                        Product = ticketDetails.getString(ticketDetails.getColumnIndex("Product"));
                        String Ticket_For = ticketDetails.getString(ticketDetails.getColumnIndex("Ticket_For"));
                        String IncidentSource = ticketDetails.getString(ticketDetails.getColumnIndex("IncidentSource"));
                        String LoggedBy = ticketDetails.getString(ticketDetails.getColumnIndex("Loggedby"));
                        HelpdeskInsert helpdeskInsert=new HelpdeskInsert();
                        generateTicket=new GenerateTicket();
                        serviceDetail =new ServiceDetail();
                        serviceDetail.setCategory(Product);
                        serviceDetail.setSubcategory(Subject);

                        if(Subject.equalsIgnoreCase("NA")){
                            /* serviceDetail =new ServiceDetail(Product,Subject,OptionValue);*/
                            serviceDetail.setIssue(OptionValue);
                        }else {
                            /* serviceDetail =new ServiceDetail(Product,Subject,Type);*/
                            serviceDetail.setIssue(Type);
                        }
                        serviceDetaillist.add(serviceDetail);
                 /*categoryarraylist.add(Product);
                     subcategoryarraylist.add(Subject);
                     if(Subject.equalsIgnoreCase("NA")){
                         issuearraylist.add(OptionValue);
                     }else {
                         issuearraylist.add(Type);
                     }*/

                        if (LOG)
                            Log.d("INSERTTicket", "132456 " + ticketDetails.getCount() + " " + ticketDetails.getString(ticketDetails.getColumnIndex("Auto_Id")) + " " + ticketDetails.getString(ticketDetails.getColumnIndex("Department")) + " " + ticketDetails.getString(ticketDetails.getColumnIndex("Component")));

                    }
                }}catch(Exception e){
                e.printStackTrace();
            }}
    }
    /*public void setIMS(boolean cbIms, int cbId){
        this.cbIms = cbIms;
        this.cbId1 = cbId;
    }*/
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null) return true;
        else return false;
    }
    private void show_pDialog(){
        progressDialog.setMessage("Creating Ticket..");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
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

    private void removeEditTextWithradio(LinearLayout linearLayout, int id){
        for (EditText editTextRB : editTextareaRemarkList){
            if (editTextRB.getId()==id){
                linearLayout.removeView(editTextRB);
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
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

           // Log.d("gh"," "+radioButton.isChecked()+" "+radioButton2.isChecked()+" "+);

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
        if(!Completed.equals("Completed") || !intentFromReset.equals("Opening")){
            if(LOG)Log.d("fjhsdg",Completed+" "+intentFromReset);
            previousReading = myDb.lastFinalReading(Field_Id,AssetId);
        }
        String[] label = field_Label.split(",");
        for(int i = 0;i<label.length-1;i++){
            String[] labelValue = label[i].split("\\|");
            if(LOG)Log.d("TestingValue", label[i] + " " + " " + labelValue+" "+i);
            if(i == 0){
                if(LOG)Log.d("jdshfj",previousReading);
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
                    if(!intentFromReset.equals("Opening")){
                    String[] previousReadingSplite = previousReading.split(",");
                    radioLayout.addView(textView(labelValue[0]));
                    radioLayout.addView(editTextConsumption(id, getData, previousReadingSplite[0]));
                    radioLayout.addView(textView(labelValue[1]));
                    if(LOG)Log.d("Teasdasdasxdcasdasd",previousReading + " "+ previousReadingSplite[0] +" "+ previousReadingSplite[1]);
                    radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i,  previousReadingSplite[1],calcutionvalue,null));
                    } else {
                        radioLayout.addView(textView(labelValue[0]));
                        radioLayout.addView(editTextConsumption(id,"", ""));
                        radioLayout.addView(textView(labelValue[1]));
                        radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i, "",calcutionvalue,null));
                    }
                }

            }else {
                if(Completed.equals("Completed")) {
                   /* radioLayout.addView(textView(labelValue[0]));
                    radioLayout.addView(editTextConsumption(id, getData, Readingvalue[1]));
                    radioLayout.addView(textView(labelValue[1]));
                    radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i, UOMvalue[2],calcutionvalue));*/
                }else {
                    //if (!intentFromReset.equals("Opening")) {
                    if(LOG)Log.d("dfjhgsd", "jhfgkj"+" "+labelValue[0]+" "+labelValue[1]);
                        radioLayout.addView(textView(labelValue[0]));
                        radioLayout.addView(editTextConsumption(id, getData, ""));
                        radioLayout.addView(textView(labelValue[1]));
                        radioLayout.addView(radiogroupMeterConsumption(Field_Options, id + i, "", calcutionvalue, radioLayout));
                    //}
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
        String[] label = field_Label.split(",");
        if(LOG)Log.d("abcdValueee", field_Label + ":" + Value+" "+label[0]+" "+label[1]);
        radioLayout.addView(textView(label[0]));
        radioLayout.addView(editTextDGTime(id, getData, value[0]));
        radioLayout.addView(textView(label[1]));
        radioLayout.addView(editTextDGTime(id, getData, value[1]));
        radioLayout.addView(textView(label[2]));
        radioLayout.addView(editTextDGRunDuration(Mandatory, field_Label, id, value[2]));
        return radioLayout;
    }
    private EditText editTextDGRunDuration(final int mandatory, String name, int id,String SetText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setFocusable(false);
        editText.setText(SetText);
        editText.setKeyListener(null);
        editText.setClickable(true);
        editText.setHint("In Minutes");
        Log.d("InChecked","inSection: ");

        if(Completed.equals("Completed")){
            editText.setEnabled(false);
        }
        final EditText editText1 = new EditText(this);
        editText1.setId(id);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean abc = false;
                String value = "";
                for (EditText editTextDGTimeList1 : editTextDGTimeList) {
                    if (!editTextDGTimeList1.getText().toString().equals("")) {
                        abc = true;
                    } else {
                        abc = false;
                        break;
                    }
                }
                if (abc == true) {
                    long diff = parseDate(editTextDGTimeList.get(1).getText().toString()).getTime() - parseDate(editTextDGTimeList.get(0).getText().toString()).getTime();
                    long diffSeconds = diff / 60000;
                    editText.setText("" + diffSeconds);
                    editText1.setText(editTextDGTimeList.get(0).getText().toString() + "," + editTextDGTimeList.get(1).getText().toString() + "," + diffSeconds);
                }
                editTextDGTimeList.clear();
            }
        });

        editTextList.add(editText1);
        return editText;
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

    private EditText editText(final int mandatory, String name, final int id, String setText, final String Field_Limit_From, final String Field_Limit_To, final String Threshold_From, final String Threshold_To, String Validation_Type,int SafeRange,final int calculationvalue) {
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

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    ID = id;
                    /*Double val = Double.parseDouble(editText.getText().toString());
                    if (!Field_Limit_From.equals("") && !Field_Limit_To.equals("")) {
                        if (val > Double.parseDouble(Field_Limit_To) || val < Double.parseDouble(Field_Limit_From)) {
                            editText.setText("");
                            editText.setError("Invalid Reading.Please Enter valid Reading");
                        } else if ((val < Double.parseDouble(Threshold_From) && (val >= Double.parseDouble(Field_Limit_From))) || (val <= Double.parseDouble(Field_Limit_To)) && (val > Double.parseDouble(Threshold_To))) {
                            editText.setError("Alert will generate.");
                        } else {
                        }
                    }*/

                    /*if(s.length() == 9){
                        editText.setFocusableInTouchMode(false);
                        editText.setFocusable(false);
                    }*/

                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                try {
                    Double val = Double.parseDouble(editText.getText().toString());
                    val=val*calculationvalue;
                    if(ID == id) {
                        editText.setText(val.toString());
                        ID = 0;
                    }
                    if (!Field_Limit_From.equals("") && !Field_Limit_To.equals("")) {
                        if (val > Double.parseDouble(Field_Limit_To) || val < Double.parseDouble(Field_Limit_From)) {
                            editText.setText("");
                            editText.setError("Invalid Reading.Please Enter valid Reading");
                        } else if ((val < Double.parseDouble(Threshold_From) && (val >= Double.parseDouble(Field_Limit_From))) || (val <= Double.parseDouble(Field_Limit_To)) && (val > Double.parseDouble(Threshold_To))) {
                            editText.setError("Alert will generate.");
                        }
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
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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

    private EditText editTextRadioRemark(String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(fittype2);
        if (Completed.equals("Completed") || Completed.equals("Delayed")) {
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
        editTextRadioRemarkList.add(editText);
        return editText;
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
        //radioLayout.addView(editTextarea("Remark", id, ""));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (Field_Option_Id == selectedId) {
                    radioLayout.addView(editTextarea("Remark", id, ""));
                } else {
                    removeEditTextWithradio(radioLayout,id);
                }
                //removeEditTextWithradio(radioLayout,id);
            }
        });
        textRadioGroupList.add(radioGroup);
        return radioLayout;
    }

    private LinearLayout radiogroupwithRemarklinearlayout( String field_Label,String field_option,final int id,String setText,final int Field_Option_Id) {
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

        if(LOG)Log.d("dfkvdfv",radioGroup.getCheckedRadioButtonId()+"");

        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        radioLayout.addView(editTextarea("Remark", id, ""));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                /*int selectedId = radioGroup.getCheckedRadioButtonId();
                if (Field_Option_Id == selectedId) {
                    radioLayout.addView(editTextarea("Remark", id, ""));
                } else {
                    removeEditTextWithradio(radioLayout,id);
                }*/
                removeEditTextWithradio(radioLayout,id);
            }
        });
        textRadioList.add(radioGroup);
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
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                        if(LOG)Log.d("ALsetr",label[i]);
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

        if (matchvalue!=null){
            if (matchvalue.equals(strvalue)) {
                radioButton.setChecked(true);
            }
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
        editTextareaRemarkList.add(editText);
        return editText;
    }

    private EditText editTextarea1(String name, int id, String setText) {
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

        mContent = dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(getApplicationContext(), null);
        mSignature.setBackgroundColor(Color.WHITE);
        mSignature.clear();
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = dialog.findViewById(R.id.clear);
        mGetSign = dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = dialog.findViewById(R.id.cancel);
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

        return checkRadio == true;
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
        return editTextCheck == true;
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
        return editTextCheck == true;
    }


    public boolean checkSubmitConsumption() {
        boolean editTextCheck = false;
        try {
            if (editTextConsumptionData.size() == 0){
                editTextCheck = true;
            }else {
                for (EditText editLongText : editTextConsumptionData) {
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
        return editTextCheck == true;
    }



    //For Unplanned Mandatory Remark
    public boolean checkSubmitRemark() {
        boolean remarkCheck = false;
        // unplanned == null
        if(Completed.equals("Pending")||Completed.equalsIgnoreCase("wogenerated") ){
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
        return remarkCheck == true;
    }

    public boolean checkSubmitEditTextMeter() {
        boolean editTextMeterCheck = false;
        try {
            if (editTextListMeter.size() == 0) {
                editTextMeterCheck = true;
                //Log.d("TestMeter","1 "+editTextMeterCheck);
            } else {
                //Log.d("TestMeter","2 "+editTextMeterCheck);

                for (EditText editLongText : editTextListMeter) {
                    //Log.d("TestMeter","3 "+editLongText.getText().toString());

                    edittextValidation.add(editLongText.getText().toString());
                    if (editLongText.getText().toString().equals("")) {
                        //Log.d("TestMeter","4 "+editTextMeterCheck);
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
        return editTextMeterCheck == true;
    }

    public boolean checkSubmitRadio() {
        boolean checkRadio = false;
        try {
            if(LOG)Log.d("jfhvjkdfhkv",textRadioGroupList.size()+"  "+ editTextareaRemarkList.size());
            if (textRadioGroupList.size() == 0) {
                checkRadio = true;
            }else {
                for (RadioGroup rdgrp : textRadioGroupList) {
                    if(LOG)Log.d("sdf", rdgrp.getCheckedRadioButtonId() + "");

                        if (rdgrp.getCheckedRadioButtonId() == -1) {
                            Snackbar snackbar = Snackbar.make(formLayout, "Please select radio value !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            checkRadio = false;
                            break;
                        } else {
                            checkRadio = true;
                        }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkRadio == true;
    }

    public boolean checkSubmitRadioRemark() {
        boolean radioRemarkCheck = false;
        // unplanned == null
        if (Completed.equals("Pending")) {
            radioRemarkCheck = true;
        } else {
            try {

                if (editTextRadioRemarkList.size() == 0) {
                    radioRemarkCheck = true;
                } else {
                    for (EditText editLongText : editTextRadioRemarkList) {
                        slnew = editLongText.getText().toString();
                        //edittextValidation.add(editLongText.getText().toString());
                        if (slnew.equals("")) {
                            radioRemarkCheck = false;
                            editLongText.setError("Please Enter Value");
                            Snackbar snackbar = Snackbar.make(formLayout, "Please Complete the form !!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                            break;
                        } else {
                            radioRemarkCheck = true;
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return radioRemarkCheck == true;
    }


    public boolean checkradioEditText(){
        boolean checkRadio = false;
        try {
            if (textRadioList.size() == 0){
                checkRadio = true;
            }else {
                for (RadioGroup radioGroup : textRadioList){
                    if (radioGroup.getCheckedRadioButtonId() == -1) {
                        if (editTextareaRemarkList.size() == 0){
                            checkRadio = true;
                        }else {
                            for (EditText editText : editTextareaRemarkList){
                                if (editText.getText().toString().equals("") && radioGroup.getCheckedRadioButtonId() == -1) {
                                    Snackbar snackbar = Snackbar.make(formLayout, "Please select edit11 value !!", Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                    checkRadio = false;
                                    break;
                                }else {
                                    if (radioGroup.getCheckedRadioButtonId() == -1){
                                        Snackbar snackbar = Snackbar.make(formLayout, "Please select radio11 value !!", Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        checkRadio = false;
                                        break;
                                    }else {
                                        checkRadio = true;
                                    }
                                }
                            }
                        }
                    } else {
                        checkRadio = true;
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return checkRadio == true;
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
        return checkRadioMeter == true;
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
                    }
                }
            });
        }
    }

    String date;
    String slnew = "";
    ArrayList<String> edittextValidation = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.N)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                //Log.d("HGjgkj"," "+data.getExtras().get("data")+" "+data.getExtras().get("android.intent.extras.CAMERA_FACING"));
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else if (requestCode == CAMERA_REQUESTAsset && resultCode == Activity.RESULT_OK) {
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if (requestCode == CAMERA_REQUEST_PPM && resultCode == Activity.RESULT_OK) {
                        //Log.d("HGjgkj"," "+data.getExtras().get("data")+" "+data.getExtras().get("android.intent.extras.CAMERA_FACING"));
                        try {
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

                            drawableBitmap.put("Vendor Form",drawImage);
                            taskSelfie.setImageBitmap(drawImage);
                            taskSelfie.getLayoutParams().height = 500;
                            taskSelfie.getLayoutParams().width = 500;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
            }else if(resultCode == Activity.RESULT_OK) {
                try {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
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
            //if(LOG)Log.d("PPMIntent",PPM_Intent);
            if (PPM_Intent != null){
                myDb.updatedPPMTaskDetails(TaskId,OldTaskId,taskStatus,Completed, applicationClass.yymmddhhmmss(), Scan_Type, User_Id, User_Group_Id, Remarks);
            }else {
                if (Checklist != null){
                    uuidChecklist = UUID.randomUUID().toString();
                        myDb.insertTaskDetails(uuidChecklist, companyId, SiteId, frequencyId, taskStatus, Completed, applicationClass.yymmddhhmmss(), Asset_Name, AssetId, Form_IdIntent, assetCode, Asset_Location, Asset_Status, Activity_Name, Scan_Type, User_Id, User_Group_Id, Checklist, Remarks);
                }else {
                        myDb.updatedTaskDetails(TaskId, taskStatus, Completed, applicationClass.yymmddhhmmss(), Scan_Type, User_Id, Remarks, 0,"JobCard");
                }
            }
        } else {

            if(!intentFromReset.equals("Closing")) {
                if(LOG)Log.d("dhskjrwe12456", TaskId);
                contentValues1.put("Auto_Id", TaskId);
            } else {
                if(LOG)Log.d("dhskjrwe132454", ClosingTaskId);
                contentValues1.put("Auto_Id", ClosingTaskId);
            }
                contentValues1.put("Company_Customer_Id", companyId);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Activity_Frequency_Id", frequencyId);
                contentValues1.put("Task_Scheduled_Date", "0000-00-00 00:00:00");
                contentValues1.put("Task_Status", "Unplanned");
                contentValues1.put("Task_Start_At", applicationClass.yymmddhhmmss());
                contentValues1.put("Assigned_To", "U");
                contentValues1.put("EndDateTime", "[ Unplanned ]");
                contentValues1.put("Asset_Name", Asset_Name);
                contentValues1.put("Activity_Type","JobCard");
                contentValues1.put("Asset_Id", AssetId);
                contentValues1.put("From_Id", Form_IdIntent);
                contentValues1.put("Asset_Code", assetCode);
                contentValues1.put("Asset_Location", Asset_Location);
                contentValues1.put("Asset_Status", Asset_Status);
                contentValues1.put("Activity_Name", Activity_Name);
                contentValues1.put("Assigned_To_User_Id", User_Id);
                contentValues1.put("Assigned_To_User_Group_Id", User_Group_Id);
                contentValues1.put("Scan_Type", Scan_Type);
                contentValues1.put("Remarks", Remarks);
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.insert("Task_Details", null, contentValues1);
                db.close();

        }

        if(!intentFromReset.equals("Closing")) {
            if (Checklist != null){
                return uuidChecklist;
            } else {
                return TaskId;
            }
        } else {
            if (Checklist != null){
                return uuidChecklist;
            } else {
                return ClosingTaskId;
            }
        }
    }

    private LinearLayout sectionLinearLayout(int Mandatory, String field_Label,int id,String field_Option,String section,String Value) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textViewIncident(field_Label));
        radioLayout.addView(radiogroupBranching(field_Option, Id, Value, section, String.valueOf(sid), radioLayout));
        return radioLayout;
    }
    private LinearLayout sectionLinearLayout(int Mandatory, String field_Label, int id, String field_Option, String section, int sid, String Value) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
       /* String[] sidOption = sid.split(",");
        String sidValue = sidOption[0];
        String sidValue1 = sidOption[1];*/
        if(LOG)Log.d("ValuesInFunction:", field_Label + ":" + section + ":" + field_Option + ":" + sid);
        radioLayout.addView(textViewIncident(field_Label));
        radioLayout.addView(radioGroupSection(field_Option, Id, Value, section, String.valueOf(sid), radioLayout));
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

    private RadioGroup radioGroupSection(String optionRadio, final int id, String setText, final String sectionId, final String sectionvalue, final LinearLayout sectionLayout) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.VERTICAL);
        final LinearLayout layoutRadioButton = new LinearLayout(this);
        layoutRadioButton.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setLayoutParams(textLayout);
        //////////////////////////////////////////////////////////////////////
       /* if (sid!=0 && sid!=1) {
            String[] sectionOption = sectionvalue.split(",");
            String sid_first = sectionOption[0];
            String sid_second = sectionOption[1];
            Log.d("splitingSID: ", sid_first + ":" + sid_second);
        }*/
        /////////////////////////////////////////////////////////////////////
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


                if(LOG)Log.d("dijf"," 43 "+sectionId);
                if (!sectionId.equals("null")) {
                    String formQuery = "select * from Form_Structure where Form_Id='" + sectionId + "' and Record_Status <> 'D' and sid='" + selectedId + "'ORDER BY Display_Order ASC";
                    db = myDb.getWritableDatabase();
                    Cursor cursor = db.rawQuery(formQuery, null);
                    //Log.d("QueryValues", formQuery + " CursorCount:" + cursor.getCount() + " SectionValue:" + sectionvalue + " SelectedId:" + selectedId);
                    if (cursor.getCount() == 0) {
                        try {
                            layoutRadioButton.setVisibility(View.GONE);
                            sectionLayout.removeView(layoutRadioButton);
                            removeRadioremark(layoutRadioButton, 1);

                            layoutRadioButton.removeAllViews();
                            sectionLayout.removeView(layoutRadioButton);
                            removeRadioremark(layoutRadioButton, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (selectedId == Integer.parseInt(sectionvalue)) {
                            try {
                                //Log.d("dropdownSelected",selectedId+"");
                                layoutRadioButton.setVisibility(View.VISIBLE);
                                sectionLayout.addView(layoutRadioButton);
                                sectionForm(sectionId, selectedId, layoutRadioButton);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        //Log.d("dropdownSelected"," 11: "+selectedId+"");
                        /*else {
                            Log.d("Teasreqasd","rewdsfxcv");

                            layoutRadioButton.setVisibility(View.GONE);
                            sectionLayout.removeView(layoutRadioButton);
                            removeRadioremark(layoutRadioButton, 1);
                            layoutRadioButton.removeAllViews();
                            sectionLayout.removeView(layoutRadioButton);
                            removeRadioremark(layoutRadioButton, 1);
                        }*/
                    }
                } else if(sectionId.equals("null")&!sectionvalue.equals("")) {
                    try {
                        if(LOG)Log.d("ChildCount","1 "+sectionLayout.getChildCount()+" 2 "+layoutRadioButton.getChildCount());
                        if(layoutRadioButton.getChildCount()!=0) {
                            layoutRadioButton.removeAllViews();
                            sectionLayout.removeView(layoutRadioButton);
                            removeRadioremark(layoutRadioButton, 0);

                            layoutRadioButton.addView(editTextRadioRemark("Remark", id, ""));
                            layoutRadioButton.setVisibility(View.VISIBLE);
                            sectionLayout.addView(layoutRadioButton);
                        } else {
                            layoutRadioButton.addView(editTextRadioRemark("Remark", id, ""));
                            layoutRadioButton.setVisibility(View.VISIBLE);
                            sectionLayout.addView(layoutRadioButton);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        textRadioGroupSectionList.add(radioGroup);
        return radioGroup;
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

    private LinearLayout checkboxSection(String field_Label, String field_option, int id, String section_id, int sid) {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(textLayout);
        layout.addView(SectiontextView(field_Label));

        String[] optionList = field_option.split(",");
        for (int i = 0; i < optionList.length; i++) {
            layout.addView(cbSection(id,optionList[i],layout,section_id,i+1, optionList.length));
            if(LOG)Log.d("jgkfghd",i+" "+section_id+" "+optionList.length);
        }

        return layout;
    }


    public CheckBox cbSection(int Id, String Options, final LinearLayout linearLayout, final String section_id, final int maxLen, final int totalval){
        final LinearLayout child_layout = new LinearLayout(getApplicationContext());
        child_layout.setOrientation(LinearLayout.VERTICAL);
        child_layout.setLayoutParams(textLayout);
        final CheckBox checkBox = new CheckBox(this);
        checkBox.setId(Id);
        checkBox.setTag(maxLen);
        checkBox.setText(Options);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                try {
                    if (checkBox.isChecked()){
                        int position = (int) compoundButton.getTag();
                        String formQuery = "select * from Form_Structure where Form_Id='" + section_id + "' and Record_Status <> 'D' and sid='" + maxLen + "'ORDER BY Display_Order ASC";
                        db = myDb.getWritableDatabase();
                        Cursor cursor = db.rawQuery(formQuery, null);
                        if(LOG)Log.d("SectionQuery",formQuery+" CoursorCount:"+cursor.getCount());
                        if (cursor.getCount() != 0){
                            linearLayout.removeView(child_layout);
                            removeRadioremark(child_layout, 1);
                            if(LOG)Log.d("ValuesHere",maxLen+" "+position);
                            if (maxLen == position){
                                if ((totalval+1) == linearLayout.getChildCount()){
                                    linearLayout.addView(child_layout,position+1);
                                    sectionForm(section_id,maxLen,child_layout);
                                }else {
                                    posVal = maxLen+val;
                                    if(LOG)Log.d("abcd",posVal+" "+val);
                                    linearLayout.addView(child_layout,posVal+1);
                                    val = val+1;
                                    if(LOG)Log.d("abcd","1: "+posVal+" "+val);
                                    sectionForm(section_id,maxLen,child_layout);
                                }
                            }
                        }
                    }else {
                        //linearLayout.removeView(child_layout);
                        removeRadioremark(child_layout, 0);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        checkBoxList.add(checkBox);
        return checkBox;
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
                //FormType = cursor.getString(cursor.getColumnIndex("FormType"));
                Field_Id =cursor.getString(cursor.getColumnIndex("Field_Id"));
                SafeRange =cursor.getInt(cursor.getColumnIndex("SafeRange"));
                calculationvalue =cursor.getInt(cursor.getColumnIndex("Calculation"));
                IMS_Id = cursor.getString(cursor.getColumnIndex("IMS_Id"));
                if(LOG)Log.d("IMS_Id",IMS_Id);
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
                if (Field_Type.equals("timeconsumption")) {

                    try {
                        formLayout.addView(dglinearlayout(Mandatory, Field_Label, Id, "", " , , "));
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

                        //formLayout.addView(sectionLinearLayout(Mandatory, Field_Label, Id, Field_Options, section, String.valueOf(sid), ""));
                        //formLayout.addView(qualifiactionBranching(Field_Options, Id, Value, "",formLayout, section, sid));
                        formLayout.addView(checkboxSection(Field_Label,Field_Options,Id,section,sid));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (Field_Type.equals("checkbox")){
                    try {
                        //formLayout.addView(textView(Field_Label));
                        String[] optionList = Field_Options.split(",");
                        for (int i = 0; i < optionList.length; i++) {
                            if(LOG)Log.d("IMS",IMS_Id);
                            formLayout.addView(multicheckLayout(optionList[i],Id,i+1,IMS_Id));
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                /*if (Field_Type.equals("section")) {
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
                }*/
                if (Field_Type.equals("textarea")) {
                    try {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(editTextarea1(Field_Label, Id, Value));
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
        for(EditText textView :editTextareaRemarkList ){
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
        for(RadioGroup radioSection :textRadioGroupSectionList ){
            layout.removeView(radioSection);
        }
        for(CheckBox checkBox :checkBoxList ){
            layout.removeView(checkBox);
        }

    }

    private RadioButton radioButtonBranching(String strvalue, int SelectedId, String matchvalue,String Section_Id,int sid,LinearLayout bracnhinglayout) {   //965

        final RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(SelectedId);
        if(Completed.equals("Completed")) {
            radioButton.setEnabled(false);
            if(matchvalue.equals(strvalue)){
                sectionForm(Section_Id, SelectedId, bracnhinglayout);
            }
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
        //ArrayList<String> checkBox = new ArrayList<String>();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Task_Id", uuid);
        contentValues.put("Asset_Id", AssetId);
        contentValues.put("UpdatedStatus", "no");
        contentValues.put("Task_Start_At",applicationClass.yymmddhhmmss());
        contentValues.put("Activity_Frequency_Id", frequencyId);
        contentValues.put("Site_Location_Id", SiteId);


        for (CheckBox checkBox1 : CheckboxListNumber){
            String Form_Structure_Id1;
            try {
                if (checkBox1.isChecked()){
                    int fieldId = checkBox1.getId();
                    Form_Structure_Id1 = myDb.getfieldId(fieldId);
                    String formId = myDb.getFormId(Form_Structure_Id1);
                    slnew = checkBox1.getText().toString();
                    editText.put(myDb.getfieldId(fieldId), slnew);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("Task_Id", uuid);
                    contentValues1.put("Form_Id", formId);
                    contentValues1.put("Form_Structure_Id", Form_Structure_Id1);
                    contentValues1.put("Site_Location_Id", SiteId);
                    contentValues1.put("Parameter_Id", "");
                    contentValues1.put("Value", slnew);
                    contentValues1.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues1);
                    db.close();
                }else {
                    getEditTextNumberValue(uuid);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (CheckBox checkBox1 : CheckboxListNumber1){
            String Form_Structure_Id1;
            try {
                if (checkBox1.isChecked()){
                    int fieldId = checkBox1.getId();
                    Form_Structure_Id1 = myDb.getfieldId(fieldId);
                    String formId = myDb.getFormId(Form_Structure_Id1);
                    slnew = checkBox1.getText().toString();
                    editText.put(myDb.getfieldId(fieldId), slnew);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("Task_Id", uuid);
                    contentValues1.put("Form_Id", formId);
                    contentValues1.put("Form_Structure_Id", Form_Structure_Id1);
                    contentValues1.put("Site_Location_Id", SiteId);
                    contentValues1.put("Parameter_Id", "");
                    contentValues1.put("Value", slnew);
                    contentValues1.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues1);
                    db.close();
                }else {
                    getEditTextNumberValue1(uuid);
                    getEditTextTextValue1(uuid);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (CheckBox checkBox1 : CheckboxListDateTime){
            String Form_Structure_Id1;
            try {
                if (checkBox1.isChecked()) {
                    int fieldId = checkBox1.getId();
                    Form_Structure_Id1 = myDb.getfieldId(fieldId);
                    String formId = myDb.getFormId(Form_Structure_Id1);
                    slnew = checkBox1.getText().toString();
                    editText.put(myDb.getfieldId(fieldId), slnew);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("Task_Id", uuid);
                    contentValues1.put("Form_Id", formId);
                    contentValues1.put("Form_Structure_Id", Form_Structure_Id1);
                    contentValues1.put("Site_Location_Id", SiteId);
                    contentValues1.put("Parameter_Id", "");
                    contentValues1.put("Value", slnew);
                    contentValues1.put("UpdatedStatus", "no");
                    db = myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues1);
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (CheckBox checkBox1 : CheckboxListRadio){
            int fieldId = checkBox1.getId();
            try {
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                if (checkBox1.isChecked()){
                    slnew = checkBox1.getText().toString();
                    editText.put(myDb.getfieldId(fieldId), slnew);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("Task_Id", uuid);
                    contentValues1.put("Form_Id", formId);
                    contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                    contentValues1.put("Site_Location_Id", SiteId);
                    contentValues1.put("Parameter_Id", "");
                    contentValues1.put("Value", slnew);
                    contentValues1.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues1);
                    db.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (CheckBox checkBox1 : CheckboxListText){
            int fieldId = checkBox1.getId();
            try {
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                if (checkBox1.isChecked()){
                    slnew = checkBox1.getText().toString();
                    editText.put(myDb.getfieldId(fieldId), slnew);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("Task_Id", uuid);
                    contentValues1.put("Form_Id", formId);
                    contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                    contentValues1.put("Site_Location_Id", SiteId);
                    contentValues1.put("Parameter_Id", "");
                    contentValues1.put("Value", slnew);
                    contentValues1.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues1);
                    db.close();
                }else {
                    getEditTextTextValue(uuid);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
                }else if(intentFromReset.equals("Opening")){
                    contentValues.put("Task_Id",TaskId);
                    contentValues.put("Reset", "0");
                }else {
                    contentValues.put("Task_Id",TaskId);
                    contentValues.put("Reset", "0");
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

                editText.put(myDb.getfieldId(fieldId), slnew);
                if (intentFromReset.equals("Closing")){
                    Log.d("TestingValuesSplit1", edittextSplit[0] + " " + edittextSplit[1]+" "+intentFromReset+" "+ClosingTaskId+" "+TaskId);

                    contentValues1.put("Task_Id",ClosingTaskId);
                    contentValues1.put("Reset", "0");
                }else if(intentFromReset.equals("Opening")) {
                    Log.d("TestingValuesSplit2", edittextSplit[0] + " " + edittextSplit[1]+" "+intentFromReset+" "+ClosingTaskId+" "+TaskId);

                    contentValues1.put("Task_Id",TaskId);
                    contentValues1.put("Reset", "1");
                }else if(intentFromReset.equals("") || intentFromReset == null){
                    Log.d("TestingValuesSplit3", edittextSplit[0] + " " + edittextSplit[1]+" "+intentFromReset+" "+ClosingTaskId+" "+TaskId);

                    contentValues1.put("Task_Id",TaskId);
                    contentValues1.put("Reset", "0");
                }

                contentValues1.put("Asset_Id", AssetId);
                //contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Reading", edittextSplit[0]);
                contentValues1.put("UOM", edittextSplit[1]);
                //contentValues1.put("Reset","0");
                contentValues1.put("Site_Location_Id", SiteId);
                //contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.insert("Meter_Reading", null, contentValues1);
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

        /*for (CheckBox checkBox1 : checkBoxList){
            int fieldId = checkBox1.getId();
            try {
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                if(LOG)Log.d("FieldIdHere",fieldId+"");

                if (checkBox1.isChecked()){
                    int selectedTag = (int) checkBox1.getTag();
                    slnew = checkBox1.getText().toString();
                    if(LOG)Log.d("CheckedVal"," "+slnew+" IMSId: "+cbId1+" StrctId: "+Form_Structure_Id+" FieldId: "+fieldId+" "+cbIms);
                    editText.put(myDb.getfieldId(fieldId), slnew);
                    ContentValues contentValues1 = new ContentValues();
                    contentValues1.put("Task_Id", uuid);
                    contentValues1.put("Form_Id", formId);
                    contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                    contentValues1.put("Site_Location_Id", SiteId);
                    contentValues1.put("Parameter_Id", "");
                    contentValues1.put("Value", slnew);
                    *//*if(cbIms && cbId1 == fieldId){
                        contentValues1.put("IMSFlag", "1");
                    } else {
                        contentValues1.put("IMSFlag", "0");
                    }*//*
                    contentValues1.put("UpdatedStatus", "no");
                    db=myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues1);
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/


        String form_struct_id = "";
        String chvalue="";
        String Form_Structure_Idch="";
        for (CheckBox checkBox1 : checkBoxList){

            int fieldId = checkBox1.getId();
            try {
                Form_Structure_Idch = myDb.getfieldId(fieldId);

                if (checkBox1.isChecked()){
                    Log.d("Test",checkBox1.getText().toString()+" "+checkBox1.isChecked()+" "+Form_Structure_Idch);
                    slnew = checkBox1.getText().toString();
                    if(Form_Structure_Idch.equals(form_struct_id)) {
                        chvalue = chvalue.concat(",").concat(slnew);
                        UpdateCheck(uuid,Form_Id,form_struct_id,chvalue);
                    } else {
                        chvalue = slnew;
                        form_struct_id = Form_Structure_Idch;
                        InsertCheck(uuid,Form_Id,form_struct_id,chvalue);
                    }
                    editText.put(form_struct_id, chvalue);
                }
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
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextarea1.getText().toString();
                if (LOG) Log.d("sdcfsdfv", slnew);
                editText.put(Form_Structure_Id, slnew);
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Value", slnew);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                emaildata.put(myDb.getfieldId(fieldId), slnew);
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


        for (RadioGroup rdgrp : textRadioGroupSectionList) {
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
                    contentValues2.put("Form_Id", myDb.getFormId(myDb.getfieldId(cbid)));
                    contentValues2.put("Form_Structure_Id", myDb.getfieldId(cbid));
                    contentValues2.put("Site_Location_Id", SiteId);
                    contentValues2.put("Parameter_Id", "");
                    if(LOG)Log.d("ValueInRadio:", selectRB);
                    contentValues2.put("Value", selectRB);
                    contentValues2.put("UpdatedStatus", "no");
                    db = myDb.getWritableDatabase();
                    db.insert("Data_Posting", null, contentValues2);
                    ////////////////////////////////////////////////////////////////////
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
                    ////////////////////////////////////////////////////////////////////
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
                    parameter.close();
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

        for (RadioGroup rdgrp : textRadioList) {
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
                            String[] label = Field_Option_Id.split("\\|");
                            for (int i = 0; i < label.length; i++) {
                                if (Integer.parseInt(label[i]) == selectedId) {
                                    Toast.makeText(getApplicationContext(),"Alert Inserted",Toast.LENGTH_LONG).show();
                                    InsertAlert(uuid,Form_Structure_Id);
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    parameter.close();
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
                if(LOG)Log.d("Field_Id",Form_Structure_Id+" "+radioButton.isChecked());
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


                    if (PPM_Intent == null) {
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
                                for (int i = 0; i < label.length; i++) {
                                    if (Integer.parseInt(label[i]) == selectedId) {
                                        Toast.makeText(getApplicationContext(),"Alert Inserted",Toast.LENGTH_LONG).show();
                                        InsertAlert(uuid,Form_Structure_Id);
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if(PPM_Intent != null ) {
                        String query = "Select * FROM Parameter" +
                                " WHERE Form_Structure_Id = '"+Form_Structure_Id+"'";

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
                                for (int i = 0; i < label.length; i++) {
                                    if (Integer.parseInt(label[i]) == selectedId) {
                                        Toast.makeText(getApplicationContext(),"Alert Inserted",Toast.LENGTH_LONG).show();
                                        InsertAlert(uuid,Form_Structure_Id);
                                    }
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        parameter.close();
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
                    parameter.close();
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
                        String[] label = Field_Option_Id.split("\\|");
                        for (int i = 0; i < label.length; i++) {
                            if (Integer.parseInt(label[i]) == (item_pos - 1)) {
                                Toast.makeText(getApplicationContext(), "Alert Inserted", Toast.LENGTH_LONG).show();
                                InsertAlert(uuid, Form_Structure_Id);
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                parameter.close();
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



        for (EditText editTextRemarks : editTextRadioRemarkList) {
            try {
                ContentValues contentValuesRemarks = new ContentValues();
                int fieldIdRemarks = editTextRemarks.getId();
                String Form_Structure_Id = myDb.getfieldId(fieldIdRemarks);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextRemarks.getText().toString();
                editText.put(Form_Structure_Id, slnew);
                if(LOG)Log.d("TestRadioRemark","12 "+slnew+" "+uuid+" "+Form_Structure_Id);
                /*contentValuesRemarks.put("Task_Id", uuid);
                contentValuesRemarks.put("Form_Id", formId);
                contentValuesRemarks.put("Form_Structure_Id", Form_Structure_Id);
                contentValuesRemarks.put("Site_Location_Id", SiteId);*/
                contentValuesRemarks.put("Remark", slnew);
                /*contentValuesRemarks.put("Parameter_Id", "");
                contentValuesRemarks.put("UpdatedStatus", "no");*/
                emaildata.put(myDb.getfieldId(fieldIdRemarks),slnew);
                db = myDb.getWritableDatabase();
                long a =db.update("Data_Posting",contentValuesRemarks,"Form_Structure_Id = '"+Form_Structure_Id+"' AND Task_Id = '"+uuid+"'",null);
                if(LOG)Log.d("TestRadioRemarkUpdate","12 "+a);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (CheckBox imsCheck : imsCheckList){
            int fieldId = imsCheck.getId();
            try {
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                //int sid = myDb.getSid(Form_Structure_Id);
                    if (LOG) Log.d("FieldIdHere21", fieldId + "");
                    ContentValues contentValues1 = new ContentValues();
                    if (imsCheck.isChecked()) {
                        slnew = imsCheck.getText().toString();
                        if (LOG)
                            Log.d("CheckedVal", " " + slnew + " StrctId: " + Form_Structure_Id);
                        contentValues1.put("IMSFlag","1");
                    } else {
                        contentValues1.put("IMSFlag","0");
                    }
                    db = myDb.getWritableDatabase();
                    db.update("Data_Posting", contentValues1, "Form_Structure_Id = '" + Form_Structure_Id + "' AND Task_Id ='" + uuid + "'", null);
                    db.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (EditText editTextarea1 : editTextareaRemarkList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextarea1.getId();
                String Form_Structure_Id = myDb.getfieldId(fieldId);
                String formId = myDb.getFormId(Form_Structure_Id);
                slnew = editTextarea1.getText().toString();
                editText.put(Form_Structure_Id, slnew);
                if(LOG)Log.d("sadfgdfg",Form_Structure_Id+" "+slnew);
                /*contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", formId);
                contentValues1.put("Form_Structure_Id", Form_Structure_Id);
                contentValues1.put("Site_Location_Id", SiteId);*/
                contentValues1.put("Remark", slnew);
                /*contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");*/
                emaildata.put(myDb.getfieldId(fieldId),slnew);
                db = myDb.getWritableDatabase();
                db.update("Data_Posting", contentValues1,"Form_Structure_Id = '"+Form_Structure_Id+"' AND Task_Id = '"+uuid+"'",null);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        Log.d("TaskIdHere","::"+uuid);
            insertTicket(uuid);
        }

    public void InsertCheck(String TaskId,String Form_Id,String form_struct_id,String chvalue){
        try {
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("Task_Id", TaskId);
            contentValues1.put("Form_Id", Form_Id);
            contentValues1.put("Form_Structure_Id", form_struct_id);
            contentValues1.put("Site_Location_Id", SiteId);
            contentValues1.put("Parameter_Id", "");
            contentValues1.put("Value", chvalue);
            contentValues1.put("UpdatedStatus", "no");
            emaildata.put(form_struct_id,chvalue);
            db = myDb.getWritableDatabase();
            db.insert("Data_Posting", null, contentValues1);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void UpdateCheck(String TaskId,String Form_Id,String form_struct_id,String chvalue){
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put("Value", chvalue);
        emaildata.put(form_struct_id,chvalue);
        db = myDb.getWritableDatabase();
        db.update("Data_Posting", contentValues1,"Task_Id = '"+TaskId+"' and Form_Id = '"+Form_Id+"' and Form_Structure_Id='"+form_struct_id+"'",null);
        db.close();

    }

    public void smsAlert(String TaskId){
        //Recipient = myDb.getMobileNumber();  //"8087382026";
        StringBuilder sb =new StringBuilder();
        try{
        sb.append("User : ");
        sb.append(myDb.EmployeeName(User_Id));
        sb.append('\n');
        sb.append("Group : ");
        sb.append(myDb.UserGroupName(myDb.UserGroupId(User_Id)));
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

        String formQuery = "SELECT td.Asset_Name,td.Activity_Name,fs.Field_Id,fs.Field_Label,fs.Form_Id,dp.Value,am.Alert_Type AS Validation_Type,am.Alert_Type,td.Task_Start_At,td.Task_Scheduled_Date,td.Task_Status,pv.Field_Limit_From  || ' - ' ||  pv.Field_Limit_To AS Range,pv.Threshold_From  || ' - ' ||  pv.Threshold_To AS Safe_Range FROM Task_Details td INNER JOIN Data_Posting dp ON dp.Task_Id=td.Auto_Id INNER JOIN AlertMaster am ON (am.Task_Id=td.Auto_Id AND am.Form_Structure_Id=dp.Form_Structure_Id) INNER JOIN Form_Structure fs ON dp.Form_Structure_Id = fs.Field_Id INNER JOIN Parameter pv ON (pv.Activity_Frequency_Id=td.Activity_Frequency_Id AND pv.Form_Structure_Id=dp.Form_Structure_Id) WHERE dp.Task_Id = '"+TaskId+"' GROUP BY dp.Task_Id,dp.Form_Structure_Id ORDER BY fs.Form_Id,fs.Display_Order";
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
                        Message = sb.toString();
                    }
                }
                indx++;
            }while (cursorEmailAlert.moveToNext());
            sendSMS();
        }
        cursorEmailAlert.close();
        db.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
        if(LOG)Log.d("ClickedValue",Message);
    }

    public void sendSMS(){
        try {
            mainURL=myDb.getSmsUrl();
            if(LOG)Log.d("SMSURL"," "+mainURL);
            if (!mainURL.equals("")) {
                URL myURL = new URL(mainURL);
                HttpURLConnection myURLConnection = (HttpURLConnection) myURL.openConnection();
                UserName=myDb.getSmsUserName();
                Password=myDb.getSmsPassword();
                type=myDb.getSmsType();
                Recipient=myDb.getMobileNumber(SiteId);
                source=myDb.getSmsSource();
                myURLConnection.setRequestMethod("POST");
                OutputStream outputStream = myURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String data = URLEncoder.encode("user", "UTF-8") + "=" + URLEncoder.encode(UserName, "UTF-8")+"&"+
                        URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(Password, "UTF-8")+"&"+
                        URLEncoder.encode("accusage", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+"&"+
                        URLEncoder.encode("mobile", "UTF-8") + "=" + URLEncoder.encode(Recipient, "UTF-8")+"&"+
                        URLEncoder.encode("senderid", "UTF-8") + "=" + URLEncoder.encode(source, "UTF-8")+"&"+
                        URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(Message, "UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream in = new BufferedInputStream(myURLConnection.getInputStream());
                String response = convertStreamToString(in);

                if(LOG)Log.d("SMSUrl",mainURL +"?"+data);
                if(LOG)Log.d("RESPONSE", "" + response);
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
        }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void emailAlert(String TaskID){
        try {
            StringBuilder sb =new StringBuilder();
            sb.append("*** This is an automatically generated email, please do not reply ***");
            sb.append('\n');
            sb.append('\n');
            sb.append("User : ");
            sb.append(myDb.EmployeeName(User_Id));
            sb.append('\n');
            sb.append("Group : ");
            sb.append(myDb.UserGroupName(myDb.UserGroupId(User_Id)));
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
                emailBackgroundData(sb.toString(),"Alert");
            }
            cursorEmailAlert.close();
            //db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void emailBackgroundData(final String value,String val){
            try{
                final String email_to = myDb.emailList_To(SiteId);
                final String email_bcc = myDb.emailList_Bcc(SiteId);
                //final String email_cc =  databaseHelper.emailList_Cc(SiteId);
                if (val.equalsIgnoreCase("Alert")) {
                    if (!email_to.equals("")||!email_bcc.equals("")) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    com.example.google.csmia_temp.emailfiles.GMailSender sender = new com.example.google.csmia_temp.emailfiles.GMailSender(
                                            emailId,
                                            emailPwd);
                                    sender.sendMail(getResources().getString(R.string.application_name)+" "+myDb.SiteName(User_Id) + " Alert Report " + Task_Scheduled_Date, value,
                                            emailId,
                                            email_to,
                                            email_bcc);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        }).start();
                    }
                } else if(val.equalsIgnoreCase("Ticket")) {
                    if (!email_to.equals("")||!email_bcc.equals("")) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    com.example.google.csmia_temp.emailfiles.GMailSender sender = new com.example.google.csmia_temp.emailfiles.GMailSender(
                                            emailId,
                                            emailPwd);
                                    sender.sendMail(getResources().getString(R.string.application_name)+" "+myDb.SiteName(User_Id) + " Ticket Generated Alert " + Task_Scheduled_Date, value,
                                            emailId,
                                            email_to,
                                            email_bcc);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                }
                            }
                        }).start();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

    }

    public void insertTicket(String Task_Id) {
        /*String query = "SELECT dp.Task_Id,dp.Form_Id,dp.Form_Structure_Id,dp.Value,dp.IMSFlag,fs.Field_Label,fs.Field_Options,fs.Display_Order,fs.sections FROM Data_Posting dp \n" +
                "LEFT JOIN Form_Structure fs \n" +
                "ON fs.Field_Id = dp.Form_Structure_Id WHERE dp.Task_Id = '"+Task_Id+"' ORDER BY fs.Display_Order";*/


        String query = "SELECT td.Activity_Frequency_Id,dp.Task_Id,dp.Form_Id,dp.Form_Structure_Id,dp.Value,dp.IMSFlag,fs.Field_Label,fs.Field_Options,fs.Display_Order,fs.sections,fs.sid FROM Data_Posting dp \n" +
                "INNER JOIN Form_Structure fs ON fs.Field_Id = dp.Form_Structure_Id \n" +
                "INNER JOIN Task_Details td \n" +
                "ON dp.Task_Id = td.Auto_Id \n" +
                "WHERE dp.Task_Id = '"+Task_Id+"' ORDER BY fs.Display_Order";

        db = myDb.getWritableDatabase();
        String Form_Structure_Id="",Value,Frequency_Id,IMSFlag;
        Cursor ticketDetails = db.rawQuery(query, null);
        if (LOG)Log.d("LogData","TicketDetailsQuery: "+query+" cursorCount: "+ticketDetails.getCount());

        if (ticketDetails.getCount() > 0) {
            if (ticketDetails.moveToNext()) {
                do {
                    Value = ticketDetails.getString(ticketDetails.getColumnIndex("Value"));
                    IMSFlag = ticketDetails.getString(ticketDetails.getColumnIndex("IMSFlag"));
                    Form_Structure_Id = ticketDetails.getString(ticketDetails.getColumnIndex("Form_Structure_Id"));
                    Frequency_Id = ticketDetails.getString(ticketDetails.getColumnIndex("Activity_Frequency_Id"));
                    if (LOG)Log.d("LogData","TicketDetailsCursor: "+Form_Structure_Id+" "+IMSFlag);

                    if (IMSFlag!= null && !IMSFlag.equals("null")) {
                            try {
                                if (!IMSFlag.contains("0")) {
                                    Log.d("CheckedIMS",IMSFlag+" "+Form_Structure_Id);
                                    InsertTicketContent(Form_Structure_Id, Value, Task_Id);
                                } else {
                                    Log.d("UnCheckedIMS",IMSFlag+" "+Form_Structure_Id);

                                    String queryParameter = "Select * FROM Parameter" +
                                            " WHERE Activity_Frequency_Id = '" + Frequency_Id + "'" + "AND Form_Structure_Id = '" + Form_Structure_Id + "'";
                                    Cursor parameter = db.rawQuery(queryParameter, null);
                                    if (LOG)Log.d("LogData","ParameterQuery: "+queryParameter+" cusrsor:"+parameter.getCount());
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
                                        parameter.close();
                                        if (LOG)Log.d("LogData","alert: "+ Task_Id + " " + Form_Structure_Id);
                                        InsertAlert(Task_Id, Form_Structure_Id);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        //}
                    }




                    /*if(IMSFlag!= null && !IMSFlag.equals("null")) {
                        Log.d("LogData","IMSValue: "+IMSFlag);
                        if (!IMSFlag.contains("0")) {
                            Log.d("LogData","IMSTicket: "+IMSFlag);
                            InsertTicketContent(Form_Structure_Id, Value, Task_Id);
                        } else if(IMSFlag.contains("0")){
                            Log.d("LogData","IMSAlert: "+IMSFlag);
                            String queryParameter = "Select * FROM Parameter" +
                                    " WHERE Activity_Frequency_Id = '" + Frequency_Id + "'" + "AND Form_Structure_Id = '" + Form_Structure_Id + "'";
                            Cursor parameter = db.rawQuery(queryParameter, null);
                            Log.d("LogData","ParameterQuery: "+queryParameter+" cusrsor:"+parameter.getCount());
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
                                parameter.close();
                                Log.d("LogData","alert: "+IMSFlag + " " + Task_Id + " " + Form_Structure_Id);
                                Log.d("Tetstsdfgs", IMSFlag + " " + Task_Id + " " + Form_Structure_Id);
                                InsertAlert(Task_Id, Form_Structure_Id);
                            }

                        }
                    }*/

                } while (ticketDetails.moveToNext());
            }
        }
        ticketDetails.close();
    }

    public void InsertTicketContent(String Form_Structure_Id,String Value,final String Task_Id) {

        ContentValues contentValues = new ContentValues();
        String [] opt = Value.split(",");
        for(int i = 0 ; i < opt.length; i++){
            String query = "SELECT Auto_Id,Product,Component,Department,Option_Selected,Assigned_To,IncidentSource,LoggedBy FROM Form_Ticket WHERE Form_section_Id = '"+Form_Structure_Id+"' AND Option_Selected = '"+opt[i]+"'";

            SQLiteDatabase db1 = myDb.getWritableDatabase();
            Cursor ticketDetails = db1.rawQuery(query, null);
            try {
                if(LOG)Log.d("ticketDetails",query+" "+ticketDetails.getCount());
                if (ticketDetails.getCount() > 0) {
                    if (ticketDetails.moveToNext()) {
                        do {
                            uuid = UUID.randomUUID().toString();
                            final String Ticket_Id = ticketDetails.getString(ticketDetails.getColumnIndex("Auto_Id"));
                            String Type = ticketDetails.getString(ticketDetails.getColumnIndex("Department"));
                            String Subject = ticketDetails.getString(ticketDetails.getColumnIndex("Component"));
                            String Group_Id = ticketDetails.getString(ticketDetails.getColumnIndex("Assigned_To"));
                            String Product = ticketDetails.getString(ticketDetails.getColumnIndex("Product"));
                            String IncidentSource = ticketDetails.getString(ticketDetails.getColumnIndex("IncidentSource"));
                            String LoggedBy = ticketDetails.getString(ticketDetails.getColumnIndex("Loggedby"));

                            if(LOG)Log.d("INSERTTicket","132456 "+ticketDetails.getCount()+" "+ticketDetails.getString(ticketDetails.getColumnIndex("Auto_Id"))+" "+ticketDetails.getString(ticketDetails.getColumnIndex("Department"))+" "+ticketDetails.getString(ticketDetails.getColumnIndex("Component")));
                            contentValues.put("Auto_Id",uuid);
                            contentValues.put("Site_Location_Id",myDb.Site_Location_Id(User_Id));
                            contentValues.put("Task_Id",Task_Id);
                            contentValues.put("Form_Ticket_Id",Ticket_Id);
                            contentValues.put("Created_User_Id",User_Id);
                            contentValues.put("User_Group_Id",User_Group_Id);
                            contentValues.put("Ticket_Type",Type);
                            contentValues.put("Ticket_Subject",Subject);
                            contentValues.put("Ticket_status","Open");
                            contentValues.put("Status","0");
                            contentValues.put("Created_DateTime",applicationClass.yymmddhhmmss());
                            contentValues.put("Record_Status","I");
                            contentValues.put("UpdatedStatus","no");

                            long result = db1.insert("Ticket_Created", null, contentValues);
                            if(LOG)Log.d("Ticket_Created","132456 "+result+" "+uuid);

                            String selectQuery = "SELECT  * FROM Ticket_Created WHERE UpdatedStatus = 'no' AND User_Group_Id IN ("+myDb.UserGroupId(User_Id)+") and Task_Id = '"+Task_Id+"' and Form_Ticket_Id = '"+Ticket_Id+"'";
                            if(LOG)Log.d("TicketQuery"," "+selectQuery);
                            String groupName="",Remark="";
                            try {
                                if(!db1.isOpen()) {
                                    db1 = myDb.getWritableDatabase();
                                }
                                Cursor cursor = db1.rawQuery(selectQuery, null);
                                if(LOG)Log.d("TicketQueryCount", " " + cursor.getCount());
                                if (cursor.getCount() > 0) {
                                    String GroupQuery = "SELECT Group_Name from User_Group where User_Group_Id = '" + Group_Id + "'";
                                    Cursor groupQueryCursor = db1.rawQuery(GroupQuery, null);
                                    if (groupQueryCursor.getCount() > 0) {
                                        if (groupQueryCursor.moveToFirst()) {
                                            do {
                                                groupName = groupQueryCursor.getString(groupQueryCursor.getColumnIndex("Group_Name"));
                                            } while (groupQueryCursor.moveToNext());
                                        }
                                    }
                                    groupQueryCursor.close();

                                    String RemarkQuery = "Select Remark from Data_Posting where Task_Id = '" + Task_Id + "' and Form_Structure_Id = '" + Form_Structure_Id + "'";
                                    Cursor RemarkCursor = db1.rawQuery(RemarkQuery, null);

                                    if(LOG)Log.d("Remark", "" + RemarkCursor.getCount() + " " + RemarkQuery);
                                    if (RemarkCursor.getCount() > 0) {
                                        if (RemarkCursor.moveToFirst()) {
                                            do {
                                                Remark = RemarkCursor.getString(RemarkCursor.getColumnIndex("Remark"));
                                                //IMSFlag = RemarkCursor.getString(RemarkCursor.getColumnIndex("IMSFlag"));
                                                if(LOG)Log.d("ValRemark", "" + Remark );
                                                //if (IMSFlag.contains("1")) {
                                                String data = URLEncoder.encode("site", "UTF-8") + "=" + URLEncoder.encode(myDb.getAssetSite(AssetId), "UTF-8") + "&" +
                                                        URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(myDb.getAssetLocationFloor(AssetId), "UTF-8") + "&" +
                                                        URLEncoder.encode("sublocation", "UTF-8") + "=" + URLEncoder.encode(myDb.getAssetSubLocation(AssetId), "UTF-8") + "&" +
                                                        URLEncoder.encode("serviceArea", "UTF-8") + "=" + URLEncoder.encode(Asset_Name, "UTF-8") + "&" +
                                                        URLEncoder.encode("department", "UTF-8") + "=" + URLEncoder.encode(groupName, "UTF-8") + "&" +
                                                        URLEncoder.encode("product", "UTF-8") + "=" + URLEncoder.encode(Product, "UTF-8") + "&" +
                                                        URLEncoder.encode("component", "UTF-8") + "=" + URLEncoder.encode(Subject, "UTF-8") + "&" +
                                                        URLEncoder.encode("IncidentSource", "UTF-8") + "=" + URLEncoder.encode(IncidentSource, "UTF-8") + "&" +
                                                        URLEncoder.encode("ProblemTitle", "UTF-8") + "=" + URLEncoder.encode(Type, "UTF-8") + "&" +
                                                        URLEncoder.encode("problemDescription", "UTF-8") + "=" + URLEncoder.encode("" + Remark, "UTF-8") + "&" +
                                                        URLEncoder.encode("Loggedby", "UTF-8") + "=" + URLEncoder.encode(LoggedBy, "UTF-8") + "&" +
                                                        URLEncoder.encode("CreatedDateTime", "UTF-8") + "=" + URLEncoder.encode(new applicationClass().yymmddhhmmss(), "UTF-8");
                                                String url = "https://punctualiti.in/csmia/android/raiseticket.php?";

                                                Log.d("Ticket Url",url+data);

                                                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url+data, null, new Response.Listener<JSONArray>() {
                                                    @Override
                                                    public void onResponse(JSONArray response) {
                                                        try {
                                                            //UpdateTicket(uuid,response.toString());
                                                            ContentValues value = new ContentValues();
                                                            value.put("Status","1");
                                                            Log.d("Response Log", "123 " + response.toString()+" "+Ticket_Id);
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Ticket Generated", Toast.LENGTH_SHORT).show();
                                                            if(!db.isOpen()){
                                                                db = myDb.getWritableDatabase();
                                                            }

                                                            long d = db.update("Ticket_Created",value,"Task_Id ='"+Task_Id+"' and Form_Ticket_Id='"+Ticket_Id+"'",null);
                                                            Log.d("Test_update","21 "+d+" "+uuid+" "+Ticket_Id);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        NetworkResponse response = error.networkResponse;
                                                        if (error instanceof ServerError && response != null) {
                                                            try {
                                                                String res = new String(response.data,
                                                                        HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                                                                // Now you can use any deserializer to make sense of data
                                                                if(LOG)Log.d("API error", "" + res);
                                                                //JSONObject obj = new JSONObject(res);
                                                            } catch (UnsupportedEncodingException e1) {
                                                                // Couldn't properly decode data to string
                                                                e1.printStackTrace();
                                                            } /*catch (JSONException e2) {
                                                    // returned data is not JSONObject?
                                                    e2.printStackTrace();
                                                }*/
                                                        }

                                                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                                                        error.printStackTrace();
                                                        Toast.makeText(getApplicationContext(),
                                                                "Something went wrong at server end", Toast.LENGTH_SHORT).show();
                                                    }
                                                }) {
                                                    @Override
                                                    public Map<String, String> getHeaders() throws AuthFailureError {
                                                        Map<String, String> params = new HashMap<>();
                                                        params.put("Content-Type", "application/json");
                                                        return params;
                                                    }
                                                };
                                                new applicationClass().getInstance().addToRequestQueue(jsonArrayRequest);
                                                //}
                                            } while (RemarkCursor.moveToNext());
                                        }
                                    }
                                    RemarkCursor.close();


                                }
                                cursor.close();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        } while (ticketDetails.moveToNext());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ticketDetails.close();
        }
    }

/*
    private void setValues(String Site,String Location, String SubLocation, String ServiceArea,String Product,String Component,String Department){
        StringBuilder sb =new StringBuilder();
        sb.append("*** This is an automatically generated email, please do not reply ***");
        try {

            sb.append('\n');
            sb.append('\n');
            sb.append('\n');
            sb.append("Product: "+Product+"\n" +
                    "Component: "+Component+"\n" +
                    "Department: "+Department+"\n" +
                    "Site: "+Site+"\n" +
                    "Location: "+Location+"\n" +
                    "Sub Location: "+SubLocation+"\n" +
                    "Service Area: "+ServiceArea+"\n" +
                    "Status: Open");
            sb.append('\n');
            sb.append('\n');

            if(LOG)Log.d("Ticket",sb.toString());

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
*/

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
        } else {
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
        if (Checklist == null){
            if (PPM_Intent == null) {
                Intent intent = new Intent(DynamicForm.this, TaskDetails.class);
                intent.putExtra("TAB","TAB2");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(DynamicForm.this, ppm_activity.class);
                intent.putExtra("TAB","TAB2");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }else {
            Intent intent = new Intent(DynamicForm.this, CheckList.class);
            intent.putExtra("TAB","TAB2");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }


    }


    private class Connection extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... Id) {
            Log.d("TaskId",Id.toString()+" "+Id.length);
            for(int i = 0; i< Id.length;i++){
                Log.d("TaskId",Id[i]+" "+Id.length);

                smsAlert(Id[i]);
            }
            //smsAlert(Id.toString());
            return null;
        }
    }

}

