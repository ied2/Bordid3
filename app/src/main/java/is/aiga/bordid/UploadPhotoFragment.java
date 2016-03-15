package is.aiga.bordid;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UploadPhotoFragment extends DialogFragment implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://bordid2.freeoda.com/PhotoUpload/Upload.php";
    public static final String UPLOAD_KEY = "image";

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    private ImageView profile_picture;
    private Button buttonUpload, buttonSave;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_upload_photo, container, false);
        getDialog().setTitle("Configure");

        // Initialize few things
        init();

        return rootView;
    }

    private void init() {
        profile_picture = (ImageView) rootView.findViewById(R.id.profile_image);
        buttonUpload = (Button) rootView.findViewById(R.id.profile_choose_photo);
        buttonSave = (Button) rootView.findViewById(R.id.profile_save);

        if(!SaveSharedPreference.getProfileImage(getActivity()).equals("")) {
            Picasso.with(getActivity()).load(SaveSharedPreference.getProfileImage(getActivity())).into(profile_picture);
        }
        buttonUpload.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_choose_photo:
                showFileChooser();
                break;
            case R.id.profile_save:
                uploadImage();
                break;
        }
    }

    // Lets pick a image from our phone
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*"); // Searches for files that are images
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // After selecting a image we display it for the user
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int RESULT_OK = -1;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                profile_picture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.d("IED", s);
                Toast.makeText(getActivity().getApplicationContext(), "Account Updated", Toast.LENGTH_LONG).show();
                ProfileActivity.profile_image.setImageBitmap(bitmap);
                SaveSharedPreference.setProfileImage(getActivity(), s);
                dismiss();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                uploadImage = SaveSharedPreference.getUserId(getActivity()) + ":" + uploadImage;
                HashMap<String,String> data = new HashMap<>();

                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data); // Send the image to the database
                return result;
            }
        }
        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos); // Compress quality to 70
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}
