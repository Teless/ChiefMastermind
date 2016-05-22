package com.business.interceptor;

import br.com.caelum.vraptor.Accepts;
import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import com.business.log.ProfilerLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.profiler.Profiler;

@Intercepts
public class ProfilerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ProfilerInterceptor.class);

    @Accepts
    public boolean intercepts() {
        return true;
    }

    @AroundCall
    public void interceptarExcecoes(SimpleInterceptorStack stack, ControllerMethod method) {

        Profiler profiler = new Profiler(ProfilerInterceptor.class.getSimpleName());
        profiler.setLogger(ProfilerLog.LOGGER);

        try {
            profiler.start(method.getController().getType().getSimpleName() + ':' + method.getMethod().getName());
            stack.next();
        } catch (Throwable ex) {
            logger.error("Error while profiling call", ex);
        } finally {
            profiler.stop().log();
        }

    }

}
