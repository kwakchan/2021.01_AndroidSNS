package com.example.androidsns.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.androidsns.MemberInfo;
import com.example.androidsns.R;
import com.example.androidsns.WriteInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class WritePostActivity extends BasicActivity{
    private static final String TAG = "WritePostActivity";
    private FirebaseUser user;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private RelativeLayout buttonsBackgroundLayout;
    private ImageView selectedImageView;
    private int pathCount ;
    private int successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        parent = findViewById(R.id.contentsLayout);
        buttonsBackgroundLayout = findViewById(R.id.buttonsBackgroundLayout);

        buttonsBackgroundLayout.setOnClickListener(onClickListener);
        findViewById(R.id.check).setOnClickListener(onClickListener);
        findViewById(R.id.image).setOnClickListener(onClickListener);
        findViewById(R.id.video).setOnClickListener(onClickListener);
        findViewById(R.id.imageModify).setOnClickListener(onClickListener);
        findViewById(R.id.videoModify).setOnClickListener(onClickListener);
        findViewById(R.id.delete).setOnClickListener(onClickListener);
    }

    @Override
    public void onActivityResult(int requestCODE, int resultCode, Intent data) {
        super.onActivityResult(requestCODE, resultCode, data);
        switch (requestCODE){
            case 0:
                if(resultCode == Activity.RESULT_OK){
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath); // 이미지 경로를 담은 리스트

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    LinearLayout linearLayout = new LinearLayout(WritePostActivity.this);
                    linearLayout.setLayoutParams(layoutParams);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    parent.addView(linearLayout);

                    // 갤러리에서 이미지 추가
                    ImageView imageView = new ImageView(WritePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buttonsBackgroundLayout.setVisibility(View.VISIBLE);
                            selectedImageView = (ImageView) v;
                        }
                    });
                    Glide.with(this).load(profilePath).override(1000).into(imageView);
                    linearLayout.addView(imageView);
                    
                    // EditText 추가
                    EditText editText = new EditText(WritePostActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    editText.setHint("내용");
                    linearLayout.addView(editText);
                }
                break;
            case 1:
                if(resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).override(1000).into(selectedImageView);
                }
                break;
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.check:
                    storageUpload();
                    break;
                case R.id.image:
                    myStartActivity(GalleryActivity.class, "image", 0);
                    break;
                case R.id.video:
                    myStartActivity(GalleryActivity.class, "video", 0);
                    break;
                case R.id.buttonsBackgroundLayout:
                    if(buttonsBackgroundLayout.getVisibility() == View.VISIBLE){
                        buttonsBackgroundLayout.setVisibility(View.GONE);
                    }
                case R.id.imageModify:
                     myStartActivity(GalleryActivity.class, "image", 1);
                     buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
                case R.id.videoModify:
                     myStartActivity(GalleryActivity.class, "video", 1);
                     buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
                case R.id.delete:
                      parent.removeView((View)selectedImageView.getParent());
                      buttonsBackgroundLayout.setVisibility(View.GONE);
                    break;
            }

        }
    };

    private void storageUpload(){
        final String title = ((EditText)findViewById(R.id.titleEditText)).getText().toString();

        if(title.length()>0){
            ArrayList<String> contentList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("cities").document();

            for(int i=0; i<parent.getChildCount(); i++){
                View view = parent.getChildAt(i);
                if(view instanceof EditText){
                    String text = ((EditText)view).getText().toString();
                    if(text.length()>0){
                        contentList.add(text);
                    }
                } else {
                    contentList.add(pathList.get(pathCount));
                    final StorageReference mountainImagesRef = storageRef.child("posts/" + documentReference.getId() +"/" +pathCount+ ".jpg");
                    try{
                        InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", ""+(contentList.size()-1)).build();
                        UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));

                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Log.e("로그: ", "uri: " +uri);
                                        contentList.set(index, uri.toString());
                                        successCount++;
                                        if(pathList.size() == successCount){
                                            // 완료
                                            WriteInfo writeInfo = new WriteInfo(title, contentList, user.getUid(), new Date());
                                            storeUpload(documentReference, writeInfo);
                                            for(int a=0; a<contentList.size(); a++){
                                                Log.e("로그: ", "콘텐츠: " +contentList.get(a) );
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }catch (FileNotFoundException e){
                        Log.e("로그:", "에러: " + e.toString() );
                    }
                    pathCount++;
                }
            }
        } else {
            startToast("회원 정보를 입력해주세요");
        }

    }

    private void storeUpload(DocumentReference documentReference, WriteInfo writeInfo){
        documentReference.set(writeInfo)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c, String media, int requestCode){
        Intent intent = new Intent(this, c);
        intent.putExtra("media", media);
        startActivityForResult(intent, requestCode);
    }


}
