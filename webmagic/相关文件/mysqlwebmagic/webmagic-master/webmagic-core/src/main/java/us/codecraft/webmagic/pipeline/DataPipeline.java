package us.codecraft.webmagic.pipeline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.SpiderModel;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.utils.ConUtil;

public class DataPipeline implements Pipeline{

	@Override
	public void process(ResultItems resultItems, Task task) {
		// TODO Auto-generated method stub
		
		ConUtil db = new ConUtil();
		
		Connection con = db.getCon();
		
		String sql = "insert into spider (title,content,date) values(?,?,?)";
		
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			
			SpiderModel sm = new SpiderModel();
			
			for(Map.Entry<String,Object> entry : resultItems.getAll().entrySet()){
				
				if(entry.getKey().equals("title")){
					
					sm.setTitle(entry.getValue().toString());
				}
				
				else if(entry.getKey().equals("content")){
					
					sm.setContent(entry.getValue().toString());
				}
				
				else{
					
					sm.setDate(entry.getValue().toString());
				}
				
				ps.setString(1, sm.getTitle());
				
				ps.setString(2, sm.getContent());
				
				ps.setString(3, sm.getDate());
				
				ps.executeUpdate();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
