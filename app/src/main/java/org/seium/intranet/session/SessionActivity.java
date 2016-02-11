package org.seium.intranet.session;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.seium.intranet.R;
import org.seium.intranet.dashboard.DashboardActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SessionActivity extends AppCompatActivity implements Validator.ValidationListener {
    @Bind(R.id.activity_session_et_username)
    @NotEmpty
    EditText mUsername;

    @Bind(R.id.activity_session_et_password)
    @Password(min = 6)
    EditText mPassword;

    private Validator mValidator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        ButterKnife.bind(this);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
    }

    @OnClick(R.id.activity_session_btn_submit)
    public void submit(View view) {
        mValidator.validate();
    }

    @Override
    public void onValidationSucceeded() {
//        ProgressDialog progress = new ProgressDialog(this);
//        progress.setIndeterminate(true);
//        progress.setMessage("A entrar...");
//        progress.show();

        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
