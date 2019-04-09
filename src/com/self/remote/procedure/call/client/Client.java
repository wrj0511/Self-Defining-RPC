package com.self.remote.procedure.call.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {
	/**
	 * 获取代表服务端接口的动态代理对象（HelloService）；serviceInterface:请求的接口名 ；addr：待请求服务端的ip:端口
	 * 
	 * @param serviceInterface
	 * @param addr
	 * @return
	 */
	public static Object getRemoteProxyObj(Class<?> serviceInterface, InetSocketAddress addr) {
		//
		/**
		 * newProxyInstance(a,b,c)； a：类加载器，需要代理哪个类（例如HelloService接口），就需要HelloService的
		 * ；b：需要代理的对象，具备哪些方法 ——接口 Java机制：单继承，多实现，A implements B接口，C接口
		 * 
		 */
		return Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class<?>[] { serviceInterface },
				new InvocationHandler() {

					// proxy：代理的对象；method：代理的方法；args：参数
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) {
						// 客户端向服务端发送请求，请求某一个具体的接口
						Socket socket = new Socket();
						ObjectOutputStream output = null;
						ObjectInputStream input = null;
						try {
							socket.connect(addr);
							output = new ObjectOutputStream(socket.getOutputStream());
							// 接口名、方法名：writeUTF
							output.writeUTF(serviceInterface.getName());
							output.writeUTF(method.getName());
							// 方法参数、方法参数的类型
							output.writeObject(method.getParameterTypes());
							output.writeObject(args);
							// 等待服务端处理后的返回值
							input = new ObjectInputStream(socket.getInputStream());

							return input.readObject();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return null;
						} finally {
							try {
								if (output != null) {
									output.close();
								}
								if (input != null) {
									input.close();
								}
								if (socket != null) {
									socket.close();
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				});
	}
}
