package ie.gmit.ds;

import com.google.protobuf.BoolValue;
import io.grpc.stub.StreamObserver;

public class PasswordServiceImpl extends PasswordServiceGrpc.PasswordServiceImplBase {

    @Override
    public void validate(ValidateRequest request, StreamObserver<BoolValue> responseObserver) {

    }

    @Override
    public void hash(HashRequest request, StreamObserver<HashResponse> responseObserver) {

        

    }
}
