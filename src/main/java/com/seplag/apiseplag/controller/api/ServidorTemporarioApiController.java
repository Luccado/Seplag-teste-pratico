package com.seplag.apiseplag.controller.api;

import com.seplag.apiseplag.model.ServidorTemporario;
import com.seplag.apiseplag.services.ServidorTemporarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/servidores-temporarios")
@Tag(name = "Servidor Temporário", description = "API para gerenciamento de servidores temporários")
public class ServidorTemporarioApiController {

    private final ServidorTemporarioService servidorTemporarioService;

    @Autowired
    public ServidorTemporarioApiController(ServidorTemporarioService servidorTemporarioService) {
        this.servidorTemporarioService = servidorTemporarioService;
    }

    @Operation(
        summary = "Listar todos os servidores temporários",
        description = "Retorna uma lista paginada de todos os servidores temporários cadastrados"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de servidores temporários recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<Page<ServidorTemporario>> listarTodos(
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") int pagina,
            @Parameter(description = "Campo para ordenação", example = "dataAdmissao")
            @RequestParam(defaultValue = "dataAdmissao") String ordenarPor,
            @Parameter(description = "Direção da ordenação (asc ou desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String direcao) {

        int tamanhoPagina = 10;
        Pageable pageable = PageRequest.of(
                pagina, tamanhoPagina,
                direcao.equals("asc") ? Sort.by(ordenarPor).ascending() : Sort.by(ordenarPor).descending()
        );

        Page<ServidorTemporario> servidores = servidorTemporarioService.listarTodos(pageable);
        return ResponseEntity.ok(servidores);
    }

    @Operation(
        summary = "Buscar servidor temporário por ID",
        description = "Retorna um servidor temporário específico pelo seu ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor temporário encontrado",
                    content = @Content(schema = @Schema(implementation = ServidorTemporario.class))),
        @ApiResponse(responseCode = "404", description = "Servidor temporário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ServidorTemporario> buscarPorId(
            @Parameter(description = "ID do servidor temporário", required = true, example = "1")
            @PathVariable Integer id) {
        
        return servidorTemporarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Criar novo servidor temporário",
        description = "Cria um novo registro de servidor temporário"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Servidor temporário criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ServidorTemporario.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<ServidorTemporario> criar(
            @Parameter(description = "Dados do servidor temporário a ser criado", required = true)
            @RequestBody ServidorTemporario servidor) {
        try {
            ServidorTemporario novoServidor = servidorTemporarioService.salvar(servidor);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoServidor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
        summary = "Atualizar servidor temporário",
        description = "Atualiza os dados de um servidor temporário existente"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servidor temporário atualizado com sucesso",
                    content = @Content(schema = @Schema(implementation = ServidorTemporario.class))),
        @ApiResponse(responseCode = "404", description = "Servidor temporário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServidorTemporario> atualizar(
            @Parameter(description = "ID do servidor temporário", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Dados atualizados do servidor temporário", required = true)
            @RequestBody ServidorTemporario servidor) {
        try {
            ServidorTemporario servidorAtualizado = servidorTemporarioService.atualizar(id, servidor);
            return ResponseEntity.ok(servidorAtualizado);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Excluir servidor temporário",
        description = "Remove um servidor temporário do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Servidor temporário excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Servidor temporário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            @Parameter(description = "ID do servidor temporário", required = true, example = "1")
            @PathVariable Integer id) {
        try {
            servidorTemporarioService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
        summary = "Buscar servidores temporários por data de admissão",
        description = "Retorna uma lista de servidores temporários que foram admitidos em uma data específica"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de servidores temporários recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/por-data-admissao")
    public ResponseEntity<List<ServidorTemporario>> buscarPorDataAdmissao(
            @Parameter(description = "Data de admissão no formato ISO (YYYY-MM-DD)", required = true, example = "2023-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        List<ServidorTemporario> servidores = servidorTemporarioService.buscarPorDataAdmissao(data);
        return ResponseEntity.ok(servidores);
    }

    @Operation(
        summary = "Buscar servidores temporários por período de admissão",
        description = "Retorna uma lista de servidores temporários que foram admitidos em um período específico"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de servidores temporários recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/por-periodo")
    public ResponseEntity<List<ServidorTemporario>> buscarPorPeriodo(
            @Parameter(description = "Data inicial do período no formato ISO (YYYY-MM-DD)", required = true, example = "2023-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Data final do período no formato ISO (YYYY-MM-DD)", required = true, example = "2023-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        
        List<ServidorTemporario> servidores = servidorTemporarioService.buscarPorPeriodoAdmissao(inicio, fim);
        return ResponseEntity.ok(servidores);
    }

    @Operation(
        summary = "Listar servidores temporários ativos",
        description = "Retorna uma lista de servidores temporários que ainda não foram demitidos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de servidores temporários ativos recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/ativos")
    public ResponseEntity<List<ServidorTemporario>> buscarAtivos() {
        List<ServidorTemporario> servidores = servidorTemporarioService.buscarServidoresAtivos();
        return ResponseEntity.ok(servidores);
    }

    @Operation(
        summary = "Listar servidores temporários inativos",
        description = "Retorna uma lista de servidores temporários que já foram demitidos"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de servidores temporários inativos recuperada com sucesso",
                    content = @Content(schema = @Schema(implementation = List.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/inativos")
    public ResponseEntity<List<ServidorTemporario>> buscarInativos() {
        List<ServidorTemporario> servidores = servidorTemporarioService.buscarServidoresInativos();
        return ResponseEntity.ok(servidores);
    }
} 