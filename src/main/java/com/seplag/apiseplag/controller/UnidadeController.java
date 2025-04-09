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

@Controller
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    @Autowired
    public UnidadeController(UnidadeService unidadeService) {
        this.unidadeService = unidadeService;
    }

    @GetMapping
    public String listarTodas(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "nome") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao,
            @RequestParam(required = false) String nome,
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

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) {
        Optional<Unidade> unidade = unidadeService.buscarPorId(id);

        if (unidade.isPresent()) {
            model.addAttribute("unidade", unidade.get());
            return "unidades/detalhes";
        } else {
            return "redirect:/unidades?erro=Unidade+não+encontrada";
        }
    }

    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("unidade", new Unidade());
        return "unidades/formulario";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model) {
        Optional<Unidade> unidade = unidadeService.buscarPorId(id);

        if (unidade.isPresent()) {
            model.addAttribute("unidade", unidade.get());
            return "unidades/formulario";
        } else {
            return "redirect:/unidades?erro=Unidade+não+encontrada";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Unidade unidade) {
        try {
            unidadeService.salvar(unidade);
            return "redirect:/unidades?sucesso=Unidade+salva+com+sucesso";
        } catch (Exception e) {
            return "redirect:/unidades?erro=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            unidadeService.excluir(id);
            return "redirect:/unidades?sucesso=Unidade+excluída+com+sucesso";
        } catch (Exception e) {
            return "redirect:/unidades?erro=" + e.getMessage();
        }
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Integer id, @ModelAttribute Unidade unidade) {
        try {
            unidadeService.atualizar(id, unidade);
            return "redirect:/unidades?sucesso=Unidade+atualizada+com+sucesso";
        } catch (Exception e) {
            return "redirect:/unidades?erro=" + e.getMessage();
        }
    }
}