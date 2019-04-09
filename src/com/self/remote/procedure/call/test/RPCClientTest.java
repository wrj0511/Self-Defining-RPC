package com.self.remote.procedure.call.test;

import java.net.InetSocketAddress;

import com.self.remote.procedure.call.client.Client;
import com.self.remote.procedure.call.service.HelloService;

public class RPCClientTest {
	public static void main(String[] args) throws ClassNotFoundException {
		HelloService service = (HelloService) Client.getRemoteProxyObj(
				Class.forName("com.self.remote.procedure.call.service.HelloService"),
				new InetSocketAddress("127.0.0.1", 9999));
		System.out.println(service.sayHello("wang"));
	}
}
