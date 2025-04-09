package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.services.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@RequestMapping("/pessoa")
@Tag(name = "Pessoas", description = "API para gerenciamento de pessoas")
public class PessoaController {

    private final PessoaService pessoaService;
    private static final int TAMANHO_PAGINA = 10;

    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @Operation(
        summary = "Listar todas as pessoas",
        description = "Retorna uma página HTML com todas as pessoas cadastradas no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de pessoas carregada com sucesso"),
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
            Model model) {

        Sort.Direction sortDirecao = direcao.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(
                pagina, TAMANHO_PAGINA, Sort.by(sortDirecao, ordenarPor));

        Page<Pessoa> paginaPessoas = pessoaService.buscarTodas(pageRequest);
        adicionarAtributosPaginacao(model, paginaPessoas, ordenarPor, direcao);

        return "pessoa/lista";
    }

    @Operation(
        summary = "Buscar pessoa por ID",
        description = "Retorna uma página HTML com os detalhes de uma pessoa específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de detalhes da pessoa carregada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id}")
    public String buscarPorId(
            @Parameter(description = "ID da pessoa", required = true)
            @PathVariable Integer id, Model model) {
        pessoaService.buscarPorId(id).ifPresentOrElse(pessoa -> model.addAttribute("pessoa", pessoa), () -> model.addAttribute("erro", "Não foi encontrado a pessoa")
        );
        return "pessoa/detalhes";
    }

    @Operation(
        summary = "Exibir formulário de nova pessoa",
        description = "Retorna uma página HTML com o formulário para criar uma nova pessoa"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("pessoa", new Pessoa());
        return "pessoa/formulario";
    }

    @Operation(
        summary = "Salvar nova pessoa",
        description = "Cria uma nova pessoa no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pessoa pessoa, RedirectAttributes redirectAttributes) {
        try{
            pessoaService.Salvar(pessoa);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar pessoa: " + e.getMessage());
        }
        return "redirect:/pessoa";
    }

    @Operation(
        summary = "Buscar pessoas por critérios",
        description = "Retorna uma página HTML com pessoas que atendem aos critérios de busca"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de pessoas carregada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/buscar")
    public String buscar(
        @Parameter(description = "Nome ou parte do nome da pessoa")
        @RequestParam(required = false) String nome,
        @Parameter(description = "Sexo da pessoa (M/F)")
        @RequestParam(required = false) String sexo,
        @Parameter(description = "Número da página (começando em 0)")
        @RequestParam(defaultValue = "0") int pagina,
        @Parameter(description = "Campo para ordenação")
        @RequestParam(defaultValue = "nome") String ordenarPor,
        @Parameter(description = "Direção da ordenação (asc/desc)")
        @RequestParam(defaultValue = "asc") String direcao,
        Model model){

        PageRequest pageRequest = PageRequest.of(pagina, TAMANHO_PAGINA, Sort.by(direcao.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, ordenarPor));

        Page<Pessoa> resultado;

        if(nome != null && !nome.isEmpty()) {
            resultado = pessoaService.buscarPorNomeContaining(nome, pageRequest);
            model.addAttribute("termoBusca", nome);
        } else if(sexo != null && !sexo.isEmpty()) {
            resultado = pessoaService.buscarPorSexo(sexo, pageRequest);
            model.addAttribute("sexoBusca", sexo);
        } else {
            return "redirect:/pessoa";
        }
        adicionarAtributosPaginacao(model, resultado, ordenarPor, direcao);
        return "pessoa/lista";
    }

    @Operation(
        summary = "Excluir pessoa",
        description = "Remove uma pessoa do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "302", description = "Redirecionamento após sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/deletar/{id}")
    public String deletar(
            @Parameter(description = "ID da pessoa", required = true)
            @PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            pessoaService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Pessoa excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir pessoa: " + e.getMessage());
        }
        return "redirect:/pessoa";
    }

    @Operation(
        summary = "Exibir formulário de edição",
        description = "Retorna uma página HTML com o formulário para editar uma pessoa existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Formulário carregado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/editar/{id}")
    public String editar(
            @Parameter(description = "ID da pessoa", required = true)
            @PathVariable Integer id, Model model) {
        pessoaService.buscarPorId(id)
                .ifPresentOrElse(
                        pessoa -> model.addAttribute("pessoa", pessoa),
                        () -> model.addAttribute("erro", "Pessoa não encontrada")
                );
        return "pessoa/formulario";
    }

    private void adicionarAtributosPaginacao(Model model, Page<Pessoa> paginaPessoas, String ordenarPor, String direcao) {

            model.addAttribute("pessoas", paginaPessoas.getContent());
            model.addAttribute("paginaAtual", paginaPessoas.getNumber());
            model.addAttribute("totalPaginas", paginaPessoas.getTotalPages());
            model.addAttribute("totalItens", paginaPessoas.getTotalElements());
            model.addAttribute("ordenarPor", ordenarPor);
            model.addAttribute("direcao", direcao);
    }
}
