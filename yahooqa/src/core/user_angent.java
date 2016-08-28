package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class user_angent {
	
	//随机产生user_Agent
	 public static int random(int min,int max){
	        Random random = new Random();
	        int s = random.nextInt(max)%(max-min+1) + min;
	        return s;
	    }
	    public static String getRandomAgent() throws IOException{
	    	FileReader fr=new FileReader(new File("data/user_agents"));
	    	BufferedReader br=new BufferedReader(fr);
	    	String line=br.readLine();
	    	ArrayList<String> agents=new ArrayList<String>();
	    	while(line!=null){
	    		agents.add(line);
	    		line=br.readLine();
	    	}
	    	int num=agents.size();
	    	int indx=random(0,num);
	    	return agents.get(indx);
	    }
	    
	    public static String getRandomIP() throws IOException{
	    	FileReader fr=new FileReader(new File("data/ip.txt"));
	    	BufferedReader br=new BufferedReader(fr);
	    	String line=br.readLine();
	    	ArrayList<String> agents=new ArrayList<String>();
	    	while(line!=null){
	    		agents.add(line);
	    		line=br.readLine();
	    	}
	    	int num=agents.size();
	    	int indx=random(0,num);
	    	return agents.get(indx);
	    }

	public static void main(String[] args) throws IOException {
		user_angent sA= new user_angent();
		System.out.println(sA.getRandomIP());

	}

}
