package com.self.remote.procedure.call.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerCenter implements Server {

	// map：服务端的所有可供客户端访问的接口，都注册到该map中
	// key：接口的名字“HelloService”
	// value：真正的HelloService实现
	private static HashMap<String, Class<?>> serviceRegister = new HashMap<String, Class<?>>();
	private int port;
	private static boolean isRunning = false;

	// 连接池：连接池中存在多个连接对象，每个连接对象都可以处理一个客户请求
	private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	public ServerCenter() {
	}

	public ServerCenter(int port) {
		this.port = port;
	}

	public void start() {
		ServerSocket server = null;
		try {
			server = new ServerSocket();
			server.bind(new InetSocketAddress(port));
			while (true) {
				// 具体的服务内容：接收客户端请求，处理请求，并返回工作
				System.out.println("Start service......");
				// 如果想让多个客户端请求并发执行-->多线程
				Socket socket = null;
				try {
					socket = server.accept();// 等待客户端连接
					isRunning = true;
					executor.execute(new ServiceTask(socket));// 启动线程处理客户请求
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
//		finally {
//			try {
//				if (server != null) {
//					server.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	public void stop() {
		isRunning = false;
		System.out.println("Stop service ...");
		executor.shutdown();
	}

	public void register(Class<?> service, Class<?> serviceImpl) {
		serviceRegister.put(service.getName(), serviceImpl);
	}

	public static class ServiceTask implements Runnable {
		private Socket socket;

		public ServiceTask() {
		}

		public ServiceTask(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			ObjectInputStream input = null;
			ObjectOutputStream output = null;
			try {

				// 接收到客户端连接及请求，处理该请求...
				input = new ObjectInputStream(socket.getInputStream());
				// 因为ObjectInputStream对发送数据的顺序严格要求，因此需要参照发送的顺序逐个接收
				String serviceName = input.readUTF();
				String methodName = input.readUTF();
				Class<?>[] parameterTypes = (Class<?>[]) input.readObject();// 方法的参数类型String、int、boolean
				Object[] arguments = (Object[]) input.readObject(); // 方法的参数名

				// 根据客户请求，到map中找到与之对应的具体接口
				Class<?> ServiceClass = serviceRegister.get(serviceName); // HelloService
				Method method = ServiceClass.getMethod(methodName, parameterTypes);

				// 执行该方法
				Object result = method.invoke(ServiceClass.newInstance(), arguments);

				// 将发放执行完毕的返回值传给客户端
				output = new ObjectOutputStream(socket.getOutputStream());
				output.writeObject(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					if (output != null) {
						output.close();
					}
					if (input != null) {
						input.close();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}
	}

}
