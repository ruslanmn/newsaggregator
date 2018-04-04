package server;

import documentprocessing.datastructures.ClusteringResult;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ClusteringWebService {
    @WebMethod
    ClusteringResult getClusters();
}
