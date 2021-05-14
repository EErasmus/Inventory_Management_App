package com.example.inventorymanagementapp;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

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

    ArrayList<String> names = new ArrayList<String >();
    ArrayList<String> images1 = new ArrayList<String >();
    String[] Names = {"Test Item 1", "Test Item 2"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        UserHelper currentUser = Utility.getAuthenticatedUser();
        int index = 0;
        currentUser.inventory.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
            names.add(entry.getValue().name);
            images1.add(entry.getKey().toString());
        });

        mListView = view.findViewById(R.id.listView_Inv);

        CustomAdapter customAdapter = new CustomAdapter();
        mListView.setAdapter(customAdapter);

        // Inflate the layout for this fragment

        return view;
    }

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return images1.size();
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
            mTextView.setText(names.get(position));

            //imgViewer.setImageURI(imgUri);


//            mStorageRef.child("uploads/" + images1.get(position)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    mImageView.setImageURI (uri.toString());
//                }
//            });

            return view;
        }
    }
}