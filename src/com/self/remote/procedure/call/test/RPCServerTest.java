package com.self.remote.procedure.call.test;

import com.self.remote.procedure.call.server.Server;
import com.self.remote.procedure.call.server.ServerCenter;
import com.self.remote.procedure.call.service.HelloService;
import com.self.remote.procedure.call.service.HelloServiceImpl;

public class RPCServerTest {

	public static void main(String[] args) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				// 服务中心
				Server server = new ServerCenter(9999);
				// 将HelloService接口及其实现类注册到服务中心
				server.register(HelloService.class, HelloServiceImpl.class);
				server.start();
			}

		}).start();

	}

}
