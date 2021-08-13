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
    String uri = "https://petstore.swagger.io/v2/pet"; //endere�o da interface swagger pet



    //3.2 - metodos (n�o devolvem resultado) e fun��es (retornam um resultado)

    //public - outras classes podem ver a fun��o
    //private - somente a classe pode ver a fun��o
    //void - m�todo que n�o retorna informa��o
    //string - texto
    //files - arquivos
    //Paths.get - pega  o caminho..???
    //Files.readAllBytes - l� o caminho..??
    //caminhoJson � uma variavel que vai carregar o caminho json
    public String lerJson(String caminhoJson) throws IOException {//fun��o
    //fun��o para ler a estrutura de qualquer arquivo json desde que eu passe o caminho e o retorno ser� o arquivo json
        return new String(Files.readAllBytes(Paths.get(caminhoJson)));
    }//retornar uma string com o conte�do do json

    //Incluir - creat - post
    @Test(priority = 1) //nota��o para saber que o m�todo � um teste. Identifica o m�todo ou fun��o como um teste para o TestNG
    public void incluirPet() throws IOException {//m�todo
        String jsonBody = lerJson("db/cachorro.json");

        //Sintaxe Gherkin
        //Dado - Quando - Ent�o
        //Given = When - Then

        //REST-Assured
        //como se tudo fosse uma s� linha, n� precisa de ponto e virgula
        //inicio da linha
        given() //dado - pr� condi��es
                .contentType("application/json") //comum em API REST - antigas era 'text/xml'
                .log().all() //registre tudo - envio - requisi��o
                .body(jsonBody) //arquivo que vou enviar para a fun��o poder ler e depois dar o retorno
        .when() //quando eu fizer
                .post(uri) //um post para o endere�o
        .then() //ent�o
                .log().all() //registre tudo - retorno - resposta
                .statusCode(200)//checar se a transa��o foi e voltou
                //o statuscode s� checa que foi feito o envio com sucesso,n�o significa que o processo de inser��o deu certo
                .body("name", is("diana")) //valida se o nome do cachorro � Lilica
                .body("status", is("available")) //valida se o status � available
                .body("category.name", is("AX2345XPTO"))//quando quero pegar informa��o de dentro de uma tag
                .body("tags.name", contains("data"))
        //quando tem colchete no arquivo json (significa que tem uma lista) ent�o preciso usar o contains
        //is � usado para quando � uma informa��o que n�o � uma lista
        //bpdy � usado para incluir
        ;//fim da linha
    }

    @Test(priority = 2) //prioridade de execu��o, s� funciona caso voc� execute o teste do arquivo do build.gradle
    public void consultarPet(){
        String petId = "13211931";

        String token=
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get(uri + "/"+ petId) //copiar o endere�o e unir com a / junto com o c�digo do pet
        .then()
                .statusCode(200)
                .log().all()
                .body("name", is("diana"))
                .body("category.name", is("AX2345XPTO"))
                .body("status", is("available"))
        .extract() //testamos para poder extrair um token, que � uma idfentifica��o de transa��o
                .path("category.name");
        System.out.println("O token � "+token);

    }
}
