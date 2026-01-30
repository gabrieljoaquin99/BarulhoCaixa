package com.example.barulho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class JogaAssisAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<Jogador> jogaTemp;

    public JogaAssisAdapter(List<Jogador> jogaTemp, Context ctx){
        this.jogaTemp = jogaTemp;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return jogaTemp.size();
    }

    @Override
    public Object getItem(int position) {
        return jogaTemp.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.resumo_temp_item, null);
        TextView txtNome = v.findViewById(R.id.txtNome);
        TextView txtNum = v.findViewById(R.id.txtNum);

        Jogador joga = jogaTemp.get(position);

        txtNome.setText(joga.getNome());
        txtNum.setText(Integer.toString(joga.getContAssist()));

        return v;
    }

    public interface ClickItemListView {
        void clickItem(String nomeJoga, int position);

    }

}
