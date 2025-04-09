package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Unidade;
import com.seplag.apiseplag.services.UnidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/unidades")
@Tag(name = "Unidades", description = "API para gerenciamento de unidades administrativas")
public class UnidadeController {

    private final UnidadeService unidadeService;

    @Autowired
    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @Operation(
        summary = "Listar todas as unidades",
        description = "Retorna uma página HTML com todas as unidades cadastradas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de unidades carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public String listarTodas(
            @Parameter(description = "Número da página (começando em 0)")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Campo para ordenação")
            @RequestParam(defaultValue = "nome") String ordenarPor,
            @Parameter(description = "Direção da ordenação (asc/desc)")
            @RequestParam(defaultValue = "asc") String direcao,
            @Parameter(description = "Filtro por nome da unidade")
            @RequestParam(required = false) String nome,
            @Parameter(description = "Filtro por sigla da unidade")
            @RequestParam(required = false) String sigla,
            Model model) {

        int tamanhoPagina = 10;
        Pageable pageable = PageRequest.of(
                pagina, tamanhoPagina,
                direcao.equals("asc") ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending()
        );

        Page<Unidade> unidades;

        if (nome != null && !nome.isEmpty()) {
            unidades = unidadeService.buscarPorNome(nome, pageable);
            model.addAttribute("nome", nome);
        } else if (sigla != null && !sigla.isEmpty()) {
            unidades = unidadeService.buscarPorSigla(sigla, pageable);
            model.addAttribute("sigla", sigla);
        } else {
            unidades = unidadeService.listarTodas(pageable);
        }

        model.addAttribute("unidades", unidades);
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("ordenarPor", ordenarPor);
        model.addAttribute("direcao", direcao);
        model.addAttribute("totalPaginas", unidades.getTotalPages());

        return "unidades/lista";
    }

    @Operation(
        summary = "Buscar unidade por ID",
        description = "Retorna uma página HTML com os detalhes de uma unidade específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de detalhes da unidade carregada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public String buscarPorId(
            @Parameter(description = "ID da unidade", required = true)
            @PathVariable Integer id, Model model) {
        Optional<Unidade> unidade = unidadeService.buscarPorId(id);

        if (unidade.isPresent()) {
            model.addAttribute("unidade", unidade.get());
            return "unidades/detalhes";
        } else {
            return "redirect:/unidades?erro=Unidade+não+encontrada";
        }
    }

    @Operation(
        summary = "Exibir formulário de nova unidade",
        description = "Retorna uma página HTML com o formulário para criar uma nova unidade"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("unidade", new Unidade());
        return "unidades/formulario";
    }

    @Operation(
        summary = "Exibir formulário de edição",
        description = "Retorna uma página HTML com o formulário para editar uma unidade existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(
            @Parameter(description = "ID da unidade", required = true)
            @PathVariable Integer id, Model model) {
        Optional<Unidade> unidade = unidadeService.buscarPorId(id);

        if (unidade.isPresent()) {
            model.addAttribute("unidade", unidade.get());
            return "unidades/formulario";
        } else {
            return "redirect:/unidades?erro=Unidade+não+encontrada";
        }
    }

    @Operation(
        summary = "Salvar nova unidade",
        description = "Cria uma nova unidade no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Unidade unidade) {
        try {
            unidadeService.salvar(unidade);
            return "redirect:/unidades?sucesso=Unidade+salva+com+sucesso";
        } catch (Exception e) {
            return "redirect:/unidades?erro=" + e.getMessage();
        }
    }

    @Operation(
        summary = "Excluir unidade",
        description = "Remove uma unidade do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/excluir/{id}")
    public String excluir(
            @Parameter(description = "ID da unidade", required = true)
            @PathVariable Integer id) {
        try {
            unidadeService.excluir(id);
            return "redirect:/unidades?sucesso=Unidade+excluída+com+sucesso";
        } catch (Exception e) {
            return "redirect:/unidades?erro=" + e.getMessage();
        }
    }

    @Operation(
        summary = "Atualizar unidade",
        description = "Atualiza os dados de uma unidade existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @Parameter(description = "ID da unidade", required = true)
            @PathVariable Integer id, @ModelAttribute Unidade unidade) {
        try {
            unidadeService.atualizar(id, unidade);
            return "redirect:/unidades?sucesso=Unidade+atualizada+com+sucesso";
        } catch (Exception e) {
            return "redirect:/unidades?erro=" + e.getMessage();
        }
    }
}