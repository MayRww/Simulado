import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Leitura de Pessoas.csv e criação da lista de Pessoa
        List<Pessoa> pessoas = lerPessoasDoCSV("Pessoas.csv");

        // Leitura de Enderecos.csv e criação do mapa de Endereco
        Map<Integer, List<Endereco>> mapEnderecos = lerEnderecosDoCSV("Enderecos.csv");

        // Criar lista de PessoasComEndereco combinando informações
        List<PessoaComEndereco> pessoasComEndereco = new ArrayList<>();
        for (Pessoa pessoa : pessoas) {
            List<Endereco> enderecos = mapEnderecos.get(pessoa.getCodigo());
            if (enderecos != null) {
                for (Endereco endereco : enderecos) {
                    pessoasComEndereco.add(new PessoaComEndereco(pessoa, endereco));
                }
            }
        }

        // Escrever PessoasComEndereco.csv
        escreverPessoasComEnderecoCSV(pessoasComEndereco, "PessoasComEndereco.csv");

        // Imprimir resultados no console
        for (PessoaComEndereco pessoaComEndereco : pessoasComEndereco) {
            System.out.println(pessoaComEndereco);
        }
    }

    // Método para ler Pessoas.csv e retornar uma lista de Pessoa
    private static List<Pessoa> lerPessoasDoCSV(String arquivo) {
        List<Pessoa> pessoas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Verifica se a linha não está vazia ou em branco
                if (linha.trim().isEmpty()) {
                    continue; // Pula para a próxima linha
                }
                String[] dados = linha.split(";");
                // Verifica se há dados suficientes na linha
                if (dados.length < 2) {
                    System.out.println("Formato inválido na linha de Pessoas.csv: " + linha);
                    continue; // Pula para a próxima linha
                }
                try {
                    int codigo = Integer.parseInt(dados[0].trim());
                    String nome = dados[1].trim();
                    pessoas.add(new Pessoa(codigo, nome));
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao converter código na linha de Pessoas.csv: " + linha);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pessoas;
    }

    // Método para ler Enderecos.csv e retornar um mapa de código para Endereco
    private static Map<Integer, List<Endereco>> lerEnderecosDoCSV(String arquivo) {
        Map<Integer, List<Endereco>> mapEnderecos = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Verifica se a linha não está vazia ou em branco
                if (linha.trim().isEmpty()) {
                    continue; // Pula para a próxima linha
                }
                String[] dados = linha.split(";");
                // Verifica se há dados suficientes na linha
                if (dados.length < 3) {
                    System.out.println("Formato inválido na linha de Enderecos.csv: " + linha);
                    continue; // Pula para a próxima linha
                }
                try {
                    String rua = dados[0].trim();
                    String cidade = dados[1].trim();
                    int codigo = Integer.parseInt(dados[2].trim());
                    mapEnderecos.computeIfAbsent(codigo, k -> new ArrayList<>()).add(new Endereco(rua, cidade));
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao converter código na linha de Enderecos.csv: " + linha);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapEnderecos;
    }

    // Método para escrever PessoasComEndereco.csv
    private static void escreverPessoasComEnderecoCSV(List<PessoaComEndereco> pessoasComEndereco, String arquivo) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo))) {
            for (PessoaComEndereco pessoaComEndereco : pessoasComEndereco) {
                bw.write(pessoaComEndereco.toCsv());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Classe PessoaComEndereco para combinar Pessoa e Endereco
class PessoaComEndereco {
    private Pessoa pessoa;
    private Endereco endereco;

    public PessoaComEndereco(Pessoa pessoa, Endereco endereco) {
        this.pessoa = pessoa;
        this.endereco = endereco;
    }

    public String toCsv() {
        return pessoa.getCodigo() + ";" + pessoa.getNome() + ";" +
               endereco.getRua() + ";" + endereco.getCidade();
    }

    @Override
    public String toString() {
        return pessoa.getCodigo() + ";" + pessoa.getNome() + ";" +
               endereco.getRua() + ";" + endereco.getCidade();
    }
}

class Pessoa {
    private int codigo;
    private String nome;

    public Pessoa(int codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Pessoa{codigo=" + codigo + ", nome='" + nome + "'}";
    }
}

class Endereco {
    private String rua;
    private String cidade;

    public Endereco(String rua, String cidade) {
        this.rua = rua;
        this.cidade = cidade;
    }

    public String getRua() {
        return rua;
    }

    public String getCidade() {
        return cidade;
    }

    @Override
    public String toString() {
        return "Endereco{rua='" + rua + "', cidade='" + cidade + "'}";
    }
}
