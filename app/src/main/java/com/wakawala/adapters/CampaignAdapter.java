package com.wakawala.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.wakawala.R;
import com.wakawala.databinding.RowCampaignBinding;
import com.wakawala.interfaces.RecyclerItemClickInterface;
import com.wakawala.model.BackgroundImageModel;
import com.wakawala.model.Campaign;
import com.wakawala.model.CampaignModel;
import com.wakawala.model.ProfileImageModel;
import com.wakawala.ui.actvities.ChartActivity;
import com.wakawala.ui.actvities.DetailCampaignActivity;
import com.wakawala.ui.fragments.PopularFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CampaignAdapter extends RecyclerView.Adapter<CampaignAdapter.CampaignHolder> {
    private RecyclerItemClickInterface<Object> clickInterface;
    public ArrayList<CampaignModel> mCampaignList;
    public boolean isProfileScreen = false;
    public Set<String> userFavorited = new HashSet<>();

    public CampaignAdapter(RecyclerItemClickInterface<Object> clickInterface, ArrayList<CampaignModel> mCampaignList) {
        this.clickInterface = clickInterface;
        this.mCampaignList = mCampaignList;

    }

    public CampaignAdapter() {
        mCampaignList = new ArrayList<>();
    }

    public CampaignAdapter(boolean isProfileScreen) {
        this.isProfileScreen = isProfileScreen;
        mCampaignList = new ArrayList<>();
    }


    @NonNull
    @Override
    public CampaignHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RowCampaignBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.row_campaign, parent, false);
        return new CampaignHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final CampaignHolder campaignHolder, final int pos) {
        if (campaignHolder.binding != null) {


            if (isProfileScreen)
                campaignHolder.binding.ivStar.setVisibility(View.GONE);
            else
                campaignHolder.binding.ivStar.setVisibility(View.VISIBLE);
            campaignHolder.binding.ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(campaignHolder.itemView.getContext(), campaignHolder.binding.ivMore);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.options_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.report:
                                    //handle menu1 click
                                    return true;

                                default:
                                    return false;
                            }
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
            campaignHolder.binding.ivChart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ChartActivity.newInstance(campaignHolder.binding.ivChart.getContext());
                }
            });
            campaignHolder.binding.setCampaign(mCampaignList.get(pos));
            campaignHolder.binding.setProfileImage(new ProfileImageModel(mCampaignList.get(pos).getUserProfileURL()));
            if (mCampaignList.get(pos).getCampaign().getUploadedImages() != null && mCampaignList.get(pos).getCampaign().getUploadedImages().size() != 0)
                campaignHolder.binding.setBackgroundImage(new BackgroundImageModel(mCampaignList.get(pos).getCampaign().getUploadedImages().get(0)));

            campaignHolder.binding.ivCampaignImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickInterface != null)
                        clickInterface.onRecyclerItemClick(null, pos);
                }
            });


            campaignHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DetailCampaignActivity.newInstance(campaignHolder.itemView.getContext(), mCampaignList.get(pos));
                }
            });
            campaignHolder.binding.ivCampaignImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //DetailCampaignActivity.newInstance(campaignHolder.itemView.getContext(), mCampaignList.get(pos));
                }
            });
            Log.v("liketest", "onBindCalled");

            campaignHolder.binding.ivStar.setImageDrawable(ContextCompat.getDrawable(campaignHolder.itemView.getContext(), R.drawable.ic_star));


            FirebaseDatabase
                    .getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getUid() != null ? FirebaseAuth.getInstance().getUid() : "nouser")
                    .child("campaignFavourited")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            userFavorited = new HashSet<>();
                            try {
                                for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                                    if (d1 != null) {
                                        userFavorited.add(d1.getKey());
                                    }
                                }
                                Log.v("liketest", new Gson().toJson(userFavorited));

                                if (userFavorited != null && userFavorited.contains(mCampaignList.get(pos).getCampaign().getId()))
                                    campaignHolder.binding.ivStar.setImageDrawable(ContextCompat.getDrawable(campaignHolder.itemView.getContext(), R.drawable.ic_star_selected));
                                else
                                    campaignHolder.binding.ivStar.setImageDrawable(ContextCompat.getDrawable(campaignHolder.itemView.getContext(), R.drawable.ic_star));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

            campaignHolder.binding.ivStar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (FirebaseAuth.getInstance().getUid() != null) {
                        final DatabaseReference ref = FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(FirebaseAuth.getInstance().getUid()).child("campaignFavourited");

                        FirebaseDatabase
                                .getInstance().getReference("users")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child("campaignFavourited")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        userFavorited = new HashSet<>();
                                        try {
                                            for (DataSnapshot d1 : dataSnapshot.getChildren()) {
                                                if (d1 != null) {
                                                    userFavorited.add(d1.getKey());
                                                }
                                            }
                                            Log.v("liketest", new Gson().toJson(userFavorited));

                                            if (userFavorited != null && userFavorited.contains(mCampaignList.get(pos).getCampaign().getId())) {

                                                Log.v("usertest", "found");
                                                ref.child(mCampaignList.get(pos).getCampaign().getId()).child("timestamp").removeValue();
                                                campaignHolder.binding.ivStar.setImageDrawable(ContextCompat.getDrawable(campaignHolder.itemView.getContext(), R.drawable.ic_star));

                                            } else {

                                                Log.v("usertest", "not found");
                                                campaignHolder.binding.ivStar.setImageDrawable(ContextCompat.getDrawable(campaignHolder.itemView.getContext(), R.drawable.ic_star_selected));

                                                ref.child(mCampaignList.get(pos).getCampaign().getId()).child("timestamp").setValue(new Date().getTime());
                                            }
//                                        notifyDataSetChanged();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                    } else
                        Toast.makeText(campaignHolder.itemView.getContext(), "Please Login", Toast.LENGTH_SHORT).show();
                }
            });
        }
        /*LinearGradient test = new LinearGradient(0.f, 0.f, 300.f, 0.0f,
                new int[] { 0xFF21354D, 0xFF24A7A0, 0xFF00C997},
                null, Shader.TileMode.CLAMP);
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setShader(test);

        campaignHolder.binding.seekbarFont.setProgressDrawable( (Drawable)shape );*/
//        campaignHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mCampaignList.size();
    }

    class CampaignHolder extends RecyclerView.ViewHolder {
        private final RowCampaignBinding binding;

        CampaignHolder(RowCampaignBinding binding) {
            super(binding.getRoot());
            this.binding = DataBindingUtil.bind(binding.getRoot());
        }
    }
}
