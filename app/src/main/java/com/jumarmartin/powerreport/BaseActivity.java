package com.jumarmartin.powerreport;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.jumarmartin.powerreport.R.string.your_uid_is;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        //Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Literally Floating Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reportView = new Intent(BaseActivity.this, ReportActivity.class);
                startActivity(reportView);
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        signInAnonymously();
        uidViewSetUID();
    }


    private void uidViewSetUID() {
        TextView uidView = (TextView) findViewById(R.id.textView);
        String uidText;
        uidText = this.getText(your_uid_is) + " " + mAuth.getCurrentUser().getUid();
        uidView.setText(uidText);
    }

   private void signInAnonymously() {



           mAuth.signInAnonymously()
                   .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               Log.d(TAG, "signInAnonymously:success");
                           } else {
                               Log.w(TAG, "signInAnonymously:failure", task.getException());
                               Toast.makeText(BaseActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });

   }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_base, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

 }
