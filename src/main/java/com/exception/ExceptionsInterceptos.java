package com.exception;

import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.controller.ControllerInstance;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.DefaultInterceptorStack;
import br.com.caelum.vraptor.core.InterceptorStackHandlersCache;
import br.com.caelum.vraptor.events.InterceptorsExecuted;
import br.com.caelum.vraptor.events.InterceptorsReady;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import com.google.gson.JsonSyntaxException;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;

import static br.com.caelum.vraptor.view.Results.http;

@Specializes
public class ExceptionsInterceptos extends DefaultInterceptorStack {

    private final Result result;
    private static final Logger logger = LoggerFactory.getLogger(ExceptionsInterceptos.class);

    public ExceptionsInterceptos() {
        this(null, null, null, null, null, null);
    }

    @Inject
    public ExceptionsInterceptos(InterceptorStackHandlersCache cache,
            Instance<ControllerMethod> controllerMethod, Instance<ControllerInstance> controllerInstance,
            Event<InterceptorsExecuted> event, Event<InterceptorsReady> stackStartingEvent, Result result) {

        super(cache, controllerMethod, controllerInstance, event, stackStartingEvent);
        this.result = result;
    }

    // TODO: 5/22/16 documentar


    @Override
    public void next(ControllerMethod method, Object controllerInstance) throws InterceptionException {
        try {
            super.next(method, controllerInstance);
        } catch (Throwable e) {
            processException(e);
        }
    }

    @Override
    public void start() {
        try {
            super.start();
        } catch (Throwable e) {
            processException(e);
        }
    }

    private void processException(Throwable ex) {
        if (ex instanceof MongoException) {
            result.use(http()).setStatusCode(500);
            result.use(http()).body("MongoDB Error");

            logger.error("Unexpected error intercepted", ex);
        } else if (ex instanceof UnexpectedException) {
            result.use(http()).setStatusCode(500);
            result.use(http()).body(ex.getMessage());

            logger.error("Unexpected error intercepted", ex);
        } else if (ex.getCause() instanceof JsonSyntaxException) {
            result.use(http()).setStatusCode(400);
            result.use(http()).body("Invalid parameters");

            logger.error("Unexpected error intercepted", ex);
        } else {
            result.use(http()).setStatusCode(500);
            result.use(http()).body("Internal server error");

            logger.error("Unexpected error intercepted", ex);
        }
    }

}
