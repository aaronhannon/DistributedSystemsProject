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
        System.out.println("VALIDATE METHOD");


        //char[] expectedPasswordChar = new char[expectedPassword.length];

//        for (int i = 0; i < expectedPassword.length; i++) {
//            expectedPasswordChar[i] = (char) expectedPassword[i];
//        }
//
//        for (char c:password) {
//            System.out.print(c);
//        }
//
//        System.out.println();
//
//
//        for (char c:expectedPasswordChar) {
//            System.out.print(c);
//        }



        boolean validPassword = Passwords.isExpectedPassword(request.getPassword(),request.getSalt(),request.getHashedPassword());

        System.out.println("\nvalidation: " + validPassword);

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


        try {
            System.out.println("HERE");
            byte[] salt = Passwords.getNextSalt();

            byte[] hashedPassword = Passwords.hash(request.getPassword().toCharArray(),salt);
            ByteString saltByteString = ByteString.copyFrom(salt);
            ByteString hashedPasswordByteString = ByteString.copyFrom(hashedPassword);
            System.out.println(saltByteString);
            System.out.println(hashedPasswordByteString);

            // HashResponse hashResponse = HashResponse.newBuilder().setUserId(request.getUserId()).setHashedPassword(hashedPasswordByteString).setSalt(saltByteString).build();


            responseObserver.onNext(HashResponse.newBuilder().setUserId(request.getUserId()).setHashedPassword(hashedPasswordByteString).setSalt(saltByteString).build());
            responseObserver.onCompleted();
        }catch (Exception e){

        }

    }
}
