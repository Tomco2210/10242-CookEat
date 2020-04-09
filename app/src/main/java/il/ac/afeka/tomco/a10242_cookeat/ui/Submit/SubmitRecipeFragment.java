package il.ac.afeka.tomco.a10242_cookeat.ui.Submit;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.Adapters.SubmitEquipmentRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.SubmitIngredientRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Adapters.SubmitInstructionRecyclerViewAdapter;
import il.ac.afeka.tomco.a10242_cookeat.Models.CategoryModel;
import il.ac.afeka.tomco.a10242_cookeat.Network.Constants;
import il.ac.afeka.tomco.a10242_cookeat.R;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.CategoriesRepository;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.SubmitRecipeRepository;

import static il.ac.afeka.tomco.a10242_cookeat.Network.Constants.READ_WRITE_EXTERNAL_STORAGE_FOR_GALLERY;


public class SubmitRecipeFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "SubmitRecipeFragment";
    private SubmitRecipeRepository submitRecipeRepository = new SubmitRecipeRepository();

    private ImageView recipeImageIV;
    private EditText recipeNameET;
    private EditText ingredientET;
    private EditText equipmentET;
    private EditText instructionET;
    private EditText readyInTimeET;
    private Spinner cuisineSpinner;
    private RecyclerView ingredientRV;
    private RecyclerView equipmentRV;
    private RecyclerView instructionsRV;
    private ProgressBar mProgressBar;

    private Uri uri;
    private static String currentPhotoPath;
    private String cuisineId;

    private List<String> ingredients = new ArrayList<String>();
    private SubmitIngredientRecyclerViewAdapter submitIngredientRecyclerViewAdapter;

    private List<String> instructions = new ArrayList<String>();
    private SubmitInstructionRecyclerViewAdapter submitInstructionRecyclerViewAdapter;

    private List<String> equipment = new ArrayList<String>();
    private SubmitEquipmentRecyclerViewAdapter submitEquipmentRecyclerViewAdapter;

    private List<CategoryModel> categoryModels = new ArrayList<>();
    private static List<String> spinnerTitles = new ArrayList<>();

    StorageTask mStorageTask;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");

        View root = inflater.inflate(R.layout.fragment_recipe_submit, container, false);

        mProgressBar = root.findViewById(R.id.progressBar);
        showProgressBar(false);

        //Image Related view parts
        recipeImageIV = root.findViewById(R.id.srfRecipeImageIV);
        final Button captureImageBtn = root.findViewById(R.id.srfTakePictureIBtn);
        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    dispatchTakePictureIntent();
                else {
                    String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, READ_WRITE_EXTERNAL_STORAGE_FOR_GALLERY);
                }
            }
        });
        final Button chooseFromGalleryBtn = root.findViewById(R.id.srfSelectImageIBtn);
        chooseFromGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //general details
        recipeNameET = root.findViewById(R.id.srfRecipeNameET);
        readyInTimeET = root.findViewById(R.id.srfReadyInTimeET);
        cuisineSpinner = root.findViewById(R.id.srfCuisineSP);
        categoryModels.addAll(CategoriesRepository.getCategories());
        categoryModels.forEach(category -> spinnerTitles.add(category.getTitle()));
        cuisineSpinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerTitles));
        cuisineSpinner.setOnItemSelectedListener(this);

        //ingredient list view, ingredient details and button
        ingredientRV = root.findViewById(R.id.IngredientsLV);
        submitIngredientRecyclerViewAdapter = new SubmitIngredientRecyclerViewAdapter(getContext(), ingredients);
        ingredientRV.setAdapter(submitIngredientRecyclerViewAdapter);
        ingredientRV.setLayoutManager(new LinearLayoutManager(this.getContext()));


        ingredientET = root.findViewById(R.id.ingredientET);
        final Button addIngredientBtn = root.findViewById(R.id.srfAddIngredientBtn);
        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingredientET.getText().length() != 0) {
                    ingredients.add(ingredientET.getText().toString());
                    ingredientET.setText("");
                    submitIngredientRecyclerViewAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getActivity(), "Please Enter Ingredient", Toast.LENGTH_SHORT).show();
            }
        });
        //equipment list view, equipment details and button
        equipmentRV = root.findViewById(R.id.EquipmentLV);
        submitEquipmentRecyclerViewAdapter = new SubmitEquipmentRecyclerViewAdapter(getContext(), equipment);
        equipmentRV.setAdapter(submitEquipmentRecyclerViewAdapter);
        equipmentRV.setLayoutManager(new LinearLayoutManager(this.getContext()));


        equipmentET = root.findViewById(R.id.equipmentET);
        final Button addEquipmentBtn = root.findViewById(R.id.srfAddEquipmentBtn);
        addEquipmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (equipmentET.getText().length() != 0) {
                    equipment.add(equipmentET.getText().toString());
                    equipmentET.setText("");
                    submitEquipmentRecyclerViewAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getActivity(), "Please Enter Equipment", Toast.LENGTH_SHORT).show();
            }
        });

        //instructions list view, instructions details and button
        instructionsRV = root.findViewById(R.id.InstructionsLV);
        submitInstructionRecyclerViewAdapter = new SubmitInstructionRecyclerViewAdapter(getContext(), instructions);
        instructionsRV.setAdapter(submitInstructionRecyclerViewAdapter);
        instructionsRV.setLayoutManager(new LinearLayoutManager(this.getContext()));

        instructionET = root.findViewById(R.id.instructionInstructionET);
        final Button addInstructionBtn = root.findViewById(R.id.srfAddInstructionBtn);
        addInstructionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (instructionET.getText().length() != 0) {
                    instructions.add(instructionET.getText().toString());
                    instructionET.setText("");
                    submitInstructionRecyclerViewAdapter.notifyDataSetChanged();
                } else
                    Toast.makeText(getActivity(), "Please Enter Instructions", Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fab = getActivity().findViewById(R.id.fab_main);
        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStorageTask == null) {
                    sendRecipe();
                } else
                    Toast.makeText(getActivity(), "Upload in Progress", Toast.LENGTH_SHORT).show();

            }
        });
        fab.setImageResource(R.drawable.ic_send_white_24dp);

        return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_main);
        final NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_submit);
            }
        });
        fab.setImageResource(R.drawable.ic_create_white_24dp);

    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(intent, Constants.PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                grantResults[2] == PackageManager.PERMISSION_GRANTED)
            return;
        else
            Toast.makeText(getContext(), "Cannot grant permissions", Toast.LENGTH_LONG).show();
    }

    private void dispatchTakePictureIntent() {
        Context context = getActivity();
        PackageManager packageManager = context.getPackageManager();
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) == false) {
            Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(getContext(), "Could Not Create File!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                uri = FileProvider.getUriForFile(getContext(),
                        "il.ac.afeka.tomco.a10242_cookeat",
                        photoFile);
                Log.d("Uri in method:", uri.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE);
                SubmitRecipeRepository.setUri(uri);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void sendRecipe() {
        if (recipeNameET.getText().toString().trim().isEmpty() ||
                readyInTimeET.getText().toString().isEmpty() ||
                ingredients.isEmpty() ||
                instructions.isEmpty() ||
                equipment.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference recipesRef = FirebaseFirestore.getInstance().collection("Recipes");
        StorageReference recipeImagesRef = FirebaseStorage.getInstance().getReference("RecipeImages");

        uri = SubmitRecipeRepository.getUri();
        Log.d("URI", uri.toString());
        if (uri != null) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                StorageReference fileRef = recipeImagesRef.child(System.currentTimeMillis()
                        + "." + getFileExtension(uri));
                StorageTask storageTask = fileRef.putFile(uri)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(getContext(), "Upload Successful", Toast.LENGTH_LONG).show();
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            SubmitRecipeRepository.setUri(Uri.parse(uri.getResult().toString()));
                            setRecipeDetails();
                            SubmitRecipeRepository.getRecipeModel().setImageUrl(uri.getResult().toString());
                            recipesRef.add(SubmitRecipeRepository.getRecipeModel());
                            Toast.makeText(getContext(), "Recipe Added!", Toast.LENGTH_SHORT).show();
                            showProgressBar(false);
                            closeFragment();
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show())
                        .addOnProgressListener(taskSnapshot -> showProgressBar(true));
            } else {
                Toast.makeText(getContext(),"Please Grant Premissins and send again",Toast.LENGTH_SHORT).show();
                String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, READ_WRITE_EXTERNAL_STORAGE_FOR_GALLERY);
            }
        } else {
            Toast.makeText(getContext(), "No File Selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setRecipeDetails() {
        SubmitRecipeRepository.getRecipeModel().setTitle(recipeNameET.getText().toString());
        SubmitRecipeRepository.getRecipeModel().setUploadingUserID(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        SubmitRecipeRepository.getRecipeModel().setReadyInMinutes(Integer.parseInt(readyInTimeET.getText().toString()));
        SubmitRecipeRepository.getRecipeModel().setIngredientsList(ingredients);
        SubmitRecipeRepository.getRecipeModel().setEquipmentList(equipment);
        SubmitRecipeRepository.getRecipeModel().setInstructionList(instructions);
        SubmitRecipeRepository.getRecipeModel().setCuisineID(cuisineId);
    }

    private void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void showProgressBar(boolean visibility) {
        mProgressBar.setVisibility(visibility ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        cuisineId = categoryModels.get(position).getDocumentId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getActivity(), "Please Select Cuisine!", Toast.LENGTH_SHORT).show();
    }

    public static String getCurrentPhotoPath() {
        return currentPhotoPath;
    }
}