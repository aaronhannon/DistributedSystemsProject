package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

//Client class handles the connection to the server
public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private final ManagedChannel channel;
    private final PasswordServiceGrpc.PasswordServiceStub asyncUserService;
    private final PasswordServiceGrpc.PasswordServiceBlockingStub syncUserService;

    public Client(String host, int port) {
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

    private ByteString expectedHash;
    private ByteString salt;

    public ByteString getSalt(){
        return salt;
    }
    public ByteString getExpectedHash(){
        return expectedHash;
    }

    // Calls the Server asynchronously to hash a password. It sets expectedHash and salt so that they can be retrieved from the ApiResource
    public void hashMethod(int userId, String password) {

        StreamObserver<HashResponse> response = new StreamObserver<HashResponse>() {
            @Override
            public void onNext(HashResponse value) {

                System.out.println(value.getUserId());
                salt = value.getSalt();
                expectedHash = value.getHashedPassword();

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
            asyncUserService.hash(hashRequest, response);
            TimeUnit.SECONDS.sleep(5);
        } catch (StatusRuntimeException | InterruptedException e) {
            return;
        }
        return;
    }

    // Calls the server synchronously to validate the password
    public boolean validateMethod(ByteString userHash, ByteString userSalt,String userPassword) {


        try {
            BoolValue value  = syncUserService.validate(ValidateRequest.newBuilder().setHashedPassword(userHash).setSalt(userSalt).setPassword(userPassword).build());

            return value.getValue();
        } catch (StatusRuntimeException e) {

            return false;
        }


    }
}
