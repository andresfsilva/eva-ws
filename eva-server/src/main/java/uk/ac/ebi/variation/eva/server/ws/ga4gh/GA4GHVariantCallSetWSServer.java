package uk.ac.ebi.variation.eva.server.ws.ga4gh;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.opencb.biodata.ga4gh.GACallSet;
import org.opencb.biodata.ga4gh.GASearchCallSetsRequest;
import org.opencb.biodata.ga4gh.GASearchCallSetsResponse;
import org.opencb.biodata.ga4gh.GASearchVariantSetsResponse;
import org.opencb.biodata.models.variant.VariantSource;
import org.opencb.biodata.models.variant.ga4gh.GACallSetFactory;
import org.opencb.datastore.core.QueryResult;
import org.opencb.opencga.lib.auth.IllegalOpenCGACredentialsException;
import org.opencb.opencga.storage.variant.VariantSourceDBAdaptor;
import org.opencb.opencga.storage.variant.mongodb.VariantSourceMongoDBAdaptor;
import uk.ac.ebi.variation.eva.server.ws.EvaWSServer;

/**
 *
 * @author Cristina Yenyxe Gonzalez Garcia <cyenyxe@ebi.ac.uk>
 */
@Path("/{version}/ga4gh/callsets")
@Produces(MediaType.APPLICATION_JSON)
public class GA4GHVariantCallSetWSServer extends EvaWSServer {
    
    private VariantSourceDBAdaptor dbAdaptor;

    public GA4GHVariantCallSetWSServer() throws IllegalOpenCGACredentialsException {
        super();
    }

    public GA4GHVariantCallSetWSServer(@DefaultValue("") @PathParam("version")String version, @Context UriInfo uriInfo, @Context HttpServletRequest hsr) 
            throws IOException, IllegalOpenCGACredentialsException {
        super(version, uriInfo, hsr);
        dbAdaptor = new VariantSourceMongoDBAdaptor(credentials);
    }

    
    @GET
    @Path("/search")
    /**
     * 
     * @see http://ga4gh.org/documentation/api/v0.5/ga4gh_api.html#/schema/org.ga4gh.GASearchCallSetsRequest
     */
    public Response getCallSets(@QueryParam("variantSetIds") String files,
                                @QueryParam("pageToken") String pageToken,
                                @DefaultValue("10") @QueryParam("pageSize") int limit,
                                @DefaultValue("false") @QueryParam("histogram") boolean histogram,
                                @DefaultValue("-1") @QueryParam("histogram_interval") int interval) {
        int idxCurrentPage = 0;
        if (pageToken != null && !pageToken.isEmpty() && StringUtils.isNumeric(pageToken)) {
            idxCurrentPage = Integer.parseInt(pageToken);
            queryOptions.put("skip", idxCurrentPage * limit);
        }
        queryOptions.put("limit", limit);
        
        List<String> filesList = Arrays.asList(files.split(","));
        QueryResult<List<String>> qr;
        if (filesList.isEmpty()) {
            // TODO This should accept a global search for all call sets (samples) in the DB
            return createErrorResponse("Please provide at least one variant set to search for");
        } else {
            qr = dbAdaptor.getSamplesBySources(filesList, queryOptions);
        }
        
        // Convert sample names objects to GACallSet
        List<GACallSet> gaCallSets = GACallSetFactory.create(filesList, qr.getResult());
        // Calculate the next page token
        int idxLastElement = idxCurrentPage * limit + limit;
        String nextPageToken = (idxLastElement < qr.getNumTotalResults()) ? String.valueOf(idxCurrentPage + 1) : null;

        // Create the custom response for the GA4GH API
        return createJsonResponse(new GASearchCallSetsResponse(gaCallSets, nextPageToken));
    }
    
    
    @POST
    @Path("/search")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getCallSets(GASearchCallSetsRequest request) {
        return getCallSets(StringUtils.join(request.getVariantSetIds(), ","), request.getPageToken(), request.getPageSize(), false, -1);
    }
    
}