package com.business.interceptor;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import com.exception.UnexpectedException;
import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static br.com.caelum.vraptor.view.Results.http;

@Intercepts
public class ExceptionsInterceptos {

    private final Result result;
    private static final Logger logger = LoggerFactory.getLogger(ExceptionsInterceptos.class);

    public ExceptionsInterceptos() {
        this(null);
    }

    @Inject
    public ExceptionsInterceptos(Result result) {
        this.result = result;
    }

    @Accepts
    public boolean intercepts() {
        return true;
    }

    // TODO: 5/22/16 documentar
    @AroundCall
    public void interceptarExcecoes(SimpleInterceptorStack stack) {
        try {
            stack.next();
        } catch (MongoException ex) {
            result.use(http()).setStatusCode(500);
            result.use(http()).body("MongoDB Error");
            // TODO: 5/22/16 check response

            logger.error("Unexpected error intercepted", ex);
        }  catch (UnexpectedException ex) {
            result.use(http()).setStatusCode(500);
            result.use(http()).body(ex.getMessage());
            // TODO: 5/22/16 check response

            logger.error("Unexpected error intercepted", ex);
        } catch (Throwable ex) {
            result.use(http()).setStatusCode(500);
            result.use(http()).body("Internal server error");
            // TODO: 5/22/16 check response

            logger.error("Unexpected error intercepted", ex);
        }
    }

}
