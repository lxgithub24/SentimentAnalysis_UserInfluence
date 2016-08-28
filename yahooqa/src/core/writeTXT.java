package core;

import java.io.FileWriter;
import java.io.IOException;

public class writeTXT {


	public static void writeInfo(String info)
	{
		 try {
			FileWriter write = new FileWriter("data/bestanswer.txt",true);
			write.append(info);
			write.write("\n");
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("写入失败");
			e.printStackTrace();
		}
			
	}
	
	//候选答案
	public static void writespace(String info)
	{
		 try {
			FileWriter write = new FileWriter("data/usersapce.txt",true);
			write.append(info);
			write.write("\n");
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("写入失败");
			e.printStackTrace();
		}
			
	}
	
	//用户连接
	public static void writeuser(String info)
	{
		 try {
			FileWriter write = new FileWriter("data/ll.txt",true);
			write.append(info);
			write.write("\n");
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("写入失败");
			e.printStackTrace();
		}
			
	}
	
	public static void writeuserinfo(String userInfo)
	{
		 try {
			FileWriter write = new FileWriter("data/userinfo.txt",true);
			write.append(userInfo);
			write.write("\n");
			write.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("写入失败");
			e.printStackTrace();
		}
			
	}
		
		
		
	
	
	public static void main(String[] args) {
	
		writeTXT txt= new writeTXT();
		txt.writeInfo("abcd");
		txt.writeInfo("cdef");

	}

}
