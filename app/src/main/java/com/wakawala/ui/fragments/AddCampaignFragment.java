package com.wakawala.ui.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wakawala.ApplicationLoader;
import com.wakawala.R;
import com.wakawala.ui.actvities.MainActivity;
import com.wakawala.ui.actvities.SelectCategoryActivity;
import com.wakawala.ui.actvities.SelectCurrencyActivity;
import com.wakawala.ui.actvities.SelectPlaceActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.NoSuchElementException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddCampaignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCampaignFragment extends Fragment {

    public AddCampaignFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static AddCampaignFragment newInstance() {
        AddCampaignFragment fragment = new AddCampaignFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_campaign, container, false);
    }

    LinearLayout llUploadPhotos;
    Slider sliderBanner;
    Date startDateSelected;
    Date endDateSelected;
    TextView startDateText;
    TextView endDateText;
    TextView category;

    EditText title, description, goal;

    View add;
    EditText youtubeUrl;

    TextView city, country;

    public static TextView currencyFragment;

    ArrayList<String> uploadImageModels = null;

    public static String selectedCurrencyFragment = "MYR";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llUploadPhotos = view.findViewById(R.id.llUploadPhotos);
        sliderBanner = view.findViewById(R.id.sliderBanner);
        View startDate = view.findViewById(R.id.startDate);
        View endDate = view.findViewById(R.id.endDate);

        title = view.findViewById(R.id.title);

        country = view.findViewById(R.id.country);
        city = view.findViewById(R.id.city);
        description = view.findViewById(R.id.description);

        goal = view.findViewById(R.id.goal);

        youtubeUrl = view.findViewById(R.id.youtubeUrl);

        category = view.findViewById(R.id.category);

        endDateText = view.findViewById(R.id.endDateText);
        startDateText = view.findViewById(R.id.startDateText);

        add = view.findViewById(R.id.add);
        currencyFragment = view.findViewById(R.id.currency);

        currencyFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SelectCurrencyActivity.class));
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                startDateSelected = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();

                                // *** same for the format String below
                                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                                startDateText.setText(dt1.format(startDateSelected));


                            }
                        },


                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );

                dpd.setMinDate(Calendar.getInstance());
                dpd.show(getFragmentManager(), "Datepickerdialog");

            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (startDate != null) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                    endDateSelected = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                                    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                                    endDateText.setText(dt1.format(endDateSelected));

                                }
                            },
                            now.get(Calendar.YEAR), // Initial year selection
                            now.get(Calendar.MONTH), // Initial month selection
                            now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                    );
                    Calendar c = Calendar.getInstance();
                    c.setTime(startDateSelected); // Now use today date.
                    c.add(Calendar.DATE, 21); // Adding 21 days
                    dpd.setMinDate(c);
                    dpd.show(getFragmentManager(), "Datepickerdialog");

                } else
                    Toast.makeText(getContext(), "Please select start date first", Toast.LENGTH_SHORT).show();
            }
        });

        add = view.findViewById(R.id.add);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {

                    HashMap<String, Object> params = new HashMap<>();

                    params.put("title", title.getText().toString().trim());

                    params.put("titleLowercased", title.getText().toString().trim().toLowerCase());

                    params.put("description", description.getText().toString().trim());

                    params.put("goal", Double.parseDouble(goal.getText().toString().trim()));

                    params.put("category", category.getText().toString().trim());

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("campaigns").push();

                    params.put("localGoalCurrencySymbol", selectedCurrencyFragment);

                    params.put("timestamp", new Date().getTime());

                    params.put("id", ref.getKey());

                    params.put("startDate", startDateText.getText().toString().trim());

                    params.put("endDate", endDateText.getText().toString().trim());


                    params.put("userId", FirebaseAuth.getInstance().getUid());

                    params.put("country", country.getText().toString().trim());

                    params.put("city", city.getText().toString().trim());

                    params.put("moneyCollected", 0);
                    params.put("numberOfLikes", 0);
                    params.put("numberOfShares", 0);
                    params.put("numberOfVisitors", 0);

                    params.put("youtubeVideoId", youtubeUrl.getText().toString().trim());

                    ref.setValue(params)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getContext(), "Successfully created campaign", Toast.LENGTH_SHORT).show();

                                    if (FirebaseAuth.getInstance().getUid() != null)
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(FirebaseAuth.getInstance().getUid())
                                                .child("campaignsCreated")
                                                .child(ref.getKey())
                                                .child("timeStamp")
                                                .setValue(new Date().getTime());

                                    if (MainActivity.viewPager != null)
                                        MainActivity.viewPager.setCurrentItem(0);
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "Please enter all details...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), SelectPlaceActivity.class);
                    intent.putExtra("isCountry", true);
                    getActivity().startActivity(intent);
                    isCountry = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), SelectPlaceActivity.class);
                    intent.putExtra("isCountry", false);
                    startActivity(intent);
                    isCity = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SelectCategoryActivity.class);
                startActivity(intent);
                isAddCategory = true;
            }
        });

        llUploadPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePermissions();
            }
        });

        uploadImageModels = new ArrayList<>();
        loadSlider();
        ApplicationLoader.getInstance().setCampaignCountryId(0);
        ApplicationLoader.getInstance().setCampaignCountry("");
        ApplicationLoader.getInstance().setCampaignCityId(0);
        ApplicationLoader.getInstance().setCampaignCity("");
    }

    private boolean isCountry = false;
    private boolean isCity = false;
    private boolean isAddCategory = false;

    @Override
    public void onResume() {
        super.onResume();
        if (isAddCategory) {
            category.setText(ApplicationLoader.getInstance().getCategoryFullForm());
            isAddCategory = false;
        }
        if (isCountry) {
            country.setText(ApplicationLoader.getInstance().getCampaignCountry());
            isCountry = false;
        }
        if (isCity) {
            city.setText(ApplicationLoader.getInstance().getCampaignCity());
            isCity = false;
        }
    }

    private void checkStoragePermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((AppCompatActivity) getContext(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            switch (requestCode) {
                case 100:
                    checkStoragePermissions();
                    break;
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    uploadImageModels = new ArrayList<>();
                    if (data.getClipData() != null) {
                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            String filePath = getImageFilePath(data.getClipData().getItemAt(i).getUri());
                            Log.e("a", filePath);
                            uploadImageModels.add(filePath);
                            //scaleImage(filePath);
                        }
                        loadSlider();
                    }

                    if (data.getData() != null) {
                        String filePath = getImageFilePath(data.getData());
                        Log.e("a", filePath);
                        uploadImageModels.add(filePath);
                        //scaleImage(filePath);
                        loadSlider();
                    }
                    break;
            }
        }
    }

    private void scaleImage(String filePath) throws NoSuchElementException {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        int width = bitmap.getWidth();

        int height = bitmap.getHeight();
        int bounding = dpToPx(250);
        Log.e("Test", "original width = " + Integer.toString(width));
        Log.e("Test", "original height = " + Integer.toString(height));
        Log.e("Test", "bounding = " + Integer.toString(bounding));

        if (width < 1200) {
            return;
        }

        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Log.e("Test", "xScale = " + Float.toString(xScale));
        Log.e("Test", "yScale = " + Float.toString(yScale));
        Log.e("Test", "scale = " + Float.toString(scale));

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        width = scaledBitmap.getWidth(); // re-use
        height = scaledBitmap.getHeight(); // re-use
        BitmapDrawable result = new BitmapDrawable(scaledBitmap);
        Log.e("Test", "scaled width = " + Integer.toString(width));
        Log.e("Test", "scaled height = " + Integer.toString(height));

        Log.e("Test", "done");
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public String getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];

        Cursor cursor = getContext().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        String imagePath = "";
        if (cursor != null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return imagePath;
    }

    private boolean check() {
        return category.getText().toString().trim().length() != 0 && endDateText.getText().toString().trim().length() != 0 && startDateText.getText().toString().trim().length() != 0 && FirebaseAuth.getInstance().getUid() != null && city.getText().toString().trim().length() != 0 && country.getText().toString().trim().length() != 0 && title.getText().toString().trim().length() != 0 && description.getText().toString().trim().length() != 0 && goal.getText().toString().trim().length() != 0;
    }

    void loadSlider() {
        if (uploadImageModels.size() > 0) {
            sliderBanner.setAdapter(new MainSliderAdapter());
            sliderBanner.setInterval(7000);
            sliderBanner.setLoopSlides(true);
            sliderBanner.setVisibility(View.VISIBLE);
            llUploadPhotos.setVisibility(View.GONE);
        } else {
            sliderBanner.setVisibility(View.GONE);
            llUploadPhotos.setVisibility(View.VISIBLE);
        }
    }

    public class MainSliderAdapter extends SliderAdapter {

        @Override
        public int getItemCount() {
            return uploadImageModels.size();
        }

        @Override
        public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {

            viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            Glide.with(getActivity())
                    .load(uploadImageModels.get(position))
                    .apply(new RequestOptions().error(R.drawable.background_splash_screen)
                            .placeholder(R.drawable.background_splash_screen).fitCenter().dontAnimate())
                    .into(viewHolder.imageView);
        }
    }
}
