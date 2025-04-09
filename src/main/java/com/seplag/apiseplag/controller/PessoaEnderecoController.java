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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Controller
@RequestMapping("/pessoa-endereco")
@Tag(name = "Endereços de Pessoas", description = "API para gerenciamento de endereços de pessoas")
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

    @Operation(
        summary = "Listar todos os endereços de pessoas",
        description = "Retorna uma página HTML com todos os endereços cadastrados para pessoas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de endereços carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public String listarTodos(
            @Parameter(description = "Número da página (começando em 0)")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Campo para ordenação")
            @RequestParam(defaultValue = "id") String ordenarPor,
            @Parameter(description = "Direção da ordenação (asc/desc)")
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

    @Operation(
        summary = "Listar endereços por pessoa",
        description = "Retorna uma página HTML com todos os endereços de uma pessoa específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de endereços da pessoa carregada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/pessoa/{pessoaId}")
    public String listarPorPessoa(
            @Parameter(description = "ID da pessoa", required = true)
            @PathVariable Integer pessoaId,
            @Parameter(description = "Número da página (começando em 0)")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Campo para ordenação")
            @RequestParam(defaultValue = "id") String ordenarPor,
            @Parameter(description = "Direção da ordenação (asc/desc)")
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

    @Operation(
        summary = "Exibir formulário de nova associação",
        description = "Retorna uma página HTML com o formulário para criar uma nova associação entre pessoa e endereço"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
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

    @Operation(
        summary = "Salvar nova associação",
        description = "Cria uma nova associação entre pessoa e endereço"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/salvar")
    public String salvar(
            @Parameter(description = "ID da pessoa", required = true)
            @RequestParam Integer pessoaId,
            @Parameter(description = "ID do endereço", required = true)
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

    @Operation(
        summary = "Remover associação por ID",
        description = "Remove uma associação entre pessoa e endereço pelo ID da associação"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "404", description = "Associação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/remover/{id}")
    public String remover(
            @Parameter(description = "ID da associação", required = true)
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

    @Operation(
        summary = "Remover associação por pessoa e endereço",
        description = "Remove uma associação entre pessoa e endereço pelos IDs da pessoa e do endereço"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "404", description = "Associação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/remover/pessoa/{pessoaId}/endereco/{enderecoId}")
    public String removerPorPessoaEEndereco(
            @Parameter(description = "ID da pessoa", required = true)
            @PathVariable Integer pessoaId,
            @Parameter(description = "ID do endereço", required = true)
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