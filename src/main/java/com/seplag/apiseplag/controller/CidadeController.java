package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Cidade;
import com.seplag.apiseplag.services.CidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.EmptyResultDataAccessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/cidades")
@Tag(name = "Cidades", description = "API para gerenciamento de cidades")
public class CidadeController {

    @Autowired
    private CidadeService cidadeService;

    @Operation(
        summary = "Listar todas as cidades",
        description = "Retorna uma página HTML com todas as cidades cadastradas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de cidades carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public String listarTodas(
            @Parameter(description = "Configuração de paginação")
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Direction.ASC) Pageable pageable,
            @Parameter(description = "Filtro por nome da cidade")
            @RequestParam(required = false) String nome,
            Model model) {

        Page<Cidade> cidades;

        if (nome != null && !nome.isEmpty()) {
            cidades = cidadeService.buscarPorNomePaginado(nome, pageable);
            model.addAttribute("filtroNome", nome);
        } else {
            cidades = cidadeService.listarTodas(pageable);
        }

        model.addAttribute("cidades", cidades);
        return "cidades/lista";
    }

    @Operation(
        summary = "Buscar cidade por ID",
        description = "Retorna uma página HTML com os detalhes de uma cidade específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de detalhes da cidade carregada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public String buscarPorId(
            @Parameter(description = "ID da cidade", required = true)
            @PathVariable Integer id, Model model) {
        try {
            Cidade cidade = cidadeService.buscarCidadePeloId(id);
            model.addAttribute("cidade", cidade);
            return "cidades/detalhes";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/cidades?erro=Cidade+não+encontrada";
        }
    }

    @Operation(
        summary = "Exibir formulário de nova cidade",
        description = "Retorna uma página HTML com o formulário para criar uma nova cidade"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("cidade", new Cidade());
        return "cidades/formulario";
    }

    @Operation(
        summary = "Salvar nova cidade",
        description = "Cria uma nova cidade no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Cidade cidade) {
        try {
            cidadeService.salvar(cidade);
            return "redirect:/cidades?sucesso=Cidade+salva+com+sucesso";
        } catch (Exception e) {
            return "redirect:/cidades?erro=" + e.getMessage();
        }
    }

    @Operation(
        summary = "Exibir formulário de edição",
        description = "Retorna uma página HTML com o formulário para editar uma cidade existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cidade não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(
            @Parameter(description = "ID da cidade", required = true)
            @PathVariable Integer id, Model model) {
        try {
            Cidade cidade = cidadeService.buscarCidadePeloId(id);
            model.addAttribute("cidade", cidade);
            return "cidades/formulario";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/cidades?erro=Cidade+não+encontrada";
        }
    }

    @Operation(
        summary = "Atualizar cidade",
        description = "Atualiza os dados de uma cidade existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @Parameter(description = "ID da cidade", required = true)
            @PathVariable Integer id, @ModelAttribute Cidade cidade) {
        try {
            cidadeService.atualizar(id, cidade);
            return "redirect:/cidades?sucesso=Cidade+atualizada+com+sucesso";
        } catch (Exception e) {
            return "redirect:/cidades?erro=" + e.getMessage();
        }
    }

    @Operation(
        summary = "Excluir cidade",
        description = "Remove uma cidade do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/excluir/{id}")
    public String excluir(
            @Parameter(description = "ID da cidade", required = true)
            @PathVariable Integer id) {
        try {
            cidadeService.excluir(id);
            return "redirect:/cidades?sucesso=Cidade+excluída+com+sucesso";
        } catch (Exception e) {
            return "redirect:/cidades?erro=" + e.getMessage();
        }
    }

    @Operation(
        summary = "Listar cidades por UF",
        description = "Retorna uma página HTML com todas as cidades de um estado específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de cidades carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/por-uf")
    public String listarPorUf(
            @Parameter(description = "UF do estado (ex: AL, SP)", required = true)
            @RequestParam Character uf, Model model) {
        model.addAttribute("cidades", cidadeService.buscarPorUf(uf));
        model.addAttribute("filtroUf", uf);
        return "cidades/lista-por-uf";
    }

    @Operation(
        summary = "Buscar cidades por nome",
        description = "Retorna uma página HTML com cidades que contêm o nome especificado"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de cidades carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscar")
    public String buscarPorNome(
            @Parameter(description = "Nome ou parte do nome da cidade", required = true)
            @RequestParam String nome, Model model) {
        model.addAttribute("cidades", cidadeService.buscarPorNome(nome));
        model.addAttribute("filtroNome", nome);
        return "cidades/lista-busca";
    }
}