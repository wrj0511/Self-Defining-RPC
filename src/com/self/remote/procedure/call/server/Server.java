package com.self.remote.procedure.call.server;

/**
 * 服务中心
 * 
 * @author jerry
 *
 */
public interface Server {

	/**
	 * 启动服务中心
	 */
	public void start();

	/**
	 * 关闭服务中心
	 */
	public void stop();

	/**
	 * 注册可访问的接口
	 * 
	 * @param service
	 * @param serviceImpl
	 */
	public void register(Class<?> service, Class<?> serviceImpl);

}
