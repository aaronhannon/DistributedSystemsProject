package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Arrays;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

    @Override
    public void validate(ValidateRequest request, StreamObserver<BoolValue> responseObserver) {
        try {
            System.out.println("VALIDATE METHOD:");

            boolean validPassword = Passwords.isExpectedPassword(request.getPassword(),request.getSalt(),request.getHashedPassword());

            System.out.println("\tValidation: " + validPassword + "\n");

            if(validPassword == true){
                responseObserver.onNext(BoolValue.newBuilder().setValue(true).build());
                responseObserver.onCompleted();
            }else{
                responseObserver.onNext(BoolValue.newBuilder().setValue(false).build());
                responseObserver.onCompleted();
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) {

        try {
            System.out.println("HASH METHOD:");
            byte[] salt = Passwords.getNextSalt();
            byte[] hashedPassword = Passwords.hash(request.getPassword().toCharArray(),salt);

            ByteString saltByteString = ByteString.copyFrom(salt);
            ByteString hashedPasswordByteString = ByteString.copyFrom(hashedPassword);

            System.out.println("\tSalt: " + saltByteString);
            System.out.println("\tHashed Password: " + hashedPasswordByteString + "\n");

            responseObserver.onNext(HashResponse.newBuilder().setUserId(request.getUserId()).setHashedPassword(hashedPasswordByteString).setSalt(saltByteString).build());
            responseObserver.onCompleted();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
