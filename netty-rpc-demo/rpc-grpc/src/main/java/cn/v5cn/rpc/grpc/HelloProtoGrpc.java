package cn.v5cn.rpc.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.27.2)",
    comments = "Source: grpc.proto")
public final class HelloProtoGrpc {

  private HelloProtoGrpc() {}

  public static final String SERVICE_NAME = "hello.HelloProto";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<HelloRequest,
      HelloReply> getSayMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Say",
      requestType = HelloRequest.class,
      responseType = HelloReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<HelloRequest,
      HelloReply> getSayMethod() {
    io.grpc.MethodDescriptor<HelloRequest, HelloReply> getSayMethod;
    if ((getSayMethod = HelloProtoGrpc.getSayMethod) == null) {
      synchronized (HelloProtoGrpc.class) {
        if ((getSayMethod = HelloProtoGrpc.getSayMethod) == null) {
          HelloProtoGrpc.getSayMethod = getSayMethod =
              io.grpc.MethodDescriptor.<HelloRequest, HelloReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Say"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  HelloReply.getDefaultInstance()))
              .setSchemaDescriptor(new HelloProtoMethodDescriptorSupplier("Say"))
              .build();
        }
      }
    }
    return getSayMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HelloProtoStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HelloProtoStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HelloProtoStub>() {
        @Override
        public HelloProtoStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HelloProtoStub(channel, callOptions);
        }
      };
    return HelloProtoStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HelloProtoBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HelloProtoBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HelloProtoBlockingStub>() {
        @Override
        public HelloProtoBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HelloProtoBlockingStub(channel, callOptions);
        }
      };
    return HelloProtoBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HelloProtoFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HelloProtoFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HelloProtoFutureStub>() {
        @Override
        public HelloProtoFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HelloProtoFutureStub(channel, callOptions);
        }
      };
    return HelloProtoFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class HelloProtoImplBase implements io.grpc.BindableService {

    /**
     */
    public void say(HelloRequest request,
                    io.grpc.stub.StreamObserver<HelloReply> responseObserver) {
      asyncUnimplementedUnaryCall(getSayMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSayMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                HelloRequest,
                HelloReply>(
                  this, METHODID_SAY)))
          .build();
    }
  }

  /**
   */
  public static final class HelloProtoStub extends io.grpc.stub.AbstractAsyncStub<HelloProtoStub> {
    private HelloProtoStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected HelloProtoStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HelloProtoStub(channel, callOptions);
    }

    /**
     */
    public void say(HelloRequest request,
                    io.grpc.stub.StreamObserver<HelloReply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSayMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class HelloProtoBlockingStub extends io.grpc.stub.AbstractBlockingStub<HelloProtoBlockingStub> {
    private HelloProtoBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected HelloProtoBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HelloProtoBlockingStub(channel, callOptions);
    }

    /**
     */
    public HelloReply say(HelloRequest request) {
      return blockingUnaryCall(
          getChannel(), getSayMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HelloProtoFutureStub extends io.grpc.stub.AbstractFutureStub<HelloProtoFutureStub> {
    private HelloProtoFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected HelloProtoFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HelloProtoFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<HelloReply> say(
        HelloRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSayMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SAY = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HelloProtoImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HelloProtoImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SAY:
          serviceImpl.say((HelloRequest) request,
              (io.grpc.stub.StreamObserver<HelloReply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class HelloProtoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HelloProtoBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return HelloService.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HelloProto");
    }
  }

  private static final class HelloProtoFileDescriptorSupplier
      extends HelloProtoBaseDescriptorSupplier {
    HelloProtoFileDescriptorSupplier() {}
  }

  private static final class HelloProtoMethodDescriptorSupplier
      extends HelloProtoBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HelloProtoMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HelloProtoGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HelloProtoFileDescriptorSupplier())
              .addMethod(getSayMethod())
              .build();
        }
      }
    }
    return result;
  }
}
