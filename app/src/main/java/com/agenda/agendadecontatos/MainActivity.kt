package com.agenda.agendadecontatos

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.agenda.agendadecontatos.adapter.ContatoAdapter
import com.agenda.agendadecontatos.dao.UsuarioDao
import com.agenda.agendadecontatos.databinding.ActivityMainBinding
import com.agenda.agendadecontatos.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usuarioDao: UsuarioDao
    private lateinit var contatoAdapter: ContatoAdapter
    private val listaUsuarios = mutableListOf<Usuario>()

    private val cadastrarUsuarioLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        carregarContatos()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuarioDao = AppDatabase.getInstance(this).usuarioDao()
        setupRecyclerView()

        carregarContatos()

        binding.btCadastrar.setOnClickListener {
            val navegarTelaCadastro = Intent(this, CadastrarUsuario::class.java)
            cadastrarUsuarioLauncher.launch(navegarTelaCadastro)
        }
    }

    private fun setupRecyclerView() {
        // Agora, o adapter recebe a função de deletar.
        contatoAdapter = ContatoAdapter(this, listaUsuarios) { usuario ->
            deletarUsuario(usuario)
        }
        binding.recyclerViewContatos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = contatoAdapter
        }
    }

    private fun deletarUsuario(usuario: Usuario) {
        lifecycleScope.launch(Dispatchers.IO) {
            usuarioDao.deletar(usuario.uid) // A operação de banco de dados acontece em uma thread de IO.
            // Após deletar, recarrega a lista na thread principal.
            withContext(Dispatchers.Main) {
                carregarContatos()
            }
        }
    }

    private fun carregarContatos() {
        lifecycleScope.launch(Dispatchers.IO) {
            val contatosDoBanco = usuarioDao.get()

            withContext(Dispatchers.Main) {
                listaUsuarios.clear()
                listaUsuarios.addAll(contatosDoBanco)
                contatoAdapter.notifyDataSetChanged()
            }
        }
    }
}
