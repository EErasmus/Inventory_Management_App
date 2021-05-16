package com.example.inventorymanagementapp;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentInventory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentInventory extends Fragment {

    StorageReference mStorageRef;

    FirebaseDatabase rootNode;
    DatabaseReference refs;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentInventory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentInventory.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentInventory newInstance(String param1, String param2) {
        FragmentInventory fragment = new FragmentInventory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    ListView mListView;

    int[] images = {R.drawable.ic_thumbnail, R.drawable.ic_thumbnail};

    ArrayList<String> keys;
    UserHelper currentUser;
    boolean shouldDel = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        rootNode = FirebaseDatabase.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        currentUser = Utility.getAuthenticatedUser();
        int index = 0;
//        currentUser.inventory.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//            keys.add(entry.getKey().toString());
//        });
        keys = new ArrayList<String>(currentUser.inventory.keySet());

        mListView = view.findViewById(R.id.listView_Inv);

        CustomAdapter customAdapter = new CustomAdapter();
        mListView.setAdapter(customAdapter);

        // Inflate the layout for this fragment

        return view;
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return keys.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.customlayout, null);

            ImageView mImageView = view.findViewById(R.id.imageView4);
            TextView mTextView = view.findViewById(R.id.textView_Inv);

            //mImageView.setImageResource(images1.get(position));
            InventoryHelper item = currentUser.inventory.get(keys.get(position));
            item.id = keys.get(position);
            mTextView.setText(item.name);

            //imgViewer.setImageURI(imgUri);
            String imageFileExt = item.id.split("_").length > 1 ?
                    item.id.split("_")[1] : "";
            if (imageFileExt != "") {
//                // update code testing //
//                item.name = item.name + "update test";
//                rootNode.getReference("users/"+Utility.getAuthenticatedUser().contactNumber+"/inventory")
//                .child(item.id).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//                        if(task.isSuccessful()){
//
//                        }
//                    }
//                });
//                // update code testing ends successfully

                // delete code testing
//                if(shouldDel) {
//                    shouldDel = false;
//                    rootNode.getReference("users/"+Utility.getAuthenticatedUser().contactNumber+"/inventory")
//                .child(item.id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull @NotNull Task<Void> task) {
//                        if(task.isSuccessful()){
//
//
//                        }
//                        else {
//                            String msg = task.getException().getMessage();
//                        }
//                    }
//                });
//                }
//
//                // delete testing ends successfully


                mStorageRef.child(item.id + "." + imageFileExt).
                        getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
//                        mImageView.setImageURI(uri);
                        try {
                            mImageView.setImageURI(uri);
                            mImageView.setImageBitmap(BitmapFactory.decodeStream(new URL(uri.toString()).openConnection().getInputStream()));
                        } catch (Exception e) {
                            String msg = e.getMessage();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        String message = item.id+"." + imageFileExt + e.getMessage();
                    }
                });
            }

            return view;
        }
    }
}