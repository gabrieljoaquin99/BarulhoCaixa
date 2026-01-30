package com.example.barulho;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class CadJogoActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogos = root.child(MainActivity.JOGOS_KEY);
    DatabaseReference presencas = root.child(MainActivity.PRESENCAS_KEY);

    FirebaseListAdapter<Jogo> listAdapterJogos;

    ListView listCadJogo;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_jogo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listCadJogo = findViewById(R.id.listCadJogo);

        listAdapterJogos = new FirebaseListAdapter<Jogo>(this, Jogo.class,
                R.layout.jogo_item, jogos) {
            @Override
            protected void populateView(View v, Jogo model, int position) {
                TextView txtSeq = v.findViewById(R.id.txtSeq);
                TextView txtData = v.findViewById(R.id.txtData);
                TextView txtLocal = v.findViewById(R.id.txtLocal);
                TextView txtResult = v.findViewById(R.id.txtResult);
                TextView txtMand = v.findViewById(R.id.txtMand);
                TextView txtVis = v.findViewById(R.id.txtVis);
                TextView txtPlacMand = v.findViewById(R.id.txtPlacMand);
                TextView txtPlacVis = v.findViewById(R.id.txtPlacVis);

                //String keyJoga = model.getWhats();

                txtSeq.setText(Integer.toString(model.getSeq()));
                txtData.setText(model.getDtDia() + "/" + model.getDtMes() + "/" + model.getDtAno());
                txtPlacMand.setText(Integer.toString(model.getGolsMand()));
                txtPlacVis.setText(Integer.toString(model.getGolsVis()));

                if (model.getLocal() == 0) {
                    txtLocal.setText("Casa");
                    txtMand.setText("Barulho");
                    txtVis.setText(model.getAdversario());
                } else {
                    txtLocal.setText("Fora");
                    txtMand.setText(model.getAdversario());
                    txtVis.setText("Barulho");
                }

                switch (model.getResultado()) {
                    case 0:
                        txtResult.setText("Derrota");
                        txtResult.setTextColor(Color.RED);
                        break;

                    case 1:
                        txtResult.setText("Empate");
                        txtResult.setTextColor(Color.YELLOW);
                        break;

                    case 2:
                        txtResult.setText("Vitória");
                        txtResult.setTextColor(Color.GREEN);
                        break;
                }
            }
        };
        listCadJogo.setAdapter(listAdapterJogos);

        listCadJogo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference item = listAdapterJogos.getRef(position);
                String jogoSelecKey = item.getKey();
                Jogo jogoSelec = listAdapterJogos.getItem(position);

                Intent it = new Intent(getBaseContext(), CadJogoSelecActivity.class);
                it.putExtra("jogoSelecKey", jogoSelecKey);
                it.putExtra("jogoSelec", jogoSelec);
                startActivity(it);
            }
        });

        listCadJogo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference item = listAdapterJogos.getRef(position);

                String key = item.getKey();

                AlertDialog.Builder builder = new AlertDialog.Builder(CadJogoActivity.this);
                builder.setTitle("DELETAR JOGO");
                builder.setMessage("Deseja deletar o jogo?");
                builder.setPositiveButton("SIM", (dialog, which) -> {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("presencas");
                    databaseReference.orderByChild("keyJogo").equalTo(key)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String keyPres = snapshot.getKey();
                                        presencas.child(keyPres).removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    printMessage("Erro ao acessar o Firebase");
                                }
                            });

                    jogos.child(key).removeValue();

                    printMessage("Jogo deletado!");
                });
                builder.setNegativeButton("NÃO", (dialog, which) -> {

                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

    }

    public void btnCadAssocAdd(View v){
        Intent it = new Intent(getBaseContext(), AddJogoActivity.class);
        startActivity(it);
    }
}