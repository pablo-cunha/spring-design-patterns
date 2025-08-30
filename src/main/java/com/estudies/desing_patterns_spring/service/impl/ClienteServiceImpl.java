package com.estudies.desing_patterns_spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudies.desing_patterns_spring.model.Cliente;
import com.estudies.desing_patterns_spring.model.ClienteRepository;
import com.estudies.desing_patterns_spring.model.Endereco;
import com.estudies.desing_patterns_spring.model.EnderecoRepository;
import com.estudies.desing_patterns_spring.service.ClienteService;
import com.estudies.desing_patterns_spring.service.ViaCepService;


@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente> clienteBancoDeDados = clienteRepository.findById(id);

        if(clienteBancoDeDados.isPresent()) {
            salvarClienteComCep(cliente);
        } 
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }
    
    private void salvarClienteComCep(Cliente cliente) {
        String cepcliente = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cliente.getEndereco().getCep())
        .orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cepcliente);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
    
}
