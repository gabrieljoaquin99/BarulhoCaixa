package com.example.barulho;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.time.LocalDate;

public class HomeAdmin extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();

    String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
            "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    LocalDate dataAtual = LocalDate.now();
    String mesRefAtual = meses[dataAtual.getMonthValue()-1]+"/"+dataAtual.getYear();

    TextView txtHomeAdminJogaNome, txtHomeAdminJogaStatus, txtHomeAdminJogaJogos,
            txtHomeAdminJogaGols, txtHomeAdminJogaAssist;
    ImageView imgHomeAdminJoga;

    Jogador cadTmp;
    String cadTmpKey;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent it = getIntent();
        cadTmpKey = (String) it.getSerializableExtra("cadTmpKey");
        cadTmp = (Jogador) it.getSerializableExtra("cadTmp");

        txtHomeAdminJogaNome = findViewById(R.id.txtHomeAdminJogaNome);
        txtHomeAdminJogaStatus = findViewById(R.id.txtHomeAdminJogaStatus);
        imgHomeAdminJoga = findViewById(R.id.imgHomeAdminJoga);
        txtHomeAdminJogaJogos = findViewById(R.id.txtHomeAdminJogaJogos);
        txtHomeAdminJogaGols = findViewById(R.id.txtHomeAdminJogaGols);
        txtHomeAdminJogaAssist = findViewById(R.id.txtHomeAdminJogaAssist);

        txtHomeAdminJogaNome.setText(cadTmp.getNome());

        txtHomeAdminJogaJogos.setText("");
        txtHomeAdminJogaGols.setText("");
        txtHomeAdminJogaAssist.setText("");
        txtHomeAdminJogaStatus.setText("");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("mensalidades");
        databaseReference.orderByChild("mesRef").equalTo(mesRefAtual)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;
                        double contVlr = 0;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String jogaEncontrado = snapshot.child("keyJoga").getValue(String.class);
                            if (cadTmp.getWhats() != null && cadTmp.getWhats().equals(jogaEncontrado)) {
                                exists = true;
                                contVlr = contVlr + snapshot.child("vlrPago").getValue(double.class);
                            }
                        }

                        if (exists) {
                            if(contVlr >= 50){
                                txtHomeAdminJogaStatus.setText("Adimplente");
                                txtHomeAdminJogaStatus.setTextColor(Color.GREEN);
                            } else{
                                txtHomeAdminJogaStatus.setText("Pag. Parcial ("+contVlr+")");
                                txtHomeAdminJogaStatus.setTextColor(Color.MAGENTA);
                            }
                        } else {
                            txtHomeAdminJogaStatus.setText("Inadimplente");
                            txtHomeAdminJogaStatus.setTextColor(Color.RED);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        printMessage("Erro ao acessar o Firebase");
                    }
                });

        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("presencas");
        databaseReference2.orderByChild("keyJogador").equalTo(cadTmpKey)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int contJogos=0, contGols=0, contAssis=0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Presenca pres = snapshot.getValue(Presenca.class);
                            contJogos = contJogos+1;
                            contGols = contGols + pres.getGols();
                            contAssis = contAssis + pres.getAssist();
                        }

                        cadTmp.setContJogos(contJogos);
                        cadTmp.setContGols(contGols);
                        cadTmp.setContAssist(contAssis);

                        txtHomeAdminJogaJogos.setText(Integer.toString(cadTmp.getContJogos()));
                        txtHomeAdminJogaGols.setText(Integer.toString(cadTmp.getContGols()));
                        txtHomeAdminJogaAssist.setText(Integer.toString(cadTmp.getContAssist()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        printMessage("Erro ao acessar o Firebase");
                    }
                });
    }

    public void btnHomeAdminJogaCad(View v){
        Intent it = new Intent(getBaseContext(), CadJogador.class);
        startActivity(it);
    }

    public void btnHomeAdminCadMensal(View v){
        Intent it = new Intent(getBaseContext(), CadMensalActivity.class);
        startActivity(it);
    }

    public void btnHomeAdminCadLancFin(View v){
        Intent it = new Intent(getBaseContext(), CadLancFinActivity.class);
        startActivity(it);
    }

    public void btnHomeAdminCadJogos(View v){
        Intent it = new Intent(getBaseContext(), CadJogoActivity.class);
        startActivity(it);
    }

    public void btnHomeAdminResumoFin(View v){
        Intent it = new Intent(getBaseContext(), ResumoFinActivity.class);
        startActivity(it);
    }

    public void btnHomeAdminResumoTemp(View v){
        Intent it = new Intent(getBaseContext(), ResumoTempActivity.class);
        startActivity(it);
    }
}