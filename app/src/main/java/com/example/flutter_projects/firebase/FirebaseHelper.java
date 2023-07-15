package com.example.flutter_projects.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class FirebaseHelper {
    //https://firebase.google.com/docs/firestore/query-data/get-data
    //https://firebase.google.com/docs/firestore/manage-data/add-data#java_4
    static FirebaseFirestore db;
    static User userInfo=new User("name","abc@gmail.com","90909090","hjkjkkjh");

    public static FirebaseAuth firebaseAuth;
    private static final String TAG = "Firebase Test";
    public static FirebaseUser user;

    public static void init() {
        // Write a message to the database
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static int logout() {
        try {
            FirebaseAuth.getInstance().signOut();
            return 1;
        } catch (Exception e) {
            Log.e("logout error", e.toString());
            return 0;
        }
    }


    public static void updateUserInfo(String uid, String mobile) {
        db.collection("users").document(uid)
                .update("mobile", mobile)
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

    public static User getUserInfo(Context context, String uid) {
        DocumentReference docRef = db.collection("users").document(uid);
        if (uid == "null") {
            Toast.makeText(context, "user id is null", Toast.LENGTH_LONG);
            return userInfo;
        } else {
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    Log.d(TAG, "DocumentSnapshot 2: " + task);
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            userInfo = document.toObject(User.class);
                            Log.d(TAG, "userInfo data: " + userInfo.getMobile());
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
            return userInfo;
        }
    }
    // single node data
//        DatabaseReference myRef2 = database.getReference("CopyWrite");
//        myRef2.setValue("I am Saboor Khan");

    // multiple nested client
//        DatabaseReference myRef3 = database.getReference("contacts");
//        String contactId = myRef3.push().getKey();
//        myRef3.child(contactId).setValue(new ContactModel("hasan", "8878789"));

    // Read from the database
//        myRef2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//            }
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });


//        myRef3.child(contactId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                ContactModel value = dataSnapshot.getValue(ContactModel.class);
//                Log.d(TAG, "Value is: " + value.name);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });


//        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(@NonNull Task<String> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("Token Detail", "Task Not Successfull");
//                    return;
//                }
//                String token = task.getResult();
//                Log.e("Token Details", "Token is " + token);
//            }
//        });
    // Create a new user with a first and last name
//        Map<String, Object> user1 = new HashMap<>();
//        user1.put("first", "Ada");
//        user1.put("last", "Lovelace");
//        user1.put("born", 1815);
//        addFirebaseDocument(user1);
//
//        Map<String, Object> user2 = new HashMap<>();
//        user2.put("first", "Saboor");
//        user2.put("last", "Khan");
//        user2.put("born", 1989);
//        addFirebaseDocument(user2);

//        readFirebaseDocuments();

    private void addFirebaseDocument(Map<String, Object> user) {
        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void readFirebaseDocuments() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
