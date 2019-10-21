package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

    @Override
    public void validate(ValidateRequest request, StreamObserver<BoolValue> responseObserver) {
        char[] password = request.getPassword().toCharArray();
        byte[] salt = request.getSalt().toByteArray();
        byte[] expectedPassword = request.getHashedPassword().toByteArray();

        boolean validPassword = Passwords.isExpectedPassword(password,salt,expectedPassword);

        if(validPassword == true){
            responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
            responseObserver.onCompleted();
        }else{
            responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
            responseObserver.onCompleted();
        }

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
