package com.example.barulho;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ResumoTempActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogadores = root.child(MainActivity.JOGADORES_KEY);
    DatabaseReference jogos = root.child(MainActivity.JOGOS_KEY);
    DatabaseReference presencas = root.child(MainActivity.PRESENCAS_KEY);

    Spinner spnResumoTempAno;
    ListView listResumoTempGols, listResumoTempAssis;
    TextView txtResumoTempJogos, txtResumoTempVit, txtResumoTempEmp, txtResumoTempDer,
            txtResumoTempGolsF, txtResumoTempGolsS, txtResumoTempSaldoGols;

    LocalDate dataAtual = LocalDate.now();

    int contJogos, contVit, contEmp, contDer, contGolsF, contGolsS, contSaldoGols;
    int temporadaSelec = dataAtual.getYear();

    List<Jogo> jogosTemp = new ArrayList<>();
    List<Jogador> jogadoresTemp = new ArrayList<>();
    List<Presenca> presencasTemp = new ArrayList<>();
    List<String> temporadas = new ArrayList<>();

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resumo_temp);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        spnResumoTempAno = findViewById(R.id.spnResumoTempAno);
        txtResumoTempJogos = findViewById(R.id.txtResumoTempJogos);
        txtResumoTempVit = findViewById(R.id.txtResumoTempVit);
        txtResumoTempEmp = findViewById(R.id.txtResumoTempEmp);
        txtResumoTempDer = findViewById(R.id.txtResumoTempDer);
        txtResumoTempGolsF = findViewById(R.id.txtResumoTempGolsF);
        txtResumoTempGolsS = findViewById(R.id.txtResumoTempGolsS);
        txtResumoTempSaldoGols = findViewById(R.id.txtResumoTempSaldoGols);

        listResumoTempGols = findViewById(R.id.listResumoTempGols);
        listResumoTempAssis = findViewById(R.id.listResumoTempAssis);

        jogadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Jogador value = snapshot.getValue(Jogador.class);
                    jogadoresTemp.add(value);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        presencas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Presenca value = snapshot.getValue(Presenca.class);
                    presencasTemp.add(value);
                }
                attAdapter();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        jogos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Jogo value = snapshot.getValue(Jogo.class);
                    jogosTemp.add(value);

                    String ano = Integer.toString(value.getDtAno());
                    if (!temporadas.contains(ano)) {
                        temporadas.add(ano);
                    }
                }
                Collections.sort(temporadas, Collections.reverseOrder());

                ArrayAdapter<String> adapterSpnMeses = new ArrayAdapter<>(ResumoTempActivity.this, android.R.layout.simple_spinner_item, temporadas);
                adapterSpnMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnResumoTempAno.setAdapter(adapterSpnMeses);

                attAdapter();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        attAdapter();
    }

    public void attAdapter(){
        contJogos=0;
        contVit=0;
        contEmp=0;
        contDer=0;
        contGolsF=0;
        contGolsS=0;

        for(int i=0 ; i<jogosTemp.size() ; i++){
            if(jogosTemp.get(i).getDtAno() == temporadaSelec){
                contJogos++;
                if(jogosTemp.get(i).getResultado() == 0){
                    contDer++;
                } else {
                    if(jogosTemp.get(i).getResultado() == 1){
                        contEmp++;
                    } else {
                        contVit++;
                    }
                }

                if (jogosTemp.get(i).getLocal() == 0){
                    contGolsF = contGolsF + jogosTemp.get(i).getGolsMand();
                    contGolsS = contGolsS + jogosTemp.get(i).getGolsVis();
                } else {
                    contGolsS = contGolsS + jogosTemp.get(i).getGolsMand();
                    contGolsF = contGolsF + jogosTemp.get(i).getGolsVis();
                }
            }
        }
        txtResumoTempJogos.setText(Integer.toString(contJogos));
        txtResumoTempVit.setText(Integer.toString(contVit));
        txtResumoTempEmp.setText(Integer.toString(contEmp));
        txtResumoTempDer.setText(Integer.toString(contDer));
        txtResumoTempGolsF.setText(Integer.toString(contGolsF));
        txtResumoTempGolsS.setText(Integer.toString(contGolsS));
        txtResumoTempSaldoGols.setText(Integer.toString(contGolsF-contGolsS));

        for(int j=0 ; j<jogadoresTemp.size() ; j++){
            jogadoresTemp.get(j).setContGols(0);
            jogadoresTemp.get(j).setContAssist(0);
        }

        for(int i=0 ; i<presencasTemp.size() ; i++){
            if(presencasTemp.get(i).getDtAno() == temporadaSelec){
                String keyJogatemp = presencasTemp.get(i).getKeyJogador();

                for(int j=0 ; j<jogadoresTemp.size() ; j++){
                    if(jogadoresTemp.get(j).getWhats().contentEquals(keyJogatemp)){
                        jogadoresTemp.get(j).setContGols(jogadoresTemp.get(j).getContGols() +
                                presencasTemp.get(i).getGols());
                        jogadoresTemp.get(j).setContAssist(jogadoresTemp.get(j).getContAssist() +
                                presencasTemp.get(i).getAssist());
                    }
                }
            }
        }

        List<Jogador> sortedByGoals = jogadoresTemp.stream()
                .sorted(Comparator.comparingInt(Jogador::getContGols).reversed())
                .collect(Collectors.toList());

        JogaGolsAdapter jogaGolsAdpt = new JogaGolsAdapter(sortedByGoals, getBaseContext());
        listResumoTempGols.setAdapter(jogaGolsAdpt);
        jogaGolsAdpt.notifyDataSetChanged();

        List<Jogador> sortedByAssis = jogadoresTemp.stream()
                .sorted(Comparator.comparingInt(Jogador::getContAssist).reversed())
                .collect(Collectors.toList());

        JogaAssisAdapter jogaAssisAdpt = new JogaAssisAdapter(sortedByAssis, getBaseContext());
        listResumoTempAssis.setAdapter(jogaAssisAdpt);
        jogaAssisAdpt.notifyDataSetChanged();
    }

    public void btnResumoTempAnoAtt(View v){
        temporadaSelec = Integer.parseInt(spnResumoTempAno.getSelectedItem().toString());
        attAdapter();
    }
}