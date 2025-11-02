package com.agenda.agendadecontatos.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.agenda.agendadecontatos.AtualizarUsuario
import com.agenda.agendadecontatos.databinding.ContatoItemBinding
import com.agenda.agendadecontatos.model.Usuario

class ContatoAdapter(
    private val context: Context,
    private val listaUsuarios: MutableList<Usuario>,
    private val onDeletarClick: (Usuario) -> Unit // Função de callback para deletar
) : RecyclerView.Adapter<ContatoAdapter.ContatoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoViewHolder {
        val itemLista = ContatoItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ContatoViewHolder(itemLista)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ContatoViewHolder, position: Int) {
        val usuario = listaUsuarios[position]
        holder.txtNome.text = usuario.nome
        holder.txtSobrenome.text = usuario.sobrenome
        holder.txtIdade.text = usuario.idade
        holder.txtCelular.text = usuario.celular

        holder.btAtualizar.setOnClickListener {
            val intent = Intent(context, AtualizarUsuario::class.java)
            intent.putExtra("nome", usuario.nome)
            intent.putExtra("sobrenome", usuario.sobrenome)
            intent.putExtra("idade", usuario.idade)
            intent.putExtra("celular", usuario.celular)
            intent.putExtra("uid", usuario.uid)
            context.startActivity(intent)
        }

        holder.btDeletar.setOnClickListener {
            // Chama a função de callback para que a MainActivity delete o usuário.
            onDeletarClick(usuario)
        }
    }

    override fun getItemCount() = listaUsuarios.size

    inner class ContatoViewHolder(binding: ContatoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val txtNome = binding.txtNome
        val txtSobrenome = binding.txtSobrenome
        val txtIdade = binding.txtIdade
        val txtCelular = binding.txtCelular
        val btAtualizar = binding.btAtualizar
        val btDeletar = binding.btDeletar
    }
}