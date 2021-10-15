package br.edu.ifsp.scl.sdm.pa1.acessacontatocp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import br.edu.ifsp.scl.sdm.pa1.acessacontatocp.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.pa1.acessacontatocp.model.Contato

class MainActivity : AppCompatActivity() {
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val contatosStringList: MutableList<String> = mutableListOf()
    private val contatosAdapter: ArrayAdapter<String> by lazy {
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contatosStringList)
    }
    companion object {
        private val CONTENT_URI = Uri.parse("content://br.edu.ifsp.contatos/contatos/")
        private const val COLUNA_ID = "id"
        private const val COLUNA_NOME = "nome"
        private const val COLUNA_FONE = "fone"
        private const val COLUNA_EMAIL = "email"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
        activityMainBinding.contatosLv.adapter = contatosAdapter

        // Buscando contatos no Content Provider de Contatos
        buscarContatos()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when(item.itemId) {
            R.id.atualizarContatosMi -> {
                // Buscando contatos no Content Provider de Contatos
                buscarContatos()
                true
            }
            else -> false
        }

    private fun buscarContatos() {
        contatosStringList.clear()

        val contatosCursor = contentResolver.query(CONTENT_URI, null, null, null, null)
        contatosCursor?.apply {
            while(moveToNext()) {
                val contato = Contato(
                    getInt(getColumnIndexOrThrow(COLUNA_ID)),
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getString(getColumnIndexOrThrow(COLUNA_FONE)),
                    getString(getColumnIndexOrThrow(COLUNA_EMAIL))
                )
                contatosStringList.add("$contato")
            }
        }

        contatosAdapter.notifyDataSetChanged()
    }
}