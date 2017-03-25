package com.alexandrelunkes.catolicapp.favoritos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alexandrelunkes.catolicapp.LeitoresDeTexto.LeitorDeBiblia;
import com.alexandrelunkes.catolicapp.R;
import com.alexandrelunkes.catolicapp.adapters.AdapterListSimples;
import com.alexandrelunkes.catolicapp.bibliaeagenda.RepositorioBibliaDAO;
import com.alexandrelunkes.catolicapp.dialogs.ShareOptionsDialog;
import com.alexandrelunkes.catolicapp.itens.CompartilhadorDeBitmap;
import com.alexandrelunkes.catolicapp.itens.EditTextPergaminho;
import com.alexandrelunkes.catolicapp.itens.TextoPergaminho;
import com.alexandrelunkes.catolicapp.paginas.ComentarioActivity;

import java.util.ArrayList;
import java.util.List;

public class FavoritoScreenActivity extends AppCompatActivity implements ShareOptionsDialog.OnShareClickedListener {

    private LeitorDeBiblia leitor;

    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams layoutParams;

    private TextView tvTitulo;
    private TextView tvPassagem;
    private TextView tvData;
    private TextView tvComent;
    private EditText editText;

    private TextoPergaminho listTexto;
    private List<String> versiculos;

    private String titulo;
    private String passagem;
    private String data;
    private String coment;

    private int idF;
    private Favorito f;
    private boolean isEditing = false;

