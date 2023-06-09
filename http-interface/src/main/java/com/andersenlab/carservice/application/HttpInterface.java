package com.andersenlab.carservice.application;

import com.andersenlab.carservice.domain.CarServiceModule;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.EnumSet;

public final class HttpInterface {

    private final Server server;

    private HttpInterface(Server server) {
        this.server = server;
    }

    public static HttpInterface forModule(CarServiceModule module) {
        var server = new Server();

        var connector = new ServerConnector(server);
        connector.setPort(8080);

        var servletHandler = new ServletHandler();
        addServlets(module, servletHandler);
        addFilters(servletHandler);

        server.setConnectors(new Connector[]{connector});
        server.setHandler(servletHandler);
        return new HttpInterface(server);
    }

    private static void addFilters(ServletHandler servletHandler) {
        servletHandler.addFilterWithMapping(
                new FilterHolder(new BusinessExceptionHandlingFilter()),
                "/*",
                EnumSet.of(DispatcherType.REQUEST)
        );
        servletHandler.addFilterWithMapping(
                new FilterHolder(new GenericExceptionHandlingFilter()),
                "/*",
                EnumSet.of(DispatcherType.REQUEST)
        );
    }

    private static void addServlets(CarServiceModule module, ServletHandler servletHandler) {
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new RepairersServlet(
                                module.listRepairersUserCase(),
                                module.addRepairerUseCase()
                        )
                ),
                "/repairers"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new SingleRepairerServlet(module.deleteRepairerUseCase())
                ),
                "/repairers/*"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new GarageSlotsServlet(module.listGarageSlotsUseCase(), module.addGarageSlotUseCase())
                ),
                "/garage-slots"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new OrdersServlet(module.createOrderUseCase(), module.listOrdersUseCase())
                ),
                "/orders"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new CancelOrderServlet(module.cancelOrderUseCase())
                ),
                "/orders/cancel"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new AssignGarageSlotOrderServlet(module.assignGarageSlotToOrderUseCase())
                ),
                "/orders/assign/garage-slot"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new AssignRepairerOrderServlet(module.assignRepairerToOrderUseCase())
                ),
                "/orders/assign/repairer"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new CompleteOrderServlet(module.completeOrderUseCase())
                ),
                "/orders/complete"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new SingleOrderServlet(module.viewOrderUseCase())
                ),
                "/orders/*"
        );
        servletHandler.addServletWithMapping(
                new ServletHolder(
                        new SingleGarageSlotServlet(module.deleteGarageSlotUseCase())
                ),
                "/garage-slots/*"
        );
    }

    public void run() {
        try {
            server.start();
        } catch (Exception e) {
            throw new IllegalStateException("Could not start server", e);
        }
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception e) {
            throw new IllegalStateException("Could not stop server", e);
        }
    }
}
