package com.wakawala.ui.actvities;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.wakawala.R;
import com.wakawala.model.CurrencyModel;
import com.wakawala.ui.fragments.AddCampaignFragment;
import com.wakawala.util.Util;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class SelectCurrencyActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_currency);
        setTitle("Select Currency");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_keyboard_arrow_left_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase.getInstance()
                .getReference("currency")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try {
                            List<CurrencyModel> data = new ArrayList<>();
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                                data.add(dataSnapshot1.getValue(CurrencyModel.class));

                            recyclerView.setAdapter(new SelectCurrencyAdapter(data));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    class SelectCurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyAdapter.SelectCurrencyViewHolder> {

        List<CurrencyModel> data;

        public SelectCurrencyAdapter(List<CurrencyModel> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public SelectCurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SelectCurrencyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.currency_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SelectCurrencyViewHolder holder, int position) {
            try {

                CurrencyModel current = data.get(position);
                if (current != null) {
                    holder.name.setText(current.name);
                    Picasso.with(SelectCurrencyActivity.this)
                            .load(current.flag)
                            .into(holder.countryFlag);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Util.saveLocally(SelectCurrencyActivity.this, "currency", current.id);

                            AddCampaignFragment.selectedCurrencyFragment = current.id;
                            if (AddCampaignFragment.currencyFragment != null)
                                AddCampaignFragment.currencyFragment.setText(current.id);

                            finish();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return data != null ? data.size() : 0;
        }

        class SelectCurrencyViewHolder extends RecyclerView.ViewHolder {

            TextView name;

            ImageView countryFlag;

            public SelectCurrencyViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                countryFlag = itemView.findViewById(R.id.flag);
            }
        }
    }
}
