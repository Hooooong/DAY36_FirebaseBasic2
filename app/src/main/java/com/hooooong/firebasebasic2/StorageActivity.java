package com.hooooong.firebasebasic2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hooooong.firebasebasic2.model.User;
import com.hooooong.firebasebasic2.util.RealPathUtil;

import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity implements UserAdapter.Callback {

    private FirebaseDatabase database;
    private DatabaseReference userRef;

    private StorageReference mStorageRef;

    private TextView textId, textToken;
    private RecyclerView recyclerView;
    private Button btnSend;
    private EditText editText;
    private ImageView imageView;

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        initView();
        initAdapter();
        load();
    }

    private void initView() {
        textId = findViewById(R.id.textId);
        textToken = findViewById(R.id.textToken);
        recyclerView = findViewById(R.id.recyclerView);
        btnSend = findViewById(R.id.btnSend);
        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
    }


    private void initAdapter() {
        userAdapter =  new UserAdapter(this);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void load() {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> userList = new ArrayList<>();
                User user = null;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    user = new User();
                    user.setToken((String)snapshot.getValue());
                    user.setId(snapshot.getKey());
                    userList.add(user);
                }

                userAdapter.modifyUserData(userList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fileLoad(String fileName){
        FirebaseStorage.getInstance().getReference().child("files/"+fileName).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(getBaseContext())
                        .load(uri)
                        .into(imageView);
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }




    /**
     * File Explore 호출
     */
    public void chooseFile(View view) {

        // 다중 선택 이미지
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);*/

        // File 탐색기 호출
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 갤러리 호출 (image/*)
        // 비디오 호출 (video/*)
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select App"), 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 999) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                upload(uri);
            }
        }/*else if (requestCode == 1){
            if(resultCode == RESULT_OK){

                ClipData clipData = data.getClipData();
                Log.e("StorageActivity", clipData.getItemCount() + "");

            }
        }*/
    }

    /**
     * 파일 업로드
     */
    public void upload(Uri uri) {
        // RealPath 를 가져오는 메소드
        String realPath = RealPathUtil.getRealPath(this, uri);
        String temp[] = realPath.split("/");
        final String fileName = temp[temp.length - 1];
        // FireBase Storage File Node
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("files/"+fileName);

        riversRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // FIle Upload 를 했을 경우
                        // 바로 Download Uri 를 가져올 수 있다.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        Glide.with(getBaseContext())
                                .load(downloadUrl)
                                .into(imageView);
                        
                        // File Download
                        //fileLoad(fileName);
                        Log.e("StorageActivity", downloadUrl.getPath());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(StorageActivity.this, "파일 업로드 실패!!" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void setUser(String id, String token) {
        textId.setText(id);
        textToken.setText(token);
    }
}
