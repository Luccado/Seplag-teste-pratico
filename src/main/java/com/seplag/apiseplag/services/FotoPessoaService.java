package com.seplag.apiseplag.services;

import com.seplag.apiseplag.model.FotoPessoa;
import com.seplag.apiseplag.model.Pessoa;
import com.seplag.apiseplag.repository.FotoPessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FotoPessoaService {

    private final FotoPessoaRepository fotoPessoaRepository;
    private final PessoaService pessoaService;

    /**
     * Lista todas as fotos de pessoas cadastradas no sistema
     * @return Lista de todas as fotos
     */
    public List<FotoPessoa> listarTodasFotos() {
        return fotoPessoaRepository.findAll();
    }

    @Transactional
    public FotoPessoa uploadFoto(MultipartFile arquivo, Integer pessoaId) {
        if (arquivo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo está vazio");
        }

        try {

            Optional<Pessoa> pessoaOptional = pessoaService.buscarPorId(pessoaId);
            if (pessoaOptional.isEmpty()) {
                throw new IllegalArgumentException("Pessoa não encontrada com o ID: " + pessoaId);
            }
            Pessoa pessoa = pessoaOptional.get();

            FotoPessoa fotoPessoa = new FotoPessoa();
            fotoPessoa.setPessoa(pessoa);
            fotoPessoa.setData(LocalDate.now());
            fotoPessoa.setBucket("fotos-pessoas");

            String hash = gerarHash(arquivo);
            fotoPessoa.setHash(hash);

            return fotoPessoaRepository.save(fotoPessoa);

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao processar o arquivo: " + e.getMessage());
        }
    }


    private String gerarHash(MultipartFile arquivo) throws IOException, NoSuchAlgorithmException {

        String nomeArquivo = arquivo.getOriginalFilename();
        String randomUUID = UUID.randomUUID().toString().substring(0, 8);

        return (nomeArquivo + "_" + randomUUID).substring(0, 50);
    }
}