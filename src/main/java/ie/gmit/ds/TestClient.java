package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestClient {
    private static final Logger logger = Logger.getLogger(TestClient.class.getName());

    private final ManagedChannel channel;
    private final PasswordServiceGrpc.PasswordServiceStub asyncUserService;
    private final PasswordServiceGrpc.PasswordServiceBlockingStub syncUserService;

    public TestClient(String host, int port) {
        channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        syncUserService = PasswordServiceGrpc.newBlockingStub(channel);
        asyncUserService = PasswordServiceGrpc.newStub(channel);
    }


    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }


    private void validate(){

        StreamObserver<BoolValue> responseObserver = new StreamObserver<BoolValue>() {

            @Override
            public void onNext(BoolValue value) {
                if(value.getValue() == true){
                    System.out.println("Login Successful");
                }else{
                    System.out.println("Login Failed");
                }
                System.out.println("VALUE: " + value.getValue());
                // logger.info("PASSWORD VALID: " + value);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };

        boolean isValid = false;

        try {
            //logger.info("Requesting all items ");
//            ValidatePwd val = ValidatePwd.newBuilder().setUser(user).setPwd(password).build();
//            asyncUserService.validate(val,responseObserver);
            //logger.info("Returned from requesting all items ");


        } catch (StatusRuntimeException ex) {
            //logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());

        }



    }

    public void hash(HashRequest hashRequest) {

    }


    public static void main(String[] args) throws Exception {
        TestClient client = new TestClient("localhost", 50551);
        Scanner console = new Scanner(System.in);  // Create a Scanner object
        boolean exit = false;
        String userID;
        String password;

        System.out.println("");



        }



    }
}
