package de.ml.endpoints;

import java.util.Map;

import javax.inject.Inject;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;
import org.apache.camel.component.restlet.RestletEndpoint;
import org.restlet.data.Method;

import com.google.common.base.Preconditions;

import de.ml.boot.AllowedUserProvider.AllowedUsers;
import de.ml.boot.ArgsConfiguration.Port;

public class RestEndpointsProvider implements RestEndpoints {

    private Map<String, String> users;
    private CamelContext context;
    private Integer port;
    public static final String HEADER_AUTO_PARAMETER = "autoTime";
    public static final String HEADER_NAME_PARAMETER = "inName";
    public static final String AUTO_PATH = "auto";
    public static final String INFO_PATH = "info";

    @Inject
    private RestEndpointsProvider(CamelContext context, @AllowedUsers Map<String, String> users, @Port Integer port) {
        this.context = Preconditions.checkNotNull(context);
        this.users = Preconditions.checkNotNull(users);
        this.port = Preconditions.checkNotNull(port);

    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#next()
     */
    @Override
    public Endpoint next() {
        return getRestEndpoint("/next");
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#nextAuto()
     */
    @Override
    public Endpoint nextAuto() {
        return getRestEndpoint("/next/" + AUTO_PATH);
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#nextAutoTime()
     */
    @Override
    public Endpoint nextAutoTime() {
        return getRestEndpoint("/next/" + AUTO_PATH + "/{" + HEADER_AUTO_PARAMETER + "}");
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#update()
     */
    @Override
    public Endpoint update() {
        return getRestEndpoint("/update");
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#prev()
     */
    @Override
    public Endpoint prev() {
        return getRestEndpoint("/prev");
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#info()
     */
    @Override
    public Endpoint info() {
        return getRestEndpoint("/" + INFO_PATH);
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#current()
     */
    @Override
    public Endpoint current() {
        return getRestEndpoint("/current");
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#filterName()
     */
    @Override
    public Endpoint filterName() {
        return getRestEndpoint("/{" + HEADER_NAME_PARAMETER + "}");
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#filterNameAuto()
     */
    @Override
    public Endpoint filterNameAuto() {
        return getRestEndpoint("/{" + HEADER_NAME_PARAMETER + "}/" + AUTO_PATH);
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#filterNameInfo()
     */
    @Override
    public Endpoint filterNameInfo() {
        return getRestEndpoint("/{" + HEADER_NAME_PARAMETER + "}/" + INFO_PATH);
    }

    /* (non-Javadoc)
     * @see de.ml.endpoints.RestEndpoints#filterNameAutoTime()
     */
    @Override
    public Endpoint filterNameAutoTime() {
        return getRestEndpoint("/{" + HEADER_NAME_PARAMETER + "}/" + AUTO_PATH + "/{" + HEADER_AUTO_PARAMETER + "}");
    }

    private Endpoint getRestEndpoint(String path) {
        RestletEndpoint endpoint = context.getEndpoint("restlet:http://localhost" + path, RestletEndpoint.class);
        endpoint.setRestletMethod(Method.GET);
        endpoint.setPort(port);
        endpoint.setRestletRealm(users);
        return endpoint;
    }

}
