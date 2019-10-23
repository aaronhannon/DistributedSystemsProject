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

    public void validateMethod(){


        StreamObserver<BoolValue> response = new StreamObserver<BoolValue>() {
            @Override
            public void onNext(BoolValue value) {
                System.out.println("LOGIN VALUE: " + value.getValue());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {
                System.exit(0);
            }
        };



        System.out.println("VALIDATE");

        try{
            asyncUserService.validate(ValidateRequest.newBuilder().setHashedPassword(expectedHash).setSalt(salt).setPassword(testPassword).build(),response);
            TimeUnit.SECONDS.sleep(1);
        }catch (StatusRuntimeException | InterruptedException ex) {
            //logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
            return;
        }
        return;



    }

    public String testPassword = " ";
    public ByteString expectedHash;
    public ByteString salt;

    public void hashMethod(int userId, String password) {


        StreamObserver<HashResponse> response = new StreamObserver<HashResponse>() {
            @Override
            public void onNext(HashResponse value) {

                System.out.println(value.getUserId());
                salt = value.getSalt();
                expectedHash = value.getHashedPassword();


                //System.out.println(value.getUserId());
                //validateMethod(value.getHashedPassword(),value.getSalt());
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onCompleted() {

            }
        };

        try {
            logger.info("Requesting all items ");
            HashRequest hashRequest = HashRequest.newBuilder().setUserId(userId).setPassword(password).build();
            asyncUserService.hash(hashRequest,response);
            TimeUnit.SECONDS.sleep(1);
//            HashResponse responseHashed = syncUserService.hash(hashRequest);
//            System.out.println(responseHashed.getHashedPassword().toByteArray());



            //logger.info("Returned from requesting all items ");
        } catch (StatusRuntimeException | InterruptedException e) {
            //logger.log(Level.WARNING, "RPC failed: {0}", ex.getStatus());
            return;
        }
        return;
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
        System.out.println("Enter Test Password:");
        client.testPassword = console.next();

        ByteString bPassword = ByteString.copyFrom(password.getBytes());

        try {
            client.hashMethod(userID, password);
            client.validateMethod();
        } finally {
            // Don't stop process, keep alive to receive async response
            Thread.currentThread().join();
        }


        System.out.println("After Hash Method");


    }



}

