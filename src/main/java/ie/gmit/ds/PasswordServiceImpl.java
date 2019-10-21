package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

    @Override
    public void validate(ValidateRequest request, StreamObserver<BoolValue> responseObserver) {

    }

    @Override
    public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) {

        System.out.println("HERE");
        byte[] salt = Passwords.getNextSalt();

        byte[] hashedPassword = Passwords.hash(request.getPassword().toCharArray(),salt);
        ByteString saltByteString = ByteString.copyFrom(salt);
        ByteString hashedPasswordByteString = ByteString.copyFrom(hashedPassword);
        System.out.println(saltByteString);
        System.out.println(hashedPasswordByteString);

        HashResponse hashResponse = HashResponse.newBuilder().setUserId(request.getUserId()).setHashedPassword(hashedPasswordByteString).setSalt(saltByteString).build();

        responseObserver.onNext(hashResponse);
        responseObserver.onCompleted();
    }
}
