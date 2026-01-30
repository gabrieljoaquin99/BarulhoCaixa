package com.example.barulho;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumoFinActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference lancamentos = root.child(MainActivity.LANCAMENTOS_KEY);
    DatabaseReference mensalidades = root.child(MainActivity.MENSALIDADES_KEY);

    FirebaseListAdapter<LancamentoFin> listAdapterResumoFinLanc;

    LocalDate dataAtual = LocalDate.now();

    ListView listResumoFinLanc;
    TextView txtResumoFinRecMes, txtResumoFinDespMes, txtResumoFinSaldoMes,
            txtResumoFinRecAno, txtResumoFinDespAno, txtResumoFinSaldoAno,
            txtResumoFinMensalMesQtd, txtResumoFinMensalMesVlr, txtResumoFinSaldoAtu;
    Spinner spnResumoFinMes;
    Button btnResumoFinMesAtt;

    String[] meses = {"1 - Janeiro", "2 - Fevereiro", "3 - Março", "4 - Abril", "5 - Maio", "6 - Junho",
            "7 - Julho", "8 - Agosto", "9 - Setembro", "10 - Outubro", "11 - Novembro", "12 - Dezembro"};

    int mesSelec, anoSelec;

    double recMes=0, despMes=0, recAno=0, despAno=0, mensalVlr=0, mensalVlrAno=0,
            recTotal, despTotal, mensalTotal;
    int mensalQtd=0;

    List<Mensalidade> mensalTmp = new ArrayList<Mensalidade>();
    List<LancamentoFin> lancsTmp = new ArrayList<LancamentoFin>();

    DecimalFormat df = new DecimalFormat("R$ #,##0.00");

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resumo_fin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listResumoFinLanc = findViewById(R.id.listResumoFinLanc);
        txtResumoFinRecMes = findViewById(R.id.txtResumoFinRecMes);
        txtResumoFinDespMes = findViewById(R.id.txtResumoFinDespMes);
        txtResumoFinSaldoMes = findViewById(R.id.txtResumoFinSaldoMes);
        txtResumoFinRecAno = findViewById(R.id.txtResumoFinRecAno);
        txtResumoFinDespAno = findViewById(R.id.txtResumoFinDespAno);
        txtResumoFinSaldoAno = findViewById(R.id.txtResumoFinSaldoAno);
        txtResumoFinMensalMesQtd = findViewById(R.id.txtResumoFinMensalMesQtd);
        txtResumoFinMensalMesVlr = findViewById(R.id.txtResumoFinMensalMesVlr);
        txtResumoFinSaldoAtu = findViewById(R.id.txtResumoFinSaldoAtu);
        spnResumoFinMes = findViewById(R.id.spnResumoFinMes);
        btnResumoFinMesAtt = findViewById(R.id.btnResumoFinMesAtt);

        mesSelec = dataAtual.getMonthValue();
        anoSelec = dataAtual.getYear();

        ArrayAdapter<String> adapterSpnMeses = new ArrayAdapter<>(ResumoFinActivity.this, android.R.layout.simple_spinner_item, meses);
        adapterSpnMeses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnResumoFinMes.setAdapter(adapterSpnMeses);
        spnResumoFinMes.setSelection(mesSelec-1);

        mensalidades.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mensalidade mensalSelec = snapshot.getValue(Mensalidade.class);
                    mensalTmp.add(mensalSelec);
                }

                attCampos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        lancamentos.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LancamentoFin lancFinSelec = snapshot.getValue(LancamentoFin.class);
                    lancsTmp.add(lancFinSelec);
                }

                attCampos();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    public void delVlr() {
        recMes = 0;
        despMes = 0;
        recAno = 0;
        despAno = 0;
        mensalVlr = 0;
        mensalVlrAno = 0;
        mensalQtd = 0;
        recTotal = 0;
        despTotal = 0;
        mensalTotal = 0;
    }

    public void attCampos() {
        delVlr();

        Query queryLancFinMesSelec = FirebaseDatabase.getInstance().getReference()
                .child("lancamentos")
                .orderByChild("dtMes")
                .equalTo(mesSelec);

        listAdapterResumoFinLanc = new FirebaseListAdapter<LancamentoFin>(this, LancamentoFin.class,
                R.layout.lancamento_item, queryLancFinMesSelec) {
            @Override
            protected void populateView(View v, LancamentoFin model, int position) {
                TextView txtLancDesc = v.findViewById(R.id.txtLancDesc);
                TextView txtLancVlr = v.findViewById(R.id.txtLancVlr);
                TextView txtLancDtPag = v.findViewById(R.id.txtLancDtPag);
                TextView txtLancTipo = v.findViewById(R.id.txtLancTipo);

                txtLancDesc.setText(model.getDescricao());
                txtLancVlr.setText(df.format(model.getVlrPago()));
                txtLancDtPag.setText(model.getDtDia() + "/" + model.getDtMes() + "/" + model.getDtAno());

                if (model.getTipo() == 0) {
                    txtLancTipo.setText("Despesa");
                    txtLancTipo.setTextColor(Color.RED);
                } else {
                    txtLancTipo.setText("Receita");
                    txtLancTipo.setTextColor(Color.GREEN);
                }
            }
        };
        listResumoFinLanc.setAdapter(listAdapterResumoFinLanc);

        for(int i=0; i<mensalTmp.size(); i++){
            if(mensalTmp.get(i).getDtAno() == anoSelec){
                if(mensalTmp.get(i).getDtMes() == mesSelec){
                    mensalQtd++;
                    mensalVlr = mensalVlr + mensalTmp.get(i).getVlrPago();
                }
                mensalVlrAno = mensalVlrAno + mensalTmp.get(i).getVlrPago();
            }
            mensalTotal = mensalTotal + mensalTmp.get(i).getVlrPago();
        }

        for(int i=0; i<lancsTmp.size(); i++){
            switch (lancsTmp.get(i).getTipo()){
                case 0:
                    if(lancsTmp.get(i).getDtAno() == anoSelec) {
                        if (lancsTmp.get(i).getDtMes() == mesSelec) {
                            despMes = despMes + lancsTmp.get(i).getVlrPago();
                        }
                        despAno = despAno + lancsTmp.get(i).getVlrPago();
                    }
                    despTotal = despTotal + lancsTmp.get(i).getVlrPago();
                break;

                case 1:
                    if(lancsTmp.get(i).getDtAno() == anoSelec) {
                        if (lancsTmp.get(i).getDtMes() == mesSelec) {
                            recMes = recMes + lancsTmp.get(i).getVlrPago();
                        }
                        recAno = recAno + lancsTmp.get(i).getVlrPago();
                    }
                    recTotal = recTotal + lancsTmp.get(i).getVlrPago();
                break;
            }
        }

        double saldoMes = (recMes + mensalVlr) - despMes;
        recAno = recAno + mensalVlrAno;
        double saldoAno = recAno - despAno;
        double saldoAtual = (recTotal + mensalTotal) - despTotal;

        txtResumoFinMensalMesQtd.setText(Integer.toString(mensalQtd));
        txtResumoFinMensalMesVlr.setText(df.format(mensalVlr));
        txtResumoFinRecMes.setText(df.format(recMes));
        txtResumoFinDespMes.setText(df.format(despMes));
        txtResumoFinSaldoMes.setText(df.format(saldoMes));
        txtResumoFinRecAno.setText(df.format(recAno));
        txtResumoFinDespAno.setText(df.format(despAno));
        txtResumoFinSaldoAno.setText(df.format(saldoAno));
        txtResumoFinSaldoAtu.setText(df.format(saldoAtual));
    }

    public void btnResumoFinMesAtt (View v){
        String mesSelecTmp = spnResumoFinMes.getSelectedItem().toString();
        int posicaoHifen = mesSelecTmp.indexOf(" - ");
        mesSelec = Integer.parseInt(mesSelecTmp.substring(0, posicaoHifen));

        delVlr();
        attCampos();
    }
}