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

@Controller
@RequestMapping("/pessoa")
public class PessoaController {

    private final PessoaService pessoaService;
    private static final int TAMANHO_PAGINA = 10;

    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    //GET
    @GetMapping
    public String listarTodas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "nome") String ordenarPor,
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




    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) {
        pessoaService.buscarPorId(id).ifPresentOrElse(pessoa -> model.addAttribute("pessoa", pessoa), () -> model.addAttribute("erro", "Não foi encontrado a pessoa")
        );
        return "pessoa/detalhes";
    }

    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("pessoa", new Pessoa());
        return "pessoa/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Pessoa pessoa, RedirectAttributes redirectAttributes) {
        try{
            pessoaService.Salvar(pessoa);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar pessoa: " + e.getMessage());
        }
        return "redirect:/pessoa";
    }

    @GetMapping("/buscar")
    public String buscar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String sexo,
        @RequestParam(defaultValue = "0") int pagina,
        @RequestParam(defaultValue = "nome") String ordenarPor,
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

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            pessoaService.deletar(id);
            redirectAttributes.addFlashAttribute("sucesso", "Pessoa excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir pessoa: " + e.getMessage());
        }
        return "redirect:/pessoa";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
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
