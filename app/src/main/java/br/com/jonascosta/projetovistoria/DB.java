package br.com.jonascosta.projetovistoria;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DB{
    private static final String NAME_DATABASE = "CadastroVeiculos.db";
    private final VeiculoActivity context;
    private SQLiteDatabase db;
    private static Integer[] ids;

    public DB(VeiculoActivity context){
        this.context = context;
    }

    public void open(){
        this.db = context.getBaseContext().openOrCreateDatabase(
                            NAME_DATABASE,
                            context.MODE_PRIVATE,
                            null);

        this.createVeiculo();
    }

    private void createVeiculo() {
        String sql = "CREATE TABLE if not exists Veiculo ( " +
                "id integer primary key autoincrement, " +
                "placa text not null, " +
                "marca text not null, " +
                "modelo text not null," +
                "nome text not null," +
                "cpf integer not null," +
                "observacao text not null," +
                "endereco text not null," +
                "vistoria boolean not null)";

        this.db.execSQL(sql);
    }

    // Drop/Create Table
    public void truncateVeiculo() {
        String sql = "DROP TABLE if exists Veiculo";
        this.db.execSQL(sql);
        this.createVeiculo();
    }

    public void salvar(String placa, String marca, String modelo, String nome, String cpf, String endereco, String observacao, boolean vistoria) {
        ContentValues values = new ContentValues();
        values.put("placa", placa);
        values.put("marca", marca);
        values.put("modelo", modelo);
        values.put("nome", nome);
        values.put("cpf", cpf);
        values.put("observacao", observacao);
        values.put("endereco", endereco);
        values.put("vistoria", vistoria);


        this.db.insert("Veiculo", null, values);
    }

    public void editar(String id, String placa, String marca, String modelo, String nome, String cpf, String endereco, String observacao, boolean vistoria) {
        ContentValues values = new ContentValues();
        values.put("placa", placa);
        values.put("marca", marca);
        values.put("modelo", modelo);
        values.put("nome", nome);
        values.put("cpf", cpf);
        values.put("observacao", observacao);
        values.put("endereco", endereco);
        values.put("vistoria", vistoria);


        db.update("Veiculo", values, "id = ?", new String[]{id});
    }

    public void excluir(String id) {
        db.delete("Veiculo", "id = ?", new String[]{id});
    }

    public String[] getListVeiculos() {
        String fields = "placa, marca, modelo, id, vistoria";
        String sql = "SELECT " + fields + " FROM Veiculo";
        Cursor ret =  this.db.rawQuery(sql, null);

        String[] values = new String[ret.getCount()];
        ids = new Integer[ret.getCount()];

        boolean b = ret.moveToFirst();
        int i = 0;

        while (b){
            ids[i] = ret.getInt(ret.getColumnIndex("id"));
            values[i++] = ret.getString(ret.getColumnIndex("marca")) + " | " +
                    ret.getString(ret.getColumnIndex("modelo")) + "(" +
                    ret.getString(ret.getColumnIndex("placa")) + ")" +
            "#" + ret.getString(ret.getColumnIndex("vistoria"));
            b = ret.moveToNext();
        }

        return values;
    }

    public static Integer pegaPorID(int position) {
        return ids[position];
    }

    public Cursor getVeiculo(int id) {
        String [] fields = {"id", "nome", "cpf", "endereco", "marca", "modelo", "placa", "vistoria", "observacao"};
        Cursor ret = db.query("Veiculo", fields, "id = " + String.valueOf(id), null, null, null, null);

        ret.moveToFirst();

        return ret;
    }
}
