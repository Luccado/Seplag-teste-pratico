package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.ServidorEfetivo;
import com.seplag.apiseplag.services.PessoaService;
import com.seplag.apiseplag.services.ServidorEfetivoService;
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
@RequestMapping("/servidores-efetivos")
public class ServidorEfetivoController {

    private final ServidorEfetivoService servidorEfetivoService;
    private final PessoaService pessoaService;

    @Autowired
    public ServidorEfetivoController(ServidorEfetivoService servidorEfetivoService,
                                     PessoaService pessoaService) {
        this.servidorEfetivoService = servidorEfetivoService;
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public String listarTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "matricula") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao,
            Model model) {

        int tamanhoPagina = 10;
        Pageable pageable = PageRequest.of(
                pagina, tamanhoPagina,
                direcao.equals("asc") ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending()
        );

        Page<ServidorEfetivo> servidores = servidorEfetivoService.listarTodos(pageable);

        model.addAttribute("servidores", servidores);
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("ordenarPor", ordenarPor);
        model.addAttribute("direcao", direcao);
        model.addAttribute("totalPaginas", servidores.getTotalPages());

        return "servidores-efetivos/lista";
    }

    @GetMapping("/{id}")
    public String buscarPorId(@PathVariable Integer id, Model model) {
        Optional<ServidorEfetivo> servidor = servidorEfetivoService.buscarPorId(id);

        if (servidor.isPresent()) {
            model.addAttribute("servidor", servidor.get());
            return "servidores-efetivos/detalhes";
        } else {
            return "redirect:/servidores-efetivos?erro=Servidor+não+encontrado";
        }
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("servidor", new ServidorEfetivo());
        model.addAttribute("pessoas", pessoaService.buscarTodas(Pageable.unpaged()).getContent());
        return "servidores-efetivos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model) {
        Optional<ServidorEfetivo> servidor = servidorEfetivoService.buscarPorId(id);

        if (servidor.isPresent()) {
            model.addAttribute("servidor", servidor.get());
            model.addAttribute("pessoas", pessoaService.buscarTodas(Pageable.unpaged()).getContent());
            return "servidores-efetivos/formulario";
        } else {
            return "redirect:/servidores-efetivos?erro=Servidor+não+encontrado";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ServidorEfetivo servidor) {
        try {
            servidorEfetivoService.salvar(servidor);
            return "redirect:/servidores-efetivos?sucesso=Servidor+salvo+com+sucesso";
        } catch (Exception e) {
            return "redirect:/servidores-efetivos?erro=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            servidorEfetivoService.excluir(id);
            return "redirect:/servidores-efetivos?sucesso=Servidor+excluído+com+sucesso";
        } catch (Exception e) {
            return "redirect:/servidores-efetivos?erro=" + e.getMessage();
        }
    }
}