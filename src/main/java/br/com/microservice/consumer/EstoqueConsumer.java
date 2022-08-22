package br.com.microservice.consumer;

import br.com.microservice.dto.EstoqueDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static br.com.microservice.constantes.RabbitMQConstantes.FILA_ESTOQUE;

@Component
public class EstoqueConsumer {
    @RabbitListener(queues = FILA_ESTOQUE)
    private void cunsumidor(EstoqueDto estoqueDto){
        System.out.println(estoqueDto.codigoProduto +" - "+ estoqueDto.quantidade);
    }
}
