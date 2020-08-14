package mail;

import java.util.ArrayList;
import java.util.List;

public class TestMail {

    public static void main(String[] args) throws Exception {
        List<String> filepath = new ArrayList<String>();
        MailUtils.sendMail("mail.sender","123@qq.com","linjing@aaa.com.cn", "测试主题", "<b>测试内容</b>", filepath,null,null,0);
//        Map<String, Object> map = new HashMap<String, Object>();
//  	    map.put("content", "测试 内容");
//  	    String templatePath = "test.ftl";
//  	    filepath.add("d:/BugReport.txt");
//  	    sendFtlMail("zhangmeng@gjdf.com.cn", "sendemail test!",templatePath,filepath, map);
//        System.out.println(getFilePath());


    }

}
