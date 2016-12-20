package ttyy.com.datasdao.modules;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 *******************************
 * @文件名称:SerializeHelper.java
 * @文件作者:Administrator
 * @创建时间:2015年9月18日
 * @文件描述:序列化工具
 ******************************
 */
public class SerializeHelper {
	
	private SerializeHelper(){
		
	}
	
	/**
	 * 序列化
	 */
	public static byte[] serialize(Object obj){
		if(obj == null)
			return null;
		
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			
			baos.flush();
			baos.close();
			oos.flush();
			oos.close();

			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 反序列化
	 */
	public static <T> T reSerialize(byte[] bytes){
		if(bytes == null){
			return null;
		}
		
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			
			obj = ois.readObject();
			
			bis.close();
			ois.close();
			
			return (T)obj;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
