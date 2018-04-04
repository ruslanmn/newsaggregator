package server;

import documentprocessing.datastructures.ClusteringResult;

import javax.jws.WebService;

@WebService(endpointInterface = "server.ClusteringWebService")
public class ClusteringWebServiceImpl implements ClusteringWebService {

    public ClusteringResult clusteringResult;

    @Override
    public ClusteringResult getClusters() {
        return clusteringResult;
    }
}
