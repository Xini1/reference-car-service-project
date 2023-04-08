package com.andersenlab.carservice;

import com.andersenlab.carservice.application.InMemoryGarageSlotStore;
import com.andersenlab.carservice.application.InMemoryOrderStore;
import com.andersenlab.carservice.application.InMemoryRepairerStore;
import com.andersenlab.carservice.application.TextInterface;
import com.andersenlab.carservice.application.command.*;
import com.andersenlab.carservice.domain.CarServiceModule;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class Main {

    public static void main(String[] args) {
        var module = new CarServiceModule(
                new InMemoryRepairerStore(),
                new InMemoryGarageSlotStore(),
                new InMemoryOrderStore(),
                Clock.systemDefaultZone()
        );
        var mainCommands = List.of(
                new CompositeCommand(
                        "repairers",
                        new AddRepairer(module.addRepairerUseCase()),
                        new ListRepairers(module.listRepairersUserCase()),
                        new DeleteRepairer(module.deleteRepairerUseCase())
                ),
                new CompositeCommand(
                        "garage-slots",
                        new AddGarageSlot(module.addGarageSlotUseCase()),
                        new ListGarageSlots(module.listGarageSlotsUseCase()),
                        new DeleteGarageSlot(module.deleteGarageSlotUseCase())
                )
        );
        new TextInterface(
                System.in,
                System.out,
                allCommands(mainCommands)
        )
                .run();
    }

    private static Collection<Command> allCommands(List<CompositeCommand> mainCommands) {
        var all = new ArrayList<Command>(mainCommands);
        all.add(new Help(mainCommands));
        return all;
    }
}
