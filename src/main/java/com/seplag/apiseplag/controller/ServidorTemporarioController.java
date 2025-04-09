package com.seplag.apiseplag.controller;

import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.model.ServidorTemporario;
import com.seplag.apiseplag.services.PessoaService;
import com.seplag.apiseplag.services.ServidorTemporarioService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/servidores-temporarios")
@Hidden
public class ServidorTemporarioController {

    private final ServidorTemporarioService servidorTemporarioService;
    private final PessoaService pessoaService;

    @Autowired
    public ServidorTemporarioController(ServidorTemporarioService servidorTemporarioService,
                                        PessoaService pessoaService) {
        this.servidorTemporarioService = servidorTemporarioService;
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public String listarTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "dataAdmissao") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao,
            Model model) {

        int tamanhoPagina = 10;
        Pageable pageable = PageRequest.of(
                pagina, tamanhoPagina,
                direcao.equals("asc") ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending()
        );

        Page<ServidorTemporario> servidores = servidorTemporarioService.listarTodos(pageable);

        model.addAttribute("servidores", servidores);
        model.addAttribute("paginaAtual", pagina);
        model.addAttribute("ordenarPor", ordenarPor);
        model.addAttribute("direcao", direcao);
        model.addAttribute("totalPaginas", servidores.getTotalPages());

        return "servidores/lista";
    }

    @GetMapping("/{id}")
    public String detalhar(@PathVariable Integer id, Model model) {
        Optional<ServidorTemporario> servidor = servidorTemporarioService.buscarPorId(id);

        if (servidor.isPresent()) {
            model.addAttribute("servidor", servidor.get());
            return "servidores/detalhes";
        } else {
            return "redirect:/servidores-temporarios?erro=Servidor+não+encontrado";
        }
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("servidor", new ServidorTemporario());
        model.addAttribute("pessoas", pessoaService.buscarTodas(Pageable.unpaged()).getContent());
        return "servidores/formulario";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Integer id, Model model) {
        Optional<ServidorTemporario> servidor = servidorTemporarioService.buscarPorId(id);

        if (servidor.isPresent()) {
            model.addAttribute("servidor", servidor.get());
            model.addAttribute("pessoas", pessoaService.buscarTodas(Pageable.unpaged()).getContent());
            return "servidores/formulario";
        } else {
            return "redirect:/servidores-temporarios?erro=Servidor+não+encontrado";
        }
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute ServidorTemporario servidor) {
        try {
            servidorTemporarioService.salvar(servidor);
            return "redirect:/servidores-temporarios?sucesso=Servidor+salvo+com+sucesso";
        } catch (Exception e) {
            return "redirect:/servidores-temporarios?erro=" + e.getMessage();
        }
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        try {
            servidorTemporarioService.excluir(id);
            return "redirect:/servidores-temporarios?sucesso=Servidor+excluído+com+sucesso";
        } catch (Exception e) {
            return "redirect:/servidores-temporarios?erro=" + e.getMessage();
        }
    }

    @GetMapping("/api/listar")
    @ResponseBody
    public ResponseEntity<Page<ServidorTemporario>> apiListarTodos(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "dataAdmissao") String ordenarPor,
            @RequestParam(defaultValue = "asc") String direcao) {

        int tamanhoPagina = 10;
        Pageable pageable = PageRequest.of(
                pagina, tamanhoPagina,
                direcao.equals("asc") ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending()
        );

        Page<ServidorTemporario> servidores = servidorTemporarioService.listarTodos(pageable);
        return ResponseEntity.ok(servidores);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ServidorTemporario> apiBuscarPorId(@PathVariable Integer id) {
        Optional<ServidorTemporario> servidor = servidorTemporarioService.buscarPorId(id);

        return servidor
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/criar")
    @ResponseBody
    public ResponseEntity<ServidorTemporario> apiCriar(@RequestBody ServidorTemporario servidor) {
        try {
            ServidorTemporario novoServidor = servidorTemporarioService.salvar(servidor);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoServidor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<ServidorTemporario> apiAtualizar(
            @PathVariable Integer id,
            @RequestBody ServidorTemporario servidor) {

        try {
            ServidorTemporario servidorAtualizado = servidorTemporarioService.atualizar(id, servidor);
            return ResponseEntity.ok(servidorAtualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> apiExcluir(@PathVariable Integer id) {
        try {
            servidorTemporarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/admissao")
    @ResponseBody
    public ResponseEntity<List<ServidorTemporario>> apiBuscarPorDataAdmissao(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        List<ServidorTemporario> servidores = servidorTemporarioService.buscarPorDataAdmissao(data);
        return ResponseEntity.ok(servidores);
    }

    @GetMapping("/api/periodo")
    @ResponseBody
    public ResponseEntity<List<ServidorTemporario>> apiBuscarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        List<ServidorTemporario> servidores = servidorTemporarioService.buscarPorPeriodoAdmissao(inicio, fim);
        return ResponseEntity.ok(servidores);
    }

    @GetMapping("/api/ativos")
    @ResponseBody
    public ResponseEntity<List<ServidorTemporario>> apiBuscarAtivos() {
        List<ServidorTemporario> servidores = servidorTemporarioService.buscarServidoresAtivos();
        return ResponseEntity.ok(servidores);
    }

    @GetMapping("/api/inativos")
    @ResponseBody
    public ResponseEntity<List<ServidorTemporario>> apiBuscarInativos() {
        List<ServidorTemporario> servidores = servidorTemporarioService.buscarServidoresInativos();
        return ResponseEntity.ok(servidores);
    }
}