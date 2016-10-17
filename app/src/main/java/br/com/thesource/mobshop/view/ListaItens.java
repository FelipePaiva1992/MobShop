package br.com.thesource.mobshop.view;

import java.io.FileNotFoundException;
import java.net.ConnectException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.*;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.thesource.mobshop.R;
import br.com.thesource.mobshop.adapter.ListaProdutosAdapter;
import br.com.thesource.mobshop.adapter.model.ListaProduto;
import br.com.thesource.mobshop.model.VendaItem;
import br.com.thesource.mobshop.util.AlterAllFontsInActivity;
import br.com.thesource.mobshop.util.ConnectionUtil;
import br.com.thesource.mobshop.util.LogUtil;
import br.com.thesource.mobshop.util.SessionStorageUtil;
import br.com.thesource.mobshop.wservices.ConexaoWSRest;

public class ListaItens extends Activity {

	final Context context = this;
    private TableLayout tabelaProdutos;
	private TextView txtTotalCarrinho;
    private ListView listViewProdutos;
	
	private ProgressDialog dialog;
	private final Handler handler = new Handler();

    private static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_lista_itens);

        AlterAllFontsInActivity.overrideFonts(this, findViewById(R.id.linearListaItens));
        txtTotalCarrinho = (TextView) findViewById(R.id.txtTotalCarrinho);
        listViewProdutos = (ListView) findViewById(R.id.listaItens);
        listProdutosVenda();
        ConnectionUtil.verificaConexao(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectionUtil.verificaConexao(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.editTextEndereco || super.onOptionsItemSelected(item);
    }

    public void listaItensBtnVoltar(View v){
        Intent intent = new Intent();
        finish();
        intent.setClass(ListaItens.this, TelaVenda.class);
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
    }
    
    private void listProdutosVenda(){
    	dialog = ProgressDialog.show(this, "", "Listando produtos da venda, por favor aguarde...", false, false);
    	new Thread() {
			public void run() {
				List<NameValuePair> params = new ArrayList<>();
                params.add(new BasicNameValuePair("idVenda", SessionStorageUtil.getIdVenda())); 	
		    	try {
		    		ConexaoWSRest conexaoWSRest = new ConexaoWSRest();
		    		final List<VendaItem> items = conexaoWSRest.buscarProdutosVenda(params,activity);
		    		montarTabelaCarrinho(items);

				} catch (ConnectException e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Verifique a conexão de dados!", Toast.LENGTH_SHORT).show();
						}
					});
				} catch (Exception e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Erro ao listar produtos!", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					dialog.dismiss();
					
				}
			}
    	}.start();
    }
    

	
    private void montarTabelaCarrinho(List<VendaItem> itens){

    	final List<ListaProduto> listaProdutos = converterListaItemParaProduto(itens);

        handler.post(new Runnable() {
			@Override
			public void run() {


                ListaProdutosAdapter adapter = new ListaProdutosAdapter(listaProdutos, ListaItens.this);

                View header = getLayoutInflater().inflate(R.layout.layout_header_lista_produtos,null);
                listViewProdutos.addHeaderView(header);
                listViewProdutos.setAdapter(adapter);

				SessionStorageUtil.setVlVenda(Double.parseDouble(totalCarrinho(listaProdutos).replace(",",".")));
				txtTotalCarrinho.setText("R$ " + String.valueOf(totalCarrinho(listaProdutos)));

			}
		});
    }

    private String totalCarrinho(List<ListaProduto> listaProdutos) {
        DecimalFormat decimal = new DecimalFormat( "0.00" );
        Double totalCarrinho = 0.0;
        for(ListaProduto produto:listaProdutos){
            totalCarrinho += produto.getTotalProduto();
        }
        return decimal.format(totalCarrinho);
    }

    private List<ListaProduto> converterListaItemParaProduto(List<VendaItem> itens) {
        List<ListaProduto> listaProdutos = new ArrayList<>();
        ListaProduto produto;
        for(VendaItem item:itens){
            Double vlProduto = item.getProduto().getPrProduto();
            int quantidadeProduto = item.getVlQuantidade();
            produto = new ListaProduto();
            produto.setId(item.getProduto().getIdRefProduto());
            produto.setNomeProduto(item.getProduto().getNmProduto());
            produto.setQuantidadeProduto(quantidadeProduto);
            produto.setTotalProduto(vlProduto*quantidadeProduto);
            listaProdutos.add(produto);
        }

        return listaProdutos;
    }

    private void removerItem(String idProd){
    	
    	final String idProduto = idProd;
    	
    	dialog = ProgressDialog.show(this, "", "Removendo item a venda, por favor aguarde...", false, false);
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
							Toast.makeText(getBaseContext(),"Produto removido!", Toast.LENGTH_SHORT).show();

							finish();
							//startActivity(getIntent());
						}
					});

		    		
				} catch (ConnectException e) {
                    LogUtil.printError(e);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(),"Verifique a conexão de dados!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
					LogUtil.printError(e);
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getBaseContext(),"Erro ao remover o produto!", Toast.LENGTH_SHORT).show();
						}
					});
				} finally {
					dialog.dismiss();
				}
			}
            	}.start();
    }

}
