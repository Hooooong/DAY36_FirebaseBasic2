package com.hooooong.firebasebasic2.view.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hooooong.firebasebasic2.R;
import com.hooooong.firebasebasic2.model.User;
import com.hooooong.firebasebasic2.view.signin.SignInActivity;
import com.hooooong.firebasebasic2.view.storage.StorageActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Android Hong on 2017-11-01.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int RC_SIGN_IN = 999;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private GoogleApiClient mGoogleApiClient;

    private Toolbar toolbar;
    private EditText editEmail, editPassword;
    private SignInButton btnGoogleSignIn;
    private ProgressBar progressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initFirebaseAndGooglApi();
        initView();
    }

    private void initFirebaseAndGooglApi() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignUpActivity.this, "로그인 실패!", Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        progressBar = findViewById(R.id.progressBar);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);
        btnGoogleSignIn.setSize(SignInButton.SIZE_WIDE);

        btnGoogleSignIn.setOnClickListener(this);
    }

    /**
     * 회원가입 작업
     *
     * @param view
     */
    public void signUp(View view) {

        showProgressBar();

        // @ 하나, . 하나 필수 , 영문, 숫자, _, -
        String email = editEmail.getText().toString();
        // 특수문자 하나이상 !, @, #, $, %, ^, &, *, (, )
        // 영문, 숫자
        String password = editPassword.getText().toString();

        // validation check
        // 정규식
        if (!isValidEmail(email) || !isValidPassword(password)) {
            if (!isValidEmail(email)) {
                editEmail.setError("Email 형식이 잘못되었습니다.");
            }
            if (!isValidPassword(password)) {
                editPassword.setError("비밀번호 형식이 잘못되었습니다");
            }
            hideProgressBar();
            return;
        }

        // 파이어베이스의 인증모듈로 사용자를 생성
        mAuth.createUserWithEmailAndPassword(email, password)
                // 완료확인 리스너
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            // 이메일 유효성을 확인하기 위해 해당 이메일로 메일이 발송된다.
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.e("task.isSuccessful()", task.isSuccessful() + "");
                                            hideProgressBar();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this
                                                        , "이메일을 발송하였습니다. 확인해주세요"
                                                        , Toast.LENGTH_SHORT).show();

                                                // SingIn Page 로 이동해야 한다.
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                intent.putExtra("EMAIL", editEmail.getText().toString());
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(SignUpActivity.this
                                                        , "이메일 발송에 실패하였습니다. 다시 시도해 주세요."
                                                        , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            hideProgressBar();
                            // 동일한 이메일로 Sign UP 을 할 경우 SignUp 이 실패한다.
                            editEmail.setError("이미 존재하는 Email 입니다.");
                        }
                    }
                });
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Comment  : 정상적인 이메일 인지 검증.
     */
    public static boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    /**
     * 영문 숫자를 포함하는 8자리 비밀번호 체크
     *
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password) {
        boolean err = false;
        // 영문자와 숫자만 허용
        String regex = "^[A-Za-z0-9]{8,}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(password);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    /**
     * Appbar 메뉴 생성
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_close, menu);
        return true;
    }

    /**
     * Appbar 메뉴 선택 이벤트
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_close:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * GoogleSignIn 버튼 눌렀을 경우
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGoogleSignIn:
                googleSignin();
                break;
        }
    }

    public void googleSignin(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                showProgressBar();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Toast.makeText(this, "Google SignIn 실패!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressBar();
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            // DB 에 Token 추가
                            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                            Log.e("MSG", "token = " + refreshedToken);
                            User data = new User(user.getUid(), refreshedToken, user.getEmail());
                            // Module  추가
                            userRef.child(user.getUid()).setValue(data);

                            Intent intent = new Intent(SignUpActivity.this, StorageActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(this);
        mGoogleApiClient.disconnect();
    }

}
