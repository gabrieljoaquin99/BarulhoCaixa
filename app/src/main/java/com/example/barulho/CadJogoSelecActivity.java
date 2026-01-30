package com.example.barulho;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class CadJogoSelecActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogos = root.child(MainActivity.JOGOS_KEY);

    EditText txtCadJogoSelecNum, txtCadJogoSelecAdv, txtCadJogoSelecPlacMand, txtCadJogoSelecPlacVis;
    RadioButton rdbtnCadJogoSelecCasa, rdbtnCadJogoSelecFora;
    DatePicker dtpCadJogoSelecData;
    Button btnCadJogoSelecAtt;

    String jogoSelecKey;
    Jogo jogoSelec;
    int liberaCampos = 0;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_jogo_selec);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCadJogoSelecNum = findViewById(R.id.txtCadJogoSelecNum);
        txtCadJogoSelecAdv = findViewById(R.id.txtCadJogoSelecAdv);
        txtCadJogoSelecPlacMand = findViewById(R.id.txtCadJogoSelecPlacMand);
        txtCadJogoSelecPlacVis = findViewById(R.id.txtCadJogoSelecPlacVis);
        rdbtnCadJogoSelecCasa = findViewById(R.id.rdbtnCadJogoSelecCasa);
        rdbtnCadJogoSelecFora = findViewById(R.id.rdbtnCadJogoSelecFora);
        dtpCadJogoSelecData = findViewById(R.id.dtpCadJogoSelecData);
        btnCadJogoSelecAtt = findViewById(R.id.btnCadJogoSelecAtt);

        txtCadJogoSelecNum.setEnabled(false);
        txtCadJogoSelecAdv.setEnabled(false);
        txtCadJogoSelecPlacMand.setEnabled(false);
        txtCadJogoSelecPlacVis.setEnabled(false);
        rdbtnCadJogoSelecCasa.setEnabled(false);
        rdbtnCadJogoSelecFora.setEnabled(false);
        dtpCadJogoSelecData.setEnabled(false);

        Intent it = getIntent();
        jogoSelecKey = (String) it.getSerializableExtra("jogoSelecKey");
        jogoSelec = (Jogo) it.getSerializableExtra("jogoSelec");

        txtCadJogoSelecNum.setText(Integer.toString(jogoSelec.getSeq()));
        txtCadJogoSelecAdv.setText(jogoSelec.getAdversario());
        txtCadJogoSelecPlacMand.setText(Integer.toString(jogoSelec.getGolsMand()));
        txtCadJogoSelecPlacVis.setText(Integer.toString(jogoSelec.getGolsVis()));

        if (jogoSelec.getLocal()==0){
            rdbtnCadJogoSelecCasa.setChecked(true);
        } else {
            rdbtnCadJogoSelecFora.setChecked(true);
        }

        dtpCadJogoSelecData.updateDate(jogoSelec.getDtAno(), jogoSelec.getDtMes(), jogoSelec.getDtDia());

    }

    public void btnCadJogoSelecAtt(View v){
        if (liberaCampos == 0){
            txtCadJogoSelecAdv.setEnabled(true);
            txtCadJogoSelecPlacMand.setEnabled(true);
            txtCadJogoSelecPlacVis.setEnabled(true);
            rdbtnCadJogoSelecCasa.setEnabled(true);
            rdbtnCadJogoSelecFora.setEnabled(true);
            dtpCadJogoSelecData.setEnabled(true);
            btnCadJogoSelecAtt.setText("ATUALIZAR JOGO");

            liberaCampos=1;
        } else {
            if(txtCadJogoSelecAdv.getText().toString().contentEquals("")){
                printMessage("Insira o Adversario...");
            } else {
                Jogo jogo = jogoSelec;

                jogo.setSeq(Integer.parseInt(txtCadJogoSelecNum.getText().toString()));
                jogo.setDtDia(dtpCadJogoSelecData.getDayOfMonth());
                jogo.setDtMes(dtpCadJogoSelecData.getMonth());
                jogo.setDtAno(dtpCadJogoSelecData.getYear());

                jogo.setAdversario(txtCadJogoSelecAdv.getText().toString());
                jogo.setGolsMand(Integer.parseInt(txtCadJogoSelecPlacMand.getText().toString()));
                jogo.setGolsVis(Integer.parseInt(txtCadJogoSelecPlacVis.getText().toString()));

                if(rdbtnCadJogoSelecCasa.isChecked()){
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

                jogos.child(jogoSelecKey).setValue(jogo);
                printMessage("Jogo atualizado!");

                liberaCampos = 0;

                txtCadJogoSelecAdv.setText("");
                txtCadJogoSelecPlacMand.setText("");
                txtCadJogoSelecPlacVis.setText("");
            }
        }

    }

    public void btnCadJogoSelecCadPres(View v){
        Intent it = new Intent(getBaseContext(), CadPresencaJogoActivity.class);
        it.putExtra("jogoSelecKey", jogoSelecKey);
        it.putExtra("jogoSelecAno", jogoSelec.getDtAno());
        startActivity(it);
    }
}