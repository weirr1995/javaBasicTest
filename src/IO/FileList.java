package IO;

/**
 * @author linjing
 * @date: Created in 2020/7/14
 */
import java.io.File;

public class FileList {
    public static void main(String[] args) throws Exception {
        String dirPath = "E:\\Factory3\\workdiary\\trunk\\docs\\20200714发布包\\9_init_data";
        File dir = new File(dirPath);
        if(!dir.exists()){
            throw new Exception("文件目录"+dirPath+"不存在");
        }
        if(!dir.isDirectory()){
            throw new Exception("文件目录"+dirPath+"不是路径");

        }

        File[] files=dir.listFiles();
        for(int i=0;i<files.length;i++){
            File file =files[i];
            System.out.println("@D:\\backup\\20200714\\"+file.getName());
        }
    }
}