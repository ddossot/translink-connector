
package org.mule.modules;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleContext;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Module;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.TransformerResolver;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.rest.HttpMethod;
import org.mule.api.annotations.rest.RestCall;
import org.mule.api.annotations.rest.RestExceptionOn;
import org.mule.api.annotations.rest.RestHeaderParam;
import org.mule.api.annotations.rest.RestQueryParam;
import org.mule.api.annotations.rest.RestUriParam;
import org.mule.api.transformer.DataType;
import org.mule.module.json.transformers.JsonToObject;
import org.mule.transformer.types.MimeTypes;
import org.mule.transformer.types.SimpleDataType;

@Module(name = "translink", schemaVersion = "3.4", friendlyName = "TransLink", minMuleVersion = "3.4.0", description = "TransLink Open API")
public abstract class TransLinkConnector
{
    public enum Service
    {
        All, Schedule, Location
    };

    /**
     * Your ApiKey received during registration.
     */
    @Configurable
    @Optional
    @RestQueryParam("apiKey")
    private String apiKey;

    @RestHeaderParam("Accept")
    private final String acceptJsonHeader = MimeTypes.JSON;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/stops/{stopNumber}", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract Map<String, Object> getStop(@RestUriParam("stopNumber") int stopNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/stops", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getStops(@RestQueryParam("Lat") double latitude,
                                                       @RestQueryParam("Long") double longitude,
                                                       @RestQueryParam("Radius") @Optional @Default("500") int radius)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/stops", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getStopsForRoute(@RestQueryParam("Lat") double latitude,
                                                               @RestQueryParam("Long") double longitude,
                                                               @RestQueryParam("Radius") @Optional @Default("500") int radius,
                                                               @RestQueryParam("RouteNo") String routeNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/stops/{stopNumber}/estimates", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getStopEstimates(@RestUriParam("stopNumber") int stopNumber,
                                                               @RestQueryParam("Count") @Optional @Default("6") int count,
                                                               @RestQueryParam("TimeFrame") @Optional @Default("1440") int timeFrame)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/stops/{stopNumber}/estimates", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getStopEstimatesForRoute(@RestUriParam("stopNumber") int stopNumber,
                                                                       @RestQueryParam("Count") @Optional @Default("6") int count,
                                                                       @RestQueryParam("TimeFrame") @Optional @Default("1440") int timeFrame,
                                                                       @RestQueryParam("RouteNo") String routeNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/buses/{busNumber}", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract Map<String, Object> getBus(@RestUriParam("busNumber") int busNumber) throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/buses", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getBusesAtStop(@RestQueryParam("StopNo") int stopNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/buses", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getBusesForRoute(@RestQueryParam("RouteNo") String routeNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/buses", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getBusesForRouteAtStop(@RestQueryParam("StopNo") int stopNumber,
                                                                     @RestQueryParam("RouteNo") String routeNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/routes/{routeNumber}", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract Map<String, Object> getRoute(@RestUriParam("routeNumber") String routeNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/routes", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getRoutes(@RestQueryParam("StopNo") int stopNumber)
        throws IOException;

    @Processor
    @RestCall(uri = "http://api.translink.ca/RTTIAPI/V1/status/{service}", method = HttpMethod.GET, contentType = "application/json", exceptions = {@RestExceptionOn(expression = "#[message.inboundProperties['http.status'] != 200]")})
    public abstract List<Map<String, Object>> getStatuses(@RestUriParam("service") @Optional @Default("All") Service service)
        throws IOException;

    @TransformerResolver
    public static org.mule.api.transformer.Transformer transformerResolver(@SuppressWarnings("unused") final DataType<?> source,
                                                                           final DataType<?> result,
                                                                           final MuleContext muleContext)
        throws Exception
    {
        if ((result.getType() == Map.class) || (result.getType() == List.class))
        {
            final JsonToObject jsonToObject = new JsonToObject();
            jsonToObject.setReturnDataType(new SimpleDataType<Object>(Object.class));
            muleContext.getRegistry().applyProcessorsAndLifecycle(jsonToObject);
            return jsonToObject;
        }

        return null;
    }

    public String getAcceptJsonHeader()
    {
        return acceptJsonHeader;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    public void setApiKey(final String apiKey)
    {
        this.apiKey = apiKey;
    }
}
