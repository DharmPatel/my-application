package com.example.google.template;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
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

public class IncidentForm extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CAMERA_REQUESTAsset = 1889;
    private static final int CAMERA_REQUESTAssets = 1890;
    static final boolean LOG = new applicationClass().checkLog();

    private List<EditText> editTextList = new ArrayList<EditText>();
    private List<EditText> editTextwithEditText1 = new ArrayList<EditText>();
    private List<EditText> editTextwithEditText2 = new ArrayList<EditText>();
    private List<TextView> textWithtext = new ArrayList<TextView>();
    private List<EditText> editTextDateTimeList = new ArrayList<EditText>();
    private List<EditText> editTextTimeList = new ArrayList<EditText>();
    private List<RadioGroup> radioGroupTimeList = new ArrayList<RadioGroup>();
    private List<RadioButton> radiobuttonTimeList = new ArrayList<RadioButton>();
    private List<EditText> editTextareaList = new ArrayList<EditText>();
    private List<RadioButton> textRadioButtonList = new ArrayList<RadioButton>();
    private List<EditText> editTextRemarkList = new ArrayList<EditText>();
    private List<TextView> textViewList = new ArrayList<TextView>();
    private List<Spinner> textSpinnerList = new ArrayList<Spinner>();
    private List<RadioGroup> textRadioGroupList = new ArrayList<RadioGroup>();
    private List<CheckBox> CheckboxList = new ArrayList<CheckBox>();
    private HashMap<CheckBox,Integer> CheckboxListhash = new HashMap<>();
    private List<EditText> editTextListMeter = new ArrayList<EditText>();
    Map<String,Bitmap> drawableBitmap = new HashMap<String,Bitmap>();
    private List<ImageView> imageAsset = new ArrayList<ImageView>();
    private List<TextView> previousReadingChange = new ArrayList<TextView>();
    private List<RadioGroup> textRadioGroupMeterList = new ArrayList<RadioGroup>();
    ArrayList<String> previoudReadings = new ArrayList<>();
    String previousReadingDatabase="";
    Button btn_get_sign, mClear, mGetSign, mCancel;
    private List<ImageView> imageSignature = new ArrayList<ImageView>();
    Spinner sp1;
    int textviewId;
    TextView reading;
    String readingConstant ="";
    Button submit;
    SQLiteDatabase db;
    DatabaseHelper myDb;
    String Section_Id,unplanned,Field_Label,Field_Type,User_Id,SiteId,Scan_Type,AssetId,Activity_Name,User_Group_Id,AssetCode,Asset_Status,Asset_Location,Value="",TaskId,Asset_Name,Field_Options,Form_Id,Form_IdIntent,FixedValue,section,FormType,Field_Id,activityFrequencyId,formStructureId,field_Limit_Form,field_Limit_To,threshold_From,threshold_To,validation_Type,Critical,Field_Option_Id,Frequency_Id,date_time;
    int Id,Sid,Incident,Mandatory,sid,SafeRange,parameterCount;
    ImageView taskSelfie,assetImage;
    LinearLayout formLayout;
    LinearLayout.LayoutParams textLayout;
    SharedPreferences settings;
    int mYear;
    int mMonth;
    int mDay;
    int mHour;
    int mMinute;
    String timeUnit= "";

    Dialog dialog;
    LinearLayout mContent;
    View view;
    signature mSignature;
    Bitmap mBitmap;
    File root = new File(Environment.getExternalStorageDirectory(), "DigitSign");
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = root + pic_name + ".png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_incident);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        myDb = new DatabaseHelper(getApplicationContext());
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        Section_Id = getIntent().getStringExtra("Section_Id");
        Sid = getIntent().getIntExtra("sid", 0);
        Frequency_Id=getIntent().getStringExtra("FrequencyId");
        Activity_Name=getIntent().getStringExtra("Activity_Name");
        Asset_Name=getIntent().getStringExtra("Asset_Name");
        AssetId=getIntent().getStringExtra("AssetId");
        Form_IdIntent=getIntent().getStringExtra("Form_Id");
        AssetCode=getIntent().getStringExtra("AssetCode");
        Asset_Location=getIntent().getStringExtra("Asset_Location");
        Asset_Status=getIntent().getStringExtra("Asset_Status");
        User_Group_Id=getIntent().getStringExtra("User_Group_Id");
        Incident=getIntent().getIntExtra("Incident", 0);
        unplanned=getIntent().getStringExtra("unplanned");
        TaskId=getIntent().getStringExtra("TaskId");
        User_Id = settings.getString("userId", null);
        Scan_Type = myDb.ScanType(User_Id);
        SiteId = myDb.Site_Location_Id(User_Id);
        if(Incident==0){
            createSectionForm();
        }else {
            viewSectionForm();
        }
    }

    public void viewSectionForm()
    {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        formLayout = new LinearLayout(this);
        formLayout = new LinearLayout(this);
        formLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(formLayout);
        toolbar(formLayout);
        textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(10, 10, 10, 0);
        try {
            String formQuery = "select a.*,b.Value from Form_Structure a,Data_Posting b where a.Field_Id=b.Form_Structure_Id and a.Form_Id='" + Section_Id + "' and b.Task_Id='"+TaskId+"' and a.Record_Status <> 'D' ORDER BY a.Display_Order ASC";
            Log.d("Teasrasdasd",formQuery);
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
                    SafeRange = cursor.getInt(cursor.getColumnIndex("SafeRange"));
                    Value = cursor.getString(cursor.getColumnIndex("Value"));

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

                    if (Field_Type.equals("text")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            if (parameterCount==0) {
                                formLayout.addView(editText(Mandatory, Field_Label, Id, Value, "", "", "", "", "", SafeRange));
                            } else {
                                formLayout.addView(editText(Mandatory, field_Limit_Form + " - " + field_Limit_To + " (Safe Range: " + threshold_From + " - " + threshold_To + " )", Id, Value, field_Limit_Form, field_Limit_To, threshold_From, threshold_To, validation_Type, SafeRange));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("dropdown")) {

                        try {
                            if (parameterCount==0) {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, Value, "", section));
                            } else {
                                formLayout.addView(spinnerlinearlayout(Field_Label, Field_Options, Id, Value, Field_Option_Id, section));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("checkboxbranching")) {
                        try {
                            //formLayout.addView(textView(Field_Label));
                            formLayout.addView(checkboxlinearlayout(Field_Label, Field_Options, Id, Value, Field_Option_Id, section));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("textwithtext")) {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(multipleTextlinearlayout(Id,Field_Label, Field_Options,Value));

                    }
                    if (Field_Type.equals("radio")) {
                        try {
                            if (parameterCount==0) {
                                formLayout.addView(radiolinearlayout(Field_Label, Field_Options, Id, Value,""));
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
                            formLayout.addView(editTextDateTime(Id, "D", Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (Field_Type.equals("date")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextDate(Id, "D", Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (Field_Type.equals("time")) {

                        try {
                            //formLayout.addView(textView(Field_Label));
                            formLayout.addView(timeeditText(Field_Label, Id, Value));
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

                    if (Field_Type.equals("incidentNumber")) {
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
                            formLayout.addView(editTextRemark(Field_Label, Id, Value));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(new applicationClass().imageVariable().equals("yes")) {

            byte[] image="".getBytes();

            taskSelfie = new ImageView(this);
            taskSelfie.setImageResource(R.drawable.ic_cam);
            formLayout.addView(textView("Image"));
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
        submit.setText("BACK");
        submit.setTextColor(Color.WHITE);
        submit.setLayoutParams(textLayout);
        submit.setBackgroundColor(getResources().getColor(R.color.bg_main));
        formLayout.addView(submit);
        setContentView(scrollView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IncidentForm.this, IncidentReport.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public void createSectionForm()
    {
        ScrollView scrollView = new ScrollView(this);
        scrollView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        formLayout = new LinearLayout(this);
        formLayout = new LinearLayout(this);
        formLayout.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(formLayout);
        toolbar(formLayout);
        textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.setMargins(10, 10, 10, 0);
        try {
            String formQuery = "select * from Form_Structure where Form_Id='" + Section_Id + "' and Record_Status <> 'D'  ORDER BY Display_Order ASC"; //and sid='"+Sid+"'
            if(LOG) Log.d("ASdasdasd",formQuery);
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

                    if (Field_Type.equals("textwithtext")) {
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(multipleTextlinearlayout(Id,Field_Label, Field_Options," , "));

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

                    if (Field_Type.equals("datetime")) {

                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextDateTime(Id, "", ""));
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
                    if (Field_Type.equals("remark")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextRemark(Field_Label, Id, ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //Log.d("FrasdasFiresdType",Field_Type);
                    if (Field_Type.equals("checkboxbranching")) {
                        try {
                            //formLayout.addView(textView(Field_Label));
                            formLayout.addView(checkboxlinearlayout(Field_Label, Field_Options, Id, null, Field_Option_Id, section));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                  /*  if (parameterCount==0) {
                        formLayout.addView(checkboxlinearlayout(Field_Label, Field_Options, Id, "", ""));
                    } else {*/
                    // }

                    if (Field_Type.equals("incidentNumber")) {
                        try {
                            formLayout.addView(textView(Field_Label));
                            formLayout.addView(editTextincidentNumber(Field_Label, Id, Value));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
                while (cursor.moveToNext());

            }
            cursor.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(new applicationClass().imageVariable().equals("yes")) {
            taskSelfie = new ImageView(this);
            taskSelfie.setImageResource(R.drawable.ic_cam);
            formLayout.addView(textView("Image"));
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
        submit.setLayoutParams(textLayout);
        submit.setBackgroundColor(getResources().getColor(R.color.bg_main));
        formLayout.addView(submit);
        setContentView(scrollView);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkSubmitEditText() == true && checkSubmitRadio() == true && checkSpinnerValue() == true) {
                    saveData(taskInsert());
                    Intent intent = new Intent(IncidentForm.this, IncidentReport.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public boolean checkSubmitEditText() {
        boolean editTextCheck = false;
        try {
            if (editTextList.size() == 0){
                editTextCheck = true;
            }else {
                for (EditText editLongText : editTextList) {
                    if (editLongText.getText().toString().equals("")) {
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
    public  void  toolbar(LinearLayout linearLayout){
        Toolbar toolbar = new Toolbar(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

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
        textViewAsset.setText(Asset_Name + "     [ Incident ]");
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

    private TextView textView(String label) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText("" + label + ": ");
        textView.setTextColor(getResources().getColor(R.color.bg_main));
        textView.setLayoutParams(textLayout);
        textViewList.add(textView);
        return textView;
    }
    private TextView perviousReading(String reading, int id) {
        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setId(id);
        textView.setEnabled(false);
        textView.setText("Previous Reading: " + reading + "");
        textView.setLayoutParams(textLayout);
        textViewList.add(textView);
        previousReadingChange.add(textView);
        previoudReadings.add(reading);
        return textView;
    }
    private EditText editText(final int mandatory, String name, final int id, String setText, final String Field_Limit_From, final String Field_Limit_To, final String Threshold_From, final String Threshold_To, String Validation_Type,int SafeRange) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if(SafeRange==1){
            editText.setHint(name);
        }
        if(Incident==1){
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
    private LinearLayout spinnerlinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id,String section) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(qualifiaction(field_option, id, setText, radioLayout, Field_Option_Id, section));
        return radioLayout;
    }
    private LinearLayout multipleTextlinearlayout(int Id,String Label,String Field_Option,String setText ) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.HORIZONTAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.setWeightSum(1f);
        String[] LabelforText=Field_Option.split(",");
        String[] Value=setText.split(",");
        radioLayout.addView(textwithtext1(Id, LabelforText[0], 0.5f, Value[0]));
        radioLayout.addView(textwithtext2(Id, LabelforText[1], 0.5f, Value[1]));
        return radioLayout;
    }
    private EditText textwithtext1(int Id,String Label,float Weight,String setText) {
        final EditText editText = new EditText(this);
        editText.setId(Id);
        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.weight=Weight;
        textLayout.setMargins(10, 10, 10, 0);
        editText.setLayoutParams(textLayout);
        editText.setHint(Label);
        if(Incident==1){
            editText.setEnabled(false);
            editText.setText(setText);
        }
        //editText.setBackgroundResource(R.drawable.edittext_shape);
        editTextwithEditText1.add(editText);
        return editText;
    }

    private EditText textwithtext2(int Id,String Label,float Weight,String setText) {
        final EditText editText = new EditText(this);
        editText.setId(Id);
        LinearLayout.LayoutParams textLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayout.weight=Weight;
        textLayout.setMargins(10, 10, 10, 0);
        editText.setLayoutParams(textLayout);
        editText.setHint(Label);
        if(Incident==1){
            editText.setEnabled(false);
            editText.setText(setText);
        }
        //editText.setBackgroundResource(R.drawable.edittext_shape);
        editTextwithEditText2.add(editText);
        return editText;
    }

    private LinearLayout checkboxlinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id,String Section) {
        LinearLayout checkboxLayout = new LinearLayout(getApplicationContext());
        checkboxLayout.setOrientation(LinearLayout.VERTICAL);
        checkboxLayout.setLayoutParams(textLayout);
        checkboxLayout.addView(textView(field_Label));
        String[] checkboxList = field_option.split(",");

        for(int i = 0; i<checkboxList.length;i++){
            checkboxLayout.addView(createCheckBox(checkboxList[i], i, setText, checkboxLayout, Field_Option_Id,Section,id));
        }

        return checkboxLayout;
    }
    private CheckBox createCheckBox(String field_option,final int id, String setText,final LinearLayout layout,String Field_Option_Id,final String Section,int idForm){
        final CheckBox cbLayout = new CheckBox(this);
        final LinearLayout checkboxLayout = new LinearLayout(getApplicationContext());

        try {

            cbLayout.setId(id);
            cbLayout.setText(field_option);
            cbLayout.setLayoutParams(textLayout);
            final String[] sectionIds = Section.split("\\|");
            // layout.addView(cbLayout);
            if(Incident == 1) {
                cbLayout.setEnabled(false);
                if(setText != null){
                    String[]  setTextValue = setText.split(",");
                    for(int i = 0;i<setTextValue.length;i++){
                        if(field_option.equals(setTextValue[i])){
                            cbLayout.setChecked(true);
                        }
                    }
                }

            }else{
                cbLayout.setChecked(false);
                cbLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(LOG) Log.d("TestingCheckBoxClick", cbLayout.getText().toString() + "");
                        if (cbLayout.isChecked()) {
                            layout.addView(checkboxLayout);
                            checkboxCheckedlinearlayout(checkboxLayout, id, cbLayout.getText().toString(), cbLayout.isChecked(), sectionIds[id]);
                        } else {
                            if (((LinearLayout) checkboxLayout).getChildCount() > 0)
                                ((LinearLayout) checkboxLayout).removeAllViews();
                            //removeRadioremark(checkboxLayout,0);
                            layout.removeView(checkboxLayout);
                        }
                    }
                });
            }

            CheckboxListhash.put(cbLayout,idForm);
            CheckboxList.add(cbLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cbLayout;
    }

    //,String field_option,int id,String setText,String Field_Option_Id
    private void checkboxCheckedlinearlayout(LinearLayout checkboxLayout,int id,String field_Label,boolean value,String Section) {

        checkboxLayout.setOrientation(LinearLayout.VERTICAL);
        checkboxLayout.setLayoutParams(textLayout);
        checkboxLayout.addView(textView(field_Label));
        checkboxLayout.setId(500 + id);

        //String[] checkboxList = field_option.split(",");
        /*for(int i = 0; i<checkboxList.length;i++){
            checkboxLayout.addView(createCheckBox(checkboxList[i], i, setText, checkboxLayout, Field_Option_Id));
        }*/
        if(LOG) Log.d("TestingValuewqad", value + "");
        //if(value){
        sectionForm(Section, checkboxLayout);
        //

        //return checkboxLayout;
    }
    private RadioGroup radiogroupMeter(String optionRadio, int id, String setText) {

        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());

        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButton(optionRadioList[i], i, setText));
        }
        textRadioGroupMeterList.add(radioGroup);
        uomChange(textRadioGroupMeterList);
        return radioGroup;
    }


    public void uomChange(List<RadioGroup> radioGroups) {
        int i = 0;

        for(RadioGroup radioGroup1:radioGroups) {
            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    try {
                        RadioButton radioButton = (RadioButton) radioGroup.getChildAt(checkedId);
                        for (int k = 0; k < previousReadingChange.size(); k++) {
                            textviewId = previousReadingChange.get(k).getId();
                            if (textviewId == radioGroup.getId() - 1) {

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
                                    reading.setText("Previous Reading: " + d.setScale(3, BigDecimal.ROUND_HALF_UP) + " KW");
                                    //reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP)+ " KW");
                                }
                            } else if (radioButton.getId() == 1) {
                                if (c.equals("KW")) {

                                    Double a2 = a / 1000;
                                    BigDecimal d = new BigDecimal(a2);
                                    reading.setText("Previous Reading: " + d.setScale(3, BigDecimal.ROUND_HALF_UP) + " MW");
                                    //reading.setText("Previous Reading: " + d.setScale(3,BigDecimal.ROUND_HALF_UP) + " MW");
                                }
                            }*/
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });


        }
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

        for(EditText textView :editTextareaList ){
            layout.removeView(textView);
        }
        for(Spinner textView :textSpinnerList ){
            layout.removeView(textView);
        }
        for(RadioGroup textView :textRadioGroupList ){
            layout.removeView(textView);
        }
        for(RadioGroup textView :textRadioGroupList ){
            layout.removeView(textView);
        }

        for(Spinner textView :textSpinnerList ){
            layout.removeView(textView);
        }

    }

    private LinearLayout incidentlinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id,String section,int sid) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(incidentSpinner(field_option, id, setText, radioLayout, Field_Option_Id, section,sid));
        return radioLayout;
    }

    private Spinner incidentSpinner(String options,final int sp_id, String qval,final LinearLayout linearLayout, final String Field_Option_Id,final String Section_Id,final int sid) {
        final Spinner qualifiactionSpinner = new Spinner(this);
        final LinearLayout spinnerForm = new LinearLayout(this);



        qualifiactionSpinner.setId(sp_id);
        sp1 = (Spinner) findViewById(qualifiactionSpinner.getId());
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("-- Select One --");

        final TextView textView = new TextView(this);
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
        if(Incident==1){
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
                if (Incident == 1) {
                    qualifiactionSpinner.setEnabled(false);
                    qualifiactionSpinner.setClickable(false);
                } else {
                    int positionvalue = position - 1;
                    if(LOG) Log.d("Asdfasdasdare", positionvalue + " " + sid);
                    if (positionvalue == sid) {



                /*    editor= settings.edit();
                    editor.putString("IncidentSpValue",SpinnerValue);
                    editor.putInt("IncidentSpId", SpinnerId);
                    editor.commit();*/
                        String formQuery = "select * from Form_Structure where Form_Id='" + Section_Id + "' and Record_Status <> 'D'  ORDER BY Display_Order ASC";//and sid='" + positionvalue + "'
                        db = myDb.getWritableDatabase();
                        Cursor cursor = db.rawQuery(formQuery, null);
                        if (cursor.getCount() > 0) {
                            if(LOG) Log.d("ASDasdasdasd", "TEasd" + formQuery);
                            spinnerForm.setOrientation(LinearLayout.VERTICAL);
                            spinnerForm.setLayoutParams(textLayout);
                            int idlayout = 500 + sp_id + positionvalue;
                            spinnerForm.setId(idlayout);
                            linearLayout.addView(spinnerForm);
                            sectionFormSpinner(Section_Id, spinnerForm, positionvalue);
                        }
                    } else {
                        linearLayout.removeView(spinnerForm);
                        removeRadioremark(spinnerForm, 0);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //textSpinnerList.add(qualifiactionSpinner);
        return qualifiactionSpinner;
    }


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
                if(LOG) Log.d("Testinbvalsdasd",Field_Type +" "+ formLayout.getId());
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
                }*/

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
                        /*formLayout.addView(textView(Field_Label));
                        formLayout.addView(qualifiaction(Field_Options, Id, "", null, "", section));*/
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
              /*  if (Field_Type.equals("dg")) {

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


    public void sectionForm(String Sections_Intent,LinearLayout formLayout){//,int Sid_Intent and sid='"+Sid_Intent+"'
        String formQuery = "select * from Form_Structure where Form_Id='" + Sections_Intent + "' and Record_Status <> 'D'  ORDER BY Display_Order ASC";
        db=myDb.getWritableDatabase();
        if(LOG) Log.d("Tasdasdasdasd",formQuery);

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
                        formLayout.addView(textView(Field_Label));
                        formLayout.addView(timeeditText(Field_Label,Id, Value));
                        //formLayout.addView(editTexttime(Id, "", Value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //previousReadingDatabase = myDb.lastMultiMeterReading(AssetId,Field_Id);
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
                }*/
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
                if (Field_Type.equals("dropdownbranching")) {

                    try {
                        /*formLayout.addView(textView(Field_Label));
                        formLayout.addView(qualifiaction(Field_Options, Id, "", null, "", section));*/
                        if (parameterCount==0) {
                            formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", "",section,sid));
                        } else {
                            formLayout.addView(incidentlinearlayout(Field_Label, Field_Options, Id, "", Field_Option_Id, section,sid));
                        }
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
              /*  if (Field_Type.equals("dg")) {

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

    public void dialog_action(final ImageView sign,final int id) {

        dialog = new Dialog(IncidentForm.this);
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
                if(LOG) Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(LOG) Log.v("log_tag", "Panel Saved");
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
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                // Calling the same class
                //recreate();
            }
        });
        dialog.show();
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
                if (!setText.equals("getData")) {
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
    private EditText editTextFixed(final int mandatory, String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setLayoutParams(textLayout);
        editText.setText(setText);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);
        editText.setEnabled(false);
        editTextList.add(editText);
        return editText;
    }

    private LinearLayout radiolinearlayout(String field_Label,String field_option,int id,String setText,String Field_Option_Id) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(radiogroup(field_option, id, setText, radioLayout, Field_Option_Id));
        return radioLayout;
    }

    private EditText editTextMeter(final int mandatory, String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);
        editText.setText(setText);
        editText.setLayoutParams(textLayout);
      /*  if(Completed.equals("Completed")){
            editText.setEnabled(false);
        }*/
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint(name);
        editTextListMeter.add(editText);
        return editText;
    }
    private EditText editTextDateTime(final int Id, final String Value,String SetText) {
        final EditText editText = new EditText(this);
        editText.setId(Id);

        editText.setLayoutParams(textLayout);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setKeyListener(null);
        if(Incident==1){
            editText.setEnabled(false);
        }
        editText.setText(SetText);
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

    private EditText editTextDate(final int Id, final String Value,String SetText) {
        final EditText editText = new EditText(this);
        editText.setId(Id);

        editText.setLayoutParams(textLayout);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);
        editText.setKeyListener(null);
        if(Incident==1){
            editText.setEnabled(false);
        }
        editText.setText(SetText);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Value.equals("D"))
                    datePickerDate(editText);
            }
        });
        editTextDateTimeList.add(editText);
        return editText;
    }

    private LinearLayout timeeditText(String field_Label,int id,String setText) {
        LinearLayout radioLayout = new LinearLayout(getApplicationContext());
        radioLayout.setOrientation(LinearLayout.VERTICAL);
        radioLayout.setLayoutParams(textLayout);
        radioLayout.addView(textView(field_Label));
        radioLayout.addView(editTexttime(id));

        radioLayout.addView(radiogroupTime(id));
        return radioLayout;
    }


    private EditText editTexttime(final int Id) {
        final EditText editText = new EditText(this);
        editText.setId(Id);

        editText.setLayoutParams(textLayout);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        //editText.setFocusable(false);
        //editText.setKeyListener(null);
        if(Incident==1){
            editText.setEnabled(false);
        }

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //radiogroupTime(editText);
              /*  Log.d("asdasdasdasdasd", Value + " ");
                if (!Value.equals("D"))
                    timePickertime(editText);*/
            }
        });


        editTextTimeList.add(editText);
        return editText;
    }


    private RadioGroup radiogroupTime( int id) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        //radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);
        String optionRadio = "Hours,Minutes,Seconds";
        radioGroup.setId(id);
        String[] optionRadioList = optionRadio.split(",");
        for (int i = 0; i < optionRadioList.length; i++) {
            radioGroup.addView(radioButtonBranching(optionRadioList[i], i));
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(selectedId);
                /*String value = editText.getText().toString();
                editText.setText(value +" "+radioButton.getText().toString());*/


            }
        });
        radioGroupTimeList.add(radioGroup);
        return radioGroup;
    }

    private RadioButton radioButtonBranching(String strvalue, int SelectedId) {   //965

        final RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(SelectedId);
        radiobuttonTimeList.add(radioButton);
        return radioButton;
    }



    private EditText editTextincidentNumber(String name, final int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(textLayout);
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(2);
        editText.setMaxLines(5);
        if(Incident==1){
            editText.setText(setText);
            editText.setEnabled(false);
        }else {
            editText.setEnabled(false);
            Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            String date = simpleDateFormat.format(calendar.getTime());
            editText.setText(date+"PS-INC SyncNumber");
        }


        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);

        editTextareaList.add(editText);
       /* editText.setClickable(true);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataDownload().execute(myDb.getfieldId(id),String.valueOf(id));
            }
        });*/
        return editText;
    }

    private EditText editTextarea(String name, final int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(textLayout);
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(2);
        editText.setMaxLines(5);
        if(Incident==1){
            editText.setEnabled(false);
        }
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);
        editText.setText(setText);
        editTextareaList.add(editText);
       /* editText.setClickable(true);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DataDownload().execute(myDb.getfieldId(id),String.valueOf(id));
            }
        });*/
        return editText;
    }
    private EditText editTextRemark(String name, int id, String setText) {
        final EditText editText = new EditText(this);
        editText.setId(id);

        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLayoutParams(textLayout);
        if (FormType.equalsIgnoreCase("Meter")) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        editText.setSingleLine(false);
        editText.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setLines(3);
        editText.setMaxLines(5);
        if(Incident==1){
            editText.setEnabled(false);
        }
        editText.setVerticalScrollBarEnabled(true);
        editText.setMovementMethod(ScrollingMovementMethod.getInstance());
        editText.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editText.setHint(name);
        editText.setText(setText);
        editTextRemarkList.add(editText);
        return editText;
    }
    private Spinner qualifiaction(String options, int sp_id, String qval,LinearLayout linearLayout, final String Field_Option_Id,final String Section_Id) {
        Spinner qualifiactionSpinner = new Spinner(this);
        qualifiactionSpinner.setId(sp_id);
        String[] optionList = options.split(",");
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("-- Select One --");

        final TextView textView = new TextView(IncidentForm.this);
        textView.setText("[ Warning ]");
        textView.setTextColor(Color.RED);
        textView.setTextSize(9);
        if(Incident==1){
            qualifiactionSpinner.setEnabled(false);
        }
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
    private RadioGroup radiogroup(String optionRadio, int id, String setText, final LinearLayout linearLayout,final String Field_Option_Id) {
        final RadioGroup radioGroup = new RadioGroup(getApplicationContext());
        radioGroup.setId(id);
        radioGroup.setOrientation(RadioGroup.HORIZONTAL);
        radioGroup.setLayoutParams(textLayout);

        String[] optionRadioList = optionRadio.split(",");
        final TextView textView = new TextView(IncidentForm.this);
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
        if(Incident==1){
            radioGroup.setEnabled(false);
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
    private RadioButton radioButton(String strvalue, int SelectedId, String matchvalue) {   //965

        RadioButton radioButton = new RadioButton(this);
        radioButton.setText(strvalue);
        radioButton.setId(SelectedId);

        if (matchvalue.equals(strvalue)) {
            radioButton.setChecked(true);
        }
        if(Incident==1){
            radioButton.setEnabled(false);
        }
        textRadioButtonList.add(radioButton);
        return radioButton;
    }
    private void datePickerDate(final EditText editText){
        // Get Current Date
        try {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Dialog,new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                    //date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //*************Call Time Picker Here ********************
                    editText.setText(date_time);
                    //timePicker(editText);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        } catch (Exception e) {
            Log.d("ua611 ", "ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua611", Toast.LENGTH_SHORT).show();
        }
    }

    private void datePicker(final EditText editText){
        // Get Current Date
        try {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,android.R.style.Theme_Holo_Dialog,new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                    //date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    date_time = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    //*************Call Time Picker Here ********************
                    timePicker(editText);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            datePickerDialog.show();
        } catch (Exception e) {
            Log.d("ua611 ", "ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua611", Toast.LENGTH_SHORT).show();
        }
    }

    private void timePickertime(final EditText editText){
        try {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Dialog,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            editText.setText(hourOfDay + ":" + minute + ":00");

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            timePickerDialog.show();

        } catch (Exception e) {
            Log.d("ua635 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua635", Toast.LENGTH_SHORT).show();
        }
    }
    private void timePicker(final EditText editText){
        try {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Dialog,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            editText.setText(date_time + " " + hourOfDay + ":" + minute + ":00");

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            timePickerDialog.show();

        } catch (Exception e) {
            Log.d("ua635 ","ERROR==" + e);
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error code: ua635", Toast.LENGTH_SHORT).show();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap drawImage = photo.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(drawImage); //bmp is the bitmap to dwaw into
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(10);
            paint.setTextAlign(Paint.Align.CENTER);
            String printText = AssetCode + "" + new applicationClass().yymmddhhmm();
            paint.setAntiAlias(true);
            Log.d("DateAssetCode",printText);
            canvas.drawText(AssetCode, 55, 10, paint);
            canvas.drawText(new applicationClass().yymmddhhmm(),45 , 20, paint);

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
            String printText = AssetCode + " " + new applicationClass().yymmddhhmm();
            canvas.drawText(AssetCode, 55, 10, paint);
            canvas.drawText(new applicationClass().yymmddhhmm(),45 , 20, paint);
            drawableBitmap.put("AssetImage",drawImage);
            assetImage.setImageBitmap(drawImage);
            assetImage.getLayoutParams().height = 500;
            assetImage.getLayoutParams().width = 500;
            AssetImages(null, Id+9, "Createform",formLayout);
            sumitbuttonShift(formLayout);

        }
        else if(requestCode == CAMERA_REQUESTAssets && resultCode == Activity.RESULT_OK) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap drawImage = photo.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(drawImage); //bmp is the bitmap to dwaw into
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setTextSize(7);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setAntiAlias(true);
            //String printText = assetCode + " " + new applicationClass().yymmddhhmm();
            canvas.drawText(AssetCode, 55, 10, paint);
            canvas.drawText(new applicationClass().yymmddhhmm(), 45, 20, paint);
            drawableBitmap.put("AssetImage", drawImage);
            AssetImages(drawImage, Id, "Createform", formLayout);
            //AssetImages(null, Id, "Createform", formLayout);
            sumitbuttonShift(formLayout);

        }
    }
    private ImageView AssetImages(Bitmap name, final int id, final String setText,LinearLayout layout) {
        byte[] image="".getBytes();
        final ImageView asset = new ImageView(this);


        layout.removeView(asset);
        asset.setId(id);
        asset.setImageResource(R.drawable.plusicon);
        layout.addView(asset);
        asset.getLayoutParams().height = 150;
        asset.getLayoutParams().width = 150;
        asset.setLayoutParams(AssetLayoutmargin(asset));

        if(name != null) {
            asset.setImageBitmap(name);
            asset.getLayoutParams().height = 500;
            asset.getLayoutParams().width = 500;
        }
        asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUESTAssets);
            }
        });

        if(imageAsset.size()>0){
            ImageView imageView = (ImageView) findViewById(Id+9);
            imageView.setVisibility(View.GONE);
            createPlusIcon(formLayout,imageAsset.size()+200);
        }
        imageAsset.add(asset);

        return asset;
    }
    private void sumitbuttonShift(LinearLayout layout){
        layout.removeView(submit);
        layout.addView(submit);


    }
    private LinearLayout.LayoutParams AssetLayoutmargin (ImageView imageView){
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        lp.setMargins(0, 10, 0, 10);
        return lp;
    }
    private ImageView createPlusIcon(LinearLayout layout,int id){
        final ImageView asset = new ImageView(this);
        layout.removeView(asset);
        asset.setId(id);
        asset.setImageResource(R.drawable.plusicon);
        layout.addView(asset);
        asset.getLayoutParams().height = 150;
        asset.getLayoutParams().width = 150;
        asset.setLayoutParams(AssetLayoutmargin(asset));

        if(imageAsset.size()>1) {
            ImageView imageView = (ImageView) findViewById(id - 1);
            imageView.setVisibility(View.GONE);
        }



        asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUESTAssets);
            }
        });
        return asset;
    }

    public class DataDownload extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... URL) {
            String Val="";
            try {
                String jsonStrTask = new HttpHandler().incidentnumber(User_Group_Id, URL[0]);
                if(!jsonStrTask.equals("")){
                    JSONObject jsonObj = new JSONObject(jsonStrTask);
                    JSONArray task = jsonObj.getJSONArray("IncidentNumber");
                    JSONObject c = task.getJSONObject(0);
                    Val=c.getString("Value")+","+URL[1];
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Val;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                if(!s.equals("")){
                    String[] Value = s.split(",");
                    for(EditText editText:editTextareaList){
                        if(editText.getId()==Integer.parseInt(Value[1])){
                            editText.setText("Va"+Value[0]);
                            editText.setEnabled(false);
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            super.onPostExecute(s);
        }
    }
    public String taskInsert(){
        String Remarks="";
        for (EditText editTextarea1 : editTextRemarkList) {
            try {
                Remarks = editTextarea1.getText().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } ContentValues contentValues1 = new ContentValues();

        if(unplanned==null){
            myDb.updatedTaskDetails(TaskId,"Completed",new applicationClass().yymmddhhmmss(),Scan_Type,User_Id,Remarks,1);
        }else {
            TaskId = UUID.randomUUID().toString();
            contentValues1.put("Auto_Id", TaskId);
            contentValues1.put("Company_Customer_Id", "");
            contentValues1.put("Site_Location_Id", SiteId);
            contentValues1.put("Activity_Frequency_Id", Frequency_Id);
            contentValues1.put("Task_Scheduled_Date", "0000-00-00 00:00:00");
            contentValues1.put("Task_Status", "Unplanned");
            contentValues1.put("Task_Start_At",new applicationClass().yymmddhhmmss());
            contentValues1.put("Assigned_To", "U");
            contentValues1.put("EndDateTime", "[ Unplanned ]");
            contentValues1.put("Asset_Name", Asset_Name);
            contentValues1.put("Asset_Id",AssetId);
            contentValues1.put("From_Id",Form_IdIntent);
            contentValues1.put("Asset_Code",AssetCode);
            contentValues1.put("Asset_Location", Asset_Location);
            contentValues1.put("Asset_Status", Asset_Status);
            contentValues1.put("Activity_Name", Activity_Name);
            contentValues1.put("Assigned_To_User_Id", User_Id);
            contentValues1.put("Assigned_To_User_Group_Id",User_Group_Id);
            contentValues1.put("Scan_Type", Scan_Type);
            contentValues1.put("Remarks", Remarks);
            contentValues1.put("Incident",1);
            contentValues1.put("UpdatedStatus", "no");
            db=myDb.getWritableDatabase();
            db.insert("Task_Details", null, contentValues1);
            db.close();

        }
        return  TaskId;
    }
    public void saveData(String uuid) {

        HashMap<String,String> editText = new HashMap<>();

        for (EditText editTextDGTimeList1 : editTextwithEditText1) {
            for (EditText editTextDGTimeList2 : editTextwithEditText2) {
                if(editTextDGTimeList1.getId()==editTextDGTimeList2.getId()){
                    TextView textView = new TextView(getApplicationContext());
                    textView.setId(editTextDGTimeList1.getId());
                    textView.setText(editTextDGTimeList1.getText().toString() + "," + editTextDGTimeList2.getText().toString());
                    textWithtext.add(textView);
                }
            }
        }



        if(!settings.getString("IncidentSpValue", null).equals(null)){
            String IncidentSpValue= settings.getString("IncidentSpValue", null);
            int IncidentSpId=settings.getInt("IncidentSpId", 0);
            try {
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("Task_Id", uuid);
                contentValues3.put("Form_Id",  myDb.getFormId(myDb.getfieldId(IncidentSpId)));
                contentValues3.put("Parameter_Id", "");
                contentValues3.put("Form_Structure_Id", myDb.getfieldId(IncidentSpId));
                contentValues3.put("Site_Location_Id", SiteId);
                contentValues3.put("Value", IncidentSpValue);
                contentValues3.put("UpdatedStatus", "no");
                db=myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues3);
                SharedPreferences.Editor editor= settings.edit();
                editor.remove("IncidentSpValue");
                editor.remove("IncidentSpId");
                editor.commit();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        for (EditText editTextList1 : editTextList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextList1.getId();
                editText.put(myDb.getfieldId(fieldId), editTextList1.getText().toString());
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Value", editTextList1.getText().toString());
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        for (EditText editTextDGTimeList1 : editTextTimeList) {
            for (RadioGroup RadioGroup2 : radioGroupTimeList) {
                if(editTextDGTimeList1.getId()==RadioGroup2.getId()){

                    int selectedId = RadioGroup2.getCheckedRadioButtonId();
                    View rb1 = RadioGroup2.findViewById(selectedId);
                    int idx = RadioGroup2.indexOfChild(rb1);
                    RadioButton radioButton = (RadioButton) RadioGroup2.getChildAt(idx);
                    String Value = "";
                    if(editTextDGTimeList1.getText().toString().equals("")||editTextDGTimeList1 == null ||radioButton== null){
                        Value = editTextDGTimeList1.getText().toString() +"";
                    }else {
                        Value = editTextDGTimeList1.getText().toString() +" "+radioButton.getText().toString();
                    }

                    Log.d("asdasdasdasdasd",editTextDGTimeList1.getText().toString() +" "+"Testing" +  Value);

                    try {
                        ContentValues contentValues1 = new ContentValues();
                        int fieldId = editTextDGTimeList1.getId();
                        //editText.put(myDb.getfieldId(fieldId), editTextDGTimeList1.getText().toString());
                        contentValues1.put("Task_Id", uuid);
                        contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                        contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                        contentValues1.put("Value", Value);
                        contentValues1.put("Site_Location_Id", SiteId);
                        contentValues1.put("Parameter_Id", "");
                        contentValues1.put("UpdatedStatus", "no");
                        db = myDb.getWritableDatabase();
                        db.insert("Data_Posting", null, contentValues1);
                        db.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                   /* TextView textView = new TextView(getApplicationContext());
                    textView.setId(editTextDGTimeList1.getId());
                    textView.setText(editTextDGTimeList1.getText().toString() + "," + editTextDGTimeList2.getText().toString());
                    textWithtext.add(textView);*/
                }
            }
        }


        for (EditText editTextSignature : editTextDateTimeList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextSignature.getId();
                editText.put(myDb.getfieldId(fieldId), editTextSignature.getText().toString());
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Value", editTextSignature.getText().toString());
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Iterator check = CheckboxListhash.entrySet().iterator();
        //String checkBocValue ="";
        StringBuilder checkBocValue = new StringBuilder();
        int FieldId = 0;
        while (check.hasNext()) {
            Map.Entry<CheckBox,Integer> pair = (Map.Entry)check.next();

            FieldId = pair.getValue();
            CheckBox checkBox = pair.getKey();

            if(checkBox.isChecked()){
                //checkBocValue = checkBox.getText().toString();
                checkBocValue.append(checkBox.getText().toString()+",");
            }


            //myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(User_Id));
            check.remove(); // avoids a ConcurrentModificationException
        }

        try {



            ContentValues contentValues1 = new ContentValues();
            //int fieldId = editTextSignature.getId();
            //editText.put(myDb.getfieldId(fieldId), editTextSignature.getText().toString());
            contentValues1.put("Task_Id", uuid);
            contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(FieldId)));
            contentValues1.put("Form_Structure_Id", myDb.getfieldId(FieldId));
            contentValues1.put("Value", checkBocValue.substring(0, checkBocValue.length() - 1));
            contentValues1.put("Site_Location_Id", SiteId);
            contentValues1.put("Parameter_Id", "");
            contentValues1.put("UpdatedStatus", "no");
            db = myDb.getWritableDatabase();
            db.insert("Data_Posting", null, contentValues1);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


        for (EditText editTextarea1 : editTextareaList) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = editTextarea1.getId();
                editText.put(myDb.getfieldId(fieldId), editTextarea1.getText().toString());
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Value", editTextarea1.getText().toString());
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues1);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (TextView textViewTextWithText : textWithtext) {
            try {
                ContentValues contentValues1 = new ContentValues();
                int fieldId = textViewTextWithText.getId();
                editText.put(myDb.getfieldId(fieldId), textViewTextWithText.getText().toString());
                contentValues1.put("Task_Id", uuid);
                contentValues1.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldId)));
                contentValues1.put("Form_Structure_Id", myDb.getfieldId(fieldId));
                contentValues1.put("Value", textViewTextWithText.getText().toString());
                contentValues1.put("Site_Location_Id", SiteId);
                contentValues1.put("Parameter_Id", "");
                contentValues1.put("UpdatedStatus", "no");
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
                editText.put(myDb.getfieldId(fieldIdRemarks), editTextRemarks.getText().toString());
                contentValuesRemarks.put("Task_Id", uuid);
                contentValuesRemarks.put("Form_Id", myDb.getFormId(myDb.getfieldId(fieldIdRemarks)));
                contentValuesRemarks.put("Form_Structure_Id", myDb.getfieldId(fieldIdRemarks));
                contentValuesRemarks.put("Site_Location_Id", SiteId);
                contentValuesRemarks.put("Value", editTextRemarks.getText().toString());
                contentValuesRemarks.put("Parameter_Id", "");
                contentValuesRemarks.put("UpdatedStatus", "no");
                db = myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValuesRemarks);
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
                    contentValues2.put("Form_Id", myDb.getFormId(myDb.getfieldId(cbid)));
                    contentValues2.put("Form_Structure_Id", myDb.getfieldId(cbid));
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


        for (Spinner textSpinner : textSpinnerList) {
            try {
                int id_sp = textSpinner.getId();
                int item_pos = textSpinner.getSelectedItemPosition();
                String Form_Structure_Id = myDb.getfieldId(id_sp);
                String sspinner = String.valueOf(textSpinner.getSelectedItem());
                ContentValues contentValues3 = new ContentValues();
                contentValues3.put("Task_Id", uuid);
                contentValues3.put("Form_Id",  myDb.getFormId(myDb.getfieldId(id_sp)));
                contentValues3.put("Parameter_Id", "");
                contentValues3.put("Form_Structure_Id", myDb.getfieldId(id_sp));
                contentValues3.put("Site_Location_Id", SiteId);
                contentValues3.put("Value", sspinner);
                contentValues3.put("UpdatedStatus", "no");
                db=myDb.getWritableDatabase();
                db.insert("Data_Posting", null, contentValues3);
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (ImageView imageView : imageAsset) {

            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            myDb.insertBitmap(bitmap, uuid, "AssetImage", myDb.Site_Location_Id(User_Id));

        }
        Iterator it = drawableBitmap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String,Bitmap> pair = (Map.Entry)it.next();

            myDb.insertBitmap(pair.getValue(), uuid,pair.getKey(),myDb.Site_Location_Id(User_Id));
            it.remove(); // avoids a ConcurrentModificationException
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(IncidentForm.this, TaskDetails.class);
        intent.putExtra("TAB","TAB2");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

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

}
