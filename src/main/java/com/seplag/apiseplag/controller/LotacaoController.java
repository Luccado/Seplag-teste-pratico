package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Lotacao;
import com.seplag.apiseplag.services.LotacaoService;
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
@RequestMapping("/lotacoes")
@Tag(name = "Lotações", description = "API para gerenciamento de lotações de servidores")
public class LotacaoController {

    @Autowired
    private LotacaoService lotacaoService;

    @Operation(
        summary = "Listar todas as lotações",
        description = "Retorna uma página HTML com todas as lotações cadastradas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de lotações carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public String listarTodas(
            @Parameter(description = "Configuração de paginação")
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
            Model model) {

        Page<Lotacao> lotacoes = lotacaoService.listarTodas(pageable);
        model.addAttribute("lotacoes", lotacoes);

        return "lotacoes/lista";
    }

    @Operation(
        summary = "Buscar lotação por ID",
        description = "Retorna uma página HTML com os detalhes de uma lotação específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de detalhes da lotação carregada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Lotação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public String buscarPorId(
            @Parameter(description = "ID da lotação", required = true)
            @PathVariable Integer id, Model model) {
        try {
            Lotacao lotacao = lotacaoService.buscarLotacaoPeloId(id);
            model.addAttribute("lotacao", lotacao);
            return "lotacoes/detalhes";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/lotacoes?erro=Lotação+não+encontrada";
        }
    }

    @Operation(
        summary = "Exibir formulário de nova lotação",
        description = "Retorna uma página HTML com o formulário para criar uma nova lotação"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("lotacao", new Lotacao());
        return "lotacoes/formulario";
    }

    @Operation(
        summary = "Salvar nova lotação",
        description = "Cria uma nova lotação no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Lotacao lotacao) {
        try {
            lotacaoService.salvar(lotacao);
            return "redirect:/lotacoes?sucesso=Lotação+salva+com+sucesso";
        } catch (Exception e) {
            return "redirect:/lotacoes?erro=" + e.getMessage();
        }
    }

    @Operation(
        summary = "Exibir formulário de edição",
        description = "Retorna uma página HTML com o formulário para editar uma lotação existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Lotação não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(
            @Parameter(description = "ID da lotação", required = true)
            @PathVariable Integer id, Model model) {
        try {
            Lotacao lotacao = lotacaoService.buscarLotacaoPeloId(id);
            model.addAttribute("lotacao", lotacao);
            return "lotacoes/formulario";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/lotacoes?erro=Lotação+não+encontrada";
        }
    }

    @Operation(
        summary = "Atualizar lotação",
        description = "Atualiza os dados de uma lotação existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/atualizar/{id}")
    public String atualizar(
            @Parameter(description = "ID da lotação", required = true)
            @PathVariable Integer id, @ModelAttribute Lotacao lotacao) {
        try {
            lotacaoService.atualizar(id, lotacao);
            return "redirect:/lotacoes?sucesso=Lotação+atualizada+com+sucesso";
        } catch (Exception e) {
            return "redirect:/lotacoes?erro=" + e.getMessage();
        }
    }

    @Operation(
        summary = "Excluir lotação",
        description = "Remove uma lotação do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/excluir/{id}")
    public String excluir(
            @Parameter(description = "ID da lotação", required = true)
            @PathVariable Integer id) {
        try {
            lotacaoService.excluir(id);
            return "redirect:/lotacoes?sucesso=Lotação+excluída+com+sucesso";
        } catch (Exception e) {
            return "redirect:/lotacoes?erro=" + e.getMessage();
        }
    }
}