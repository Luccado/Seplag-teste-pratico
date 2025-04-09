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

@Controller
@RequestMapping("/lotacoes")
public class LotacaoController {

    @Autowired
    private LotacaoService lotacaoService;

    @GetMapping
    public String listarTodas(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
            Model model) {

        Page<Lotacao> lotacoes = lotacaoService.listarTodas(pageable);
        model.addAttribute("lotacoes", lotacoes);

        return "lotacoes/lista";
    }

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) {
        try {
            Lotacao lotacao = lotacaoService.buscarLotacaoPeloId(id);
            model.addAttribute("lotacao", lotacao);
            return "lotacoes/detalhes";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/lotacoes?erro=Lotação+não+encontrada";
        }
    }

    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("lotacao", new Lotacao());
        return "lotacoes/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Lotacao lotacao) {
        try {
            lotacaoService.salvar(lotacao);
            return "redirect:/lotacoes?sucesso=Lotação+salva+com+sucesso";
        } catch (Exception e) {
            return "redirect:/lotacoes?erro=" + e.getMessage();
        }
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model) {
        try {
            Lotacao lotacao = lotacaoService.buscarLotacaoPeloId(id);
            model.addAttribute("lotacao", lotacao);
            return "lotacoes/formulario";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/lotacoes?erro=Lotação+não+encontrada";
        }
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable Integer id, @ModelAttribute Lotacao lotacao) {
        try {
            lotacaoService.atualizar(id, lotacao);
            return "redirect:/lotacoes?sucesso=Lotação+atualizada+com+sucesso";
        } catch (Exception e) {
            return "redirect:/lotacoes?erro=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            lotacaoService.excluir(id);
            return "redirect:/lotacoes?sucesso=Lotação+excluída+com+sucesso";
        } catch (Exception e) {
            return "redirect:/lotacoes?erro=" + e.getMessage();
        }
    }
}