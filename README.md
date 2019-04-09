# Self-Defining-RPC
自定义RPC

<p>1 基本概念</p>

<p>1.1 定义<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;RPC：Remote Procedure Call，远程过程调用。</p>
<p>1.2 RPC功能目标<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;RPC 的主要功能目标是让构建分布式计算（应用）更容易，在提供强大的远程调用能力时不损失本地调用的语义简洁性。 <br/>
    &nbsp;&nbsp;&nbsp;&nbsp;为实现该目标，RPC 框架需提供一种透明调用机制让使用者不必显式的区分本地调用和远程调用。</p>

<p>2 RPC结构拆解<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RPC 服务方通过 RpcServer 去导出（export）远程接口方法，而客户方通过 RpcClient 去引入（import）远程接口方法。 客户方像调用本地方法一样去调用远程接口方法，RPC 框架提供接口的代理实现，实际的调用将委托给代理 RpcProxy 。 代理封装调用信息并将调用转交给 RpcInvoker 去实际执行。 在客户端的 RpcInvoker 通过连接器 RpcConnector 去维持与服务端的通道 RpcChannel， 并使用 RpcProtocol 执行协议编码（encode）并将编码后的请求消息通过通道发送给服务方。<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RPC 服务端接收器 RpcAcceptor 接收客户端的调用请求，同样使用 RpcProtocol 执行协议解码（decode）。 解码后的调用信息传递给 RpcProcessor 去控制处理调用过程，最后再委托调用给 RpcInvoker 去实际执行并返回调用结果。<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcServer	负责导出（export）远程接口<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcClient	负责导入（import）远程接口的代理实现<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcProxy	远程接口的代理实现<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcInvoker	客户方实现：负责编码调用信息和发送调用请求到服务方并等待调用结果返回。<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;服务方实现：负责调用服务端接口的具体实现并返回调用结<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcProtocol	负责协议编/解码<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcConnector	负责维持客户方和服务方的连接通道和发送数据到服务方<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcAcceptor	负责接收客户方请求并返回请求结果<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcProcessor	负责在服务方控制调用过程，包括管理调用线程池、超时时间等<br/>
&nbsp;&nbsp;&nbsp;&nbsp;RpcChannel	数据传输通道

</p>
 



<p>3 RPC流程<br/>
&nbsp;&nbsp;&nbsp;&nbsp;（1）	客户端：通过socket请求服务端，并且通过字符串形式将需要请求的接口发送给服务端。<br/>
&nbsp;&nbsp;&nbsp;&nbsp;（2）	服务端：将可以提供的接口注册到服务中心（通过map保存key（接口的名字），value（接口的实现类））。<br/>
&nbsp;&nbsp;&nbsp;&nbsp;（3）	服务端接收到客户端的请求后，通过请求的接口名，在服务中心的map中寻求对应的接口实现类，找到后<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	解析刚才客户端发送来的接口名、方法名，<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	解析完毕后通过反射技术将该方法执行，<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;	执行完毕后再将该方法的返回值返回给客户端。</p>





参考资料：
1、腾讯课堂：https://ke.qq.com/course/296283#term_id=100351080
