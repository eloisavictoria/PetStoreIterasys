//1 - pacote: grupo de classes
package petstore;

//2 - bibliotecas

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

//3 - classe
public class Pet {
    //3.1 - atributos
    String uri = "https://petstore.swagger.io/v2/pet"; //endereço da interface swagger pet



    //3.2 - metodos (não devolvem resultado) e funções (retornam um resultado)

    //public - outras classes podem ver a função
    //private - somente a classe pode ver a função
    //void - método que não retorna informação
    //string - texto
    //files - arquivos
    //Paths.get - pega  o caminho..???
    //Files.readAllBytes - lê o caminho..??
    //caminhoJson é uma variavel que vai carregar o caminho json
    public String lerJson(String caminhoJson) throws IOException {
    //função para ler a estrutura de qualquer arquivo json desde que eu passe o caminho e o retorno será o arquivo json
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }//retornar uma string com o conteúdo do json

    //Incluir - creat - post
    @Test //notação para saber que o método é um teste. Identifica o método ou função como um teste para o TestNG
    public void incluirPet() throws IOException {
        String jsonBody = lerJson("db/cachorro.json");

        //Sintaxe Gherkin
        //Dado - Quando - Então
        //Given = When - Then

        //como se tudo fosse uma só linha, nã precisa de ponto e virgula
        //inicio da linha
        given() //dado - pré condições
                .contentType("application/json") //comum em API REST - antigas era 'text/xml'
                .log().all() //registre tudo - envio - requisição
                .body(jsonBody) //arquivo que vou enviar para a função poder ler e depois dar o retorno
        .when() //quando eu fizer
                .post(uri) //um post para o endereço
        .then() //então
                .log().all() //registre tudo - retorno - resposta
                .statusCode(200)//checar se a transação foi e voltou
        ;//fim da linha
    }

}
