package il.ac.afeka.tomco.a10242_cookeat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import il.ac.afeka.tomco.a10242_cookeat.Network.Constants;
import il.ac.afeka.tomco.a10242_cookeat.Repositories.SubmitRecipeRepository;
import il.ac.afeka.tomco.a10242_cookeat.ui.Submit.SubmitRecipeFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;

    private static FirebaseUser currentUser;
    private NavigationView navigationView;
    private ProgressBar mProgressBar;

    public static String getUserID() {
        return currentUser.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mProgressBar = findViewById(R.id.progressBar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_favorites, R.id.nav_auth, R.id.nav_submit)
                .setDrawerLayout(drawer)
                .build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        final FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.nav_submit);
            }
        });
        if (currentUser == null) {
            navigationView.getMenu().findItem(R.id.nav_favorites).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_submit).setVisible(false);
            fab.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void createSignInIntent() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());

        // Create and launch sign-in intent
        if (currentUser == null)
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setLogo(R.drawable.cookingappwithname)
                            .build(),
                    Constants.RC_SIGN_IN);
        // [END auth_fui_create_intent]
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Sign-in related results:
        if (requestCode == Constants.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                currentUser = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(currentUser);
            } else {
                Toast.makeText(MainActivity.this, "Authentication failed. (" + response.getError().getErrorCode() + ")", Toast.LENGTH_SHORT).show();
                updateUI(null);
            }
        }
        //Taking a picture related request:
        if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {

                ImageView recipeImageIV = findViewById(R.id.srfRecipeImageIV);
//                SubmitRecipeRepository.setUri(Uri.parse(SubmitRecipeFragment.getCurrentPhotoPath()));
//                Log.d("imageURI:", SubmitRecipeRepository.getUri().toString());
                recipeImageIV.setImageURI(Uri.parse(SubmitRecipeFragment.getCurrentPhotoPath()));
            }
        }
        if (requestCode == Constants.PICK_IMAGE_REQUEST) {
            if (resultCode == RESULT_OK) {
                ImageView recipeImageIV = findViewById(R.id.srfRecipeImageIV);
                recipeImageIV.setImageURI(data.getData());
                SubmitRecipeRepository.setUri(data.getData());
                Log.d("imageURI:", SubmitRecipeRepository.getUri().toString());
            }
        }
    }
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        ImageView i = findViewById(R.id.userAvatarCIV);
                        i.setImageResource(R.drawable.ic_person_white_24dp);
                        Menu menu = navigationView.getMenu();
                        menu.findItem(R.id.nav_auth).setTitle(R.string.menu_auth_up_in);
                        TextView name = findViewById(R.id.userNameTV);
                        name.setText(R.string.userNotAuth);
                        TextView email = findViewById(R.id.userEmailTV);
                        email.setText(R.string.userPleaseSignIn);
                        final FloatingActionButton fab = findViewById(R.id.fab_main);
                        fab.setVisibility(View.INVISIBLE);
                        navigationView.getMenu().findItem(R.id.nav_submit).setVisible(false);
                        navigationView.getMenu().findItem(R.id.nav_favorites).setVisible(false);
                        currentUser = null;
                    }
                });
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            ImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.userAvatarCIV);
            if (currentUser.getPhotoUrl() != null) {
                Picasso
                        .get()
                        .load(currentUser.getPhotoUrl())
                        .placeholder(R.drawable.ic_person_white_24dp)
                        .into(imageView);
            }
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_auth).setTitle(R.string.menu_auth_out);
            TextView name = navigationView.getHeaderView(0).findViewById(R.id.userNameTV);
            name.setText(currentUser.getDisplayName());
            TextView email = navigationView.getHeaderView(0).findViewById(R.id.userEmailTV);
            email.setText(currentUser.getEmail());
            final FloatingActionButton fab = findViewById(R.id.fab_main);
            fab.setVisibility(View.VISIBLE);
            navigationView.getMenu().findItem(R.id.nav_submit).setVisible(true);
            navigationView.getMenu().findItem(R.id.nav_favorites).setVisible(true);
        }
    }

    public void auth(MenuItem item) {
        if (currentUser == null)
            createSignInIntent();
        else {
            FirebaseAuth.getInstance().signOut();
            signOut();
        }
    }


}
