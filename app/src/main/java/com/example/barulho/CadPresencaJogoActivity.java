package com.example.barulho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

public class CadPresencaJogoActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogadores = root.child(MainActivity.JOGADORES_KEY);
    DatabaseReference presencas = root.child(MainActivity.PRESENCAS_KEY);

    CheckBox ckbCadPresJogo1, ckbCadPresJogo2, ckbCadPresJogo3, ckbCadPresJogo4, ckbCadPresJogo5,
            ckbCadPresJogo6, ckbCadPresJogo7, ckbCadPresJogo8, ckbCadPresJogo9, ckbCadPresJogo10;
    Spinner spnCadPresJogo1, spnCadPresJogo2, spnCadPresJogo3, spnCadPresJogo4, spnCadPresJogo5,
            spnCadPresJogo6, spnCadPresJogo7, spnCadPresJogo8, spnCadPresJogo9, spnCadPresJogo10;
    EditText txtCadPresJogoGols1, txtCadPresJogoGols2, txtCadPresJogoGols3, txtCadPresJogoGols4, txtCadPresJogoGols5,
            txtCadPresJogoGols6, txtCadPresJogoGols7, txtCadPresJogoGols8, txtCadPresJogoGols9, txtCadPresJogoGols10,
            txtCadPresJogoAssis1, txtCadPresJogoAssis2, txtCadPresJogoAssis3, txtCadPresJogoAssis4, txtCadPresJogoAssis5,
            txtCadPresJogoAssis6, txtCadPresJogoAssis7, txtCadPresJogoAssis8, txtCadPresJogoAssis9, txtCadPresJogoAssis10;

    String jogoSelecKey;
    int jogoSelecAno;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_presenca_jogo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent it = getIntent();
        jogoSelecKey = (String) it.getSerializableExtra("jogoSelecKey");
        jogoSelecAno = (int) it.getSerializableExtra("jogoSelecAno");

        ckbCadPresJogo1 = findViewById(R.id.ckbCadPresJogo1);
        ckbCadPresJogo2 = findViewById(R.id.ckbCadPresJogo2);
        ckbCadPresJogo3 = findViewById(R.id.ckbCadPresJogo3);
        ckbCadPresJogo4 = findViewById(R.id.ckbCadPresJogo4);
        ckbCadPresJogo5 = findViewById(R.id.ckbCadPresJogo5);
        ckbCadPresJogo6 = findViewById(R.id.ckbCadPresJogo6);
        ckbCadPresJogo7 = findViewById(R.id.ckbCadPresJogo7);
        ckbCadPresJogo8 = findViewById(R.id.ckbCadPresJogo8);
        ckbCadPresJogo9 = findViewById(R.id.ckbCadPresJogo9);
        ckbCadPresJogo10 = findViewById(R.id.ckbCadPresJogo10);

        spnCadPresJogo1 = findViewById(R.id.spnCadPresJogo1);
        spnCadPresJogo2 = findViewById(R.id.spnCadPresJogo2);
        spnCadPresJogo3 = findViewById(R.id.spnCadPresJogo3);
        spnCadPresJogo4 = findViewById(R.id.spnCadPresJogo4);
        spnCadPresJogo5 = findViewById(R.id.spnCadPresJogo5);
        spnCadPresJogo6 = findViewById(R.id.spnCadPresJogo6);
        spnCadPresJogo7 = findViewById(R.id.spnCadPresJogo7);
        spnCadPresJogo8 = findViewById(R.id.spnCadPresJogo8);
        spnCadPresJogo9 = findViewById(R.id.spnCadPresJogo9);
        spnCadPresJogo10 = findViewById(R.id.spnCadPresJogo10);

        txtCadPresJogoGols1 = findViewById(R.id.txtCadPresJogoGols1);
        txtCadPresJogoGols2 = findViewById(R.id.txtCadPresJogoGols2);
        txtCadPresJogoGols3 = findViewById(R.id.txtCadPresJogoGols3);
        txtCadPresJogoGols4 = findViewById(R.id.txtCadPresJogoGols4);
        txtCadPresJogoGols5 = findViewById(R.id.txtCadPresJogoGols5);
        txtCadPresJogoGols6 = findViewById(R.id.txtCadPresJogoGols6);
        txtCadPresJogoGols7 = findViewById(R.id.txtCadPresJogoGols7);
        txtCadPresJogoGols8 = findViewById(R.id.txtCadPresJogoGols8);
        txtCadPresJogoGols9 = findViewById(R.id.txtCadPresJogoGols9);
        txtCadPresJogoGols10 = findViewById(R.id.txtCadPresJogoGols10);

        txtCadPresJogoAssis1 = findViewById(R.id.txtCadPresJogoAssis1);
        txtCadPresJogoAssis2 = findViewById(R.id.txtCadPresJogoAssis2);
        txtCadPresJogoAssis3 = findViewById(R.id.txtCadPresJogoAssis3);
        txtCadPresJogoAssis4 = findViewById(R.id.txtCadPresJogoAssis4);
        txtCadPresJogoAssis5 = findViewById(R.id.txtCadPresJogoAssis5);
        txtCadPresJogoAssis6 = findViewById(R.id.txtCadPresJogoAssis6);
        txtCadPresJogoAssis7 = findViewById(R.id.txtCadPresJogoAssis7);
        txtCadPresJogoAssis8 = findViewById(R.id.txtCadPresJogoAssis8);
        txtCadPresJogoAssis9 = findViewById(R.id.txtCadPresJogoAssis9);
        txtCadPresJogoAssis10 = findViewById(R.id.txtCadPresJogoAssis10);

        jogadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> spinnerData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(Jogador.class).getWhats()+" - "+snapshot.getValue(Jogador.class).getNome();
                    spinnerData.add(value);
                }
                ArrayAdapter<String> adapterSpnAssoc = new ArrayAdapter<>(CadPresencaJogoActivity.this, android.R.layout.simple_spinner_item, spinnerData);
                adapterSpnAssoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCadPresJogo1.setAdapter(adapterSpnAssoc);
                spnCadPresJogo2.setAdapter(adapterSpnAssoc);
                spnCadPresJogo3.setAdapter(adapterSpnAssoc);
                spnCadPresJogo4.setAdapter(adapterSpnAssoc);
                spnCadPresJogo5.setAdapter(adapterSpnAssoc);
                spnCadPresJogo6.setAdapter(adapterSpnAssoc);
                spnCadPresJogo7.setAdapter(adapterSpnAssoc);
                spnCadPresJogo8.setAdapter(adapterSpnAssoc);
                spnCadPresJogo9.setAdapter(adapterSpnAssoc);
                spnCadPresJogo10.setAdapter(adapterSpnAssoc);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void btnCadPresJogoAdd(View v){
        if(ckbCadPresJogo1.isChecked()){
            int num = 1;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo1.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols1.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols1.getText().toString());
            }

            if(txtCadPresJogoAssis1.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis1.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo2.isChecked()){
            int num = 2;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo2.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols2.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols2.getText().toString());
            }

            if(txtCadPresJogoAssis2.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis2.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo3.isChecked()){
            int num = 3;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo3.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols3.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols3.getText().toString());
            }

            if(txtCadPresJogoAssis3.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis3.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo4.isChecked()){
            int num = 4;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo4.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols4.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols4.getText().toString());
            }

            if(txtCadPresJogoAssis4.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis4.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo5.isChecked()){
            int num = 5;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo5.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols5.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols5.getText().toString());
            }

            if(txtCadPresJogoAssis5.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis5.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo6.isChecked()){
            int num = 6;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo6.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols6.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols6.getText().toString());
            }

            if(txtCadPresJogoAssis6.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis6.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo7.isChecked()){
            int num = 7;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo7.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols7.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols7.getText().toString());
            }

            if(txtCadPresJogoAssis7.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis7.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo8.isChecked()){
            int num = 8;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo8.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols8.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols8.getText().toString());
            }

            if(txtCadPresJogoAssis8.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis8.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo9.isChecked()){
            int num = 9;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo9.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols9.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols9.getText().toString());
            }

            if(txtCadPresJogoAssis9.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis9.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        if(ckbCadPresJogo10.isChecked()){
            int num = 10;
            int golsTmp=0, assisTmp=0;
            String jogaSelec;

            String jogaSelecTmp = spnCadPresJogo10.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            if(txtCadPresJogoGols10.getText().toString().contentEquals("")){
            } else {
                golsTmp = Integer.parseInt(txtCadPresJogoGols10.getText().toString());
            }

            if(txtCadPresJogoAssis10.getText().toString().contentEquals("")){
            } else {
                assisTmp = Integer.parseInt(txtCadPresJogoAssis10.getText().toString());
            }

            addPresenca(num, jogoSelecKey, jogaSelec, golsTmp, assisTmp);
        }

        finish();
    }

    public void addPresenca(int num, String jogoTmp, String jogaTmp, int golsTmp, int assisTmp){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("presencas");
        databaseReference.orderByChild("keyJogo").equalTo(jogoTmp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String cpfEncontrado = snapshot.child("cpfAssoc").getValue(String.class);
                            if (jogaTmp != null && jogaTmp.equals(cpfEncontrado)) {
                                exists = true;
                                break;
                            }
                        }

                        if (exists) {
                            printMessage("Presença já existe para o Jogador "+num+" e Jogo selecionados...");
                        } else {
                            Presenca presenca = new Presenca();

                            presenca.setKeyJogo(jogoTmp);
                            presenca.setKeyJogador(jogaTmp);
                            presenca.setGols(golsTmp);
                            presenca.setAssist(assisTmp);
                            presenca.setDtAno(jogoSelecAno);

                            String key = presencas.push().getKey();
                            presencas.child(key).setValue(presenca);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        printMessage("Erro ao acessar o Firebase");
                    }
                });
    }
}