package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Endereco;
import com.seplag.apiseplag.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    @Autowired
    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping
    public String listarTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "logradouro") String ordenarPor,
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

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) {
        Endereco endereco = enderecoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Endereço inválido com ID: " + id));

        model.addAttribute("endereco", endereco);
        return "endereco/detalhes";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("endereco", new Endereco());
        return "endereco/formulario";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model) {
        Endereco endereco = enderecoService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Endereço inválido com ID: " + id));

        model.addAttribute("endereco", endereco);
        return "endereco/formulario";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Endereco endereco) {
        enderecoService.salvar(endereco);
        return "redirect:/enderecos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        enderecoService.excluir(id);
        return "redirect:/enderecos";
    }

    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String logradouro,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) Integer cidadeId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "logradouro") String ordenarPor,
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