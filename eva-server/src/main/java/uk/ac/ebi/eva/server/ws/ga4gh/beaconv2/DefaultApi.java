/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.8).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package uk.ac.ebi.eva.server.ws.ga4gh.beaconv2;

import uk.ac.ebi.eva.commons.beacon.models.Beacon;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-06-18T18:08:34.969Z[GMT]")
@Api(value = "default", description = "the default API")
public interface DefaultApi {

    @ApiOperation(value = "", nickname = "getBeacon", notes = "Get information about the beacon", response = Beacon.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Beacon.class) })
    @RequestMapping(value = "/",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Beacon> getBeacon();

}
