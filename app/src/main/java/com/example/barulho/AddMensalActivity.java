package com.example.barulho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddMensalActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogadores = root.child(MainActivity.JOGADORES_KEY);
    DatabaseReference mensalidades = root.child(MainActivity.MENSALIDADES_KEY);

    EditText txtAddMensalVlr;
    Spinner spnAddMensalMesRef, spnAddMensalJoga;
    DatePicker dtpAddMensalDtPag;

    String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
            "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    int posAssoc;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_mensal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtAddMensalVlr = findViewById(R.id.txtAddMensalVlr);
        dtpAddMensalDtPag = findViewById(R.id.dtpAddMensalDtPag);
        spnAddMensalMesRef = findViewById(R.id.spnAddMensalMesRef);
        spnAddMensalJoga = findViewById(R.id.spnAddMensalJoga);

        txtAddMensalVlr.setText("50");

        ArrayAdapter<String> adapterSpnMeses = new ArrayAdapter<>(AddMensalActivity.this, android.R.layout.simple_spinner_item, meses);
        adapterSpnMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAddMensalMesRef.setAdapter(adapterSpnMeses);
        spnAddMensalMesRef.setSelection(dtpAddMensalDtPag.getMonth());

        Intent it = getIntent();
        String jogaSelec = (String) it.getSerializableExtra("jogaSelec");

        jogadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> spinnerData = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(Jogador.class).getWhats()+" - "+snapshot.getValue(Jogador.class).getNome();
                    spinnerData.add(value);

                    if (jogaSelec.contentEquals(snapshot.getValue(Jogador.class).getWhats())){
                        posAssoc = spinnerData.size()-1;
                    }
                }
                ArrayAdapter<String> adapterSpnAssoc = new ArrayAdapter<>(AddMensalActivity.this, android.R.layout.simple_spinner_item, spinnerData);
                adapterSpnAssoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnAddMensalJoga.setAdapter(adapterSpnAssoc);
                spnAddMensalJoga.setSelection(posAssoc);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void btnAddMensalAdd(View v){
        if(txtAddMensalVlr.getText().toString().contentEquals("")){
            printMessage("Preencha o Valor da Mensalidade...");
        } else {
            String mesRefTmp = spnAddMensalMesRef.getSelectedItem().toString() + "/" + dtpAddMensalDtPag.getYear();
            String jogaSelecTmp = spnAddMensalJoga.getSelectedItem().toString();
            int posicaoHifen = jogaSelecTmp.indexOf(" - ");
            String jogaSelec = jogaSelecTmp.substring(0, posicaoHifen);

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("mensalidades");
            databaseReference.orderByChild("mesRef").equalTo(mesRefTmp)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean exists = false;

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String cpfEncontrado = snapshot.child("cpfAssoc").getValue(String.class);
                                if (jogaSelec != null && jogaSelec.equals(cpfEncontrado)) {
                                    exists = true;
                                    break;
                                }
                            }

                            if (exists) {
                                printMessage("Mensalidade já existe para o Jogador e Mês selecionados...");
                            } else {
                                Mensalidade mensal = new Mensalidade();

                                mensal.setVlrPago(Double.parseDouble(txtAddMensalVlr.getText().toString()));

                                mensal.setDtDia(dtpAddMensalDtPag.getDayOfMonth());
                                mensal.setDtMes(dtpAddMensalDtPag.getMonth()+1);
                                mensal.setDtAno(dtpAddMensalDtPag.getYear());

                                mensal.setMesRef(mesRefTmp);
                                mensal.setKeyJoga(jogaSelec);

                                String key = mensalidades.push().getKey();
                                mensalidades.child(key).setValue(mensal);

                                printMessage("Mensalidade Adicionada!!!");
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            printMessage("Erro ao acessar o Firebase");
                        }
                    });
        }
    }
}