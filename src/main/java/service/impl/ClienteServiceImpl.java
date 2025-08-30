package service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import model.Cliente;
import model.ClienteRepository;
import model.Endereco;
import model.EnderecoRepository;
import service.ClienteService;
import service.ViaCepService;

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
