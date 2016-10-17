package br.com.thesource.mobshop.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.adapter.model.ListaProduto;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;

/**
 * Created by felipepaiva on 14/01/15.
 */
public class ListaProdutosAdapter extends BaseAdapter {

    private final List<ListaProduto> produtos;
    private final Activity activity;


    private ProgressDialog dialog;
    private final Handler handler = new Handler();

    public ListaProdutosAdapter(List<ListaProduto> listaProdutos, Activity activity) {
        this.produtos = listaProdutos;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return produtos.size();
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return produtos.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ListaProduto produto = produtos.get(position);

        LayoutInflater inflater = activity.getLayoutInflater();
        
        View linha = inflater.inflate(R.layout.layout_lista_produtos, null);

        TextView nomeProduto;
        nomeProduto = (TextView) linha.findViewById(R.id.nomeProduto);
        nomeProduto.setText(produto.getNomeProduto());

        TextView quantidadeProduto;
        quantidadeProduto = (TextView) linha.findViewById(R.id.quantidadeProduto);
        quantidadeProduto.setText(String.valueOf(produto.getQuantidadeProduto()));

        TextView totalProduto;
        totalProduto = (TextView) linha.findViewById(R.id.totalProduto);
        DecimalFormat decimal = new DecimalFormat( "0.00" );
        totalProduto.setText(String.valueOf(decimal.format(produto.getTotalProduto())));

        ImageView removerProduto;
        removerProduto = (ImageView) linha.findViewById(R.id.removeProduto);

        removerProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

                // set title
                alertDialogBuilder.setTitle("Remover Item");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Deseja remover o item do carrinho?")
                        .setCancelable(false)
                        .setPositiveButton("Sim",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                removerItem(String.valueOf(produto.getId()));
                                produtos.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Não",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

        return linha;
    }

    private void removerItem(String idProd){

        final String idProduto = idProd;

        dialog = ProgressDialog.show(activity, "", "Removendo item a venda, por favor aguarde...", false, false);
        new Thread() {
            public void run() {
                List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("idProduto", idProduto));
                params.add(new BasicNameValuePair("idVenda", SessionStorageUtil.getIdVenda()));
                try {
                    ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
                    conexaoWSRest.removerProdutoVenda(params,activity);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getBaseContext(), "Produto removido!", Toast.LENGTH_SHORT).show();

                            //activity.finish();
                        }
                    });


                } catch (Exception e) {
                    LogUtil.printError(e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity.getBaseContext(),"Erro ao remover o produto!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    dialog.dismiss();
                }
            }
        }.start();
    }
}
