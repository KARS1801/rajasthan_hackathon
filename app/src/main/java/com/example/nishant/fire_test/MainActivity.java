package com.example.nishant.fire_test;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

   private FirebaseDatabase mfirebaseDatabase;
   private FirebaseAuth mfirebaseAuth;
   private DatabaseReference mdatabaseReference;
   private FirebaseAuth.AuthStateListener mauthStateListener;
   private static final int RC_SIGN_IN =1;
   private ChildEventListener mchildEventListener;
   private TextView mUserId;
   private Button add_item_button;
   private EditText item_name_edit_text;
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private List<ListItem> listItemList;
    private RecyclerAdapter mRecyclerAdapter;
    private int counter=1;
    private ListView mCartListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add_item_button=(Button)findViewById(R.id.add_item_button);
        item_name_edit_text=(EditText)findViewById(R.id.item_name_edit_text);
        mCartListView=(ListView)findViewById(R.id.cart_item_list);
        mUserId=(TextView)findViewById(R.id.mUser_Id);

        final List<ListItem> listItemList = new ArrayList<>();
        mRecyclerAdapter = new RecyclerAdapter(this, R.layout.item_list, listItemList);
        mCartListView.setAdapter(mRecyclerAdapter);


        mfirebaseDatabase=FirebaseDatabase.getInstance();
        mfirebaseAuth=FirebaseAuth.getInstance();
        mdatabaseReference=mfirebaseDatabase.getReference().child("ITEMS_ADDED");

        item_name_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    add_item_button.setEnabled(true);
                } else {
                    add_item_button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        item_name_edit_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});


        mauthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mfirebaseAuth.getCurrentUser();
                if(user!=null){
                    user.getDisplayName();
                      onSignedIn();
                    mUserId.setText(mdatabaseReference.child("ITEMS_ADDED").getKey());
                    Toast.makeText(MainActivity.this,"Signed_In",Toast.LENGTH_SHORT).show();

                } else{
                         onSignedOut();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(
                                            Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            if(resultCode==RESULT_OK){
                Toast.makeText(MainActivity.this,"SIGNED_IN",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode==RESULT_CANCELED) {
                Toast.makeText(MainActivity.this,"SIGNED_CANCELLED",Toast.LENGTH_SHORT).show();
            }
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:{
                AuthUI.getInstance().signOut(this);
                return true;
            }

            default:
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume(){
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mauthStateListener);

    }

    @Override
    protected void onPause(){
        super.onPause();
        if(mauthStateListener!=null)
        mfirebaseAuth.removeAuthStateListener(mauthStateListener);
        detachDatabaseReadListener();
        mRecyclerAdapter.clear();
    }

    private void onSignedIn(){

       attachDatabaseReadListener();
    }
    private void onSignedOut(){
       detachDatabaseReadListener();
       mRecyclerAdapter.clear();
    }

    private void attachDatabaseReadListener(){
        if(mchildEventListener==null){
            mchildEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    ListItem listItem=dataSnapshot.child("ITEMS_ADDED").getValue(ListItem.class);
                    mRecyclerAdapter.add(listItem);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mdatabaseReference.addChildEventListener(mchildEventListener);
        }
    }

    private void detachDatabaseReadListener(){
        if(mchildEventListener!=null)
        mdatabaseReference.removeEventListener(mchildEventListener);
        mchildEventListener=null;
    }

    public void onClick(View view) {
        if (view.getId() == R.id.add_item_button) {
            add_item_button = (Button) findViewById(R.id.add_item_button);
            item_name_edit_text = (EditText) findViewById(R.id.item_name_edit_text);
            mUserId=(TextView)findViewById(R.id.mUser_Id);
            String item_name,count;
            item_name = item_name_edit_text.getText().toString();
            counter++;
            count=Integer.toString(counter);
            Toast.makeText(MainActivity.this, item_name + "has been added to cart", Toast.LENGTH_SHORT).show();
            ListItem listItem=new ListItem(item_name_edit_text.getText().toString(),count,mUserId.toString());
            mdatabaseReference.push().setValue(listItem);
            item_name_edit_text.setText("");
        }

    }
}
