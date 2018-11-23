# nettyrpc
netty client for protobuf transfer
# how to use
refer to test, there are two ways to use it.
# for spring
<bean id="nettyRpcBootstrap" class="nettyrpc.protobuf.client.Bootstrap" destroy-method="stop" init-method="startDefault">
