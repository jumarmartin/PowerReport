package com.jumarmartin.powerreport;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

import static com.jumarmartin.powerreport.R.id.capture_from_camera;
import static com.jumarmartin.powerreport.R.id.description_of_incident;
import static com.jumarmartin.powerreport.R.id.incident_type;
import static com.jumarmartin.powerreport.R.id.incident_type_bullying;
import static com.jumarmartin.powerreport.R.id.incident_type_drugs;
import static com.jumarmartin.powerreport.R.id.incident_type_fighting;
import static com.jumarmartin.powerreport.R.id.incident_type_other;
import static com.jumarmartin.powerreport.R.id.incident_type_power;
import static com.jumarmartin.powerreport.R.id.school_spinner;
import static com.jumarmartin.powerreport.R.id.select_image;
import static com.jumarmartin.powerreport.R.id.select_school;
import static com.jumarmartin.powerreport.R.id.submit_button;
import static com.jumarmartin.powerreport.R.string.upload_failure;
import static com.jumarmartin.powerreport.R.string.upload_successful;


public class ReportActivity extends AppCompatActivity {

//    Code Declarations
    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PERMS_REQUEST_CODE = 1;
//    UI Element Properties
    EditText mDescriptionIncident;
    RadioGroup mIncidentType;
    Spinner mSchool;
    Button mSelectImage;
    Button mCaptureCamera;
    Button mSubmit;
    TextView mSchoolbro;
    //      Array List
    ArrayList<String> pathArray;
    int array_position;
    String schoolSelected;
    //    Firebase Authentication Declaration
    private FirebaseAuth mAuth;
    //    Firebase Storage Declarations
    private StorageReference mStorage;
    //    Firebase Database Declarations
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_report);
        super.onCreate(savedInstanceState);
        addSchoolsToSpinner();

//        Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference("School");

//        Firebase Storage

        mStorage = FirebaseStorage.getInstance().getReference();

//        Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

//        Get UI Elements
        mDescriptionIncident = (EditText)findViewById(description_of_incident);
        mIncidentType = (RadioGroup)findViewById(incident_type);
        mSchool = (Spinner)findViewById(school_spinner);
        mSelectImage = (Button)findViewById(select_image);
        mCaptureCamera = (Button)findViewById(capture_from_camera);
        mSubmit = (Button)findViewById(submit_button);
        mSchoolbro = (TextView)findViewById(select_school);

//      PathArray Dec.
        pathArray = new ArrayList<>();





    }

    @Override
    protected void onStart() {
        super.onStart();

        if (hasPermissions()) {
            Log.d("Permissions", "We have sufficient permissions.");
        } else {
            requestPerms();
        }

        //Select from gallery
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*, video/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});

                startActivityForResult(intent, GALLERY_INTENT);

            }
        });

