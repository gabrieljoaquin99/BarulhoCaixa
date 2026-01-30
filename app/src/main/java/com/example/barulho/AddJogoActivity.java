package com.example.barulho;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class AddJogoActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogos = root.child(MainActivity.JOGOS_KEY);

    EditText txtAddJogoAddNum, txtAddJogoAddAdv, txtAddJogoAddPlacMand, txtAddJogoAddPlacVis;
    RadioButton rdbtnAddJogoAddCasa, rdbtnAddJogoAddFora;
    DatePicker dtpAddJogoAddData;

    int seqTmp;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_jogo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtAddJogoAddNum = findViewById(R.id.txtAddJogoAddNum);
        txtAddJogoAddAdv = findViewById(R.id.txtAddJogoAddAdv);
        txtAddJogoAddPlacMand = findViewById(R.id.txtAddJogoAddPlacMand);
        txtAddJogoAddPlacVis = findViewById(R.id.txtAddJogoAddPlacVis);
        rdbtnAddJogoAddCasa = findViewById(R.id.rdbtnAddJogoAddCasa);
        rdbtnAddJogoAddFora = findViewById(R.id.rdbtnAddJogoAddFora);
        dtpAddJogoAddData = findViewById(R.id.dtpAddJogoAddData);

        txtAddJogoAddNum.setText("");
        txtAddJogoAddAdv.setText("");
        txtAddJogoAddPlacMand.setText("");
        txtAddJogoAddPlacVis.setText("");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("jogos");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long qtd = dataSnapshot.getChildrenCount();

                seqTmp = (int) qtd + 1;
                txtAddJogoAddNum.setText(Integer.toString(seqTmp));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void btnAddAssocAdd(View v){
        if(txtAddJogoAddAdv.getText().toString().contentEquals("")){
            printMessage("Insira o Adversario...");
        } else {
            int seqNumTmp = Integer.parseInt(txtAddJogoAddNum.getText().toString());
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("jogos");
            databaseReference.orderByChild("seq").equalTo(seqNumTmp)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean exists = false;

                            if (dataSnapshot.exists()) {
                                printMessage("Já existe um Jogo para esse N°...");
                            } else {
                                Jogo jogo = new Jogo();

                                jogo.setSeq(seqNumTmp);
                                jogo.setDtDia(dtpAddJogoAddData.getDayOfMonth());
                                jogo.setDtMes(dtpAddJogoAddData.getMonth()+1);
                                jogo.setDtAno(dtpAddJogoAddData.getYear());

                                jogo.setAdversario(txtAddJogoAddAdv.getText().toString());
                                jogo.setGolsMand(Integer.parseInt(txtAddJogoAddPlacMand.getText().toString()));
                                jogo.setGolsVis(Integer.parseInt(txtAddJogoAddPlacVis.getText().toString()));

                                if(rdbtnAddJogoAddCasa.isChecked()){
                                    jogo.setLocal(0);
                                    if(jogo.getGolsMand() > jogo.getGolsVis()){
                                        jogo.setResultado(2);
                                    } else {
                                        if(jogo.getGolsMand() == jogo.getGolsVis()){
                                            jogo.setResultado(1);
                                        } else {
                                            jogo.setResultado(0);
                                        }
                                    }
                                } else {
                                    jogo.setLocal(1);
                                    if(jogo.getGolsMand() > jogo.getGolsVis()){
                                        jogo.setResultado(0);
                                    } else {
                                        if(jogo.getGolsMand() == jogo.getGolsVis()){
                                            jogo.setResultado(1);
                                        } else {
                                            jogo.setResultado(2);
                                        }
                                    }
                                }

                                String key = jogos.push().getKey();
                                jogos.child(key).setValue(jogo);

                                printMessage("Jogo adicionado!");

                                txtAddJogoAddAdv.setText("");
                                txtAddJogoAddPlacMand.setText("");
                                txtAddJogoAddPlacVis.setText("");

                                seqTmp = seqTmp+1;
                                txtAddJogoAddNum.setText(Integer.toString(seqTmp));
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