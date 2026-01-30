package com.example.barulho;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class CadLancFinActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference();
    DatabaseReference lancamentos = root.child(MainActivity.LANCAMENTOS_KEY);

    FirebaseListAdapter<LancamentoFin> listAdapterLancamentos;

    ListView listCadLancFin;
    EditText txtCadLancFinAddVlr, txtCadLancFinAddDesc;
    RadioButton rdbtnCadLancFinAddRec, rdbtnCadLancFinAddDesp;
    DatePicker dtpCadLancFinAddDtPag;

    DecimalFormat df = new DecimalFormat("R$ #,##0.00");

    public void printMessage(String m){
        Toast.makeText(this, m, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cad_lanc_fin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listCadLancFin = findViewById(R.id.listCadLancFin);
        txtCadLancFinAddVlr = findViewById(R.id.txtCadLancFinAddVlr);
        txtCadLancFinAddDesc = findViewById(R.id.txtCadLancFinAddDesc);
        rdbtnCadLancFinAddRec = findViewById(R.id.rdbtnCadLancFinAddRec);
        rdbtnCadLancFinAddDesp = findViewById(R.id.rdbtnCadLancFinAddDesp);
        dtpCadLancFinAddDtPag = findViewById(R.id.dtpCadLancFinAddDtPag);

        txtCadLancFinAddVlr.setText("");
        txtCadLancFinAddDesc.setText("");

        listAdapterLancamentos = new FirebaseListAdapter<LancamentoFin>(this, LancamentoFin.class,
                R.layout.lancamento_item, lancamentos) {
            @Override
            protected void populateView(View v, LancamentoFin model, int position) {
                TextView txtLancDesc = v.findViewById(R.id.txtLancDesc);
                TextView txtLancVlr = v.findViewById(R.id.txtLancVlr);
                TextView txtLancDtPag = v.findViewById(R.id.txtLancDtPag);
                TextView txtLancTipo = v.findViewById(R.id.txtLancTipo);

                txtLancDesc.setText(model.getDescricao());
                txtLancVlr.setText(df.format(model.getVlrPago()));
                txtLancDtPag.setText(model.getDtDia()+"/"+model.getDtMes()+"/"+model.getDtAno());

                if(model.getTipo()==0){
                    txtLancTipo.setText("Despesa");
                    txtLancTipo.setTextColor(Color.RED);
                } else {
                    txtLancTipo.setText("Receita");
                    txtLancTipo.setTextColor(Color.GREEN);
                }
            }
        };
        listCadLancFin.setAdapter(listAdapterLancamentos);

        listCadLancFin.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference item = listAdapterLancamentos.getRef(position);
                item.removeValue();

                printMessage("Lançamento Financeiro Removido...");

                return false;
            }
        });
    }

    public void btnCadLancFinAdd(View v){
        if((txtCadLancFinAddDesc.getText().toString().contentEquals(""))&
                (txtCadLancFinAddVlr.getText().toString().contentEquals(""))){
            printMessage("Preencha todos os dados do Lançamento...");
        } else {
            LancamentoFin lancFin = new LancamentoFin();

            lancFin.setDescricao(txtCadLancFinAddDesc.getText().toString());
            lancFin.setVlrPago(Double.parseDouble(txtCadLancFinAddVlr.getText().toString()));

            if (rdbtnCadLancFinAddRec.isChecked()){
                lancFin.setTipo(1);
            } else {
                lancFin.setTipo(0);
            }

            lancFin.setDtDia(dtpCadLancFinAddDtPag.getDayOfMonth());
            lancFin.setDtMes(dtpCadLancFinAddDtPag.getMonth()+1);
            lancFin.setDtAno(dtpCadLancFinAddDtPag.getYear());

            String key = lancamentos.push().getKey();
            lancamentos.child(key).setValue(lancFin);

            txtCadLancFinAddVlr.setText("");
            txtCadLancFinAddDesc.setText("");
        }
    }
}