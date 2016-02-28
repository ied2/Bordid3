package is.aiga.bordid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity{

    private EditText mUsernameView, mPasswordView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        this.setTitle("Create Account");

        // Back arrow enabled
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUsernameView = (EditText) findViewById(R.id.username_create);
        mPasswordView = (EditText) findViewById(R.id.password_create);

        Button mEmailSignInButton = (Button) findViewById(R.id.create_account);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateAccount();
                Toast.makeText(CreateAccountActivity.this, "Account to be created :)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // this takes the user 'back', as if they pressed the left-facing triangle icon on the main android toolbar.
                // if this doesn't work as desired, another possibility is to call `finish()` here.
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
