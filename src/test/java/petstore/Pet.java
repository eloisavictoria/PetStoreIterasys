//1 - pacote: grupo de classes
package petstore;

//2 - bibliotecas

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;

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
    public String lerJson(String caminhoJson) throws IOException {//função
    //função para ler a estrutura de qualquer arquivo json desde que eu passe o caminho e o retorno será o arquivo json
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }//retornar uma string com o conteúdo do json

    //Incluir - creat - post
    @Test(priority = 1) //notação para saber que o método é um teste. Identifica o método ou função como um teste para o TestNG
    public void incluirPet() throws IOException {//método
        String jsonBody = lerJson("db/cachorro.json");

        //Sintaxe Gherkin
        //Dado - Quando - Então
        //Given = When - Then

        //REST-Assured
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
                //o statuscode só checa que foi feito o envio com sucesso,não significa que o processo de inserção deu certo
                .body("name", is("diana")) //valida se o nome do cachorro é Lilica
                .body("status", is("available")) //valida se o status é available
                .body("category.name", is("AX2345XPTO"))//quando quero pegar informação de dentro de uma tag
                .body("tags.name", contains("data"))
        //quando tem colchete no arquivo json (significa que tem uma lista) então preciso usar o contains
        //is é usado para quando é uma informação que não é uma lista
        //bpdy é usado para incluir
        ;//fim da linha
    }

    @Test(priority = 2) //prioridade de execução, só funciona caso você execute o teste do arquivo do build.gradle
    public void consultarPet(){
        String petId = "13211931";

        String token=
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/"+ petId) //copiar o endereço e unir com a / junto com o código do pet
        .then()
                .statusCode(200)
                .log().all()
                .body("name", is("diana"))
                .body("category.name", is("AX2345XPTO"))
                .body("status", is("available"))
        .extract() //testamos para poder extrair um token, que é uma idfentificação de transação
                .path("category.name");
        System.out.println("O token é "+token);

    }
}
