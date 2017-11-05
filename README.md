Android Programing
----------------------------------------------------
### 2017.10.31~11.01 29, 30일차

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
                        // mAuth.getCurrentUser() 에 마지막으로 Sign Up 한 정보가 들어가 있다.
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
  //    getDownloadUrl() 메소드를 사용하여 uri 를 가져온다.
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

##### __Firebase Cloud Messaging__

- Firebase Cloud Messaging 란?

  > Firebase 클라우드 메시징(FCM)은 무료로 메시지를 안정적으로 전송할 수 있는 교차 플랫폼 메시징 솔루션이다. FCM을 사용하면 새 이메일이나 기타 데이터를 동기화할 수 있음을 클라이언트 앱에 알릴 수 있고, 알림 메시지를 전송하여 사용자를 유지하고 재참여를 유도할 수 있다. 채팅 메시지와 같은 사용 사례에서는 메시지로 최대 4KB의 페이로드를 클라이언트 앱에 전송할 수 있다.

- Firebase Cloud Messaging 기본 설정

  - Gradle 설정 : [FCM](https://firebase.google.com/docs/cloud-messaging/android/client?hl=ko)

      - Gradle 만 설정해줘도 Firebase Console 에서 보내는 Message 를 백그라운드에서 받을 수 있다.

  - Manifast.xml 설정

      - FirebaseMessagingService 확장 서비스 추가

      > FirebaseMessagingService를 확장하는 서비스를 추가한다. 백그라운드에서 앱의 알림을 수신하는 것 외에 다른 방식으로 메시지를 처리하려는 경우에 필요하다. 포그라운드 앱의 알림 수신, 데이터 페이로드 수신, 업스트림 메시지 전송 등을 수행하려면 이 서비스를 확장해야 한다.

      ```xml
      <service
          android:name=".MyFirebaseMessagingService">
          <intent-filter>
              <action android:name="com.google.firebase.MESSAGING_EVENT"/>
          </intent-filter>
      </service>
      ```

      - FirebaseInstanceIdService 확장 서비스 추가

      > 등록 토큰 생성, 순환, 업데이트를 처리하기 위해 FirebaseInstanceIdService를 확장하는 서비스를 추가한다. 특정 기기로 전송하거나 기기 그룹을 만드는 경우에 필요하다.

      ```xml
      <service
          android:name=".MyFirebaseInstanceIDService">
          <intent-filter>
              <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
          </intent-filter>
      </service>
      ```

  - Token 생성

      > 특정 기기로 메시지를 보내려면 기기의 등록 토큰을 알아야 한다. 앱을 처음 시작할 때 FCM SDK에서 클라이언트 앱 인스턴스용 등록 토큰을 생성한다. 단일 기기를 타겟팅하거나 기기 그룹을 만들려면 FirebaseInstanceIdService를 확장하여 이 토큰에 액세스해야 한다. 토큰은 최초 시작 후에 회전될 수 있으므로 마지막으로 업데이트된 등록 토큰을 검색하는 것이 좋다.

      - 앱에서 인스턴스 ID 삭제, 새 기기에서 앱 복원, 사용자가 앱 삭제/재설치, 사용자가 앱 데이터 소거 할 때 Token 값이 변경 될 수 있다.

      ```java
      // 기기별로 고유한 Token 을 얻을 수 있다.
      String refreshedToken = FirebaseInstanceId.getInstance().getToken();
      ```

  - Service 확장하기

      - MyFirebaseMessagingService ( Token 이 새로 생성될 때 마다 `onTokenRefresh` 메소드가 실행 )

      ```java
      public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

          private static final String TAG = "IDService";

          // Token 이 새로 생성될 때 마다 onTokenRefresh 가 실행
          @Override
          public void onTokenRefresh() {
              // Get updated InstanceID token.
              String refreshedToken = FirebaseInstanceId.getInstance().getToken();
              Log.d(TAG, "Refreshed token: " + refreshedToken);

              // Token 업데이트 작업
              sendRegistrationToServer(refreshedToken);
          }
          // [END refresh_token]
          private void sendRegistrationToServer(String token) {
              // TODO: 여기서 토큰 값을 갱신해야 한다.
          }
      }
      ```

      - MyFirebaseInstanceIDService ( App 이 화면에 떠 있을 때 `onMessageReceived` 메소드가 실행  )

      ```java
      public class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = "MsgService";

        /**
         * 내 앱이 화면에 현재 떠있으면 Notification이 전송되었을 때 이 함수가 호출된다.
         *
         * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
         */
        // [START receive_message]
        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {
            // TODO(developer): Handle FCM messages here.
            // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                // 여기서 notification 메세지를 받아 처리
            }

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }
        // [END receive_message]
      }
      ```

  - Firebase Cloud Messaging 메세지 보내기

    1. FCM 홈페이지에서 보내기

        ![FCM message](https://github.com/Hooooong/DAY36_FirebaseBasic2/blob/master/image/FCMmessage.PNG)

        - https://console.firebase.google.com/project/프로젝트명/notification/compose 에서 다중, 단일 메세지를 보낼 수 있다.

    2. 외부에서 보내기

        - 참조 : [FCM Server Setting](https://github.com/Hooooong/DAY37_FCM-Setting)
