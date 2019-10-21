package ie.gmit.ds;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Logger;

public class PasswordServiceServer {

    private Server grpcServer;
    private static final Logger logger = Logger.getLogger(PasswordServiceServer.class.getName());
    private static final int PORT = 50551;



    private void start() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        grpcServer = ServerBuilder.forPort(PORT)
                .addService(new PasswordServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + PORT);

    }

    public static void main(String[] args) throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
        final PasswordServiceServer passwordServer = new PasswordServiceServer();
        passwordServer.start();
        passwordServer.blockUntilShutdown();
    }

    private void stop() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (grpcServer != null) {
            grpcServer.awaitTermination();
        }
    }

}
