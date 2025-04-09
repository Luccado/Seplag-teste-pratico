package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.model.Unidade;
import com.seplag.apiseplag.model.UnidadeEndereco;
import com.seplag.apiseplag.services.UnidadeEnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/unidades-enderecos")
public class UnidadeEnderecoController {

    @Autowired
    private UnidadeEnderecoService unidadeEnderecoService;

    @GetMapping
    public String listarTodos(Model model) {
        model.addAttribute("unidadesEnderecos", unidadeEnderecoService.listarTodos());
        return "unidades-enderecos/lista";
    }

    @GetMapping("/unidade/{unidadeId}")
    public String listarPorUnidade(@PathVariable Integer unidadeId, Model model) {
        try {
            List<UnidadeEndereco> unidadesEnderecos = unidadeEnderecoService.buscarPorUnidade(unidadeId);
            model.addAttribute("unidadesEnderecos", unidadesEnderecos);
            model.addAttribute("unidadeId", unidadeId);
            return "unidades-enderecos/lista-por-unidade";
        } catch (Exception e) {
            return "redirect:/unidades-enderecos?erro=" + e.getMessage();
        }
    }

    @GetMapping("/endereco/{enderecoId}")
    public String listarPorEndereco(@PathVariable Integer enderecoId, Model model) {
        try {
            List<UnidadeEndereco> unidadesEnderecos = unidadeEnderecoService.buscarPorEndereco(enderecoId);
            model.addAttribute("unidadesEnderecos", unidadesEnderecos);
            model.addAttribute("enderecoId", enderecoId);
            return "unidades-enderecos/lista-por-endereco";
        } catch (Exception e) {
            return "redirect:/unidades-enderecos?erro=" + e.getMessage();
        }
    }

    @GetMapping("/enderecos-por-unidade/{unidadeId}")
    public String listarEnderecosPorUnidade(@PathVariable Integer unidadeId, Model model) {
        try {
            List<Endereco> enderecos = unidadeEnderecoService.listarEnderecosPorUnidade(unidadeId);
            model.addAttribute("enderecos", enderecos);
            model.addAttribute("unidadeId", unidadeId);
            return "unidades-enderecos/enderecos-por-unidade";
        } catch (Exception e) {
            return "redirect:/unidades-enderecos?erro=" + e.getMessage();
        }
    }

    @GetMapping("/unidades-por-endereco/{enderecoId}")
    public String listarUnidadesPorEndereco(@PathVariable Integer enderecoId, Model model) {
        try {
            List<Unidade> unidades = unidadeEnderecoService.listarUnidadesPorEndereco(enderecoId);
            model.addAttribute("unidades", unidades);
            model.addAttribute("enderecoId", enderecoId);
            return "unidades-enderecos/unidades-por-endereco";
        } catch (Exception e) {
            return "redirect:/unidades-enderecos?erro=" + e.getMessage();
        }
    }

    @GetMapping("/associar")
    public String exibirFormularioAssociacao(Model model) {
        return "unidades-enderecos/formulario-associacao";
    }

    @PostMapping("/associar")
    public String associarEnderecoAUnidade(@RequestParam Integer unidadeId, @RequestParam Integer enderecoId) {
        try {
            unidadeEnderecoService.associarEnderecoAUnidade(unidadeId, enderecoId);
            return "redirect:/unidades-enderecos?sucesso=Endereço+associado+à+unidade+com+sucesso";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/unidades-enderecos/associar?erro=" + e.getMessage();
        } catch (IllegalStateException e) {
            return "redirect:/unidades-enderecos/associar?erro=" + e.getMessage();
        } catch (Exception e) {
            return "redirect:/unidades-enderecos/associar?erro=Erro+ao+associar:+" + e.getMessage();
        }
    }

    @GetMapping("/desassociar/{unidadeId}/{enderecoId}")
    public String desassociarEnderecoDeUnidade(@PathVariable Integer unidadeId, @PathVariable Integer enderecoId) {
        try {
            unidadeEnderecoService.desassociarEnderecoDeUnidade(unidadeId, enderecoId);
            return "redirect:/unidades-enderecos?sucesso=Associação+removida+com+sucesso";
        } catch (EmptyResultDataAccessException e) {
            return "redirect:/unidades-enderecos?erro=" + e.getMessage();
        } catch (Exception e) {
            return "redirect:/unidades-enderecos?erro=Erro+ao+remover+associação:+" + e.getMessage();
        }
    }
}