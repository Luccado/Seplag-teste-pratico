package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/enderecos")
@Tag(name = "Endereços", description = "API para gerenciamento de endereços")
public class EnderecoController {

    private final EnderecoService enderecoService;

    @Autowired
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @Operation(
        summary = "Listar todos os endereços",
        description = "Retorna uma página HTML com todos os endereços cadastrados no sistema"
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
            @RequestParam(defaultValue = "logradouro") String ordenarPor,
            @Parameter(description = "Direção da ordenação (asc/desc)")
            @RequestParam(defaultValue = "asc") String direcao,
            Model model) {

        Page<Endereco> enderecos = enderecoService.listarTodos(pagina, ordenarPor, direcao);

        model.addAttribute("enderecos", enderecos);
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("ordenarPor", ordenarPor);
        model.addAttribute("direcao", direcao);
        model.addAttribute("direcaoInversa", direcao.equals("asc") ? "desc" : "asc");

        return "endereco/lista";
    }

    @Operation(
        summary = "Buscar endereço por ID",
        description = "Retorna uma página HTML com os detalhes de um endereço específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de detalhes do endereço carregada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public String buscarPorId(
            @Parameter(description = "ID do endereço", required = true)
            @PathVariable Integer id, Model model) {
        Endereco endereco = enderecoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Endereço inválido com ID: " + id));

        model.addAttribute("endereco", endereco);
        return "endereco/detalhes";
    }

    @Operation(
        summary = "Exibir formulário de novo endereço",
        description = "Retorna uma página HTML com o formulário para criar um novo endereço"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("endereco", new Endereco());
        return "endereco/formulario";
    }

    @Operation(
        summary = "Exibir formulário de edição",
        description = "Retorna uma página HTML com o formulário para editar um endereço existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(
            @Parameter(description = "ID do endereço", required = true)
            @PathVariable Integer id, Model model) {
        Endereco endereco = enderecoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Endereço inválido com ID: " + id));

        model.addAttribute("endereco", endereco);
        return "endereco/formulario";
    }

    @Operation(
        summary = "Salvar novo endereço",
        description = "Cria um novo endereço no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Endereco endereco) {
        enderecoService.salvar(endereco);
        return "redirect:/enderecos";
    }

    @Operation(
        summary = "Excluir endereço",
        description = "Remove um endereço do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/excluir/{id}")
    public String excluir(
            @Parameter(description = "ID do endereço", required = true)
            @PathVariable Integer id) {
        enderecoService.excluir(id);
        return "redirect:/enderecos";
    }

    @Operation(
        summary = "Buscar endereços por critérios",
        description = "Retorna uma página HTML com endereços que atendem aos critérios de busca"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de endereços carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscar")
    public String buscar(
            @Parameter(description = "Logradouro ou parte do logradouro")
            @RequestParam(required = false) String logradouro,
            @Parameter(description = "Bairro ou parte do bairro")
            @RequestParam(required = false) String bairro,
            @Parameter(description = "ID da cidade")
            @RequestParam(required = false) Integer cidadeId,
            @Parameter(description = "Número da página (começando em 0)")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Campo para ordenação")
            @RequestParam(defaultValue = "logradouro") String ordenarPor,
            @Parameter(description = "Direção da ordenação (asc/desc)")
            @RequestParam(defaultValue = "asc") String direcao,
            Model model) {

        Page<Endereco> enderecos;

        if (logradouro != null && !logradouro.isEmpty()) {
            enderecos = enderecoService.buscarPorLogradouro(logradouro, pagina, ordenarPor, direcao);
            model.addAttribute("termoBusca", logradouro);
            model.addAttribute("tipoBusca", "logradouro");
        } else if (bairro != null && !bairro.isEmpty()) {
            enderecos = enderecoService.buscarPorBairro(bairro, pagina, ordenarPor, direcao);
            model.addAttribute("termoBusca", bairro);
            model.addAttribute("tipoBusca", "bairro");
        } else if (cidadeId != null) {
            enderecos = enderecoService.buscarPorCidadeId(cidadeId, pagina, ordenarPor, direcao);
            model.addAttribute("termoBusca", cidadeId.toString());
            model.addAttribute("tipoBusca", "cidade");
        } else {
            enderecos = enderecoService.listarTodos(pagina, ordenarPor, direcao);
        }

        model.addAttribute("enderecos", enderecos);
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("ordenarPor", ordenarPor);
        model.addAttribute("direcao", direcao);
        model.addAttribute("direcaoInversa", direcao.equals("asc") ? "desc" : "asc");

        return "endereco/lista";
    }
}