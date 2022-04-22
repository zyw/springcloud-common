package cn.v5cn.rpc.grpc;

import io.grpc.stub.StreamObserver;

/**
 * @author ZYW
 * @version 1.0
 * @date 2020-03-05 22:14
 */
public class HelloServiceImpl extends HelloProtoGrpc.HelloProtoImplBase {
    @Override
    public void say(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + request.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
