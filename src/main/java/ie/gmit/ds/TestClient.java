package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;


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

    public void validateMethod(ByteString expectedHash,ByteString salt){


//        StreamObserver<BoolValue> response = new StreamObserver<BoolValue>() {
//            @Override
//            public void onNext(BoolValue value) {
//                System.out.println("ONNEXT VALUE: " + value.getValue());
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//            }
//
//            @Override
//            public void onCompleted() {
//
//            }
//        };


        String testPasword = "password";
        System.out.println("VALIDATE");
        ValidateRequest validateRequest = ValidateRequest.newBuilder().setHashedPassword(expectedHash).setSalt(salt).setPassword(testPasword).build();

        BoolValue isValid = syncUserService.validate(validateRequest);
        System.out.println(isValid.getValue());
        //asyncUserService.validate(validateRequest,response);


    }

    public void hashMethod(int userId, String password) {


        try {
            //logger.info("Requesting all items ");
            HashRequest hashRequest = HashRequest.newBuilder().setUserId(userId).setPassword(password).build();
            HashResponse response = syncUserService.hash(hashRequest);
            System.out.println(response.getHashedPassword().toByteArray());

            validateMethod(response.getHashedPassword(),response.getSalt());

            //logger.info("Returned from requesting all items ");
        } catch (StatusRuntimeException ex) {
            //logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
            return;
        }

    }

    public static void main(String[] args) throws Exception {
        TestClient client = new TestClient("localhost", 50551);
        Scanner console = new Scanner(System.in);  // Create a Scanner object
        int userID;
        String password;

        System.out.println("Enter UserID: ");
        userID = console.nextInt();
        System.out.println("Enter Password:");
        password = console.next();

        client.hashMethod(userID, password);

    }



}

