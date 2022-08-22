package br.com.microservice.connections;

import br.com.microservice.constantes.RabbitMQConstantes;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RabbitMQConnection {

    private static final String NOME_EXCHANGE = "amq.direct";
    private AmqpAdmin amqpAdmin;

    public RabbitMQConnection(AmqpAdmin amqpAdmin){
        this.amqpAdmin = amqpAdmin;
    }

    // 1º criamos a fila
    private Queue fila(String nomeFila){
        return new Queue(nomeFila, true, false, false);
    }

    // 2º criamos a exchange (usamos uma exchange default amp.direct)
    private DirectExchange trocaDireta(){
        return new DirectExchange(NOME_EXCHANGE);
    }

    // 3º - relacionamos a fila com a exchange
    private Binding relacionamento(Queue fila, DirectExchange troca){
        //return new Bindin
        return new Binding(fila.getName(), Binding.DestinationType.QUEUE, troca.getName(), fila.getName(), null);
    }

    @PostConstruct //Chama o metodo no momento de build da aplicação.
    private void adiciona(){
        // 4º instanciamos as filas
        Queue filaEstoque = this.fila(RabbitMQConstantes.FILA_ESTOQUE);
        Queue filaPreco = this.fila(RabbitMQConstantes.FILA_PRECO);

        // 5º instanciamos a exchange
        DirectExchange troca = this.trocaDireta();

        // 6º fazamos o bind da fila com a exchange
        Binding ligacaoEstoque = this.relacionamento(filaEstoque, troca);
        Binding ligacaoPreco = this.relacionamento(filaPreco, troca);

        //7º - Criando as filas no RabbitMQ.
        this.amqpAdmin.declareQueue(filaEstoque);
        this.amqpAdmin.declareQueue(filaPreco);

        //8º - criando a exchenge
        this.amqpAdmin.declareExchange(troca);

        //9º - criamos o relacionamento fila com exchange.
        this.amqpAdmin.declareBinding(ligacaoEstoque);
        this.amqpAdmin.declareBinding(ligacaoPreco);
    }
}
