Android Programing
----------------------------------------------------
### 2017.10.31 29일차

#### 예제
____________________________________________________

#### 공부정리
____________________________________________________

##### __Firebase Authetication__

- Firebase Authetication 란?

  > Firebase는 간편하고 안전한 방법으로 사용자를 관리한다. Firebase 인증은 Email&Password, 타사 제공업체(예: Google, Facebook, Twitter, GitHub 등), 기존 계정 시스템 직접 사용 등의 다양한 인증 방법을 제공한다. 인터페이스를 직접 만들 수도 있고, 완벽하게 맞춤설정 가능한 오픈소스 UI를 활용할 수도 있다.

- Firebase Authetication 사용 방법 (Email & Password)

  ![Auth Setting](https://github.com/Hooooong/DAY36_FirebaseBasic2/blob/master/image/AuthSetting.PNG)

  - 프로젝트를 생성한 후, 위와 같이 인증에 대한 로그인 방법을 설정해야 한다.

  - Gradle 설정 : [Authetication](https://firebase.google.com/docs/auth/android/start/?authuser=0)

  - 기본적인 선언 ( `FirebaseAuth` 사용 )

  ```java
  // Firebase 인증 모듈 선언
  private FirebaseAuth mAuth;
  // FireabaseAuth 사용
  mAuth = FirebaseAuth.getInstance();
  ```

  - SignUp ( `createUserWithEmailAndPassword` 메소드 사용 )

  ```java
  // 사용자 등록
   public void signup(View view) {

     // 입력한 Email 을 가져온다.
     String email = editEmail.getText().toString();
     // 입력한 Password 를 가져온다.
     String password = editPassword.getText().toString();

     // 파이어베이스의 인증모듈로 사용자를 생성
     FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
             // 완료확인 리스너
             .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                 @Override
                 public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()) {
                        // User 생성 완료되었으면 실행
                        // User 생성이 완료되면
                        // mAuth.getCurrentUser() 에 마지막으로 Sign Np 한 정보가 들어가 있다.
                     } else {
                       // User 생성 실패하였으면 실행
                     }
                 }
             });
   }
  ```

  ![SignUp](https://github.com/Hooooong/DAY36_FirebaseBasic2/blob/master/image/signup.PNG)

  - Email 인증 메일 보내기 ( 회원가입 직후 실행, `sendEmailVerification` 메소드 사용 )

  ```java
  final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
  // 이메일 유효성을 확인하기 위해 해당 이메일로 메일이 발송된다.
  user.sendEmailVerification()
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                  // Email 발송이 성공하였으면 실행
                } else {
                  // Email 발송이 실패하였으면 실행
                }
            }
        });
  ```

  - SignIn ( `signInWithEmailAndPassword` 메소드 사용)

  ```java
  // 사용자 로그인
  public void signin(View view) {
    String email = signEmail.getText().toString();
    String password = signPassword.getText().toString();
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                      // SignIn 에 성공했으면
                      // Email 검증이 안되었어도
                      // signInWithEmailAndPassword 은 Complete 를 호출하기 때문에
                      // 추가적인 검증이 필요하다.

                        // Email 검증
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user.isEmailVerified()) {
                            // Email 검증이 되었으면 실행
                        } else {
                            // Email 검증이 안되었으면 실행
                        }
                    } else {
                      // SignIn 에 실패했으면 실행
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
  }
  ```

  - Logout ( `signOut` 메소드 사용 )

  ```java
  FirebaseAuth.getInstance().signOut();
  ```

##### __Firebase Storage__

- Firebase Storage 란?

  > 개발자들이 쉽고 안전하게 파일들을 업로드하고 다운로드 할 수 있는 Firebase Cloud 저장소를 제공해준다. Cloud Storage용 Firebase SDK는 네트워크 품질과 관계없이 Firebase 앱의 파일 업로드 및 다운로드에 Google 보안을 적용한다.

- Firebase Storage 사용 방법

  - Gradle 설정 : [Storage](https://firebase.google.com/docs/storage/android/start/?authuser=0)

  - 기본적인 선언 ( `FirebaseStorage` 사용 )

  ```java
  // Firebase Storage 모듈 선언
  private FirebaseStorage mStorageRef;
  // FirebaseStorage 사용
  mStorageRef = FirebaseStorage.getInstance().getReference();
  ```

  - File Upload ( `putFile` 메소드 사용, `putStream`, `putByte` 메소드도 있음 )

  ```java
  // Firebase Storage File Node
  // Firebase Realtime Database 와 동일하다.
  // Node 형식으로 폴더구조를 참조할 수 있다.

  // StorageReference 를 지정해줘야 한다.
  // child(fileName) 을 설정해주거나,
  // child(경로).child(filename) 을 설정해줘야 한다.
  StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("files/" + fileName);

  riversRef.putFile(File Uri)
          .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // 파일 업로드가 성공했으면
              }
          })
          .addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception exception) {
                // 파일 업로드가 실패하였으면
              }
          });
  ```

  ![File Upload](https://github.com/Hooooong/DAY36_FirebaseBasic2/blob/master/image/fileupload.PNG)

  - File Download( `getDownloadUrl` 메소드 사용)

  ```java
  // File Download URL 가져오는 방법
  // 1. File Upload 를 하고, 성공했을 경우
  //    taskSnapshot.getDownloadUrl() 를 호출하면 가져와진다.
  //
  // 2. 직접적으로 File Node 를 탐색하여 가져오는 경우
  //    getDownloadUrl() 메소드를 사용하여 rui 를 가져온다.
  private void fileLoad(String fileName){
      FirebaseStorage.getInstance().getReference().child("files/"+fileName).getDownloadUrl()
              .addOnSuccessListener(new OnSuccessListener<Uri>() {
          @Override
          public void onSuccess(Uri uri) {
              // "files/"+fileName" 에 대한 uri 를 가져온다.
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception exception) {
          }
      });
  }
  ```
