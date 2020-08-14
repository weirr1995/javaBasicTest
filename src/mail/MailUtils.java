package mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class MailUtils {
	private  static  String tryCountWhenError = "3";

    public static boolean sendMail(String fromProp , String to, String copyto, String subject, String body,
                                   List<String> filepath, Map<String,Object> businData, Map<String, Object> map,int errorCount) throws UnsupportedEncodingException,Exception {
    	boolean sendresult = false;
		String host = "mail.aaa.com.cn";
		String use_from = "aaaa@gjdf.com.cn";
    	String use_username = "aaaa";
    	String use_password = "111111";
    	try{
    		System.out.println("发件人："+use_from+"....");

			// 创建Properties对象
			Properties props = System.getProperties();
			// 创建信件服务器
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true"); // 通过验证
			//props.put("mail.debug", "true"); //调试时启用这句
			// 得到默认的对话对象
			Session session = Session.getDefaultInstance(props, null);
			// 创建一个消息，并初始化该消息的各项元素
			MimeMessage msg = new MimeMessage(session);
			//        nick = MimeUtility.encodeText(nick);
			//        msg.setFrom(new InternetAddress(nick + "<" + from + ">"));
			msg.setFrom(new InternetAddress(use_from));
			// 创建收件人列表
			System.out.println("收件人:"+to+".....");
			if (to != null && to.trim().length() > 0) {
				String[] arr = to.split(",");
				int receiverCount = arr.length;
				if (receiverCount > 0) {
					InternetAddress[] address = new InternetAddress[receiverCount];
					for (int i = 0; i < receiverCount; i++) {
						//                	不满足邮件格式的收件人不加
						address[i] = new InternetAddress(arr[i]);
					}
					msg.addRecipients(Message.RecipientType.TO, address);

					if (!StringUtils.isEmpty(copyto)) {
						String[] arrCopy = copyto.split(",");
						InternetAddress[] addressCopy = new InternetAddress[arrCopy.length];
						for (int i = 0; i < arrCopy.length; i++) {
							System.out.println("抄送人："+arrCopy[i]);
							//                	不满足邮件格式的收件人不加
							addressCopy[i] = new InternetAddress(arrCopy[i]);
						}
						msg.addRecipients(Message.RecipientType.CC, addressCopy);
					}
					msg.setSubject(subject);
					// 后面的BodyPart将加入到此处创建的Multipart中
					Multipart mp = new MimeMultipart();
					// 创建一个包含HTML内容的MimeBodyPart
					BodyPart html = new MimeBodyPart();
					html.setContent(body, "text/html;charset=gbk");
					mp.addBodyPart(html);
					// 附件操作
					if (filepath != null && filepath.size() > 0) {
						for (String filename : filepath) {
							MimeBodyPart mbp = new MimeBodyPart();
							// 得到数据源
							FileDataSource fds = new FileDataSource(filename);
							// 得到附件本身并至入BodyPart
							mbp.setDataHandler(new DataHandler(fds));
							// 得到文件名同样至入BodyPart
							//MimeUtility.encodeText()自动适应编码
							mbp.setFileName(MimeUtility.encodeText(fds.getName()));
							mp.addBodyPart(mbp);
						}
						// 移走集合中的所有元素
						//filepath.clear();
					}

					//设置图片
					if (!StringUtils.isEmpty(map)) {
						for( String key : map.keySet()){
							mp.addBodyPart((BodyPart) map.get(key));
						}
					}

					msg.setContent(mp);
					// 设置信件头的发送日期
					msg.setSentDate(new Date());
					msg.saveChanges();
					// 发送信件
					Transport transport = session.getTransport("smtp");
					transport.connect(host, use_username, use_password);
					transport.sendMessage(msg,msg.getRecipients(Message.RecipientType.TO));
					if (!StringUtils.isEmpty(copyto)) {
						transport.sendMessage(msg,msg.getRecipients(Message.RecipientType.CC));
					}
					transport.close();
					sendresult = true;
					saveSenderResult( use_from , to, subject, body, filepath,businData,sendresult,"");
				} else {
					System.out.println("None receiver!");
					sendresult = false;
					saveSenderResult( use_from , to, subject, body, filepath,businData,sendresult,"无收件人");
					return false;
				}
			} else {
				System.out.println("None receiver!");
				sendresult = false;
				saveSenderResult( use_from , to, subject, body, filepath,businData,sendresult,"无收件人");
				return false;
			}
		}catch(AddressException e){
			try {
				sendresult = false;
				String msg = "收件人地址错误!"+e.getMessage();
				saveSenderResult( use_from , to, subject, body, filepath,businData,sendresult,msg);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw e ;
		}catch(MessagingException e){
			//重发机制start
			errorCount++;
			if(errorCount <=Integer.parseInt(tryCountWhenError)){
				Thread.sleep(1000);//间隔1s
				log.error("邮件:"+subject+"发送失败，重试发送第"+errorCount+"次; 收件人:"+to);
				sendMail( fromProp , to, copyto, subject, body,filepath, businData, map,errorCount);
			}else{
				sendresult = false;
				String msg = "邮件无法发送错误!已尝试发送"+tryCountWhenError+"次"+e.getMessage();
				saveSenderResult( use_from , to, subject, body, filepath,businData,sendresult,msg);
			}
			//重发机制end
		}catch(UnsupportedEncodingException e){
			try {
				sendresult = false;
				String msg = "不支持编码异常!"+e.getMessage();
				saveSenderResult( use_from , to, subject, body, filepath,businData,sendresult,msg);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw e ;
		}catch(Exception e){
			try {
				sendresult = false;
				String msg = "邮件发送异常!"+e.getMessage();
				saveSenderResult( use_from , to, subject, body, filepath,businData,sendresult,msg);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			throw e ;
		}
		return true;
    }


	public static void saveSenderResult(String from ,String to,String subject,String body,
										List<String> filepath,Map<String,Object> businData,Boolean sendresult,String msg) throws Exception{

			log.info("from:{},to:{},subject:{},body:{},filepath:{},businData:{},sendresult:{},msg:{}",from,to,subject,body,filepath,businData,sendresult,msg);

	}
}

