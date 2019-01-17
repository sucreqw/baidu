package com.sucre.myNet;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

/**
 * 负责socket发�?�与接收.接收后的数据交出调用者自己处�?.
 * 
 * @author sucre
 *
 */
public class Nets {

	/**
	 * https数据包发�?.get/post通用.
	 * 
	 * @param host 服务器域�?
	 * @param port 服务器端�?
	 * @param data  要发送的数据
	 * @return 成功后返回服务器返回的数�?,不成功返回错误码.
	 */
	public String goPost(String host, int port, byte[] data) {
		StringBuilder ret = new StringBuilder(data.length);
		// 创建sslsocket工厂
		SocketFactory factory = SSLSocketFactory.getDefault();
		try (
				// 括号内的对象自动释放.
				// 创建socker对象
				Socket sslsocket = factory.createSocket(host, port);
				// 创建要写入的数据对象,直接用bufferedoutputstream 更灵�?.必要时可传文件之�?.
				BufferedOutputStream out = new BufferedOutputStream(sslsocket.getOutputStream());

		) {
			// 写入要发送的数据并刷�?!
			out.write(data);
			out.flush();
			// 接收数据,为了解决乱码的情�?,要用inputstreamreader,用bufferedreader 包装后会更高效些.
			BufferedReader in = new BufferedReader(new InputStreamReader(sslsocket.getInputStream(), "UTF-8"));
			// String str = null;
			char[] rev = new char[1024];
			int len = -1;
			while ((len = in.read(rev)) != -1) {
				ret.append(new String(rev, 0, len));
				// 由于socket会阻�?,当装不满缓冲区时,当作是结�?,
				if (len < 1024) {
					break;
				}
			}

			// 安全起见还是关闭�?下资�?.
			in.close();
			sslsocket.close();
			out.close();

		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println("网络错误：" + e.getMessage());
		}

		return ret.toString();
	}

	/**
	 * 普通数据包,非https
	 * 
	 * @param host
	 * @param port
	 * @param data
	 * @return
	 */
	public String GoHttp(String host, int port, byte[] data) {
		StringBuilder ret = new StringBuilder(data.length);
		try (
				// 括号内的对象自动释放.
				// 创建socker对象
				Socket socket = new Socket(host, port);
				// 创建要写入的数据对象,直接用bufferedoutputstream 更灵�?.必要时可传文件之�?.
				BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

		) {
			// 写入要发送的数据并刷�?!
			out.write(data);
			out.flush();
			// 接收数据,为了解决乱码的情�?,要用inputstreamreader,用bufferedreader 包装后会更高效些.
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			String str = null;
			char[] rev = new char[1024];
			int len = -1;
			while ((len = in.read(rev)) != -1) {
				ret.append(new String(rev, 0, len));
				// 由于socket会阻�?,当装不满缓冲区时,当作是结�?,
				if (len < 1024) {
					break;
				}
			}

			// 安全起见还是关闭�?下资�?.
			in.close();
			socket.close();
			out.close();

		} catch (Exception e) {
			System.err.println("http网络错误：" + e.getMessage());
		}

		return ret.toString();
	}

	/**
	 * http数据包返回文件
	 * 
	 * @param host
	 * @param port
	 * @param data
	 * @return
	 */
	public byte[] goPostByte(String host, int port, byte[] data) {
		// StringBuilder ret = new StringBuilder(data.length);
		byte[] ret = null;
		// 创建sslsocket工厂
		SocketFactory factory = SSLSocketFactory.getDefault();
		try (
				// 括号内的对象自动释放.
				// 创建socker对象
				Socket sslsocket = factory.createSocket(host, port);
				// 创建要写入的数据对象,直接用bufferedoutputstream 更灵�?.必要时可传文件之�?.
				BufferedOutputStream out = new BufferedOutputStream(sslsocket.getOutputStream());

		) {
			// 写入要发送的数据并刷�?!
			out.write(data);
			out.flush();
			// 接收数据,为了解决乱码的情�?,要用inputstreamreader,用bufferedreader 包装后会更高效些.
			// BufferedReader in = new BufferedReader(new
			// InputStreamReader(sslsocket.getInputStream(), "UTF-8"));
			// String str = null;
			InputStream in = new DataInputStream(sslsocket.getInputStream());
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			byte[] rev = new byte[1024];
			int len = -1;
			int start = 0;
			while ((len = in.read(rev)) != -1) {
				// ret.append(new String(rev, 0, len));
				// 由于socket会阻�?,当装不满缓冲区时,当作是结�?,
				start = serachB(rev, new byte[] { 13, 10, 13, 10 });
				start=start==-1?0:start+4;
				output.write(rev, start, len-start);
				
				/*if (len < 1024) {
					break;
				}*/
			}

			ret = output.toByteArray();
			// 安全起见还是关闭�?下资�?.
			in.close();
			sslsocket.close();
			out.close();

		} catch (Exception e) {
			// e.printStackTrace();
			System.err.println("网络错误：" + e.getMessage());
		}

		return ret;
	}

	/**
	 * byte形式的indexof
	 * @param source
	 * @param target
	 * @return
	 */
	private int serachB(byte[] source, byte[] target) {
		int ret = -1;
		for (int i = 0; i < source.length; i++) {
			if (source[i] == target[0]) {
				for (int j = 1; j < target.length; j++) {
					if (target[j] == source[i + j]) {
						ret = i;
					} else {
						ret = -1;
						break;
					}

				}
				if (ret != -1) {
					return ret;
				}
			}
		}
		return ret;
	}
}
