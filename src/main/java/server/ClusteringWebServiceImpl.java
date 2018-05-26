package server;

import documentprocessing.datastructures.ClusteringResult;

import javax.jws.WebService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@WebService(endpointInterface = "server.ClusteringWebService")
public class ClusteringWebServiceImpl implements ClusteringWebService {

    public ClusteringWebServiceImpl() {
        lock = new ReentrantLock();
    }

    public ClusteringResult clusteringResult;
    private Lock lock;

    public void setClusteringResult(ClusteringResult clusteringResult) {
        lock.lock();
        this.clusteringResult = clusteringResult;
        lock.unlock();
    }

    @Override
    public ClusteringResult getClusters() {
        lock.lock();
        ClusteringResult result = clusteringResult;
        lock.unlock();

        return result;
    }

    public static void main(String[] args) {

        ClusteringWebServiceImpl c = new ClusteringWebServiceImpl();

        Thread tSet = new Thread(new Runnable() {
            @Override
            public void run() {
                c.setClusteringResult(null);
            }
        });

        Thread tGet = new Thread(new Runnable() {
            @Override
            public void run() {
                c.getClusters();
            }
        });

        tSet.start();
        tGet.start();
    }

}
