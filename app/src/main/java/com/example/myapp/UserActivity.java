package com.example.myapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import android.net.Uri;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private List<String> imageUrls;
    private RecyclerView recyclerViewImages;
    private ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        recyclerViewImages = findViewById(R.id.recyclerViewImages);
        recyclerViewImages.setLayoutManager(new GridLayoutManager(this, 3)); // 3 columns in grid

        imageUrls = new ArrayList<>();
        adapter = new ImageAdapter(imageUrls, new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                String selectedImageUrl = imageUrls.get(position);
                String dateTime = extractDateTime(selectedImageUrl);
                if (!dateTime.isEmpty()) {
                    Toast.makeText(UserActivity.this, "Date and Time: " + dateTime, Toast.LENGTH_SHORT).show();
                }

                new AlertDialog.Builder(UserActivity.this)
                        .setTitle("Delete Image")
                        .setMessage("Are you sure you want to delete this image?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                deleteImage(imageUrls.get(position), position);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        recyclerViewImages.setAdapter(adapter);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference().child("unknown_faces");

        storageReference.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageUrls.add(uri.toString());
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                        Toast.makeText(UserActivity.this, "Images fetched successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(UserActivity.this, "Error fetching images", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteImage(String imageUrl, final int position) {
        StorageReference imageRef = firebaseStorage.getReferenceFromUrl(imageUrl);

        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                imageUrls.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(UserActivity.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(UserActivity.this, "Error in deleting image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String extractDateTime(String imageUrl) {
        try {
            String decodedUrl = java.net.URLDecoder.decode(imageUrl, "UTF-8");
            String[] urlParts = decodedUrl.split("/");
            String filename = urlParts[urlParts.length - 1];

            int jpgIndex = filename.indexOf(".jpg");
            if (jpgIndex != -1) {
                String dateTimePart = filename.substring(0, jpgIndex);
                String[] parts = dateTimePart.split("_");
                if (parts.length > 1) {
                    String dateTime = parts[1].trim();
                    String[] dateTimeSplit = dateTime.split(" ");
                    if (dateTimeSplit.length >= 6) {
                        String formattedDate = String.join("/", dateTimeSplit[0], dateTimeSplit[1], dateTimeSplit[2]);
                        String formattedTime = String.join(":", dateTimeSplit[3], dateTimeSplit[4], dateTimeSplit[5]);
                        return formattedDate + "  " + formattedTime;
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(UserActivity.this, "Error processing image URL", Toast.LENGTH_SHORT).show();
        }
        return "";
    }
}
