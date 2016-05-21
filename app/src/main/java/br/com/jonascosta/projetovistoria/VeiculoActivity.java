package br.com.jonascosta.projetovistoria;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class VeiculoActivity extends AppCompatActivity {


    private static final int RETCODE = 100;
    private static DB veiculoDB;
    private ListView listVeiculo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.veiculoDB = new DB(this);
        this.veiculoDB.open();

       /* this.veiculoDB.truncateVeiculo();0
        this.veiculoDB.insertVeiculoSamples();*/

        this.showVeiculos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(0, 0, 0, "Incluir");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case 0:
                Intent i = new Intent(this, VeiculoEntryActivity.class);
                startActivityForResult(i, RETCODE);
                return true;
        }

        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        this.showVeiculos();

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showVeiculos() {
        listVeiculo = (ListView) findViewById(R.id.veiculoView);

        String[] values = this.veiculoDB.getListVeiculos();

        values = adjustValue(values);

        ArrayAdapter<String> studentValues =
                new ArrayAdapter<String>(this,
                        android.R.layout.simple_list_item_1,
                        values);

        listVeiculo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer idVeiculo = DB.pegaPorID(position);

                Intent i = new Intent(view.getContext(), VeiculoEntryActivity.class);

                i.putExtra("ID", idVeiculo);

                startActivityForResult(i, RETCODE);
            }
        });

        listVeiculo.setAdapter(studentValues);
    }

    private String[] adjustValue(String[] values) {
        String [] aux;

        for(int i = 0;i < values.length;i++){
            aux = values[i].split("#");

            values[i] = aux[0] + (aux[1].equals("1") ? "  ===>  OK" : "");
        }

        return values;
    }

    public static DB getVeiculoDb(){
        return veiculoDB;
    }
}
