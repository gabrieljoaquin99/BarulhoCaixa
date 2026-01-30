package com.example.barulho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class SolicCadJogadorActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogadores = root.child(MainActivity.JOGADORES_KEY);

    EditText txtSolicCadJogaNome, txtSolicCadJogaWhats, txtSolicCadJogaSenha;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_solic_cad_jogador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent it = getIntent();
        String whatsTmp = (String) it.getSerializableExtra("whatsTmp");
        String senhaTmp = (String) it.getSerializableExtra("senhaTmp");

        txtSolicCadJogaNome = findViewById(R.id.txtSolicCadJogaNome);
        txtSolicCadJogaWhats = findViewById(R.id.txtSolicCadJogaWhats);
        txtSolicCadJogaSenha = findViewById(R.id.txtSolicCadJogaSenha);

        txtSolicCadJogaNome.setText("");
        txtSolicCadJogaWhats.setText(whatsTmp);
        txtSolicCadJogaSenha.setText(senhaTmp);
    }

    public void btnSolicCadJoga(View v){
        if(txtSolicCadJogaNome.getText().toString().contentEquals("") &&
                txtSolicCadJogaWhats.getText().toString().contentEquals("") &&
                txtSolicCadJogaSenha.getText().toString().contentEquals("")){
            printMessage("Preencha todos os dados...");
        } else {
            Jogador joga = new Jogador();

            joga.setNome(txtSolicCadJogaNome.getText().toString());
            joga.setWhats(txtSolicCadJogaWhats.getText().toString());
            joga.setSenha(txtSolicCadJogaSenha.getText().toString());
            joga.setContJogos(0);
            joga.setContGols(0);
            joga.setContAssist(0);
            joga.setAtivo(0);
            joga.setNivel(0);

            final String key = joga.getWhats();
            jogadores.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        printMessage("Jogador já cadastrado, verifique com o Administrador...");
                    } else {
                        jogadores.child(key).setValue(joga);

                        printMessage("Jogador cadastrado...");
                        finish();
                    }
                }
                @Override
                public void onCancelled (DatabaseError databaseError){
                    printMessage("Erro ao verificar se o Associado já existe: " + databaseError.getMessage());
                }
            });
        }
    }
}