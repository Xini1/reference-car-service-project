package com.andersenlab.carservice.application;

import com.andersenlab.carservice.port.usecase.CompleteOrderUseCase;

import java.io.PrintStream;
import java.util.List;
import java.util.UUID;

final class CompleteOrder extends NamedCommandWithDescription {

    private final CompleteOrderUseCase useCase;

    CompleteOrder(CompleteOrderUseCase useCase) {
        super("complete");
        this.useCase = useCase;
    }

    @Override
    List<String> expectedArguments() {
        return List.of("id");
    }

    @Override
    String description() {
        return "complete an order";
    }

    @Override
    void executeIfMatched(PrintStream output, List<String> arguments) {
        var id = UUID.fromString(arguments.get(0));
        useCase.complete(id);
        output.println("Order completed " + id);
    }
}
