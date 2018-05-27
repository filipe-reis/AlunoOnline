package br.iesb.mobile.alunoonline;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity{

    private TextView esqueceuSenha, cadastro;
    private Button loginBtn, btnTeste;
    private ImageButton btnGoogle, btnFacebook;

    private GoogleApiClient       mGoogleApiClient          ;
    public  GoogleSignInOptions   gso                       ;
    private GoogleSignInClient    mGoogleSignInAccount      ;
    private static final int      RC_SIGN_IN_GOOGLE  = 9001 ;
    private static final String   TAG = "Login Activity"    ;

    private CallbackManager callbackManager;

    //Autencicacao Firebase
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Firebase
        auth = FirebaseAuth.getInstance();

        //Facebook
        callbackManager = CallbackManager.Factory.create();

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInAccount = GoogleSignIn.getClient(this, gso);


        //Localizando Componentes
        esqueceuSenha = findViewById(R.id.textEsqueceuSenha );
        cadastro      = findViewById(R.id.textCadastro      );
        loginBtn      = findViewById(R.id.btnLogin          );
        btnTeste      = findViewById(R.id.btnTeste          );
        btnGoogle     = findViewById(R.id.btnGoogle         );
        btnFacebook   = findViewById(R.id.btnFacebook       );

        esqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecuperarSenha.class);
                startActivity(intent);
            }
        });

        cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Cadastro.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmail();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                signInProvider("google");
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInProvider("facebook");
            }
        });

        btnTeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, TelaInicial.class);
                startActivity(it);
            }
        });
    }

    /**
     * Define com qual provedor será feito o login.
     * @param provider
     */
    private void signInProvider(String provider) {
        if(provider.equals("facebook")){
            signInFacebook();
        }else{
            //Google
            signInGoogle();
        }
    }

    private void loginEmail() {

        //Recuperando campos de login
        EditText editEmail = findViewById(R.id.editEmailRecuperar);
        EditText editSenha = findViewById(R.id.editSenha);

        //Metodo assincrono
        Task<AuthResult> processo = auth.signInWithEmailAndPassword(editEmail.getText().toString(), editSenha.getText().toString());
        processo.addOnCompleteListener(new OnCompleteListener<AuthResult>() { //Metodo que espera o resultado da autenticacao
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(MainActivity.this, TelaInicial.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "E-mail ou senha inválido!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInAccount.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void signInFacebook(){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {}

            @Override
            public void onError(FacebookException error) {

            }
        });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_status"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent( data );
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else{
                //Google SignIn Failure
                Toast.makeText(MainActivity.this, "Falha no login com Google", Toast.LENGTH_LONG).show();
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "signInWithiCredential:sucess");
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                }else{
                    Log.w(TAG, "signInWithCredential: failure", task.getException());
                    Toast.makeText(MainActivity.this, "Autenticação falhou.", Toast.LENGTH_LONG).show();
                    updateUI(null);
                }
            }
        });
    }

    private void firebaseAuthWithFacebook(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user){
        if(user != null){
            Intent intent = new Intent(MainActivity.this, TelaInicial.class);
            startActivity(intent);
            finish();
        }
    }


    public void signOutGoogle(){

        mGoogleSignInAccount.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                updateUI(null);
            }
        });
    }
}
