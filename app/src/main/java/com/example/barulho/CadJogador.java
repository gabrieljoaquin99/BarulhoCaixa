package com.example.barulho;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
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

import java.time.LocalDate;

public class CadJogador extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference jogadores = root.child(MainActivity.JOGADORES_KEY);

    FirebaseListAdapter<Jogador> listAdapterJogadores;

    String[] meses = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
            "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};

    LocalDate dataAtual = LocalDate.now();
    String mesRefAtual = meses[dataAtual.getMonthValue()-1]+"/"+dataAtual.getYear();

    ListView listCadJoga;
    EditText txtCadJogaNome, txtCadJogaWhats, txtCadJogaSenha;
    ToggleButton tgbtnCadJogaAtivo, tgbtnCadJogaNivel;
    Button btnCadJogaAdd;

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_jogador);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtCadJogaNome = findViewById(R.id.txtCadJogaNome);
        txtCadJogaWhats = findViewById(R.id.txtCadJogaWhats);
        txtCadJogaSenha = findViewById(R.id.txtCadJogaSenha);
        tgbtnCadJogaAtivo = findViewById(R.id.tgbtnCadJogaAtivo);
        tgbtnCadJogaNivel = findViewById(R.id.tgbtnCadJogaNivel);
        btnCadJogaAdd = findViewById(R.id.btnCadJogaAdd);

        txtCadJogaNome.setText("");
        txtCadJogaWhats.setText("519");
        txtCadJogaSenha.setText("");

        listCadJoga = findViewById(R.id.listCadJoga);

        listAdapterJogadores = new FirebaseListAdapter<Jogador>(this, Jogador.class,
                R.layout.jogador_item, jogadores) {
            @Override
            protected void populateView(View v, Jogador model, int position) {
                TextView txtNome = v.findViewById(R.id.txtNome);
                TextView txtWhatsApp = v.findViewById(R.id.txtWhatsApp);
                TextView txtAtivo = v.findViewById(R.id.txtAtivo);
                TextView txtStatus = v.findViewById(R.id.txtStatus);

                txtStatus.setText("");

                String keyJoga = model.getWhats();

                txtNome.setText(model.getNome());
                txtWhatsApp.setText(model.getWhats());

                if (model.getAtivo() == 0){
                    txtAtivo.setText("INATIVO");
                } else {
                    txtAtivo.setText("ATIVO");
                }

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("mensalidades");
                databaseReference.orderByChild("mesRef").equalTo(mesRefAtual)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean exists = false;
                                double contVlr = 0;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String jogaEncontrado = snapshot.child("keyJoga").getValue(String.class);
                                    if (keyJoga != null && keyJoga.equals(jogaEncontrado)) {
                                        exists = true;
                                        contVlr = contVlr + snapshot.child("vlrPago").getValue(double.class);
                                        //break;
                                    }
                                }

                                if (exists) {
                                    if(contVlr >= 50){
                                        txtStatus.setText("Adimplente");
                                        txtStatus.setTextColor(Color.GREEN);
                                    } else{
                                        txtStatus.setText("Pag. Parcial ("+contVlr+")");
                                        txtStatus.setTextColor(Color.MAGENTA);
                                    }

                                } else {
                                    txtStatus.setText("Inadimplente");
                                    txtStatus.setTextColor(Color.RED);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                printMessage("Erro ao acessar o Firebase");
                            }
                        });
            }
        };
        listCadJoga.setAdapter(listAdapterJogadores);

        listCadJoga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference item = listAdapterJogadores.getRef(position);

                String jogaSelec = listAdapterJogadores.getItem(position).getWhats();
                Intent it = new Intent(getBaseContext(), AddMensalActivity.class);
                it.putExtra("jogaSelec", jogaSelec);
                startActivity(it);
            }
        });

        listCadJoga.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference item = listAdapterJogadores.getRef(position);

                String key = item.getKey();

                Jogador jogaSelec = listAdapterJogadores.getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(CadJogador.this);
                builder.setTitle("DELETAR/ALTERAR JOGADOR");
                builder.setMessage("Deseja deletar ou alterar o Jogador?");
                builder.setPositiveButton("DELETAR", (dialog, which) -> {
                    jogadores.child(key).removeValue();

                    printMessage("Cadastro do associado deletado!");
                });
                builder.setNegativeButton("ALTERAR", (dialog, which) -> {
                    txtCadJogaNome.setText(jogaSelec.getNome());
                    txtCadJogaWhats.setText(jogaSelec.getWhats());
                    txtCadJogaSenha.setText(jogaSelec.getSenha());

                    if(jogaSelec.getNivel() == 0){
                        tgbtnCadJogaNivel.setChecked(false);
                    }else{
                        tgbtnCadJogaNivel.setChecked(true);
                    }

                    if(jogaSelec.getAtivo() == 0){
                        tgbtnCadJogaAtivo.setChecked(false);
                    }else{
                        tgbtnCadJogaAtivo.setChecked(true);
                    }

                    btnCadJogaAdd.setText("ATUALIZAR JOGADOR");
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

    }

    public void btnCadJogaAdd(View v){
        if((txtCadJogaNome.getText().toString().contentEquals("")) |
           (txtCadJogaWhats.getText().toString().contentEquals("")) |
           (txtCadJogaSenha.getText().toString().contentEquals(""))){
            printMessage("Preencha todos os dados...");
        } else {
            Jogador joga = new Jogador();

            joga.setNome(txtCadJogaNome.getText().toString());
            joga.setWhats(txtCadJogaWhats.getText().toString());
            joga.setSenha(txtCadJogaSenha.getText().toString());
            joga.setContJogos(0);
            joga.setContGols(0);
            joga.setContAssist(0);

            if (tgbtnCadJogaAtivo.getText().toString().contentEquals("INATIVO")) {
                joga.setAtivo(0);
            } else {
                joga.setAtivo(1);
            }

            if (tgbtnCadJogaNivel.getText().toString().contentEquals("NORMAL")) {
                joga.setNivel(0);
            } else {
                joga.setNivel(1);
            }

            if(btnCadJogaAdd.getText().toString().contentEquals("ATUALIZAR JOGADOR")){
                String key = joga.getWhats();
                jogadores.child(key).setValue(joga);
                btnCadJogaAdd.setText("ADICIONAR JOGADOR");
                printMessage("Jogador atualizado...");
            } else{
                final String key = joga.getWhats();
                jogadores.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            printMessage("Jogador já cadastrado, verifique com o Administrador...");
                        } else {
                            jogadores.child(key).setValue(joga);

                            printMessage("Jogador cadastrado...");
                        }
                    }
                    @Override
                    public void onCancelled (DatabaseError databaseError){
                        printMessage("Erro ao verificar se o Associado já existe: " + databaseError.getMessage());
                    }
                });
            }

            txtCadJogaNome.setText("");
            txtCadJogaWhats.setText("519");
            txtCadJogaSenha.setText("");
        }
    }
}