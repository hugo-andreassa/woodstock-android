package com.hyperdrive.woodstock.ui.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.hyperdrive.woodstock.BuildConfig;
import com.hyperdrive.woodstock.R;
import com.hyperdrive.woodstock.api.config.RetrofitConfig;
import com.hyperdrive.woodstock.api.services.ProjectService;
import com.hyperdrive.woodstock.models.ProjectModel;
import com.hyperdrive.woodstock.persistence.SharedPreferencesUtil;
import com.hyperdrive.woodstock.utils.LoadImage;
import com.hyperdrive.woodstock.utils.SnackbarUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectActionFragment extends Fragment {

    private String TAG = "PROJECT_ACTION_FRAGEMENT";

    private final String SERVER_ERROR = "Erro na comunicação com o servidor";

    private final String BAD_REQUEST_UPDATE = "Erro ao atualizar os dados do Projeto";
    private final String OK_REQUEST_UPDATE = "Projeto atualizado com sucesso";
    private final String BAD_REQUEST_INSERT = "Erro ao inserir os dados do Projeto";
    private final String OK_REQUEST_INSERT = "Projeto inserido com sucesso";

    private static final String ARG_PARAM1 = "budgetItemId";
    private static final String ARG_PARAM2 = "project";

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ProjectModel mProject;
    private Long mBudgetItemId;

    /* PHOTO FILE VARIABLES */
    private String mCurrentPhotoPath;
    private Uri mPhotoURI;

    private ImageView image;
    private ImageButton cameraButton;
    private TextInputEditText comment;

    private ProgressDialog progressDialog;
    private SharedPreferencesUtil sharedPreferences;

    public ProjectActionFragment() {

    }

    public static ProjectActionFragment newInstance(Long budgetItemId, ProjectModel project) {
        ProjectActionFragment fragment = new ProjectActionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, budgetItemId);
        args.putSerializable(ARG_PARAM2, project);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBudgetItemId = getArguments().getLong(ARG_PARAM1);
            mProject = (ProjectModel) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_project_action, container, false);

        sharedPreferences = new SharedPreferencesUtil(getContext());
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Carregando...");

        setupCameraButton(view);
        setupEditTexts(view);
        setupSaveButton(view);
        if(mProject != null) {
            loadFieldsInformation(view);
            setupDeleteButton(view);
        }

        return view;
    }

    private void setupDeleteButton(View view) {
        Button button = view.findViewById(R.id.project_delete_button);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(v -> {
            deleteProject(view);
        });
    }

    private void loadFieldsInformation(View view) {
        comment.setText(mProject.getComment());
        LoadImage loadImage = new LoadImage(image, getContext());
        loadImage.execute(mProject.getUrl());
        image.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(image.getTag().toString()), "image/*");
            startActivity(intent);
        });
    }

    private void setupSaveButton(View view) {
        Button saveButton = view.findViewById(R.id.project_save_button);
        saveButton.setOnClickListener(v -> {
            ProjectModel project = getValuesFromFields();

            if(project != null) {
                progressDialog.show();

                if(mProject != null) {
                    project.setId(mProject.getId());
                    updateProjectInApi(project, view);
                } else {
                    insertProjectInAPi(project, view);
                }
            }
        });
    }

    private ProjectModel getValuesFromFields() {

        if(validateFields()) {
            ProjectModel project = new ProjectModel();
            project.setBudgetItemId(mBudgetItemId);
            project.setComment(comment.getText().toString());
            project.setUrl("");

            return project;
        }

        return null;
    }

    private boolean validateFields() {

        if (image.getDrawable() == null) {
            SnackbarUtil.showError(getActivity(), "Você precisa " +
                    "adicionar uma imagem para poder salvar");

            return false;
        }

        return true;
    }

    private void setupEditTexts(View view) {
        comment = view.findViewById(R.id.project_comment);
    }

    private void clearFields() {
        comment.setText("");
        image.setImageDrawable(null);
    }

    private void insertProjectInAPi(ProjectModel project, View view) {
        String auth = sharedPreferences.getAuthentication();

        int length = RetrofitConfig.getRetrofitInstance().baseUrl()
                .toString().concat("projects/").length();

        ProjectService projectService = RetrofitConfig.getRetrofitInstance().create(ProjectService.class);
        Call<Void> call = projectService.insert(project, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Long id = Long.parseLong(response.headers().get("Location").substring(length));
                    uploadFile(id, auth, OK_REQUEST_INSERT);
                    clearFields();
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_INSERT);
                    Log.e(TAG, project.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
            }
        });
    }

    private void updateProjectInApi(ProjectModel project, View view) {
        String auth = sharedPreferences.getAuthentication();

        ProjectService projectService = RetrofitConfig.getRetrofitInstance().create(ProjectService.class);
        Call<Void> call = projectService.update(project.getId(), project, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    if(mPhotoURI != null) {
                        uploadFile(project.getId(), auth, OK_REQUEST_UPDATE);
                    } else {
                        ProjectActivity.updateRecyclerView();
                        SnackbarUtil.showSuccess(getActivity(), OK_REQUEST_UPDATE);
                    }
                } else {
                    SnackbarUtil.showError(getActivity(), BAD_REQUEST_UPDATE);
                    Log.e(TAG, project.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
            }
        });
    }

    private void uploadFile(Long id, String auth, String msg) {

        Log.e("Upload", "ok");
        Log.e("Upload", mCurrentPhotoPath);
        File file = new File(mCurrentPhotoPath);
        file = saveBitmapToFile(file);

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(
                                getActivity().getContentResolver().getType(mPhotoURI)),
                                file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Log.e("Upload", "ok");

        ProjectService projectService = RetrofitConfig.getRetrofitInstance().create(ProjectService.class);
        Call<Void> call = projectService.uploadImage(id, body, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    ProjectActivity.updateRecyclerView();
                    SnackbarUtil.showSuccess(getActivity(), msg);
                } else {
                    SnackbarUtil.showError(getActivity(), "Erro ao fazer upload da foto para os servidor");
                    Log.e("Upload", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();

                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e("Upload", t.getMessage());
            }
        });
    }

    private void deleteProject(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Deseja mesmo excluir este Projeto?")
                .setPositiveButton("Sim", (dialog, id) -> {
                    deleteProjectFromApi(mProject.getId());
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.dismiss();
                });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void deleteProjectFromApi(Long id) {
        String auth = sharedPreferences.getAuthentication();

        ProjectService projectService = RetrofitConfig.getRetrofitInstance().create(ProjectService.class);
        Call<Void> call = projectService.delete(id, auth);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()) {
                    ProjectActivity.updateRecyclerView();
                    getActivity().getSupportFragmentManager().popBackStackImmediate();
                } else {
                    SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                    Log.e(TAG, response.toString());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                SnackbarUtil.showError(getActivity(), SERVER_ERROR);
                Log.e(TAG, id.toString());
            }
        });
    }

    /* PICTURE METHODS */
    private void setupCameraButton(View view) {
        ImageButton cameraButton = view.findViewById(R.id.project_camera_button);
        image = view.findViewById(R.id.project_image);

        cameraButton.setOnClickListener((v) -> {
            callTakePictureIntent();
            Log.e(TAG, mPhotoURI.getPath());
        });
    }

    private void callTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage());
            }

            if (photoFile != null) {
                mPhotoURI = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID +".provider",
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK)
        {
            rotateImage(mCurrentPhotoPath);

            try {
                Bitmap photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), mPhotoURI);
                image.setImageBitmap(photo);
                image.setOnClickListener(v -> {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(mCurrentPhotoPath), "image/*");
                    startActivity(intent);
                });
            } catch (IOException e) {
                Log.e(TAG, "Erro ao exibir a foto");
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public File saveBitmapToFile(File file){
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;

            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

    public void rotateImage(String path) {

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_ROTATE_180 + "");
            Log.e(TAG, "RODOU");
        }

        try {
            exifInterface.saveAttributes();
        } catch (IOException e) {
            Log.e(TAG, "Erro ao salvar Exif");
            e.printStackTrace();
        }
    }
}