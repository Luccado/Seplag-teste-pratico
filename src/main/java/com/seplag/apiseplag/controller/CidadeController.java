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

@Controller
@RequestMapping("/cidades")
public class CidadeController {

    @Autowired
    private CidadeService cidadeService;

    @GetMapping
    public String listarTodas(
            @PageableDefault(page = 0, size = 10, sort = "nome", direction = Direction.ASC) Pageable pageable,
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

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) {
        try {
            Cidade cidade = cidadeService.buscarCidadePeloId(id);
            model.addAttribute("cidade", cidade);
            return "cidades/detalhes";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/cidades?erro=Cidade+não+encontrada";
        }
    }

    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("cidade", new Cidade());
        return "cidades/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Cidade cidade) {
        try {
            cidadeService.salvar(cidade);
            return "redirect:/cidades?sucesso=Cidade+salva+com+sucesso";
        } catch (Exception e) {
            return "redirect:/cidades?erro=" + e.getMessage();
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model) {
        try {
            Cidade cidade = cidadeService.buscarCidadePeloId(id);
            model.addAttribute("cidade", cidade);
            return "cidades/formulario";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/cidades?erro=Cidade+não+encontrada";
        }
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Integer id, @ModelAttribute Cidade cidade) {
        try {
            cidadeService.atualizar(id, cidade);
            return "redirect:/cidades?sucesso=Cidade+atualizada+com+sucesso";
        } catch (Exception e) {
            return "redirect:/cidades?erro=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            cidadeService.excluir(id);
            return "redirect:/cidades?sucesso=Cidade+excluída+com+sucesso";
        } catch (Exception e) {
            return "redirect:/cidades?erro=" + e.getMessage();
        }
    }

    @GetMapping("/por-uf")
    public String listarPorUf(@RequestParam Character uf, Model model) {
        model.addAttribute("cidades", cidadeService.buscarPorUf(uf));
        model.addAttribute("filtroUf", uf);
        return "cidades/lista-por-uf";
    }

    @GetMapping("/buscar")
    public String buscarPorNome(@RequestParam String nome, Model model) {
        model.addAttribute("cidades", cidadeService.buscarPorNome(nome));
        model.addAttribute("filtroNome", nome);
        return "cidades/lista-busca";
    }
}