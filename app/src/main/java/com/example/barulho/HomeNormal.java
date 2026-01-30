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

public class HomeNormal extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();

    String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
            "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    LocalDate dataAtual = LocalDate.now();
    String mesRefAtual = meses[dataAtual.getMonthValue()-1]+"/"+dataAtual.getYear();

    TextView txtHomeNormalJogaNome, txtHomeNormalJogaStatus, txtHomeNormalJogaJogos,
            txtHomeNormalJogaGols, txtHomeNormalJogaAssist;
    ImageView imgHomeNormalJoga;

    Jogador cadTmp;
    String cadTmpKey;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_normal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent it = getIntent();
        cadTmpKey = (String) it.getSerializableExtra("cadTmpKey");
        cadTmp = (Jogador) it.getSerializableExtra("cadTmp");

        txtHomeNormalJogaNome = findViewById(R.id.txtHomeNormalJogaNome);
        txtHomeNormalJogaStatus = findViewById(R.id.txtHomeNormalJogaStatus);
        imgHomeNormalJoga = findViewById(R.id.imgHomeNormalJoga);
        txtHomeNormalJogaJogos = findViewById(R.id.txtHomeNormalJogaJogos);
        txtHomeNormalJogaGols = findViewById(R.id.txtHomeNormalJogaGols);
        txtHomeNormalJogaAssist = findViewById(R.id.txtHomeNormalJogaAssist);

        txtHomeNormalJogaNome.setText(cadTmp.getNome());

        txtHomeNormalJogaJogos.setText("");
        txtHomeNormalJogaGols.setText("");
        txtHomeNormalJogaAssist.setText("");
        txtHomeNormalJogaStatus.setText("");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("mensalidades");
        databaseReference.orderByChild("mesRef").equalTo(mesRefAtual)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exists = false;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String jogaEncontrado = snapshot.child("keyJoga").getValue(String.class);
                            if (cadTmp.getWhats() != null && cadTmp.getWhats().equals(jogaEncontrado)) {
                                exists = true;
                                break;
                            }
                        }

                        if (exists) {
                            txtHomeNormalJogaStatus.setText("Adimplente");
                            txtHomeNormalJogaStatus.setTextColor(Color.GREEN);
                        } else {
                            txtHomeNormalJogaStatus.setText("Inadimplente");
                            txtHomeNormalJogaStatus.setTextColor(Color.RED);
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

                        txtHomeNormalJogaJogos.setText(Integer.toString(cadTmp.getContJogos()));
                        txtHomeNormalJogaGols.setText(Integer.toString(cadTmp.getContGols()));
                        txtHomeNormalJogaAssist.setText(Integer.toString(cadTmp.getContAssist()));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        printMessage("Erro ao acessar o Firebase");
                    }
                });
    }


    public void btnHomeNormalCadMensal(View v){
        Intent it = new Intent(getBaseContext(), CadMensalJogaActivity.class);
        it.putExtra("cadTmpKey", cadTmpKey);
        startActivity(it);
    }


    public void btnHomeNormalResumoFin(View v){
        Intent it = new Intent(getBaseContext(), ResumoFinActivity.class);
        startActivity(it);
    }

    public void btnHomeNormalResumoTemp(View v){
        Intent it = new Intent(getBaseContext(), ResumoTempActivity.class);
        startActivity(it);
    }
}