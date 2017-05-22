package com.jumarmartin.powerreport;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.jumarmartin.powerreport.R.id.capture_from_camera;
import static com.jumarmartin.powerreport.R.id.description_of_incident;
import static com.jumarmartin.powerreport.R.id.incident_type;
import static com.jumarmartin.powerreport.R.id.school_spinner;
import static com.jumarmartin.powerreport.R.id.select_image;
import static com.jumarmartin.powerreport.R.id.submit_button;
import static com.jumarmartin.powerreport.R.id.up;


public class ReportActivity extends AppCompatActivity {
    //Firebase Storage
    private StorageReference mStorage;

    //Firebase Database
    private DatabaseReference mDatabase;

    //Codes
    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int PERMS_REQUEST_CODE = 1;

    //UI Element Properties
    EditText mDescriptionIncident;
    RadioGroup mIncidentType;
    Spinner mSchool;
    Button mSelectImage;
    Button mCaptureCamera;
    Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_report);
        super.onCreate(savedInstanceState);
        addSchoolsToSpinner();

        //Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Firebase Storage

        mStorage = FirebaseStorage.getInstance().getReference();

        //THIS AREA IS FOR TESTING PURPOSES ONLY
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("message/:id");
            myRef.setValue("Hello, \n World!");
            DatabaseReference myUUID = database.getReference("UUID");
            FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            myUUID.setValue(currentFirebaseUser.getUid());
        //END TESTING AREA

        //Get UI Elements
        mDescriptionIncident = (EditText)findViewById(description_of_incident);
        mIncidentType = (RadioGroup)findViewById(incident_type);
        mSchool = (Spinner)findViewById(school_spinner);
        mSelectImage = (Button)findViewById(select_image);
        mCaptureCamera = (Button)findViewById(capture_from_camera);
        mSubmit = (Button)findViewById(submit_button);


    }

    @Override
    protected void onStart() {
        super.onStart();
        //Select from gallery
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasPermissions()) {
                    Log.d("Permissions", "We have sufficient permissions.");
                } else {
                    requestPerms();
                }

                Intent intent = new Intent(Intent.ACTION_PICK);

                intent.setType("image/*, video/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});

                startActivityForResult(intent, GALLERY_INTENT);

            }
        });

        //Capture from Camera
        mCaptureCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (hasPermissions()) {
                    Log.d("Permissions", "We have sufficient permissions.");
                } else {
                    requestPerms();
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });



    }

    @IgnoreExtraProperties
    public class Incident {

        //NOTE:  The JSON layout is School (Which school location)/Type (Drugs, Bullying, etc.)/uid (unique identifier)/media (photo or video)

        /*
        The JSON layout is
            School/ (Which school location)
                Type/ (Drugs, Bullying, etc.)
                    key/ (randomly generated)
                        UUID: (Unique Identifier)
                        Description: (text)
                        Media: (either Photo or Video)
                        timestamp: (numeric -- Epoch)
         */
        private String school;
        private String type;
        private String uid;
        private String description;
        private Uri media;
//TODO: Somehow include this into uploadData()

        public Incident() {

        }

        public Incident(String uid, String type, String description, String school, Uri media) {
            this.school = school;
            this.type = type;
            this.uid = uid;
            this.description = description;
            this.media = media;
        }
    }





    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        mSubmit.setOnClickListener(new View.OnClickListener() {

            Uri uri;

            public void uploadData() {

                StorageReference filepath = mStorage.child("media").child(uri.getLastPathSegment());

                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ReportActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ReportActivity.this, "Upload Failure", Toast.LENGTH_LONG).show();
                    }
                });
//TODO: PREVENT UPLOAD UNTIL "SUBMIT" BUTTON IS CLICKED - UPLOAD ALL DATA FROM MODEL
                //        else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                //            Uri uri = data.getData();
                //            StorageReference filepath = mStorage.child("media").child(uri.getLastPathSegment());
                //        }
                finish();
            }

            @Override
            public void onClick(View v) {
                //Select from gallery

                if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
                    uri = data.getData();

                    uploadData();

                    //Capture from Camera


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


