package ie.gmit.ds;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.net.BindException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;
import java.util.logging.Logger;

public class PasswordServiceServer {

    private Server grpcServer;
    private static final Logger logger = Logger.getLogger(PasswordServiceServer.class.getName());
    //public int PORT = 50551;

    //SERVER CODE

    private void start(int port) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        grpcServer = ServerBuilder.forPort(port)
                .addService(new PasswordServiceImpl())
                .build()
                .start();
        logger.info("Server started, listening on " + port);

    }

    // Try catch to Handle port already in use also  port must be within the range of 50000 and 65000
    public static void main(String[] args) throws IOException, InterruptedException, InvalidKeySpecException, NoSuchAlgorithmException {
        int port;
        boolean connected = false;
        final PasswordServiceServer passwordServer = new PasswordServiceServer();
        Scanner console = new Scanner(System.in);
        do {
            do {
                System.out.print("Enter Server's Port (50000 - 65000): ");
                port = console.nextInt();
                if (port < 50000 || port > 65000){
                    System.out.println("ERROR - Please enter a port within the range of 50000 - 65000....\n");
                }



            }while(port < 50000 || port > 65000);

            try {

                passwordServer.start(port);
                passwordServer.blockUntilShutdown();
                connected = true;
            }catch (Throwable e){
                System.out.println("Port already in use. Try again...");
            }
        }while(connected == false);


    }

    private void stop() {
        if (grpcServer != null) {
            grpcServer.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (grpcServer != null) {
            grpcServer.awaitTermination();
        }
    }

}
