package br.com.jonascosta.projetovistoria;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class VeiculoEntryActivity extends AppCompatActivity {

    private static int id;
    private boolean editado = false;
    private EditText nome;
    private EditText cpf ;
    private EditText endereco;
    private EditText marca;
    private EditText modelo;
    private EditText placa;
    private CheckBox vistoria;
    private Button btnConcluir;
    private Button btnExcluir;
    private Button btnLimpaCampos;
    private EditText observacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiculo_entry);

        if (this.getIntent().hasExtra("ID")){
            editado = true;
            this.id = this.getIntent().getIntExtra("ID", -1);

            this.showFields( id );
        } else
            editado = false;

        btnConcluir = (Button) findViewById(R.id.btConc);
        btnExcluir = (Button) findViewById(R.id.btExc);
        btnLimpaCampos = (Button) findViewById(R.id.btLimpar);

        btnExcluir.setVisibility((editado == true ? View.VISIBLE : View.INVISIBLE)); //Só mostra se for alteração

        //Concluir
        btnConcluir.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                nome = (EditText) findViewById(R.id.txtNome);
                cpf = (EditText) findViewById(R.id.txtCPF);
                endereco = (EditText) findViewById(R.id.txtEnd);
                marca = (EditText) findViewById(R.id.txtMarca);
                modelo = (EditText) findViewById(R.id.txtModelo);
                placa = (EditText) findViewById(R.id.txtPlaca);
                vistoria = (CheckBox) findViewById(R.id.swOK);
                observacoes = (EditText) findViewById(R.id.idObservacoes);


                if (!isFieldEmpty(nome, cpf, marca, modelo, placa, observacoes)) {
                    saveRecord(nome, cpf, endereco, marca, modelo, placa, vistoria, observacoes);
                    finish();
                }
            }

            private void saveRecord(EditText nome, EditText cpf, EditText endereco, EditText marca, EditText modelo,
                                    EditText placa, CheckBox vistoria, EditText observacoes) {
                if (!editado) {
                    VeiculoActivity.getVeiculoDb().salvar(
                            placa.getText().toString(),
                            marca.getText().toString(),
                            modelo.getText().toString(),
                            nome.getText().toString(),
                            cpf.getText().toString(),
                            endereco.getText().toString(),
                            observacoes.getText().toString(),
                            (vistoria.isChecked() ? true : false));

                } else {
                    VeiculoActivity.getVeiculoDb().editar(String.valueOf(VeiculoEntryActivity.id),
                            placa.getText().toString(),
                            marca.getText().toString(),
                            modelo.getText().toString(),
                            nome.getText().toString(),
                            cpf.getText().toString(),
                            endereco.getText().toString(),
                            observacoes.getText().toString(),
                            (vistoria.isChecked() ? true : false));
                }
            }

            private boolean isFieldEmpty(EditText... fields) {
                boolean empty = false;

                for (EditText field : fields) {
                    if (TextUtils.isEmpty(field.getText().toString())) {
                        empty = true;
                        field.setError("Campo deve ser preenchido!");
                    }
                }
                return empty;
            }

        });

        //Excluir
        btnExcluir.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeiculoActivity.getVeiculoDb().excluir(String.valueOf(VeiculoEntryActivity.id));
                finish();
            }
        });

        //Limpar
        btnLimpaCampos.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = (EditText) findViewById(R.id.txtNome);
                cpf = (EditText) findViewById(R.id.txtCPF);
                endereco = (EditText) findViewById(R.id.txtEnd);
                marca = (EditText) findViewById(R.id.txtMarca);
                modelo = (EditText) findViewById(R.id.txtModelo);
                placa = (EditText) findViewById(R.id.txtPlaca);
                vistoria = (CheckBox) findViewById(R.id.swOK);
                observacoes = (EditText) findViewById(R.id.idObservacoes);


                nome.setText("");
                cpf.setText("");
                endereco.setText("");
                marca.setText("");
                modelo.setText("");
                placa.setText("");
                observacoes.setText("");
                vistoria.setChecked(false);

            }
        });

}

    private void showFields(int id) {
        Cursor veiculo = VeiculoActivity.getVeiculoDb().getVeiculo(id);

        nome     = (EditText) findViewById(R.id.txtNome);
        cpf      = (EditText) findViewById(R.id.txtCPF);
        endereco = (EditText) findViewById(R.id.txtEnd);
        marca    = (EditText) findViewById(R.id.txtMarca);
        modelo   = (EditText) findViewById(R.id.txtModelo);
        placa    = (EditText) findViewById(R.id.txtPlaca);
        vistoria = (CheckBox) findViewById(R.id.swOK);
        observacoes = (EditText) findViewById(R.id.idObservacoes);


        nome.setText(veiculo.getString(veiculo.getColumnIndex("nome")));
        cpf.setText(veiculo.getString(veiculo.getColumnIndex("cpf")));
        endereco.setText(veiculo.getString(veiculo.getColumnIndex("endereco")));
        marca.setText(veiculo.getString(veiculo.getColumnIndex("marca")));
        modelo.setText(veiculo.getString(veiculo.getColumnIndex("modelo")));
        placa.setText(veiculo.getString(veiculo.getColumnIndex("placa")));
        observacoes.setText(veiculo.getString(veiculo.getColumnIndex("observacao")));
        vistoria.setChecked(veiculo.getString(veiculo.getColumnIndex("vistoria")).equals("1") ? true : false);

    }

}