    private ItemSuportPassagem itemSuportPassagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorito_screen);
        setAppBar();

        leitor = new LeitorDeBiblia(this);

        Bundle arg = getIntent().getExtras();
        f = (Favorito) arg.getSerializable("favorito");

        passagem = f.getPassagem();
        data = f.getData();
        coment = f.getAnotacao();

        passagem = passagem.replaceAll("[()]", "");

        itemSuportPassagem = getItemPassagem(passagem);




        RepositorioBibliaDAO dao = RepositorioBibliaDAO.getInstance(this);

        ArrayList<String[]> livroCompleto = dao.listarLivro(
                itemSuportPassagem.getTestamento()
                ,itemSuportPassagem.getLivro()).getLivro();


        if (itemSuportPassagem.getTestamento() == 0) {
            titulo = getResources().getStringArray(
                    R.array.lista_antigo_testamento_livros_array)[itemSuportPassagem.getLivro()];
        } else {
            titulo = getResources().getStringArray(
                    R.array.lista_novo_testamento_livros_array)[itemSuportPassagem.getLivro()];
        }


        versiculos = new ArrayList<>();


        Integer[] itens = itemSuportPassagem.getVersiculo();

        for (int i = 0; i < itens.length; i++) {
            Log.i("teste_favorito", itens[i].toString());
        }

        for (int i = 0; i < itens.length; i++) {
            versiculos.add(livroCompleto.get(itemSuportPassagem.getCapitulo())[itens[i]]);
            Log.i("teste_favorito", versiculos.get(0));
        }


        listTexto = (TextoPergaminho) findViewById(R.id.list_texto_favorito);
        StringBuilder sb = new StringBuilder();

        for(String linha:versiculos.toArray(new String[versiculos.size()])){
            sb.append(linha+"\n\n");
        }

        listTexto.setText(sb.toString());

        tvPassagem = (TextView) findViewById(R.id.favorito_passagem);
        tvTitulo = (TextView) findViewById(R.id.favorito_titulo);
        tvComent = (TextView) findViewById(R.id.favorito_coment);
        tvData = (TextView) findViewById(R.id.favorito_data);
        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        editText = new EditTextPergaminho(this);

        tvPassagem.setText(passagem);
        tvData.setText(data);
        tvComent.setText(coment);
        tvTitulo.setText(titulo);
        Log.i("teste_atualizar", "onCreate");

        idF = f.getId();
    }


    private ItemSuportPassagem getItemPassagem(String passagem) {

        Log.i("teste_favorito", passagem);

        int liv;
        int cap;

        List<Integer> vers = new ArrayList<>();
        int testamento;

        String[] palavras = passagem.split(" ");
        String capitulo;
        String livro;
        String versiculo = palavras[palavras.length - 1];
        if (palavras.length > 3) {
            capitulo = palavras[2];
            livro = palavras[0] + " " + palavras[1];
        } else {
            capitulo = palavras[1];
            livro = palavras[0];
        }
        int tam = leitor.getLivrosAntigoTestamento().length;

        testamento = retornaTestamento(livro);

        capitulo = capitulo.replace(",", "");

        liv = getLivroIndex(livro, testamento);

        Log.i("teste_favorito", versiculo);

        cap = Integer.parseInt(capitulo);

        StringBuilder sbVers = new StringBuilder();

        char[] itens = palavras[palavras.length - 1].toCharArray();

        Log.i("teste_favorito", "palavras " + palavras[palavras.length - 1]);

        Log.i("teste_favorito", "item " + itens[0]);

        // carregando versiculos

        boolean continua = false;

        for (int i = 0; i < itens.length; i++) {

            if (itens[i] == ';') {

                if(continua){
                    continuacaoVers(vers,new Integer(sbVers.toString().replaceAll(" ", ""))-1);
                    sbVers.delete(0,sbVers.length());

                    continua = false;
                }else {
                    vers.add(new Integer(sbVers.toString().replaceAll(" ", "")) - 1);
                    sbVers.delete(0,sbVers.length());
                }
            }
            if (itens[i] == '-') {
                vers.add(new Integer(sbVers.toString().replaceAll(" ", "")) - 1);
                sbVers.delete(0,sbVers.length());
                continua = true;
            }
            if (Character.isDigit(itens[i])) {
                sbVers.append(itens[i]);
            }

            Log.i("teste_get_vers", sbVers.toString()+ " volta "+i);

        }

        if(continua){
            continuacaoVers(vers,new Integer(sbVers.toString().replaceAll(" ", ""))-1);
         //   continua = false;
        }else {
            vers.add(new Integer(sbVers.toString().replaceAll(" ", "")) - 1);
            sbVers.delete(0,sbVers.length());
        }

        Integer ver[] = vers.toArray(new Integer[vers.size()]);

        return new ItemSuportPassagem(testamento, liv, cap - 1, ver);
    }

    private int retornaTestamento(String livro) {

        for (String s : getResources().getStringArray(R.array.abrev_livros_novo_testam)) {

            if (s.equals(livro)) {
                return 1;
            }

        }
        return 0;
    }


    private List<Integer> continuacaoVers(List<Integer> vers, int fim){

        int voltas = fim-vers.get(vers.size()-1);
        int inicio = vers.size()-1;


        for (int j =1; j <= voltas; j++) {
            vers.add(vers.get(inicio)+j);
        }

        return vers;
    }


    @Override
    public Bitmap onShareClicked() {
        int testamento = itemSuportPassagem.getTestamento();
        Integer[] selecionados = itemSuportPassagem.getVersiculo();

        List<Integer> itensSelecionados = new ArrayList<>();

        for (Integer i : selecionados) {
            itensSelecionados.add(i);
        }
        int liv = itemSuportPassagem.getLivro();
        int cap = itemSuportPassagem.getCapitulo();


        CompartilhadorDeBitmap compartilhador = new CompartilhadorDeBitmap(this, testamento, itensSelecionados, liv, cap+1);

        return compartilhador.getBitMap(versiculos.toArray(new String[versiculos.size()]));
    }

    @Override
    public String onCopy() {

        String data = tvData.getText().toString();
        String passagem = tvPassagem.getText().toString();
        String texto = listTexto.getText().toString();

        StringBuilder sb = new StringBuilder(passagem);
        sb.append("\n");
        sb.append(data);
        sb.append("\n");
        sb.append("\n");
        sb.append(texto);
        sb.append("\n");
        sb.append("\n");

        return sb.toString();
    }

    public class ItemSuportPassagem {
        private int livro;
        private int capitulo;
        private Integer versiculos[];
        private int testamento;


        public ItemSuportPassagem(int testamento, int livro, int capitulo, Integer versiculos[]) {
            this.livro = livro;
            this.capitulo = capitulo;
            this.versiculos = versiculos;
            this.testamento = testamento;
        }

        public int getTestamento() {
            return testamento;
        }

        public void setTestamento(int testamento) {
            this.testamento = testamento;
        }


        public int getLivro() {
            return livro;
        }

        public void setLivro(int livro) {
            this.livro = livro;
        }

        public int getCapitulo() {
            return capitulo;
        }

        public void setCapitulo(int capitulo) {
            this.capitulo = capitulo;
        }

        public Integer[] getVersiculo() {
            return versiculos;
        }

        public void setVersiculo(Integer[] versiculos) {
            this.versiculos = versiculos;
        }
    }

    private int getLivroIndex(String livro, int testamento) {

        int i = 0;

        if (testamento == 1) {
            for (String abr : getResources().getStringArray(R.array.abrev_livros_novo_testam)) {
                if (abr.equals(livro)) {
                    return i;
                }
                i++;
            }
        } else {
            for (String abr : getResources().getStringArray(R.array.abrev_livros_antigo_testam)) {
                if (abr.equals(livro)) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_favorito_screen, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                Intent i = new Intent(this, ComentarioActivity.class);
                i.putExtra("favorito",f);
                startActivityForResult(i,1);
//
//
//                editText.setText(tvComent.getText().toString());
//                layoutParams = (LinearLayout.LayoutParams) tvComent.getLayoutParams();
//                linearLayout.removeViewAt(1);
//                editText.setLayoutParams(layoutParams);
//                linearLayout.addView(editText);
//                isEditing = true;
//                invalidateOptionsMenu();
                break;
            case R.id.done:
                tvComent.setText(editText.getText().toString());
                editText.setText("");
                linearLayout.removeViewAt(1);
                linearLayout.addView(tvComent);
                isEditing = false;
                invalidateOptionsMenu();
                break;
            case R.id.share:
                ShareOptionsDialog dialog = new ShareOptionsDialog();
                dialog.setOnShareClickedListener(this);
                dialog.show(getFragmentManager().beginTransaction(), "shareOptions");
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void setAppBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isEditing) {
            menu.findItem(R.id.edit).setVisible(false);
            menu.findItem(R.id.done).setVisible(true);
        } else {
            menu.findItem(R.id.edit).setVisible(true);
            menu.findItem(R.id.done).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1: f = (Favorito) data.getExtras().getSerializable("favorito");
                tvComent.setText(f.getAnotacao());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();


        Log.i("teste_atualizar","onResume");
    }


}
