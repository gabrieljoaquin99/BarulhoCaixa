package com.example.barulho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class CadMensalJogaActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogadores = root.child(MainActivity.JOGADORES_KEY);

    FirebaseListAdapter<Mensalidade> listAdapterMensalidades;

    String cadTmpKey;

    ListView listCadMensal;

    DecimalFormat df = new DecimalFormat("R$ #,##0.00");

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_mensal_joga);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent it = getIntent();
        cadTmpKey = (String) it.getSerializableExtra("cadTmpKey");

        listCadMensal = findViewById(R.id.listCadMensal);

        Query queryMensais = FirebaseDatabase.getInstance().getReference()
                .child("mensalidades")
                .orderByChild("keyJoga")
                .equalTo(cadTmpKey);

        listAdapterMensalidades = new FirebaseListAdapter<Mensalidade>(this, Mensalidade.class,
                R.layout.mensalidade_item, queryMensais) {
            @Override
            protected void populateView(View v, Mensalidade model, int position) {
                TextView txtMesRef = v.findViewById(R.id.txtMesRef);
                TextView txtNomeJoga = v.findViewById(R.id.txtNomeJoga);
                TextView txtVlr = v.findViewById(R.id.txtVlr);
                TextView txtDtPag = v.findViewById(R.id.txtDtPag);

                txtMesRef.setText(model.getMesRef());
                txtVlr.setText(df.format(model.getVlrPago()));
                txtDtPag.setText(model.getDtDia()+"/"+model.getDtMes()+"/"+model.getDtAno());

                final String key = model.getKeyJoga();
                jogadores.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Jogador cadTemp = dataSnapshot.getValue(Jogador.class);
                            txtNomeJoga.setText(cadTemp.getNome());
                        }
                    }
                    @Override
                    public void onCancelled (DatabaseError databaseError){
                        txtNomeJoga.setText("Erro");
                    }
                });
            }
        };
        listCadMensal.setAdapter(listAdapterMensalidades);
    }
}