//        Capture from Camera
        mCaptureCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });


        selectedSchool();



    }

        public String selectedSchool() {

            mSchool.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override


                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    switch (position){
                        case 0:
                            schoolSelected = "Ardrey";
                            break;
                        case 1:
                            schoolSelected = "Butler";
                            break;
                        case 3:
                            schoolSelected = "Cato";
                            break;
                        case 4:
                            schoolSelected = "EEG";
                            break;
                        case 5:
                            schoolSelected = "eLearning";
                            break;
                        case 6:
                            schoolSelected = "Garinger";
                            break;
                        case 7:
                            schoolSelected = "Harding";
                            break;
                        case 8:
                            schoolSelected = "Harper";
                            break;
                        case 9:
                            schoolSelected = "Hawthorne";
                            break;
                        case 10:
                            schoolSelected = "Hopewell";
                            break;
                        case 11:
                            schoolSelected = "Cochrane";
                            break;
                        case 12:
                            schoolSelected = "Independence";
                            break;
                        case 13:
                            schoolSelected = "Levine";
                            break;
                        case 14:
                            schoolSelected = "Mallard";
                            break;
                        case 15:
                            schoolSelected = "Marie";
                            break;
                        case 16:
                            schoolSelected = "Myers";
                            break;
                        case 17:
                            schoolSelected = "North";
                            break;
                        case 18:
                            schoolSelected = "Northwest";
                            break;
                        case 19:
                            schoolSelected = "Olympic";
                            break;
                        case 20:
                            schoolSelected = "Performance";
                            break;
                        case 21:
                            schoolSelected = "Berry";
                            break;
                        case 22:
                            schoolSelected = "Providence";
                            break;
                        case 23:
                            schoolSelected = "Rocky";
                            break;
                        case 24:
                            schoolSelected = "South";
                            break;
                        case 25:
                            schoolSelected = "WestCLT";
                            break;
                        case 26:
                            schoolSelected = "WestMeck";
                            break;
                        case 27:
                            schoolSelected = "Hough";
                            break;
                        case 28:
                            schoolSelected = "Vance";
                            break;

                        default:
                            schoolSelected = null;
                            break;

                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        return  schoolSelected;
        }

    public String getSchoolSelected() {
        return schoolSelected;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);





        mSubmit.setOnClickListener(new View.OnClickListener() {

            Uri uri;



            private void uploadData() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = user.getUid();

                StorageReference filepath = mStorage.child("media").child(uid).child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        @SuppressWarnings("VisibleForTests") String downloadUri = taskSnapshot.getDownloadUrl().toString();

                        Toast.makeText(ReportActivity.this, upload_successful, Toast.LENGTH_LONG).show();
                        String typeOfIncident = "";

                        if (mIncidentType.getCheckedRadioButtonId() == incident_type_bullying) {
                            typeOfIncident = "Bullying";
                        }else if (mIncidentType.getCheckedRadioButtonId() == incident_type_fighting) {
                            typeOfIncident = "Fighting";
                        }else if (mIncidentType.getCheckedRadioButtonId() == incident_type_drugs){
                            typeOfIncident = "Drugs";
                        }else if (mIncidentType.getCheckedRadioButtonId() == incident_type_power){
                            typeOfIncident = "Abuse of Power";
                        }else if (mIncidentType.getCheckedRadioButtonId() == incident_type_other){
                            typeOfIncident = "Other";
                        }

                        mDescriptionIncident.getText();

                        String description = mDescriptionIncident.getText().toString().trim();
                        HashMap<String, Object> dataMap = new HashMap<>();
                        dataMap.put("UID", uid);
                        dataMap.put("description", description);
                        dataMap.put("Media", downloadUri);

                        dataMap.put("timeStamp", ServerValue.TIMESTAMP);
                        getSchoolSelected();


                        if (typeOfIncident == null){
                            Toast.makeText(ReportActivity.this, "Please select an Incident", Toast.LENGTH_SHORT).show();
                        }else if(schoolSelected == null) {

                           Toast.makeText(ReportActivity.this, "Please select a school!", Toast.LENGTH_SHORT).show();
                        } else if(description.length() < 0) {

                            Toast.makeText(ReportActivity.this, "Please enter a description!", Toast.LENGTH_SHORT).show();
                        } else{

                            mDatabase.child(schoolSelected).child("incident_type").child(typeOfIncident).push().setValue(dataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ReportActivity.this, upload_successful, Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ReportActivity.this, upload_failure, Toast.LENGTH_LONG).show();
                                }
                            });

                            finish();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReportActivity.this, upload_failure, Toast.LENGTH_LONG).show();
                    }
                });


            }


            @Override
            public void onClick(View v) {
                //Select from gallery

                if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
                    uri = data.getData();

                    uploadData();

                    //Capture from Camera


                }else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                    uri = data.getData();

                    uploadData();
                }



            }

        });




    }


    private void addSchoolsToSpinner() {
        Spinner spinner = (Spinner) findViewById(school_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.school_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }


    //request perms

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case PERMS_REQUEST_CODE:
                for (int res :grantResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;

            default:
                allowed = false;
        }

        if (allowed) {
            Log.d("wow", "onRequestPermissionsResult: yay");
        }
    }
    private boolean hasPermissions() {
        int res;

        String[] permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET };

        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (res == PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    private void requestPerms() {
        String[] permissions = new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA, android.Manifest.permission.INTERNET };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, PERMS_REQUEST_CODE);
        }
    }


}