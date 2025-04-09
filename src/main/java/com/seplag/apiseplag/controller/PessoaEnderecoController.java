package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.model.PessoaEndereco;
import com.seplag.apiseplag.services.EnderecoService;
import com.seplag.apiseplag.services.PessoaEnderecoService;
import com.seplag.apiseplag.services.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pessoa-endereco")
public class PessoaEnderecoController {

    private final PessoaEnderecoService pessoaEnderecoService;
    private final PessoaService pessoaService;
    private final EnderecoService enderecoService;
    private static final int TAMANHO_PAGINA = 10;

    @Autowired
    public PessoaEnderecoController(
            PessoaEnderecoService pessoaEnderecoService,
            PessoaService pessoaService,
            EnderecoService enderecoService) {
        this.pessoaEnderecoService = pessoaEnderecoService;
        this.pessoaService = pessoaService;
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public String listarTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao,
            Model model) {

        Page<PessoaEndereco> pessoaEnderecos = pessoaEnderecoService.listarPaginado(pagina, TAMANHO_PAGINA, ordenarPor, direcao);

        model.addAttribute("associacoes", pessoaEnderecos);
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("ordenarPor", ordenarPor);
        model.addAttribute("direcao", direcao);
        model.addAttribute("direcaoInversa", direcao.equals("asc") ? "desc" : "asc");

        return "pessoa-endereco/lista";
    }

    @GetMapping("/pessoa/{pessoaId}")
    public String listarPorPessoa(
            @PathVariable Integer pessoaId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "id") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao,
            Model model) {

        Pessoa pessoa = pessoaService.buscarPorId(pessoaId)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa inválida com ID: " + pessoaId));

        Page<PessoaEndereco> pessoaEnderecos = pessoaEnderecoService.buscarPorPessoaIdPaginado(pessoaId, pagina, ordenarPor, direcao);

        model.addAttribute("pessoa", pessoa);
        model.addAttribute("associacoes", pessoaEnderecos);
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("ordenarPor", ordenarPor);
        model.addAttribute("direcao", direcao);
        model.addAttribute("direcaoInversa", direcao.equals("asc") ? "desc" : "asc");

        return "pessoa-endereco/lista-por-pessoa";
    }

    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        // Usando paginação para obter todas as pessoas
        Sort.Direction sortDirection = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.by(sortDirection, "nome"));
        Page<Pessoa> pessoas = pessoaService.buscarTodas(pageable);

        // Usando o serviço de endereço com paginação
        Page<Endereco> enderecos = enderecoService.listarTodos(0, "logradouro", "asc");

        model.addAttribute("pessoas", pessoas.getContent());
        model.addAttribute("enderecos", enderecos.getContent());

        return "pessoa-endereco/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(
            @RequestParam Integer pessoaId,
            @RequestParam Integer enderecoId,
            RedirectAttributes redirectAttributes) {

        try {
            PessoaEndereco pessoaEndereco = pessoaEnderecoService.associarPessoaEndereco(pessoaId, enderecoId);
            redirectAttributes.addFlashAttribute("mensagem", "Associação realizada com sucesso!");
            return "redirect:/pessoa-endereco";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao associar: " + e.getMessage());
            return "redirect:/pessoa-endereco/nova";
        }
    }

    @GetMapping("/remover/{id}")
    public String remover(
            @PathVariable Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            pessoaEnderecoService.removerAssociacao(id);
            redirectAttributes.addFlashAttribute("mensagem", "Associação removida com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover associação: " + e.getMessage());
        }

        return "redirect:/pessoa-endereco";
    }

    @GetMapping("/remover/pessoa/{pessoaId}/endereco/{enderecoId}")
    public String removerPorPessoaEEndereco(
            @PathVariable Integer pessoaId,
            @PathVariable Integer enderecoId,
            RedirectAttributes redirectAttributes) {

        try {
            pessoaEnderecoService.removerAssociacaoPorPessoaEEndereco(pessoaId, enderecoId);
            redirectAttributes.addFlashAttribute("mensagem", "Associação removida com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao remover associação: " + e.getMessage());
        }

        return "redirect:/pessoa-endereco/pessoa/" + pessoaId;
    }
}