# coding=utf-8
import MySQLdb  
import codecs

#读取爬取到的文件，按照用户名判断是否在数据库中存在，并返回id
infofile = codecs.open("hottopic.txt", 'r', 'utf-8')
# infofilenew = codecs.open("infowrite.txt", 'w', 'utf-8')
username = infofile.readline().rstrip('\r\n')
conn = MySQLdb.connect (host = "localhost", user = "root", passwd = "zijidelu", db = "opensns681", use_unicode=True, charset="utf8")  
cursor = conn.cursor ()  
while username!="":
    username = username.encode("utf8")
    username = username.split(' ')
    givenname = username[0]
    #判断该用户是否已存在
    cursor.execute ("SELECT id from opensns_ucenter_member where username='"+givenname+"'")  
    rows = cursor.fetchall()  
    if len(rows)>0:
        #存在则返回该用户的id
        id = rows[0][0]
    elif len(rows)==0:
        #不存在则插入到数据库
        cursor.execute("insert into opensns_ucenter_member(username,password,email,mobile,reg_time,reg_ip,last_login_time,last_login_ip,update_time,status,type) values("+"'"+givenname+"', '8dc685f99ea58bd21ba9094b5d3ce377', '', '', 1453512703, 2130706433, 1453512703, 2130706433, 1453512703, 1, 1)")
        conn.commit()  
        cursor.execute("insert into opensns_member(nickname,sex,birthday,qq,login,reg_ip,reg_time,last_login_ip,last_login_time,status,last_login_role,show_role,signature,pos_province,pos_city,pos_district,pos_community,score1,score2,score3,score4,con_check,total_check) values("+"'"+givenname+"', 0, '0000-00-00', '', 3, 0, 1453427494, 2887392922, 1453539021, 1, 1, 1, '', 0, 0, 0, 0, 10, 0, 0, 0, 0, 0)")
        conn.commit()
        cursor.execute ("SELECT id from opensns_ucenter_member where username='"+givenname+"' order by id desc")
        row = cursor.fetchall()
        id = row[0][0]
#     print id
#     infofilenew.write(str(id)+" "+username[1]+" "+username[2]+" "+username[3])
    if(username[2]=="转发"):
        repostnum = "0"
    else:
        repostnum = username[2].replace("转发","")
    if(username[3]=="评论"):
        commentnum = "0"
    else:
        commentnum = username[3].replace("评论","")
    cursor.execute ("SELECT id from opensns_weibo where uid="+str(id)+" and create_time="+username[4])  
    rows = cursor.fetchall() 
    if len(rows)==0:
#         cursor.execute("insert into opensns_weibo(uid,content,create_time,comment_count,status,is_top,type,data,repost_count,`from`) values(11,'11发的微博', 1453513417,1, 1, 0, 'feed', 'a:0:{}',1,'')")
        cursor.execute("insert into opensns_weibo(uid,content,create_time,comment_count,status,is_top,type,data,repost_count,`from`) values("+str(id)+",'"+username[1]+"', "+username[4]+","+commentnum+", 1, 0, 'feed', 'a:0:{}'," +repostnum+",'')")
        conn.commit()
    username = infofile.readline().rstrip('\r\n')

infofile.close()
cursor.close ()  
conn.close ()  
