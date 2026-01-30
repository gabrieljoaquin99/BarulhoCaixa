package com.example.barulho;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    public static final String JOGADORES_KEY = "jogadores";
    public static final String MENSALIDADES_KEY = "mensalidades";
    public static final String LANCAMENTOS_KEY = "lancamentos";
    public static final String JOGOS_KEY = "jogos";
    public static final String PRESENCAS_KEY = "presencas";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogadores = root.child(JOGADORES_KEY);

    String arqManter = "arqmanter.txt";

    ImageView imgMain;
    EditText txtMainLogin, txtMainSenha;
    Switch swtMainSenha;
    CheckBox ckbMainManter;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgMain = findViewById(R.id.imgMain);
        txtMainLogin = findViewById(R.id.txtMainLogin);
        txtMainSenha = findViewById(R.id.txtMainSenha);
        swtMainSenha = findViewById(R.id.swtMainSenha);
        ckbMainManter = findViewById(R.id.ckbMainManter);

        txtMainLogin.setText("");
        txtMainSenha.setText("");

        try (FileInputStream fis = openFileInput(arqManter);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {
            String tmp = br.readLine();

            String[] loginData = tmp.split("\\|");

            txtMainLogin.setText(loginData[0]);
            txtMainSenha.setText(loginData[1]);

            ckbMainManter.setChecked(true);
        } catch (IOException e) {
        }

    }

    public void btnMainSolicitar(View v) {
        String whatsTmp = txtMainLogin.getText().toString();
        String senhaTmp = txtMainSenha.getText().toString();

        Intent it = new Intent(getBaseContext(), SolicCadJogadorActivity.class);
        it.putExtra("whatsTmp", whatsTmp);
        it.putExtra("senhaTmp", senhaTmp);
        startActivity(it);
    }

    public void btnMainLogin(View v) {
        if ((txtMainLogin.getText().toString().isEmpty()) | (txtMainSenha.getText().toString().isEmpty())){
            printMessage("Preencha todos os campos...");
        } else {

            final String key = txtMainLogin.getText().toString();
            jogadores.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Jogador cadTemp = dataSnapshot.getValue(Jogador.class);
                        if (cadTemp.getAtivo() == 1) {
                            if (cadTemp.getSenha().contentEquals(txtMainSenha.getText().toString())) {
                                if (ckbMainManter.isChecked()) {
                                    ckbLoginManter();
                                } else {
                                    File file = new File(getFilesDir(), arqManter);
                                    if (file.exists()) {
                                        boolean deleted = file.delete();
                                    }
                                }

                                if (cadTemp.getNivel() == 1) {
                                    changeToHomeAdmin(key, cadTemp);
                                } else {
                                    changeToHomeNormal(key, cadTemp);
                                }
                            } else {
                                printMessage("Senha incorreta.");
                            }
                        } else {
                            printMessage("CPF não está ATIVO, chame o Administrador...");
                        }
                    } else {
                        printMessage("Usuario não encontrado, se o CPF estiver correto chame o Administrador");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    printMessage("Erro ao verificar se o Associado já existe: " + databaseError.getMessage());
                }
            });
        }
    }

    public void changeToHomeAdmin(String cadTmpKey, Jogador cadTmp){
        Intent it = new Intent(getBaseContext(), HomeAdmin.class);
        it.putExtra("cadTmpKey", cadTmpKey);
        it.putExtra("cadTmp", cadTmp);
        startActivity(it);
    }

    public void changeToHomeNormal(String cadTmpKey, Jogador cadTmp){
        Intent it = new Intent(getBaseContext(), HomeNormal.class);
        it.putExtra("cadTmpKey", cadTmpKey);
        it.putExtra("cadTmp", cadTmp);
        startActivity(it);
    }

    public void ckbLoginManter(){
        try (FileOutputStream fos = openFileOutput(arqManter, Context.MODE_PRIVATE)) {
            String dadosLogin = txtMainLogin.getText().toString()+"|"+txtMainSenha.getText().toString();

            fos.write(dadosLogin.getBytes());
        } catch (IOException e) {
        }
    }

    public void swtMainSenha(View v) {
        if(swtMainSenha.isChecked()){
            txtMainSenha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            txtMainSenha.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

}