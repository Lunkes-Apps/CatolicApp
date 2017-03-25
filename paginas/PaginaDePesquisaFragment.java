package com.alexandrelunkes.catolicapp.paginas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexandrelunkes.catolicapp.PassagemTexto;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.AdapterListMenuPrincipal;
import com.alexandrelunkes.catolicapp.adapters.ItemListViewPrincipal;
import com.alexandrelunkes.catolicapp.bibliaeagenda.ItemBiblia;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioBibliaDAO;
import com.alexandrelunkes.catolicapp.dialogs.FiltrosDialog;
import com.alexandrelunkes.catolicapp.itens.BotaoCatolic;
import com.alexandrelunkes.catolicapp.itens.EditTextPergaminho;
import com.alexandrelunkes.catolicapp.itens.TextoPergaminho;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Alexandre Lunkes on 29/04/2016.
 */
public class PaginaDePesquisaFragment extends Fragment implements View.OnClickListener, FiltrosDialog.OnSelectFiltroListener,
        AdapterView.OnItemClickListener {


    private OnOkClicked myCallBack;
    List<ItemListViewPrincipal> lista;
    List<ItemBiblia> itensAchados;
    private AdapterListMenuPrincipal adapter;
    private String query;
    private EditText editText;
    private TextoPergaminho texto;
    private ListView listView;
    private List<ItemBiblia> versiculosAchados;
    private String[] abrNovo;
    private String[] abrAnt;
    private ImageView filter;
    private ProgressDialog progress;
    private int filtro;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmente_pesquisa_em_branco, null);


        editText = (EditText) view.findViewById(R.id.editText_pesquisa);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(editText.getText().toString());
                    return true;
                }
                return false;
            }
        });
        texto = (TextoPergaminho) view.findViewById(R.id.textViewQty);
        listView = (ListView) view.findViewById(R.id.list_pesquisa);
        listView.setOnItemClickListener(this);

        abrAnt = getActivity().getResources().getStringArray(R.array.abrev_livros_antigo_testam);
        abrNovo = getActivity().getResources().getStringArray(R.array.abrev_livros_novo_testam);

        filtro = -1;

        return view;
    }

    private void search(String query) {
        query = editText.getText().toString();

        String[] palavras = query.split(" ");
        StringBuilder sb = new StringBuilder();

        boolean ok = false;

        for (String s : palavras) {
            if (!s.equals("")) {
                ok = true;
                Log.i("teste_pesquisa", "palavra =" + s);
                break;
            } else {
                ok = false;
            }
        }


        if (ok) {

            if (!palavras[0].equals("")) sb.append(palavras[0]);
            for (int i = 1; i < palavras.length; i++) {
                if (!palavras[i].equals("")) sb.append(" " + palavras[i]);
            }

            query = sb.toString();

            Log.i("teste_pesquisa", " query " + query);
            closeKeyboard(getActivity(), editText.getWindowToken());


            final RepositorioBibliaDAO dao = RepositorioBibliaDAO.getInstance(getContext());

            final String pesquisa = query;

            progress = ProgressDialog.show(getContext(), "Aguarde.", "Carregando dados da pesquisa...");

            new Thread(new Runnable() {
                @Override
                public void run() {

                    itensAchados = dao.buscarPesquisa(pesquisa);
                    Message msg = new Message();
                    msg.obj = itensAchados;
                    myHandler.sendMessage(msg);
                }
            }).start();

        } else {
            Toast.makeText(getContext(), "Digite ao menos uma palavra.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.pesquisa_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.filtro:

                FiltrosDialog dialog = new FiltrosDialog();
                dialog.setOnSelectFiltroListener(this);
                dialog.show(getActivity().getFragmentManager().beginTransaction(), "shareOptions");

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myCallBack = (OnOkClicked) activity;

    }

    @Override
    public void onClick(View v) {


    }

    private void closeKeyboard(FragmentActivity activity, IBinder windowToken) {

        InputMethodManager inputMethodManager = (InputMethodManager)
                activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0);

    }

    @Override
    public void filtroSelecionado(int i) {


        switch (i) {
            case 1:
                lista = criarLista(versiculosAchados);
                filtro = -1;
                Log.i("teste_filtro", "Filtro todos");
                break;
            case 2:
                filtro = 0;
                lista = criarLista(versiculosAchados, 0);
                Log.i("teste_filtro", "Filtro antigo");
                break;
            case 3:
                filtro = 1;
                lista = criarLista(versiculosAchados, 1);
                Log.i("teste_filtro", "Filtro novo");
                break;
            default:
                Log.i("teste_filtro", "Filtro nenhum");
                break;
        }

        if (lista != null) {
            switch (filtro) {
                case -1:
                    texto.setText("Foram encontrados " + lista.size() + " versiculos");
                    break;
                case 0:
                    texto.setText("Foram encontrados " + lista.size() + " versiculos \n filtrados apenas o Antigo Testamento");
                    break;
                case 1:
                    texto.setText("Foram encontrados " + lista.size() + " versiculos \n filtrados apenas o Novo Testamento");
                    break;
                default:
                    texto.setText("Foram encontrados " + lista.size() + " versiculos");
                    break;
            }
        }
        adapter = new AdapterListMenuPrincipal(getContext(), (ArrayList<ItemListViewPrincipal>) lista);
        listView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        List<ItemBiblia> aux = new ArrayList<>();
        if (filtro > 0) {

            for (ItemBiblia i : versiculosAchados) {
                if (i.getTestamento() == filtro) {
                    aux.add(i);
                }
            }

        } else {
            aux = versiculosAchados;
        }


        ItemBiblia item = aux.get(position);

        Intent intent = new Intent(getActivity(), LivroCompletoActivity.class);
        intent.putExtra(LivroCompletoActivity.TESTAMENTO_ID, item.getTestamento());
        intent.putExtra(LivroCompletoActivity.LIVRO_ID, item.getLivro());
        intent.putExtra(LivroCompletoActivity.CAPITULO_ID, item.getCapitulo());
        intent.putExtra(LivroCompletoActivity.VERSICULO_ID, item.getVersiculo());

        startActivity(intent);

    }


    public interface OnOkClicked {
        public void okClicked(String query);
    }


    Handler myHandler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.obj != null) {
                versiculosAchados = (List) msg.obj;
            }

            lista = criarLista(versiculosAchados);

            adapter = new AdapterListMenuPrincipal(getContext(), (ArrayList<ItemListViewPrincipal>) lista);

            listView.setAdapter(new AdapterListMenuPrincipal(getContext(), (ArrayList<ItemListViewPrincipal>) lista));

            if (lista != null) {
                switch (filtro) {
                    case -1:
                        texto.setText("Foram encontrados " + lista.size() + " versiculos");
                        break;
                    case 0:
                        texto.setText("Foram encontrados " + lista.size() + " versiculos \n Filtrados apenas o Antigo Testamento");
                        break;
                    case 1:
                        texto.setText("Foram encontrados " + lista.size() + " versiculos \n Filtrados apenas o Novo Testamento");
                        break;
                    default:
                        texto.setText("Foram encontrados " + lista.size() + " versiculos");
                        break;
                }

            }


            progress.dismiss();
        }
    };


    private List<ItemListViewPrincipal> criarLista(List<ItemBiblia> versiculosAchados, int testamento) {

        List<ItemListViewPrincipal> lista = new ArrayList<>();

        String titulo;
        String sub;
        StringBuilder sb = new StringBuilder();

        if (versiculosAchados != null) {
            for (ItemBiblia i : versiculosAchados) {
                if (i.getTestamento() == testamento) {
                    titulo = i.getTexto();

                    if (i.getTestamento() == 0) {
                        sb.append(abrAnt[i.getLivro()]);
                    } else {
                        sb.append(abrNovo[i.getLivro()]);
                    }

                    sb.append(" " + Integer.toString(i.getCapitulo() + 1) + ", " +
                            Integer.toString(i.getVersiculo() + 1) + ".");

                    sub = sb.toString();

                    lista.add(new ItemListViewPrincipal(0, titulo, sub));

                    sb.delete(0, sb.length());
                }
            }

        }
        return lista;

    }

    private List<ItemListViewPrincipal> criarLista(List<ItemBiblia> versiculosAchados) {

        List<ItemListViewPrincipal> lista = new ArrayList<>();

        String titulo;
        String sub;
        StringBuilder sb = new StringBuilder();

        if (versiculosAchados != null) {

            for (ItemBiblia i : versiculosAchados) {

                titulo = i.getTexto();

                if (i.getTestamento() == 0) {
                    sb.append(abrAnt[i.getLivro()]);
                } else {
                    sb.append(abrNovo[i.getLivro()]);
                }

                sb.append(" " + Integer.toString(i.getCapitulo() + 1) + ", " +
                        Integer.toString(i.getVersiculo() + 1) + ".");

                sub = sb.toString();

                lista.add(new ItemListViewPrincipal(0, titulo, sub));

                sb.delete(0, sb.length());
            }
        }
        return lista;
    }


}
