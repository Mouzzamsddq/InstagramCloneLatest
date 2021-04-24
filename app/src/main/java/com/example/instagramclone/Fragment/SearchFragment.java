package com.example.instagramclone.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.instagramclone.Adapter.UserAdapter;
import com.example.instagramclone.Model.User;
import com.example.instagramclone.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    ImageView backImageView;
    private RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    EditText searchBar;
    ImageView searchImageView;
    /* access modifiers changed from: private */
    public UserAdapter userAdapter;
    /* access modifiers changed from: private */
    public List<User> userList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setHasFixedSize(true);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.userList = new ArrayList();
        this.searchBar = (EditText) view.findViewById(R.id.searchText);
        this.searchImageView = (ImageView) view.findViewById(R.id.searchImageView);
        this.backImageView = (ImageView) view.findViewById(R.id.backButton);
        this.relativeLayout = (RelativeLayout) view.findViewById(R.id.rlLayout);
        UserAdapter userAdapter2 = new UserAdapter(getContext(), this.userList);
        this.userAdapter = userAdapter2;
        this.recyclerView.setAdapter(userAdapter2);
        this.searchImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SearchFragment.this.backImageView.setVisibility(0);
                SearchFragment.this.searchImageView.setVisibility(8);
                SearchFragment.this.searchBar.requestFocus();
                ((InputMethodManager) SearchFragment.this.getContext().getSystemService("input_method")).toggleSoftInputFromWindow(SearchFragment.this.relativeLayout.getApplicationWindowToken(), 2, 0);
            }
        });
        this.backImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SearchFragment.this.backImageView.setVisibility(8);
                SearchFragment.this.searchImageView.setVisibility(0);
                SearchFragment.hideKeyboardFrom(SearchFragment.this.getContext(), SearchFragment.this.relativeLayout);
            }
        });
        readUsers();
        return view;
    }

    private void readUsers() {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                if (SearchFragment.this.searchBar.getText().toString().equals("")) {
                    SearchFragment.this.userList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        SearchFragment.this.userList.add((User) dataSnapshot.getValue(User.class));
                    }
                    SearchFragment.this.userAdapter.notifyDataSetChanged();
                }
            }

            public void onCancelled(DatabaseError error) {
            }
        });
    }

    public static void hideKeyboardFrom(Context context, View view) {
        ((InputMethodManager) context.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
